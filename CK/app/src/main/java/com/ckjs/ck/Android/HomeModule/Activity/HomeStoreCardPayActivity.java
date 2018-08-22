package com.ckjs.ck.Android.HomeModule.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.AlipayBean;
import com.ckjs.ck.Bean.OrderQueryBean;
import com.ckjs.ck.Bean.PrivilegeInfoBean;
import com.ckjs.ck.Bean.SubmitOrderBean;
import com.ckjs.ck.Bean.WeiPayBean;
import com.ckjs.ck.Manager.NotifyWxPayManager;
import com.ckjs.ck.Message.NotifyWxpayFinishendMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.PayTool.PayResult;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ckjs.ck.Tool.MoudleUtils.initNullPayInterfosion;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class HomeStoreCardPayActivity extends AppCompatActivity implements NotifyWxpayFinishendMessage, View.OnClickListener {


    private String token = "";
    private int pay = 1;
    private static final int SDK_PAY_FLAG = 1;
    private IWXAPI api;
    private String sign = "";
    private int num = 1;
    /**
     * 支付宝的异步支付结果
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        getZhifubaoResult();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShortNotInternet("支付失败：操作已经取消");

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private TextView text_num;
    private Button btn_num_up;
    private Button btn_num_down;
    private Button store_card_submit;
    private TextView store_card_pay_manual;
    private TextView store_coach_pay_price;
    private TextView store_card_pay_total;
    private TextView store_card_pay_total_2;
    private PrivilegeInfoBean privilegeinfo;
    private String name;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_store_card_pay);
        initNullPayInterfosion();
        initId();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        id = Integer.parseInt(intent.getStringExtra("id"));
        initToolbar();
        initData();
        SPUtils.put(this, "pay_type", "shop");
    }


    /**
     * 通过接口获取数据
     */
    private void initData() {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        //TODO
        Call<PrivilegeInfoBean> callBack = restApi.privilegeinfo(id);

        callBack.enqueue(new Callback<PrivilegeInfoBean>() {
            @Override
            public void onResponse(Call<PrivilegeInfoBean> call, Response<PrivilegeInfoBean> response) {
                privilegeinfo = response.body();
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        initUI(privilegeinfo);
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(HomeStoreCardPayActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.show(HomeStoreCardPayActivity.this, response.body().getMsg(), 0);
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<PrivilegeInfoBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(builder);
                MoudleUtils.toChekWifi(HomeStoreCardPayActivity.this);
            }
        });
    }

    /**
     * 拿到数据更新UI
     *
     * @param privilegeinfo
     */
    private void initUI(PrivilegeInfoBean privilegeinfo) {
        if (privilegeinfo.getInfo() != null) {
            store_card_pay_manual.setText(privilegeinfo.getInfo().getManual());
            store_coach_pay_price.setText("￥" + privilegeinfo.getInfo().getPrice());
            store_card_pay_total.setText("￥" + privilegeinfo.getInfo().getPrice());
            store_card_pay_total_2.setText("￥" + privilegeinfo.getInfo().getPrice());
            btn_num_up.setClickable(true);
        }
    }

    private void initId() {

        store_coach_pay_price = (TextView) findViewById(R.id.store_coach_pay_price);
        store_card_pay_total = (TextView) findViewById(R.id.store_card_pay_total);
        store_card_pay_total_2 = (TextView) findViewById(R.id.store_card_pay_total_2);
        store_card_pay_manual = (TextView) findViewById(R.id.store_card_pay_manual);
        btn_num_down = (Button) findViewById(R.id.btn_num_down);
        btn_num_up = (Button) findViewById(R.id.btn_num_up);
        text_num = (TextView) findViewById(R.id.text_num);

        //        submit_order_card = (Button) findViewById(R.id.submit_order_card);
        //        RadioButton card_zhifibao = (RadioButton) findViewById(R.id.card_zhifibao);
        //        RadioButton card_weixin = (RadioButton) findViewById(R.id.card_weixin);

        store_card_submit = (Button) findViewById(R.id.store_card_submit);
        RadioButton card_zhifibao = (RadioButton) findViewById(R.id.card_zhifibao);
        RadioButton card_weixin = (RadioButton) findViewById(R.id.card_weixin);


        card_zhifibao.setOnClickListener(this);
        card_weixin.setOnClickListener(this);
        store_card_submit.setOnClickListener(this);
        btn_num_down.setOnClickListener(this);
        btn_num_up.setOnClickListener(this);
        NotifyWxPayManager.getInstance().setNotifyMessage(this);
    }

    /**
     * 支付宝支付成功结果接口查询
     */
    private void getZhifubaoResult() {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");

        Call<OrderQueryBean> callBack = restApi.aliquery(token, (Integer) SPUtils.get(HomeStoreCardPayActivity.this, "user_id", 0), AppConfig.order_id, "shop");

        callBack.enqueue(new Callback<OrderQueryBean>() {
            @Override
            public void onResponse(Call<OrderQueryBean> call, Response<OrderQueryBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("0")) {
                        toFinish(response.body().getMsg());
                    } else if (response.body().getStatus().equals("1")) {
                        toFinish("订单已支付完成，即可到我→个人中心→我的订单中查看详情");
                    } else if (response.body().getStatus().equals("2")) {
                        MoudleUtils.initStatusTwo(HomeStoreCardPayActivity.this, true);
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<OrderQueryBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(builder);
                MoudleUtils.toChekWifi(HomeStoreCardPayActivity.this);

            }

        });

    }

    AlertDialog alertDialogZfb;

    private void toFinish(String msg) {
        //TODO
        alertDialogZfb = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }

                }).create(); // 创建对话框
        alertDialogZfb.setCancelable(false);
        if (alertDialogZfb != null) {
            if (!alertDialogZfb.isShowing()) {
                alertDialogZfb.show(); // 显示
            }
        }
    }

    /**
     * toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(name);
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

    KyLoadingBuilder builder;

    /**
     * 提交订单逻辑处理
     */
    private void toSubmitOrder() {
        builder = new KyLoadingBuilder(this);
        //微信支付掉起接口
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        //TODO
        token = (String) SPUtils.get(this, "token", "");
        Call<SubmitOrderBean> callBack = restApi.submitorderjsf((int) SPUtils.get(this, "user_id", 0), token, id + "", num + "", "2");

        callBack.enqueue(new Callback<SubmitOrderBean>() {
            @Override
            public void onResponse(Call<SubmitOrderBean> call, Response<SubmitOrderBean> response) {
                SubmitOrderBean submit_orderbean = response.body();

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (submit_orderbean.getInfo() != null) {
                            AppConfig.order_id = submit_orderbean.getInfo().getOrder_id();
                            toPay(AppConfig.order_id);
                        } else {
                            MoudleUtils.kyloadingDismiss(builder);
                        }
                    } else if (response.body().getStatus().equals("0")) {
                        MoudleUtils.kyloadingDismiss(builder);
                        ToastUtils.showShortNotInternet(response.body().getMsg());
                    } else if (response.body().getStatus().equals("2")) {
                        MoudleUtils.kyloadingDismiss(builder);
                        ToastUtils.showShort(HomeStoreCardPayActivity.this, response.body().getMsg());
                    }
                } else {
                    MoudleUtils.kyloadingDismiss(builder);
                }
            }

            @Override
            public void onFailure(Call<SubmitOrderBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(builder);
                MoudleUtils.toChekWifi(HomeStoreCardPayActivity.this);
            }
        });
    }

    //选择支付方式
    private void toPay(String order_id) {
        if (pay == 1) {
            toZhifubao(order_id);
        } else {
            toWeixin(order_id);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initNullPayInterfosion();
    }

    private void toZhifubao(String order_id) {
        //支付宝调起接口

        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<AlipayBean> callBack = restApi.alipay(token, (Integer) SPUtils.get(HomeStoreCardPayActivity.this, "user_id", 0), order_id, "shop");

        callBack.enqueue(new Callback<AlipayBean>() {
            @Override
            public void onResponse(Call<AlipayBean> call, Response<AlipayBean> response) {

                AlipayBean order_infobean = response.body();

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        //掉起支付宝
                        cacthZhifubao(order_infobean);
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(HomeStoreCardPayActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(HomeStoreCardPayActivity.this, response.body().getMsg());

                    }
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<AlipayBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HomeStoreCardPayActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });
    }

    /**
     * 支付宝后台掉起
     *
     * @param order_infobean
     */
    private void cacthZhifubao(AlipayBean order_infobean) {
        //        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
        //        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        sign = order_infobean.getInfo().getDate();
        //        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(HomeStoreCardPayActivity.this);
                Map<String, String> result = alipay.payV2(sign, true);
                //                LogUtils.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void toWeixin(String order_id) {

        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<WeiPayBean> callBack = restApi.weipay(token, (Integer) SPUtils.get(HomeStoreCardPayActivity.this, "user_id", 0), order_id, "shop");

        callBack.enqueue(new Callback<WeiPayBean>() {
            @Override
            public void onResponse(Call<WeiPayBean> call, Response<WeiPayBean> response) {

                WeiPayBean order_infobean = response.body();

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        //掉起微信
                        //                        Log.i(TAG, "getSign: " + order_infobean.getInfo().getSign());
                        catchWeixin(order_infobean);
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(HomeStoreCardPayActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(HomeStoreCardPayActivity.this, response.body().getMsg());

                    }
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<WeiPayBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HomeStoreCardPayActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });
    }

    /**
     * 微信调起官方接口
     *
     * @param order_infobean
     */
    private void catchWeixin(WeiPayBean order_infobean) {

        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(order_infobean.getInfo().getAppid());

        isWXAppInstalledAndSupported();

        if (api != null) {
            PayReq request = new PayReq();
            request.appId = order_infobean.getInfo().getAppid();
            request.partnerId = order_infobean.getInfo().getPartnerid();
            request.prepayId = order_infobean.getInfo().getPrepayid();
            request.packageValue = order_infobean.getInfo().getWpackage();
            request.nonceStr = order_infobean.getInfo().getNoncestr();
            request.timeStamp = order_infobean.getInfo().getTimestamp();
            request.sign = order_infobean.getInfo().getSign();
            api.sendReq(request);
            //            Log.i(TAG, "api: " + api.toString());
            //            Log.e(TAG, "checkArgs=" + request.checkArgs());
            //            Log.d(AppConfig.TAG, "request.sign:" + request.sign);
            //查询结果
        }
    }

    /**
     * 判断手机是否支持微信支付
     */
    private void isWXAppInstalledAndSupported() {
        if (!api.isWXAppInstalled()) {
            ToastUtils.showShortNotInternet("请先安装微信客户端");
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtils.showShortNotInternet("因为手机权限原因，请先开启微信客户端");
            return;
        }
    }

    @Override
    public void sendMWxpayFinishendFlag(boolean flag, String status) {
        if (flag) {
            if (status.equals("1")) {
                toFinish("订单已支付完成，即可到我→个人中心→我的订单中查看详情");
            } else if (status.equals("0")) {

            } else if (status.equals("2")) {

            }
        }
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_num_down:
                num--;
                if (num >= 1) {
                    btn_num_down.setClickable(true);
                    text_num.setText(num + "");
                    String s = MoudleUtils.roundDouble(privilegeinfo.getInfo().getPrice() * num, 2);
                    store_card_pay_total.setText("￥" + s);
                    store_card_pay_total_2.setText("￥" + s);
                } else {
                    btn_num_down.setClickable(false);
                }
                break;
            case R.id.btn_num_up:
                num++;
                btn_num_down.setClickable(true);
                text_num.setText(num + "");
                String s = MoudleUtils.roundDouble(privilegeinfo.getInfo().getPrice() * num, 2);
                store_card_pay_total.setText("￥" + s);
                store_card_pay_total_2.setText("￥" + s);
                break;
            case R.id.card_zhifibao:
                pay = 1;
                break;
            case R.id.card_weixin:
                pay = 2;
                break;
            case R.id.store_card_submit:
                bind();
                break;
        }
    }

    private void bind() {

        String token = (String) SPUtils.get("token", "");
        if (token.equals("")) {
            MoudleUtils.initToLogin(HomeStoreCardPayActivity.this);
        } else {
            try {
                initToBd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void initToBd() {
        if ((SPUtils.get(HomeStoreCardPayActivity.this, "vip", "").equals("1"))) {
            if ((SPUtils.get(HomeStoreCardPayActivity.this, "type", "") + "").equals("1")) {
                startActivity(new Intent().setClass(HomeStoreCardPayActivity.this, RealNameYsActivity.class));
            } else {
                startActivity(new Intent().setClass(HomeStoreCardPayActivity.this, RealNameActivity.class));
            }
        } else {
            toSubmitOrder();
        }
    }

}
