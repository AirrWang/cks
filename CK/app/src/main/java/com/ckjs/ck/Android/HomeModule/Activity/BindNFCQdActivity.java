package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.GetnfcsignBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.HSHTool;
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

public class BindNFCQdActivity extends Activity {

    private KyLoadingBuilder builder;
    private TextView tv_nfc_card;
    private GetnfcsignBean.GetnfcsignBeanInfo body;
    private TextView tv_nfc_relname;
    private Button btn_sure;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_nfc_qd);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        //        p.height = (int) (d.getHeight() * 0.34);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.8
        //        p.alpha = 1.0f;      //设置本身透明度
        //        p.dimAmount = 1.0f;      //设置黑暗度
        builder = new KyLoadingBuilder(this);
        initId();
        registerReceiver(mGattUpdateReceiver, MoudleUtils.makeGattUpdateIntentFilter());
        initData();
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (AppConfig.ACTION_GATT_CONNECTED.equals(action)) {


            } else if (AppConfig.ACTION_GATT_DISCONNECTED.equals(action)) {
                MoudleUtils.dialogDismiss(dialog);
                finish();
            } else if (AppConfig.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {


            } else if (AppConfig.ACTION_DATA_AVAILABLE.equals(action)) {


            } else if (AppConfig.ACTION_DATA_AVAILABLE_WRITE.equals(action)) {
                final Intent i = intent;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        displayData(i.getStringExtra(AppConfig.EXTRA_DATA_UUID), i.getByteArrayExtra(AppConfig.EXTRA_DATA));

                    }
                });

            }
        }
    };

    private synchronized void displayData(String uuid, byte[] data) {
        if (uuid.contains("fff6")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 2) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initToWriteCardTwo(array, 21, 30, (array.length + 1) / 4 + 21 - 1, (array.length + 1) % 4);
                            }
                        }, 380);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGattUpdateReceiver != null) {
            MoudleUtils.unregisterReceivermGattUpdateReceiver(this, mGattUpdateReceiver);
        }
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        String token = (String) SPUtils.get(BindNFCQdActivity.this, "token", "");
        int user_id = (int) SPUtils.get(BindNFCQdActivity.this, "user_id", 0);
        Call<GetnfcsignBean> callBack = restApi.getnfcsign(token, user_id);

        callBack.enqueue(new Callback<GetnfcsignBean>() {
            @Override
            public void onResponse(Call<GetnfcsignBean> call, Response<GetnfcsignBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("0")) {
                        ToastUtils.showShort(BindNFCQdActivity.this, response.body().getMsg());
                        finish();
                    } else if (response.body().getStatus().equals("1")) {
                        body = response.body().getInfo();
                        if (body != null) {
                            initUI();
                        }
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(BindNFCQdActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<GetnfcsignBean> call, Throwable t) {
                MoudleUtils.toChekWifi(BindNFCQdActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });

    }

    private synchronized void initToWriteCard(final char[] array, final int i1, final int i2, final int n, final int nY) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    initToWriteNfc(i2, n, array, nY);
                }
            }
        });
    }

    private synchronized void initToWriteCardTwo(final char[] array, final int i1, final int i2, final int n, final int nY) {


        initToWriteNfc(i2, n, array, nY);


    }


    int i = 21;
    int j = 0;
    int in = 21;
    int inY = 0;
    char[] array;

    synchronized void initToWriteNfc(int i2, int n, char[] array, int nY) {
        if (array == null || array.length == 0)
            return;
        //        Log.i(AppConfig.TAG, "i:" + i);
        //        Log.i(AppConfig.TAG, "in:" + in);
        //        Log.i(AppConfig.TAG, "n:" + n);
        if (i <= i2) {
            if (in != -1 && in <= n) {
                if (in == 21) {
                    String s = MoudleUtils.checkData(i / 32) + MoudleUtils.checkData(i % 32);
                    //                    Log.i(AppConfig.TAG, "writeNFC1:" + s);
                    final byte[] bytes = DataUtils.hexStrToByteArray(s, 2);
                    //                    Log.i(AppConfig.TAG, "writeNFC2:" + (byte) bytes[0] + "," + (byte) bytes[1]);
                    BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff6", new byte[]{(byte) 0x02, (byte) bytes[0], (byte) bytes[1], (byte) 0x00, (byte) array[j], (byte) array[j + 1], (byte) array[j + 2]});
                    j = j + 3;
                } else {
                    HSHTool.writeNFC(i / 32, i % 32, array[j], array[j + 1], array[j + 2], array[j + 3]);
                    j = j + 4;
                }
                in++;
                i++;
            } else if (in == n + 1) {
                switch (nY) {
                    case 0:
                        String s = MoudleUtils.checkData(i / 32) + MoudleUtils.checkData(i % 32);
                        //                        Log.i(AppConfig.TAG, "writeNFC1:" + s);
                        final byte[] bytes = DataUtils.hexStrToByteArray(s, 2);
                        //                        Log.i(AppConfig.TAG, "writeNFC2:" + (byte) bytes[0] + "," + (byte) bytes[1]);
                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff6", new byte[]{(byte) 0x02, (byte) bytes[0], (byte) bytes[1], (byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
                        i++;
                        initFinshed();
                        break;
                    case 1:
                        String s1 = MoudleUtils.checkData(i / 32) + MoudleUtils.checkData(i % 32);
                        //                        Log.i(AppConfig.TAG, "writeNFC1:" + s1);
                        final byte[] bytes2 = DataUtils.hexStrToByteArray(s1, 2);
                        //                        Log.i(AppConfig.TAG, "writeNFC2:" + (byte) bytes2[0] + "," + (byte) bytes2[1]);
                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff6", new byte[]{(byte) 0x02, (byte) bytes2[0], (byte) bytes2[1], (byte) array[j], (byte) 0xFE, (byte) 0xFF, (byte) 0xFF});
                        i++;
                        initFinshed();
                        break;
                    case 2:
                        String s2 = MoudleUtils.checkData(i / 32) + MoudleUtils.checkData(i % 32);
                        //                        Log.i(AppConfig.TAG, "writeNFC1:" + s2);
                        final byte[] bytes3 = DataUtils.hexStrToByteArray(s2, 2);
                        //                        Log.i(AppConfig.TAG, "writeNFC2:" + (byte) bytes3[0] + "," + (byte) bytes3[1]);
                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff6", new byte[]{(byte) 0x02, (byte) bytes3[0], (byte) bytes3[1], (byte) array[j], (byte) array[j + 1], (byte) 0xFE, (byte) 0xFF});
                        i++;
                        initFinshed();
                        break;
                    case 3:
                        String s3 = MoudleUtils.checkData(i / 32) + MoudleUtils.checkData(i % 32);
                        //                        Log.i(AppConfig.TAG, "writeNFC1:" + s3);
                        final byte[] bytes4 = DataUtils.hexStrToByteArray(s3, 2);
                        //                        Log.i(AppConfig.TAG, "writeNFC2:" + (byte) bytes4[0] + "," + (byte) bytes4[1]);
                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff6", new byte[]{(byte) 0x02, (byte) bytes4[0], (byte) bytes4[1], (byte) array[j], (byte) array[j + 1], (byte) array[j + 2], (byte) 0xFE});
                        i++;
                        in++;
                        break;
                }
            } else if (in == n + 2) {
                String s3 = MoudleUtils.checkData(i / 32) + MoudleUtils.checkData(i % 32);
                //                Log.i(AppConfig.TAG, "writeNFC1:" + s3);
                final byte[] bytes4 = DataUtils.hexStrToByteArray(s3, 2);
                //                Log.i(AppConfig.TAG, "writeNFC2:" + (byte) bytes4[0] + "," + (byte) bytes4[1]);
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff6", new byte[]{(byte) 0x02, (byte) bytes4[0], (byte) bytes4[1], (byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
                i++;
                initFinshed();
            } else if (in == -1) {
                initEnd();
            }

        } else {

        }
    }

    private void initFinshed() {
        in = -1;

    }

    private void initEnd() {
        ToastUtils.showShortNotInternet("写入完成");
        MoudleUtils.dialogDismiss(dialog);
        finish();

    }

    private void initUI() {
        final String card = body.getSigncard();
        MoudleUtils.textViewSetText(tv_nfc_card, card);
        MoudleUtils.textViewSetText(tv_nfc_relname, body.getGymname());
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                        if (card != null && !card.equals("")) {
                            MoudleUtils.dialogShow(dialog);
                            array = card.toCharArray();
                            for (int j = 0; j < array.length; j++) {
                                //                                Log.i(AppConfig.TAG, "array[" + j + "]:" + array[j]);
                            }
                            initToWriteCard(array, 21, 30, (array.length + 1) / 4 + 21 - 1, (array.length + 1) % 4);
                        }

                    } else {
                        ToastUtils.showShortNotInternet("请等待数据同步结束");
                    }
                } else {
                    ToastUtils.showShortNotInternet("手环未连接");
                }

            }
        });
    }

    private void initId() {
        tv_nfc_card = (TextView) findViewById(R.id.tv_nfc_card);
        tv_nfc_relname = (TextView) findViewById(R.id.tv_nfc_relname);
        btn_sure = (Button) findViewById(R.id.btn_sure);
        dialog = new ProgressDialog(this);

    }

}
