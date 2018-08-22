package com.ckjs.ck.Tool;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.ckjs.ck.Android.MainActivity;
import com.ckjs.ck.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class UpdateAppManager {
    // 文件分隔符
    private static final String FILE_SEPARATOR = "/";
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + FILE_SEPARATOR + "autoupdate" + FILE_SEPARATOR;
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "autoupdate.apk";
    // 更新应用版本标记
    private static final int UPDARE_TOKEN = 0x29;
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 0x31;

    private Context context;
    private String message = "检测到本程序有新版本发布，建议您更新！";
    // 目前以微信为例
    private String spec = "";
    // 下载应用的对话框
    private Dialog dialog;
    // 下载应用的进度条
    private ProgressBar progressBar;
    // 进度条的当前刻度值
    private int curProgress;
    // 用户是否取消下载
    private boolean isCancel;
    private String name;

    public UpdateAppManager(Context context, String name) {
        this.context = context;
        this.name = name;
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDARE_TOKEN:
                    progressBar.setProgress(curProgress);
                    break;

                case INSTALL_TOKEN:
                    installApp();
                    break;
            }
        }
    };

    /**
     * 检测应用更新信息
     *
     * @param downurl
     * @param isupdate
     * @param verdec
     */
    private String isupdate;

    public void checkUpdateInfo(String downurl, String isupdate, String verdec) {
        this.isupdate = isupdate;
        spec = downurl;
        if (!isupdate.equals("") && isupdate != null) {
            if (name.equals("main")) {
                if (isupdate.equals("0")) {
                    showNoticeDialog(verdec);
                } else {
                    showMustDialog(verdec);
                }
            } else {
                showNoticeDialog(verdec);
            }

        }

    }

    /**
     * 显示提示更新对话框 不强制
     *
     * @param verdec
     */
    AlertDialog alertDialog;

    private void showNoticeDialog(String verdec) {
        if (!verdec.equals("") && verdec != null) {
            message = verdec;
        }
        alertDialog = new AlertDialog.Builder(context)
                .setTitle("软件版本更新")
                .setMessage(message)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.put(context, "zq", "0");
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                }).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        initShowAlert();
    }

    /**
     * 强制更新
     *
     * @param verdec
     */

    private void showMustDialog(String verdec) {
        if (!verdec.equals("") && verdec != null) {
            message = verdec;
        }
        alertDialog = new AlertDialog.Builder(context)
                .setTitle("软件版本更新")
                .setMessage(message)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.put(context, "zq", "0");
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (name.equals("main")) {
                            ((MainActivity) context).finish();
                            System.exit(0);
                        }


                    }
                }).create();
        initShowAlert();
    }

    private void initShowAlert() {
        if (alertDialog != null) {
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing()) {
                alertDialog.show(); // 显示
            }
        }
    }

    /**
     * 显示下载进度对话框
     */
    private void showDownloadDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.progressbar, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("软件版本更新");
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (name.equals("main")) {
                    if (isupdate.equals("0")) {
                        dialog.dismiss();
                        isCancel = true;
                    } else {
                        ((MainActivity) context).finish();
                        System.exit(0);
                    }
                } else {
                    dialog.dismiss();
                    isCancel = true;
                }

            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        downloadApp();
    }

    /**
     * 下载新版本应用
     */
    private void downloadApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                InputStream in = null;
                FileOutputStream out = null;
                HttpURLConnection conn = null;
                try {
                    url = new URL(spec);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    long fileLength = conn.getContentLength();
                    in = conn.getInputStream();
                    File filePath = new File(FILE_PATH);
                    if (!filePath.exists()) {
                        filePath.mkdir();
                    }
                    out = new FileOutputStream(new File(FILE_NAME));
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    long readedLength = 0l;
                    while ((len = in.read(buffer)) != -1) {
                        // 用户点击“取消”按钮，下载中断
                        if (isCancel) {
                            break;
                        }
                        out.write(buffer, 0, len);
                        readedLength += len;
                        curProgress = (int) (((float) readedLength / fileLength) * 100);
                        handler.sendEmptyMessage(UPDARE_TOKEN);
                        if (readedLength >= fileLength) {
                            dialog.dismiss();
                            // 下载完毕，通知安装

                            handler.sendEmptyMessage(INSTALL_TOKEN);
                            break;
                        }
                    }
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }


    /**
     * 安装新版本应用
     */
    private void installApp() {
        try {
            File appFile = new File(FILE_NAME);
            if (!appFile.exists()) {
                ToastUtils.showShortNotInternet("网络开小差，安装失败");
                return;
            }
            // 跳转到新版本应用安装页面
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShortNotInternet("网络开小差，安装失败");
        }

    }

}
