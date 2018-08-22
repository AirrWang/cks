package com.ckjs.ck.Tool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.HomeShopH5Activity;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.R;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class DataUtils {
    //byte数组转10进制
    //16-10:Integer.parseInt("0xAA",16)
    public static int[] byteTo10(byte[] bytes) {
        int[] i_hex = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            i_hex[i] = Integer.parseInt(hex, 16);
            //            Log.d("tag", "--------write success----- hex.toUpperCase():" + i_hex[i]);
        }
        return i_hex;
    }

    //byte数组转16进制String
    public static String[] byteTo10S(byte[] bytes) {
        String i_hex[] = new String[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            i_hex[i] = hex;
            //            Log.d("tag", "--------write success----- hexS.toUpperCase():" + i_hex[i]);
        }
        return i_hex;
    }

    //byte数组转String
    public static String byteToS(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = null;
        try {
            str = new String(byteArray, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 16进制String 转 byte
     *
     * @param str
     * @return
     */
    public static byte[] hexStrToByteArray(String str, int n) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / n];
        for (int i = 0; i < byteArray.length; i++) {
            String subStr = str.substring(n * i, n * i + n);
            byteArray[i] = ((byte) (Integer.parseInt(subStr, 16)));
        }
        return byteArray;
    }

    /**
     * String 转 10用于xyz数值转化
     *
     * @param str
     * @return
     */
    public static int[] hexStrTo10(String str, int n) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new int[0];
        }
        int[] is = new int[str.length() / n];
        for (int i = 0; i < is.length; i++) {
            String subStr = str.substring(n * i, n * i + n);
            is[i] = Integer.valueOf(subStr, 16);
            String s2 = Integer.toBinaryString(is[i]);
            if (s2 != null && s2.length() > 0) {
                String s2s[] = new String[s2.length()];
                for (int j = 0; j < s2.length(); j++) {
                    s2s[j] = s2.substring(1 * j, 1 * j + 1);
                }
                if (s2s[0].equals("1")) {
                    String s = "";
                    for (int j = 0; j < s2s.length; j++) {
                        if (s2s[j].equals("1")) {
                            s2s[j] = "0";
                        } else {
                            s2s[j] = "1";
                        }
                        s = s + s2s[j];
                    }
                    s2 = s;
                    is[i] = Integer.parseInt(Integer.valueOf(s2, 2).toString()) + 1;
                }
            }
        }
        return is;
    }

    /**
     * 开启健康数据上传后测量设备自动打开
     */
    public static void startHearDeng()

    {
        //        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x05, 0x03, 0x00, 0x00});

        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x05, 0x01, 0x00, 0x00});
    }

    /**
     * 关闭健康数据上传和心率测量设备
     */
    public static void stopHearDeng()

    {
        //        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x05, 0x04, 0x00, 0x00});

        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x05, 0x02, 0x00, 0x00});
    }

    /**
     * x,y,z值转化
     */
    public static int[] displayDataFFF4Xyz(byte[] data) {
        int[] arr = new int[6];
        int[] arr1 = new int[3];
        for (int x = 0; x < 6; x++) {
            arr[x] = data[x + 1];
            arr[x] &= 0xff;
        }
        arr1[0] = (arr[0] << 8) + arr[1];
        if (arr1[0] > 32767) {
            arr1[0] = (65536 - arr1[0]) * -1;
        }
        arr1[1] = (arr[2] << 8) + arr[3];
        if (arr1[1] > 32767) {
            arr1[1] = (65536 - arr1[1]) * -1;
        }
        arr1[2] = (arr[4] << 8) + arr[5];
        if (arr1[2] > 32767) {
            arr1[2] = (65536 - arr1[2]) * -1;
        }

        return arr1;
    }


    /**
     * 基本代谢算法
     */
    public static int setTvBMR(Context context) {
        int age;
        age = (int) SPUtils.get(context, "age", 0);
        int sex;
        sex = (int) SPUtils.get(context, "sex", 0);
        float weight = 0;
        int height = 0;
        weight = (float) SPUtils.get(context, "weight", weight);
        height = (int) SPUtils.get(context, "height", 0);
        int bmr = 0;
        if (sex == 2) {
            bmr = (int) (661 + 9.6 * weight + 1.72 * height - 4.7 * age);
        } else {
            bmr = (int) (67 + 13.73 * weight + 5 * height - 6.9 * age);
        }
        return bmr;
    }

    /**
     * 体重BMI值算法
     */
    public static float setTvBMI(Context context, TextView tvWeight, TextView tv_biao_zhun_weight) {
        float weight = 0;
        int height = 0;
        weight = (float) SPUtils.get(context, "weight", weight);
        height = (int) SPUtils.get(context, "height", 0);
        MoudleUtils.textViewSetText(tvWeight, weight + "");
        //成年人标准 体重（千克）＝[身高（厘米）－100]×0.9
        if (height > 100) {
            MoudleUtils.textViewSetText(tv_biao_zhun_weight, "最佳体重:" + ((height - 100) * 0.9f) + "KG");
        } else {
            MoudleUtils.textViewSetText(tv_biao_zhun_weight, "最佳体重:" + 21 + "KG");
        }
        float BMI;
        float h2 = height * height / 10000.0f;
        if (h2 != 0) {
            BMI = weight / h2;
            BMI = (float) Math.round(BMI * 10) / 10;
        } else {
            BMI = 0;
        }
        return BMI;

    }

    /**
     * @param BMI 界面标记
     * @return
     */
    public static LinearLayout.LayoutParams initBmiPic(float BMI) {
        float setLeft;
        float width = ScreenUtils.getScreenWidth();
        float bmiWidth = width - CkApplication.getInstance().getResources().getDimension(R.dimen.jtou_yi_ban_w) * 2;
        setLeft = (float) (bmiWidth / 25 * (BMI - 15));

        if (setLeft < 0) {
            setLeft = 0;
        }
        if (setLeft > width - CkApplication.getInstance().getResources().getDimension(R.dimen.d22)) {
            setLeft = width - CkApplication.getInstance().getResources().getDimension(R.dimen.d22);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins((int) setLeft, 0, 0, 0);

        return lp;
    }

    public static int s16ToInt(int s16) {
        String hex = Integer.toHexString(s16 & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return Integer.parseInt(hex, 16);
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 保存图片到本地
     */
    public static boolean initTosee(Bitmap bitmap) {
        try {
            String filePath = AppConfig.hss_save
                    + "/"
                    + System.currentTimeMillis()
                    + AppConfig.JPG;
            File file = new File(filePath);
            if (MoudleUtils.initImageFile(file, AppConfig.hss_save)) {
                FileOutputStream fos = new FileOutputStream(file);
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                if (file != null) {
                    if (file.exists()) {
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(file);
                        intent.setData(uri);
                        CkApplication.getInstance().sendBroadcast(intent);
//                        Log.i(AppConfig.TAG, "保存成功！路径：" + file.getAbsolutePath());
                        return true;
                    } else {
//                        Log.i(AppConfig.TAG, "保存图片失败啦,无法下载图片");
                        return false;
                    }
                } else {
//                    Log.i(AppConfig.TAG, "保存图片失败啦,无法下载图片");
                    return false;
                }
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 进行图片高斯模糊
     *
     * @param uri
     * @param sd_touxiang_friends
     * @param context
     */
    public static void downLoadImage(Uri uri, final SimpleDraweeView sd_touxiang_friends, final Context context) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(Bitmap bitmap) {
                if (bitmap == null) {

                    return;
                }

                sd_touxiang_friends.setImageBitmap(MoudleUtils.blurBitmap(bitmap, context, 16.2f));
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {

            }
        }, CallerThreadExecutor.getInstance());
    }

    /**
     * Get Mobile Type
     *
     * @return
     */
    private static String getMobileType(String name) {
        //        return Build.MANUFACTURER;
        return name;
    }

    /**
     * GoTo Open Self Setting Layout
     * Compatible Mainstream Models 兼容市面主流机型
     *
     * @param context
     */
    public static void jumpStartInterface(String mtype, Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            LogUtils.e( "******************当前手机型号为：" + getMobileType(mtype));
            ComponentName componentName = null;
            if (mtype.startsWith("Redmi") || mtype.startsWith("MI") || mtype.startsWith("Xiaomi")) {
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (mtype.startsWith("HUAWEI")) {
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
            }
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

    /**
     * GoTo Open Self Setting Layout
     * Compatible Mainstream Models 兼容市面主流机型
     *
     * @param context
     */
    public static void jumpStartInterfaceHome(String mtype, Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            LogUtils.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType(mtype));
            ComponentName componentName = null;
            if (mtype.startsWith("Redmi") || mtype.startsWith("MI") || mtype.startsWith("Xiaomi")) {
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
                intent.setComponent(componentName);
                context.startActivity(intent);
            } else if (mtype.startsWith("HUAWEI")) {
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
                intent.setComponent(componentName);
                context.startActivity(intent);
            } else {
                Intent intent1 = new Intent();
                intent1.putExtra("acurl", AppConfig.h5_ydqx);
                intent1.setClass(context, HomeShopH5Activity.class);
                context.startActivity(intent1);
            }

        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

}


