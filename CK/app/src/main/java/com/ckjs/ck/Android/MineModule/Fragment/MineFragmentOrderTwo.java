package com.ckjs.ck.Android.MineModule.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMineSjOrderList;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.MyCoachOrderBean;
import com.ckjs.ck.Manager.NotifyJpushReceiveManager;
import com.ckjs.ck.Message.NotifyjpushReceiveMessage;
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
public class MineFragmentOrderTwo extends Fragment implements NotifyjpushReceiveMessage {

    private View view;
    private ListView lv_coach_home_list;
    private ListAdapterMineSjOrderList listAdapterHomeCoach;
    private List<MyCoachOrderBean.MyCoachOrderInfoBean> list = new ArrayList<>();
//    private KyLoadingBuilder builder;
    private View footView;
    private int page = 1;
    private boolean flag;

    public MineFragmentOrderTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NotifyJpushReceiveManager.getInstance().setNotifyMessage(this);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.mine_fragment_two, container, false);
        footView = LayoutInflater.from(getActivity()).inflate(
                R.layout.next_foot, null);
//        builder = new KyLoadingBuilder(getActivity());
        initId();

        return view;
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

    @Override
    public void onResume() {
        super.onResume();
        initOnRefrsh();
    }

    private void initId() {
        lv_coach_home_list = (ListView) view.findViewById(R.id.listViewOrderSj);
        lv_coach_home_list.addFooterView(footView);
    }

    /**
     *
     */
    private void initCustomTask() {

//        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(getActivity(), "user_id", 0));
        String token = (String) SPUtils.get(getActivity(), "token", "");
        Call<MyCoachOrderBean> callBack = restApi.mycoachorder(user_id + "", token, page + "");

        callBack.enqueue(new Callback<MyCoachOrderBean>() {
            @Override
            public void onResponse(Call<MyCoachOrderBean> call, Response<MyCoachOrderBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            if (page == 1) {
                                list.clear();
                            }
                            list.addAll(response.body().getInfo());
                            if (list.size() == 0) {
                                ToastUtils.show(getActivity(), "您还没有预约私教哦", 0);
                            } else if (list.size() > 0) {
                                page++;
                            }
                            initAdatepter();
                            initSet();

                        }
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(getActivity(),response.body().getMsg());
                    }
                }
                showFoot();
//                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<MyCoachOrderBean> call, Throwable t) {
                MoudleUtils.toChekWifi(getActivity());
//                MoudleUtils.kyloadingDismiss(builder);
                showFoot();
            }
        });
    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 进行listView数据绑定
     */
    private void initAdatepter() {
        if (listAdapterHomeCoach == null) {
            listAdapterHomeCoach = new ListAdapterMineSjOrderList(getActivity());
            listAdapterHomeCoach.setList(list);
            lv_coach_home_list.setAdapter(listAdapterHomeCoach);
        } else {
            listAdapterHomeCoach.setList(list);
            listAdapterHomeCoach.notifyDataSetChanged();
        }
    }


    @Override
    public void sendNotifyJpushReceiveFlag(boolean flag) {
        if (flag) {
            if (listAdapterHomeCoach != null) {
                initOnRefrsh();
            }
        }
    }

    private void initOnRefrsh() {
        page = 1;
        initCustomTask();
    }
}
