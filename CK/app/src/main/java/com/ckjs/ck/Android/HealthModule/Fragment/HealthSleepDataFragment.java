package com.ckjs.ck.Android.HealthModule.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.SleepInfoBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.DensityUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HealthSleepDataFragment extends Fragment {


    private View view;
    private List<Map<String, Object>> list;
    private Context context;
    private KyLoadingBuilder builder;
    private List<SleepInfoBean.SleepInfoDetailBean.SleepDetailBean> body;
    private int p = 0;
    private SleepInfoBean.SleepInfoDetailBean answer;

    public HealthSleepDataFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.llbg)
    LinearLayout llbg;//画睡眠状况条形图变量
    @BindView(R.id.tv_sleep_time)
    TextView tv_sleep_time;
    @BindView(R.id.tv_awaketime)
    TextView tv_awaketime;
    @BindView(R.id.tv_sleep_timelength)
    TextView tv_sleep_timelength;
    @BindView(R.id.tv_sleep_date)
    TextView tv_sleep_date;
    @BindView(R.id.tv_sleep_quality)
    TextView tv_sleep_quality;
    @BindView(R.id.tv_sleep_goal)
    TextView tv_sleep_goal;
    @BindView(R.id.tv_wakeup)
    TextView tv_wakeup;
    @BindView(R.id.tv_ruhusi)
    TextView tv_ruhusi;
    @BindView(R.id.sd_sleep_pre)
    SimpleDraweeView sd_sleep_pre;
    @BindView(R.id.sd_sleep_lat)
    SimpleDraweeView sd_sleep_lat;
    @BindView(R.id.tv_shenshui_hour)
    TextView tv_shenshui_hour;
    @BindView(R.id.tv_shenshui_minute)
    TextView tv_shenshui_minute;
    @BindView(R.id.tv_qianshui_hour)
    TextView tv_qianshui_hour;
    @BindView(R.id.tv_qianshui_minute)
    TextView tv_qianshui_minute;
    @BindView(R.id.tv_awake_hour)
    TextView tv_awake_hour;
    @BindView(R.id.tv_awake_minute)
    TextView tv_awake_minute;
    @BindView(R.id.tv_s_hour)
    TextView tv_s_hour;
    @BindView(R.id.tv_s_minute)
    TextView tv_s_minute;
    @BindView(R.id.tv_q_hour)
    TextView tv_q_hour;
    @BindView(R.id.tv_q_minute)
    TextView tv_q_minute;
    @BindView(R.id.tv_x_hour)
    TextView tv_x_hour;
    @BindView(R.id.tv_x_minute)
    TextView tv_x_minute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_health_sleep_data, container, false);
        ButterKnife.bind(this, view);
        builder = new KyLoadingBuilder(getActivity());
        MoudleUtils.viewGone(sd_sleep_lat);
        initData();

        return view;
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(getActivity(), "user_id", 0));
        String token = (String) SPUtils.get(getActivity(), "token", "");
        Call<SleepInfoBean> callBack = restApi.sleepinfo(user_id + "", token);

        callBack.enqueue(new Callback<SleepInfoBean>() {
            @Override
            public void onResponse(Call<SleepInfoBean> call, Response<SleepInfoBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            answer = response.body().getInfo();
                            if (response.body().getInfo().getSleepinfo() != null) {
                                body = response.body().getInfo().getSleepinfo();
                                if (body.size() != 0) {
                                    if (body.get(p).getSleepquality() == null || body.get(p).getSleepquality().equals("")) {
//                                        ToastUtils.show(getActivity(), "暂无睡眠数据", 0);
                                        initSleepZw();
                                        initFailUI();
                                    } else {
                                        huaTu(p);
                                        initUI(answer);
                                    }
                                } else {
                                    initSleepZw();
//                                    ToastUtils.show(getActivity(), "暂无睡眠数据", 0);
                                    initFailUI();
                                }
                            }
                        }
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(getActivity(),response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<SleepInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(getActivity());
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    private void initSleepZw() {
        llbg.removeAllViews();
        MoudleUtils.textViewSetText(tv_sleep_time, "无数据");
        MoudleUtils.textViewSetText(tv_awaketime, "无数据");
        initSleepNull();
    }

    private void initFailUI() {
        tv_sleep_goal.setText(answer.getSleepgoals());
        tv_sleep_date.setText(body.get(p).getDate());
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
    }

    private void initUI(SleepInfoBean.SleepInfoDetailBean info) {
        tv_sleep_time.setText(body.get(p).getSleeptime());
        tv_awaketime.setText(body.get(p).getAwaketime());
        tv_sleep_timelength.setText(body.get(p).getHlength() + "小时" + body.get(p).getIlength() + "分");
        tv_sleep_date.setText(body.get(p).getDate());
        tv_sleep_quality.setText(body.get(p).getSleepquality());
        tv_sleep_goal.setText(info.getSleepgoals());
        tv_wakeup.setText(body.get(p).getAwaketime());
        tv_ruhusi.setText(body.get(p).getSleeptime());
        tv_shenshui_hour.setText(body.get(p).getShlength());
        tv_shenshui_minute.setText(body.get(p).getSilength());
        tv_qianshui_hour.setText(body.get(p).getQhlength());
        tv_qianshui_minute.setText(body.get(p).getQilength());
        tv_awake_hour.setText(body.get(p).getAhlength());
        tv_awake_minute.setText(body.get(p).getAilength());
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

    /**
     * 画睡眠状况条形图
     */
    private void huaTu(int position) {
        if (getActivity() == null) return;
        //0浅1深2醒
        //142,120,153
        int q = getActivity().getResources().getDimensionPixelSize(R.dimen.health_sleep_qian_shui);
        int s = getActivity().getResources().getDimensionPixelSize(R.dimen.health_sleep_qian_shui);
        int x = getActivity().getResources().getDimensionPixelSize(R.dimen.health_sleep_qian_shui);
        int mh = getActivity().getResources().getDimensionPixelSize(R.dimen.health_sleep_mh);
        llbg.removeAllViews();
        if (body.get(position).getSlog() == null) {
            initSleepNull();
            return;
        }
        int timeLineAll = body.get(position).getSlog().size();
        if (timeLineAll == 0) {
            initSleepNull();
            return;
        }

        list = new ArrayList();
        List<String> types = body.get(position).getSlog();

        for (int i = 0; i < timeLineAll; i++) {
            Map<String, Object> map = new HashMap();
            map.put("type", types.get(i));
            if (types.get(i).equals("0")) {
                map.put("h", q);
            } else if (types.get(i).equals("1")) {
                map.put("h", s);
            } else if (types.get(i).equals("2")) {
                map.put("h", x);
            } else {
                map.put("h", 0);
            }
            list.add(map);
        }


        for (int i = 0; i < list.size(); i++) {
            TextView textView = new TextView(context);
            String type = (String) list.get(i).get("type");
            int h = (int) list.get(i).get("h");


            double w = (ScreenUtils.getScreenWidth() - mh * 2) / timeLineAll;
            switch (type) {
                case "0":
                    textView.setBackgroundResource(R.color.c_0fd59f);
                    break;
                case "1":
                    textView.setBackgroundResource(R.color.c_5007ac83);

                    break;
                case "2":
                    textView.setBackgroundResource(R.color.c_bdfbf6);

                    break;
            }
            LinearLayout.LayoutParams layoutParams = null;
            int i_w = (int) w;
            layoutParams = new LinearLayout.LayoutParams(i_w, h);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setLayoutParams(layoutParams);
            llbg.addView(textView);
        }
        llbg.setPadding(mh, 0, mh, mh);
    }

    private void initSleepNull() {
        if (getActivity() == null) return;
        //0浅1深2醒
        //142,120,153
        int q = getActivity().getResources().getDimensionPixelSize(R.dimen.health_sleep_qian_shui);
        int s = getActivity().getResources().getDimensionPixelSize(R.dimen.health_sleep_shen_shui);
        int x = getActivity().getResources().getDimensionPixelSize(R.dimen.health_sleep_qing_xing);
        int mh = getActivity().getResources().getDimensionPixelSize(R.dimen.health_sleep_mh);
        TextView textView = new TextView(context);

        textView.setTextSize(DensityUtils.px2sp(getActivity(), getActivity().getResources().getDimensionPixelSize(R.dimen.tv_sleep_null)));
        textView.setTextColor(getActivity().getResources().getColor(R.color.c_99));
        textView.setText("暂无数据");
        textView.setGravity(Gravity.CENTER);
//            ToastUtils.showShort("暂无睡眠数据");
        LinearLayout.LayoutParams layoutParams = null;
        double w = (ScreenUtils.getScreenWidth() - mh * 2);
        int i_w = (int) w;
        layoutParams = new LinearLayout.LayoutParams(i_w, s);
        textView.setLayoutParams(layoutParams);
        llbg.addView(textView);
    }

    @OnClick({R.id.sd_sleep_pre, R.id.sd_sleep_lat})
    public void viewOnclick(View view) {
        switch (view.getId()) {
            case R.id.sd_sleep_pre:
                if (body == null) {
                    return;
                }
                p++;
                if (p == 2) {
                    MoudleUtils.viewGone(sd_sleep_pre);
                    MoudleUtils.viewShow(sd_sleep_lat);
                } else if (p == 1) {
                    MoudleUtils.viewShow(sd_sleep_pre);
                    MoudleUtils.viewShow(sd_sleep_lat);
                }
                llbg.removeAllViews();

                if (body.get(p).getSleepquality().equals("")) {
//                    ToastUtils.show(getActivity(), "暂无睡眠数据", 0);
                    initSleepZw();
                    initFailUI();
                } else {
                    huaTu(p);
                    initUI(answer);
                }
                break;
            case R.id.sd_sleep_lat:
                if (body == null) {
                    return;
                }
                p--;
                if (p == 0) {
                    MoudleUtils.viewShow(sd_sleep_pre);
                    MoudleUtils.viewGone(sd_sleep_lat);
                } else if (p == 1) {
                    MoudleUtils.viewShow(sd_sleep_pre);
                    MoudleUtils.viewShow(sd_sleep_lat);
                }
                llbg.removeAllViews();
                if (body.get(p).getSleepquality().equals("")) {
//                    ToastUtils.show(getActivity(), "暂无睡眠数据", 0);
                    initSleepZw();
                    initFailUI();
                } else {
                    huaTu(p);
                    initUI(answer);
                }
                break;
        }
    }
}
