package com.ckjs.ck.Android.HealthModule.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.UpoxygenBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.Waterview.WaveHelper;
import com.ckjs.ck.Tool.ViewTool.Waterview.WaveView;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class HealthBloodOxygenMonitorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_xy_bloodoxgen_week;
    private TextView tv_num_xy;
    private WaveView wv_health_bloodoxygen_line;
    private ProgressDialog builder;
    private Button btn_measure_bloodoxygen;
    private int n = 36;
    private float ele1 = (96 - 80) / 20.0f;
    private float ele;
    private TextView tv_xy_state;
    private LinearLayout ll_line;
    private float w, w2;
    private TextView tv_line_bhd, tv_line_ml;
    private TextView tv_jl;
    private TextView tv_time;
    private WaveHelper mWaveHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_bloodoxygen);
        initToolbar();
        initId();
        w = (float) (ScreenUtils.getScreenWidth() * 0.666666667);
        w2 = (float) (ScreenUtils.getScreenWidth() / 102.8571429);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) w, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll_line.setLayoutParams(layoutParams);
        builder = new ProgressDialog(this);
        registerReceiver(mGattUpdateReceiver, MoudleUtils.makeGattUpdateIntentFilter());
        AppConfig.EXTRA_DATA_STATE = 4;//0,1,2,3,4,5首页，健康监测，睡眠测量，心率测量，血氧测量，血压测量
        initData();
    }

    private void initData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String date = formatter.format(curDate);
        tv_time.setText(date);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        initStop();
        if (mGattUpdateReceiver != null) {
            MoudleUtils.unregisterReceivermGattUpdateReceiver(this, mGattUpdateReceiver);
        }
    }


    private void initStop() {
        initStopCl();
        initRegistData();

    }

    private void initStopCl() {
        if (AppConfig.EXTRA_DATA_STATE == 4) {
            if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                    DataUtils.stopHearDeng();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 标题栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("血氧监测");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * 返回设置
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

    private void initId() {
        btn_measure_bloodoxygen = (Button) findViewById(R.id.btn_measure_bloodoxygen);
        ll_line = (LinearLayout) findViewById(R.id.ll_line);
        btn_measure_bloodoxygen.setOnClickListener(this);
        tv_xy_bloodoxgen_week = (TextView) findViewById(R.id.tv_xy_bloodoxgen_week);
        tv_num_xy = (TextView) findViewById(R.id.tv_num_xy);
        tv_line_bhd = (TextView) findViewById(R.id.tv_line_bhd);
        tv_line_ml = (TextView) findViewById(R.id.tv_line_ml);
        tv_xy_state = (TextView) findViewById(R.id.tv_xy_state);
        tv_jl = (TextView) findViewById(R.id.tv_j_l);
        LinearLayout ll_health_bloodoxygen_emergency = (LinearLayout) findViewById(R.id.ll_health_bloodoxygen_emergency);

        ll_health_bloodoxygen_emergency.setOnClickListener(this);
        tv_xy_bloodoxgen_week.setOnClickListener(this);

        wv_health_bloodoxygen_line = (WaveView) findViewById(R.id.ll_health_bloodoxygen_line);
        wv_health_bloodoxygen_line.setShapeType(WaveView.ShapeType.SQUARE);
        tv_max = (TextView) findViewById(R.id.tv_max);
        tv_min = (TextView) findViewById(R.id.tv_min);
        tv_time = (TextView) findViewById(R.id.tv_time);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_xy_bloodoxgen_week:
                startActivity(new Intent().setClass(HealthBloodOxygenMonitorActivity.this, HealthBloodOxygenWeekActivity.class));
                break;
            case R.id.ll_health_bloodoxygen_emergency:
                startActivity(new Intent().setClass(HealthBloodOxygenMonitorActivity.this, HealthBloodOxygenEmergencyActivity.class));
                break;
            case R.id.btn_measure_bloodoxygen:
                if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                        if (!((boolean) (SPUtils.get("isSportMode", false)))) {
                            i = 0;
                            iNum = 0;
                            ele = 0;
                            now = 0;
                            avg = 0;
                            he = 0;
                            he5 = 0;
                            min = 100000000;
                            max = 0;
                            AppConfig.EXTRA_DATA_STATE = 4;//0,1,2,3,4,5首页，健康监测，睡眠测量，心率测量，血氧测量，血压测量
                            btn_measure_bloodoxygen.setEnabled(false);
                            MoudleUtils.dialogShow(builder, true);
                            DataUtils.startHearDeng();
                        } else {
                            ToastUtils.showShort(HealthBloodOxygenMonitorActivity.this,"请将手环模式调为日常模式");
                        }
                    } else {
                        ToastUtils.showShort(HealthBloodOxygenMonitorActivity.this,"请等待数据同步结束");
                    }
                } else {
                    ToastUtils.showShort(HealthBloodOxygenMonitorActivity.this,"手环未连接");
                }
                break;
        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (AppConfig.ACTION_GATT_CONNECTED.equals(action)) {

            } else if (AppConfig.ACTION_GATT_DISCONNECTED.equals(action)) {
                i = 0;
                iNum = 0;
                btn_measure_bloodoxygen.setEnabled(false);
                MoudleUtils.dialogDismiss(builder);
            } else if (AppConfig.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

            } else if (AppConfig.ACTION_DATA_AVAILABLE.equals(action)) {

            } else if (AppConfig.ACTION_DATA_AVAILABLE_TIME.equals(action)) {
                final Intent intent1 = intent;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            if (AppConfig.EXTRA_DATA_STATE == 4) {
                                if (btn_measure_bloodoxygen != null && !btn_measure_bloodoxygen.isEnabled()) {
                                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag)
                                        displayData(intent1.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent1.getByteArrayExtra(AppConfig.EXTRA_DATA));

                                }
                            }
                        }
                    }
                });

            } else if (AppConfig.ACTION_DATA_AVAILABLE_WRITE.equals(action)) {
                //new byte[]{0x05, 0x00, 0x03, 0x00}
                displayDataWrite(intent);
            }
        }
    };

    /**
     * 判断是否开启心率设备成功
     */
    private void displayDataWrite(Intent intent) {
        String uuid = intent.getStringExtra(AppConfig.EXTRA_DATA_UUID).toString();
        byte[] data = intent.getByteArrayExtra(AppConfig.EXTRA_DATA);
        if (uuid.contains("fff1")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 5) {
                        if (data.length == 4) {
                            if (data[1] == (byte) 0x01) {
                                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff5", AppConfig.FFF5);
                            } else if (data[1] == 4) {
                                initRegistData();
                            }
                        }
                    }
                }
            }
        }
    }

    private void initRegistData() {
        if (btn_measure_bloodoxygen != null) {
            btn_measure_bloodoxygen.setEnabled(true);
        }
    }

    /**
     * 判断血氧状况
     *
     * @param value
     */
    //    private void initXYState(int i) {
    //        if (i >= 94 && i <= 100) {
    //            MoudleUtils.textViewSetText(tv_xy_state, "正常");
    //        } else if (i < 90) {
    //            MoudleUtils.textViewSetText(tv_xy_state, "低血氧症");
    //        } else if (i > 90 && i < 94) {
    //            MoudleUtils.textViewSetText(tv_xy_state, "供氧不足");
    //        }
    //    }
    private synchronized void initAnimate(int value) {
        if (value >= 80 && value < 90) {
            wv_health_bloodoxygen_line.setWaveColor(
                    Color.parseColor("#7ffabc73"),
                    Color.parseColor("#fabc73"));
        } else if (value >= 90 && value < 100) {
            wv_health_bloodoxygen_line.setWaveColor(
                    Color.parseColor("#c3e135"),
                    Color.parseColor("#5ead81"));
        } else if (value >= 100) {
            wv_health_bloodoxygen_line.setWaveColor(
                    Color.parseColor("#fca29d"),
                    Color.parseColor("#7ffca29d"));
        }
        if (value >= 80) {
            ele = (value - 80) / 20.0f;
        } else {
            ele = 0f;
        }
        mWaveHelper = new WaveHelper(wv_health_bloodoxygen_line, ele1, ele);
        mWaveHelper.start();
        ele1 = ele;
    }


    int i, iNum;
    int now;
    int avg, he, he5;
    int max, min = 1000;
    private TextView tv_max;
    private TextView tv_min;

    private synchronized void displayData(String uuid, byte[] data) {
        if (uuid.contains("fff5")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 1) {
                        if (data.length == 10) {
                            final int is[] = DataUtils.byteTo10(data);
                            if (is != null && is.length == 10) {
                                if (is[2] > 0 && is[5] > 0) {
                                    if (i == 0) {
                                        MoudleUtils.dialogDismiss(builder);
                                    }
                                    if (i < n - 1) {
                                        if (iNum % 10 == 0) {
                                            initSetData(is[2], is[5]);
                                            now = is[2];
                                            he = he + now;
                                            he5 = he5 + is[5];

                                            if (is[2] > max) {
                                                max = is[2];
                                            }
                                            if (is[2] < min) {
                                                min = is[2];
                                            }
                                            MoudleUtils.textViewSetText(tv_max, max + "bpm");
                                            MoudleUtils.textViewSetText(tv_min, min + "bpm");
                                            i++;
                                        }
                                        iNum++;
                                    }
                                    if (i == n - 1) {
                                        i++;
                                        avg = he / (n - 1);
                                        initSetData(avg, he5 / (n - 1));
                                        now = avg;
                                        initXyTask(now);
                                        initStop();
                                    }
                                } else {
                                    MoudleUtils.dialogDismiss(builder);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private synchronized void initSetData(int is2, int is5) {

        initAnimate(is2);
        //        initXYState(is2);判断血氧状况
        MoudleUtils.textViewSetText(tv_num_xy, is2 + "");
        int i2 = (int) ((w / 20 * (is2 - 80)) - w2);
        int i5 = (int) ((w / 150 * (is5 - 40)) - w2);
        tv_line_bhd.setWidth(i2);
        tv_line_ml.setWidth(i5);
    }


    /**
     * 血氧上传接口
     *
     * @param now
     */
    private void initXyTask(int now) {
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get(this, "user_id", 0));
        String token = (String) SPUtils.get(this, "token", "");
        Call<UpoxygenBean> callBack = restApi.upoxygen(token, user_id, now + "");

        callBack.enqueue(new Callback<UpoxygenBean>() {
            @Override
            public void onResponse(Call<UpoxygenBean> call, Response<UpoxygenBean> response) {
                UpoxygenBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        MoudleUtils.broadcastUpdate(HealthBloodOxygenMonitorActivity.this, AppConfig.ACTION_XIN_LV);
                        if (bean.getInfo() != null) {
                            MoudleUtils.textViewSetText(tv_xy_state, bean.getInfo().getOxygen_grade());
                            tv_xy_state.setTextColor(Color.parseColor(bean.getInfo().getOxygen_vel()));
                            MoudleUtils.textViewSetText(tv_jl, bean.getInfo().getContent());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UpoxygenBean> call, Throwable t) {

            }
        });
    }


}
