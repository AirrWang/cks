package com.ckjs.ck.Android.MineModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.BindInfoBean;
import com.ckjs.ck.Bean.BindacceptBean;
import com.ckjs.ck.Manager.NotifyInfoUpdateMessageManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MineBindJSFSureActivity extends AppCompatActivity implements View.OnClickListener {

    private KyLoadingBuilder builder;
    private String bind_id;
    private TextView tv_sure_title;
    private TextView tv_sure_cardnum;
    private TextView tv_sure_name;
    private TextView tv_sure_address;
    private TextView tv_sure_tel;
    private Button btn_sure_sure;
    private TextView tv_sure_no;
    private int status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_sure);
        initToolbar();
        builder = new KyLoadingBuilder(this);
        Intent intent = getIntent();
        bind_id = intent.getStringExtra("id");
        initId();
        initData();
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(this, "user_id", 0));
        String token = (String) SPUtils.get(this, "token", "");
        Call<BindInfoBean> callBack = restApi.bindinfo(user_id + "", token, bind_id);

        callBack.enqueue(new Callback<BindInfoBean>() {
            @Override
            public void onResponse(Call<BindInfoBean> call, Response<BindInfoBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            initToUpdataUI(response.body().getInfo());
                        }
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(MineBindJSFSureActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MineBindJSFSureActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<BindInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineBindJSFSureActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });

    }

    private void initToUpdataUI(BindInfoBean.BindInfoDetailBean info) {
        tv_sure_title.setText(info.getTitle());
        tv_sure_cardnum.setText("您的卡号: " + info.getCard());
        tv_sure_name.setText("所属健身房: " + info.getName());
        tv_sure_address.setText("地址: " + info.getPlace());
        tv_sure_tel.setText("电话: " + info.getTel());
        if (info.getStatus().equals("1")) {
            btn_sure_sure.setText("已点亮");
            btn_sure_sure.setEnabled(false);
            tv_sure_no.setEnabled(false);
        } else if (info.getStatus().equals("-1")) {
            btn_sure_sure.setText("已拒绝");
            btn_sure_sure.setEnabled(false);
            tv_sure_no.setEnabled(false);
        }
    }

    private void initId() {
        tv_sure_title = (TextView) findViewById(R.id.tv_sure_title);
        tv_sure_cardnum = (TextView) findViewById(R.id.tv_sure_cardnum);
        tv_sure_name = (TextView) findViewById(R.id.tv_sure_name);
        tv_sure_address = (TextView) findViewById(R.id.tv_sure_address);
        tv_sure_tel = (TextView) findViewById(R.id.tv_sure_tel);
        btn_sure_sure = (Button) findViewById(R.id.btn_sure_sure);
        btn_sure_sure.setOnClickListener(this);
        tv_sure_no = (TextView) findViewById(R.id.tv_sure_no);
        tv_sure_no.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("通知");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure_sure:
                status = 1;
                initToSure();
                break;
            case R.id.tv_sure_no:
                status = -1;
                initToSure();
                break;
        }
    }

    private void initToSure() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(this, "user_id", 0));
        String token = (String) SPUtils.get(this, "token", "");
        Call<BindacceptBean> callBack = restApi.bindaccept(user_id + "", token, bind_id, status + "");

        callBack.enqueue(new Callback<BindacceptBean>() {
            @Override
            public void onResponse(Call<BindacceptBean> call, Response<BindacceptBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (status == 1) {
                            NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                        }
                        ToastUtils.show(MineBindJSFSureActivity.this, response.body().getMsg(), 0);
                        finish();
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(MineBindJSFSureActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MineBindJSFSureActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<BindacceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineBindJSFSureActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }
}
