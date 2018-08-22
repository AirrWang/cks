package com.ckjs.ck.Tool;

import android.util.Log;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class HSHTool {
    /**
     * 输入校验密码命令
     * 0xF101E240
     */
    public synchronized static void start0xF1() {
        if (AppConfig.bluetoothGattServices == null)
            return;
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0xF1, 0x01, (byte) 0xE2, 0x40});
    }

    /**
     * 久坐数据查询
     */
    public synchronized static void sedentaryChek() {
        if (AppConfig.bluetoothGattServices == null)
            return;
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x07, 0x01, (byte) 0xE2, 0x40});
    }

    /**
     * 读取久坐
     */
    public synchronized static void sedentaryRead(int i) {
        if (AppConfig.bluetoothGattServices == null)
            return;
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x07, 0x02, (byte) i, 0x00});
    }

    /**
     * 睡眠数据查询
     */
    public synchronized static void sleepChek() {
        if (AppConfig.bluetoothGattServices == null)
            return;
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x06, 0x01, (byte) 0x00, 0x00});
//        Log.i(AppConfig.TAG, "sleepChekfff1");
    }

    /**
     * 睡眠数据读取
     */
    public synchronized static void sleepRead(int i) {
        if (AppConfig.bluetoothGattServices == null)
            return;
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x06, 0x02, (byte) i, 0x00});
    }


    /**
     * 步数数据读取
     *
     * @param month
     * @param data
     */
    public synchronized static void stepRead(int month, int data) {
        if (AppConfig.bluetoothGattServices == null)
            return;
//        Log.i(AppConfig.TAG, "stepRead1:" + month + "," + data);
        String s = MoudleUtils.checkData(month) + MoudleUtils.checkData(data);
//        Log.i(AppConfig.TAG, "stepRead2:" + s);
        final byte[] bytes = DataUtils.hexStrToByteArray(s, 2);
//        Log.i(AppConfig.TAG, "stepRead3:" + (byte) bytes[0] + "," + (byte) bytes[1]);
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x04, 0x05, (byte) bytes[0], (byte) bytes[1]});
    }

    /**
     * 健康数据读取
     *
     * @param data
     * @param hour
     */
    public synchronized static void healthRead(int data, int hour) {
        if (AppConfig.bluetoothGattServices == null)
            return;
//        Log.i(AppConfig.TAG, "healthRead1:" + data + "," + hour);

        String s = MoudleUtils.checkData(data) + MoudleUtils.checkData(hour);
//        Log.i(AppConfig.TAG, "healthRead2:" + s);
        final byte[] bytes = DataUtils.hexStrToByteArray(s, 2);
//        Log.i(AppConfig.TAG, "healthRead3:" + (byte) bytes[0] + "," + (byte) bytes[1]);
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x05, 0x06, (byte) bytes[0], (byte) bytes[1]});
    }


    /**
     * 运动健康数据读取
     *
     * @param hour
     * @param min
     */
    public synchronized static void runHealthRead(int hour, int min) {
        if (AppConfig.bluetoothGattServices == null)
            return;
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{(byte) 0x0B, 0x04, (byte) hour, (byte) min});
    }

    /**
     * f1数据读取
     */
    public synchronized static void f1Read() {
        if (AppConfig.bluetoothGattServices == null)
            return;
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1");
//        Log.i(AppConfig.TAG, "f1Readed");
    }

    /**
     * f5数据读取
     */
    public synchronized static void f5Read() {
        if (AppConfig.bluetoothGattServices == null)
            return;
        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff5");
    }

    static String string = "";

    public synchronized static void writeNFC(int n, int m, char i1, char i2, char i3, char i4) {
        string = string + i1 + i2 + i3 + i4;
//        Log.i(AppConfig.TAG, "writeNFCi1 + i2 + i3 + i4:" + i1 + i2 + i3 + i4);
//        Log.i(AppConfig.TAG, "writeNFC0:" + string);
        String s = MoudleUtils.checkData(n) + MoudleUtils.checkData(m);
//        Log.i(AppConfig.TAG, "writeNFC1:" + s);
        byte[] bytes = DataUtils.hexStrToByteArray(s, 2);
//        Log.i(AppConfig.TAG, "writeNFC2:" + (byte) bytes[0] + "," + (byte) bytes[1]);
//        Log.i(AppConfig.TAG, "writeNFC3:" + (byte) i1 + (byte) i2 + (byte) i3 + (byte) i4);

        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff6", new byte[]{(byte) 0x02, (byte) bytes[0], (byte) bytes[1], (byte) i1, (byte) i2, (byte) i3, (byte) i4});

    }
}
