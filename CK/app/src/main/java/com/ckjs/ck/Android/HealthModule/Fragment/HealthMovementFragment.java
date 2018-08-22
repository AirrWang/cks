package com.ckjs.ck.Android.HealthModule.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Activity.HealthBloodOxygenMonitorActivity;
import com.ckjs.ck.Android.HealthModule.Activity.HealthBloodPressMonitorActivity;
import com.ckjs.ck.Android.HealthModule.Activity.HealthHeartMonitorActivity;
import com.ckjs.ck.Android.HealthModule.Activity.HealthSleepMonitorActivity;
import com.ckjs.ck.Android.HealthModule.Activity.MainRunJiuPianActivity;
import com.ckjs.ck.Android.HealthModule.Activity.ShockActivity;
import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.HealthBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment.diatance;


/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */


public class HealthMovementFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;
    public static TextView tv_health_jibu;
    public static TextView tv_health_distance;
    public static TextView tv_health_expand;
    private TextView tv_time_now;


    private SimpleDraweeView sd_run;
    private TextView tv_health_heart;
    private TextView tv_health_bloodoxygen;
    private TextView tv_health_bloodpress;
    private TextView tv_xl_state;
    private TextView tv_xy_state;
    private TextView tv_sleep;
    private TextView tv_health_bloodpress_state;
    private TextView tv_sleep_state;
    private TextView tv_sport_mode;
    private AlertDialog alert;
    private View view1;
    public static TextView tv_health_sportmode;
    private SwipeRefreshLayout swipeRefreshLayoutHealth;


    public HealthMovementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().registerReceiver(mGattUpdateReceiver, MoudleUtils.makeGattUpdateIntentFilter());
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fragment_health_movement, container, false);
        initId();
        initStartStep();
        initHealthTask();
        return view;
    }

    private void initStartStep() {
        if (HomeFragment.tv_bu_shu != null) {
            String s = HomeFragment.tv_bu_shu.getText().toString().trim();
            if (s != null && !s.equals("")) {
                tv_health_jibu.setText(s);
            }
        }
        if (HomeFragment.tv_kcal != null) {
            String s = HomeFragment.tv_kcal.getText().toString().trim();
            if (s != null && !s.equals("")) {
                tv_health_expand.setText(s);
            }
        }
        float diatanceLs = (float) Math.round(diatance * 100) / 100;
        if (HealthMovementFragment.tv_health_distance != null) {
            HealthMovementFragment.tv_health_distance.setText(diatanceLs + "公里");
        }
    }


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (AppConfig.ACTION_GATT_CONNECTED.equals(action)) {

            } else if (AppConfig.ACTION_GATT_DISCONNECTED.equals(action)) {
                initStateRunHealth();
            } else if (AppConfig.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                initStateRunHealth();
            } else if (AppConfig.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
            } else if (AppConfig.ACTION_DATA_AVAILABLE_WRITE.equals(action)) {
                /**
                 * 在此处做相关运动非运动模式的改变
                 */
                displayDataFF1(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
            } else if (AppConfig.ACTION_XIN_LV.equals(action)) {
                initHealthTask();
            }
        }
    };

    private void displayDataFF1(String uuid, byte[] data) {
        if (uuid.contains("fff1")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == (byte) 0x0B) {
                        if (data[1] == 1) {
                            tv_sport_mode.setText("运动\n模式");
                            SPUtils.put(getActivity(), "isSportMode", true);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SPUtils.put(getActivity(), "isSportModeRun", true);
                                    //true时可以手机运动模式下的健康数据
                                }
                            }, 100000);
                        } else if (data[1] == 2) {
                            tv_sport_mode.setText("日常\n模式");
                            SPUtils.put(getActivity(), "isSportMode", false);
                            SPUtils.put(getActivity(), "isSportModeRun", false);
                        }
                    }
                }
            }
        }
    }

    private void displayData(String uuid, byte[] data) {
        if (uuid.contains("fff5")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 1) {
                        if (data.length == 10) {
                            int s16[] = DataUtils.byteTo10(data);
                            if (s16.length == 10) {
                                initHeartState(s16[1]);
                                initXYState(s16);
                                tv_health_bloodpress.setText(s16[3] + "/" + s16[4] + "kpa");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 血氧95%－100%正常，<90%低血氧症，90%－95%供氧不足
     *
     * @param s16
     */
    private void initXYState(int[] s16) {
        tv_health_bloodoxygen.setText(s16[2] + "%");
        int i = s16[2];
        if (i >= 94 && i <= 100) {
            MoudleUtils.textViewSetText(tv_xy_state, "正常");
        } else if (i < 90) {
            MoudleUtils.textViewSetText(tv_xy_state, "低血氧症");
        } else if (i > 90 && i < 94) {
            MoudleUtils.textViewSetText(tv_xy_state, "供氧不足");
        }
    }

    /**
     * 心率，脉率（<60偏低，60-100正常，100-150正常运动，>150剧烈运动，>190不正常）
     *
     * @param i
     */
    private void initHeartState(int i) {
        tv_health_heart.setText(i + "bmp");
        if (i < 60) {
            MoudleUtils.textViewSetText(tv_xl_state, "偏低");
        } else if (i >= 60 && i <= 100) {
            MoudleUtils.textViewSetText(tv_xl_state, "正常");
        } else if (i > 100 && i <= 150) {
            MoudleUtils.textViewSetText(tv_xl_state, "正常运动");
        } else if (i > 150 && i <= 190) {
            MoudleUtils.textViewSetText(tv_xl_state, "剧烈运动");
        } else if (i > 190) {
            MoudleUtils.textViewSetText(tv_xl_state, "不正常");
        }

    }

    private synchronized void initTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        tv_time_now.setText(str);
    }


    private void initId() {

        LinearLayout ll_health_sleep = (LinearLayout) view.findViewById(R.id.ll_health_sleep);
        LinearLayout ll_health_heart = (LinearLayout) view.findViewById(R.id.ll_health_heart);
        LinearLayout ll_health_shake_phone = (LinearLayout) view.findViewById(R.id.ll_health_shake_phone);
        LinearLayout ll_health_bloodpress = (LinearLayout) view.findViewById(R.id.ll_health_bloodpress);
        LinearLayout ll_health_bloodoxygen = (LinearLayout) view.findViewById(R.id.ll_health_bloodoxygen);
        tv_health_jibu = (TextView) view.findViewById(R.id.tv_health_jibu);
        tv_time_now = (TextView) view.findViewById(R.id.tv_time_now);
        tv_xl_state = (TextView) view.findViewById(R.id.tv_xl_state);
        tv_xy_state = (TextView) view.findViewById(R.id.tv_xy_state);
        tv_health_bloodpress_state = (TextView) view.findViewById(R.id.tv_health_bloodpress_state);
        tv_sleep_state = (TextView) view.findViewById(R.id.tv_sleep_state);
        tv_sleep = (TextView) view.findViewById(R.id.tv_sleep);
        //TODO

        tv_health_distance = (TextView) view.findViewById(R.id.tv_health_distance);
        tv_health_expand = (TextView) view.findViewById(R.id.tv_health_expand_new);
        sd_run = (SimpleDraweeView) view.findViewById(R.id.sd_run);
        LinearLayout sd_health_sleep = (LinearLayout) view.findViewById(R.id.sd_health_sleep);
        LinearLayout sd_health_heart = (LinearLayout) view.findViewById(R.id.sd_health_heart);
        LinearLayout sd_health_bloodpress = (LinearLayout) view.findViewById(R.id.sd_health_bloodpress);
        LinearLayout sd_health_bloodoxygen = (LinearLayout) view.findViewById(R.id.sd_health_bloodoxygen);

        tv_health_heart = (TextView) view.findViewById(R.id.tv_health_heart);
        tv_health_bloodoxygen = (TextView) view.findViewById(R.id.tv_health_bloodoxygen);
        tv_health_bloodpress = (TextView) view.findViewById(R.id.tv_health_bloodpress);
        tv_health_sportmode = (TextView) view.findViewById(R.id.tv_health_sportmode);

        ll_health_bloodoxygen.setOnClickListener(this);
        ll_health_bloodpress.setOnClickListener(this);
        ll_health_shake_phone.setOnClickListener(this);
        ll_health_heart.setOnClickListener(this);
        ll_health_sleep.setOnClickListener(this);
        sd_run.setOnClickListener(this);
        sd_health_sleep.setOnClickListener(this);
        sd_health_heart.setOnClickListener(this);
        sd_health_bloodpress.setOnClickListener(this);
        sd_health_bloodoxygen.setOnClickListener(this);

        tv_sport_mode = (TextView) view.findViewById(R.id.tv_sport_mode);
        tv_sport_mode.setOnClickListener(this);
        swipeRefreshLayoutHealth = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutHealth);
        swipeRefreshLayoutHealth.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayoutHealth.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutHealth.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutHealth.setOnRefreshListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_health_sleep:  //睡眠监测
            case R.id.sd_health_sleep:
                startActivity(new Intent().setClass(getActivity(), HealthSleepMonitorActivity.class));
                break;
            case R.id.ll_health_heart:  //心率监测
            case R.id.sd_health_heart:
                AppConfig.EXTRA_DATA_STATE = 3;//0,1,2,3,4,5首页，健康监测，睡眠测量，心率测量，血氧测量，血压测量
                startActivity(new Intent().setClass(getActivity(), HealthHeartMonitorActivity.class));
                break;
            case R.id.ll_health_bloodpress:   //血压监测
            case R.id.sd_health_bloodpress:
                AppConfig.EXTRA_DATA_STATE = 5;//0,1,2,3,4,5首页，健康监测，睡眠测量，心率测量，血氧测量，血压测量
                startActivity(new Intent().setClass(getActivity(), HealthBloodPressMonitorActivity.class));
                break;
            case R.id.ll_health_shake_phone:  //震动提醒  电话
                if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                        startActivity(new Intent().setClass(getActivity(), ShockActivity.class));
                    } else {
                        ToastUtils.showShortNotInternet("请等待数据同步结束");
                    }
                } else {
                    ToastUtils.showShortNotInternet("手环未连接");
                }
                break;
            case R.id.sd_run:  //轨迹  电话
                startActivity(new Intent().setClass(getActivity(), MainRunJiuPianActivity.class));
                break;
            case R.id.ll_health_bloodoxygen://血氧监测
            case R.id.sd_health_bloodoxygen:

                AppConfig.EXTRA_DATA_STATE = 4;//0,1,2,3,4,5首页，健康监测，睡眠测量，心率测量，血氧测量，血压测量
                startActivity(new Intent().setClass(getActivity(), HealthBloodOxygenMonitorActivity.class));

                break;
            case R.id.tv_sport_mode:
                toChangeMode();
                break;
            case R.id.ll_sport_mode1:
                if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                        if (alert != null) {
                            alert.dismiss();
                        }
                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x0B, 0x02, 0x00, 0x00});
                    } else {
                        ToastUtils.showShortNotInternet("请等待数据同步结束哦，很快的！");
                    }
                } else {
                    ToastUtils.showShortNotInternet("手环未连接");
                }
                break;
            case R.id.ll_sport_mode2:
                if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                        if (alert != null) {
                            alert.dismiss();
                        }
                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x0B, 0x01, 0x00, 0x00});
                    } else {
                        ToastUtils.showShortNotInternet("请等待数据同步结束哦，很快的！");
                    }
                } else {
                    ToastUtils.showShortNotInternet("手环未连接");
                }
                break;
        }
    }

    private void toChangeMode() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        view1 = inflater.inflate(R.layout.dialog_sport_mode, null);
        LinearLayout ll_sport_mode1 = (LinearLayout) view1.findViewById(R.id.ll_sport_mode1);
        ll_sport_mode1.setOnClickListener(this);
        LinearLayout ll_sport_mode2 = (LinearLayout) view1.findViewById(R.id.ll_sport_mode2);
        ll_sport_mode2.setOnClickListener(this);
        TextView tv_mode_1 = (TextView) view1.findViewById(R.id.tv_mode_1);
        TextView tv_mode_2 = (TextView) view1.findViewById(R.id.tv_mode_2);
        if (tv_mode_1 == null || tv_mode_2 == null)
            return;
        if ((Boolean) SPUtils.get(getActivity(), "isSportMode", false)) {
            tv_mode_2.setTextColor(getActivity().getResources().getColor(R.color.c_4491F2));
            tv_mode_1.setTextColor(getActivity().getResources().getColor(R.color.c_33));
        } else {
            tv_mode_1.setTextColor(getActivity().getResources().getColor(R.color.c_4491F2));
            tv_mode_2.setTextColor(getActivity().getResources().getColor(R.color.c_33));
        }

        builder.setView(view1);

        alert = builder.create();

        alert.show();

        android.view.WindowManager.LayoutParams p = alert.getWindow().getAttributes();  //获取对话框当前的参数值
        //        p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.8);    //宽度设置为屏幕的0.5
        alert.getWindow().setAttributes(p);     //设置生效
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        initGetTime();
    }

    private void initGetTime() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        initTime();
                        initStateRunHealth();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGattUpdateReceiver != null) {
            MoudleUtils.unregisterReceivermGattUpdateReceiver(getActivity(), mGattUpdateReceiver);
        }
    }

    /**
     * 更改运动或日常模式状态
     */
    synchronized void initStateRunHealth() {
        if (tv_sport_mode == null)
            return;

        if ((boolean) (SPUtils.get("isSportMode", false))) {
            tv_sport_mode.setText("运动\n模式");
        } else {
            tv_sport_mode.setText("日常\n模式");
        }
    }


    /**
     * 获取健康数据显示
     */
    private void initHealthTask() {
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        Call<HealthBean> callBack = restApi.health(token, user_id);

        callBack.enqueue(new Callback<HealthBean>() {
            @Override
            public void onResponse(Call<HealthBean> call, Response<HealthBean> response) {
                HealthBean bean = response.body();
                if (bean == null)
                    return;
                if (bean.getStatus().equals("1")) {
                    if (bean.getInfo() != null) {

                        MoudleUtils.textViewSetText(tv_health_heart, bean.getInfo().getRate());
                        tv_xl_state.setTextColor(Color.parseColor(bean.getInfo().getRate_vel()));
                        MoudleUtils.textViewSetText(tv_xl_state, bean.getInfo().getRate_grade());

                        MoudleUtils.textViewSetText(tv_health_bloodoxygen, bean.getInfo().getOxygen());
                        MoudleUtils.textViewSetText(tv_xy_state, bean.getInfo().getOxygen_grade());
                        tv_xy_state.setTextColor(Color.parseColor(bean.getInfo().getOxygen_vel()));

                        MoudleUtils.textViewSetText(tv_health_bloodpress, bean.getInfo().getPressure());
                        MoudleUtils.textViewSetText(tv_health_bloodpress_state, bean.getInfo().getPressure_grade());
                        tv_health_bloodpress_state.setTextColor(Color.parseColor(bean.getInfo().getPressure_vel()));

                        MoudleUtils.textViewSetText(tv_sleep, bean.getInfo().getSleep());
                        MoudleUtils.textViewSetText(tv_sleep_state, bean.getInfo().getSleep_grade());
                        tv_sleep_state.setTextColor(Color.parseColor(bean.getInfo().getSleep_vel()));

                    }
                } else if (bean.getStatus().equals("2")) {
                    MoudleUtils.initStatusTwo(getActivity(), true);
                }
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHealth);

            }

            @Override
            public void onFailure(Call<HealthBean> call, Throwable t) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHealth);
            }
        });


    }

    @Override
    public void onRefresh() {
        onResume();
        initHealthTask();
    }
}
