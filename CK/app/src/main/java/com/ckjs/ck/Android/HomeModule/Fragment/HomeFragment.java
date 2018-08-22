package com.ckjs.ck.Android.HomeModule.Fragment;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.ckjs.ck.Android.HealthModule.Fragment.HealthMovementFragment;
import com.ckjs.ck.Android.HomeModule.Activity.CkSjActivity;
import com.ckjs.ck.Android.HomeModule.Activity.DeviceControlActivity;
import com.ckjs.ck.Android.HomeModule.Activity.HomeAcActivity;
import com.ckjs.ck.Android.HomeModule.Activity.HomeHSFNewActivity;
import com.ckjs.ck.Android.HomeModule.Activity.HomeJsfMoreActivity;
import com.ckjs.ck.Android.HomeModule.Activity.HomeShopH5Activity;
import com.ckjs.ck.Android.HomeModule.Activity.HomeTjActivity;
import com.ckjs.ck.Android.HomeModule.Activity.PoiSearchListJsfActivity;
import com.ckjs.ck.Android.HomeModule.Activity.PunchCardDialog;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.GetSignBean;
import com.ckjs.ck.Bean.GetgymBean;
import com.ckjs.ck.Bean.HealthBean;
import com.ckjs.ck.Bean.PostHealthBean;
import com.ckjs.ck.Bean.RecordBean;
import com.ckjs.ck.Bean.SleepBean;
import com.ckjs.ck.Bean.StepPostBean;
import com.ckjs.ck.Manager.NotifyActivityToMessageManager;
import com.ckjs.ck.Manager.NotifySignBeanMessageManager;
import com.ckjs.ck.Manager.NotifyToHomeSignBeanMessageManager;
import com.ckjs.ck.Message.NotifyActicityToMessage;
import com.ckjs.ck.Message.NotifyToHomeSignBeanMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Service.StepService;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.HSHTool;
import com.ckjs.ck.Tool.Location.Utils;
import com.ckjs.ck.Tool.LogUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.RememberStep.Constants;
import com.ckjs.ck.Tool.RememberStep.CountDownTimer;
import com.ckjs.ck.Tool.RememberStep.StepDcretor;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.SynchronizeDataTool;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ckjs.ck.Tool.MoudleUtils.broadcastUpdate;


/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HomeFragment extends Fragment implements AMapLocationListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, NotifyActicityToMessage, NotifyToHomeSignBeanMessage, Handler.Callback {


    private TextView bind_bracelet;
    private View viewFoot;

    private SwipeRefreshLayout swipeRefreshLayoutHome;
    private TextView button;
    private GetSignBean.GetSignInfoBean listSign;
    private boolean flagFist;

    private LinearLayout ll_bind_bracelet;


    private LinearLayout ll_fu_jin;

    private TextView textViewDingWei;
    private LinearLayout llBgJf;
    private LinearLayout tv_hJF;
    private float otherFat;


    @BindView(R.id.ll_activity)
    LinearLayout ll_activity;
    @BindView(R.id.ll_read)
    LinearLayout ll_read;

    @BindView(R.id.tv_again)
    TextView tv_again;

    public static TextView tv_bu_shu;
    public static TextView tv_kcal;
    public static float diatance;
    private float one;
    private float expand;
    //循环取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL = 100;
    private Handler delayHandler;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    public static int bushu;
    public int n_step;
    private String tag = "home";
    private LinearLayout ll_bind_bracelet_new;
    private TextView tv_home_heart;
    private TextView tv_home_bloodoxygen;
    private TextView tv_sleep;
    private double lat;
    private double lon;
    private String city = "";
    private int day1;
    private int day2;
    private int day3;
    private int month1;
    private int month2;
    private int month3;
    private int month;
    private int day;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        viewFoot = inflater.inflate(R.layout.home_foot_layout, container, false);
        ButterKnife.bind(this, viewFoot);
        initId();
        initDw();
        initToolbar();
        init();
        setupService();
        getActivity().registerReceiver(mGattUpdateReceiver, MoudleUtils.makeGattUpdateIntentFilter());
        onRefresh();
        initHSHStepStart();
        try {
            if ((SPUtils.get("zq", "0") + "").equals("0")) {
                initToZq();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewFoot;
    }

    public static void startTimeCount() {
        time = new TimeCountHSh(duration, 1000);
        time.start();
    }

    private static TimeCountHSh time;
    private static int duration = 1000 * 60 * 3;

    static class TimeCountHSh extends CountDownTimer {
        public TimeCountHSh(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始同步步数
            time.cancel();
            if (AppConfig.bluetoothGattServices != null && AppConfig.mBluetoothGatt != null) {
                if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                    initStartTbStep("timeUp");
                }
                startTimeCount();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

    /**
     * 切换为手机传感器
     */
    private void changeUserStepType(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_step_type, null);
        builder.setView(view);
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);
        Button button = (Button) view.findViewById(R.id.buttonTrue);
        Button button4 = (Button) view.findViewById(R.id.buttonFalse);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nNot = 0;
                if (!StepDcretor.flagStep) {
                    SPUtils.put(CkApplication.getInstance(), "nameStepType", "0");
                    MoudleUtils.broadcastUpdate(CkApplication.getInstance(), AppConfig.ACTION_PHONE);
                } else {
                    ToastUtils.showShortNotInternet("手环连接中，无法切换为手机传感器");
                }
                alert.dismiss();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nNot = 0;
                alert.dismiss();
            }
        });
        alert.show();
    }


    int nNot;
    private int numStep;
    private int numStepI;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (AppConfig.ACTION_GATT_CONNECTED.equals(action)) {
                i = 0;
                h = 0;
                nNot = -1;
                showHomeAcssess(true);
                initTvShowState(getResources().getText(R.string.buletooth_start));
            } else if (AppConfig.ACTION_GATT_DISCONNECTED.equals(action)) {
                nNot++;
                if (nNot == 3) {
                    changeUserStepType(getActivity());
                }
                i = 0;
                h = 0;
                flagStart = false;
                initTvShowState(getResources().getText(R.string.buletooth_again));
                AppConfig.NEWSTATE = 0;
            } else if (AppConfig.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                initTvShowState(getResources().getText(R.string.buletooth_dl));
                initStartHsH();
            } else if (AppConfig.ACTION_DATA_AVAILABLE.equals(action)) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            initDianLiang(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
                            initReadSleep(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
                        }
                    }
                });
            } else if (AppConfig.ACTION_DATA_AVAILABLE_TIME.equals(action)) {
                final Intent intent2 = intent;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            //                            initHealth(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
                            initPostHsHData(intent2);
                        }
                    }
                });
                initBushu(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            initReadStep(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
                        }
                    }
                });
            } else if (AppConfig.ACTION_DATA_AVAILABLE_WRITE.equals(action)) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            displayStart(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));

                            initStartFF4IS(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
                            initCheckSleep(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
                        }
                    }
                });

            } else if (AppConfig.ACTION_FD.equals(action)) {
                if ((boolean) SPUtils.get(CkApplication.getInstance(), "antilost", false)) {
                    final int iv = intent.getIntExtra(AppConfig.EXTRA_VALUE, 0);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                int myiRssi = Math.abs(iv);
                                intiToISFd(myiRssi);
                            }
                        }
                    });
                }
            } else if (AppConfig.ACTION_LOGIN_OUT.equals(action)) {
                getActivity().finish();
            } else if (AppConfig.ACTION_XIN_LV.equals(action)) {
                initHealthTask();
            } else if (AppConfig.ACTION_LOGIN.equals(action)) {
                initToZq();
            } else if (AppConfig.ACTION_START_NOT.equals(action)) {
                initStartHsH();
            } else if (AppConfig.ACTION_HSH_STEP_0.equals(action)) {
                String data = (String) SPUtils.get(CkApplication.getInstance(), "hsh_step_data", StepService.getTodayDate());
                if (!data.equals(StepService.getTodayDate())) {
                    SPUtils.put(CkApplication.getInstance(), "hsh_step_data", StepService.getTodayDate());
                    initSteps(0);
                }
            } else if (AppConfig.ACTION_STEP_TB.equals(action)) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            numStep = intent.getIntExtra(AppConfig.EXTRA_VALUE, 0);
                            if (numStep >= 3) {
                                numStep = 3;
                            }
                            initStarTbStepData(numStep);
                        }
                    }
                });

            }
        }
    };

    private synchronized void initReadStep(String uuid, byte[] data) {
        if (uuid.contains("fff4")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] != (byte) 0x20) {
                        //                        LogUtils.d(data.length + ",data[0]initReadStep:" + data[0]);
                        if (data.length == 5) {
                            String[] s = DataUtils.byteTo10S(data);
                            String st = s[2] + s[1];
                            int anInt = Integer.parseInt(st, 16);
                            initStep(month, day, anInt);
                            numStepI++;
                            if (numStepI < numStep) {
                                if (numStepI == 1) {
                                    month = month2 + 1;
                                    day = day2;
                                    toReadStep();
                                } else if (numStepI == 2) {
                                    month = month3 + 1;
                                    day = day3;
                                    toReadStep();
                                }
                            }
                            if (numStepI == numStep) {
                                initToTbPost(listStep);
                                SynchronizeDataTool.initGetHttpTongBuData();
                            }
                        }
                    }
                }
            }
        }
    }

    private void initStepChuShiHa() {
        numStepI = 0;
        numStep = 0;
        day = 0;
        month = 0;
        day1 = 0;
        day2 = 0;
        day3 = 0;
        month1 = 0;
        month2 = 0;
        month3 = 0;
        if (listStep == null) {
            listStep = new ArrayList<>();
        } else {
            listStep.clear();
        }
    }

    private void initToTbPost(final List<StepPostBean> listStep) {
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        Call<AcceptBean> callBack = restApi.upsteps(token, user_id, SynchronizeDataTool.initToStepJson(listStep));

        callBack.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {

            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {

            }
        });
    }

    /**
     * "month": "5",
     * "day": "13",
     * "steps": "85",
     * "fat": "130",
     * "mileage": "130",
     */
    private synchronized void initStep(int month, int day, int step) {

        int height = (int) SPUtils.get("height", 0);
        one = (float) (height * 0.414);//一步的距离

        float mileage = one * step / 100.0f;//米数
        mileage = (float) Math.round(mileage * 10) / 10;

        float diatanceOneKm = one * step / 100000.0f;//千米
        float weight = 0;
        weight = (float) SPUtils.get("weight", weight);

        float fat = (float) (weight * 1.036 * diatanceOneKm);//卡路里数
        fat = (float) Math.round(fat * 100) / 100;

        StepPostBean stepPostBean = new StepPostBean();
        stepPostBean.setMonth(month);
        stepPostBean.setDay(day);
        stepPostBean.setSteps(step);
        stepPostBean.setFat(MoudleUtils.roundDouble(fat, 2));
        stepPostBean.setMileage(MoudleUtils.roundDouble(mileage, 2));
        listStep.add(stepPostBean);
    }

    private synchronized void initStarTbStepData(int num) {
        if (num == 0) {
            SynchronizeDataTool.initGetHttpTongBuData();
            return;
        }
        if (num == 1) {
            initData1();
        } else if (num == 2) {
            initData1();
            initData2();
        } else if (num >= 3) {
            initData1();
            initData2();
            initData3();
        }
        month = month1 + 1;
        day = day1;
        stepReadStart();
    }

    /**
     * 数据同步
     */
    private void stepReadStart() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //用来进行数据同步开启广播
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff4", AppConfig.FFF4);
                toReadStep();
            }
        }, 1000);
    }

    private void toReadStep() {
        /**
         * 数据同步，睡眠数据
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("stepRead");
                HSHTool.stepRead(month, day);
            }
        }, 1000);
    }

    private void initData3() {
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DATE, -3);//得到前3天
        day3 = calendar3.get(Calendar.DAY_OF_MONTH);
        month3 = calendar3.get(Calendar.MONTH);
    }

    private void initData2() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, -2);//得到前2天
        day2 = calendar2.get(Calendar.DAY_OF_MONTH);
        month2 = calendar2.get(Calendar.MONTH);

    }

    private void initData1() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);//得到前一天
        day1 = calendar.get(Calendar.DAY_OF_MONTH);
        month1 = calendar.get(Calendar.MONTH);

    }

    private synchronized void initHSHStep(final int anInt) {
        (getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initSteps(anInt);
                if (!flagTrueHSh) {
                    flagTrueHSh = true;
                    initData(anInt);
                }
            }
        });

    }

    /**
     * 显示上次的步数
     */
    private synchronized void initHSHStepStart() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String nameStep = "0";
                nameStep = (String) SPUtils.get(CkApplication.getInstance(), "nameStepType", nameStep);
                if (nameStep.equals("1")) {
                    initHshStep();
                    int anInt = 0;
                    anInt = (int) SPUtils.get(CkApplication.getInstance(), "hsh_step", anInt);
                    initSteps(anInt);
                    if (!flagTrueHSh) {
                        flagTrueHSh = true;
                        initData(anInt);
                    }
                }
            }
        });
    }

    /**
     * 更新本地步数消耗卡路里
     *
     * @param anInt
     */
    private void initSteps(int anInt) {
        if (HealthMovementFragment.tv_health_jibu != null) {
            HealthMovementFragment.tv_health_jibu.setText(anInt + "步");
        }
        tv_bu_shu.setText(anInt + "步");
        setDistance(anInt);
        setExpand();
    }

    private static void initHshStep() {
        if (AppConfig.loginStep > 0) {
            SPUtils.put(CkApplication.getInstance(), "hsh_step", AppConfig.loginStep);
            AppConfig.loginStep = 0;
        }
        String data = (String) SPUtils.get(CkApplication.getInstance(), "hsh_step_data", StepService.getTodayDate());
        if (!data.equals(StepService.getTodayDate())) {
            SPUtils.put(CkApplication.getInstance(), "hsh_step_data", StepService.getTodayDate());
            SPUtils.remove(CkApplication.getInstance(), "hsh_step");
        }
    }

    int n;

    /**
     * 显示首页步数
     *
     * @param uuid
     * @param data
     */
    private synchronized void initBushu(String uuid, byte[] data) {
        ThreadFFF4 fff = new ThreadFFF4(uuid, data);
        Thread tb = new Thread(fff, (n++) + "");
        tb.start();

    }

    public class ThreadFFF4 implements Runnable {
        byte[] data;
        String uuid;

        public ThreadFFF4(String uuid, byte[] data) {
            this.data = data;
            this.uuid = uuid;
        }


        public void run() {
            synchronized (ThreadFFF4.class) {
                initFFF4(uuid, data);
            }
        }


    }

    private void initFFF4(String uuid, byte[] data) {
        if (uuid.contains("fff4")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == (byte) 0x20) {
                        //                        Log.i(AppConfig.TAG, data.length + ",data[0]fff4:" + data[0]);
                        if (data.length == 5) {
                            initTimeStep(data);
                            initToTongBu();
                        }
                    }
                }
            }
        }
    }

    private synchronized void initTimeStep(byte[] data) {
        String[] s = DataUtils.byteTo10S(data);
        String st = s[2] + s[1];
        int anInt = Integer.parseInt(st, 16);
        //        LogUtils.d(st + ",anInt:" + anInt);
        SPUtils.put(CkApplication.getInstance(), "hsh_step_data", StepService.getTodayDate());
        SPUtils.put(CkApplication.getInstance(), "hsh_step", anInt);
        initHSHStep(anInt);
    }

    private void initToTongBu() {
        if (AppConfig.nameStep != null && !AppConfig.nameStep.equals("timeUp")) {
            if (!AppConfig.flag) {
                AppConfig.flag = true;
                initStepChuShiHa();
                MoudleUtils.initTongBuData();
                SynchronizeDataTool.initGetHttpTongBuDataStep();
            }
        }
    }

    private int i = 0;
    private int[] ir = new int[111];
    private int in = 111;
    int h = 0;


    private synchronized void intiToISFd(int rssi) {
        if (i <= in) {
            if (i >= 0 && i < in) {
                initFdGuolv(rssi);
            } else if (i == in) {
                initToFdShow();
                i++;
            }
        } else if (i > in) {
            if (rssi != ir[in - 1]) {
                i = 0;
                h = 0;
            }
        }
    }

    private void initToFdShow() {
        int ave = h / (in);
        double dism = MoudleUtils.getDistance(ave);
        String dm = (String) SPUtils.get(CkApplication.getInstance(), "losedistance", "0");
        double d = Integer.parseInt(dm);
        double nd = 4;
        if (d == 4) {
            nd = 4.1;
        } else if (d == 5) {
            nd = 4.14;
        }
        //        Log.d(AppConfig.TAG, ir[0] + "," + ir[1] + "," + ir[2] + "," + ir[3] + "," + ir[4] + ",ave:" + ave + ",dism:" + dism);
        if (dism > 0 && dism >= nd) {
            if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                    BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff3", new byte[]{0x07});
                }
                MoudleUtils.initToFd();
            }
        }
    }

    private void initFdGuolv(int rssi) {
        if (rssi > 86 && rssi < 90) {
            ir[i] = rssi;
            //            Log.d(AppConfig.TAG, i + "ir[i]:" + ir[i]);
            h = h + ir[i];
            i++;
        } else {
            i = 0;
            h = 0;
        }
    }


    private void initToZq() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("设置自启动权限");
            builder.setMessage("建议你设置超空权限，\n以便更准确的记录你的运动健康状态。\n\n可以在：\n'我->设置->设置健康监测权限'重新设置");
            builder.setCancelable(false);

            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String mtype = Build.MANUFACTURER; // 手机型号
                    DataUtils.jumpStartInterfaceHome(mtype, getActivity());
                }
            });
            builder.setNegativeButton("去看看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("acurl", AppConfig.h5_ydqx);
                    intent1.setClass(getActivity(), HomeShopH5Activity.class);
                    startActivity(intent1);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            SPUtils.put(getActivity(), "zq", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 输入手环验证码
     */
    private synchronized void initStartHsH() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    HSHTool.start0xF1();
                }
            }
        }, 3000);
    }

    synchronized void initPostHsHData(Intent intent) {
        synchronized (this) {
            String uuid = intent.getStringExtra(AppConfig.EXTRA_DATA_UUID);
            byte[] bytes = intent.getByteArrayExtra(AppConfig.EXTRA_DATA);
            if (uuid.contains("fff5")) {
                initGetSleepData(uuid, bytes);
                if (nSleepFlag && !nHealthFlag) {
                    initPostHealth(uuid, bytes);
                }
            }
        }
    }

    void initGetSleepData(String uuid, byte[] bytes) {
        if (nSleepAll > 0) {
            if (nSleepI <= nSleepAll) {
                if (!nSleepFlag) {
                    initPostSleep(uuid, bytes);
                }
            }
        } else if (nSleepAll == 0) {
            nSleepFlag = true;
        }
    }

    /**
     * 读健康数据
     */
    public static synchronized void initReadHealth() {

        if (SynchronizeDataTool.daypoor == 0) {
            initNowDayHealthData();
        }
        if (SynchronizeDataTool.daypoor == 1) {
            initOneDayHealthData();
        }
        if (SynchronizeDataTool.daypoor == 2) {
            initTwoDayHealthData();
        }
        if (SynchronizeDataTool.daypoor >= 3) {
            if (SynchronizeDataTool.daypoor > 3) {
                SynchronizeDataTool.beforDataTime = 0;
            }
            initThreeDayHealthData();
        }


    }

    /**
     * 大于三天未使用数据
     */
    private synchronized static void initThreeDayHealthData() {
        nHealthAll = initTimeChange(SynchronizeDataTool.nowHour) + 1 + 8 + 8 + 8 - initTimeChange(SynchronizeDataTool.beforDataTime);
        initMoreOneTimeAlln();
        //        Log.i(AppConfig.TAG, "num2:" + nHealthAll);
        if (nHealthAll > 0) {
            if (nHealthn <= nHealthAll) {
                if (nHealthn < (initTimeChange(SynchronizeDataTool.nowHour) + 1)) {
                    if (nHealthn == 0) {
                        nHealthI = initTimeChange(SynchronizeDataTool.nowHour);
                    } else {
                        nHealthI--;
                    }
                    nHealthn++;
                    //                    LogUtils.d( "nHealth1iiii:" + nHealthI);
                    if (nHealthI >= 0) {
                        HSHTool.healthRead(SynchronizeDataTool.nowDay, intsHeath[nHealthI]);
                    } else {
                        initTbStop();
                    }

                } else if (nHealthn >= initTimeChange(SynchronizeDataTool.nowHour) + 1 && nHealthn < (initTimeChange(SynchronizeDataTool.nowHour) + 8 + 1)) {
                    if (nHealthn == initTimeChange(SynchronizeDataTool.nowHour) + 1) {
                        nHealthI = 7;
                    } else {
                        nHealthI--;
                    }
                    //                   LogUtils.d("nHealth1iiii:" + nHealthI + ",initGetNday(1):" + initGetNday(1));
                    nHealthn++;
                    if (nHealthI >= 0) {
                        HSHTool.healthRead(initGetNday(1), intsHeath[nHealthI]);
                    } else {
                        initTbStop();
                    }

                } else if (nHealthn >= (initTimeChange(SynchronizeDataTool.nowHour) + 8 + 1) && nHealthn < initTimeChange(SynchronizeDataTool.nowHour)
                        + 8 + 8 + 1) {
                    if (nHealthn == initTimeChange(SynchronizeDataTool.nowHour) + 8 + 1) {
                        nHealthI = 7;
                    } else {
                        nHealthI--;
                    }

                    nHealthn++;
                    //                    LogUtils.d( "nHealth1iiii:" + nHealthI + ",initGetNday(2):" + initGetNday(2));
                    if (nHealthI >= 0) {
                        HSHTool.healthRead(initGetNday(2), intsHeath[nHealthI]);
                    } else {
                        initTbStop();
                    }

                } else if (nHealthn >= initTimeChange(SynchronizeDataTool.nowHour) + 8 + 8 + 1 && nHealthn
                        <= nHealthAll) {
                    if (nHealthn == initTimeChange(SynchronizeDataTool.nowHour) + 8 + 8 + 1) {
                        nHealthI = 7;
                    } else {
                        nHealthI--;
                    }
                    nHealthn++;
                    //                   LogUtils.d( "nHealth1iiii:" + nHealthI + ",initGetNday(3):" + initGetNday(3));
                    if (nHealthI >= 0) {
                        HSHTool.healthRead(initGetNday(3), intsHeath[nHealthI]);
                    } else {
                        initTbStop();
                    }
                    //                   LogUtils.d( "nHealth1iiin:" + nHealthn);
                }
                //               LogUtils.d( "nHealthn:" + nHealthn + ",nHealthAll:" + nHealthAll+",nHealthI:"+nHealthI);
                //                LogUtils.d( "day:" + SynchronizeDataTool.nowDay + ",nHealthI:" + nHealthI);
            }
        } else {
            initTbStop();
        }
    }

    private static synchronized void initOneDayHealthData() {
        nHealthAll = initTimeChange(SynchronizeDataTool.nowHour) + 1 + 8 - initTimeChange(SynchronizeDataTool.beforDataTime);
        initMoreOneTimeAlln();
        if (nHealthAll > 0) {
            if (nHealthn <= nHealthAll) {
                if (nHealthn < initTimeChange(SynchronizeDataTool.nowHour) + 1) {
                    if (nHealthn == 0) {
                        nHealthI = initTimeChange(SynchronizeDataTool.nowHour);
                    } else {
                        nHealthI--;
                    }
                    nHealthn++;
                    if (nHealthI >= 0) {
                        HSHTool.healthRead(SynchronizeDataTool.nowDay, intsHeath[nHealthI]);
                    } else {
                        initTbStop();
                    }

                } else if (nHealthn >= initTimeChange(SynchronizeDataTool.nowHour) + 1 &&
                        nHealthn <= nHealthAll) {
                    if (nHealthn == initTimeChange(SynchronizeDataTool.nowHour) + 1) {
                        nHealthI = 7;
                    } else {
                        nHealthI--;
                    }
                    nHealthn++;
                    if (nHealthI >= 0) {
                        HSHTool.healthRead(initGetNday(1), intsHeath[nHealthI]);
                    } else {
                        initTbStop();
                    }
                }
                //                LogUtils.d("day:" + SynchronizeDataTool.nowDay + ",nHealthI:" + nHealthI);
            }
        } else {
            initTbStop();
        }
    }

    private synchronized static void initMoreOneTimeAlln() {
        boolean bf = false;
        for (int i = 0; i < intsHeath.length; i++) {
            if (SynchronizeDataTool.beforDataTime == intsHeath[i]) {
                bf = true;
                break;
            }
        }
        if (!bf) {
            nHealthAll = nHealthAll - 1;
        }
    }

    /**
     * 大于两天未使用数据
     */
    private synchronized static void initTwoDayHealthData() {
        nHealthAll = initTimeChange(SynchronizeDataTool.nowHour) + 1 + 8 + 8 - initTimeChange(SynchronizeDataTool.beforDataTime);
        initMoreOneTimeAlln();
        if (nHealthAll > 0) {
            if (nHealthn <= nHealthAll) {
                if (nHealthn < (initTimeChange(SynchronizeDataTool.nowHour) + 1)) {
                    if (nHealthn == 0) {
                        nHealthI = initTimeChange(SynchronizeDataTool.nowHour);
                    } else {
                        nHealthI--;
                    }
                    nHealthn++;
                    if (nHealthI >= 0) {
                        HSHTool.healthRead(SynchronizeDataTool.nowDay, intsHeath[nHealthI]);
                    } else {
                        initTbStop();
                    }
                } else if (nHealthn >= (initTimeChange(SynchronizeDataTool.nowHour) + 1) && nHealthn < (initTimeChange(SynchronizeDataTool.nowHour) + 8 + 1)) {

                    if (nHealthn == (initTimeChange(SynchronizeDataTool.nowHour) + 1)) {

                        nHealthI = 7;
                    } else {
                        nHealthI--;
                    }
                    nHealthn++;
                    if (nHealthI >= 0) {
                        HSHTool.healthRead(initGetNday(1), intsHeath[nHealthI]);
                    } else {
                        initTbStop();
                    }

                } else if (nHealthn >= (initTimeChange(SynchronizeDataTool.nowHour) + 8 + 1) &&
                        nHealthn <= nHealthAll) {
                    if (nHealthn == (initTimeChange(SynchronizeDataTool.nowHour) + 8 + 1)) {
                        nHealthI = 7;
                    } else {
                        nHealthI--;
                    }
                    nHealthn++;
                    if (nHealthI >= 0) {
                        HSHTool.healthRead(initGetNday(2), intsHeath[nHealthI]);
                    } else {
                        initTbStop();
                    }

                }
            }
        } else {
            initTbStop();
        }
    }

    private synchronized static void initNowDayHealthData() {
        nHealthAll = initTimeChange(SynchronizeDataTool.nowHour) - initTimeChange(SynchronizeDataTool.beforDataTime);
        initNowDawNAll();
        if (nHealthAll > 0) {
            if (nHealthn <= nHealthAll) {
                if (nHealthn <= initTimeChange(SynchronizeDataTool.nowHour) - initTimeChange(SynchronizeDataTool.beforDataTime)) {
                    if (nHealthn == 0) {
                        nHealthI = initTimeChange(SynchronizeDataTool.nowHour);
                    } else {
                        nHealthI--;
                    }
                    nHealthn++;
                    if (nHealthI >= 0) {
                        HSHTool.healthRead(SynchronizeDataTool.nowDay, intsHeath[nHealthI]);
                    } else {
                        initTbStop();
                    }
                }
            }
            //            LogUtils.d("day:" + SynchronizeDataTool.nowDay + ",nHealthI:" + nHealthI);
        } else {
            initTbStop();
        }
    }

    private synchronized static void initNowDawNAll() {
        for (int i = 0; i < intsHeath.length; i++) {
            if (SynchronizeDataTool.beforDataTime == intsHeath[i]) {
                nHealthAll = nHealthAll + 1;
                break;
            }
        }
    }

    /**
     * 上传睡眠数据
     */
    public synchronized static void initPostSleepTask() {
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        Call<AcceptBean> callBack = restApi.upsleep(token, user_id, SynchronizeDataTool.initToJson(listSleep));

        callBack.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                AcceptBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        LogUtils.d("initPostSleepTask:睡眠上传成功");
                        broadcastUpdate(CkApplication.getInstance(), AppConfig.ACTION_XIN_LV);
                        if (listSleep != null) {
                            listSleep.clear();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {

            }
        });
    }

    public static int initTimeChange(int time) {
        int i;
        if (time >= 0 && time < 6) {
            i = 0;
            return i;
        }
        if (time >= 6 && time < 8) {
            i = 1;
            return i;
        }
        if (time >= 8 && time < 12) {
            i = 2;
            return i;
        }
        if (time >= 12 && time < 14) {
            i = 3;
            return i;
        }
        if (time >= 14 && time < 18) {
            i = 4;
            return i;
        }
        if (time >= 18 && time < 20) {
            i = 5;
            return i;
        }
        if (time >= 20 && time < 22) {
            i = 6;
            return i;
        }
        if (time >= 22 && time <= 23) {
            i = 7;
            return i;
        }


        return 0;
    }

    /**
     *
     */
    private static void initPostHealthTask() {
        //        LogUtils.d(SynchronizeDataTool.nowDay + "" + "data:" + SynchronizeDataTool.nowDate);
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        String oneHour = "";
        if (listHealth != null && listHealth.size() > 0) {
            oneHour = listHealth.get(0).getHours();
            if (oneHour == null) {
                oneHour = "";
            }
        } else {
            return;
        }
        if (oneHour == null || oneHour.equals(""))
            return;
        Call<AcceptBean> callBack = restApi.upbodyinfo(token, user_id, "1", SynchronizeDataTool.initToJsonHealth(listHealth), SynchronizeDataTool.nowDate, oneHour);

        callBack.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                AcceptBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        broadcastUpdate(CkApplication.getInstance(), AppConfig.ACTION_XIN_LV);
                        if (listHealth != null) {
                            listHealth.clear();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {
                ToastUtils.showShortNotInternet("网络异常,请检查");
            }
        });
    }

    static int initGetNday(int n) {
        //Java获取当前日期的前一个月,前一天时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -n);//得到前n天
        int nday = calendar.get(Calendar.DATE);
        return nday;
    }

    public static List<SleepBean> listSleep = new ArrayList<>();
    public static List<PostHealthBean> listHealth = new ArrayList<>();
    private List<StepPostBean> listStep = new ArrayList<>();

    private synchronized void initPostHealth(String uuid, byte[] data) {
        if (uuid.contains("fff5")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 0x01) {
                        if (data.length == 10) {
                            SynchronizeDataTool.initSleepHealth(data, intsHeath[nHealthI]);
                            //                            LogUtils.d("listHealth.size():" + listHealth.size());
                            if (nHealthn < nHealthAll) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        initReadHealth();
                                    }
                                }, 380);
                            }
                            if (listHealth.size() == nHealthAll) {
                                initTbStop();

                            }
                        }
                    }
                }
            }
        }
    }

    private synchronized static void initTbStop() {
        MoudleUtils.kyloadingDismiss(DeviceControlActivity.kyLoadingBuilderDEvice);
        MoudleUtils.kyloadingDismiss(MoudleUtils.kyLoadingBuilder);
        ToastUtils.showShortNotInternet("数据同步结束");
        nHealthFlag = true;
        AppConfig.flag = false;
        //        LogUtils.d("nHealthn:" + nHealthn + ",nHealthAll:" + nHealthAll);
        initPostHealthTask();
    }


    private synchronized void initReadSleep(String uuid, byte[] data) {
        if (uuid.contains("fff1")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 0x06) {
                        if (data.length == 4) {
                            //                            LogUtils.d("nSleepAll_data[1]:" + Integer.toHexString(data[1] & 0xFF));
                            if (data[1] == (byte) 0xEE) {
                                if (!flagStartCheckSleep) {
                                    flagStartCheckSleep = true;
                                    if (nSleepAll == -1) {
                                        nSleepAll = DataUtils.s16ToInt(data[2]);
                                        //                                        LogUtils.d("nSleepAll:num=" + nSleepAll + ",data[2]:" + data[2]);
                                        //                                        ToastUtils.showShortNotInternet("nSleepAll:" + nSleepAll);
                                        if (nSleepAll > 0) {
                                            if (nSleepAll > 1) {
                                                nSleepI = nSleepAll - 1;
                                            }
                                            String s = MoudleUtils.checkData(nSleepI);
                                            byte b = (byte) (Integer.parseInt(s, 16));
                                            //                                            LogUtils.d("b:b=" + b + "");
                                            HSHTool.sleepRead(b);
                                        } else if (nSleepAll == 0) {
                                            initStopSleep();
                                        }
                                    }
                                }
                            } else {
                                if (!flagStartCheckSleep) {
                                    flagStartCheckSleep = true;
                                }
                                initStopSleep();
                            }
                        }
                    }
                }
            }
        }
    }

    private void initStopSleep() {
        nSleepFlag = true;
        initReadHealth();
    }

    /**
     * 显示首页电量
     *
     * @param uuid
     * @param data
     */
    public static int nSleep;
    public static int nSleepAll = -1;
    public static int nSleepI = 1;
    public static int nHealthI;
    public static int nHealthn = 0;
    public static boolean nSleepFlag;
    public static boolean nHealthFlag;
    public static int nHealthAll = -1;
    private static int[] intsHeath = {0, 6, 8, 12, 14, 18, 20, 22};
    public static boolean flagStartCheckSleep = false;

    private synchronized void initPostSleep(String uuid, byte[] data) {
        if (uuid.contains("fff5")) {
            if (data != null) {
                if (data.length > 0) {
                    //                    Log.i(AppConfig.TAG, "data[0]:fff5" + data[0]);
                    if (data[0] == 0x02) {
                        if (data.length == 10) {
                            if (nSleep == 0) {
                                if (data[1] == 0x01) {
                                    AppConfig.aByte1 = data;
                                }
                                nSleep++;
                            } else {
                                if (data[1] == 0x02) {
                                    AppConfig.aByte2 = data;
                                }
                                nSleep = 0;
                                SynchronizeDataTool.initSleepData(AppConfig.aByte1, AppConfig.aByte2);
                            }
                        }
                    } else {
                        initStopSleep();
                    }
                }
            }
        }
    }

    private synchronized void initCheckSleep(String uuid, byte[] data) {
        if (uuid.contains("fff1")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data[0] == 0x06) {
                        if (data.length == 4) {
                            if (data[1] == 0x01) {
                                //                                Log.i(AppConfig.TAG, "f1Read");
                                initReadF1();
                            }
                        }
                    }
                }
            }
        }
    }

    private void initReadF1() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HSHTool.f1Read();
            }
        }, 1000);
    }

    synchronized void initStartFF4IS(String uuid, byte[] data) {
        if (uuid.contains("fff1")) {
            if (data != null) {
                if (data.length == 4) {
                    if (data[0] == (byte) 0x04) {
                        if (data[1] == (byte) 0x04) {
                            //                            Log.i(AppConfig.TAG, data[1] + ":data[1]readTimeStep:");
                        }
                    }
                }
            }
        } else if (uuid.contains("fff7")) {
            initTvShowState(getResources().getText(R.string.buletooth_dl));
            String clockhour = (String) SPUtils.get(getActivity(), "clockhour", "");
            //            Log.d(AppConfig.TAG, "---clockhour:" + clockhour);
            if (clockhour != null && !clockhour.equals("")) {
                SPUtils.put(getActivity(), "longtime", false);//久坐关闭
                SPUtils.put(getActivity(), "clock", false);//闹钟开关
                initFFF1Rc();
                initFFF5Jk();
                initFFF2Dl();
            } else {
                MoudleUtils.initChuShiHua(getActivity());
            }
        }
    }

    private void initFFF1Rc() {
        SPUtils.put(getActivity(), "isSportMode", false);
        SPUtils.put(getActivity(), "isSportModeRun", false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //                Log.d(AppConfig.TAG, "--------日常模式:");
                //日常模式
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x0B, 0x02, 0x00, 0x00});
            }
        }, 500);
    }

    private void initFFF5Jk() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //开启健康数据上传
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff5", AppConfig.FFF5);

            }
        }, 1000);
    }

    private void initFFF2Dl() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取电量
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff2");
            }
        }, 2500);
    }


    private void initJymAcsses() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MoudleUtils.initTime();
            }
        }, 1000);

    }


    private synchronized void displayStart(String uuid, byte[] data) {
        if (uuid.contains("fff1")) {
            if (data != null) {
                if (data.length == 4) {
                    if (data[0] == (byte) 0xF1) {
                        initJymAcsses();
                    }
                }
            }
        }
    }

    private boolean flagStart;

    /**
     * 显示首页电量
     *
     * @param uuid
     * @param data
     */
    private synchronized void initDianLiang(String uuid, byte[] data) {
        if (uuid.contains("fff2")) {
            if (data != null) {
                if (data.length > 0) {
                    if (data.length == 2) {
                        int[] ints = DataUtils.byteTo10(data);
                        if (ints.length > 1) {
                            AppConfig.dl = ints[1];
                            MoudleUtils.textViewSetText(tv_again, "剩余电量  " + ints[1] + "%");
                            //                            Log.i(AppConfig.TAG, "AppConfig.flagStart:" + flagStart);
                            if (!flagStart) {
                                flagStart = true;
                                initStartTbStep("start");
                                startTimeCount();
                            }
                        }
                    }
                }
            }
        }
    }


    private synchronized static void initStartTbStep(final String name) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String nameStep = "0";
                nameStep = (String) SPUtils.get(CkApplication.getInstance(), "nameStepType", nameStep);
                if (nameStep.equals("1")) {
                    initHshStep();
                    //TODO 步数
                    AppConfig.nameStep = name;
                    //用来进行数据同步开启广播
                    BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff4", AppConfig.FFF4);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x04, 0x04, 0x00, 0x00});
                        }
                    }, 1000);
                }
            }
        }, 1000);
    }


    @OnClick(R.id.ll_read)
    void llRead() {
        initTj();
    }

    @OnClick(R.id.ll_activity)
    void llActivity() {
        initToAc();
    }

    @OnClick(R.id.ll_sj)
    void llSj() {
        initToSj();
    }

    private void initToSj() {
        startActivity(new Intent().setClass(getActivity(), CkSjActivity.class));
    }


    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, Constants.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private boolean flagTrue;
    private boolean flagTrueHSh;

    /**
     * 上传步数消耗卡路里
     *
     * @param step
     */
    private synchronized void initData(final int step) {
        int height = (int) SPUtils.get("height", 0);
        one = (float) (height * 0.414);//一步的距离
        String token = (String) SPUtils.get(getActivity(), "token", "");
        int user_id = (int) (SPUtils.get(getActivity(), "user_id", 0));
        float mileage = one * step / 100.0f;
        mileage = (float) Math.round(mileage * 10) / 10;
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<RecordBean> callBack = restApi.record(user_id, token, step, expand + "", mileage + "");


        callBack.enqueue(new Callback<RecordBean>() {
            @Override
            public void onResponse(Call<RecordBean> call, Response<RecordBean> response) {
                flagTrue = false;
                flagTrueHSh = false;
            }

            @Override
            public void onFailure(Call<RecordBean> call, Throwable t) {
                flagTrue = false;
                flagTrueHSh = false;
            }
        });
    }

    /**
     * 更新消耗
     */
    private synchronized void setExpand() {
        float weight = 0;
        weight = (float) SPUtils.get("weight", weight);
        expand = (float) (weight * 1.036 * diatance);
        expand = (float) Math.round(expand * 100) / 100;
        if (HealthMovementFragment.tv_health_expand != null) {
            int e = (int) expand;
            if (expand - e >= 0.5) {
                e = e + 1;
            }
            HealthMovementFragment.tv_health_expand.setText(e + "千卡");
        }
        if (tv_kcal != null) {
            int eall = (int) (expand + otherFat);
            if (expand + otherFat - eall >= 0.5) {
                eall = eall + 1;
            }
            tv_kcal.setText(eall + "");
        }

    }

    /**
     * 更新距离
     *
     * @param step
     */
    private synchronized void setDistance(int step) {
        int height = (int) SPUtils.get("height", 0);
        one = (float) (height * 0.414);//一步的距离
        diatance = 0;
        diatance = one * step / 100000.0f;//走的距离
        float diatanceLs = (float) Math.round(diatance * 100) / 100;
        if (HealthMovementFragment.tv_health_distance != null) {
            HealthMovementFragment.tv_health_distance.setText(diatanceLs + "公里");
        }
    }


    @Override
    public boolean handleMessage(final Message msg) {
        switch (msg.what) {
            case Constants.MSG_FROM_SERVER:
                // 更新界面上的步数
                initPhoneStep(msg);
                break;
            case Constants.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, Constants.MSG_FROM_CLIENT);
                    msg1.replyTo = mGetReplyMessenger;
                    messenger.send(msg1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
        return false;
    }

    private synchronized void initPhoneStep(Message msg) {
        synchronized (this) {
            try {
                if (!StepDcretor.flagStep) {
                    String nameStep = "0";
                    nameStep = (String) SPUtils.get(CkApplication.getInstance(), "nameStepType", nameStep);
                    if (nameStep.equals("0")) {
                        n_step = msg.getData().getInt("step");
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                initSteps(n_step);//更新步数和卡路里以及距离
                                toUpdata(n_step);//50步一上传新步数到服务器
                            }
                        });
                        final String sportMode = msg.getData().getString("SportMode");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initToModle(sportMode);//更新运动模式
                            }
                        }, 500);
                    }
                }
                delayHandler.sendEmptyMessageDelayed(Constants.REQUEST_SERVER, TIME_INTERVAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void initToModle(String sportMode) {
        if (HealthMovementFragment.tv_health_sportmode != null && getActivity() != null && sportMode != null && !sportMode.equals("")) {
            if (sportMode.equals("步行")) {
                HealthMovementFragment.tv_health_sportmode.setText(sportMode);
                HealthMovementFragment.tv_health_sportmode.setTextColor(getActivity().getResources().getColor(R.color.fea21249));
            } else {
                HealthMovementFragment.tv_health_sportmode.setText(sportMode);
                HealthMovementFragment.tv_health_sportmode.setTextColor(getActivity().getResources().getColor(R.color.c_ea5050));
            }
        }
    }

    private synchronized void toUpdata(int step) {
        if ((step - bushu) >= 50) {
            bushu = step;
            if (!flagTrue) {
                flagTrue = true;
                Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。

                t.setToNow(); // 取得系统时间。

                int hour = t.hour; // 0-23
                int minute = t.minute;

                if (hour == 0 && minute == 0) {
                    return;
                }
                initData(step);
            }
        } else if ((step - bushu) < 0) {
            bushu = step;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MoudleUtils.initRefrushTrue(swipeRefreshLayoutHome);

    }


    private void initTaskDak() {
        int userid = (int) SPUtils.get(getActivity(), "user_id", 0);
        String token = (String) SPUtils.get(getActivity(), "token", "");

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            Call<GetSignBean> getSignBeanCall = RetrofitUtils.retrofit.create(WfApi.class).getsign(token, userid);
            getSignBeanCall.enqueue(new Callback<GetSignBean>() {
                @Override
                public void onResponse(Call<GetSignBean> call, Response<GetSignBean> response) {
                    GetSignBean getSignBean = response.body();
                    try {
                        initNextData(getSignBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<GetSignBean> call, Throwable t) {

                }
            });
        }
    }

    private void initNextData(GetSignBean getSignBean) {
        if (getSignBean != null) {
            if (getSignBean.getStatus().equals("1")) {
                listSign = getSignBean.getInfo();
            } else if (getSignBean.getStatus().equals("0")) {
                flagFist = true;
                listSign = null;
            } else if (getSignBean.getStatus().equals("2")) {
                MoudleUtils.initStatusTwo(getActivity(), true);
            }
        }
    }


    private void initDakTask() {
        String token = (String) SPUtils.get(getActivity(), "token", "");
        if (!token.equals("")) {
            initTaskDak();
        }
    }

    private void init() {

        NotifyActivityToMessageManager.getInstance().setNotifyMessage(this);//通知打卡页面我要打卡了(提问者)
        NotifyToHomeSignBeanMessageManager.getInstanceSignBean().setNotifyMessageSignBean(this);//通知打卡页面我要打卡了(提问者)
    }


    private void initClearHJF() {
        llBgJf.removeAllViews();
        tv_hJF.removeAllViews();
    }


    private void initToolbar() {
        LinearLayout toolbar = (LinearLayout) viewFoot.findViewById(R.id.my_toolbar);
        TextView textView = (TextView) viewFoot.findViewById(R.id.toolbar_title);
        textView.setText("健康监测");
        TextView textViewShop = (TextView) viewFoot.findViewById(R.id.toolbar_pretitle);
        RelativeLayout l_toolbar_button = (RelativeLayout) viewFoot.findViewById(R.id.l_toolbar_button);
        textViewShop.setText("家庭");
        l_toolbar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(getActivity(), HomeHSFNewActivity.class));
            }
        });
        textViewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(getActivity(), HomeHSFNewActivity.class));
            }
        });
        toolbar.setBackgroundResource(R.drawable.home_bg_top);

        button = (TextView) viewFoot.findViewById(R.id.toolbar_button);
        button.setOnClickListener(this);
        button.setText("签到");
        RelativeLayout r_toolbar_button = (RelativeLayout) viewFoot.findViewById(R.id.r_toolbar_button);
        r_toolbar_button.setOnClickListener(this);

    }


    private void initId() {
        delayHandler = new Handler(this);

        bind_bracelet = (TextView) viewFoot.findViewById(R.id.bind_bracelet);
        tv_bu_shu = (TextView) viewFoot.findViewById(R.id.tv_bu_shu);
        tv_kcal = (TextView) viewFoot.findViewById(R.id.tv_kcal);
        tv_home_heart = (TextView) viewFoot.findViewById(R.id.tv_home_heart);
        tv_sleep = (TextView) viewFoot.findViewById(R.id.tv_sleep);
        tv_home_bloodoxygen = (TextView) viewFoot.findViewById(R.id.tv_home_bloodoxygen);

        llBgJf = (LinearLayout) viewFoot.findViewById(R.id.llbg);
        tv_hJF = (LinearLayout) viewFoot.findViewById(R.id.tv_h);
        swipeRefreshLayoutHome = (SwipeRefreshLayout) viewFoot.findViewById(R.id.swipeRefreshLayoutHome);
        swipeRefreshLayoutHome.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayoutHome.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutHome.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutHome.setOnRefreshListener(this);

        ll_bind_bracelet = (LinearLayout) viewFoot.findViewById(R.id.ll_bind_bracelet);
        ll_bind_bracelet.setOnClickListener(this);
        ll_bind_bracelet_new = (LinearLayout) viewFoot.findViewById(R.id.ll_bind_bracelet_new);
        ll_bind_bracelet_new.setOnClickListener(this);

        ll_fu_jin = (LinearLayout) viewFoot.findViewById(R.id.ll_fu_jin);

        textViewDingWei = (TextView) viewFoot.findViewById(R.id.textViewDingWei);
        textViewDingWei.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_bind_bracelet:
            case R.id.ll_bind_bracelet_new:
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        bind();
                    }
                });
                break;
            case R.id.toolbar_button:
            case R.id.r_toolbar_button:
                showDialog();
                break;
            case R.id.textViewDingWei:
                initDingWeiAc();
                break;
        }
    }

    /**
     * type0：默认；1：共享手环；
     */
    private void initDingWeiAc() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            Intent intent = new Intent(getActivity(), PoiSearchListJsfActivity.class).putExtra("type", 0);
            startActivity(intent);
        } else {
            MoudleUtils.toChekWifi(getActivity());
        }
    }

    private void initToAc() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), HomeAcActivity.class);
        startActivity(intent);
    }

    private void initTj() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), HomeTjActivity.class);
        startActivity(intent);
    }

    private void showDialog() {
        String token = (String) SPUtils.get(getActivity(), "token", "");
        if (token.equals("")) {
            MoudleUtils.initToLogin(getActivity());
        } else {
            if (listSign != null) {
                initToDaAc();
            } else {
                if (flagFist) {
                    initToDaAc();
                } else {
                    ToastUtils.showShortNotInternet("客官请稍等哦");
                    initDakTask();

                }
            }
        }
    }

    private void initToDaAc() {
        Intent intent = new Intent().setClass(getActivity(), PunchCardDialog.class);
        startActivity(intent);
    }

    private void bind() {
        try {
            if (BluethUtils.bluetoothAdapter == null) {
                ToastUtils.showShortNotInternet("对不起，您的机器不具备蓝牙功能");
                return;
            }
            if (BluethUtils.bluetoothAdapter.isEnabled()) {
                String token = (String) SPUtils.get(getActivity(), "token", "");
                if (token.equals("")) {
                    MoudleUtils.initToLogin(getActivity());
                } else {
                    MoudleUtils.initToBd(getActivity(), "home");
                }
            } else {
                AppConfig.NEWSTATE = 0;
                BluethUtils.initGetBluetooth(getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        initPrompt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showHomeAcssess(boolean flag) {
        if (flag) {
            MoudleUtils.viewShow(ll_bind_bracelet_new);
            MoudleUtils.viewGone(ll_bind_bracelet);
        } else {
            MoudleUtils.viewShow(ll_bind_bracelet);
            MoudleUtils.viewGone(ll_bind_bracelet_new);
        }
    }


    private synchronized void initPrompt() {
        if (BluethUtils.bluetoothAdapter != null) {
            if (BluethUtils.bluetoothAdapter.isEnabled()) {
                if ((SPUtils.get(getActivity(), "mDeviceAddress", "") + "") != null
                        && !(SPUtils.get(getActivity(), "mDeviceAddress", "") + "").equals("")) {
                    showHomeAcssess(true);
                    if (AppConfig.mBluetoothGatt == null || AppConfig.bluetoothGattServices == null) {
                        initTvShowState(getResources().getText(R.string.buletooth_again));
                        if (AppConfig.NEWSTATE == -1) {
                            AppConfig.NEWSTATE = 0;
                            MoudleUtils.connect(getActivity(), SPUtils.get(getActivity(), "mDeviceAddress", "") + "");
                        } else {
                            AppConfig.NEWSTATE = 0;
                        }
                    } else {
                        if (tv_again != null) {
                            if (tv_again.getText().toString().trim().equals("请重新连接")) {
                                if (AppConfig.NEWSTATE == 2) {
                                    if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                                        initTvShowState(getResources().getText(R.string.buletooth_dl));
                                        broadcastUpdate(getActivity(), AppConfig.ACTION_GATT_SERVICES_DISCOVERED);
                                        ToastUtils.showShortNotInternet("手环开始进行通讯");
                                    }
                                }
                            }
                        }
                    }
                } else {
                    AppConfig.NEWSTATE = 0;
                    showHomeAcssess(false);
                    initTvShowState(getResources().getText(R.string.buletooth_again));
                    initTvBindBraceletShowState(getResources().getText(R.string.buletooth_activity));
                }
            } else {
                AppConfig.NEWSTATE = 0;
                initTvShowState(getResources().getText(R.string.buletooth_again));
                initTvBindBraceletShowState(getResources().getText(R.string.buletooth_open));
                initBindAgin();
            }
        }
    }

    private void initBindAgin() {
        if ((SPUtils.get(getActivity(), "mDeviceAddress", "") + "") != null
                && !(SPUtils.get(getActivity(), "mDeviceAddress", "") + "").equals("")) {
            showHomeAcssess(true);
        } else {
            showHomeAcssess(false);
        }
    }

    private void initTvShowState(CharSequence text) {
        tv_again.setText(text);
    }

    private void initTvBindBraceletShowState(CharSequence text) {
        bind_bracelet.setText(text);
    }


    @Override
    public void onRefresh() {
        initJianShenFangTask();
        initStartDw();
        initDakTask();
        initHealthTask();
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
                        MoudleUtils.textViewSetText(tv_home_heart, bean.getInfo().getRate());
                        MoudleUtils.textViewSetText(tv_home_bloodoxygen, bean.getInfo().getOxygen());
                        MoudleUtils.textViewSetText(tv_sleep, bean.getInfo().getSleep());
                        otherFat = bean.getInfo().getOther();
                    }
                } else if (bean.getStatus().equals("2")) {
                    MoudleUtils.initStatusTwo(getActivity(), true);
                }

            }

            @Override
            public void onFailure(Call<HealthBean> call, Throwable t) {

            }
        });

    }

    private AMapLocationClient mlocationClient;

    private synchronized void initStartDw() {
        if (mlocationClient == null) {
            ToastUtils.showShortNotInternet("请开启定位权限,然后点击刷新定位");
            return;
        }
        mlocationClient.startLocation();
    }

    private void initDw() {
        mlocationClient = new AMapLocationClient(getActivity());
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位参数
        mlocationClient.setLocationOption(Utils.initLocation());
    }


    private void initStopDw() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
        mlocationClient = null;
    }

    private void initJianShenFangTask() {
        int user_id = (int) (SPUtils.get(getActivity(), "user_id", 0));
        String token = (String) SPUtils.get(getActivity(), "token", "");
        Call<GetgymBean> tuiJianBeanCall = RetrofitUtils.retrofit.create(NpApi.class).getgym(token, user_id, lat, lon, city);
        tuiJianBeanCall.enqueue(new Callback<GetgymBean>() {
            @Override
            public void onResponse(Call<GetgymBean> call, Response<GetgymBean> response) {
                GetgymBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {
                            initClearHJF();
                            initImageJianShenFangH(bean.getInfo());
                        }
                    } else {
                        ToastUtils.showShort(getActivity(), bean.getMsg());
                    }
                }
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);
            }

            @Override
            public void onFailure(Call<GetgymBean> call, Throwable t) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);
                ToastUtils.showShort(getActivity(), getResources().getString(R.string.not_wlan_show));

            }
        });

    }

    private void initImageJianShenFangH(List<GetgymBean.GetgymBeanInfo> list) {

        for (int i = 0; i < list.size(); i++) {
            SimpleDraweeView ivPoint = new SimpleDraweeView(
                    CkApplication.getInstance());
            LinearLayout.LayoutParams layoutParams = null;
            int margenH = ScreenUtils.getScreenWidth(CkApplication.getInstance()) / 72 / 2;
            int margenHRight = ScreenUtils.getScreenWidth(CkApplication.getInstance()) / 72 / 2;
            layoutParams = new LinearLayout.LayoutParams((int) ((ScreenUtils.getScreenWidth(CkApplication.getInstance()) - margenH * 6) / 3), (int) (((ScreenUtils.getScreenWidth(CkApplication.getInstance()) - margenH * 6) / 3) / 1.1));

            layoutParams.setMargins(margenH, 0, margenH, margenHRight);
            ivPoint.setScaleType(ImageView.ScaleType.FIT_CENTER);//设置缩放类型
            ivPoint.setLayoutParams(layoutParams);
            FrescoUtils.setImage(ivPoint, AppConfig.url_jszd + list.get(i).getPicture());
            llBgJf.addView(ivPoint);
            TextView tv = new TextView(
                    getActivity());
            LinearLayout.LayoutParams layoutParamsTv = null;
            layoutParamsTv = new LinearLayout.LayoutParams((int) ((ScreenUtils.getScreenWidth(CkApplication.getInstance()) - margenH * 6) / 3), ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTv.setMargins(margenHRight, 0, margenHRight, margenHRight);
            tv.setLayoutParams(layoutParamsTv);
            tv.setText(list.get(i).getName());
            tv.setGravity(Gravity.BOTTOM);
            tv.setPadding(0, 0, 0, margenHRight);
            /**
             *        先得到手机的scaledDecsity
             *        设置字体大小时
             */
            DisplayMetrics dm = getResources().getDisplayMetrics();
            float value = dm.scaledDensity;
            tv.setTextSize(CkApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.d12) / value);
            tv.setSingleLine(true);
            tv_hJF.addView(tv);
            initHClickJsMore(i, ivPoint, list);
        }
    }

    private void initHClickJsMore(final int position, SimpleDraweeView ivPoint, final List<GetgymBean.GetgymBeanInfo> list) {
        ivPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    Intent intent = new Intent();
                    intent.putExtra("gym_id", list.get(position).getGymId());
                    intent.putExtra("name", list.get(position).getName());
                    intent.setClass(getActivity(), HomeJsfMoreActivity.class);
                    startActivity(intent);
                } else {
                    MoudleUtils.toChekWifi(getActivity());

                }
            }
        });
    }


    @Override
    public void sendMessageActivityToFlag(boolean flag) {
        if (flag) {
            NotifySignBeanMessageManager.getInstanceSignBean().sendNotifyGetSignInfoBean(listSign);
        }

    }


    @Override
    public void sendMessageGetSignBean(GetSignBean.GetSignInfoBean getSignInfoBean) {
        if (getSignInfoBean != null) {
            this.listSign = getSignInfoBean;
            try {
                initTaskDak();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        initStopDw();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        initUbindCoon();
        if (mGattUpdateReceiver != null) {
            MoudleUtils.unregisterReceivermGattUpdateReceiver(getActivity(), mGattUpdateReceiver);
        }
    }


    private void setupService() {
        if (conn != null) {
            Intent intent = new Intent(getActivity(), StepService.class);
            getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
            getActivity().startService(intent);
        }
    }


    private void initUbindCoon() {
        if (conn != null && getActivity() != null) {
            getActivity().unbindService(conn);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                lon = amapLocation.getLongitude();
                lat = amapLocation.getLatitude();
                city = amapLocation.getCity();
                initJianShenFangTask();
            } else {
                //                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                //                Log.e("AmapErr", errText);
                ToastUtils.showShortNotInternet("定位失败!你是否开启了定位权限");
                initJianShenFangTask();
            }
        }
    }

}
