package com.ckjs.ck.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.OrderQueryBean;
import com.ckjs.ck.Manager.NotifyWxPayManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.PayTool.Constants;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        AppConfig.wxAlertDialog = null;
    }

    private void getResult() {
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");
        String type = (String) SPUtils.get(this, "pay_type", "");
        Call<OrderQueryBean> callBack = restApi.orderquery(token, (Integer) SPUtils.get(WXPayEntryActivity.this, "user_id", 0), AppConfig.order_id, type);

        callBack.enqueue(new Callback<OrderQueryBean>() {
            @Override
            public void onResponse(Call<OrderQueryBean> call, Response<OrderQueryBean> response) {
                OrderQueryBean orderQueryBean = response.body();
                if (orderQueryBean != null) {
                    String statuus = orderQueryBean.getStatus();
                    if (statuus.equals("1")) {
                        toFinish(statuus, orderQueryBean.getMsg());
                    } else if (statuus.equals("0")) {
                        toFinish(statuus, orderQueryBean.getMsg());
                    } else if (statuus.equals("2")) {
                        MoudleUtils.initStatusTwo(WXPayEntryActivity.this, true);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderQueryBean> call, Throwable t) {
                MoudleUtils.toChekWifi(WXPayEntryActivity.this);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        AppConfig.wxAlertDialog = null;
        super.onDestroy();
    }

    private void toFinish(final String status, String msg) {
        if (AppConfig.wxAlertDialog == null) {
            //TODO
            AppConfig.wxAlertDialog = new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            NotifyWxPayManager.getInstance().sendNotifyMWxpayFinishendFlag(true, status);
                            finish();
                        }

                    }).create(); // 创建对话框
            AppConfig.wxAlertDialog.setCancelable(false);
            if (AppConfig.wxAlertDialog != null) {
                if (!AppConfig.wxAlertDialog.isShowing()) {
                    AppConfig.wxAlertDialog.show(); // 显示
                }
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * 名称	描述	解决方案
     * 0	成功	展示成功页面
     * -1	错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
     * -2	用户取消	无需处理。发生场景：用户不支付了，点击取消，返回APP。
     *
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == 0) {
            if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                getResult();
            }
        } else if (resp.errCode == -2) {
            ToastUtils.showShortNotInternet("支付失败：操作已经取消");
        } else if (resp.errCode == -1) {
            ToastUtils.showShortNotInternet("支付失败：操作已经取消");
        }
    }
}