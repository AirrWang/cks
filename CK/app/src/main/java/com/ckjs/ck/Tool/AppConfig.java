package com.ckjs.ck.Tool;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.os.Environment;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class AppConfig {
//    public static String class_type = "";
//    public static String NAME = "";
//    public static String RELNAME = "";
//    public static String class_id = "";
    public static String select_jsf = "";
    public static String select_jsf_id = "";
    public static String phonenum = "";
    public static String phonename = "";

    public static boolean isVideoCalling;
    //bluetooth
    public static String res = "res://com.ckjs.ck/";
    public static String file = "file://";

    public static String url = RetrofitUtils.picUrl + "public/uploads/";

    public static String url_jszd = RetrofitUtils.picUrl + "public/picture/";

    public static String shop_id = RetrofitUtils.httpH5Jq + "shop";
    public static String focus_id = RetrofitUtils.httpH5Jq + "circle/gofocusinfo";
    public static String os_type = RetrofitUtils.httpH5Jq + "circle/go_setting";

    public static String h5_shjs = RetrofitUtils.baseUrlH5 + "circle/banddetail?safe=1";//手环介绍
    public static String h5_ydqx = RetrofitUtils.baseUrlH5 + "circle/setpermission?safe=1";//设置运动权限
    public static String h5_hsf_url = RetrofitUtils.baseUrlH5 + "circle/familyhelp ";//介绍超空家
    public static String h5_szbz = RetrofitUtils.baseUrlH5 + "help/index?system=android&version=";//帮助
    public static String h5_ya_jin = RetrofitUtils.baseUrlH5 + "deposit";//了解押金


    public static String token = "";
    public static int user_id;
    public static String picurl = "";
    public static int gym_id = 1;
    public static int height;
    public static float weight;
    public static int sex;
    public static String name = "";
    public static String fanssum = " ？";
    public static String motto = "这个家伙很懒，什么都没有留下";
    public static boolean firstLogin;
    public static String order_id = "";
    public static String vip = "";
    public static String age;
    public static final int CAPTURE_IMAGE = 0x01;
    public static final int SELECT_IMAGE = 0x02;
    public static final int CROP_IMAGE = 0x03;
    public static String JPG = ".jpg";
    //自定义磁盘缓存目录
    public static String hss_save = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HSS/hss_img_save";
    public static String hss_cache = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HSS/hss_img_cache";
    public static int point_w = 60;

    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static List<BluetoothGattService> bluetoothGattServices;
    public static BluetoothGatt mBluetoothGatt;
    public static int NEWSTATE = -1;
    public final static String ACTION_GATT_CONNECTED = "com.ckjs.ck.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.ckjs.ck.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.ckjs.ck.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.ckjs.ck.ACTION_DATA_AVAILABLE";
    public final static String ACTION_DATA_AVAILABLE_TIME = "com.ckjs.ck.ACTION_DATA_AVAILABLE_TIME";//进行实时健康数据同步的数据存储的广播
    public final static String ACTION_DATA_AVAILABLE_TIME_SPORT_MODE = "com.ckjs.ck.ACTION_DATA_AVAILABLE_TIME_SPORT_MODE";//进行实时运动模式的数据存储的广播
    public final static String ACTION_DATA_AVAILABLE_WRITE = "com.ckjs.ck.ACTION_DATA_AVAILABLE_WRITE";
    public final static String ACTION_FD = "com.ckjs.ck.ACTION_FD";
    public final static String EXTRA_DATA = "com.ckjs.ck.EXTRA_DATA";
    public final static String EXTRA_DATA_UUID = "com.ckjs.ck.EXTRA_DATA_UUID";
    public final static String ACTION_LOGIN = "com.ckjs.ck.ACTION_LOGIN";
    public final static String ACTION_LOGIN_OUT = "com.ckjs.ck.ACTION_LOGIN_OUT";
    public final static String ACTION_XIN_LV = "com.ckjs.ck.ACTION_XIN_LV";
    public final static String ACTION_START_NOT = "com.ckjs.ck.ACTION_START_NOT";
    public final static String ACTION_HSH_STEP_0 = "com.ckjs.ck.ACTION_HSH_STEP_0";
    public final static String ACTION_PHONE = "com.ckjs.ck.ACTION_PHONE";
    public final static String ACTION_STEP_TB = "com.ckjs.ck.ACTION_STEP_TB";
    public final static String EXTRA_VALUE = "com.ckjs.ck.EXTRA_VALUE";

    public static int EXTRA_DATA_STATE = -1;//0,1,2,3,4,5首页，健康监测，睡眠测量，心率测量，血氧测量，血压测量

    public static boolean mastershake = false;


    public final static String FFF4 = "00002902-0000-1000-8000-00805f9b34fb";
    public final static String FFF5 = "00002902-0000-1000-8000-00805f9b34fb";

    public static int n;


//    public static final String TAG = "-----TAG----";

    /**
     * // 第一天 第2时 第3分 睡觉
     * // 第4时 第5分 醒来 总条数:1a = 26
     * // 0 : ff 1: f3 2: fd 3:fc 4: f0 5:e6 6:dc 7:d2 8:c8 9:be 10:b4
     */
    public static byte aByte1[] = {0x02, 0x01, 0x01, 0x02, 0x03, 0x1a, (byte) 0xff, (byte) 0xf3, (byte) 0xfd, 0x02};
    public static byte aByte2[] = {0x02, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x05};

    public static AlertDialog wxAlertDialog;
    public static int loginStep;
    public static boolean flag;
    public static int dl;
    public static String nameStep = "start";

}
