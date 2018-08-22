package com.ckjs.ck.Service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.text.format.Time;

import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.LogUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RememberStep.Constants;
import com.ckjs.ck.Tool.RememberStep.CountDownTimer;
import com.ckjs.ck.Tool.RememberStep.DbUtils;
import com.ckjs.ck.Tool.RememberStep.StepData;
import com.ckjs.ck.Tool.RememberStep.StepDcretor;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 * 手机步数Service
 */

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class StepService extends Service implements SensorEventListener {
    //默认为30秒进行一次存储
    private static int duration = 1000;
    public static String CURRENTDATE = "";
    private SensorManager sensorManager;
    public StepDcretor stepDetector;

    private Messenger messenger = new Messenger(new MessenerHandler());
    private BroadcastReceiver mBatInfoReceiver;
    private WakeLock mWakeLock;
    private TimeCount time;
    //测试
    private String DB_NAME = "hshdo";
    public static Sensor detectorSensor;
    public static Sensor countSensor;
    public static Sensor sensor;


    private static class MessenerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_CLIENT:
                    try {
                        Messenger messenger = msg.replyTo;
                        Message replyMsg = Message.obtain(null, Constants.MSG_FROM_SERVER);
                        Bundle bundle = new Bundle();
                        bundle.putInt("step", StepDcretor.CURRENT_SETP);
                        bundle.putString("SportMode", StepDcretor.SportMode);
                        replyMsg.setData(bundle);
                        messenger.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBroadcastReceiver();
        new Thread(new Runnable() {
            public void run() {
                StepDcretor.step = 0;
                initToOnStartCommand();
            }
        }).start();
        startTimeCount();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtils.d("onStart");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("onStartCommand");
        new Thread(new Runnable() {
            public void run() {
                StepDcretor.step = 0;
                initToOnStartCommand();
            }
        }).start();
        return START_STICKY;
    }

    private synchronized void initToOnStartCommand() {
        String beforData = (String) SPUtils.get(CkApplication.getInstance(), "beforDataTiime", getTodayDate());
//        LogUtils.d("beforDataTiime:" + beforData);
        if (!getTodayDate().equals(beforData)) {
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "beforDataTiime", getTodayDate());
            LogUtils.d("beforDataTiimeSave");
            clearData();
            initAgagin("onStartCommand", true);
            startStepDetector();
        } else {
            initAgagin("onStartCommand", false);
            startStepDetector();
        }
    }

    private synchronized void initAgagin(String name, boolean flag) {

        initTodayData(name, flag);
    }


    private synchronized static void initHouTaiJiBu(int nowStep, int stepCha) {
//        LogUtils.d("stepCha:" + stepCha);
        /*
         *重启手机后记录的之前的步数以及本地手机后台计步比前台大则进行重新记录后台计步的步数
         */
        int steps = 0;
        steps = (int) SPUtils.get("nowMyStep", steps);
//        LogUtils.d("stepsCq:" + steps);

        if (steps < 0) {
            steps = 0;
        }

//        LogUtils.d("stepsEnd:" + steps);
        stepCha = stepCha + steps;

//        LogUtils.d("stepChaEnd:" + stepCha);
//        LogUtils.d("stepChaEnd_StepDcretor.CURRENT_SETP:" + (StepDcretor.CURRENT_SETP));
//        LogUtils.d("step:" + nowStep + ",stepCha:" + stepCha + "," + StepDcretor.CURRENT_SETP);

        if (stepCha >= StepDcretor.CURRENT_SETP) {
            StepDcretor.CURRENT_SETP = stepCha;
        } else {
            /**
             * 本地手机后台计步比前台大则进行重新记录后台计步的步数
             */
            SavaDataLocalUtils.saveDataInt(CkApplication.getInstance(), "beforeStep", nowStep);
//            LogUtils.d("steps0:" + StepDcretor.CURRENT_SETP);
            SavaDataLocalUtils.saveDataInt(CkApplication.getInstance(), "nowMyStep", StepDcretor.CURRENT_SETP);
        }
    }


    public static synchronized String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private synchronized void initTodayData(String name, boolean flag) {

        try {
            CURRENTDATE = getTodayDate();
            DbUtils.createDb(this, DB_NAME);
            //获取当天的数据，用于展示
            List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENTDATE});
            if (list.size() == 0 || list.isEmpty()) {
//                LogUtils.d("0:listIdclear！" + name + AppConfig.loginStep);
                initRemoveStep(name, flag);
                initGetLoginSteps();
            } else if (list.size() == 1) {
                StepDcretor.CURRENT_SETP = Integer.parseInt(list.get(0).getStep());
//                LogUtils.d("1:StepDcretor.CURRENT_SETP:" + StepDcretor.CURRENT_SETP);
                initGetLoginSteps();
            } else {
                LogUtils.v("list出错了！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 得到登录后的步数
     */
    private synchronized void initGetLoginSteps() {
        String nameStep = "0";
        nameStep = (String) SPUtils.get(CkApplication.getInstance(), "nameStepType", nameStep);
        if (nameStep.equals("0")) {
            if (StepDcretor.CURRENT_SETP == 0) {
                StepDcretor.CURRENT_SETP = AppConfig.loginStep;
                initToSava();
                if (AppConfig.loginStep > 0) {
                    AppConfig.loginStep = 0;
                }
            }
        }
    }

    public synchronized static void initGetBeforStep() {
        int beforeStep = 0;
        beforeStep = (int) SPUtils.get("beforeStep", beforeStep);
//        LogUtils.d("beforeStep:" + beforeStep);
        /**
         * 查看之前的步数
         */
        //        int noewStepBefor = 0;
        //        noewStepBefor = (int) SPUtils.get("NowStep", noewStepBefor);
        //        LogUtils.d("NowStep:" + noewStepBefor);

        /**
         * 保存当前的步数
         */
        SavaDataLocalUtils.saveDataInt(CkApplication.getInstance(), "NowStep", StepDcretor.step);
        /**
         * 获取当前的步数
         */
        int NowStep = 0;
        NowStep = (int) SPUtils.get("NowStep", NowStep);
//        LogUtils.d("NowStep:" + NowStep);

        if (beforeStep > 0 && NowStep > 0) {
            if (NowStep >= beforeStep) {
                int stepCha = NowStep - beforeStep;
                initHouTaiJiBu(NowStep, stepCha);
            } else {
                SavaDataLocalUtils.saveDataInt(CkApplication.getInstance(), "beforeStep", NowStep);
//                LogUtils.d("steps0:" + StepDcretor.CURRENT_SETP);
                SavaDataLocalUtils.saveDataInt(CkApplication.getInstance(), "nowMyStep", StepDcretor.CURRENT_SETP);
            }
        } else if (beforeStep == 0) {
            SavaDataLocalUtils.saveDataInt(CkApplication.getInstance(), "beforeStep", NowStep);
        } else if (beforeStep > 0 && NowStep == 0) {
            SavaDataLocalUtils.saveDataInt(CkApplication.getInstance(), "beforeStep", NowStep);
        }
    }


    public synchronized static void clearData() {
        initRemoveStep("clearData", true);
        initRemoveHshStep();
        StepDcretor.CURRENT_SETP = 0;
        HomeFragment.bushu = 0;
        DbUtils.deleteAll(StepData.class);
        LogUtils.d("clearData");
    }

    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //日期修改
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        //关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filter.addAction(AppConfig.ACTION_LOGIN);
        filter.addAction(AppConfig.ACTION_GATT_SERVICES_DISCOVERED);
        filter.addAction(AppConfig.ACTION_GATT_DISCONNECTED);
        filter.addAction(AppConfig.ACTION_DATA_AVAILABLE_TIME);
        filter.addAction(AppConfig.ACTION_LOGIN_OUT);
        filter.addAction(AppConfig.ACTION_PHONE);
        /**
         * 数据同步，等睡眠数据到位再进行
         */

        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();

                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    save();
                    LogUtils.v("screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    LogUtils.v("screen off");
                    save();
                    //改为60秒一存储
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    LogUtils.d("screen unlock");
                    save();
                    //改为30秒一存储
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    LogUtils.d(" receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                    //保存一次
                    save();
                } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                    LogUtils.d(" receive ACTION_SHUTDOWN");
                    save();
                } else if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction())) {
                    LogUtils.d(" receive ACTION_TIME_CHANGED");
                    new Thread(new Runnable() {
                        public void run() {
                            StepDcretor.step = 0;
                            initToOnStartCommand();
                        }
                    }).start();
                    //日期便会清零

                } else if (AppConfig.ACTION_LOGIN.equals(intent.getAction())) {
                    SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "beforDataTiime", getTodayDate());
                    new Thread(new Runnable() {
                        public void run() {
                            initAgagin("ACTION_LOGIN", false);
                            startStepDetector();
                            StepDcretor.stepBoolean = false;
                            String nameStep = "0";
                            nameStep = (String) SPUtils.get(CkApplication.getInstance(), "nameStepType", nameStep);
                            if (nameStep.equals("0")) {
                                int steps = 0;
                                if (StepDcretor.CURRENT_SETP == 0) {
                                    steps = (int) SPUtils.get("steps", steps);
                                    //                                    Log.i(AppConfig.TAG, "steps_ACTION_LOGIN:" + steps);
                                    StepDcretor.CURRENT_SETP = steps;
                                }
                            }
                        }
                    }).start();
                } else if (AppConfig.ACTION_GATT_DISCONNECTED.equals(intent.getAction())) {
                    StepDcretor.flagStep = false;
                } else if (AppConfig.ACTION_GATT_SERVICES_DISCOVERED.equals(intent.getAction())) {
                    StepDcretor.flagStep = true;
                } else if (AppConfig.ACTION_DATA_AVAILABLE_TIME.equals(action)) {
                } else if (AppConfig.ACTION_LOGIN_OUT.equals(action)) {

                } else if (AppConfig.ACTION_PHONE.equals(action)) {
                    new Thread(new Runnable() {
                        public void run() {
                            initToOnStartCommand();
                        }
                    }).start();
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }

    private synchronized static void initRemoveHshStep() {
        String data = (String) SPUtils.get(CkApplication.getInstance(), "hsh_step_data", StepService.getTodayDate());
        if (!data.equals(StepService.getTodayDate())) {
            SPUtils.remove(CkApplication.getInstance(), "hsh_step");
            MoudleUtils.broadcastUpdate(CkApplication.getInstance(), AppConfig.ACTION_HSH_STEP_0);
        }
    }


    /**
     * 清除本地记录的步数
     *
     * @param name
     */
    public synchronized static void initRemoveStep(String name, boolean flag) {
        StepDcretor.step = 0;
        SPUtils.remove(CkApplication.getInstance(), "NowStep");
        SPUtils.remove(CkApplication.getInstance(), "beforeStep");
        SPUtils.remove(CkApplication.getInstance(), "nowMyStep");
        if (!name.equals("ACTION_LOGIN")) {
            SPUtils.remove(CkApplication.getInstance(), "steps");
        }
        LogUtils.v("initRemoveStep！");
    }

    private void startTimeCount() {
        time = new TimeCount(duration, 1000);
        time.start();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private synchronized void startStepDetector() {
        try {
            if (sensorManager != null && stepDetector != null) {
                sensorManager.unregisterListener(stepDetector);
                sensorManager = null;
                stepDetector = null;
            }
            sensorManager = (SensorManager) this
                    .getSystemService(SENSOR_SERVICE);
            getLock(this);
            addBasePedoListener();
            addCountStepListener();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addCountStepListener() {
        detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (detectorSensor != null) {
            if (sensorManager.registerListener(stepDetector, detectorSensor, SensorManager.SENSOR_DELAY_FASTEST)) {
                LogUtils.d("detectorSensor");
            }
        } else {
            LogUtils.d("Count detectorSensor not available!");
        }
        if (countSensor != null) {

            if (sensorManager.registerListener(stepDetector, countSensor, SensorManager.SENSOR_DELAY_FASTEST)) {
                LogUtils.d("countSensor");
            }
        } else {
            LogUtils.d("Count countSensor not available!");
        }
    }

    private void addBasePedoListener() {
        if (stepDetector == null) {
            stepDetector = new StepDcretor();
        }
        // 获得传感器的类型，这里获得的类型是加速度传感器
        // 此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
        sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            sensorManager.registerListener(stepDetector, sensor,
                    SensorManager.SENSOR_DELAY_UI);
        } else {
            LogUtils.d("Count sensor(TYPE_ACCELEROMETER) not available!");

        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            time.cancel();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

    private synchronized void save() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    if (initTimeChange())
                        return;
                    if (!StepDcretor.stepBoolean) {
                        if (!StepDcretor.flagStep) {
                            //1：已绑定；0：未绑定
                            String nameStep = "0";
                            nameStep = (String) SPUtils.get(CkApplication.getInstance(), "nameStepType", nameStep);
                            if (nameStep.equals("0")) {
                                initToSava();
                            }
                        }
                    }
                }
            }
        });
    }

    private synchronized void initToSava() {
        try {
            int tempStep = StepDcretor.CURRENT_SETP;
            List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENTDATE});
            if (list.size() == 0 || list.isEmpty()) {
                StepData data = new StepData();
                data.setToday(CURRENTDATE);
                data.setStep(tempStep + "");
                DbUtils.insert(data);
            } else if (list.size() == 1) {
                StepData data = list.get(0);
                data.setStep(tempStep + "");
                DbUtils.update(data);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean initTimeChange() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。

        t.setToNow(); // 取得系统时间。

        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        String beforData = (String) SPUtils.get(CkApplication.getInstance(), "beforDataTiime", getTodayDate());
        //        LogUtils.d("save:beforDataTiime:" + beforData);
        if (hour == 0 && minute == 0 && second == 0) {
            initTimeChangeClear();
            return true;
        }
        if (!getTodayDate().equals(beforData)) {
            initTimeChangeClear();
            return true;
        }
        return false;
    }

    private synchronized void initTimeChangeClear() {
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "beforDataTiime", getTodayDate());
        clearData();
        initAgagin("initTimeChangeClear", true);
        startStepDetector();
    }


    @Override
    public void onDestroy() {
        //取消前台进程

        stopForeground(true);
        DbUtils.closeDb();
        unregisterReceiver(mBatInfoReceiver);
        Intent intent = new Intent(this, StepService.class);
        startService(intent);
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }



    synchronized private PowerManager.WakeLock getLock(Context context) {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld())
                mWakeLock.release();
            mWakeLock = null;
        }

        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    StepService.class.getName());
            mWakeLock.setReferenceCounted(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 23 || hour <= 6) {
                mWakeLock.acquire(5000);
            } else {
                mWakeLock.acquire(300000);
            }
        }
        return (mWakeLock);
    }
}
