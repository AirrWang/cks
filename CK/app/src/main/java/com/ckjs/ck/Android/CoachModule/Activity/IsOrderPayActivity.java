package com.ckjs.ck.Android.CoachModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.OrderinfoBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class IsOrderPayActivity extends AppCompatActivity {
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_call)
    TextView tv_call;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_fu_kuan)
    TextView tv_fu_kuan;
    @BindView(R.id.sd_fu_kuan)
    SimpleDraweeView sd_fu_kuan;
    @BindView(R.id.tv_fu_kuan_ti_shi)
    TextView tv_fu_kuan_ti_shi;
    @BindView(R.id.custom_sex)
    SimpleDraweeView custom_sex;
    private String id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_order_pay);
        ButterKnife.bind(this);
        initToolbar();
        initGetId();
        initCustomTask();
    }

    private void initGetId() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
    }

    /**
     *
     */
    private void initCustomTask() {
        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);

        Call<OrderinfoBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).orderinfo(token, user_id, id);
        getSignBeanCall.enqueue(new Callback<OrderinfoBean>() {
            @Override
            public void onResponse(Call<OrderinfoBean> call, Response<OrderinfoBean> response) {
                OrderinfoBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {
                            MoudleUtils.textViewSetText(tv_name, bean.getInfo().getRelname());
                            MoudleUtils.textViewSetText(tv_address, bean.getInfo().getAdress());

                            MoudleUtils.textViewSetText(tv_money, "￥" + bean.getInfo().getAmount());
                            String sex = bean.getInfo().getSex();
                            if (sex != null) {
                                if (sex.equals("1")) {
                                    FrescoUtils.setImage(custom_sex, AppConfig.res + R.drawable.my_boy);
                                } else if (sex.equals("2")) {
                                    FrescoUtils.setImage(custom_sex, AppConfig.res + R.drawable.my_girl);
                                }
                            }
                            String s_type = bean.getInfo().getType();
                            if (s_type != null) {
                                if (s_type.equals("distance")) {
                                    MoudleUtils.textViewSetText(tv_type, "视频指导");
                                } else if (s_type.equals("personal")) {
                                    MoudleUtils.textViewSetText(tv_type, "上门指导");
                                }
                            }
                            String status = bean.getInfo().getStatus();
                            if (status != null) {
                                if (status.equals("3") || status.equals("4") || status.equals("5")) {
                                    FrescoUtils.setImage(sd_fu_kuan, AppConfig.res + R.drawable.smile_color);
                                    MoudleUtils.textViewSetText(tv_fu_kuan, "已付款");
                                    MoudleUtils.textViewSetText(tv_fu_kuan_ti_shi, "对方已付款，相互联系去完成课程吧");
                                    MoudleUtils.textViewSetText(tv_call, bean.getInfo().getTel());
                                } else if (status.equals("-1") || status.equals("1") || status.equals("2")) {
                                    FrescoUtils.setImage(sd_fu_kuan, AppConfig.res + R.drawable.cry_collor);
                                    MoudleUtils.textViewSetText(tv_fu_kuan, "未付款");
                                    MoudleUtils.textViewSetText(tv_fu_kuan_ti_shi, "对方未付款，请继续等待");
                                    MoudleUtils.textViewSetText(tv_call, "暂不显示");

                                }
                            }
                        }
                    } else {
                        ToastUtils.showShort(IsOrderPayActivity.this,bean.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderinfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
            }
        });


    }

    /**
     * toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("付款状况");
        setSupportActionBar(toolbar);
        ActionBar actionBar = (getSupportActionBar());
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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
