package com.ckjs.ck.Receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.SPUtils;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 * 来电广播
 */

public class MonitorBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //来电通知打开
        if ((Boolean) SPUtils.get(context,"phoneShake",false)) {
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

            } else {
                // 如果是来电
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Service.TELEPHONY_SERVICE);

                switch (tm.getCallState()) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff3", new byte[]{0x01});
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
//                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff3", new byte[]{(byte) 0xFE});
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
//                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff3", new byte[]{(byte) 0xFE});
                        break;
                }
            }
        }
    }
}
