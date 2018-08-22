package com.ckjs.ck.Tool;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.amap.api.trace.TraceLocation;
import com.ckjs.ck.Android.HomeModule.Activity.BackStateActivity;
import com.ckjs.ck.Android.HomeModule.Activity.BindActivity;
import com.ckjs.ck.Android.HomeModule.Activity.DeviceControlActivity;
import com.ckjs.ck.Android.HomeModule.Activity.RealNameActivity;
import com.ckjs.ck.Android.HomeModule.Activity.RealNameYsActivity;
import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Android.LoginActivity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.RuntrackBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Service.StepService;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.RememberStep.StepDcretor;
import com.ckjs.ck.Tool.ViewTool.GoodView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.ufreedom.floatingview.Floating;
import com.ufreedom.floatingview.FloatingBuilder;
import com.ufreedom.floatingview.FloatingElement;
import com.ufreedom.floatingview.spring.SimpleReboundListener;
import com.ufreedom.floatingview.spring.SpringHelper;
import com.ufreedom.floatingview.transition.BaseFloatingPathTransition;
import com.ufreedom.floatingview.transition.FloatingPath;
import com.ufreedom.floatingview.transition.PathPosition;
import com.ufreedom.floatingview.transition.YumFloating;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.data.JPushLocalNotification;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 * 封装的各个公共使用的方法
 */

public class MoudleUtils {


    private static BluetoothGattCallback mGattCallback;
    private static String mDeviceAddress;


    public static void unregisterReceivermGattUpdateReceiver(Context context, BroadcastReceiver mGattUpdateReceiver) {
        if (mGattUpdateReceiver != null) {
            context.unregisterReceiver(mGattUpdateReceiver);
        }
    }

    /**
     * @return 注册蓝牙回掉广播
     */
    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConfig.ACTION_GATT_CONNECTED);
        intentFilter.addAction(AppConfig.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(AppConfig.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(AppConfig.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(AppConfig.ACTION_DATA_AVAILABLE_TIME);
        intentFilter.addAction(AppConfig.ACTION_DATA_AVAILABLE_WRITE);
        intentFilter.addAction(AppConfig.ACTION_FD);
        intentFilter.addAction(AppConfig.EXTRA_DATA);
        intentFilter.addAction(AppConfig.EXTRA_DATA_UUID);
        intentFilter.addAction(AppConfig.ACTION_LOGIN_OUT);
        intentFilter.addAction(AppConfig.ACTION_XIN_LV);
        intentFilter.addAction(AppConfig.ACTION_LOGIN);
        intentFilter.addAction(AppConfig.ACTION_START_NOT);
        intentFilter.addAction(AppConfig.ACTION_HSH_STEP_0);
        intentFilter.addAction(AppConfig.ACTION_DATA_AVAILABLE_TIME_SPORT_MODE);
        intentFilter.addAction(AppConfig.ACTION_STEP_TB);
        return intentFilter;
    }


    public synchronized static void broadcastUpdate(Context context, final String action) {
        final Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }


    public synchronized static void broadcastUpdate(Context context, final String action,
                                                    final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        String uuid = characteristic.getUuid().toString();
        byte[] heartRate = characteristic.getValue();

        if (heartRate.length != 0 && uuid != null) {
            intent.putExtra(AppConfig.EXTRA_DATA_UUID, uuid);
            intent.putExtra(AppConfig.EXTRA_DATA, heartRate);
        }
        context.sendBroadcast(intent);
    }

    public synchronized static void broadcastUpdate(Context context, final String action,
                                                    final int value) {
        final Intent intent = new Intent(action);
        intent.putExtra(AppConfig.EXTRA_VALUE, value);
        context.sendBroadcast(intent);
    }


    public static BluetoothGattCallback initCallBack(final Context context) {
        if (mGattCallback == null) {
            mGattCallback = new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {

                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        if (AppConfig.mBluetoothGatt == null) {
                            initHSHDisconet(0, context, "无法进行连接");
                            return;
                        }
                        initHSHAcssces(newState, context);
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                        initHSHDisconetDk(0, context, "连接断开了");
                    } else {
                        initHSHDisconet(0, context, "连接断开");
                    }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        AppConfig.bluetoothGattServices = MoudleUtils.getSupportedGattServices(context);
                        if (AppConfig.bluetoothGattServices == null) {
                            initHSHDisconet(0, context, "连接后获取不到服务");
                            return;
                        } else if (AppConfig.bluetoothGattServices.size() == 0) {
                            initHSHDisconet(0, context, "连接后获取不到相关通讯服务");
                            return;
                        }
                        broadcastUpdate(context, AppConfig.ACTION_GATT_SERVICES_DISCOVERED);

                    } else {
                        initHSHDisconet(0, context, "连接后无法进行通讯服务获取");
                    }
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        synchronized (this) {
                            broadcastUpdate(context, AppConfig.ACTION_DATA_AVAILABLE, characteristic);
                        }
                    }

                }

                @Override
                public void onCharacteristicWrite(BluetoothGatt gatt,
                                                  BluetoothGattCharacteristic characteristic, int status) {
                    synchronized (this) {
                        if (status == 0) {
                            //                            Log.d(AppConfig.TAG, "--------write success----- status:" + status);
                            broadcastUpdate(context, AppConfig.ACTION_DATA_AVAILABLE_WRITE, characteristic);
                        } else {
                            initNotWrite(context, characteristic, status);
                        }
                    }

                }


                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt,
                                                    BluetoothGattCharacteristic characteristic) {
                    synchronized (this) {
                        broadcastUpdate(context, AppConfig.ACTION_DATA_AVAILABLE_TIME, characteristic);
                        if ((boolean) (SPUtils.get("isSportMode", false))) {
                            if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                                if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                                    if ((boolean) (SPUtils.get("isSportModeRun", false))) {
                                        //进行运动模式的数据存储的广播
                                        //broadcastUpdate(context, AppConfig.ACTION_DATA_AVAILABLE_TIME_SPORT_MODE, characteristic);
                                        initPostRunData(characteristic);
                                    }
                                }
                            }
                        }

                    }
                }

                @Override
                public void onDescriptorWrite(BluetoothGatt gatt,
                                              BluetoothGattDescriptor descriptor, int status) {

                }

                @Override
                public void onReadRemoteRssi(BluetoothGatt gatt, final int rssi, int status) {
                    MoudleUtils.initFd();
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        MoudleUtils.broadcastUpdate(context, AppConfig.ACTION_FD, rssi);
                    } else {
                        //                        Log.w(AppConfig.TAG, "Error getting RSSI value.");
                    }
                }

            };
        }
        return mGattCallback;
    }

    /**
     * user_id	用户id
     * token	token值
     * rate	心率
     * oxygen	血氧
     * prate	脉率
     * spressure	收缩压
     * dpressure	舒张压
     *
     * @param characteristic
     */

    private static void initPostRunData(BluetoothGattCharacteristic characteristic) {
        if (characteristic == null)
            return;
        if (characteristic.getUuid().toString() == null)
            return;
        if (characteristic.getUuid().toString().contains("fff5")) {
            byte[] bb = characteristic.getValue();
            if (bb == null || bb.length < 6)
                return;
            if (bb[0] != (byte) 0x01) {
                return;
            }
            int heartRate[] = DataUtils.byteTo10(bb);
            if (heartRate == null || heartRate.length < 6)
                return;
            NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
            int user_id = (int) (SPUtils.get("user_id", 0));
            String token = (String) SPUtils.get("token", "");
            //            Log.d(AppConfig.TAG, heartRate[1] + "，" + heartRate[2] + "，" + heartRate[3] + "，" + heartRate[4] + "，" + heartRate[5]);

            Call<AcceptBean> callBack = restApi.upmovement(token, user_id, heartRate[1] + "", heartRate[2] + "", heartRate[3] + "", heartRate[4] + "", heartRate[5] + "");

            callBack.enqueue(new Callback<AcceptBean>() {
                @Override
                public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {

                }

                @Override
                public void onFailure(Call<AcceptBean> call, Throwable t) {
                }
            });
        }
    }


    static synchronized void initNotWrite(Context context, BluetoothGattCharacteristic characteristic, int status) {
        //        Log.d(AppConfig.TAG, "--------write not----- status:" + status);
        if (characteristic != null) {
            String uuid = characteristic.getUuid().toString();
            byte[] data = characteristic.getValue();
            if (uuid.contains("fff1")) {
                if (data != null) {
                    if (data.length == 4) {
                        //                        Log.d(AppConfig.TAG, "--------write not----- uuid:" + uuid + "," + data[0]);
                        if (data[0] == (byte) 0xF1) {
                            broadcastUpdate(context, AppConfig.ACTION_START_NOT);
                        }
                    }
                }
            }
        }
    }

    //A和n的值，需要根据实际环境进行检测得出
    public static int A_Value = 62;
    /**
     * A - 发射端和接收端相隔1米时的信号强度
     */
    public static double n_Value = 4.05;

    /**
     * n - 环境衰减因子
     */
    public static double getDistance(int rssi) {
        double power = (rssi - A_Value) / (10 * n_Value);
        return Math.pow(10, power);
    }

    public static void initToFd() {
        JPushLocalNotification ln = new JPushLocalNotification();
        ln.setBuilderId(0);
        ln.setContent("快看看超空手环是否在您身边");
        ln.setTitle("超空防丢通知");
        ln.setNotificationId(11111111);

        JPushInterface.addLocalNotification(CkApplication.getInstance(), ln);
    }

    /**
     * if (groupPosition==0){
     * groupViewHolder.sw_xduan_ze.setChecked((Boolean) SPUtils.get(content,"phoneShake",false));
     * }else if (groupPosition==1){
     * groupViewHolder.sw_xduan_ze.setChecked((Boolean) SPUtils.get(content,"smsShake",false));
     * }else if (groupPosition==2){
     * groupViewHolder.sw_xduan_ze.setChecked((Boolean) SPUtils.get(content,"longtime",false));
     * }else if (groupPosition==4){
     * groupViewHolder.sw_xduan_ze.setChecked((Boolean) SPUtils.get(content,"goalset",false));
     * }else if (groupPosition==5){
     * groupViewHolder.sw_xduan_ze.setChecked((Boolean) SPUtils.get(content,"antilost",false));
     * }
     *
     * @param context
     */
    public static void initChuShiHua(Context context) {
        SPUtils.put(context, "longtime", false);//久坐关闭
        SPUtils.put(context, "clock", false);//闹钟开关
        SPUtils.put(context, "isSportMode", false);
        SPUtils.put(context, "isSportModeRun", false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //                Log.d(AppConfig.TAG, "日常模式");
                //日常模式
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x0B, 0x02, 0x00, 0x00});
            }
        }, 500);
        /**
         * 开启健康数据上传
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //开启健康数据上传
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff5", AppConfig.FFF5);

            }
        }, 1000);

        String clockhour = (String) SPUtils.get(context, "clockhour", "");
        if (clockhour != null && !clockhour.equals("")) {
            return;
        }
        //        Log.d(AppConfig.TAG, "csh:" + clockhour);
        /**
         * 所有设置变为打开状态
         */
        AppConfig.mastershake = true;
        SPUtils.put(context, "mastershake_all", AppConfig.mastershake);
        SPUtils.put(context, "phoneShake", true);
        SPUtils.put(context, "smsShake", true);
        SPUtils.put(context, "goalset", true);
        SPUtils.put(context, "antilost", true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //震动总开关
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x02, 0x01, 0x00, 0x00});
            }
        }, 1500);
        //闹钟
        SPUtils.put(context, "clockhour", "设置时间");
        //防丢
        SPUtils.put(context, "losedistance", "3");
        //久坐
        SPUtils.put(context, "timeset", "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff2");
            }
        }, 3000);
    }

    /**
     * 对时
     * String year16 = Integer.toHexString(year);
     * if (year16.length()==3){
     * year16="0"+year16;
     * }
     * String preyear="0x"+year16.substring(0,2);
     * String lastyear="0x"+year16.substring(2,year16.length());
     * <p>
     * BluethUtils.displayGattServices(AppConfig.bluetoothGattServices,"fff7",new byte[]{0x02,(byte)Integer.parseInt(preyear,16),
     * (byte)Integer.parseInt(lastyear,16),(byte)checkData(month),(byte)checkData(day),(byte)checkData(hour),(byte)checkData(minute),(byte)checkData(second)});
     */
    public static void initTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        int day = c.get(Calendar.DAY_OF_MONTH);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        String s = "02" + checkData(year) + checkData(month) + checkData(day) + checkData(hour) + checkData(minute) + checkData(second);
        //        LogUtils.d("sTime:"+s);
        byte[] bytes = DataUtils.hexStrToByteArray(s, 2);
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff7", bytes);
    }


    /**
     * String s = Integer.toHexString(str);
     * if (s.length() == 1) {
     * s = "0x0" + s;
     * }
     * if (s.length() == 2) {
     * s = "0x" + s;
     * }if (s.length() == 3) {
     * s = "0x0" + s;
     * }
     * <p>
     * return s;
     *
     * @param str
     * @return
     */
    public static String checkData(int str) {
        String s = Integer.toHexString(str);
        if (s.length() == 1) {
            s = "0" + s;
        }
        if (s.length() == 3) {
            s = "0" + s;
        }

        return s;
    }


    /**
     * 手环连接出现异常
     *
     * @param newState
     * @param context
     */
    public static void initHSHDisconet(int newState, Context context, String s) {
        AppConfig.NEWSTATE = newState;
        initJieBang(newState);
        initRunShow(context, s);
    }

    /**
     * 手环连接断开
     *
     * @param newState
     * @param context
     */
    public static void initHSHDisconetDk(int newState, Context context, String s) {

        initJieBangDk(newState);
        broadcastUpdate(context, AppConfig.ACTION_GATT_DISCONNECTED);
        initRunShow(context, s);
        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
        MoudleUtils.kyloadingDismiss(DeviceControlActivity.kyLoadingBuilderDEvice);
    }


    /**
     * 手环绑定完成
     *
     * @param newState
     * @param context
     * @param
     */
    static void initHSHAcssces(int newState, Context context) {
        //0手机设备 1手环设备
        StepDcretor.flagStep = true;
        SPUtils.put(CkApplication.getInstance(), "nameStepType", "1");
        SPUtils.put(context, "isSportMode", false);
        SPUtils.put(context, "isSportModeRun", false);
        initFd();
        initSavaMac(context, mDeviceAddress);
        AppConfig.NEWSTATE = newState;
        AppConfig.mBluetoothGatt.discoverServices();//触发onServicesDiscovered
        initRunShow(context, "手环开始进行通讯");
        broadcastUpdate(context, AppConfig.ACTION_GATT_CONNECTED);
    }

    /**
     * 进行防丢回掉
     */
    public synchronized static void initFd() {
        if (AppConfig.mBluetoothGatt != null) {
            AppConfig.mBluetoothGatt.readRemoteRssi();
        }
    }

    /**
     * 绑定成功保存mac
     *
     * @param context
     * @param mDeviceAddress
     */
    public static void initSavaMac(Context context, String mDeviceAddress) {
        SavaDataLocalUtils.saveDataString(context, "mDeviceAddress", mDeviceAddress);
    }


    private static void initRunShow(Context context, final String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message != null && !message.equals("")) {
                    ToastUtils.showShortNotInternet(message);
                }
            }
        });
    }


    public synchronized static void initToBd(Context context, String name) {
        try {
            if ((SPUtils.get(context, "vip", "").equals("1"))) {
                if ((SPUtils.get(context, "type", "") + "").equals("1")) {
                    context.startActivity(new Intent().setClass(context, RealNameYsActivity.class));
                } else {
                    context.startActivity(new Intent().setClass(context, RealNameActivity.class));
                }
            } else {
                if ((SPUtils.get(context, "mDeviceAddress", "") + "") != null
                        && !(SPUtils.get(context, "mDeviceAddress", "") + "").equals("")
                        ) {
                    initAgainBind(context, name);
                } else {
                    //0可用1不可用
                    if ((SPUtils.get("unrentstatus", "0") + "").equals("1")) {
                        context.startActivity(new Intent().setClass(context, BackStateActivity.class));
                    } else if ((SPUtils.get("unrentstatus", "0") + "").equals("0")) {
                        context.startActivity(new Intent().setClass(context, BindActivity.class));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地重新绑定
     *
     * @param context
     * @param name
     */
    private synchronized static void initAgainBind(Context context, String name) {
        if (AppConfig.mBluetoothGatt == null || AppConfig.bluetoothGattServices == null) {
            if (AppConfig.NEWSTATE != -1) {
                MoudleUtils.connect(context, SPUtils.get(context, "mDeviceAddress", "") + "");
            }
        } else {
            if (name.equals("mine")) {
                if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                    context.startActivity(new Intent().setClass(context, DeviceControlActivity.class));
                }
            }
        }
    }


    public static void initJieBang(int newState) {

        if (AppConfig.mBluetoothGatt != null) {
            AppConfig.mBluetoothGatt.disconnect();
        }

    }

    public static void initTongBuData() {
        initListPostSc();
        HomeFragment.nSleep = 0;
        HomeFragment.nSleepAll = -1;
        HomeFragment.nSleepI = 1;
        HomeFragment.nHealthI = 0;
        HomeFragment.nSleepFlag = false;
        HomeFragment.nHealthFlag = false;
        HomeFragment.nHealthn = 0;
        HomeFragment.nHealthAll = -1;
        HomeFragment.flagStartCheckSleep = false;
    }

    public static void initListPostSc() {
        if (HomeFragment.listHealth != null) {
            HomeFragment.listHealth.clear();
        }
        if (HomeFragment.listSleep != null) {
            HomeFragment.listSleep.clear();
        }
    }

    //手环连接断开进行释放资源
    public static void initJieBangDk(int newState) {
        AppConfig.dl = 0;
        StepDcretor.flagStep = false;
        AppConfig.flag = false;
        initTongBuData();
        AppConfig.NEWSTATE = newState;
        SPUtils.put(CkApplication.getInstance(), "isSportMode", false);
        SPUtils.put(CkApplication.getInstance(), "isSportModeRun", false);
        if (AppConfig.bluetoothGattServices != null) {
            AppConfig.bluetoothGattServices.clear();
        }
        if (AppConfig.mBluetoothGatt != null) {
            AppConfig.mBluetoothGatt.close();

        }
        AppConfig.mBluetoothGatt = null;
        AppConfig.bluetoothGattServices = null;
        HomeFragment.bushu = 0;
        MoudleUtils.broadcastUpdate(CkApplication.getInstance(), AppConfig.ACTION_PHONE);
    }

    public static List<BluetoothGattService> getSupportedGattServices(Context context) {
        if (AppConfig.mBluetoothGatt == null) {
            initHSHDisconet(0, context, "连接时出现异常，请重新连接");
            return null;
        }
        return AppConfig.mBluetoothGatt.getServices();
    }

    public static KyLoadingBuilder kyLoadingBuilder;

    public synchronized static boolean connect(Context context, String myDeviceAddress) {
        try {
            kyLoadingBuilder = new KyLoadingBuilder(context);
            MoudleUtils.kyloadingShow(kyLoadingBuilder, false, true);
            mDeviceAddress = myDeviceAddress;
            MoudleUtils.initCallBack(context);
            if (BluethUtils.bluetoothAdapter == null || mDeviceAddress == null || mDeviceAddress.equals("")) {
                initHSHDisconet(0, context, "连接时出现异常，请重新连接");
                return false;
            }
            BluetoothDevice device = null;
            if (BluethUtils.bluetoothAdapter == null || myDeviceAddress == null) {
                initHSHDisconet(0, context, "连接时出现异常，请重新连接");
                return false;
            }
            device = BluethUtils.bluetoothAdapter
                    .getRemoteDevice(mDeviceAddress);
            if (device == null) {
                initHSHDisconet(0, context, "连接时出现异常，请重新连接");
                return false;
            }
            AppConfig.mBluetoothGatt = device.connectGatt(context, false, mGattCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public static byte[] reverseBytes(byte[] a) {
        int len = a.length;
        byte[] b = new byte[len];
        for (int k = 0; k < len; k++) {
            b[k] = a[a.length - 1 - k];
        }
        return b;
    }

    // byte转十六进制字符串
    public static String bytes2HexString(byte[] bytes) {
        String ret = "";
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * 将AMapLocation List 转为TraceLocation list
     *
     * @param list
     * @return
     */
    public static List<TraceLocation> parseTraceLocationList(
            List<RuntrackBean.RuntrackBeanInfo.Track> list) {
        List<TraceLocation> traceList = new ArrayList<>();
        if (list == null) {
            return traceList;
        }
        for (int i = 0; i < list.size(); i++) {
            RuntrackBean.RuntrackBeanInfo.Track runtrackBeanInfo = list.get(i);
            if (runtrackBeanInfo == null) {
                return traceList;
            }
            TraceLocation location = new TraceLocation();
            location.setLatitude(Double.parseDouble(runtrackBeanInfo.getLat()));
            location.setLongitude(Double.parseDouble(runtrackBeanInfo.getLon()));
            traceList.add(location);
        }
        return traceList;
    }

    /**
     * 点赞效果/收藏效果
     */
    public static void PointPraiseEffect(GoodView mGoodView, View view, Context context, int id) {
        try {
            if (mGoodView != null) {
                if (view != null) {
                    if (context != null) {
                        mGoodView.setImage(context.getResources().getDrawable(id));
                        mGoodView.show(view);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * HorizontalScrollView跳转到最右边
     */
    public static void initHTooRight(Handler mHandler, final HorizontalScrollView h_years) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                h_years.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

            }
        });
    }

    /**
     * if ever logged in
     *
     * @return
     */
    public static boolean isLoggedIn() {
        try {
            if (EMClient.getInstance().isLoggedInBefore()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 环信进行注册和登录
     *
     * @param et_name
     * @param et_pass
     */
    public static void hxLG(String et_name, String et_pass) {
        try {
            MoudleUtils.hxLogin(et_name, et_pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 环信注册
     */
    public static void hxRegist(final String username, final String pwd) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(username, pwd);

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 环信登录
     */
    public static void hxLogin(String et_name, String et_pass) {
        EMClient.getInstance().login(et_name, et_pass, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                LogUtils.d("登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                LogUtils.d("登录聊天服务器失败！");
            }
        });
    }

    /**
     * token==2时跳转登录页面
     *
     * @param context
     */
    public static void initToLogin(Context context) {
        initStatusTwo(context, true);
    }


    /**
     * 图片上传转Base64码
     *
     * @param bitmap
     * @return
     */
    public static String Bitmap2Base64(Bitmap bitmap, int n) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int option = 100;
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
            //            Log.i(TAG, "baos.toByteArray().length/1024_1:：" + baos.toByteArray().length / 1024);
            while (baos.toByteArray().length / 1024 > 100 && option > 6) {
                baos.reset();
                option -= 6;
                bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
            }
            //            Log.i(TAG, "baos.toByteArray().length/1024_2：" + baos.toByteArray().length / 1024);
            //            Log.i(TAG, "option：" + option);
            byte[] bytes = baos.toByteArray();

            return Base64.encodeToString(bytes, Base64.CRLF);
        } else {
            return null;
        }
    }


    /**
     * textView.setText(s);
     *
     * @param textView
     * @param s
     */
    public static void textViewSetText(TextView textView, String s) {
        if (s == null) {
            s = "";
        }
        if (textView != null) {
            textView.setText(s);
        }
    }

    /**
     * 退出登录清除本地数据和别名
     *
     * @param context
     */
    public static void initOut(Context context) {
        StepDcretor.stepBoolean = true;
        StepService.clearData();
        SPUtils.clear(context);
        SPUtils.put(context, "first", false);
        initJieBang(0);
        initSaveBeforStep();
        EMClient.getInstance().logout(true);
        MoudleUtils.mySetAlias("");
    }

    public static void initSaveBeforStep() {
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "beforDataTiime", StepService.getTodayDate());
        StepService.initRemoveStep("initSaveBeforStep", true);
    }


    public static AlertDialog alertDialog = null;

    /**
     * 重新登录的弹框提示
     */
    public synchronized static void initStatusTwo(final Context context, final boolean flag) {
        if (alertDialog == null && context != null) {
            alertDialog = new AlertDialog.Builder(context).setTitle("请重新登录")
                    .setMessage("1.可能此账号已在其他设备登录\n\n2.可能清除了用户账户信息等等")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            initOut(context);
                            initOutNofiySetThis();
                            toLogin(context);
                        }

                    }).create(); // 创建对话框
            if (alertDialog != null) {
                alertDialog.setCancelable(false);
                if (!alertDialog.isShowing()) {
                    alertDialog.show(); // 显示
                }
            }
        }

    }

    public synchronized static void toLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


    /**
     * 退出登录的各个模块的通知
     */
    public synchronized static void initOutNofiySetThis() {
        broadcastUpdate(CkApplication.getInstance(), AppConfig.ACTION_LOGIN_OUT);
    }

    /**
     * 登录的各个模块的通知
     */
    public synchronized static void initToNotifyLoginSetThis() {

        broadcastUpdate(CkApplication.getInstance(), AppConfig.ACTION_LOGIN);

    }

    /**
     * 下拉刷新控件隐藏
     *
     * @param swipeRefreshLayoutTj
     */
    public static void initSwipeRefreshLayoutFalse(SwipeRefreshLayout swipeRefreshLayoutTj) {
        if (swipeRefreshLayoutTj != null) {
            if (swipeRefreshLayoutTj.isRefreshing()) {
                swipeRefreshLayoutTj.setRefreshing(false);
            }
        }
    }

    /**
     * 下来刷新控件出现
     *
     * @param swipeRefreshLayoutTj
     */
    public static void initRefrushTrue(SwipeRefreshLayout swipeRefreshLayoutTj) {
        if (swipeRefreshLayoutTj != null) {
            if (!swipeRefreshLayoutTj.isRefreshing()) {
                swipeRefreshLayoutTj.setRefreshing(true);
            }
        }
    }

    /**
     * 下拉刷新控件不可用
     *
     * @param swipeRefreshLayoutTj
     */
    public static void initF(SwipeRefreshLayout swipeRefreshLayoutTj) {
        if (swipeRefreshLayoutTj != null) {
            if (swipeRefreshLayoutTj.isEnabled()) {
                swipeRefreshLayoutTj.setEnabled(false);
            }
        }
    }

    /**
     * 下拉刷新控件可用
     *
     * @param swipeRefreshLayoutTj
     */
    public static void initT(SwipeRefreshLayout swipeRefreshLayoutTj) {
        if (swipeRefreshLayoutTj != null) {
            if (!swipeRefreshLayoutTj.isEnabled()) {
                swipeRefreshLayoutTj.setEnabled(true);
            }
        }
    }

    /**
     * 网络不好提示
     *
     * @param context
     */
    public static void toChekWifi(Context context) {
        try {
            ToastUtils.showShort(context, context.getResources().getString(R.string.not_wlan_show));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络不好提示
     */
    public static void toChekWifi() {
        try {
            ToastUtils.showShort(CkApplication.getInstance(), CkApplication.getInstance().getResources().getString(R.string.not_wlan_show));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片压缩
     *
     * @param bitmap
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, double width, double height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) (width / w));
        float scaleHeight = ((float) (height / h));
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }


    /**
     * 朋友圈图片上传的图片压缩的原始图片的比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 从BitmapFactory.Options中获取图片的原始高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // 计算原始尺寸与显示尺寸的比例
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // 使用两个比例中较小的作为采样比，可以保证图片在两个方向上都满足需求
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // 为了防止一些纵横比异常（很宽或者很长）的图片仍然会占用较大内存，增加
            // 对显示像素的限制
            final float totalPixels = width * height;

            // 如果以前面计算到的采样比采样得到的bitmap像素大于请求的像素的两倍，继续
            // 提高采样比
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * 朋友圈图片上传的图片压缩的原始图片
     *
     * @param imagePath
     * @param requestWidth
     * @param requestHeight
     * @return
     */
    public static Bitmap getSmallBitmap(String imagePath, int requestWidth, int requestHeight) {
        if (!TextUtils.isEmpty(imagePath)) {
            if (requestWidth <= 0 || requestHeight <= 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                return bitmap;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//不加载图片到内存，仅获得图片宽高
            BitmapFactory.decodeFile(imagePath, options);
            if (options.outHeight == -1 || options.outWidth == -1) {
                try {
                    ExifInterface exifInterface = new ExifInterface(imagePath);
                    int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                    int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                    options.outWidth = width;
                    options.outHeight = height;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight); //计算获取新的采样率
            options.inJustDecodeBounds = false;
            //避免出现内存溢出的情况，进行相应的属性设置。
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            return BitmapFactory.decodeFile(imagePath, options);

        } else {
            return null;
        }
    }

    //    public static String TAG = "tag";

    /**
     * 保存登录信息
     *
     * @param context
     */
    public static void initSaveData(Context context) {
        AppConfig.token = (String) SPUtils.get(context, "token", "");
        AppConfig.user_id = (int) SPUtils.get(context, "user_id", 0);
        AppConfig.picurl = (String) SPUtils.get(context, "picurl", "");
        AppConfig.gym_id = (int) SPUtils.get(context, "gym_id", 1);
        AppConfig.height = (int) SPUtils.get(context, "height", 0);
        AppConfig.weight = (float) SPUtils.get(context, "weight", AppConfig.weight);
        AppConfig.name = (String) SPUtils.get(context, "name", "");
        AppConfig.firstLogin = (Boolean) SPUtils.get(context, "firstLogin", true);
        AppConfig.vip = (String) SPUtils.get(context, "vip", "");
        AppConfig.fanssum = (String) SPUtils.get(context, "fanssum", "");
        AppConfig.motto = (String) SPUtils.get(context, "motto", "");
    }


    /**
     * 加载提示框 显示
     *
     * @param dialog
     */
    public static void dialogShow(ProgressDialog dialog) {
        try {

            if (dialog != null) {
                dialog.setTitle("正在加载...请耐心等待");
                dialog.setCancelable(false);
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载提示框 显示
     *
     * @param dialog
     */
    public static void dialogShow(ProgressDialog dialog, String msg) {
        try {

            if (dialog != null) {
                dialog.setTitle(msg);
                dialog.setCancelable(false);
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载提示框 显示
     *
     * @param dialog
     */
    public static void dialogShow(ProgressDialog dialog, boolean flag) {
        try {

            if (dialog != null) {
                dialog.setTitle("正在加载...请耐心等待");
                dialog.setCancelable(flag);
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载提示框 隐藏
     *
     * @param dialog
     */
    public static void dialogDismiss(ProgressDialog dialog) {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 载入提示框 显示
     *
     * @param
     */
    public static void kyloadingShow(KyLoadingBuilder builder) {
        try {
            if (builder != null) {
                builder.setIcon(R.drawable.upload_again);
                builder.setText("...请稍后...");
                builder.setOutsideTouchable(false);//点击空白区域是否关闭
                builder.setBackTouchable(false);//按返回键是否关闭
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 载入提示框 显示,动态控制点击空白区域是否关闭flag为true时关闭，按返回键是否关闭flagTwo为true时关闭
     *
     * @param
     */
    public static void kyloadingShow(KyLoadingBuilder builder, boolean flag, boolean flagTwo) {
        try {
            if (builder != null) {
                builder.setIcon(R.drawable.upload_again);
                builder.setText("...请稍后...");
                builder.setOutsideTouchable(flag);//点击空白区域是否关闭
                builder.setBackTouchable(flagTwo);//按返回键是否关闭
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 载入提示框 隐藏
     *
     * @param builder
     */
    public static void kyloadingDismiss(KyLoadingBuilder builder) {
        try {
            if (builder != null) {
                builder.dismiss();//关闭
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    /**
     * 相机照相后旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        return resizedBitmap;
    }

    /**
     * 图片绝对路径
     *
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 检测手机号码格式
     *
     * @param str
     * @return
     */
    public static boolean isPhone(String str) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * 设置极光推送别名
     */
    public static void mySetAlias(String alias) {
        if (alias != null && !alias.equals("0") && CkApplication.getInstance() != null) {
            JPushInterface.setAlias(CkApplication.getInstance(), (alias), new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {

                }
            });
        }
    }


    /**
     * Gone隐藏View
     *
     * @param view
     */
    public static void viewGone(View view) {
        if (view != null) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Invisibily隐藏View
     *
     * @param view
     */
    public static void viewInvisibily(View view) {
        if (view != null) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 显示View
     *
     * @param view
     */
    public static void viewShow(View view) {
        if (view != null) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 初始化存储图片的文件
     *
     * @param imageFile
     * @return 初始化成功返回true，否则false
     */
    public static boolean initImageFile(File imageFile, String wenJianJia) {
        // 有SD卡时才初始化文件
        if (hasSDCard()) {
            if (initToCacheFiles(wenJianJia)) {
                if (!imageFile.exists()) {// 如果文件不存在，就创建文件
                    try {
                        imageFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 是否生成了次文件夹
     *
     * @param wenJianJia
     * @return
     */
    private static boolean initToCacheFiles(String wenJianJia) {
        File cacheLocation = new File(wenJianJia);
        if (!cacheLocation.exists()) {
            cacheLocation.mkdirs();
        }
        return true;
    }

    /**
     * 点击按钮后调用相机拍照
     */

    public static void btnclick(Uri imageUri, File imageFile, Context context, String wenJianJia) {
        // 如果初始化文件成功，则调用相机
        if (initImageFile(imageFile, wenJianJia)) {
            //下面用第一种方式获取图片，还可以调用startTakePhoto2()方法获取图片
            startTakePhoto(imageUri, imageFile, context);
            //startTakePhoto2();
        } else {
            ToastUtils.showShortNotInternet("初始化文件失败，无法调用相机拍照！");

        }
    }

    /**
     * 点击照片后选择系统相册
     *
     * @param imageUri
     * @param imageFile
     * @param context
     * @param wenJianJia
     */

    public static void btnclickxc(Uri imageUri, File imageFile, Context context, String wenJianJia) {
        // 如果初始化文件成功，则调用相机
        if (initImageFile(imageFile, wenJianJia)) {
            selectImage(imageUri, imageFile, context);
        } else {
            ToastUtils.showShortNotInternet("初始化文件失败，无法调用相机拍照！");
        }
    }

    /**
     * 调用系统相册
     *
     * @param imageUri
     * @param imageFile
     * @param context
     */
    public static void selectImage(Uri imageUri, File imageFile, Context context) {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ((Activity) (context)).startActivityForResult(intent, AppConfig.SELECT_IMAGE);
    }

    /**
     * 判断是否有SD卡
     *
     * @return 有SD卡返回true，否则false
     */
    private static boolean hasSDCard() {
        // 获取外部存储的状态
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 有SD卡
            return true;
        }
        return false;
    }

    public static String getCameraPhoneAppInfos(Context context) {
        try {
            String strCamera = "";
            List<PackageInfo> packages = context.getPackageManager()
                    .getInstalledPackages(0);
            for (int i = 0; i < packages.size(); i++) {
                try {
                    PackageInfo packageInfo = packages.get(i);
                    String strLabel = packageInfo.applicationInfo.loadLabel(
                            context.getPackageManager()).toString();
                    // 一般手机系统中拍照软件的名字
                    if ("相机,照相机,照相,拍照,摄像,Camera,camera".contains(strLabel)) {
                        strCamera = packageInfo.packageName;
                        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (strCamera != null) {
                return strCamera;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 跳转到系统相机拍照
     * // 尽可能调用系统相机
     * try {
     * String cameraPackageName = getCameraPhoneAppInfos(context);
     * if (cameraPackageName == null) {
     * cameraPackageName = "com.android.camera";
     * }
     * final Intent intent_camera = context.getPackageManager()
     * .getLaunchIntentForPackage(cameraPackageName);
     * if (intent_camera != null) {
     * intent.setPackage(cameraPackageName);
     * }
     * } catch (Exception e) {
     * // TODO Auto-generated catch block
     * e.printStackTrace();
     * }
     */
    private static void startTakePhoto(Uri imageUri, File imageFile, Context context) {
        try {
            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // 设置拍照后保存的图片存储在文件中
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // 启动activity并获取返回数据
            ((Activity) (context)).startActivityForResult(intent, AppConfig.CAPTURE_IMAGE);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private void startTakePhoto2(Context context) {
        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 启动activity并获取返回数据
        ((Activity) (context)).startActivityForResult(intent, AppConfig.CAPTURE_IMAGE);
    }

    /**
     * 保存网络图片到本地
     *
     * @param imageFile
     * @param context
     * @param picStr
     */

    public static void savePic(File imageFile, Context context, String picStr, String wenJianJia) {
        // 如果初始化文件成功，则调用相机
        if (MoudleUtils.initImageFile(imageFile, wenJianJia)) {
            FrescoUtils.savePicture(picStr, context, imageFile);
        } else {
            ToastUtils.showShortNotInternet("初始化文件失败，保存失败");
        }
    }

    /***
     * 拍摄图片后通知手机系统相册进行扫描
     *
     * @param file
     */
    public static void initToseeDongTai(File file) {
        if (file != null) {
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                CkApplication.getInstance().sendBroadcast(intent);
                //                ToastUtils.showShort("保存成功！路径：" + file.getAbsolutePath());
            } else {
                ToastUtils.showShortNotInternet("由于手机权限原因，图片拍摄失败啦");
            }
        } else {
            ToastUtils.showShortNotInternet("由于手机权限原因，图片拍摄失败啦");
        }

    }

    /**
     * 小数 四舍五入
     *
     * @param precision
     * @return
     */
    public static String roundDouble(double price, int precision) {
        BigDecimal b = new BigDecimal(price);
        double endPrice = b.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();

        DecimalFormat df = new DecimalFormat("0.00");

        return df.format(endPrice);
    }

    public static void integralAnimation(final Context context, final View view, final String str) {
        final Floating floating = new Floating((Activity) context);


        TextView imageView = new TextView(context);
        //                imageView.setLayoutParams(new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth()/10, ScreenUtils.getScreenHeight()/10));
        imageView.setBackgroundResource(R.drawable.floating_plane);
        imageView.setText(str);
        imageView.setTextColor(CkApplication.getInstance().getResources().getColor(R.color.c_33));
        imageView.setGravity(Gravity.CENTER_HORIZONTAL);


        FloatingElement floatingElement = new FloatingBuilder()
                .anchorView(view)
                .targetView(imageView)
                .floatingTransition(new PlaneFloating())
                .offsetY((int) (view.getMeasuredHeight() * 4.5))
                .offsetX(view.getMeasuredWidth() / 4)
                .build();
        floating.startFloating(floatingElement);
    }

    static class PlaneFloating extends BaseFloatingPathTransition {

        @Override
        public FloatingPath getFloatingPath() {
            Path path = new Path();
            path.moveTo(0, 0);
            path.quadTo(100, -300, 0, -ScreenUtils.getScreenHeight());

            //            path.rLineTo(0, -ScreenUtils.getScreenHeight() - 300);
            return FloatingPath.create(path, false);
        }

        @Override
        public void applyFloating(final YumFloating yumFloating) {

            ValueAnimator translateAnimator = ObjectAnimator.ofFloat(getStartPathPosition(), getEndPathPosition());
            translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    PathPosition floatingPosition = getFloatingPosition(value);
                    yumFloating.setTranslationX(floatingPosition.x);
                    yumFloating.setTranslationY(floatingPosition.y);

                }
            });
            translateAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    yumFloating.setTranslationX(0);
                    yumFloating.setTranslationY(0);
                    yumFloating.setAlpha(0f);
                    yumFloating.clear();
                }
            });


            SpringHelper.createWidthBouncinessAndSpeed(0.0f, 1.0f, 14, 15)
                    .reboundListener(new SimpleReboundListener() {
                        @Override
                        public void onReboundUpdate(double currentValue) {
                            yumFloating.setScaleX((float) currentValue);
                            yumFloating.setScaleY((float) currentValue);
                        }
                    }).start(yumFloating);

            translateAnimator.setDuration(5000);
            translateAnimator.start();
        }
    }

    /**
     * 初始化微信回掉参数
     */
    public static void initNullPayInterfosion() {
        AppConfig.order_id = "";
        AppConfig.wxAlertDialog = null;
    }

    /************************
     * 高斯模糊处理
     * @param bitmap
     * @param context
     * @return
     ***********************/

    public static Bitmap blurBitmap(Bitmap bitmap, Context context, float radius) {

        // 用需要创建高斯模糊bitmap创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
        RenderScript rs = RenderScript.create(context);

        // 创建高斯模糊对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //设定模糊度(注：Radius最大只能设置25.f)
        blurScript.setRadius(radius);

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        // After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }
}
