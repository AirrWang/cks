package com.ckjs.ck.Android.MineModule.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ckjs.ck.Android.MineModule.Activity.OrderDetailActivity;
import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMineOrder;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetMyOrderBean;
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

public class MineFragmentOrderOne extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;


    public MineFragmentOrderOne() {
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
        view = inflater.inflate(R.layout.activity_mine_order, container, false);
        footView = inflater.inflate(
                R.layout.next_foot, null);
        initId();
        initSet();
        MoudleUtils.initRefrushTrue(swipeRefreshLayoutHome);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    private int page = 1;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayoutHome;
    private boolean flag;
    private List<GetMyOrderBean.GetMyOrderInfoBean> listAll = new ArrayList<>();
    private ListAdapterMineOrder listAdapterMineOrder;
    private View footView;


    private void initId() {
        listView = (ListView) view.findViewById(R.id.listViewOrder);

        swipeRefreshLayoutHome = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutFavorite);
        swipeRefreshLayoutHome.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayoutHome.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutHome.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutHome.setOnRefreshListener(this);
        swipeRefreshLayoutHome.setEnabled(false);
        initFoot();
    }


    /**
     * 上拉加载逻辑
     */
    private void initSet() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                if (page > 1) {
                                    initTaskGetMyOrder();
                                }
                            }
                        }
                        //判断滚动到顶部
                        if (listView.getFirstVisiblePosition() == 0) {

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
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        initTaskGetMyOrder();
    }

    /**
     * 脚布局处理
     */
    private void initFoot() {
        listView.addFooterView(footView);
    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 得到订单数据
     */
    private void initTaskGetMyOrder() {
        int userid = (int) SPUtils.get(getActivity(), "user_id", 0);
        String token = (String) SPUtils.get(getActivity(), "token", "");
        Call<GetMyOrderBean> getSignBeanCall = RetrofitUtils.retrofit.create(WfApi.class).myorder(userid, token, page);
        getSignBeanCall.enqueue(new Callback<GetMyOrderBean>() {
            @Override
            public void onResponse(Call<GetMyOrderBean> call, Response<GetMyOrderBean> response) {
                GetMyOrderBean bean = response.body();
                if (bean != null) {

                    if (bean.getStatus().equals("1")) {

                        if (bean.getInfo() != null) {
                            List<GetMyOrderBean.GetMyOrderInfoBean> list = new ArrayList<>();
                            list = bean.getInfo();
                            if (page == 1) {
                                listAll.clear();
                            }
                            listAll.addAll(list);
                            if (listAll.size() == 0) {
                                ToastUtils.showShort(getActivity(), "暂无商品订单");
                                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);
                                return;
                            }
                            initAdapter();
                            initTo(listAll);
                            page++;
                        }
                    } else if (bean.getStatus().equals("0")) {
                        ToastUtils.showShort(getActivity(), bean.getMsg());
                    }else if (bean.getStatus().equals("2")){
                        MoudleUtils.initStatusTwo(getActivity(), true);
                    }

                }
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);
                showFoot();
            }

            @Override
            public void onFailure(Call<GetMyOrderBean> call, Throwable t) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);

                MoudleUtils.toChekWifi(getActivity());
                showFoot();

            }
        });

    }

    private void initTo(final List<GetMyOrderBean.GetMyOrderInfoBean> listAll) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent();
                    intent.putExtra("type", listAll.get(position).getShop_type());
                    intent.putExtra("order_id",listAll.get(position).getOrder_id());
                    intent.setClass(getActivity(), OrderDetailActivity.class);
                    startActivity(intent);

            }
        });
    }

    /**
     * 配置adapter
     */
    private void initAdapter() {

        if (listAdapterMineOrder == null) {
            listAdapterMineOrder = new ListAdapterMineOrder(getActivity());
            listAdapterMineOrder.setList(listAll);
            listView.setAdapter(listAdapterMineOrder);
        } else {
            listAdapterMineOrder.setList(listAll);
            listAdapterMineOrder.notifyDataSetChanged();
        }
    }
}
