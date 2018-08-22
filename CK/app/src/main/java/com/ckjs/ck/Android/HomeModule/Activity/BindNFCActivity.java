package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.ToastUtils;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class BindNFCActivity extends Activity {
    private LinearLayout ll_vip;
    private LinearLayout ll_mj;
    private LinearLayout ll_qd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_nfc);
        registerReceiver(mGattUpdateReceiver, MoudleUtils.makeGattUpdateIntentFilter());
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        //        p.height = (int) (d.getHeight() * 0.34);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.8
        //        p.alpha = 1.0f;      //设置本身透明度
        //        p.dimAmount =0.0f;      //设置黑暗度

        initId();
        ll_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initRetun())
                    return;
                startActivity(new Intent().setClass(BindNFCActivity.this, BindNFCVipActivity.class));
                finish();
            }
        });
        ll_mj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initRetun())
                    return;
                startActivity(new Intent().setClass(BindNFCActivity.this, BindNFCMjActivity.class));
                finish();
            }
        });
        ll_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initRetun())
                    return;
                startActivity(new Intent().setClass(BindNFCActivity.this, BindNFCQdActivity.class));
                finish();
            }
        });
    }

    private boolean initRetun() {
        if (AppConfig.bluetoothGattServices == null || AppConfig.mBluetoothGatt == null) {
            ToastUtils.showShortNotInternet("手环未连接");
            return true;
        }
        if (!HomeFragment.nHealthFlag || !HomeFragment.nSleepFlag) {
            ToastUtils.showShortNotInternet("请等待数据同步结束");
            return true;
        }
        return false;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (AppConfig.ACTION_GATT_DISCONNECTED.equals(action)) {
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGattUpdateReceiver != null) {
            MoudleUtils.unregisterReceivermGattUpdateReceiver(this, mGattUpdateReceiver);
        }
    }

    private void initId() {
        ll_vip = (LinearLayout) findViewById(R.id.ll_vip);
        ll_mj = (LinearLayout) findViewById(R.id.ll_mj);
        ll_qd = (LinearLayout) findViewById(R.id.ll_qd);
    }

}
