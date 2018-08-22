package com.ckjs.ck.Android.HealthModule.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.UprateBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.PathView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class HealthHeartMonitorActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tv_week)
    TextView tv_week;
    @BindView(R.id.tv_line)
    TextView tv_line;
    @BindView(R.id.ll_health_bloodoxygen_emergency)
    LinearLayout ll_health_bloodoxygen_emergency;
    @BindView(R.id.ll_line)
    LinearLayout ll_line;

    private Toolbar toolbar;
    private PathView snakeViewNew;
    private TextView text;
    private boolean stop = false;
    private int position = 0;
    private List<Integer> lists = new ArrayList<>();
    private ProgressDialog builder;
    private Button btn_heart_test;
    private TextView tv_max;
    private TextView tv_min;
    private TextView tv_time;
    private float w;
    private float w2;
    private int avg;
    private int he;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_heart_monitor);
        initId();
        initToolbar();
        ButterKnife.bind(this);
        w = (float) (ScreenUtils.getScreenWidth() * 0.902777778);
        w2 = (float) (ScreenUtils.getScreenWidth() / 102.8571429);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) w, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll_line.setLayoutParams(layoutParams);
        builder = new ProgressDialog(this);
        registerReceiver(mGattUpdateReceiver, MoudleUtils.makeGattUpdateIntentFilter());
        AppConfig.EXTRA_DATA_STATE = 3;//0,1,2,3,4,5首页，健康监测，睡眠测量，心率测量，血氧测量，血压测量
        initData();
    }

    private void initData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String date = formatter.format(curDate);
        tv_time.setText(date);
    }

    private void initId() {
        snakeViewNew = (PathView) findViewById(R.id.snake_new);
        text = (TextView) findViewById(R.id.tv_num_bpm);
        tv_max = (TextView) findViewById(R.id.tv_max);
        tv_min = (TextView) findViewById(R.id.tv_min);
        tv_time = (TextView) findViewById(R.id.tv_time);

        btn_heart_test = (Button) findViewById(R.id.btn_heart_test);
        btn_heart_test.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stop = true;
    }

    private int imax, imin = 1000, now;

    private synchronized void generateValue() {
        float value = lists.get(position);
        addPoint(value, (float) (value));
        position++;
        if (position == n - 1) {
            snakeViewNew.setaBoolean(false);
            avg = he / lists.size();
            initSetData(avg);
            if (avg > 0) {
                initPostHeartTask(avg, imax, imin);
            }
            initStop();
        }
    }


    // 顯示心跳線
    private synchronized void addPoint(final float value, final float viewValue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initSetData(value);
                snakeViewNew.setValue(viewValue);
            }
        });
    }

    void initSetData(float value) {
        MoudleUtils.textViewSetText(tv_max, imax + "bpm");
        MoudleUtils.textViewSetText(tv_min, imin + "bpm");
        int i = (int) ((w / 130 * (value - 60)) - w2);
        tv_line.setWidth(i);
        text.setText(Integer.toString((int) value));
    }


    private void initStopCl() {
        if (AppConfig.EXTRA_DATA_STATE == 3) {
            if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                    DataUtils.stopHearDeng();
                }
            }
        }
    }


    /**
     * 跳转到心率突发状况
     */
    @OnClick(R.id.ll_health_bloodoxygen_emergency)
    void llHealthBloodoxygenEmergency() {
        startActivity(new Intent().setClass(HealthHeartMonitorActivity.this, HealthHeartEmergencyActivity.class));
    }

    @OnClick(R.id.tv_week)
    void tvWeek() {
        startActivity(new Intent().setClass(HealthHeartMonitorActivity.this, HealthHeartWeekActivity.class));
    }

    /**
     * 标题栏
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("心率监测");
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

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (AppConfig.ACTION_GATT_CONNECTED.equals(action)) {

            } else if (AppConfig.ACTION_GATT_DISCONNECTED.equals(action)) {
                stop = true;
                MoudleUtils.dialogDismiss(builder);
                btn_heart_test.setEnabled(true);
            } else if (AppConfig.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

            } else if (AppConfig.ACTION_DATA_AVAILABLE.equals(action)) {

            } else if (AppConfig.ACTION_DATA_AVAILABLE_TIME.equals(action)) {
                if (AppConfig.EXTRA_DATA_STATE == 3) {
                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag)
                        displayData(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
                }
            } else if (AppConfig.ACTION_DATA_AVAILABLE_WRITE.equals(action)) {
                //new byte[]{0x05, 0x00, 0x03, 0x00}
                displayDataWrite(intent);
            }
        }
    };

    /**
     * 判断是否开启心率设备成功
     */
    private synchronized void displayDataWrite(Intent intent) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initStop();
        if (mGattUpdateReceiver != null) {
            MoudleUtils.unregisterReceivermGattUpdateReceiver(this, mGattUpdateReceiver);
        }
    }

    int n = 351;


    private synchronized void displayData(String uuid, byte[] data) {
        if (uuid.contains("fff5")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 1) {
                        if (data.length == 10) {
                            final int is[] = DataUtils.byteTo10(data);
                            if (is != null && is.length == 10) {

                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        synchronized (this) {
                                            if (is[1] > 0) {
                                                if (lists.size() < n - 1) {
                                                    stop = false;
                                                    now = is[1];
                                                    he = he + now;
                                                    if (is[1] > imax) {
                                                        imax = is[1];
                                                    }
                                                    if (is[1] < imin) {
                                                        imin = is[1];
                                                    }
                                                    lists.add(is[1]);
                                                    if (lists.size() == 1) {
                                                        MoudleUtils.dialogDismiss(builder);
                                                    }
                                                    if (lists.size() >= 1) {
                                                        if (!stop) {
                                                            generateValue();
                                                        }
                                                    }
                                                }
                                            } else {
                                                MoudleUtils.dialogDismiss(builder);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 心率上传接口
     *
     * @param avg
     * @param imax
     * @param imin
     */
    private void initPostHeartTask(int avg, int imax, int imin) {
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(HealthHeartMonitorActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(HealthHeartMonitorActivity.this, "token", "");
        Call<UprateBean> callBack = restApi.uprate(user_id + "", token, avg + "", imax + "", imin + "");

        callBack.enqueue(new Callback<UprateBean>() {
            @Override
            public void onResponse(Call<UprateBean> call, Response<UprateBean> response) {
                MoudleUtils.broadcastUpdate(HealthHeartMonitorActivity.this, AppConfig.ACTION_XIN_LV);
            }

            @Override
            public void onFailure(Call<UprateBean> call, Throwable t) {

            }
        });
    }


    private void initStop() {
        initStopCl();
        initRegistData();

    }

    private void initRegistData() {
        stop = true;
        if (btn_heart_test != null) {
            btn_heart_test.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_heart_test:
                if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                        if (!((boolean) (SPUtils.get("isSportMode", false)))) {
                            snakeViewNew.setMaxValue(165); //設定圖形數字最大值
                            AppConfig.EXTRA_DATA_STATE = 3;//0,1,2,3,4,5首页，健康监测，睡眠测量，心率测量，血氧测量，血压测量
                            imax = 0;
                            imin = 100000000;
                            position = 0;
                            stop = true;
                            now = 0;
                            avg = 0;
                            he = 0;
                            btn_heart_test.setEnabled(false);
                            if (lists != null) {
                                lists.clear();
                            } else {
                                lists = new ArrayList<>();
                            }
                            MoudleUtils.dialogShow(builder, true);
                            DataUtils.startHearDeng();
                        } else {
                            ToastUtils.showShort(HealthHeartMonitorActivity.this,"请将手环模式调为日常模式");
                        }
                    } else {
                        ToastUtils.showShort(HealthHeartMonitorActivity.this,"请等待数据同步结束");
                    }
                } else {
                    ToastUtils.showShort(HealthHeartMonitorActivity.this,"手环未连接");
                }
                break;
        }
    }
}

