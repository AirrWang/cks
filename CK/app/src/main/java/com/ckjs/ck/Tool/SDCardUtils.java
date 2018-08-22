package com.ckjs.ck.Tool;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 * 文件存储工具类
 */
public class SDCardUtils {
    private SDCardUtils() {
/** cannot be instantiated **/
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }


    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
// 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
// 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }


    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
// 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }


    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    public static String saveFronSDCard(String fileName, byte[] data) {
        String sdc_file = Environment.getExternalStorageDirectory().toString() + "/CK/Customer/";

        File dir = new File(sdc_file);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String state = Environment.getExternalStorageState().toString();
        FileOutputStream fileOutputStream = null;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File rel_file = new File(sdc_file, fileName);

                System.out.println("==================" + sdc_file);
                System.out.println("==================写数据");
                fileOutputStream = new FileOutputStream(rel_file);
                fileOutputStream.write(data, 0, data.length);
                System.out.println("=========================SDCard保存成功" + fileName);
                System.out.println("=======================路径" + rel_file.getAbsolutePath());
                return rel_file.getAbsolutePath();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("=======================路径" + e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }
        return null;

    }

}
