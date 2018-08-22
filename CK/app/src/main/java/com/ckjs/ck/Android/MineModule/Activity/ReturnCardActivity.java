package com.ckjs.ck.Android.MineModule.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.AcH5Activity;
import com.ckjs.ck.Android.HomeModule.Activity.BackStateActivity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.BindacceptBean;
import com.ckjs.ck.Bean.RefundinfoBaom;
import com.ckjs.ck.Manager.NotifyInfoUpdateMessageManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
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
public class ReturnCardActivity extends AppCompatActivity {

    private TextView tv_zan_wu, tv_more;
    private TextView tv_return_member_id;
    private TextView tv_return_name;
    private TextView tv_return_tel;
    private TextView tv_return_desposit;
    private Button btn_return_card;
    private KyLoadingBuilder builder;
    private LinearLayout ll_have;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_deposit);
        initToolbar();
        initId();
        initData();
    }

    private void initData() {
        builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<RefundinfoBaom> callBack = restApi.refundinfo(token, user_id);

        callBack.enqueue(new Callback<RefundinfoBaom>() {
            @Override
            public void onResponse(Call<RefundinfoBaom> call, Response<RefundinfoBaom> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            final String pay = response.body().getInfo().getPay();
                            if (pay != null) {
                                if (pay.equals("1")) {
                                    MoudleUtils.viewInvisibily(tv_more);
                                    tv_zan_wu.setText("");
                                    tv_more.setText("");
                                    MoudleUtils.viewShow(ll_have);
                                    initUI(response.body().getInfo());
                                } else if (pay.equals("0")) {
                                    MoudleUtils.viewGone(ll_have);
                                    tv_zan_wu.setText("暂无押金\n" + "你未取得共享手环资格");
                                    tv_more.setText("了解押金");
                                    MoudleUtils.viewShow(tv_more);
                                    tv_more.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (pay.equals("0")) {
                                                startActivity(new Intent().putExtra("acurl", AppConfig.h5_ya_jin).setClass(ReturnCardActivity.this, AcH5Activity.class));
                                            }
                                        }
                                    });
                                }
                            }

                        }
                    } else {
                        ToastUtils.showShort(ReturnCardActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<RefundinfoBaom> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(builder);
            }
        });


    }

    private void initUI(RefundinfoBaom.RefundinfoBaomInfo info) {
        MoudleUtils.textViewSetText(tv_return_member_id, info.getUser_id());
        MoudleUtils.textViewSetText(tv_return_tel, info.getTel());
        MoudleUtils.textViewSetText(tv_return_desposit, "￥" + info.getAmount());
        MoudleUtils.textViewSetText(tv_return_name, info.getRelname());
        btn_return_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.mBluetoothGatt == null && AppConfig.bluetoothGattServices == null) {
                    initUpData();//提交申请
                } else {
                    ToastUtils.showShortNotInternet("手环使用中，不可进行退押金操作");
                }
            }
        });
    }

    private void initId() {
        ll_have = (LinearLayout) findViewById(R.id.ll_have);
        tv_return_member_id = (TextView) findViewById(R.id.tv_return_member_id);
        tv_return_name = (TextView) findViewById(R.id.tv_return_name);
        tv_return_tel = (TextView) findViewById(R.id.tv_return_tel);
        tv_return_desposit = (TextView) findViewById(R.id.tv_return_desposit);
        btn_return_card = (Button) findViewById(R.id.btn_return_card);
        tv_zan_wu = (TextView) findViewById(R.id.tv_zan_wu);
        tv_more = (TextView) findViewById(R.id.tv_more);
        dialog = new ProgressDialog(this);
    }

    private void initUpData() {
        MoudleUtils.dialogShow(dialog);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<BindacceptBean> callBack = restApi.refund(user_id + "", token);

        callBack.enqueue(new Callback<BindacceptBean>() {
            @Override
            public void onResponse(Call<BindacceptBean> call, Response<BindacceptBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                        //成功操作
                        finish();
                        startActivity(new Intent().setClass(ReturnCardActivity.this, BackStateActivity.class));
                    } else {
                        ToastUtils.showShort(ReturnCardActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<BindacceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dialog);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("押金管理");
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
