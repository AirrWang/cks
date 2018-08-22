package com.ckjs.ck.Tool;

import android.os.Handler;
import android.text.format.Time;

import com.ckjs.ck.Android.HomeModule.Activity.DeviceControlActivity;
import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.LastbodyinfoBean;
import com.ckjs.ck.Bean.LaststepsBean;
import com.ckjs.ck.Bean.PostHealthBean;
import com.ckjs.ck.Bean.SleepBean;
import com.ckjs.ck.Bean.StepPostBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class SynchronizeDataTool {

    /**
     * 数据同步
     */
    public static void initTbStartPostdata() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //用来进行数据同步开启广播
                BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff5", AppConfig.FFF5);
                /**
                 * 数据同步，睡眠数据
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //                        Log.i(AppConfig.TAG, "sleepChek");
                        HSHTool.sleepChek();
                    }
                }, 1000);
            }
        }, 1000);
    }

    /**
     * 数据同步
     */
    public static void initTbStartPostdataStep(int num) {
        MoudleUtils.broadcastUpdate(CkApplication.getInstance(), AppConfig.ACTION_STEP_TB, num);
    }

    public static int daypoor = -1;
    public static int beforDataTime;
    public static int nowDay;
    public static int nowHour;
    public static String nowDate = "";

    public static void initGetHttpTongBuDataStep() {
        String token = (String) SPUtils.get("token", "");
        int user_id = (int) (SPUtils.get("user_id", 0));

        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<LaststepsBean> callBack = restApi.laststeps(token, user_id);


        callBack.enqueue(new Callback<LaststepsBean>() {
            @Override
            public void onResponse(Call<LaststepsBean> call, Response<LaststepsBean> response) {
                LaststepsBean lastbodyinfoBean = response.body();
                if (lastbodyinfoBean == null) {
                    initGetHttpTongBuData();
                    return;
                }
                String status = lastbodyinfoBean.getStatus();
                if (status == null) {
                    initGetHttpTongBuData();
                    return;
                }
                if (status.equals("1")) {
                    if (lastbodyinfoBean.getInfo() == null) {
                        initGetHttpTongBuData();
                        return;
                    }
                    String num = lastbodyinfoBean.getInfo().getNum();
                    //                    LogUtils.d("stepNum:" + num);
                    if (num == null || num.equals("") || num.equals("0")) {
                        initGetHttpTongBuData();
                        return;
                    }
                    int inum = Integer.parseInt(num);
                    if (inum > 0) {
                        initTbStartPostdataStep(inum);
                    } else {
                        initGetHttpTongBuData();
                    }
                } else if (status.equals("0")) {
                    initGetHttpTongBuData();
                } else {
                    initStopPost();
                }
            }

            @Override
            public void onFailure(Call<LaststepsBean> call, Throwable t) {
                initStopPost();
                ToastUtils.showShortNotInternet("网络异常,请检查");
            }
        });
    }

    public static void initGetHttpTongBuData() {
        String token = (String) SPUtils.get("token", "");
        int user_id = (int) (SPUtils.get("user_id", 0));

        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<LastbodyinfoBean> callBack = restApi.lastbodyinfo(token, user_id);


        callBack.enqueue(new Callback<LastbodyinfoBean>() {
            @Override
            public void onResponse(Call<LastbodyinfoBean> call, Response<LastbodyinfoBean> response) {
                LastbodyinfoBean lastbodyinfoBean = response.body();
                if (lastbodyinfoBean == null) {
                    initStopPost();
                    return;
                }
                String status = lastbodyinfoBean.getStatus();
                if (status == null) {
                    initStopPost();
                    return;
                }
                //                Log.i(AppConfig.TAG, "status:" + status);
                if (status.equals("1")) {
                    if (lastbodyinfoBean.getInfo() != null) {
                        daypoor = lastbodyinfoBean.getInfo().getDaypoor();
                        beforDataTime = lastbodyinfoBean.getInfo().getHours();
                        //                        int sNum = lastbodyinfoBean.getInfo().getNum();
                        //                        Log.i(AppConfig.TAG, "daypoor:" + daypoor + ",beforDataTime:" + beforDataTime + ",sNum:" + sNum);
                        //                        if (sNum >= 0) {
                        //                            getNoDayTime();
                        //                            initTbStartPostdata();
                        //                        } else {
                        //                            initStopPost();
                        //                            ToastUtils.showShortNotInternet("数据同步完成了哦");
                        //                        }
                        getNoDayTime();
                        initTbStartPostdata();
                    } else {
                        initStopPost();

                    }
                } else {
                    initStopPost();
                }
            }

            @Override
            public void onFailure(Call<LastbodyinfoBean> call, Throwable t) {
                initStopPost();
                ToastUtils.showShortNotInternet("网络异常,请检查");
            }
        });
    }

    private static void initStopPost() {
        MoudleUtils.kyloadingDismiss(MoudleUtils.kyLoadingBuilder);
        MoudleUtils.kyloadingDismiss(DeviceControlActivity.kyLoadingBuilderDEvice);
        AppConfig.flag = false;
        HomeFragment.nHealthFlag = true;
        HomeFragment.nSleepFlag = true;
        daypoor = -1;
    }

    private static void getNoDayTime() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        nowDay = t.monthDay;
        nowHour = t.hour; // 0-23
        nowDate = year + "-" + (month + 1) + "-" + nowDay;
        //        Log.i(AppConfig.TAG, "nowDate:" + nowDate + "nowHour:" + nowHour);
    }


    public synchronized static void initSleepData(byte[] bytes1, byte[] bytes2) {
        Calendar calendar0 = Calendar.getInstance();
        int day0 = calendar0.get(Calendar.DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);//得到前一天
        int day1 = calendar.get(Calendar.DATE);


        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, -2);//得到前2天
        int day2 = calendar2.get(Calendar.DATE);


        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DATE, -3);//得到前3天
        int day3 = calendar3.get(Calendar.DATE);

        int[] is1 = DataUtils.byteTo10(bytes1);
        int[] is2 = DataUtils.byteTo10(bytes2);
        //        LogUtils.i("is1[2]:" + is1[2] + "," + "day1:" + day0 + "," + day1 + "," + day2 + "," + day3 + ", StepService.nSleepI:" + HomeFragment.nSleepI);
        if (is1[2] == day0 || is1[2] == day1 || is1[2] == day2 || is1[2] == day3) {
            if (HomeFragment.nSleepI <= HomeFragment.nSleepAll && HomeFragment.nSleepI >= 1) {
                String startData = is1[2] + "";
                String hours = is1[3] + "";
                String minute = "" + is1[4];
                if (hours.length() == 1) {
                    hours = "0" + hours;
                }
                if (minute.length() == 1) {
                    minute = "0" + minute;
                }
                String sleepNum = is1[5] + "";

                String stopTimeH = is2[8] + "";
                String stopTimeM = is2[9] + "";
                if (stopTimeH.length() == 1) {
                    stopTimeH = "0" + stopTimeH;
                }
                if (stopTimeM.length() == 1) {
                    stopTimeM = "0" + stopTimeM;
                }
                String stopTime = stopTimeH + ":" + stopTimeM;
                int[] is = new int[10];

                for (int i = 6; i < is1.length; i++) {
                    is[i - 6] = is1[i];
                }
                for (int i = 2; i < is2.length - 2; i++) {
                    is[i + 2] = is2[i];
                }
                int n = is1[5] / 8;
                int n2 = is1[5] % 8;
                String s = "";
                if (is1[5] > 0) {
                    for (int i = 0; i <= n; i++) {
                        String si = Integer.toBinaryString(is[i]);
                        if (i != n) {
                            si = initSi(si, 8);
                            s = s + si;
                        } else if (i == n) {
                            si = initSi(si, n2);
                            s = s + si;
                        }
                    }
                }
                //                LogUtils.i("startData:" + startData + "," + "hours:" + hours + ",minute:" + minute + ",stopTime:" + stopTime + ",sleepNum:" + sleepNum);

                SleepBean sleepBean = new SleepBean();
                sleepBean.setDay(startData);
                sleepBean.setHours(hours);
                sleepBean.setMinute(minute);
                sleepBean.setAwaketime(stopTime);
                sleepBean.setNum(sleepNum);
                sleepBean.setSlog(s);
                if (HomeFragment.nSleepI < HomeFragment.nSleepAll) {
                    HomeFragment.listSleep.add(sleepBean);
                    //                    LogUtils.d("StepService.listSleep1:" + HomeFragment.listSleep.size());
                } else if (HomeFragment.nSleepI == HomeFragment.nSleepAll) {
                    List<SleepBean> listSleep = new ArrayList<>();
                    listSleep.add(sleepBean);
                    listSleep.addAll(HomeFragment.listSleep);
                    HomeFragment.listSleep = listSleep;
                    //                    LogUtils.d("StepService.listSleep:2" + HomeFragment.listSleep.size());
                    initToPostSleepList();
                }
                if (HomeFragment.nSleepI < HomeFragment.nSleepAll && HomeFragment.nSleepI >= 1) {
                    HomeFragment.nSleepI--;
                }
                if (HomeFragment.nSleepI < HomeFragment.nSleepAll && HomeFragment.nSleepI > 0) {
                    HSHTool.sleepRead(HomeFragment.nSleepI);
                } else if (HomeFragment.nSleepI == 0) {
                    if (HomeFragment.nSleepAll > 1) {
                        initReadEndSleepData();
                    } else if (HomeFragment.nSleepAll == 1) {
                        initToPostSleepList();
                    }
                }
            }

        } else {
            LogUtils.d("sleepDayElse");
            if (HomeFragment.nSleepI != HomeFragment.nSleepAll) {
                initReadEndSleepData();
            } else {
                initToPostSleepList();

            }
        }

    }

    static void initReadEndSleepData() {
        HomeFragment.nSleepI = HomeFragment.nSleepAll;
        HSHTool.sleepRead(HomeFragment.nSleepI);
    }

    static void initToPostSleepList() {
        //        LogUtils.d("StepService.nSleepFlag:" + HomeFragment.nSleepFlag + ",StepService.listSleep" +
        //                HomeFragment.listSleep.size());

        if (!HomeFragment.nSleepFlag) {
            HomeFragment.nSleepFlag = true;
            if (HomeFragment.listSleep != null && HomeFragment.listSleep.size() > 0) {
                HomeFragment.initPostSleepTask();
            }
            HomeFragment.initReadHealth();
        }
    }


    private static String initSi(String si, int i) {
        if (si.length() < i) {
            for (int j = 0; j < i - si.length(); j++) {
                si = "0" + si;
            }
        }
        return si;
    }

    public synchronized static void initSleepHealth(byte[] bytes, int i) {
        int[] is = DataUtils.byteTo10(bytes);
        PostHealthBean postHealthBean = new PostHealthBean();
        //        LogUtils.d("i:" + i);
        postHealthBean.setHours(i + "");
        postHealthBean.setRate(is[1] + "");
        postHealthBean.setOxygen(is[2] + "");
        postHealthBean.setSpressure(is[3] + "");
        postHealthBean.setDpressure(is[4] + "");
        postHealthBean.setPrate(is[5] + "");
        HomeFragment.listHealth.add(postHealthBean);
    }


    /**
     * 数组转换成json数组然后以字符串得形式上传
     */
    public synchronized static String initToJson(List<SleepBean> list) {
        JSONArray jsonArray = new JSONArray();
        JSONObject tmpObj = null;
        int count = list.size();
        for (int i = 0; i < count; i++) {
            tmpObj = new JSONObject();
            try {
                tmpObj.put("day", list.get(i).getDay());
                tmpObj.put("hours", list.get(i).getHours());
                tmpObj.put("minute", list.get(i).getMinute());
                tmpObj.put("awaketime", list.get(i).getAwaketime());
                tmpObj.put("num", list.get(i).getNum());
                tmpObj.put("slog", list.get(i).getSlog());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(tmpObj);
            tmpObj = null;
        }
        String personInfos = jsonArray.toString(); // 将JSONArray转换得到String
//        LogUtils.d("personInfos" + personInfos);
        return personInfos;
    }

    /**
     * "month": "5",
     * "day": "13",
     * "steps": "85",
     * "fat": "130",
     * "mileage": "130",
     */
    public synchronized static String initToStepJson(List<StepPostBean> list) {
        JSONArray jsonArray = new JSONArray();
        JSONObject tmpObj = null;
        int count = list.size();
        for (int i = 0; i < count; i++) {
            tmpObj = new JSONObject();
            try {
                tmpObj.put("day", list.get(i).getDay());
                tmpObj.put("month", list.get(i).getMonth());
                tmpObj.put("steps", list.get(i).getSteps());
                tmpObj.put("fat", list.get(i).getFat());
                tmpObj.put("mileage", list.get(i).getMileage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(tmpObj);
            tmpObj = null;
        }
        String personInfos = jsonArray.toString(); // 将JSONArray转换得到String
//        LogUtils.d("personInfos" + personInfos);
        return personInfos;
    }

    /**
     * 数组转换成json数组然后以字符串得形式上传
     */
    public synchronized static String initToJsonHealth(List<PostHealthBean> list) {
        JSONArray jsonArray = new JSONArray();
        JSONObject tmpObj = null;
        int count = list.size();
        for (int i = 0; i < count; i++) {
            tmpObj = new JSONObject();
            try {
                /**
                 *
                 private String rate;
                 private String oxygen;
                 private String prate;
                 private String dpressure;
                 private String spressure;
                 */
                tmpObj.put("hours", list.get(i).getHours());
                tmpObj.put("rate", list.get(i).getRate());
                tmpObj.put("oxygen", list.get(i).getOxygen());
                tmpObj.put("prate", list.get(i).getPrate());
                tmpObj.put("dpressure", list.get(i).getDpressure());
                tmpObj.put("spressure", list.get(i).getSpressure());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(tmpObj);
            tmpObj = null;
        }
        String personInfos = jsonArray.toString(); // 将JSONArray转换得到String
//        LogUtils.d("personInfosHealth" + personInfos);
        return personInfos;
    }

}
