package com.ckjs.ck.Android.CoachModule.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.CoachModule.Adapter.ListAdapterHisList;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.OrderBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
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


public class HealthFrgFragmentCoach extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private ListView lv_coach_home_list;
    private SwipeRefreshLayout swipeRefreshLayoutTj;
    private ListAdapterHisList listAdapterHomeCoach;
    private List<OrderBean.OrderBeanInfo> list = new ArrayList<>();
    private int page = 1;
    private boolean flag;
    private View footView;
    private TextView tv_zw;

    public HealthFrgFragmentCoach() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_two_coach, container, false);
        footView = inflater.inflate(R.layout.next_foot, null);
        initToolbar();
        initId();


        initCustomTask();
        initSet();
        return view;
    }

    private void initFoot() {
        lv_coach_home_list.addFooterView(footView);
    }

    private void initSet() {
        lv_coach_home_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_coach_home_list.getLastVisiblePosition() == (lv_coach_home_list.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                if (page > 1) {
                                    initCustomTask();
                                }
                            }
                        }
                        //判断滚动到顶部
                        if (lv_coach_home_list.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && !flag) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });
    }

    /**
     * 约定宾客信息查看接口
     */
    private void initCustomTask() {
        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        Call<OrderBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).order(token, user_id, "2", page);
        getSignBeanCall.enqueue(new Callback<OrderBean>() {
            @Override
            public void onResponse(Call<OrderBean> call, Response<OrderBean> response) {
                OrderBean bean = response.body();
                initToTaskData(bean);
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutTj);
                showFoot();
            }

            @Override
            public void onFailure(Call<OrderBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutTj);
                showFoot();
            }
        });


    }

    private void initToTaskData(final OrderBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                List<OrderBean.OrderBeanInfo> listLs = new ArrayList<>();
                listLs = bean.getInfo();
                if (list != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(listLs);
                    initAdatepter();
                    if (list.size() == 0) {
                        tv_zw.setText("暂无");
//                        ToastUtils.showShort(bean.getMsg());
                    } else if (list.size() > 0) {
                        tv_zw.setText("");
                        page++;
                    }

                }

            } else if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(getActivity(),bean.getMsg());
            } else if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(getActivity(),bean.getMsg());
            }
        }
    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    private void initId() {
        lv_coach_home_list = (ListView) view.findViewById(R.id.lv_coach_home_list);
        tv_zw = (TextView) view.findViewById(R.id.tv_zw);
        swipeRefreshLayoutTj = (SwipeRefreshLayout) view.findViewById(R.id.swp_home_swiprrefresh);
        swipeRefreshLayoutTj.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        swipeRefreshLayoutTj.setDistanceToTriggerSync(400);
        swipeRefreshLayoutTj.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutTj.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutTj.setOnRefreshListener(this);
        initFoot();
    }

    /**
     * toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        TextView textView = (TextView) view.findViewById(R.id.toolbar_title);
        textView.setText("历史订单");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }


    /**
     * 进行listView数据绑定
     */
    private void initAdatepter() {
        if (listAdapterHomeCoach == null) {
            listAdapterHomeCoach = new ListAdapterHisList(getActivity());
            listAdapterHomeCoach.setList(list);
            lv_coach_home_list.setAdapter(listAdapterHomeCoach);
        } else {
            listAdapterHomeCoach.setList(list);
            listAdapterHomeCoach.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        initCustomTask();
    }
}
