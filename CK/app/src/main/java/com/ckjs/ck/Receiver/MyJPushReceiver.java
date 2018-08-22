package com.ckjs.ck.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ckjs.ck.Android.CkCircleModule.Activity.CirPaiHangTodayActivity;
import com.ckjs.ck.Android.HomeModule.Activity.AcH5Activity;
import com.ckjs.ck.Android.HomeModule.Activity.BaoMingActivity;
import com.ckjs.ck.Android.HomeModule.Activity.HomeJsfMoreActivity;
import com.ckjs.ck.Android.HomeModule.Activity.TjH5HomeActivity;
import com.ckjs.ck.Android.MainActivity;
import com.ckjs.ck.Android.MainCoachActivity;
import com.ckjs.ck.Android.MineModule.Activity.HSFTrueActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineOrderNewActivity;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Manager.NotifyActivityAddFamilyManager;
import com.ckjs.ck.Manager.NotifyActivityAddJsfManager;
import com.ckjs.ck.Manager.NotifyInfoUpdateMessageManager;
import com.ckjs.ck.Manager.NotifyJpushClientEndManager;
import com.ckjs.ck.Manager.NotifyJpushClientStartManager;
import com.ckjs.ck.Manager.NotifyJpushCoachEndManager;
import com.ckjs.ck.Manager.NotifyJpushCoachStartManager;
import com.ckjs.ck.Manager.NotifyJpushCommenManager;
import com.ckjs.ck.Manager.NotifyJpushLetterManager;
import com.ckjs.ck.Manager.NotifyJpushManager;
import com.ckjs.ck.Manager.NotifyJpushPaymentManager;
import com.ckjs.ck.Manager.NotifyJpushReceiveManager;
import com.ckjs.ck.Manager.NotifyJpushSubmitManager;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.SPUtils;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MyJPushReceiver extends BroadcastReceiver {
    private static String TAG = "pushreceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            System.out.println("收到了extra自定义消息@@消息内容是:" + extra);
            if (CkApplication.getInstance() == null) {
                Intent launchIntent = context.getPackageManager().
                        getLaunchIntentForPackage("com.ckjs.ck");
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(launchIntent);
            } else {
                if (!(SPUtils.get("token", "") + "").equals("")) {
                    Intent launchIntent = new Intent();
                    launchIntent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    launchIntent.setClass(context, MainActivity.class);
                    context.startActivity(launchIntent);
                    try {
                        JSONObject jsonObject;
                        jsonObject = new JSONObject(extra);
                        String type = jsonObject.getString("type");
                        if (type.equals("read")) {
                            //value：阅读id；url：跳转网址；title：标题；intro：简介；picture；图片地址
                            /**
                             * 跳转到阅读
                             */
                            String value = jsonObject.getString("value");
                            String url = jsonObject.getString("url");
                            String intro = jsonObject.getString("intro");
                            String picture = jsonObject.getString("picture");
                            Intent intent1 = new Intent(context, TjH5HomeActivity.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent1.putExtra("acurl", url);
                            intent1.putExtra("read_id", value);
                            intent1.putExtra("subhead", intro);
                            intent1.putExtra("imageurl", AppConfig.url_jszd + picture);
                            context.startActivity(intent1);
                            return;
                        } else if (type.equals("topinfo")) {
                            //排行榜
                            Intent detailIntent = new Intent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setClass(context, CirPaiHangTodayActivity.class);
                            Intent[] intents = {launchIntent, detailIntent};
                            context.startActivities(intents);
                            return;
                        } else if (type.equals("activity")) {
                            //活动
                            String tp = jsonObject.getString("tp");
                            if (tp.equals("1")) {
                                int value = jsonObject.getInt("value");
                                Intent intentB = new Intent();
                                intentB.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentB.setClass(context, BaoMingActivity.class);
                                intentB.putExtra("activity_id", value);
                                intentB.putExtra("name", "活动详情");
                                context.startActivity(intentB);
                                return;
                            } else if (tp.equals("2")) {
                                String url = jsonObject.getString("url");
                                Intent intentH5 = new Intent();
                                intentH5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentH5.putExtra("acurl", url);
                                intentH5.setClass(context, AcH5Activity.class);
                                context.startActivity(intentH5);
                                return;
                            }

                        } else if (type.equals("gym")) {
                            //超空门店
                            String value = jsonObject.getString("value");
                            Intent intent2 = new Intent();
                            intent2.putExtra("gym_id", value);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent2.putExtra("name", "超空门店");
                            intent2.setClass(context, HomeJsfMoreActivity.class);
                            context.startActivity(intent2);
                            return;
                        } else if (type.equals("shoporder")) {
                            //商品订单
                            Intent detailIntent = new Intent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setClass(context, MineOrderNewActivity.class);//我的订单
                            context.startActivity(detailIntent);
                            return;
                        } else if (type.equals("coachorder")) {
                            //客户私教订单
                            Intent detailIntent = new Intent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setClass(context, MineOrderNewActivity.class);//我的订单
                            context.startActivity(detailIntent);
                            return;
                        } else if (type.equals("pricoach")) {
                            //教练私教订单
                            Intent detailIntent = new Intent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setClass(context, MainCoachActivity.class);//教练私教订单
                            context.startActivity(detailIntent);
                            return;
                        } else if (type.equals("familyapply")) {
                            String value = jsonObject.getString("value");
                            //超空家申请
                            Intent detailIntent = new Intent()
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra("bind_id", value)
                                    .putExtra("name", "超空家邀请")
                                    .setClass(context, HSFTrueActivity.class);//教练私教订单
                            context.startActivity(detailIntent);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Intent launchIntent = context.getPackageManager().
                            getLaunchIntentForPackage("com.ckjs.ck");
                    launchIntent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    context.startActivity(launchIntent);
                }
            }

        } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {


        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

            System.out.println("收到了自定义消息@@消息内容是:" + content);
            System.out.println("收到了自定义消息@@消息title是:" + title);
            System.out.println("收到了自定义消息@@消息extra是:" + extra);
            //**************解析推送过来的json数据并存放到集合中 begin******************
            JSONObject jsonObject;
            switch (content) {
                case "letter":
                    try {
                        jsonObject = new JSONObject(extra);
                        String letter = jsonObject.getString("letter");
                        if (!(SPUtils.get("token", "") + "").equals("")) {
                            NotifyJpushLetterManager.getInstance().sendNotifyJpushLetterFlag(true, letter);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "bodyinfo":
                    try {
                        jsonObject = new JSONObject(extra);
                        String bodyanalyse = jsonObject.getString("bodyanalyse");
                        String bodyinfo = jsonObject.getString("bodyinfo");

                        if (!(SPUtils.get("token", "") + "").equals("")) {
                            NotifyJpushManager.getInstance().sendNotifyJpushFlag(true, bodyanalyse, bodyinfo);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "comment":
                    try {
                        jsonObject = new JSONObject(extra);
                        String comment = jsonObject.getString("comment");

                        if (!(SPUtils.get("token", "") + "").equals("")) {
                            NotifyJpushCommenManager.getInstance().sendNotifyJpushCommentFlag(true, comment);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "submit"://客户提交
                    try {
                        if (!(SPUtils.get("token", "") + "").equals("")) {
                            NotifyJpushSubmitManager.getInstance().sendNotifyFlag(true);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "receive"://教练接单
                    try {

                        if (!(SPUtils.get("token", "") + "").equals("")) {
                            NotifyJpushReceiveManager.getInstance().sendNotifyFlag(true);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "payment"://用户支付
                    try {
                        if (!(SPUtils.get("token", "") + "").equals("")) {
                            NotifyJpushPaymentManager.getInstance().sendNotifyFlag(true);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "client_start":
                    try {
                        if (!(SPUtils.get("token", "") + "").equals("")) {
                            NotifyJpushClientStartManager.getInstance().sendNotifyFlag(true);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "client_end":
                    try {

                        if (!(SPUtils.get("token", "") + "").equals("")) {
                            NotifyJpushClientEndManager.getInstance().sendNotifyFlag(true);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "coach_start":
                    try {
                        if (!(SPUtils.get("token", "") + "").equals("")) {
                            NotifyJpushCoachStartManager.getInstance().sendNotifyFlag(true);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "coach_end":
                    try {
                        if (!(SPUtils.get("token", "") + "").equals("")) {
                            NotifyJpushCoachEndManager.getInstance().sendNotifyJpushCoachEndFlag(true);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "set_target":
                    if ((boolean) SPUtils.get(CkApplication.getInstance(), "goalset", false)) {
                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff3", new byte[]{0x04});
                    }
                    break;
                case "gym_bind":
                    NotifyActivityAddJsfManager.getInstance().sendNotifyFlag(true);
                    break;
                case "family":
                    NotifyActivityAddFamilyManager.getInstance().sendNotifyFlag(true);
                    break;
                case "hshback":
                    NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                    break;
            }

            //**************解析推送过来的json数据并存放到集合中 end******************

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}

