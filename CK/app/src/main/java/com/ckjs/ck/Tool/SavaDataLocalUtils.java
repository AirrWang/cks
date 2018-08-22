package com.ckjs.ck.Tool;

import android.content.Context;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class SavaDataLocalUtils {

    public static void saveDataString(Context context, String name, String s) {
        if (s != null && !s.equals("")) {
            SPUtils.put(context, name, s);
        }
    }

    public static void saveDataInt(Context context, String name, int s) {
        if (s != 0 && s > 0) {
            SPUtils.put(context, name, s);
        }
    }

    public static void saveDataFlot(Context context, String name, float s) {
        if (s != 0 && s > 0) {
            SPUtils.put(context, name, s);
        }
    }
}
