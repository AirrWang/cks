package com.ckjs.ck.Android.HealthModule.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.WSleepInfoBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.SectorView.PieChartView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HealthSleepWeekFragment extends Fragment {

    private View view;
    private PieChartView myView;
    private TextView tv_sleep_timelength;
    private TextView tv_sleep_quality;
    private TextView tv_sleep_goal;
    private TextView tv_shenshui_hour;
    private TextView tv_shenshui_minute;
    private TextView tv_qianshui_hour;
    private TextView tv_qianshui_minute;
    private TextView tv_ruhusi;
    private TextView tv_wakeup;
    private TextView tv_awake_hour;
    private TextView tv_awake_minute;
    private KyLoadingBuilder builder;
    private WSleepInfoBean.WSleepDetailInfoBean answer;
    private TextView tv_sleep_weektime;
    private TextView tv_week_startday;
    private TextView tv_week_endday;
    private TextView tv_s_hour;
    private TextView tv_s_minute;
    private TextView tv_q_hour;
    private TextView tv_q_minute;
    private TextView tv_x_hour;
    private TextView tv_x_minute;
    private TextView tvShape;

    public HealthSleepWeekFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_health_sleep_week, container, false);
        builder = new KyLoadingBuilder(getActivity());
        initId();
        initData();
        return view;
    }

    private void initId() {
        myView = (PieChartView) view.findViewById(R.id.myViewShape);
        TextView tv_ss_time = (TextView) view.findViewById(R.id.tv_ss_time);
        tv_ss_time.setText("平均深睡");
        TextView tv_qs_time = (TextView) view.findViewById(R.id.tv_qs_time);
        tv_qs_time.setText("平均浅睡");
        TextView tv_yestoday_time = (TextView) view.findViewById(R.id.tv_yestoday_time);
        tv_yestoday_time.setText("平均入睡");
        TextView tv_day_xing_lai_time = (TextView) view.findViewById(R.id.tv_day_xing_lai_time);
        tv_day_xing_lai_time.setText("平均醒来");
        TextView tv_qx_time = (TextView) view.findViewById(R.id.tv_qx_time);
        tv_qx_time.setText("平均清醒");
        TextView tv_sleep_change = (TextView) view.findViewById(R.id.tv_sleep_change);
        tv_sleep_change.setText("本周睡眠");

        tv_sleep_timelength = (TextView) view.findViewById(R.id.tv_sleep_timelength);
        tv_sleep_quality = (TextView) view.findViewById(R.id.tv_sleep_quality);
        tv_sleep_goal = (TextView) view.findViewById(R.id.tv_sleep_goal);
        tv_shenshui_hour = (TextView) view.findViewById(R.id.tv_shenshui_hour);
        tv_shenshui_minute = (TextView) view.findViewById(R.id.tv_shenshui_minute);
        tv_qianshui_hour = (TextView) view.findViewById(R.id.tv_qianshui_hour);
        tv_qianshui_minute = (TextView) view.findViewById(R.id.tv_qianshui_minute);
        tv_ruhusi = (TextView) view.findViewById(R.id.tv_ruhusi);
        tv_wakeup = (TextView) view.findViewById(R.id.tv_wakeup);
        tv_awake_hour = (TextView) view.findViewById(R.id.tv_awake_hour);
        tv_awake_minute = (TextView) view.findViewById(R.id.tv_awake_minute);
        tv_sleep_weektime = (TextView) view.findViewById(R.id.tv_sleep_weektime);
        tv_week_startday = (TextView) view.findViewById(R.id.tv_week_startday);
        tv_week_endday = (TextView) view.findViewById(R.id.tv_week_endday);

        tv_s_hour = (TextView) view.findViewById(R.id.tv_s_hour);
        tv_s_minute = (TextView) view.findViewById(R.id.tv_s_minute);
        tv_q_hour = (TextView) view.findViewById(R.id.tv_q_hour);
        tv_q_minute = (TextView) view.findViewById(R.id.tv_q_minute);
        tv_x_hour = (TextView) view.findViewById(R.id.tv_x_hour);
        tv_x_minute = (TextView) view.findViewById(R.id.tv_x_minute);
        tvShape = (TextView) view.findViewById(R.id.tvShape);
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(getActivity(), "user_id", 0));
        String token = (String) SPUtils.get(getActivity(), "token", "");
        Call<WSleepInfoBean> callBack = restApi.wsleepinfo(user_id + "", token);

        callBack.enqueue(new Callback<WSleepInfoBean>() {
            @Override
            public void onResponse(Call<WSleepInfoBean> call, Response<WSleepInfoBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            answer = response.body().getInfo();
                            if (answer.getSleepquality() == null || answer.getSleepquality().equals("")) {
                                //                                ToastUtils.showShort("暂无睡眠数据");
                                initFailUI();
                            } else {
                                initUI();
                            }
                        }
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(getActivity(), response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<WSleepInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(getActivity());
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    private void initFailUI() {
        MoudleUtils.textViewSetText(tvShape, "暂无数据");
        tv_sleep_timelength.setText("无数据");
        tv_sleep_quality.setText("无数据");
        tv_s_hour.setText("");
        tv_s_minute.setText("无数据");
        tv_q_hour.setText("");
        tv_q_minute.setText("无数据");
        tv_wakeup.setText("无数据");
        tv_ruhusi.setText("无数据");
        tv_x_hour.setText("");
        tv_x_minute.setText("无数据");
        tv_shenshui_hour.setText("");
        tv_shenshui_minute.setText("");
        tv_qianshui_hour.setText("");
        tv_qianshui_minute.setText("");
        tv_awake_hour.setText("");
        tv_awake_minute.setText("");
        tv_wakeup.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv_wakeup.setTextColor(getActivity().getResources().getColor(R.color.c_99));
        tv_ruhusi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv_ruhusi.setTextColor(getActivity().getResources().getColor(R.color.c_99));
        tv_sleep_goal.setText(answer.getSleepgoals());
        tv_sleep_weektime.setText(answer.getStartday() + "-" + answer.getEndday());
        tv_week_startday.setText(answer.getStartday());
        tv_week_endday.setText(answer.getEndday());
    }

    private int[] mColors;
    private double[] valueD;

    //0浅1深2醒
    private void initUI() {
        if (valueD == null) {
            valueD = new double[3];
        }
        if (mColors == null) {
            mColors = new int[]{getActivity().getResources().getColor(R.color.c_0fd59f), getActivity().getResources().getColor(R.color.c_5007ac83), getActivity().getResources().getColor(R.color.c_bdfbf6)};
        }
        if (answer.getQpercentage() > 0) {
            valueD[0] = answer.getQpercentage();
        }
        if (answer.getSpercentage() > 0) {
            valueD[1] = answer.getSpercentage();
        }
        if (answer.getApercentage() > 0) {
            valueD[2] = answer.getApercentage();
        }
        if (answer.getQpercentage() <= 0 && answer.getSpercentage() <= 0 && answer.getApercentage() <= 0) {
            MoudleUtils.textViewSetText(tvShape, "暂无数据");
        } else {
            if (valueD != null && valueD.length > 0) {
                MoudleUtils.textViewSetText(tvShape, "");
                myView.setFanClickAbleData(valueD, mColors, 0.01);
            } else {
                MoudleUtils.textViewSetText(tvShape, "暂无数据");
            }
        }
        tv_sleep_weektime.setText(answer.getStartday() + "-" + answer.getEndday());
        tv_week_startday.setText(answer.getStartday());
        tv_week_endday.setText(answer.getEndday());
        tv_sleep_timelength.setText(answer.getHlength() + "小时" + answer.getIlength() + "分");
        tv_sleep_quality.setText(answer.getSleepquality());
        tv_sleep_goal.setText(answer.getSleepgoals());
        tv_shenshui_hour.setText(answer.getShlength());
        tv_shenshui_minute.setText(answer.getSilength());
        tv_qianshui_hour.setText(answer.getQhlength());
        tv_qianshui_minute.setText(answer.getQilength());
        tv_ruhusi.setText(answer.getSleeptime());
        tv_wakeup.setText(answer.getAwaketime());
        tv_awake_hour.setText(answer.getAhlength());
        tv_awake_minute.setText(answer.getAilength());
        tv_s_hour.setText("小时");
        tv_s_minute.setText("分");
        tv_q_hour.setText("小时");
        tv_q_minute.setText("分");
        tv_x_hour.setText("小时");
        tv_x_minute.setText("分");
        tv_wakeup.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv_wakeup.setTextColor(getActivity().getResources().getColor(R.color.c_33));
        tv_ruhusi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv_ruhusi.setTextColor(getActivity().getResources().getColor(R.color.c_33));
    }

}
