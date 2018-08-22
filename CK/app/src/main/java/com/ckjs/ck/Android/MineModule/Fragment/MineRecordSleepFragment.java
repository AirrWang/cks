package com.ckjs.ck.Android.MineModule.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetWrecordBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class MineRecordSleepFragment extends android.support.v4.app.Fragment {
    private View view;//主布局和List的头布局

    private GetWrecordBean getWrecordBean;
    private LinearLayout ll_wRecord;
    private List<GetWrecordBean.GetWrecordInfoBean> listRead = new ArrayList<>();
    private LinearLayout ll_wRecordTime;
    private LinearLayout ll_mRecord;
    private LinearLayout ll_mRecordTime;
    private LinearLayout ll_yRecord;
    private LinearLayout ll_yRecordTime;
    private HorizontalScrollView h_years;


    public MineRecordSleepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fragment_mine_record_sleep, container, false);

        initId();
        initWeekData();
        initMonthData();
        initYearData();
        return view;
    }


    private void initWeekData() {
        int userid = (int) SPUtils.get(getActivity(), "user_id", 0);
        String token = (String) SPUtils.get(getActivity(), "token", "");
        String type = "sleep";
        Call<GetWrecordBean> getSignBeanCall = RetrofitUtils.retrofit.create(WfApi.class).wrecord(userid, token, type);
        getSignBeanCall.enqueue(new Callback<GetWrecordBean>() {
            @Override
            public void onResponse(Call<GetWrecordBean> call, Response<GetWrecordBean> response) {
                getWrecordBean = response.body();
                initMineRecordTaskData();
            }

            @Override
            public void onFailure(Call<GetWrecordBean> call, Throwable t) {

                ToastUtils.showShort(getActivity(), "网络状况异常，请检查");

            }
        });

    }

    private void initMonthData() {
        int userid = (int) SPUtils.get(getActivity(), "user_id", 0);
        String token = (String) SPUtils.get(getActivity(), "token", "");
        String type = "sleep";
        Call<GetWrecordBean> getSignBeanCall = RetrofitUtils.retrofit.create(WfApi.class).mrecord(userid, token, type);
        getSignBeanCall.enqueue(new Callback<GetWrecordBean>() {
            @Override
            public void onResponse(Call<GetWrecordBean> call, Response<GetWrecordBean> response) {
                getWrecordBean = response.body();
                if (getWrecordBean != null) {
                    if (getWrecordBean.getStatus().equals("1")) {

                        if (getWrecordBean.getInfo() != null) {
                            listRead = getWrecordBean.getInfo();
                            if (listRead != null) {
                                try {
                                    initMonth(listRead);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        ToastUtils.showShort(getActivity(), getWrecordBean.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetWrecordBean> call, Throwable t) {

                //                ToastUtils.showShort(getActivity(), "网络状况异常，请检查");

            }
        });

    }

    private void initMonth(List<GetWrecordBean.GetWrecordInfoBean> listRead) {
        for (int i = 0; i < listRead.size(); i++) {
            TextView weekdata = new TextView(
                    getActivity());
            int margenH = ScreenUtils.getScreenWidth(getActivity()) / 72 / 2;
            int margenHRight = ScreenUtils.getScreenWidth(getActivity()) / 72 / 2;
            LinearLayout.LayoutParams layoutParams = null;
            layoutParams = new LinearLayout.LayoutParams((int) (((ScreenUtils.getScreenWidth(getActivity()) - margenH * listRead.size() * 2)) / listRead.size()), ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(margenH, 0, margenH, margenHRight);
            weekdata.setLayoutParams(layoutParams);
            weekdata.setText(listRead.get(i).getValue());
            weekdata.setPadding(margenHRight, 0, margenHRight, margenHRight);
            weekdata.setGravity(Gravity.CENTER);
            weekdata.setTextColor(getResources().getColor(R.color.fea21249));
            /**
             *        先得到手机的scaledDecsity
             *        设置字体大小时
             */

            DisplayMetrics dm = getResources().getDisplayMetrics();
            float value = dm.scaledDensity;
            weekdata.setTextSize(getResources().getDimensionPixelSize(R.dimen.d14) / value);
            weekdata.setSingleLine(true);

            ll_mRecord.addView(weekdata);
            TextView tv = new TextView(
                    getActivity());
            LinearLayout.LayoutParams layoutParamsTv = null;
            layoutParamsTv = new LinearLayout.LayoutParams((int) (((ScreenUtils.getScreenWidth(getActivity()) - margenH * listRead.size() * 2)) / listRead.size()), ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTv.setMargins(margenHRight, margenHRight * 2, margenHRight, margenHRight);
            tv.setLayoutParams(layoutParamsTv);
            tv.setText(listRead.get(i).getCreattime());
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.c_33));
            tv.setPadding(margenHRight, 0, margenHRight, margenHRight);
            tv.setSingleLine(true);
            /**
             *        先得到手机的scaledDecsity
             *        设置字体大小时
             */

            tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.d14) / value);
            tv.setSingleLine(true);
            ll_mRecordTime.addView(tv);


        }
    }

    private void initYearData() {
        int userid = (int) SPUtils.get(getActivity(), "user_id", 0);
        String token = (String) SPUtils.get(getActivity(), "token", "");
        String type = "sleep";
        Call<GetWrecordBean> getSignBeanCall = RetrofitUtils.retrofit.create(WfApi.class).yrecord(userid, token, type);
        getSignBeanCall.enqueue(new Callback<GetWrecordBean>() {
            @Override
            public void onResponse(Call<GetWrecordBean> call, Response<GetWrecordBean> response) {
                getWrecordBean = response.body();
                if (getWrecordBean != null) {
                    if (getWrecordBean.getStatus().equals("1")) {

                        if (getWrecordBean.getInfo() != null) {
                            listRead = getWrecordBean.getInfo();
                            if (listRead != null) {
                                try {
                                    initYear(listRead);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else if (getWrecordBean.getStatus().equals("2")) {
                        MoudleUtils.initStatusTwo(getActivity(), true);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetWrecordBean> call, Throwable t) {

                //                ToastUtils.showShort(getActivity(), "网络状况异常，请检查");

            }
        });

    }

    private void initYear(List<GetWrecordBean.GetWrecordInfoBean> listRead) {
        for (int i = 0; i < listRead.size(); i++) {
            TextView weekdata = new TextView(
                    getActivity());
            int margenH = ScreenUtils.getScreenWidth(getActivity()) / 72 / 2;
            int margenHRight = ScreenUtils.getScreenWidth(getActivity()) / 72 / 2;
            LinearLayout.LayoutParams layoutParams = null;
            layoutParams = new LinearLayout.LayoutParams((int) (((ScreenUtils.getScreenWidth(getActivity()) - margenH * 6 * 2)) / 6), ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(margenH, 0, margenH, margenHRight);
            weekdata.setLayoutParams(layoutParams);
            weekdata.setText(listRead.get(i).getValue());
            weekdata.setPadding(margenHRight, 0, margenHRight, margenHRight);
            weekdata.setGravity(Gravity.CENTER);
            weekdata.setTextColor(getResources().getColor(R.color.fea21249));
            /**
             *        先得到手机的scaledDecsity
             *        设置字体大小时
             */

            DisplayMetrics dm = getResources().getDisplayMetrics();
            float value = dm.scaledDensity;
            weekdata.setTextSize(getResources().getDimensionPixelSize(R.dimen.d14) / value);
            weekdata.setSingleLine(true);

            ll_yRecord.addView(weekdata);
            TextView tv = new TextView(
                    getActivity());
            LinearLayout.LayoutParams layoutParamsTv = null;
            layoutParamsTv = new LinearLayout.LayoutParams((int) (((ScreenUtils.getScreenWidth(getActivity()) - margenH * 6 * 2)) / 6), ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTv.setMargins(margenHRight, margenHRight * 2, margenHRight, margenHRight);
            tv.setLayoutParams(layoutParamsTv);
            tv.setText(listRead.get(i).getCreattime());
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.c_33));
            tv.setPadding(margenHRight, 0, margenHRight, margenHRight);
            tv.setSingleLine(true);
            /**
             *        先得到手机的scaledDecsity
             *        设置字体大小时
             */

            tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.d14) / value);
            tv.setSingleLine(true);
            ll_yRecordTime.addView(tv);


        }
        MoudleUtils.initHTooRight(mHandler, h_years);
    }


    Handler mHandler = new Handler();

    private void initMineRecordTaskData() {
        if (getWrecordBean != null) {
            if (getWrecordBean.getStatus().equals("1")) {

                if (getWrecordBean.getInfo() != null) {
                    listRead = getWrecordBean.getInfo();
                    //                    initClearH();
                    if (listRead != null) {
                        try {
                            initImageH(listRead);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (getWrecordBean.getStatus().equals("2")) {
                MoudleUtils.initStatusTwo(getActivity(), true);
            }
        }
    }

    private void initImageH(List<GetWrecordBean.GetWrecordInfoBean> listRead) {
        for (int i = 0; i < listRead.size(); i++) {
            TextView weekdata = new TextView(
                    getActivity());
            int margenH = ScreenUtils.getScreenWidth(getActivity()) / 72 / 2;
            int margenHRight = ScreenUtils.getScreenWidth(getActivity()) / 72 / 2;
            LinearLayout.LayoutParams layoutParams = null;
            layoutParams = new LinearLayout.LayoutParams((int) (((ScreenUtils.getScreenWidth(getActivity()) - margenH * listRead.size() * 2)) / listRead.size()), ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(margenH, 0, margenH, margenHRight);
            weekdata.setLayoutParams(layoutParams);
            weekdata.setText(listRead.get(i).getValue());
            weekdata.setPadding(margenHRight, 0, margenHRight, margenHRight);
            weekdata.setGravity(Gravity.CENTER);
            weekdata.setTextColor(getResources().getColor(R.color.fea21249));
            /**
             *        先得到手机的scaledDecsity
             *        设置字体大小时
             */

            DisplayMetrics dm = getResources().getDisplayMetrics();
            float value = dm.scaledDensity;
            weekdata.setTextSize(getResources().getDimensionPixelSize(R.dimen.d14) / value);
            weekdata.setSingleLine(true);

            ll_wRecord.addView(weekdata);
            TextView tv = new TextView(
                    getActivity());
            LinearLayout.LayoutParams layoutParamsTv = null;
            layoutParamsTv = new LinearLayout.LayoutParams((int) (((ScreenUtils.getScreenWidth(getActivity()) - margenH * listRead.size() * 2)) / listRead.size()), ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTv.setMargins(margenHRight, margenHRight * 2, margenHRight, margenHRight);
            tv.setLayoutParams(layoutParamsTv);
            tv.setText(listRead.get(i).getCreattime());
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.c_33));
            tv.setPadding(margenHRight, 0, margenHRight, margenHRight);
            tv.setSingleLine(true);
            /**
             *        先得到手机的scaledDecsity
             *        设置字体大小时
             */

            tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.d14) / value);
            tv.setSingleLine(true);
            ll_wRecordTime.addView(tv);


        }

    }

    //    private void initClearH() {
    //        ll_wRecord.removeAllViews();
    //        ll_wRecordTime.removeAllViews();
    //    }

    private void initId() {
        ll_wRecord = (LinearLayout) view.findViewById(R.id.ll_mine_weekrecordsleep);
        ll_wRecordTime = (LinearLayout) view.findViewById(R.id.ll_mine_weekrecordsleeptime);
        ll_mRecord = (LinearLayout) view.findViewById(R.id.ll_mine_monthrecordsleep);
        ll_mRecordTime = (LinearLayout) view.findViewById(R.id.ll_mine_monthrecordsleeptime);
        ll_yRecord = (LinearLayout) view.findViewById(R.id.ll_mine_yearrecordsleep);
        ll_yRecordTime = (LinearLayout) view.findViewById(R.id.ll_mine_yearrecordsleeptime);
        h_years = (HorizontalScrollView) view.findViewById(R.id.h_years);
    }
}
