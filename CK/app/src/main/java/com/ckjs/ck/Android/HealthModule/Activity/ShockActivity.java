package com.ckjs.ck.Android.HealthModule.Activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ckjs.ck.Android.HealthModule.Adapter.ShockAdapter;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.WheelView;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ShockActivity extends AppCompatActivity {
    @BindView(R.id.expand_list)
    ExpandableListView elistview;
    private Toolbar toolbar;
    private static final String[] LONGTIMESET = new String[]{"1", "2", "3", "4", "5", "6"};
    private static final String[] PREVENTLOST = new String[]{"3", "4", "5"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shock);
        ButterKnife.bind(this);
        initToolbar();


        registerReceiver(mGattUpdateReceiver, MoudleUtils.makeGattUpdateIntentFilter());
        initView();
        initData();

    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (AppConfig.ACTION_DATA_AVAILABLE_WRITE.equals(action)) {
                displayDataZd(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
            }else if (AppConfig.ACTION_GATT_DISCONNECTED.equals(action)){
                MoudleUtils.kyloadingDismiss(ky);
                finish();//手环断开时设置手环页面的圈圈消失以及页面的关闭
            }
        }
    };

    private void displayDataZd(String uuid, byte[] data) {
        if (uuid.contains("fff1")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 7) {
                        if (data[1] == 3) {
                            MoudleUtils.kyloadingDismiss(ky);
                            ToastUtils.showShort(ShockActivity.this,"设置久坐提醒成功！");
                        } else if (data[1] == 5) {
                            MoudleUtils.kyloadingDismiss(ky);
                            ToastUtils.showShort(ShockActivity.this,"久坐已开启,请设置时长");
                        } else if (data[1] == 6) {
                            MoudleUtils.kyloadingDismiss(ky);
                            ToastUtils.showShort(ShockActivity.this,"久坐已关闭");
                        }
                    } else if (data[0] == 8) {
                        if (data[1] == 1) {
                            ToastUtils.showShort(ShockActivity.this,"设置双向防丢米数成功");
                        }
                    } else if (data[0] == 9) {
                        if (data[1] == 2) {
                            MoudleUtils.kyloadingDismiss(ky);
                            ToastUtils.showShort(ShockActivity.this,"闹钟已关闭");
                        } else if (data[1] == 1) {
                            MoudleUtils.kyloadingDismiss(ky);
                            ToastUtils.showShort(ShockActivity.this,"闹钟已开启,请设置时间");
                        } else if (data[1] == 3) {
                            MoudleUtils.kyloadingDismiss(ky);
                            ToastUtils.showShort(ShockActivity.this,"闹钟提醒设置成功");
                        }
                    }
                }
            }
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

    /**
     * 标题栏
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("提醒设置");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    private String time = "4";
    private String distance = "4";
    ShockAdapter shockAdapter;

    private void initData() {
        shockAdapter = new ShockAdapter();
        shockAdapter.setContent(ShockActivity.this);
        elistview.setAdapter(shockAdapter);

        elistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(final ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (groupPosition == 2) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShockActivity.this);
                    final View outerView = LayoutInflater.from(ShockActivity.this).inflate(R.layout.wheel_view, null);
                    final WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                    final Button buttonSure = (Button) outerView.findViewById(R.id.sure);
                    wv.setOffset(2);
                    wv.setItems(Arrays.asList(LONGTIMESET));
                    wv.setSeletion(3);
                    alertDialog.setView(outerView).create();
                    final AlertDialog alert = alertDialog.create();
                    alert.setTitle("设置久坐提醒时间");
                    buttonSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initToJz(wv.getSeletedItem());
                            alert.dismiss();
                        }
                    });
                    alert.setCanceledOnTouchOutside(true);
                    alert.setCancelable(true);
                    alert.show();

                } else if (groupPosition == 5) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShockActivity.this);
                    View outerView = LayoutInflater.from(ShockActivity.this).inflate(R.layout.wheel_view, null);
                    final WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                    final Button buttonSure = (Button) outerView.findViewById(R.id.sure);
                    wv.setOffset(1);
                    wv.setItems(Arrays.asList(PREVENTLOST));
                    wv.setSeletion(1);
                    alertDialog.setView(outerView).create();
                    final AlertDialog alert = alertDialog.create();
                    alert.setTitle("设置防丢距离");
                    buttonSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initSetToFd(wv.getSeletedItem());
                            alert.dismiss();
                        }
                    });
                    alert.setCanceledOnTouchOutside(true);
                    alert.setCancelable(true);
                    alert.show();
                } else if (groupPosition == 3) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(System.currentTimeMillis());
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
                    new TimePickerDialog(ShockActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            if ((Boolean) SPUtils.get(ShockActivity.this, "clock", false)) {
                                if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                                    view.setIs24HourView(true);
                                    String hours = hourOfDay + "";
                                    if (hours.length() == 1) {
                                        hours = "0" + hourOfDay;
                                    }
                                    String mint = minute + "";
                                    if (mint.length() == 1) {
                                        mint = "0" + mint;
                                    }
                                    final String time = hours + ":" + mint;

                                    String s = "09" + "03" + MoudleUtils.checkData(hourOfDay) + MoudleUtils.checkData(minute);
                                    final byte[] bytes = DataUtils.hexStrToByteArray(s, 2);
                                    initShow();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", bytes);
                                            SPUtils.put(ShockActivity.this, "clockhour", time);
                                            if (shockAdapter != null) {
                                                shockAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }, 1000);
                                } else {
                                    ToastUtils.showShort(ShockActivity.this,"手环未连接");
                                }
                            } else {
                                ToastUtils.showShort(ShockActivity.this,"请先打开闹钟开关");
                            }
                        }
                    }, hour, minute, true).show();
                } else {
                    if (shockAdapter != null) {
                        shockAdapter.notifyDataSetChanged();
                    }
                }
                return true;
            }
        });
    }

    private void initSetToFd(String item) {
        if ((Boolean) SPUtils.get(ShockActivity.this, "antilost", false)) {
            if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                distance = item;
                SPUtils.put(ShockActivity.this, "losedistance", distance);
                if (shockAdapter != null) {
                    shockAdapter.notifyDataSetChanged();
                }


            } else {
                ToastUtils.showShort(ShockActivity.this,"手环未连接");
            }
        } else {
            ToastUtils.showShort(ShockActivity.this,"请先打开双向防丢开关");
        }
    }

    private void initToJz(String item) {
        if ((Boolean) SPUtils.get(ShockActivity.this, "longtime", false)) {
            if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                time = item;
                String s = "07" + "03" + MoudleUtils.checkData(Integer.parseInt(time)) + "00";
                final byte[] bytes = DataUtils.hexStrToByteArray(s, 2);
                initShow();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", bytes);
                        SPUtils.put(ShockActivity.this, "timeset", time);
                        if (shockAdapter != null) {
                            shockAdapter.notifyDataSetChanged();
                        }
                    }
                }, 1000);


            } else {
                ToastUtils.showShort(ShockActivity.this,"手环未连接");
            }
        } else {
            ToastUtils.showShort(ShockActivity.this,"请先打开久坐提醒开关");
        }
    }

    public static KyLoadingBuilder ky;

    private void initShow() {
        ky = new KyLoadingBuilder(ShockActivity.this);
        MoudleUtils.kyloadingShow(ky);
    }

    private void initView() {
        elistview.setGroupIndicator(null);//将控件默认的左边箭头去掉，
    }

}
