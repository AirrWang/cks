package com.ckjs.ck.Android.HomeModule.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.AlipayBean;
import com.ckjs.ck.Bean.BandBean;
import com.ckjs.ck.Bean.OrderQueryBean;
import com.ckjs.ck.Bean.RentInfoBean;
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
import com.ckjs.ck.Tool.SavaDataLocalUtils;
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

public class DepositActivity extends AppCompatActivity implements View.OnClickListener, NotifyWxpayFinishendMessage {

    private RadioButton desposit_zhifibao;
    private RadioButton desposit_weixin;
    private int pay = 1;
    private Button submit_order_desposit;
    private String ordernum;
    private String token = "";
    private TextView tv_desposit_userid;
    private TextView tv_desposit_name;
    private TextView tv_desposit_tel;
    private TextView tv_desposit;
    private TextView order_desposit_price_total;
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
                        //该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShortNotInternet("支付失败：操作已经取消");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private ProgressDialog dialig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desposit);
        initNullPayInterfosion();
        Intent intent = getIntent();
        ordernum = intent.getStringExtra("ordernum");
        bind = intent.getStringExtra("bind");
        initId();
        initToolbar();
        initTaskGet();
        SPUtils.put(this, "pay_type", "shop");
    }

    private void getZhifubaoResult() {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");

        Call<OrderQueryBean> callBack = restApi.aliquery(token, (Integer) SPUtils.get(DepositActivity.this, "user_id", 0), AppConfig.order_id, "shop");

        callBack.enqueue(new Callback<OrderQueryBean>() {
            @Override
            public void onResponse(Call<OrderQueryBean> call, Response<OrderQueryBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(DepositActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("1")) {
                        initToBind(bind);
                        ToastUtils.show(DepositActivity.this, response.body().getMsg(), 0);


                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(DepositActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<OrderQueryBean> call, Throwable t) {
                MoudleUtils.toChekWifi(DepositActivity.this);
            }
        });

    }

    void initBind(String band) {
        if (band == null || band.equals("")) {
            finish();
            ToastUtils.showShortNotInternet("绑定时出现未知错误！");
            return;
        }
    }

    private void initToBindTask(final Context context, final String bind) {
        int userid = (int) SPUtils.get(context, "user_id", 0);
        String token = (String) SPUtils.get(context, "token", "");
        Call<BandBean> bandstatusBean = RetrofitUtils.retrofit.create(NpApi.class).band(token, userid, bind);
        bandstatusBean.enqueue(new Callback<BandBean>() {
            @Override
            public void onResponse(Call<BandBean> call, Response<BandBean> response) {
                MoudleUtils.dialogDismiss(dialig);
                BandBean bandstatusBean = response.body();
                if (bandstatusBean == null)
                    return;
                //status|状态|0：绑定失败；1：绑定成功；2：token错误；

                String status = bandstatusBean.getStatus();
                String msg = bandstatusBean.getMsg();
                if (status == null)
                    return;
                if (status.equals("0") || status.equals("2")) {
                    ToastUtils.showShort(DepositActivity.this, msg);
                    return;
                }
                //type1正常2租赁
                if (status.equals("1")) {
                    SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "isbind", bandstatusBean.getInfo().getIsbind());
                    initBind(bind);
                    initToBindEd(bind);
                }
                finish();
            }

            @Override
            public void onFailure(Call<BandBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialig);
                MoudleUtils.toChekWifi();
                finish();
            }
        });
    }

    void initToBindEd(String bind) {
        final Intent intent = new Intent(DepositActivity.this, DeviceControlActivity.class);
        intent.putExtra(AppConfig.EXTRAS_DEVICE_ADDRESS, bind);
        startActivity(intent);
    }

    void initToBind(String bind) {
        initBind(bind);
        MoudleUtils.dialogShow(dialig);
        initToBindTask(DepositActivity.this, bind);
    }

    private void initId() {
        desposit_zhifibao = (RadioButton) findViewById(R.id.desposit_zhifibao);
        desposit_weixin = (RadioButton) findViewById(R.id.desposit_weixin);
        submit_order_desposit = (Button) findViewById(R.id.submit_order_desposit);
        tv_desposit_userid = (TextView) findViewById(R.id.tv_desposit_userid);
        tv_desposit_name = (TextView) findViewById(R.id.tv_desposit_name);
        tv_desposit_tel = (TextView) findViewById(R.id.tv_desposit_tel);
        tv_desposit = (TextView) findViewById(R.id.tv_desposit);
        order_desposit_price_total = (TextView) findViewById(R.id.order_desposit_price_total);

        submit_order_desposit.setOnClickListener(this);
        desposit_zhifibao.setOnClickListener(this);
        desposit_weixin.setOnClickListener(this);
        NotifyWxPayManager.getInstance().setNotifyMessage(this);
        dialig = new ProgressDialog(this);
    }

    /**
     * 得到订单信息
     */

    private void initTaskGet() {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        //微信支付掉起接口
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        token = (String) SPUtils.get(this, "token", "");

        if (token.equals("")) {

        } else {
            Call<RentInfoBean> callBack = restApi.rentinfo((int) SPUtils.get(this, "user_id", 0) + "", token, ordernum);

            callBack.enqueue(new Callback<RentInfoBean>() {
                @Override
                public void onResponse(Call<RentInfoBean> call, Response<RentInfoBean> response) {

                    if (response.body() != null) {
                        RentInfoBean rentInfoBean = response.body();
                        if (response.body().getStatus().equals("1")) {
                            if (response.body().getInfo() != null) {
                                if (rentInfoBean.getInfo() != null) {
                                    AppConfig.order_id = rentInfoBean.getInfo().getOrder_id();
                                    initUI(rentInfoBean);
                                }
                            } else {
                                ToastUtils.showShortNotInternet("暂无数据，请稍后重试！");
                            }
                        } else if (response.body().getStatus().equals("0")) {
                            ToastUtils.show(DepositActivity.this, response.body().getMsg(), 0);
                        } else if (response.body().getStatus().equals("2")) {
                            ToastUtils.showShort(DepositActivity.this, response.body().getMsg());
                        }
                    }
                    MoudleUtils.kyloadingDismiss(builder);
                }

                @Override
                public void onFailure(Call<RentInfoBean> call, Throwable t) {
                    MoudleUtils.toChekWifi(DepositActivity.this);
                    MoudleUtils.kyloadingDismiss(builder);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initNullPayInterfosion();
    }


    private void initUI(RentInfoBean rentInfoBean) {
        tv_desposit_userid.setText("会员ID:" + rentInfoBean.getInfo().getUser_id());
        tv_desposit_name.setText("姓名:" + rentInfoBean.getInfo().getRelname());
        tv_desposit_tel.setText("电话:" + rentInfoBean.getInfo().getTel());
        tv_desposit.setText("￥" + rentInfoBean.getInfo().getAmount());
        order_desposit_price_total.setText("合计:￥" + rentInfoBean.getInfo().getAmount());
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("押金支付");
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
            case R.id.desposit_zhifibao:
                pay = 1;
                break;
            case R.id.desposit_weixin:
                pay = 2;
                break;
            case R.id.submit_order_desposit:
                if (token.equals("")) {
                    MoudleUtils.initStatusTwo(DepositActivity.this, true);
                } else {
                    if (AppConfig.order_id != null && !AppConfig.order_id.equals("")) {
                        if (pay == 1) {
                            toZhifubao(AppConfig.order_id);
                        } else {
                            toWeixin(AppConfig.order_id);
                        }
                    } else {
                        finish();
                        ToastUtils.showShortNotInternet("订单异常,支付失败");
                    }

                }
                break;
        }
    }

    private void toWeixin(String id) {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        //微信支付掉起接口
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<WeiPayBean> callBack = restApi.weipay(token, (Integer) SPUtils.get(DepositActivity.this, "user_id", 0), id, "shop");

        callBack.enqueue(new Callback<WeiPayBean>() {
            @Override
            public void onResponse(Call<WeiPayBean> call, Response<WeiPayBean> response) {

                WeiPayBean order_infobean = response.body();

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        //掉起微信
                        catchWeixin(order_infobean);
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(DepositActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(DepositActivity.this, response.body().getMsg());

                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<WeiPayBean> call, Throwable t) {
                MoudleUtils.toChekWifi(DepositActivity.this);
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
            ToastUtils.show(DepositActivity.this, "请先安装微信客户端", 0);
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtils.show(DepositActivity.this, "因为手机权限原因，请先开启微信客户端", 0);
            return;
        }
    }

    private void toZhifubao(String id) {
        //支付宝调起接口
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<AlipayBean> callBack = restApi.alipay(token, (Integer) SPUtils.get(DepositActivity.this, "user_id", 0), id, "shop");

        callBack.enqueue(new Callback<AlipayBean>() {
            @Override
            public void onResponse(Call<AlipayBean> call, Response<AlipayBean> response) {

                if (response.body() != null) {
                    AlipayBean order_infobean = response.body();
                    if (response.body().getStatus().equals("1")) {
                        //掉起支付宝
                        cacthZhifubao(order_infobean);
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(DepositActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(DepositActivity.this, response.body().getMsg());

                    }
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<AlipayBean> call, Throwable t) {
                MoudleUtils.toChekWifi(DepositActivity.this);
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
                PayTask alipay = new PayTask(DepositActivity.this);
                Map<String, String> result = alipay.payV2(sign, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private String bind;

    @Override
    public void sendMWxpayFinishendFlag(boolean flag, String status) {
        if (flag) {
            if (status.equals("1")) {
                initToBind(bind);
            } else if (status.equals("0")) {

            } else if (status.equals("2")) {

            }
        }
    }
}
