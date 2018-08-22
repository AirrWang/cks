package com.ckjs.ck.Android.CoachModule.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.HSHH5Activity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.WithdrawalBean;
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


public class TiXianActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_tx_xy;
    private TextView tv_coach_tixian;
    private KyLoadingBuilder builder;
    private TextView tv_coach_tixian_bank;
    private TextView tv_coach_tixian_yue;
    private EditText et_coach_tixian_jine;
    private float rate;
    private TextView tv_coach_tixian_shouxufei;
    private TextView tv_coach_tixian_daozhang;
    private Button btn_tx;
    private ProgressDialog dilag;
    private String money = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_mine_tixian);
        builder = new KyLoadingBuilder(this);
        dilag = new ProgressDialog(this);
        initToolbar();
        initId();
        initData();
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) SPUtils.get(this, "user_id", 0);
        String token = (String) SPUtils.get(this, "token", "");

        Call<WithdrawalBean> callBack = restApi.withdrawal(user_id + "", token);


        callBack.enqueue(new Callback<WithdrawalBean>() {
            @Override
            public void onResponse(Call<WithdrawalBean> call, Response<WithdrawalBean> response) {
                WithdrawalBean coachRecordBean = response.body();
                if (coachRecordBean != null) {
                    if (coachRecordBean.getStatus().equals("1")) {
                        initUI(coachRecordBean);
                        btn_tx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (money != null) {
                                        if (!money.equals("")) {
                                            toXian();
                                        } else {
                                            ToastUtils.showShort(TiXianActivity.this,"输入提现金额");
                                        }
                                    } else {
                                        ToastUtils.showShort(TiXianActivity.this,"输入提现金额");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else if (coachRecordBean.getStatus().equals("0")) {
                        ToastUtils.showShort(TiXianActivity.this, coachRecordBean.getMsg());
                    } else if (coachRecordBean.getStatus().equals("2")) {
                        ToastUtils.showShort(TiXianActivity.this,coachRecordBean.getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<WithdrawalBean> call, Throwable t) {

                ToastUtils.show(TiXianActivity.this, getResources().getString(R.string.not_wlan_show), 0);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });

    }

    private void initUI(WithdrawalBean coachRecordBean) {
        if (coachRecordBean.getInfo() != null) {
            tv_coach_tixian_bank.setText(coachRecordBean.getInfo().getBank() + "(" + coachRecordBean.getInfo().getBankid() + ")");
            tv_coach_tixian_yue.setText("￥" + coachRecordBean.getInfo().getBalance());
            rate = coachRecordBean.getInfo().getRate();
        }
    }

    private void initId() {
        tv_coach_tixian = (TextView) findViewById(R.id.tv_coach_tixian);
        tv_tx_xy = (TextView) findViewById(R.id.tv_tx_xy);
        btn_tx = (Button) findViewById(R.id.btn_tx);
        tv_coach_tixian.setOnClickListener(this);
        tv_tx_xy.setOnClickListener(this);
        tv_coach_tixian_bank = (TextView) findViewById(R.id.tv_coach_tixian_bank);
        tv_coach_tixian_yue = (TextView) findViewById(R.id.tv_coach_tixian_yue);
        et_coach_tixian_jine = (EditText) findViewById(R.id.et_coach_tixian_jine);
        et_coach_tixian_jine.addTextChangedListener(textWatcher);
        tv_coach_tixian_shouxufei = (TextView) findViewById(R.id.tv_coach_tixian_shouxufei);
        tv_coach_tixian_daozhang = (TextView) findViewById(R.id.tv_coach_tixian_daozhang);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        private String dz;
        private String sxf;
        private Double daozhang;
        private Double shouxufei;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            try {
                if (et_coach_tixian_jine.getText().toString().trim().equals("")){
                    sxf="0.00";
                    dz="0.00";
                }else {
                    float ff= Float.parseFloat(et_coach_tixian_jine.getText().toString().trim());
                    shouxufei = ff * rate * 1.0d;
                    sxf = MoudleUtils.roundDouble(shouxufei, 1);
                    daozhang = (Float.parseFloat(et_coach_tixian_jine.getText().toString().trim()) - shouxufei)*1.0d;
                    dz = MoudleUtils.roundDouble(daozhang, 1);
                }
                tv_coach_tixian_shouxufei.setText("-" + sxf);
                tv_coach_tixian_daozhang.setText("￥" + dz);
                money = shouxufei + daozhang + "";
            } catch (Exception e) {
                e.printStackTrace();

            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
//


        }
    };

    AlertDialog.Builder albuilder;
    AlertDialog alert;

    private void toXian() {
        if (albuilder == null) {
            albuilder = new AlertDialog.Builder(this);
        }
        albuilder.setMessage("您是否提现：");


        albuilder.setPositiveButton("提现", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MoudleUtils.dialogShow(dilag);
                initJieDanTask();
            }
        });
        albuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert = albuilder.create();
        if (!alert.isShowing()) {
            alert.show();
        }
    }

    private void initJieDanTask() {

        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        Call<AcceptBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).withdraw(token, user_id, money);
        getSignBeanCall.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                AcceptBean bean = response.body();
                if (bean != null) {

                    ToastUtils.showShort(TiXianActivity.this,bean.getMsg());
                    if (bean.getStatus().equals("1")) {
                        finish();
                    }
                }
                MoudleUtils.dialogDismiss(dilag);
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dilag);

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("申请提现");//设置标题
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_coach_tixian:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("如需变更到账账户，请至官方网站进行更改");
                builder.create().show();
                break;
            case R.id.tv_tx_xy:
                startActivity(new Intent().putExtra("acurl","http://www.chaokongs.com/circle/withdrawalagreement ").putExtra("name", "超空私教提现协议").setClass(this, HSHH5Activity.class));
                break;
        }
    }
}
