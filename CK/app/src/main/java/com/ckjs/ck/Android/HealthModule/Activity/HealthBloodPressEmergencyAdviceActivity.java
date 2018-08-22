package com.ckjs.ck.Android.HealthModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.PreWarningBean;
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
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HealthBloodPressEmergencyAdviceActivity extends AppCompatActivity {

    private KyLoadingBuilder builder;
    private String id;
    private TextView tv_advice_2;
    private TextView tv_advice_data;
    private TextView tv_advice_4;
    private TextView tv_advice_5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_advice);
        initToolbar();
        initId();
        Intent intent=getIntent();
        id = intent.getStringExtra("id");
        builder =new KyLoadingBuilder(this);
        initData();
    }
    /**
     * 走接口
     */
    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(HealthBloodPressEmergencyAdviceActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(HealthBloodPressEmergencyAdviceActivity.this, "token", "");
        Call<PreWarningBean> callBack = restApi.prewarning(user_id+"",token,id);

        callBack.enqueue(new Callback<PreWarningBean>() {
            @Override
            public void onResponse(Call<PreWarningBean> call, Response<PreWarningBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            initUI(response.body().getInfo());
                        }
                    }else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(HealthBloodPressEmergencyAdviceActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<PreWarningBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HealthBloodPressEmergencyAdviceActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    /**
     * 更新UI
     * @param info
     */
    private void initUI(PreWarningBean.PreWarningInfoBean info) {
        tv_advice_data.setText(info.getDay()+" "+info.getApm()+info.getTime());
        tv_advice_2.setText(info.getDpressure()+"/"+info.getSpressure()+"kpa");
        tv_advice_4.setText(info.getWarnings());
        tv_advice_5.setText(info.getAdvice());
    }

    private void initId() {
        TextView tv_advice_1= (TextView) findViewById(R.id.tv_advice_1);
        tv_advice_1.setText("血压数值：");
        tv_advice_2 = (TextView) findViewById(R.id.tv_advice_2);
        TextView tv_advice_3= (TextView) findViewById(R.id.tv_advice_3);
        tv_advice_3.setText("血压诊断：");

        tv_advice_data = (TextView) findViewById(R.id.tv_advice_data);
        tv_advice_4 = (TextView) findViewById(R.id.tv_advice_4);
        tv_advice_5 = (TextView) findViewById(R.id.tv_advice_5);

        LinearLayout ll_health_advice= (LinearLayout) findViewById(R.id.ll_health_advice);
        ll_health_advice.setBackgroundResource(R.drawable.ll_rectangle_circl_bloodpress);
    }

    /**
     * 标题栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("详情");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * 返回设置
     *
     * @param item
     * @return
     */
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
