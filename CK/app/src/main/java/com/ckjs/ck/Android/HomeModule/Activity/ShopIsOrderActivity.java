package com.ckjs.ck.Android.HomeModule.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.AlipayBean;
import com.ckjs.ck.Bean.GetShopAdressBean;
import com.ckjs.ck.Bean.GetShopInfoBean;
import com.ckjs.ck.Bean.OrderQueryBean;
import com.ckjs.ck.Bean.SubmitOrderBean;
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
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ShopIsOrderActivity extends AppCompatActivity implements View.OnClickListener, NotifyWxpayFinishendMessage {

    private TextView order_shop_name;
    private SimpleDraweeView order_shop_pic;
    private TextView order_shop_price;
    private TextView order_shop_price1;
    private TextView order_shop_price2;
    private TextView order_shop_price3;
    private LinearLayout updAdress;
    private Button submitOrder;
    private GetShopAdressBean shopadressbean;
    private String shop_id;
    private String token = "";
    private GetShopInfoBean shop_info;
    private LinearLayout ll_shop_adressdetail;
    private TextView shop_name;
    private TextView shop_tel;
    private TextView shop_adress;
    private int pay = 1;
    private RadioButton zhifubao;
    private RadioButton weixin;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_is_order);
        initNullPayInterfosion();
        initId();
        initToolbar();
        initTaskGet();
        initGetAdress();
        SPUtils.put(this, "pay_type", "shop");
    }


    private void getZhifubaoResult() {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");

        Call<OrderQueryBean> callBack = restApi.aliquery(token, (Integer) SPUtils.get(ShopIsOrderActivity.this, "user_id", 0), AppConfig.order_id, "shop");

        callBack.enqueue(new Callback<OrderQueryBean>() {
            @Override
            public void onResponse(Call<OrderQueryBean> call, Response<OrderQueryBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("0")) {
                        toFinish(response.body().getMsg());
                    } else if (response.body().getStatus().equals("1")) {
                        toFinish("订单已支付完成，即可到我→个人中心→我的订单中查看详情");
                    } else if (response.body().getStatus().equals("2")) {
                        MoudleUtils.initStatusTwo(ShopIsOrderActivity.this, true);
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<OrderQueryBean> call, Throwable t) {
                MoudleUtils.toChekWifi(ShopIsOrderActivity.this);
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

    private void initGetAdress() {
        WfApi userInfoApi = RetrofitUtils.retrofit.create(WfApi.class);
        token = (String) SPUtils.get(this, "token", "");

        if (token.equals("")) {

        } else {
            Call<GetShopAdressBean> callBack = userInfoApi.getadress((int) SPUtils.get(this, "user_id", 0) + "", token);

            callBack.enqueue(new Callback<GetShopAdressBean>() {
                @Override
                public void onResponse(Call<GetShopAdressBean> call, Response<GetShopAdressBean> response) {
                    shopadressbean = response.body();
                    if (shopadressbean.getInfo() != null) {
                        if (shopadressbean.getStatus().equals("1")) {
                            //更新UI
                            updAdress.setVisibility(View.GONE);
                            ll_shop_adressdetail.setVisibility(View.VISIBLE);
                            shop_name.setText("收件人：" + shopadressbean.getInfo().getName());
                            shop_tel.setText("电话：" + shopadressbean.getInfo().getTel());
                            shop_adress.setText("收货地址: " + shopadressbean.getInfo().getAdress());
                        } else if (shopadressbean.getStatus().equals("0")) {

                            ToastUtils.show(ShopIsOrderActivity.this, response.body().getMsg(), 0);
                        } else if (shopadressbean.getStatus().equals("2")) {
                            ToastUtils.showShort(ShopIsOrderActivity.this, shopadressbean.getMsg());

                        }
                    }

                }

                @Override
                public void onFailure(Call<GetShopAdressBean> call, Throwable t) {
                    MoudleUtils.toChekWifi(ShopIsOrderActivity.this);

                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initTaskGet();
        initGetAdress();
    }

    /**
     * 得到订单信息
     */

    private void initTaskGet() {
        Intent intent = getIntent();
        shop_id = intent.getStringExtra("id");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<GetShopInfoBean> callBack = restApi.shopinfo(shop_id);

        callBack.enqueue(new Callback<GetShopInfoBean>() {
            @Override
            public void onResponse(Call<GetShopInfoBean> call, Response<GetShopInfoBean> response) {

                shop_info = response.body();

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        initUI();
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(ShopIsOrderActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(ShopIsOrderActivity.this, shopadressbean.getMsg());

                    }
                }
            }

            @Override
            public void onFailure(Call<GetShopInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(ShopIsOrderActivity.this);
            }
        });

    }

    /**
     * 得到订单信息布置在界面上
     */
    private void initUI() {
        FrescoUtils.setImage(order_shop_pic, AppConfig.url_jszd + shop_info.getInfo().getPicture());
        order_shop_name.setText(shop_info.getInfo().getShopname());
        order_shop_price.setText("￥" + shop_info.getInfo().getPrice());
        order_shop_price1.setText("￥" + shop_info.getInfo().getPrice());
        order_shop_price2.setText("￥" + shop_info.getInfo().getPrice());
        order_shop_price3.setText("￥" + shop_info.getInfo().getPrice());
    }

    private void initId() {
        ll_shop_adressdetail = (LinearLayout) findViewById(R.id.shop_adressdetail);
        updAdress = (LinearLayout) findViewById(R.id.shop_updadress);

        shop_name = (TextView) findViewById(R.id.shop_name);
        shop_tel = (TextView) findViewById(R.id.shop_tel);
        shop_adress = (TextView) findViewById(R.id.shop_adress);
        submitOrder = (Button) findViewById(R.id.submit_order);
        order_shop_name = (TextView) findViewById(R.id.order_shop_name);
        order_shop_pic = (SimpleDraweeView) findViewById(R.id.order_shop_pic);
        order_shop_price = (TextView) findViewById(R.id.order_shop_price);
        order_shop_price1 = (TextView) findViewById(R.id.order_shop_price1);
        order_shop_price2 = (TextView) findViewById(R.id.order_shop_price2);
        order_shop_price3 = (TextView) findViewById(R.id.order_shop_price3);
        zhifubao = (RadioButton) findViewById(R.id.shop_zhifibao);
        weixin = (RadioButton) findViewById(R.id.shop_weixin);

        submitOrder.setOnClickListener(this);
        updAdress.setOnClickListener(this);
        zhifubao.setOnClickListener(this);
        weixin.setOnClickListener(this);
        ll_shop_adressdetail.setOnClickListener(this);

        NotifyWxPayManager.getInstance().setNotifyMessage(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("提交订单");
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
            case R.id.shop_updadress:
                startActivity(new Intent().setClass(ShopIsOrderActivity.this, UpdAdressActivity.class));
                break;
            case R.id.submit_order:
                toSubmitOrder();
                break;
            case R.id.shop_zhifibao:
                pay = 1;
                break;
            case R.id.shop_weixin:
                pay = 2;
                break;
            case R.id.shop_adressdetail:
                startActivity(new Intent().setClass(ShopIsOrderActivity.this, UpdAdressActivity.class));
                break;
        }
    }

    /**
     * 提交订单逻辑处理
     */

    private void toSubmitOrder() {
        if (token.equals("")) {
            MoudleUtils.initStatusTwo(ShopIsOrderActivity.this, true);
        } else {
            if (shopadressbean.getInfo() != null) {
                submitOrder();
            } else {
                ToastUtils.showShort(ShopIsOrderActivity.this, "请完善收获地址信息");
            }
        }
    }



    //选择支付方式
    private void toPay(String order_id) {
        if (pay == 1) {
            toZhifubao(order_id);
        } else {
            toWeixin(order_id);
        }
    }

    private void toWeixin(String order_id) {
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<WeiPayBean> callBack = restApi.weipay(token, (Integer) SPUtils.get(ShopIsOrderActivity.this, "user_id", 0), order_id, "shop");

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

                        ToastUtils.show(ShopIsOrderActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(ShopIsOrderActivity.this, shopadressbean.getMsg());

                    }
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<WeiPayBean> call, Throwable t) {
                MoudleUtils.toChekWifi(ShopIsOrderActivity.this);
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
            ToastUtils.show(ShopIsOrderActivity.this, "请先安装微信客户端", 0);
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtils.show(ShopIsOrderActivity.this, "因为手机权限原因，请先开启微信客户端", 0);
            return;
        }
    }

    private void toZhifubao(String order_id) {
        //支付宝调起接口
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<AlipayBean> callBack = restApi.alipay(token, (Integer) SPUtils.get(ShopIsOrderActivity.this, "user_id", 0), order_id, "shop");

        callBack.enqueue(new Callback<AlipayBean>() {
            @Override
            public void onResponse(Call<AlipayBean> call, Response<AlipayBean> response) {

                AlipayBean order_infobean = response.body();

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        //掉起支付宝
                        cacthZhifubao(order_infobean);
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(ShopIsOrderActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(ShopIsOrderActivity.this, shopadressbean.getMsg());

                    }
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<AlipayBean> call, Throwable t) {
                MoudleUtils.toChekWifi(ShopIsOrderActivity.this);
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
                PayTask alipay = new PayTask(ShopIsOrderActivity.this);
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

    KyLoadingBuilder builder;

    /**
     * 提交订单接口
     */
    private void submitOrder() {
        builder = new KyLoadingBuilder(this);
        //微信支付掉起接口
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<SubmitOrderBean> callBack = restApi.submitorder((int) SPUtils.get(this, "user_id", 0), token, shop_id, "1", "1", shopadressbean.getInfo().getAdress(),
                shopadressbean.getInfo().getName(), shopadressbean.getInfo().getTel());

        callBack.enqueue(new Callback<SubmitOrderBean>() {
            @Override
            public void onResponse(Call<SubmitOrderBean> call, Response<SubmitOrderBean> response) {
                SubmitOrderBean submit_orderbean = response.body();

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (submit_orderbean.getInfo() != null) {
                            AppConfig.order_id = submit_orderbean.getInfo().getOrder_id();
                            toPay(AppConfig.order_id);
                        }else {
                            MoudleUtils.kyloadingDismiss(builder);
                        }
                    } else if (response.body().getStatus().equals("0")) {
                        MoudleUtils.kyloadingDismiss(builder);
                        ToastUtils.showShortNotInternet(response.body().getMsg());
                    } else if (response.body().getStatus().equals("2")) {
                        MoudleUtils.kyloadingDismiss(builder);
                        MoudleUtils.initStatusTwo(ShopIsOrderActivity.this, true);
                    }
                }else {
                    MoudleUtils.kyloadingDismiss(builder);
                }
            }

            @Override
            public void onFailure(Call<SubmitOrderBean> call, Throwable t) {
                MoudleUtils.toChekWifi(ShopIsOrderActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initNullPayInterfosion();
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
}
