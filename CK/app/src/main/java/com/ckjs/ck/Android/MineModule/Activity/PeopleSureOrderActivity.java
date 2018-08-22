package com.ckjs.ck.Android.MineModule.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.AlipayBean;
import com.ckjs.ck.Bean.OrderQueryBean;
import com.ckjs.ck.Bean.WeiPayBean;
import com.ckjs.ck.Manager.NotifyWxPayManager;
import com.ckjs.ck.Message.NotifyWxpayFinishendMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.PayTool.PayResult;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ckjs.ck.Tool.MoudleUtils.initNullPayInterfosion;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class PeopleSureOrderActivity extends AppCompatActivity implements View.OnClickListener, NotifyWxpayFinishendMessage {

    private int way = 1;
    private RadioButton sijiao_sure_zhifibao;
    private RadioButton sijiao_sure_weixin;

    String sign = "";

    private IWXAPI api;

    private static final int SDK_PAY_FLAG = 1;


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
    private String id;
    private TextView people_sure_name;
    private String name;
    private String sex;
    private SimpleDraweeView people_sure_sex;
    private TextView people_sure_price;
    private String price;
    private TextView people_sure_type;
    private String type;
    private String status;
    private TextView people_sure_status;
    private TextView people_sure_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_sure_order);
        initNullPayInterfosion();
        initToolbar();
        initId();
        SPUtils.put(this, "pay_type", "coach");
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        sex = intent.getStringExtra("sex");
        price = intent.getStringExtra("price");
        type = intent.getStringExtra("sijiaotype");
        status = intent.getStringExtra("status");
        initUI();

    }


    /**
     * 一开始界面设置
     */
    private void initUI() {
        people_sure_name.setText(name);
        if (sex.equals("2")) {
            FrescoUtils.setImage(people_sure_sex, AppConfig.res + R.drawable.my_girl);
        } else if (sex.equals("1")) {
            FrescoUtils.setImage(people_sure_sex, AppConfig.res + R.drawable.my_boy);
        } else {
            FrescoUtils.setImage(people_sure_sex, AppConfig.res + R.drawable.my_boy);
        }
        people_sure_price.setText(price);
        people_sure_total.setText("合计：￥" + price);
        if (type.equals("distance")) {
            people_sure_type.setText("视频指导");
        } else {
            people_sure_type.setText("上门指导");
        }
        if (status.equals("1")) {
            people_sure_status.setText("教练未确认");
        } else if (status.equals("2")) {
            people_sure_status.setText("教练已确认");
        } else if (status.equals("3")) {
            people_sure_status.setText("用户已支付");
        }
    }

    private void initId() {
        NotifyWxPayManager.getInstance().setNotifyMessage(this);
        people_sure_total = (TextView) findViewById(R.id.people_sure_total);
        people_sure_status = (TextView) findViewById(R.id.people_sure_status);
        people_sure_type = (TextView) findViewById(R.id.people_sure_type);
        people_sure_price = (TextView) findViewById(R.id.people_sure_price);
        people_sure_name = (TextView) findViewById(R.id.people_sure_name);
        people_sure_sex = (SimpleDraweeView) findViewById(R.id.people_sure_sex);
        sijiao_sure_zhifibao = (RadioButton) findViewById(R.id.sijiao_sure_zhifibao);
        sijiao_sure_zhifibao.setOnClickListener(this);
        sijiao_sure_weixin = (RadioButton) findViewById(R.id.sijiao_sure_weixin);
        sijiao_sure_weixin.setOnClickListener(this);
        Button submit_order_sijiao = (Button) findViewById(R.id.submit_order_sijiao);
        submit_order_sijiao.setOnClickListener(this);
    }


    /**
     * toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("私教支付");
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
            case R.id.sijiao_sure_zhifibao:
                way = 1;
                break;
            case R.id.sijiao_sure_weixin:
                way = 2;
                break;
            case R.id.submit_order_sijiao:
                AppConfig.order_id = id;
                if (way == 1) {
                    toZhiFuBao(AppConfig.order_id);
                } else if (way == 2) {
                    toWeixin(AppConfig.order_id);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initNullPayInterfosion();
    }

    /**
     * 得到支付宝支付结果
     */
    private void getZhifubaoResult() {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");
        //TODO
        Call<OrderQueryBean> callBack = restApi.aliquery(token, (Integer) SPUtils.get(PeopleSureOrderActivity.this, "user_id", 0), id, "coach");

        callBack.enqueue(new Callback<OrderQueryBean>() {
            @Override
            public void onResponse(Call<OrderQueryBean> call, Response<OrderQueryBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(PeopleSureOrderActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("1")) {

                        ToastUtils.show(PeopleSureOrderActivity.this, response.body().getMsg(), 0);
                        finish();
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(PeopleSureOrderActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<OrderQueryBean> call, Throwable t) {
                MoudleUtils.toChekWifi(PeopleSureOrderActivity.this);
            }
        });

    }

    private void toWeixin(String order_id) {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        //微信支付掉起接口
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");
        Call<WeiPayBean> callBack = restApi.weipay(token, (Integer) SPUtils.get(PeopleSureOrderActivity.this, "user_id", 0), order_id, "coach");

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

                        ToastUtils.show(PeopleSureOrderActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(PeopleSureOrderActivity.this, response.body().getMsg());

                    }
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<WeiPayBean> call, Throwable t) {
                MoudleUtils.toChekWifi(PeopleSureOrderActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });
    }

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
            //查询结果
        }
    }

    /**
     * 判断手机是否支持微信支付
     */
    private void isWXAppInstalledAndSupported() {

        if (!api.isWXAppInstalled()) {
            ToastUtils.show(PeopleSureOrderActivity.this, "请先安装微信客户端", 0);
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtils.show(PeopleSureOrderActivity.this, "因为手机权限原因，请先开启微信客户端", 0);
            return;
        }
    }

    private void toZhiFuBao(String order_id) {
        //支付宝调起接口
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");
        Call<AlipayBean> callBack = restApi.alipay(token, (Integer) SPUtils.get(PeopleSureOrderActivity.this, "user_id", 0), order_id, "coach");

        callBack.enqueue(new Callback<AlipayBean>() {
            @Override
            public void onResponse(Call<AlipayBean> call, Response<AlipayBean> response) {

                AlipayBean order_infobean = response.body();

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        //掉起支付宝
                        cacthZhifubao(order_infobean);
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(PeopleSureOrderActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(PeopleSureOrderActivity.this, order_infobean.getMsg());

                    }
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<AlipayBean> call, Throwable t) {
                MoudleUtils.toChekWifi(PeopleSureOrderActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });
    }

    private void cacthZhifubao(AlipayBean order_infobean) {
        //        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
        //        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        sign = order_infobean.getInfo().getDate();
        //        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PeopleSureOrderActivity.this);
                Map<String, String> result = alipay.payV2(sign, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void sendMWxpayFinishendFlag(boolean flag, String status) {
        if (flag) {
            if (status.equals("1")) {
                finish();
            } else if (status.equals("0")) {

            } else if (status.equals("2")) {

            }
        }
    }
}
