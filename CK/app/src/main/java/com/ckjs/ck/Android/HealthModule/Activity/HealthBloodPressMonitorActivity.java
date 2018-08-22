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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.UppressureBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

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

public class HealthBloodPressMonitorActivity extends AppCompatActivity {
    @BindView(R.id.tv_xy_week)
    TextView tv_xy_week;
    @BindView(R.id.ll_health_bloodoxygen_emergency)
    LinearLayout ll_health_bloodoxygen_emergency;
    @BindView(R.id.btn_bloodoxygen)
    Button btn_bloodoxygen;
    @BindView(R.id.tv_bloodpress_1)
    TextView tv_bloodpress_1;
    @BindView(R.id.tv_bloodpress_2)
    TextView tv_bloodpress_2;

    @BindView(R.id.tv_bloodpress_max3)
    TextView tv_bloodpress_max3;
    @BindView(R.id.tv_bloodpress_max4)
    TextView tv_bloodpress_max4;
    @BindView(R.id.tv_bloodpress_min3)
    TextView tv_bloodpress_min3;
    @BindView(R.id.tv_bloodpress_min4)
    TextView tv_bloodpress_min4;
    @BindView(R.id.tv_xy_state)
    TextView tv_xy_state;
    @BindView(R.id.tv_time)
    TextView tv_time;

    private Toolbar toolbar;
    private ProgressDialog builder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_bloodpress);
        initToolbar();
        ButterKnife.bind(this);
        builder = new ProgressDialog(this);
        registerReceiver(mGattUpdateReceiver, MoudleUtils.makeGattUpdateIntentFilter());
        AppConfig.EXTRA_DATA_STATE = 5;
        initData();
    }

    private void initData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String date = formatter.format(curDate);
        tv_time.setText(date);
    }

    @OnClick(R.id.tv_xy_week)
    void tvXyWeek() {
        startActivity(new Intent().setClass(HealthBloodPressMonitorActivity.this, HealthPressWeekActivity.class));
    }

    @OnClick(R.id.ll_health_bloodoxygen_emergency)
    void llHealthBloodoxygenEmergency() {
        startActivity(new Intent().setClass(HealthBloodPressMonitorActivity.this, HealthBloodPressEmergencyActivity.class));
    }

    @OnClick(R.id.btn_bloodoxygen)
    void test() {
        if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
            if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                if (!((boolean) (SPUtils.get("isSportMode", false)))) {
                    i = 0;
                    min4 = 100000000;
                    min3 = 100000000;
                    max3 = 0;
                    max3 = 0;
                    he = 0;
                    he4 = 0;
                    btn_bloodoxygen.setEnabled(false);
                    AppConfig.EXTRA_DATA_STATE = 5;
                    MoudleUtils.dialogShow(builder, true);
                    DataUtils.startHearDeng();
                } else {
                    ToastUtils.showShort(HealthBloodPressMonitorActivity.this,"请将手环模式调为日常模式");
                }
            } else {
                ToastUtils.showShort(HealthBloodPressMonitorActivity.this,"请等待数据同步结束");
            }
        } else {
            ToastUtils.showShort(HealthBloodPressMonitorActivity.this,"手环未连接");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initStopCl();
        if (mGattUpdateReceiver != null) {
            MoudleUtils.unregisterReceivermGattUpdateReceiver(this, mGattUpdateReceiver);
        }
    }

    private void initStopCl() {
        if (AppConfig.EXTRA_DATA_STATE == 5) {
            if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                    DataUtils.stopHearDeng();
                }
            }

        }
        btn_bloodoxygen.setEnabled(true);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("血压监测");
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
                btn_bloodoxygen.setEnabled(true);
                MoudleUtils.dialogDismiss(builder);
                //                ToastUtils.showShort("连接出现异常了哦");
            } else if (AppConfig.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

            } else if (AppConfig.ACTION_DATA_AVAILABLE.equals(action)) {

            } else if (AppConfig.ACTION_DATA_AVAILABLE_TIME.equals(action)) {
                final Intent intent1 = intent;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            if (AppConfig.EXTRA_DATA_STATE == 5) {
                                if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag)
                                    displayData(intent1.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent1.getByteArrayExtra(AppConfig.EXTRA_DATA));
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
                                MoudleUtils.dialogDismiss(builder);
                            }
                        }
                    }
                }
            }
        }
    }

    int i;
    int he;
    int he4;
    int max3, min3 = 1000;
    int max4, min4 = 1000;
    int n = 351;

    private synchronized void displayData(String uuid, byte[] data) {
        if (uuid.contains("fff5")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 1) {
                        if (data.length == 10) {
                            final int is[] = DataUtils.byteTo10(data);
                            if (is != null && is.length == 10) {
                                if (is[3] > 0 && is[4] > 0) {
                                    if (i == 0) {
                                        MoudleUtils.dialogDismiss(builder);
                                    }
                                    if (i < n - 1) {
                                        he = he + is[3];
                                        he4 = he4 + is[4];
                                        if (is[3] > max3) {
                                            max3 = is[3];
                                        }
                                        if (is[3] < min3) {
                                            min3 = is[3];
                                        }
                                        if (is[4] > max4) {
                                            max4 = is[4];
                                        }
                                        if (is[4] < min4) {
                                            min4 = is[4];
                                        }
                                        MoudleUtils.textViewSetText(tv_bloodpress_1, is[3] + "");//收高
                                        MoudleUtils.textViewSetText(tv_bloodpress_2, is[4] + "");

                                        MoudleUtils.textViewSetText(tv_bloodpress_max3, max3 + "mmHg");
                                        MoudleUtils.textViewSetText(tv_bloodpress_min3, min3 + "mmHg");
                                        MoudleUtils.textViewSetText(tv_bloodpress_max4, max4 + "mmHg");
                                        MoudleUtils.textViewSetText(tv_bloodpress_min4, min4 + "mmHg");
                                        i++;
                                    }
                                    if (i == n - 1) {
                                        i++;
                                        MoudleUtils.textViewSetText(tv_bloodpress_1, (he / (n - 1)) + "");//收高
                                        MoudleUtils.textViewSetText(tv_bloodpress_2, (he4 / (n - 1)) + "");
                                        initXyaTask(he / (n - 1), he4 / (n - 1));
                                        initStopCl();
                                    }
                                    //                                if (i < n - 1) {
                                    //                                    BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff5");
                                    //                                }
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

    /**
     * 血氧上传接口
     *
     * @param
     */
    private void initXyaTask(int nowG, int nowD) {
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get(this, "user_id", 0));
        String token = (String) SPUtils.get(this, "token", "");
        Call<UppressureBean> callBack = restApi.uppressure(token, user_id, nowG + "", nowD + "");

        callBack.enqueue(new Callback<UppressureBean>() {
            @Override
            public void onResponse(Call<UppressureBean> call, Response<UppressureBean> response) {
                UppressureBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        MoudleUtils.broadcastUpdate(HealthBloodPressMonitorActivity.this, AppConfig.ACTION_XIN_LV);
                        if (bean.getInfo() != null) {
                            MoudleUtils.textViewSetText(tv_xy_state, bean.getInfo().getPressure_grade());
                            tv_xy_state.setTextColor(Color.parseColor(bean.getInfo().getPressure_vel()));

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UppressureBean> call, Throwable t) {

            }
        });
    }

}
