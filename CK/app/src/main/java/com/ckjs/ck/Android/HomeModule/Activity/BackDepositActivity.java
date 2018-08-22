package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.UnRentInfoBean;
import com.ckjs.ck.Bean.UnbandBean;
import com.ckjs.ck.Manager.NotifyInfoUpdateMessageManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class BackDepositActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_despoit_select;
    private KyLoadingBuilder builder;
    private ProgressDialog dialog;
    private UnRentInfoBean body;
    private TextView tv_back_name;
    private TextView tv_bacck_tel;
    private TextView tv_back_band;
    private TextView tv_back_desposit;
    private Button btn_pay_jie_bang;
    private String band;
    private TextView tv_back_memberID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_deposit);
        dialog = new ProgressDialog(this);
        initCsh();
        initId();
        initToolbar();
        initData();
    }

    private void initCsh() {
        AppConfig.select_jsf = "";
        AppConfig.select_jsf_id = "";
    }

    private void initData() {
        Intent intent = getIntent();
        String mac = intent.getStringExtra("mac");
        builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);
        int user_id = (int) (SPUtils.get(BackDepositActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(BackDepositActivity.this, "token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<UnRentInfoBean> callBack = restApi.unrentinfo(user_id + "", token, mac);

        callBack.enqueue(new Callback<UnRentInfoBean>() {
            @Override
            public void onResponse(Call<UnRentInfoBean> call, Response<UnRentInfoBean> response) {
                if (response.body() != null) {
                    body = response.body();
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            initUI();
                        }
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(BackDepositActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(BackDepositActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<UnRentInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(BackDepositActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });


    }

    private void initUI() {
        tv_back_name.setText(body.getInfo().getRelname());
        tv_bacck_tel.setText(body.getInfo().getTel());
        band = body.getInfo().getBand();
        tv_back_band.setText(body.getInfo().getBand());
        tv_back_desposit.setText("￥" + body.getInfo().getAmount());
        tv_back_memberID.setText(body.getInfo().getUser_id());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppConfig.select_jsf == null || AppConfig.select_jsf.equals("")) {
            MoudleUtils.textViewSetText(tv_despoit_select, "请选择门店");
        } else {
            MoudleUtils.textViewSetText(tv_despoit_select, AppConfig.select_jsf);
            tv_despoit_select.setTextColor(this.getResources().getColor(R.color.c_33));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initCsh();
    }

    private void initId() {
        tv_back_memberID = (TextView) findViewById(R.id.tv_back_memberID);
        tv_despoit_select = (TextView) findViewById(R.id.tv_despoit_select);
        tv_back_name = (TextView) findViewById(R.id.tv_back_name);
        tv_bacck_tel = (TextView) findViewById(R.id.tv_bacck_tel);
        tv_back_band = (TextView) findViewById(R.id.tv_back_band);
        tv_back_desposit = (TextView) findViewById(R.id.tv_back_desposit);
        btn_pay_jie_bang = (Button) findViewById(R.id.btn_pay_jie_bang);

        tv_despoit_select.setOnClickListener(this);
        btn_pay_jie_bang.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("退还押金");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_despoit_select:
                startActivity(new Intent().setClass(BackDepositActivity.this, DespositSelectActivity.class));
                break;
            case R.id.btn_pay_jie_bang:

                if (AppConfig.select_jsf_id == null || AppConfig.select_jsf_id.equals("")) {
                    ToastUtils.showShortNotInternet("请选择健身房");
                    return;
                }
                if (AppConfig.select_jsf == null || AppConfig.select_jsf.equals("")) {
                    ToastUtils.showShortNotInternet("请选择健身房");
                    return;
                }
                if (band != null && !band.equals("")) {
                    if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                        if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                            initToPayJieBang();
                        } else {
                            ToastUtils.showShortNotInternet("请等待数据同步结束");
                        }
                    } else {
                        ToastUtils.showShortNotInternet("手环未连接");
                    }
                    break;
                }
        }
    }

    private void initToPayJieBang() {
        MoudleUtils.dialogShow(dialog);
        int user_id = (int) (SPUtils.get(BackDepositActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(BackDepositActivity.this, "token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<UnbandBean> callBack = restApi.unrent(token, user_id, band, AppConfig.select_jsf_id);

        callBack.enqueue(new Callback<UnbandBean>() {
            @Override
            public void onResponse(Call<UnbandBean> call, Response<UnbandBean> response) {

                UnbandBean acceptBean = response.body();
                if (acceptBean == null) {
                    MoudleUtils.dialogDismiss(dialog);
                    return;
                }
                String status = acceptBean.getStatus();
                if (status != null) {
                    if (status.equals("1")) {
                        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "isbind", acceptBean.getInfo().getIsbind());
                        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "nameStepType", "0");
                        NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                        initHuFu();
                        initToJiBangAceese();
                    } else {
                        MoudleUtils.dialogDismiss(dialog);
                        ToastUtils.showShort(BackDepositActivity.this, acceptBean.getMsg());
                    }
                } else {
                    MoudleUtils.dialogDismiss(dialog);
                }

            }

            @Override
            public void onFailure(Call<UnbandBean> call, Throwable t) {
                MoudleUtils.toChekWifi(BackDepositActivity.this);
                MoudleUtils.dialogDismiss(dialog);
            }
        });

    }

    private void initToJiBangAceese() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MoudleUtils.dialogDismiss(dialog);
                finish();
                startActivity(new Intent().setClass(BackDepositActivity.this, BackStateActivity.class));
            }
        }, 3500);
        return;
    }

    private void initHuFu() {
        if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x0A, 0x01, 0x00, 0x00});
                }
            }, 500);
        }
    }
}
