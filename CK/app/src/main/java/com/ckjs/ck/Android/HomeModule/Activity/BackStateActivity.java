package com.ckjs.ck.Android.HomeModule.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.UnRentStatusBean;
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
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class BackStateActivity extends AppCompatActivity {

    private KyLoadingBuilder build;
    private UnRentStatusBean body;
    private TextView tv_status_name;
    private TextView tv_status_userid;
    private TextView tv_status_realname;
    private TextView tv_status_tel;
    private TextView tv_status_band;
    private TextView tv_status_desposit;
    private TextView tv_status;
    private TextView tv_hsh_yj;
    private LinearLayout ll_name;
    private LinearLayout ll_band;
    private TextView ll_line;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_state);
        build = new KyLoadingBuilder(this);
        initId();
        initToolbar();
        initData();
    }

    private void initId() {
        ll_line = (TextView) findViewById(R.id.ll_line);
        tv_status_userid = (TextView) findViewById(R.id.tv_status_userid);
        tv_status_name = (TextView) findViewById(R.id.tv_status_name);
        tv_status_realname = (TextView) findViewById(R.id.tv_status_realname);
        tv_status_tel = (TextView) findViewById(R.id.tv_status_tel);
        tv_status_band = (TextView) findViewById(R.id.tv_status_band);
        tv_status_desposit = (TextView) findViewById(R.id.tv_status_desposit);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_hsh_yj = (TextView) findViewById(R.id.tv_hsh_yj);
        ll_name = (LinearLayout) findViewById(R.id.ll_name);
        ll_band = (LinearLayout) findViewById(R.id.ll_band);
    }

    private void initData() {
        MoudleUtils.kyloadingShow(build);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");
        Call<UnRentStatusBean> callBack = restApi.unrentstatus((int) SPUtils.get(this, "user_id", 0) + "", token);

        callBack.enqueue(new Callback<UnRentStatusBean>() {
            @Override
            public void onResponse(Call<UnRentStatusBean> call, Response<UnRentStatusBean> response) {

                if (response.body() != null) {
                    body = response.body();
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            initUI();
                        } else {
                            ToastUtils.showShortNotInternet("暂无数据，请稍后重试！");
                        }
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(BackStateActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(BackStateActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(build);
            }

            @Override
            public void onFailure(Call<UnRentStatusBean> call, Throwable t) {
                MoudleUtils.toChekWifi(BackStateActivity.this);
                MoudleUtils.kyloadingDismiss(build);
            }
        });
    }

    private void initUI() {

        if (body.getInfo().getStatus().equals("0")) {
            tv_hsh_yj.setText("手环退还");
            tv_status.setText("审核中");
            MoudleUtils.viewShow(ll_line);
            MoudleUtils.viewShow(ll_name);
            MoudleUtils.viewShow(ll_name);

        } else if (body.getInfo().getStatus().equals("1")) {
            NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
            tv_hsh_yj.setText("手环退还");
            tv_status.setText("已审核");
            MoudleUtils.viewGone(ll_line);
            MoudleUtils.viewGone(ll_band);
            MoudleUtils.viewGone(ll_name);
        } else if (body.getInfo().getStatus().equals("2")) {
            tv_hsh_yj.setText("押金");
            tv_status.setText("退款中");
            MoudleUtils.viewGone(ll_line);
            MoudleUtils.viewGone(ll_band);
            MoudleUtils.viewGone(ll_name);

        } else if (body.getInfo().getStatus().equals("3")) {
            NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
            tv_hsh_yj.setText("押金");
            tv_status.setText("已退款");
            MoudleUtils.viewGone(ll_line);
            MoudleUtils.viewGone(ll_band);
            MoudleUtils.viewGone(ll_name);
        }
        tv_status_name.setText(body.getInfo().getGymname());
        tv_status_realname.setText(body.getInfo().getRelname());
        tv_status_userid.setText(body.getInfo().getUser_id());
        tv_status_tel.setText(body.getInfo().getTel());
        tv_status_band.setText(body.getInfo().getBand());
        if (body.getInfo().getAmount() != null && !body.getInfo().getAmount().equals("")) {
            tv_status_desposit.setText("￥" + body.getInfo().getAmount());
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("退还状态");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * toolbar返回键设置
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
