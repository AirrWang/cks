package com.ckjs.ck.Android.MineModule.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.OrderInfoShopBean;
import com.ckjs.ck.Bean.ReceiptBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
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

public class OrderDetailActivity extends AppCompatActivity {

    private String type;
    private ProgressDialog dialog;
    private LinearLayout order_adressdetail;
    private LinearLayout ll_yunfei;
    private String order_id="";
    private TextView shouhuo_order_name;
    private TextView shouhuo_order_tel;
    private TextView shouhuo_order_adress;
    private TextView order_detail_status;
    private TextView order_detail_ck_id;
    private TextView order_detail_express;
    private TextView order_detail_expressid;
    private SimpleDraweeView mine_order_detail_pic;
    private TextView tv_order_num;
    private TextView order_detail_name;
    private TextView tv_yunfei;
    private TextView tv_totalprice;
    private TextView tv_amounte;
    private TextView tv_sure_order;
    private AlertDialog alertDialog;
    private OrderInfoShopBean.OrderInfoShopDetailBean info;
    private LinearLayout ll_jump;
    private LinearLayout ll_sure_order;
    private LinearLayout ll_order_detail_express;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Intent intent=getIntent();
        type = intent.getStringExtra("type");
        order_id = intent.getStringExtra("order_id");
        initID();
        initUI();
        initToolbar();
        initTask();
    }

    private void initTask() {
        MoudleUtils.dialogShow(dialog);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<OrderInfoShopBean> callBack = restApi.orderinfo(user_id+"",token,order_id);

        callBack.enqueue(new Callback<OrderInfoShopBean>() {
            @Override
            public void onResponse(Call<OrderInfoShopBean> call, Response<OrderInfoShopBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo()!= null) {
                            initSet(response.body().getInfo());
                            info = response.body().getInfo();
                            tv_sure_order.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initToSure();
                                }
                            });
                        }
                    } else {
                        ToastUtils.showShort(OrderDetailActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<OrderInfoShopBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dialog);
            }
        });

    }

    private void initSet(OrderInfoShopBean.OrderInfoShopDetailBean info) {
        if (info.getShop_type().equals("1")) {
            shouhuo_order_name.setText("收货人:" + info.getName());
            shouhuo_order_tel.setText(info.getTel());
            shouhuo_order_adress.setText("收货地址:" + info.getAdress());
            tv_yunfei.setText("￥"+info.getFreight());
            tv_order_num.setText("共"+info.getNum()+"件商品");
        }else if (info.getShop_type().equals("2")) {
            MoudleUtils.textViewSetText(tv_order_num, "共" + info.getNum() + "张卡" );
        } else if (info.getShop_type().equals("3")) {
            MoudleUtils.textViewSetText(tv_order_num, "共" + info.getNum() + "节课程");
        }
        if (info.getOrderstatus() != null) {
            if (info.getOrderstatus().equals("1")) {
                MoudleUtils.textViewSetText(order_detail_status, "等待发货");
            } else if (info.getOrderstatus().equals("2")) {
                MoudleUtils.textViewSetText(order_detail_status, "等待收货");
            } else if (info.getOrderstatus().equals("3")) {
                MoudleUtils.textViewSetText(order_detail_status, "订单完成");
                MoudleUtils.viewGone(ll_sure_order);
            }
        }
        order_detail_ck_id.setText(info.getOrder_id());
        order_detail_express.setText(info.getExpress());
        order_detail_expressid.setText(info.getExpressid());
        FrescoUtils.setImage(mine_order_detail_pic, AppConfig.url_jszd + info.getPicture());

        order_detail_name.setText(info.getShopname());
        tv_totalprice.setText("￥"+info.getOrderamount());
        tv_amounte.setText("￥"+info.getAmount());
    }

    private void initID() {
        order_adressdetail = (LinearLayout) findViewById(R.id.order_adressdetail);
        ll_yunfei = (LinearLayout) findViewById(R.id.ll_yunfei);
        shouhuo_order_name = (TextView) findViewById(R.id.shouhuo_order_name);
        shouhuo_order_tel = (TextView) findViewById(R.id.shouhuo_order_tel);
        shouhuo_order_adress = (TextView) findViewById(R.id.shouhuo_order_adress);
        order_detail_status = (TextView) findViewById(R.id.order_detail_status);
        order_detail_ck_id = (TextView) findViewById(R.id.order_detail_ck_id);
        order_detail_express = (TextView) findViewById(R.id.order_detail_express);
        order_detail_expressid = (TextView) findViewById(R.id.order_detail_expressid);
        mine_order_detail_pic = (SimpleDraweeView) findViewById(R.id.mine_order_detail_pic);
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        order_detail_name = (TextView) findViewById(R.id.order_detail_name);
        tv_yunfei = (TextView) findViewById(R.id.tv_yunfei);
        tv_totalprice = (TextView) findViewById(R.id.tv_totalprice);
        tv_amounte = (TextView) findViewById(R.id.tv_amounte);
        tv_sure_order = (TextView) findViewById(R.id.tv_sure_order);
        ll_jump = (LinearLayout) findViewById(R.id.ll_jump);
        ll_sure_order = (LinearLayout) findViewById(R.id.ll_sure_order);
        ll_order_detail_express = (LinearLayout) findViewById(R.id.ll_order_detail_express);
        dialog=new ProgressDialog(this);
    }

    private void initToSure() {
        if (info.getShop_type().equals("1")){
                    alertDialog = new AlertDialog.Builder(OrderDetailActivity.this)
                            .setMessage("确认后不可更改")
                            .setPositiveButton("确定收货", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    initToInfoTask();
                                }
                            }).setNegativeButton("暂不确认",
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
        }else {
            alertDialog = new AlertDialog.Builder(this)
                    .setMessage("已确认在门店完成交易")
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
    }

    private void initUI() {
        if (type!=null) {
            if (type.equals("2")||type.equals("3")) {
                MoudleUtils.viewGone(order_adressdetail);
                MoudleUtils.viewGone(ll_yunfei);
                MoudleUtils.viewGone(ll_order_detail_express);
            }
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("订单详情");
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

    /**
     * 确认收货 接口
     *
     */

    private void initToInfoTask() {
        dialog = new ProgressDialog(this);
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(OrderDetailActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(OrderDetailActivity.this, "token", "");

        Call<ReceiptBean> callBack = restApi.receipt(user_id, token, info.getOrder_id());
        callBack.enqueue(new Callback<ReceiptBean>() {
            @Override
            public void onResponse(Call<ReceiptBean> call, Response<ReceiptBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        finish();
                        ToastUtils.show(OrderDetailActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(OrderDetailActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(OrderDetailActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }
            @Override
            public void onFailure(Call<ReceiptBean> call, Throwable t) {
                MoudleUtils.toChekWifi(OrderDetailActivity.this);
                MoudleUtils.dialogDismiss(dialog);
            }
        });
    }
}
