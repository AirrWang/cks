package com.ckjs.ck.Android.MineModule.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.MedicalFileBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
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

public class MineAimShowActivity extends AppCompatActivity implements View.OnClickListener {
    private KyLoadingBuilder builder;
    private MedicalFileBean.MedicalFileInfoBean body;
    private TextView tv_card_name;
    private TextView tv_card_age;
    private TextView tv_card_sex;
    private SimpleDraweeView sd_card_pic;
    private TextView tv_card_mcondition;
    private TextView tv_card_notes;
    private TextView tv_card_allergy;
    private TextView tv_card_druguse;
    private TextView tv_card_bloodtype;
    private TextView tv_card_weight;
    private TextView tv_card_height;
    private TextView tv_card_contact;
    private TextView tv_card_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_show);
        builder = new KyLoadingBuilder(this);
        initToolBar();
        initId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MineAimShowActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(MineAimShowActivity.this, "token", "");

        Call<MedicalFileBean> callBack = restApi.medicalfile(token, user_id + "");

        callBack.enqueue(new Callback<MedicalFileBean>() {
            @Override
            public void onResponse(Call<MedicalFileBean> call, Response<MedicalFileBean> response) {


                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        body = response.body().getInfo();
                        if (body != null) {
                            initUpUiTask();
                        } else {
                            ToastUtils.showShortNotInternet("暂无数据,快去添加吧！");
                            MoudleUtils.kyloadingDismiss(builder);
                        }

                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MineAimShowActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MineAimShowActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<MedicalFileBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineAimShowActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }


    private void initTvData(TextView textView, String s) {
        if (s != null && !s.equals("")) {
            textView.setText(s);
        }
    }

    private void initUpUiTask() {
        tv_card_name.setText(body.getRelname());
        tv_card_age.setText(body.getAge() + "岁");
        if (body.getSex().equals("1")) {
            tv_card_sex.setText("男");
        } else {
            tv_card_sex.setText("女");
        }
        FrescoUtils.setImage(sd_card_pic, AppConfig.url + body.getPicurl());

        initTvData(tv_card_mcondition, body.getMcondition());
        initTvData(tv_card_notes, body.getNotes());
        initTvData(tv_card_allergy, body.getAllergy());
        initTvData(tv_card_druguse, body.getDruguse());
        initTvData(tv_card_bloodtype, body.getBloodtype());

        if (body.getWeight() != null && !body.getWeight().equals("")) {
            tv_card_weight.setText(body.getWeight() + "kg");
        }
        if (body.getHeight() != null && !body.getHeight().equals("")) {
            tv_card_height.setText(body.getHeight() + "cm");
        }

        if (body.getContactname() != null && body.getContacttel() != null && !body.getContactname().equals("") && !body.getContacttel().equals("")) {
            tv_card_contact.setText(body.getContactname() + "   " + body.getContacttel());
        }
        if (body.getUpdatetime() != null && !body.getUpdatetime().equals("")) {
            tv_card_time.setText( "更新于  " + body.getUpdatetime());
        }


    }

    private void initId() {
        tv_card_time = (TextView) findViewById(R.id.tv_card_time);
        tv_card_contact = (TextView) findViewById(R.id.tv_card_contact);
        tv_card_height = (TextView) findViewById(R.id.tv_card_height);
        tv_card_weight = (TextView) findViewById(R.id.tv_card_weight);
        tv_card_druguse = (TextView) findViewById(R.id.tv_card_druguse);
        tv_card_bloodtype = (TextView) findViewById(R.id.tv_card_bloodtype);
        tv_card_allergy = (TextView) findViewById(R.id.tv_card_allergy);
        tv_card_notes = (TextView) findViewById(R.id.tv_card_notes);
        tv_card_mcondition = (TextView) findViewById(R.id.tv_card_mcondition);
        sd_card_pic = (SimpleDraweeView) findViewById(R.id.sd_card_pic);
        tv_card_sex = (TextView) findViewById(R.id.tv_card_sex);
        tv_card_name = (TextView) findViewById(R.id.tv_card_name);
        tv_card_age = (TextView) findViewById(R.id.tv_card_age);
        LinearLayout ll_detail_contact = (LinearLayout) findViewById(R.id.ll_detail_contact);
        ll_detail_contact.setOnClickListener(this);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("医疗档案急救卡");
        setSupportActionBar(toolbar);

        TextView button = (TextView) findViewById(R.id.toolbar_button);
        button.setText("编辑");
        button.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_button:
                if (body!=null&&body.getRelname()!=null&&!body.getRelname().equals("")) {
                    Intent intent = new Intent();
                    intent.putExtra("realname", body.getRelname());
                    intent.setClass(MineAimShowActivity.this, MineAimCardActivity.class);
                    startActivity(intent);//编辑
                }else {
                    ToastUtils.showShortNotInternet("获取信息失败，无法编辑");
                }
                break;
            case R.id.ll_detail_contact:
                goToCall();
                break;
        }
    }

    /**
     * 提示打电话
     */
    public void goToCall() {
        if (!body.getContacttel().equals("")) {
            new AlertDialog.Builder(this).setTitle("联系电话")
                    .setMessage(body.getContacttel())
                    .setPositiveButton("拨打", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initToCall();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }

    /**
     * 打电话
     */
    private void initToCall() {
        try {
            String mobile = body.getContacttel();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "没有相关应用", Toast.LENGTH_SHORT).show();
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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
