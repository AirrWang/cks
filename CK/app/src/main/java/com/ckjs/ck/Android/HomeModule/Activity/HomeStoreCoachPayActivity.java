package com.ckjs.ck.Android.HomeModule.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ckjs.ck.Android.MineModule.Activity.TouXiangActivity;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.AlipayBean;
import com.ckjs.ck.Bean.CoachInfoBean;
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
import com.ckjs.ck.Tool.ScreenUtils;
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
public class HomeStoreCoachPayActivity extends AppCompatActivity implements NotifyWxpayFinishendMessage, View.OnClickListener {


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
    private Button btn_num_up_coach;
    private Button btn_num_down_coach;
    private Button submit_order_coach;
    private SimpleDraweeView coach_pay_pic;
    private TextView store_coach_pay_tel;
    private String name;
    private int coach_id;
    private CoachInfoBean coachinfo;
    private TextView store_coach_pay_manual;
    private TextView store_coach_pay_price;
    private TextView store_coach_pay_total;
    private TextView store_coach_pay_total_2;
    private TextView store_coach_pay_unitprice;
    private LinearLayout sd_sj_yy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_store_coach_pay);
        initId();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        coach_id = Integer.parseInt(intent.getStringExtra("id"));
        initToolbar();
        initData();
        SPUtils.put(this, "pay_type", "shop");
        initNullPayInterfosion();
        sd_sj_yy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(HomeStoreCoachPayActivity.this, CkSjActivity.class));
            }
        });
    }


    private void initId() {
        store_coach_pay_unitprice = (TextView) findViewById(R.id.store_coach_pay_unitprice);
        store_coach_pay_manual = (TextView) findViewById(R.id.store_coach_pay_manual);
        store_coach_pay_price = (TextView) findViewById(R.id.store_coach_pay_price);
        store_coach_pay_total = (TextView) findViewById(R.id.store_coach_pay_total);
        store_coach_pay_total_2 = (TextView) findViewById(R.id.store_coach_pay_total_2);

        store_coach_pay_tel = (TextView) findViewById(R.id.store_coach_pay_tel);
        btn_num_down_coach = (Button) findViewById(R.id.btn_num_down_coach);
        btn_num_up_coach = (Button) findViewById(R.id.btn_num_up_coach);
        text_num = (TextView) findViewById(R.id.text_num_coach);
        submit_order_coach = (Button) findViewById(R.id.submit_order_coach);
        RadioButton coach_zhifibao = (RadioButton) findViewById(R.id.coach_zhifibao);
        RadioButton coach_weixin = (RadioButton) findViewById(R.id.coach_weixin);

        coach_pay_pic = (SimpleDraweeView) findViewById(R.id.coach_pay_pic);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(this), (int) (ScreenUtils.getScreenWidth(this) / 2));
        coach_pay_pic.setLayoutParams(layoutParams);
        store_coach_pay_tel.setOnClickListener(this);
        coach_pay_pic.setOnClickListener(this);
        coach_zhifibao.setOnClickListener(this);
        coach_weixin.setOnClickListener(this);
        submit_order_coach.setOnClickListener(this);
        btn_num_down_coach.setOnClickListener(this);
        btn_num_down_coach.setClickable(false);
        btn_num_up_coach.setOnClickListener(this);
        NotifyWxPayManager.getInstance().setNotifyMessage(this);

        sd_sj_yy = (LinearLayout) findViewById(R.id.sd_sj_yy);
        sd_sj_yy.setOnClickListener(this);
    }

    /**
     * 通过接口获取数据
     */
    private void initData() {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        //TODO
        String token = "";
        token = (String) SPUtils.get(this, "token", token);
        Call<CoachInfoBean> callBack = restApi.coachinfo(coach_id);

        callBack.enqueue(new Callback<CoachInfoBean>() {
            @Override
            public void onResponse(Call<CoachInfoBean> call, Response<CoachInfoBean> response) {
                coachinfo = response.body();
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {

                        initUI(coachinfo);
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(HomeStoreCoachPayActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.show(HomeStoreCoachPayActivity.this, response.body().getMsg(), 0);
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<CoachInfoBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(builder);
                MoudleUtils.toChekWifi(HomeStoreCoachPayActivity.this);
            }
        });
    }

    /**
     * 拿到数据更新UI
     *
     * @param coachinfo
     */
    private void initUI(CoachInfoBean coachinfo) {
        if (coachinfo.getInfo() != null) {
            FrescoUtils.setImage(coach_pay_pic, AppConfig.url_jszd + coachinfo.getInfo().getDatu());
            store_coach_pay_manual.setText(coachinfo.getInfo().getIntroduce());
            store_coach_pay_price.setText("￥" + coachinfo.getInfo().getPrice());
            store_coach_pay_total.setText("￥" + coachinfo.getInfo().getPrice());
            store_coach_pay_unitprice.setText(coachinfo.getInfo().getUnitprice());
            store_coach_pay_total_2.setText("￥" + coachinfo.getInfo().getPrice());
            store_coach_pay_tel.setText("电话：" + coachinfo.getInfo().getTel());
            btn_num_up_coach.setClickable(true);
        }
    }

    /**
     * 支付宝支付成功结果接口查询
     */
    private void getZhifubaoResult() {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = "";
        token = (String) SPUtils.get(this, "token", token);

        Call<OrderQueryBean> callBack = restApi.aliquery(token, (Integer) SPUtils.get(HomeStoreCoachPayActivity.this, "user_id", 0), AppConfig.order_id, "shop");

        callBack.enqueue(new Callback<OrderQueryBean>() {
            @Override
            public void onResponse(Call<OrderQueryBean> call, Response<OrderQueryBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("0")) {
                        toFinish(response.body().getMsg());
                    } else if (response.body().getStatus().equals("1")) {
                        toFinish("订单已支付完成，即可到我→个人中心→我的订单中查看详情");
                    } else if (response.body().getStatus().equals("2")) {
                        MoudleUtils.initStatusTwo(HomeStoreCoachPayActivity.this, true);
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<OrderQueryBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HomeStoreCoachPayActivity.this);
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

    private void bind() {

        String token = (String) SPUtils.get("token", "");
        if (token.equals("")) {
            MoudleUtils.initToLogin(HomeStoreCoachPayActivity.this);
        } else {
            try {
                initToBd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void initToBd() {
        if ((SPUtils.get(HomeStoreCoachPayActivity.this, "vip", "").equals("1"))) {
            if ((SPUtils.get(HomeStoreCoachPayActivity.this, "type", "") + "").equals("1")) {
                startActivity(new Intent().setClass(HomeStoreCoachPayActivity.this, RealNameYsActivity.class));
            } else {
                startActivity(new Intent().setClass(HomeStoreCoachPayActivity.this, RealNameActivity.class));
            }
        } else {
            toSubmitOrder();
        }
    }
     KyLoadingBuilder builder;
    /**
     * 提交订单逻辑处理
     */
    private void toSubmitOrder() {
        builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        //TODO
        String token = "";
        token = (String) SPUtils.get(this, "token", token);
        Call<SubmitOrderBean> callBack = restApi.submitorderjsf((int) SPUtils.get(this, "user_id", 0), token, coach_id + "", num + "", "3");

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
                        ToastUtils.show(HomeStoreCoachPayActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        MoudleUtils.kyloadingDismiss(builder);
                        ToastUtils.show(HomeStoreCoachPayActivity.this, response.body().getMsg(), 0);
                    }
                }else {
                    MoudleUtils.kyloadingDismiss(builder);
                }
            }

            @Override
            public void onFailure(Call<SubmitOrderBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HomeStoreCoachPayActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initNullPayInterfosion();
    }

    //选择支付方式
    private void toPay(String id) {
        if (pay == 1) {
            toZhifubao(id);
        } else {
            toWeixin(id);
        }
    }

    private void toZhifubao(String id) {
        //支付宝调起接口

        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = "";
        token = (String) (SPUtils.get("token", token));

        int use_id = (int) SPUtils.get(HomeStoreCoachPayActivity.this, "user_id", 0);
        Call<AlipayBean> callBack = restApi.alipay(token, use_id, id, "shop");

        callBack.enqueue(new Callback<AlipayBean>() {
            @Override
            public void onResponse(Call<AlipayBean> call, Response<AlipayBean> response) {

                AlipayBean order_infobean = response.body();

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        //掉起支付宝
                        cacthZhifubao(order_infobean);
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(HomeStoreCoachPayActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(HomeStoreCoachPayActivity.this, response.body().getMsg());

                    }
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<AlipayBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HomeStoreCoachPayActivity.this);
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
                PayTask alipay = new PayTask(HomeStoreCoachPayActivity.this);
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

    private void toWeixin(String id) {
        //微信支付掉起接口
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = "";
        token = (String) (SPUtils.get("token", token));
        Call<WeiPayBean> callBack = restApi.weipay(token, (int) SPUtils.get(HomeStoreCoachPayActivity.this, "user_id", 0), id, "shop");

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

                        ToastUtils.show(HomeStoreCoachPayActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(HomeStoreCoachPayActivity.this, response.body().getMsg());

                    }
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<WeiPayBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HomeStoreCoachPayActivity.this);
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
            //查询结果
        }
    }

    /**
     * 判断手机是否支持微信支付
     */
    private void isWXAppInstalledAndSupported() {

        if (!api.isWXAppInstalled()) {
            ToastUtils.show(HomeStoreCoachPayActivity.this, "请先安装微信客户端", 0);
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtils.show(HomeStoreCoachPayActivity.this, "因为手机权限原因，请先开启微信客户端", 0);
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
            case R.id.btn_num_down_coach:
                num--;
                if (num >= 1) {
                    btn_num_down_coach.setClickable(true);
                    text_num.setText(num + "");
                    String s = MoudleUtils.roundDouble(coachinfo.getInfo().getPrice() * num, 2);
                    store_coach_pay_total.setText("￥" + s);
                    store_coach_pay_total_2.setText("￥" + s);
                } else {
                    btn_num_down_coach.setClickable(false);
                }
                break;
            case R.id.btn_num_up_coach:
                num++;
                btn_num_down_coach.setClickable(true);
                text_num.setText(num + "");
                String s = MoudleUtils.roundDouble(coachinfo.getInfo().getPrice() * num, 2);
                store_coach_pay_total.setText("￥" + s);
                store_coach_pay_total_2.setText("￥" + s);
                break;
            case R.id.submit_order_coach:
                bind();
                break;
            case R.id.coach_zhifibao:
                pay = 1;
                break;
            case R.id.coach_weixin:
                pay = 2;
                break;
            case R.id.coach_pay_pic:
                //跳转头像查看界面
                if (coachinfo != null) {
                    if (coachinfo.getInfo() != null) {
                        Intent intent = new Intent();
                        intent.putExtra("touxiang", coachinfo.getInfo().getDatu());
                        intent.putExtra("type", "1");
                        intent.setClass(HomeStoreCoachPayActivity.this, TouXiangActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.store_coach_pay_tel:
                goToCall();
                break;
        }
    }

    /**
     * 提示打电话
     */
    public void goToCall() {
        if (!store_coach_pay_tel.getText().toString().trim().equals("")) {
            new AlertDialog.Builder(this).setTitle("联系电话")
                    .setMessage(store_coach_pay_tel.getText().toString().trim())
                    .setPositiveButton("拨打", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initToCall();

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    /**
     * 打电话
     */
    private void initToCall() {
        try {
            String mobile = store_coach_pay_tel.getText().toString().trim();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "没有相关应用", Toast.LENGTH_SHORT).show();
        }
    }

}
