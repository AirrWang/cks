package com.ckjs.ck.Tool;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;

import java.util.List;
import java.util.UUID;

import static com.ckjs.ck.Tool.AppConfig.mBluetoothGatt;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class BluethUtils {
    public static BluetoothAdapter bluetoothAdapter;

    public synchronized static void initGetBluetooth(Context context) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            ToastUtils.showShortNotInternet("对不起，您的机器不具备蓝牙功能");
        } else if (bluetoothAdapter.isEnabled()) {
            //            ToastUtils.showShortNotInternet("蓝牙已开启,开始绑定手环吧");
        } else {
            context.startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
    }

    public synchronized static void initGetBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            ToastUtils.showShortNotInternet("对不起，您的机器不具备蓝牙功能");
        } else if (bluetoothAdapter.isEnabled()) {
            //            ToastUtils.showShortNotInternet("蓝牙已开启,开始绑定手环吧");
        } else {
            //            context.startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
    }


    /**
     * 手环写入命令
     *
     * @param gattServices
     * @param order
     * @param bytes
     */
    public synchronized static void displayGattServices(List<BluetoothGattService> gattServices, String order, byte[] bytes) {
        if (gattServices == null)
            return;
        String uuid = "";
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.contains(order)) {
                    setCharacteristicNotification(bytes, gattCharacteristic, false);
                }
            }
        }
    }

    /**
     * 手环写入命令
     *
     * @param bytes
     */
    public synchronized static void sendCMD(BluetoothGattCharacteristic characteristic, byte[] bytes) {
        if (characteristic != null && mBluetoothGatt != null) {
            mBluetoothGatt.setCharacteristicNotification(characteristic, true);
            //将指令放置进来
            characteristic.setValue(bytes);
            //设置回复形式
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            //开始写数据
            mBluetoothGatt.writeCharacteristic(characteristic);
        }

    }

    /**
     * 手环读取命令
     *
     * @param gattServices
     * @param str
     */
    public synchronized static void displayGattServices(List<BluetoothGattService> gattServices, String str) {

        if (gattServices == null)
            return;
        String uuid = null;
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.contains(str)) {
                    int charaProp = gattCharacteristic.getProperties();
                    if ((charaProp | gattCharacteristic.PROPERTY_READ) > 0) {//可读
                        setCharacteristicNotification(gattCharacteristic, true, true);
                    }
                }
            }
        }
    }

    /**
     * 接受fff4数据实时广播
     *
     * @param gattServices
     * @param str
     * @param notify
     */
    public synchronized static void displayGattServices(List<BluetoothGattService> gattServices, String str, String notify) {

        if (gattServices == null)
            return;
        String uuid = null;
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.contains(str)) {
                    int charaProp = gattCharacteristic.getProperties();
                    if ((charaProp | gattCharacteristic.PROPERTY_READ) > 0) {
                        setCharacteristicNotification(notify, gattCharacteristic, true);
                    }
                }
            }
        }
    }

    /**
     * 接受fff4/fff5数据实时广播
     *
     * @param notify
     * @param gattCharacteristic
     * @param enabled
     */
    private synchronized static void setCharacteristicNotification(String notify, BluetoothGattCharacteristic gattCharacteristic, boolean enabled) {
        if (bluetoothAdapter == null || mBluetoothGatt == null || gattCharacteristic == null) {
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, enabled);
        BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(
                UUID.fromString(notify));
        if (descriptor == null) {
            return;
        }
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
        //        Log.i(AppConfig.TAG, "no" + notify);
    }

    /**
     * 手环命令进程控制
     */
    public static class ThreadFFF implements Runnable {
        private BluetoothGattCharacteristic characteristic;
        private boolean enabled;
        private boolean flag;
        private byte[] bytes;

        public ThreadFFF(BluetoothGattCharacteristic characteristic, boolean enabled, boolean flag) {
            this.enabled = enabled;
            this.characteristic = characteristic;
            this.flag = flag;
        }

        public ThreadFFF(byte[] bytes, BluetoothGattCharacteristic characteristic, boolean flag) {
            this.flag = flag;
            this.bytes = bytes;
            this.characteristic = characteristic;
        }

        public void run() {
            synchronized (ThreadFFF.class) {
                if (flag) {
                    if (mBluetoothGatt == null || characteristic == null)
                        return;
                    mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
                    if (mBluetoothGatt.readCharacteristic(characteristic)) {
                        //                        Log.d(TAG, characteristic.getUuid().toString() + "成功");
                    } else {
                        //                        Log.d(TAG, characteristic.getUuid().toString() + "失败");
                    }
                } else {
                    sendCMD(characteristic, bytes);
                }
            }
        }
    }

    /**
     * flag:true,read
     * flag:false,write
     * <p>
     * 手环读取命令
     *
     * @param characteristic
     * @param enabled
     * @param flag
     */
    public synchronized static void setCharacteristicNotification(
            final BluetoothGattCharacteristic characteristic, final boolean enabled, boolean flag) {
        if (bluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        ThreadFFF fff = new ThreadFFF(characteristic, enabled, true);
        Thread ta = new Thread(fff, (AppConfig.n++) + "");
        ta.start();

    }

    /**
     * flag:true,read
     * flag:false,write
     * <p>
     * 手环写入命令
     *
     * @param bytes
     * @param
     * @param flag
     */
    public synchronized static void setCharacteristicNotification(
            final byte[] bytes, BluetoothGattCharacteristic characteristic, boolean flag) {
        if (bluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        ThreadFFF fff = new ThreadFFF(bytes, characteristic, false);
        Thread tc = new Thread(fff, (AppConfig.n++) + "");
        //        Log.d(AppConfig.TAG, "" + characteristic.getUuid().toString() + "," + AppConfig.n);

        tc.start();

    }
}