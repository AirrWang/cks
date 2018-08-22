package com.ckjs.ck.Android;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.ThirdLoginBean;
import com.ckjs.ck.Bean.UserInfoBean;
import com.ckjs.ck.Manager.NotifyPostUserInforMessageManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Service.StepService;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RememberStep.StepDcretor;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ThirdUserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button next;


    private EditText et_height;
    private EditText et_weight;

    private Toolbar toolbar;
    private TextView et_age;
    private RadioButton info_man;
    private RadioButton info_woman;
    private int sex = 1;
    private ThirdLoginBean thirdloginBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_info);
        initId();
        initGetUser();
        initTest();
        initToolbar();

    }

    /**
     * 接受登录页面传过来的登录信息
     */
    private void initGetUser() {
        Intent intent = this.getIntent();
        thirdloginBean = (ThirdLoginBean) intent.getSerializableExtra("user");
    }

    /**
     * 标题栏
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(getResources().getText(R.string.user_info_activity));
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }


    }

    /**
     * 各个点击事件的监听注册
     */
    private void initTest() {
        next.setOnClickListener(this);
        info_man.setOnClickListener(this);
        info_woman.setOnClickListener(this);
    }

    /**
     * 控件id绑定
     */
    private void initId() {
        next = (Button) findViewById(R.id.info_next);

        et_age = (TextView) findViewById(R.id.et_age);
        et_age.setOnClickListener(this);
        et_height = (EditText) findViewById(R.id.et_height);
        et_weight = (EditText) findViewById(R.id.et_weight);
        info_man = (RadioButton) findViewById(R.id.info_man);
        info_woman = (RadioButton) findViewById(R.id.info_woman);
        dialog = new ProgressDialog(this);

    }

    /**
     * 各个点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_next:
                toNext();

                break;
            case R.id.info_man:
                sex = 1;

                break;
            case R.id.info_woman:
                sex = 2;

                break;
            case R.id.et_age:

                showpicker();

                break;
        }

    }

    /**
     * 选择 出生年月日期
     */
    private void showpicker() {

        //Calendar是设定年度日期对象和一个整数字段之间转换的抽象基类
        //获取当前年月日
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(ThirdUserInfoActivity.this, 3,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        et_age.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();

    }

    /**
     * 各项所需上传的个人信息数据的控制
     */
    private void toNext() {

        if (TextUtils.isEmpty(et_height.getText().toString().trim())) {
            ToastUtils.showShort(this, "身高不能为空");
            return;
        } else if (100 > toNumber(et_height) || toNumber(et_height) > 250) {
            ToastUtils.showShort(this, "身高不正确（100-250cm以内）");
            return;
        } else if (TextUtils.isEmpty(et_weight.getText().toString().trim())) {
            ToastUtils.showShort(this, "体重不能为空");
            return;
        } else if (40 > toNumberF(et_weight) || toNumberF(et_weight) > 130) {
            ToastUtils.showShort(this, "体重不正确(40-130kg以内)");
            return;
        } else if (et_age.getText().toString().trim().equals("")) {
            ToastUtils.showShort(this, "请选择出生年月");
            return;
        } else {
            initToNext(this);
        }
    }

    /**
     * 提交个人信息时的警醒提示
     */
    private AlertDialog alertDialog;

    private void initToNext(Context context) {
        alertDialog = new AlertDialog.Builder(context).setTitle("确认提交")
                .setMessage("性别和出生日期提交后不可更改\n\n身高单位为'cm',体重单位为'kg'")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        initToInfoTask();
                    }

                }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create(); // 创建对话框
        alertDialog.setCancelable(false);
        if (alertDialog != null) {
            if (!alertDialog.isShowing()) {
                alertDialog.show(); // 显示
            }
        }
    }

    private ProgressDialog dialog;

    /**
     * 进行个人信息上传接口
     */
    private void initToInfoTask() {
        MoudleUtils.dialogShow(dialog);
        WfApi userInfoApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = thirdloginBean.getInfo().getUser_id();
        String token = thirdloginBean.getInfo().getToken();
        //TODO
        Call<UserInfoBean> callBack = userInfoApi.thirduserinfo(token, user_id, toNumber(et_height), toNumberF(et_weight), sex, et_age.getText().toString().trim());

        callBack.enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                UserInfoBean userinfobean = response.body();
                toLoginFinishNext(userinfobean);//登录成功后的操作
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {

                MoudleUtils.toChekWifi(ThirdUserInfoActivity.this);
                MoudleUtils.dialogDismiss(dialog);


            }
        });
    }

    /**
     * 上传个人信息接口获取的数据处理
     * @param userinfobean
     */
    private void toLoginFinishNext(UserInfoBean userinfobean) {
        if (userinfobean != null) {
            if (userinfobean.getStatus().equals("1")) {
                ToastUtils.show(ThirdUserInfoActivity.this, userinfobean.getMsg(), 0);
                initSaveThirdLoginData(thirdloginBean);
                MoudleUtils.mySetAlias(SPUtils.get(CkApplication.getInstance(), "user_id", 0) + "");//设置别名

                KeyBoardUtils.closeKeyboard(et_height, ThirdUserInfoActivity.this);
                Intent intent = new Intent();
                intent.setClass(ThirdUserInfoActivity.this, MainActivity.class);
                startActivity(intent);

                MoudleUtils.initToNotifyLoginSetThis();//登录成功回掉通知
                NotifyPostUserInforMessageManager.getInstance().sendNotifyPostUserInfoFlag(true);//登录成功回掉通知

                finish();
            } else if (userinfobean.getStatus().equals("0")) {
                ToastUtils.show(ThirdUserInfoActivity.this, userinfobean.getMsg(), 0);
            } else {
                ToastUtils.show(ThirdUserInfoActivity.this, userinfobean.getMsg(), 0);
            }
        } else {

        }
    }

    /**
     * 保存个人信息
     * @param thirdloginBean
     */
    private void initSaveThirdLoginData(ThirdLoginBean thirdloginBean) {
        SavaDataLocalUtils.saveDataString(this, "token", thirdloginBean.getInfo().getToken());
        SavaDataLocalUtils.saveDataString(this, "isbind", thirdloginBean.getInfo().getUser_info().getIsbind());
        SavaDataLocalUtils.saveDataString(this, "type", thirdloginBean.getInfo().getType());
        SavaDataLocalUtils.saveDataInt(this, "sex", thirdloginBean.getInfo().getUser_info().getSex());
        SavaDataLocalUtils.saveDataString(this, "picurl", thirdloginBean.getInfo().getUser_info().getPicurl());
        SavaDataLocalUtils.saveDataString(this, "name", thirdloginBean.getInfo().getUser_info().getUsername());

        SavaDataLocalUtils.saveDataInt(this, "user_id", thirdloginBean.getInfo().getUser_id());
        SavaDataLocalUtils.saveDataString(this, "easepsd", thirdloginBean.getInfo().getUser_info().getEasepsd());

        SavaDataLocalUtils.saveDataInt(this, "gym_id", thirdloginBean.getInfo().getUser_info().getGym_id());

        SavaDataLocalUtils.saveDataString(this, "gymname", thirdloginBean.getInfo().getUser_info().getGymname());
        SavaDataLocalUtils.saveDataString(this, "vip", thirdloginBean.getInfo().getUser_info().getVip());
        SavaDataLocalUtils.saveDataString(this, "tel", thirdloginBean.getInfo().getUser_info().getTel());
        SavaDataLocalUtils.saveDataInt(this, "height", thirdloginBean.getInfo().getUser_info().getHeight());
        SavaDataLocalUtils.saveDataString(this, "relname", thirdloginBean.getInfo().getUser_info().getRelname());

        SavaDataLocalUtils.saveDataFlot(this, "weight", thirdloginBean.getInfo().getUser_info().getWeight());

        SavaDataLocalUtils.saveDataString(this, "motto", thirdloginBean.getInfo().getUser_info().getMotto());
        SavaDataLocalUtils.saveDataInt(this, "age", thirdloginBean.getInfo().getUser_info().getAge());
        SavaDataLocalUtils.saveDataString(this, "bodyanalyse", thirdloginBean.getInfo().getUser_info().getBodyanalyse());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyinfo", thirdloginBean.getInfo().getBodyinfo());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "iscoach", thirdloginBean.getInfo().getUser_info().getIscoach());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "unrentstatus", thirdloginBean.getInfo().getUser_info().getUnrentstatus());
        SavaDataLocalUtils.saveDataInt(this, "steps", thirdloginBean.getInfo().getUser_info().getSteps());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "beforDataTiime", StepService.getTodayDate());
        AppConfig.loginStep=thirdloginBean.getInfo().getUser_info().getSteps();

        StepDcretor.stepBoolean = false;
        MoudleUtils.initSaveData(this);
    }

    /**
     * String型强转成int
     * @param editText
     * @return
     */
    private int toNumber(EditText editText) {
        String str = editText.getText().toString().trim();
        int mInt = Integer.parseInt(str);
        return mInt;
    }

    /**
     * String型强转成float
     * @param editText
     * @return
     */
    private float toNumberF(EditText editText) {
        String str = editText.getText().toString().trim();
        float mInt = Float.parseFloat(str);
        return mInt;
    }


    /**
     * 返回键锁定
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
