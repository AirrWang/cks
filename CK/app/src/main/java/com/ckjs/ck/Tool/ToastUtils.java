package com.ckjs.ck.Tool;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.ckjs.ck.Application.CkApplication;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ToastUtils {


    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    public static boolean isShow = true;

    /**
     * 网络请求得到的提示必须用这个
     * @param context
     * @param message
     */
    public static void showShort(Context context, String message) {
        try {
            if (isShow) {
                Toast toast = null;
                if (message.contains("token错误")) {
                    MoudleUtils.initStatusTwo(context, true);
                } else {
                    toast = Toast.makeText(CkApplication.getInstance(), message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 一些于界面无关的提示可以用这个，慎重使用
     * @param message
     */
    public static void showShortNotInternet(String message) {
        try {
            if (isShow) {
                Toast toast = null;
                toast = Toast.makeText(CkApplication.getInstance(), message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void show(Context context, String message, int duration) {
        try {
            if (isShow) {
                Toast toast = null;
                if (message.contains("token错误")) {
                    MoudleUtils.initStatusTwo(context, true);
                } else {
                    toast = Toast.makeText(CkApplication.getInstance(), message, duration);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
