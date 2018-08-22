package com.ckjs.ck.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.SPUtils;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 * 短信广播
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //短信提醒
        if ((Boolean) SPUtils.get(context,"smsShake",false)){
            BluethUtils.displayGattServices(AppConfig.bluetoothGattServices,"fff3",new byte[]{0x02});
        }
    }
}
