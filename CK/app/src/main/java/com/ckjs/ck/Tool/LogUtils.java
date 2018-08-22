package com.ckjs.ck.Tool;

import android.util.Log;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class LogUtils {


    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    public static boolean isDebug = false;//上线时改为false,开发时改为true
    private static final String TAG = "CKHSH";


    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }


    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }


    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }


    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }


    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }


    public static void d(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }


    public static void e(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }


    public static void v(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }
}
