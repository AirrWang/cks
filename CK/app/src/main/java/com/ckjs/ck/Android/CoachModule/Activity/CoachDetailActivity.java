package com.ckjs.ck.Android.CoachModule.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.CoachCoachInfoBean;
import com.ckjs.ck.Bean.CoachOrderBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class CoachDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_coach_mine_jianjie;
    private SimpleDraweeView coach_mine_detail;
    private TextView coach_mine_name;
    private TextView coach_mine_sex;
    private TextView coach_mine_gymname;
    private TextView coach_mine_shangmen;
    private TextView coach_mine_shipin;
    private KyLoadingBuilder builder;
    private TextView coach_mine_tel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_mine_detail);
        builder = new KyLoadingBuilder(this);
        initId();
        initToolbar();
        initData();
        dialog = new ProgressDialog(this);
    }

    /**
     * 获取个人信息
     */
    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        String user_id = SPUtils.get(this, "user_id", 0) + "";
        String token = (String) SPUtils.get(this, "token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<CoachCoachInfoBean> callBack = restApi.coachinfo(user_id,token);

        callBack.enqueue(new Callback<CoachCoachInfoBean>() {
            @Override
            public void onResponse(Call<CoachCoachInfoBean> call, Response<CoachCoachInfoBean> response) {
                CoachCoachInfoBean coachinfoBean = response.body();
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        initUI(coachinfoBean);
                    } else {
                        ToastUtils.show(CoachDetailActivity.this, response.body().getMsg(), 0);
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<CoachCoachInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(CoachDetailActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    /**
     * 放置UI
     * @param coachinfoBean
     */
    private void initUI(CoachCoachInfoBean coachinfoBean) {
        if (coachinfoBean.getInfo()!=null){
            FrescoUtils.setImage(coach_mine_detail,AppConfig.url_jszd+coachinfoBean.getInfo().getPicture());
            coach_mine_name.setText(coachinfoBean.getInfo().getName());
            if (coachinfoBean.getInfo().getSex().equals("1")){
                coach_mine_sex.setText("男");
            }else if (coachinfoBean.getInfo().getSex().equals("2")){
                coach_mine_sex.setText("女");
            }
            coach_mine_gymname.setText(coachinfoBean.getInfo().getGymname());
            coach_mine_shangmen.setText("￥"+coachinfoBean.getInfo().getPersonal());
            coach_mine_shipin.setText("￥"+coachinfoBean.getInfo().getDistance());
            et_coach_mine_jianjie.setText(coachinfoBean.getInfo().getIntro());
            coach_mine_tel.setText(coachinfoBean.getInfo().getTel());
        }
    }

    private void initId() {
        coach_mine_shipin = (TextView) findViewById(R.id.coach_mine_shipin);
        coach_mine_shangmen = (TextView) findViewById(R.id.coach_mine_shangmen);
        coach_mine_gymname = (TextView) findViewById(R.id.coach_mine_gymname);
        coach_mine_sex = (TextView) findViewById(R.id.coach_mine_sex);
        et_coach_mine_jianjie = (EditText) findViewById(R.id.et_coach_mine_jianjie);
        coach_mine_detail = (SimpleDraweeView) findViewById(R.id.coach_mine_detail);
        coach_mine_name = (TextView) findViewById(R.id.coach_mine_name);
        coach_mine_tel = (TextView) findViewById(R.id.coach_mine_tel);
    }

    /**
     * 设置Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(getResources().getText(R.string.title_activity_mine_userinfo));
        setSupportActionBar(toolbar);

        TextView button = (TextView) findViewById(R.id.toolbar_button);
        button.setText("保存");
        button.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify shouhuan_serch parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            KeyBoardUtils.closeKeyboard(et_coach_mine_jianjie, CoachDetailActivity.this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String s=et_coach_mine_jianjie.getText().toString().trim();
        if (s==null&&s.equals("")){
            ToastUtils.showShort(this,"个人介绍不能为空");
        }else {
            if (!DataUtils.containsEmoji(s)) {
                toNext();
            }else {
                ToastUtils.showShort(CoachDetailActivity.this,"不支持输入Emoji表情符号");
            }
        }

    }

    /**
     * 更改个人介绍
     */
    private ProgressDialog dialog;
    private void toNext() {
        MoudleUtils.dialogShow(dialog);
        String user_id = SPUtils.get(this, "user_id", 0) + "";
        String token = (String) SPUtils.get(this, "token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<CoachOrderBean> callBack = restApi.upintro(user_id,token,et_coach_mine_jianjie.getText().toString());

        callBack.enqueue(new Callback<CoachOrderBean>() {
            @Override
            public void onResponse(Call<CoachOrderBean> call, Response<CoachOrderBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        ToastUtils.show(CoachDetailActivity.this, response.body().getMsg(), 0);
                        KeyBoardUtils.closeKeyboard(et_coach_mine_jianjie, CoachDetailActivity.this);
                        finish();
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(CoachDetailActivity.this, response.body().getMsg(), 0);
                    }else {
                        ToastUtils.show(CoachDetailActivity.this, response.body().getMsg(), 0);
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<CoachOrderBean> call, Throwable t) {
                MoudleUtils.toChekWifi(CoachDetailActivity.this);
                MoudleUtils.dialogDismiss(dialog);
            }
        });
    }
}
