package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.BandinfoBean;
import com.ckjs.ck.Bean.UnbandBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.Waterview.WaveHelper;
import com.ckjs.ck.Tool.ViewTool.Waterview.WaveView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class DeviceControlActivity extends AppCompatActivity {
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.tv_mak)
    TextView tv_mak;
    @BindView(R.id.btn_jie_bang)
    Button btn_jie_bang;
    @BindView(R.id.wave)
    WaveView wave;
    @BindView(R.id.sw_master_shake)
    Switch sw_master_shake;
    @BindView(R.id.tv_start_tong_bu)
    TextView tv_start_tong_bu;
    @BindView(R.id.tv_firmware)
    TextView tv_firmware;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_set)
    TextView tv_set;
    @BindView(R.id.ll_pay)
    LinearLayout ll_pay;

    private String mDeviceAddress;
    private String tag = "bindContro";

    private WaveHelper mWaveHelper;
    private ProgressDialog dialig;
    private String sType = "";

    public DeviceControlActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ck_shouhuan);
        ButterKnife.bind(this);
        initToolbar();
        AppConfig.mastershake = (boolean) SPUtils.get("mastershake_all", AppConfig.mastershake);
        sw_master_shake.setChecked(AppConfig.mastershake);
        dialig = new ProgressDialog(this);
        initData();
        initBinded();
        initOpen();
        initJieBang();

    }

    public static KyLoadingBuilder kyLoadingBuilderDEvice;

    private void initStartTbStep() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff4", AppConfig.FFF4);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //TODO 步数
                        AppConfig.nameStep = "dv_tb";
                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x04, 0x04, 0x00, 0x00});
                    }
                }, 1000);
            }
        }, 1000);
    }

    /**
     * 一键同步
     */
    @OnClick(R.id.tv_start_tong_bu)
    void tvStartTongBu() {
        if (AppConfig.bluetoothGattServices != null && AppConfig.mBluetoothGatt != null) {
            if (!((boolean) (SPUtils.get("isSportMode", false)))) {
                if (!AppConfig.flag) {
                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                        ToastUtils.showShortNotInternet("开启数据同步");
                        kyLoadingBuilderDEvice = new KyLoadingBuilder(this);
                        MoudleUtils.kyloadingShow(kyLoadingBuilderDEvice);
                        initStartTbStep();
                    } else {
                        ToastUtils.showShortNotInternet("数据同步中...");
                    }
                } else {

                    ToastUtils.showShortNotInternet("数据同步中...");
                }
            } else {

                ToastUtils.showShortNotInternet("正处于运动模式，不可同步数据");
            }
        } else {
            ToastUtils.showShortNotInternet("手环未连接");
        }
    }


    /**
     * 超空智能手环编程
     */
    @OnClick(R.id.tv_set)
    void tvSet() {
        if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
            if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                startActivity(new Intent().setClass(this, BindNFCActivity.class));
            } else {
                ToastUtils.showShortNotInternet("请等待数据同步结束");
            }
        } else {
            ToastUtils.showShortNotInternet("手环未连接");
        }
    }


    private void initOpen() {
        sw_master_shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                        if (sw_master_shake.isChecked()) {
                            initShowKyzz();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x02, 0x01, 0x00, 0x00});
                                }
                            }, 500);
                        } else {
                            initShowKyzz();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x02, 0x02, 0x00, 0x00});
                                }
                            }, 500);
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

    private void initShowKyzz() {
        kyzzd = new KyLoadingBuilder(DeviceControlActivity.this);
        MoudleUtils.kyloadingShow(kyzzd);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGattUpdateReceiver != null) {
            MoudleUtils.unregisterReceivermGattUpdateReceiver(this, mGattUpdateReceiver);
        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (AppConfig.ACTION_GATT_CONNECTED.equals(action)) {


            } else if (AppConfig.ACTION_GATT_DISCONNECTED.equals(action)) {
                initDis();
            } else if (AppConfig.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {


            } else if (AppConfig.ACTION_DATA_AVAILABLE.equals(action)) {
                final Intent i = intent;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        displayData(i.getStringExtra(AppConfig.EXTRA_DATA_UUID), i.getByteArrayExtra(AppConfig.EXTRA_DATA));
                    }
                });

            } else if (AppConfig.ACTION_DATA_AVAILABLE_WRITE.equals(action)) {
                final Intent i = intent;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        displayDataZd(i.getStringExtra(AppConfig.EXTRA_DATA_UUID), i.getByteArrayExtra(AppConfig.EXTRA_DATA));
                        displayJieBang(i.getStringExtra(AppConfig.EXTRA_DATA_UUID), i.getByteArrayExtra(AppConfig.EXTRA_DATA));
                    }
                });
            }
        }
    };

    synchronized void displayJieBang(String uuid, byte[] data) {
        if (uuid.contains("fff1")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == (byte) 0x0A) {
                        if (data[1] == 1) {
                            initJiebang();
                        }
                    }
                }
            }
        }
    }

    KyLoadingBuilder kyzzd;

    private synchronized void displayDataZd(String uuid, byte[] data) {
        if (uuid.contains("fff1")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 2) {
                        if (data[1] == 1) {
                            MoudleUtils.kyloadingDismiss(kyzzd);
                            AppConfig.mastershake = true;
                            SPUtils.put(this, "mastershake_all", AppConfig.mastershake);
                            sw_master_shake.setChecked(AppConfig.mastershake);
                            ToastUtils.showShortNotInternet("震动提醒功能已开启");
                        } else if (data[1] == 2) {
                            MoudleUtils.kyloadingDismiss(kyzzd);
                            AppConfig.mastershake = false;
                            SPUtils.put(this, "mastershake_all", AppConfig.mastershake);
                            sw_master_shake.setChecked(AppConfig.mastershake);
                            ToastUtils.showShortNotInternet("震动提醒功能已关闭");
                        }
                    }
                }
            }
        }
    }

    private synchronized void displayData(String uuid, byte[] data) {
        if (uuid.contains("fff2")) {
            if (data != null) {
                if (data.length > 0) {
                    initDl(data);
                    MoudleUtils.textViewSetText(tv_mak, mDeviceAddress);
                    initHSHMore(mDeviceAddress);
                }
            }
        }
    }

    private void initHSHMore(String band) {
        int userid = (int) SPUtils.get(this, "user_id", 0);
        String token = (String) SPUtils.get(this, "token", "");
        Call<BandinfoBean> bandstatusBean = RetrofitUtils.retrofit.create(NpApi.class).bandinfo(token, userid, band);
        bandstatusBean.enqueue(new Callback<BandinfoBean>() {
            @Override
            public void onResponse(Call<BandinfoBean> call, Response<BandinfoBean> response) {
                BandinfoBean bandstatusBean = response.body();
                if (bandstatusBean == null)
                    return;
                //status|状态|0：未绑定；1：已自己账号绑定；2：已与其他账号绑定；
                /**
                 *  type	 手环类型	1：正常；2：租凭
                 */
                String statusOne = bandstatusBean.getStatus();
                String msg = bandstatusBean.getMsg();
                if (statusOne == null)
                    return;
                if (statusOne.equals("2") || statusOne.equals("0")) {
                    ToastUtils.showShort(DeviceControlActivity.this, msg);
                    return;
                }
                if (statusOne.equals("1")) {
                    String type = bandstatusBean.getInfo().getType();
                    String amount = bandstatusBean.getInfo().getAmount();
                    String firmware = bandstatusBean.getInfo().getFirmware();
                    MoudleUtils.textViewSetText(tv_firmware, firmware);
                    sType = type;
                    if (type.equals("1")) {
                        MoudleUtils.viewGone(ll_pay);
                    } else if (type.equals("2")) {
                        MoudleUtils.viewShow(ll_pay);
                        MoudleUtils.textViewSetText(tv_amount, "￥" + amount);
                    }
                }


            }

            @Override
            public void onFailure(Call<BandinfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
            }
        });
    }

    private synchronized void initDl(byte[] data) {
        if (data.length == 2) {
            int[] ints = DataUtils.byteTo10(data);
            if (ints.length > 1) {
                initIsHaveDl(ints[1]);
            }
        }
        MoudleUtils.kyloadingDismiss(kyLoadingBuilderDl);
    }

    private synchronized void initIsHaveDl(int anInt) {
        MoudleUtils.textViewSetText(tv_num, "剩余电量" + anInt + "%");
        initAnima(anInt);
    }

    /**
     * 电量动画
     *
     * @param anInt
     */

    private void initAnima(int anInt) {
        if (wave != null) {
            wave.setBorder(5, Color.parseColor("#ffffffff"));
            wave.setShapeType(WaveView.ShapeType.CIRCLE);
            wave.setWaveColor(
                    Color.parseColor("#b25bacf1"),
                    Color.parseColor("#5bacf1"));
            float ele = anInt / 100.0f;
            mWaveHelper = new WaveHelper(wave, AppConfig.dl / 100.0f, ele);
            mWaveHelper.start();
        }
    }

    private void initBinded() {
        initIsHaveDl(AppConfig.dl);
        registerReceiver(mGattUpdateReceiver, MoudleUtils.makeGattUpdateIntentFilter());
        if (mDeviceAddress != null && !mDeviceAddress.equals("")) {
            if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                    initAcsse();
                } else {
                    out();
                }
            } else {
                MoudleUtils.connect(this, mDeviceAddress);
            }
        } else {
            MoudleUtils.connect(this, mDeviceAddress);
        }
    }


    private void out() {
        ToastUtils.showShortNotInternet("请等待数据同步结束");
    }

    private KyLoadingBuilder kyLoadingBuilderDl;

    private void initAcsse() {
        kyLoadingBuilderDl = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilderDl);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff2");
            }
        }, 1000);
    }

    private void initJieBang() {
        btn_jie_bang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceControlActivity.this);

                builder.setMessage("解绑后手环将恢复出厂设置并清空所有数据，确认解绑吗？");

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initToJieB();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void initToJieB() {
        if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
            if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                if (sType.equals("1")) {
                    MoudleUtils.dialogShow(dialig);
                    initJiebangTask();
                } else if (sType.equals("2")) {
                    startActivity(new Intent().putExtra("mac", mDeviceAddress).setClass(DeviceControlActivity.this, BackDepositActivity.class));
                }
            } else {
                ToastUtils.showShortNotInternet("请等待数据同步结束");
            }
        } else {
            ToastUtils.showShortNotInternet("手环未连接");
        }
    }

    private void initJiebangTask() {
        int userid = (int) SPUtils.get(this, "user_id", 0);
        String token = (String) SPUtils.get(this, "token", "");
        String bind = (String) SPUtils.get(this, "mDeviceAddress", "");
        Call<UnbandBean> bandstatusBean = RetrofitUtils.retrofit.create(NpApi.class).unband(token, userid, bind);
        bandstatusBean.enqueue(new Callback<UnbandBean>() {
            @Override
            public void onResponse(Call<UnbandBean> call, Response<UnbandBean> response) {

                UnbandBean bandstatusBean = response.body();
                if (bandstatusBean == null) {
                    MoudleUtils.dialogDismiss(dialig);
                    return;
                }
                String status = bandstatusBean.getStatus();
                if (status == null) {
                    MoudleUtils.dialogDismiss(dialig);
                    return;
                }
                if (status.equals("0") || status.equals("2")) {
                    MoudleUtils.dialogDismiss(dialig);
                    ToastUtils.showShort(DeviceControlActivity.this, bandstatusBean.getMsg());
                    return;
                } else if (status.equals("1")) {
                    SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "isbind", bandstatusBean.getInfo().getIsbind());
                    SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "nameStepType", "0");
                    initHuFu();
                } else {
                    MoudleUtils.dialogDismiss(dialig);
                }

            }

            @Override
            public void onFailure(Call<UnbandBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialig);
                ToastUtils.showShort(DeviceControlActivity.this, getResources().getString(R.string.not_wlan_show));
            }
        });
    }

    private void initHuFu() {
        if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MoudleUtils.dialogDismiss(dialig);
                    BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x0A, 0x01, 0x00, 0x00});
                }
            }, 500);
        } else {
            MoudleUtils.dialogDismiss(dialig);
        }
    }

    synchronized void initJiebang() {
        if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
            SPUtils.remove(DeviceControlActivity.this, "mDeviceAddress");
            SPUtils.remove(DeviceControlActivity.this, "clockhour");
            MoudleUtils.initJieBang(0);
        } else {
            ToastUtils.showShortNotInternet("手环未连接");
        }
    }


    private void initDis() {
        MoudleUtils.kyloadingDismiss(kyLoadingBuilderDl);
        MoudleUtils.kyloadingDismiss(kyLoadingBuilderDEvice);
        finish();
    }


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

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("超空手环");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mDeviceAddress = intent.getStringExtra(AppConfig.EXTRAS_DEVICE_ADDRESS);
        if (mDeviceAddress == null
                || mDeviceAddress.equals("")) {
            mDeviceAddress = (String) SPUtils.get("mDeviceAddress", "");
//            LogUtils.d("mDeviceAddress", mDeviceAddress);
        }
    }


}
