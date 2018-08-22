package com.ckjs.ck.Android.CoachModule.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ckjs.ck.Android.CoachModule.Activity.OrderPlaceActivity;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Android.CoachModule.Activity.CoachDetailActivity;
import com.ckjs.ck.Android.CoachModule.Activity.OrderHisActivity;
import com.ckjs.ck.Android.CoachModule.Activity.TiXianActivity;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.CanAcceptBean;
import com.ckjs.ck.Bean.CoachCoachMyInfoBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */


public class MineFragmentCoach extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tv_coach_mine_name;
    private TextView tv_coach_mine_gymname;
    private LinearLayout ll_coach_mine_star;
    private TextView tv_coach_todaymoney;
    private TextView tv_coach_accumulated;
    private TextView tv_coach_balance;
    private TextView tv_coach_num;
    private SimpleDraweeView coach_touxiang;
    private Switch coach_mine_switch;


    public MineFragmentCoach() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_three_coach, container, false);
        initToolbar();
        initId();

        return view;
    }

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    /**
     * 请求界面数据
     */
    private void initData() {

        String user_id = SPUtils.get(getActivity(), "user_id", 0) + "";
        String token = (String) SPUtils.get(getActivity(), "token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<CoachCoachMyInfoBean> callBack = restApi.coachmyinfo(user_id, token);

        callBack.enqueue(new Callback<CoachCoachMyInfoBean>() {
            @Override
            public void onResponse(Call<CoachCoachMyInfoBean> call, Response<CoachCoachMyInfoBean> response) {
                try {
                    CoachCoachMyInfoBean coachinfoBean = response.body();
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            initUI(coachinfoBean);
                        } else if (response.body().getStatus().equals("0")) {
                            ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                        } else if (response.body().getStatus().equals("2")) {
                            ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<CoachCoachMyInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(getActivity());
            }
        });
    }

    private void initUI(CoachCoachMyInfoBean coachinfoBean) {
        if (coachinfoBean.getInfo() != null) {
            tv_coach_mine_name.setText(coachinfoBean.getInfo().getName());
            tv_coach_mine_gymname.setText(coachinfoBean.getInfo().getGymname());
            ll_coach_mine_star.removeAllViews();
            drawStars(coachinfoBean);
            tv_coach_todaymoney.setText(coachinfoBean.getInfo().getToday());
            tv_coach_accumulated.setText("￥" + coachinfoBean.getInfo().getAccumulated());
            tv_coach_balance.setText("￥" + coachinfoBean.getInfo().getBalance());
            tv_coach_num.setText(coachinfoBean.getInfo().getNum());
            FrescoUtils.setImage(coach_touxiang, AppConfig.url_jszd + coachinfoBean.getInfo().getPicture());
            if (coachinfoBean.getInfo().getStatus().equals("0")) {
                coach_mine_switch.setChecked(false);
            } else if (coachinfoBean.getInfo().getStatus().equals("1")) {
                coach_mine_switch.setChecked(true);
            }

        }
    }

    /**
     * 画星方法
     */
    private void drawStars(CoachCoachMyInfoBean coachinfoBean) {
        for (int i = 0; i < Integer.parseInt(coachinfoBean.getInfo().getGrade()); i++) {
            SimpleDraweeView ivPoint = new SimpleDraweeView(getActivity());
            int w = CkApplication.getInstance().getResources().getDimensionPixelOffset(R.dimen.city_jsf_xing);
            int w2 = CkApplication.getInstance().getResources().getDimensionPixelOffset(R.dimen.city_jsf_xing_m);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w, w);
            layoutParams.setMargins(0, 0, w2, w2);
            ivPoint.setLayoutParams(layoutParams);
            FrescoUtils.setImage(ivPoint, AppConfig.res + R.drawable.list_attention);
            ll_coach_mine_star.addView(ivPoint);
        }
    }

    private void initId() {

        ll_coach_mine_star = (LinearLayout) view.findViewById(R.id.ll_coach_mine_star);
        tv_coach_mine_gymname = (TextView) view.findViewById(R.id.tv_coach_mine_gymname);
        tv_coach_mine_name = (TextView) view.findViewById(R.id.tv_coach_mine_name);
        LinearLayout ll_coach_mine_detail = (LinearLayout) view.findViewById(R.id.ll_coach_mine_detail);
        ll_coach_mine_detail.setOnClickListener(this);
        LinearLayout ll_coach_mine_tixian = (LinearLayout) view.findViewById(R.id.ll_coach_mine_tixian);
        ll_coach_mine_tixian.setOnClickListener(this);
        LinearLayout ll_order_his = (LinearLayout) view.findViewById(R.id.ll_order_his);
        ll_order_his.setOnClickListener(this);
        LinearLayout ll_order_place= (LinearLayout) view.findViewById(R.id.ll_order_place);
        ll_order_place.setOnClickListener(this);

        tv_coach_todaymoney = (TextView) view.findViewById(R.id.tv_coach_todaymoney);
        tv_coach_accumulated = (TextView) view.findViewById(R.id.tv_coach_accumulated);
        tv_coach_balance = (TextView) view.findViewById(R.id.tv_coach_balance);
        tv_coach_num = (TextView) view.findViewById(R.id.tv_coach_num);
        coach_touxiang = (SimpleDraweeView) view.findViewById(R.id.coach_touxiang);
        coach_mine_switch = (Switch) view.findViewById(R.id.coach_mine_switch);
        coach_mine_switch.setOnClickListener(this);
    }

    /**
     * toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        TextView textView = (TextView) view.findViewById(R.id.toolbar_title);
        textView.setText("我");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_coach_mine_tixian:
                startActivity(new Intent().setClass(getActivity(), TiXianActivity.class));//申请提现
                break;
            case R.id.ll_order_his:
                startActivity(new Intent().setClass(getActivity(), OrderHisActivity.class));//交易记录
                break;
            case R.id.ll_coach_mine_detail:
                startActivity(new Intent().setClass(getActivity(), CoachDetailActivity.class));//个人资料
                break;
            case R.id.coach_mine_switch:
                toChange();
                break;
            case R.id.ll_order_place://接单地点
                startActivity(new Intent().setClass(getActivity(), OrderPlaceActivity.class));//个人资料
                break;
        }
    }

    /**
     *
     */
    private void toChange() {
        String user_id = SPUtils.get(getActivity(), "user_id", 0) + "";
        String token = (String) SPUtils.get(getActivity(), "token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<CanAcceptBean> callBack = restApi.canaccept(user_id, token);

        callBack.enqueue(new Callback<CanAcceptBean>() {
            @Override
            public void onResponse(Call<CanAcceptBean> call, Response<CanAcceptBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                    }
                }

            }

            @Override
            public void onFailure(Call<CanAcceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi(getActivity());
            }
        });
    }
}
