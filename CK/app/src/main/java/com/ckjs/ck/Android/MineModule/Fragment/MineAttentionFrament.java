package com.ckjs.ck.Android.MineModule.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ckjs.ck.Android.MineModule.Activity.MineAttentionPeople;
import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMineAttention;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetMyfocusBean;
import com.ckjs.ck.Manager.NotifyActivityBzManager;
import com.ckjs.ck.Manager.NotifyActivityGzManager;
import com.ckjs.ck.Manager.NotifyInfoUpdateMessageManager;
import com.ckjs.ck.Message.NotifyActicityBzMessage;
import com.ckjs.ck.Message.NotifyActicityGzMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
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

public class MineAttentionFrament extends Fragment implements NotifyActicityBzMessage,NotifyActicityGzMessage {

    private int page = 1;
    private boolean flag;
    private View footView;
    private List<GetMyfocusBean.GetMyfocusInfoBean> listAll = new ArrayList<>();
    private ListView listviewWDGZ;
    private KyLoadingBuilder builder;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_mine_attention, container, false);

        builder = new KyLoadingBuilder(getActivity());
        footView = LayoutInflater.from(getActivity()).inflate(
                R.layout.next_foot, null);
        initId();
        initTask();
        return view;
    }

    private void initFoot() {
        listviewWDGZ.addFooterView(footView);
    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    private void initSet() {
        listviewWDGZ.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (listviewWDGZ.getLastVisiblePosition() == (listviewWDGZ.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                initTask();
                            }

                        }
                        //判断滚动到顶部
                        if (listviewWDGZ.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                flag = firstVisibleItem + visibleItemCount == totalItemCount && !flag;
            }
        });
    }

    private ListAdapterMineAttention listAdapterMineAttention;

    private void initAdapter() {
        if (listAdapterMineAttention == null) {
            listAdapterMineAttention = new ListAdapterMineAttention();
            listAdapterMineAttention.setDataSource(listAll);
            listviewWDGZ.setAdapter(listAdapterMineAttention);
        } else {
            listAdapterMineAttention.setDataSource(listAll);
            listAdapterMineAttention.notifyDataSetChanged();
        }
    }

    private void initTask() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(getActivity(), "user_id", 0));
        String token = (String) SPUtils.get(getActivity(), "token", "");
        Call<GetMyfocusBean> callBack = restApi.myfocus(token, user_id, page);

        callBack.enqueue(new Callback<GetMyfocusBean>() {
            @Override
            public void onResponse(Call<GetMyfocusBean> call, Response<GetMyfocusBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            if (page == 1) {
                                listAll.clear();
                            }
                            listAll.addAll(response.body().getInfo());
                            if (listAll.size() == 0) {
                                ToastUtils.show(getActivity(), "您还没有关注任何人哦", 0);
                            }
                            initAdapter();
                            page++;
                            initSet();
                            initSetTo();
                            NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                        }

                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(getActivity(), response.body().getMsg());
                    }
                }
                showFoot();
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<GetMyfocusBean> call, Throwable t) {
                MoudleUtils.toChekWifi(getActivity());
                MoudleUtils.kyloadingDismiss(builder);
                showFoot();
            }
        });

    }


    private void initId() {
        NotifyActivityBzManager.getInstance().setNotifyMessage(this);
        NotifyActivityGzManager.getInstance().setNotifyMessage(this);
        listviewWDGZ = (ListView) view.findViewById(R.id.mine_attention_list);
        initFoot();
    }

    /**
     * 条目点击事件
     */
    private void initSetTo() {
        listviewWDGZ.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), MineAttentionPeople.class);
                intent.putExtra("focus_id", listAll.get(position).getFocus_id());
                startActivity(intent);

            }
        });
    }

    @Override
    public void sendNotifyActicityBzMessage(boolean flag, final String bzName, final int focus_id) {
        if (flag) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (listAll != null) {
                            if (listAll.size() > 0) {
                                for (int i = 0; i < listAll.size(); i++) {
                                    if ((focus_id + "").equals(listAll.get(i).getFocus_id())) {
                                        listAll.get(i).setFocus_name(bzName);
                                        listAdapterMineAttention.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void sendNotifyActicityGzMessageFlag(boolean flag) {
        if (flag) {
            page = 1;
            initTask();
        }
    }
}
