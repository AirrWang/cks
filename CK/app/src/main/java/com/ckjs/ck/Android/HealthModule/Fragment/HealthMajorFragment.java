package com.ckjs.ck.Android.HealthModule.Fragment;


import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ckjs.ck.Android.HealthModule.Adapter.ListAdapterHealth;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Interface.TaskResultApi;
import com.ckjs.ck.Bean.GetdirectBaen;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HealthMajorFragment extends Fragment implements TaskResultApi, SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private ListAdapterHealth listAdapterHealth;
    private ListView listView;
    private GetdirectBaen beaned;
    private SwipeRefreshLayout swipeRefreshLayoutHome;
    private View footView;
    private Call<GetdirectBaen> call;

    public HealthMajorFragment() {
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
        view = inflater.inflate(R.layout.fragment_health_major, container, false);
        footView = inflater.inflate(R.layout.next_foot_health, null);
        initId();
        initData();

        return view;
    }


    private void initData() {
        MoudleUtils.initRefrushTrue(swipeRefreshLayoutHome);
        onRefresh();
    }


    private void initTask() {

        call = RetrofitUtils.retrofit.create(NpApi.class).getdirect();
        call.enqueue(new Callback<GetdirectBaen>() {
            @Override
            public void onResponse(Call<GetdirectBaen> call, Response<GetdirectBaen> response) {
                if (getActivity() != null) {
                    GetdirectBaen bean = response.body();
                    beaned = bean;
                    initStatusOne();
                    initStatusZero();
                    initStatusTwo();
                }
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);

            }

            @Override
            public void onFailure(Call<GetdirectBaen> call, Throwable t) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);
                ToastUtils.showShort(getActivity(), "网络状况异常，请检查");
            }
        });

    }


    private void initAdapter(List<GetdirectBaen.GetdirectBeanInfo> list) {
        listAdapterHealth = new ListAdapterHealth(getActivity());
        listAdapterHealth.setList(list);
        listView.setAdapter(listAdapterHealth);
    }

    private void initId() {
        listView = (ListView) view.findViewById(R.id.listViewHealthMajor);
        swipeRefreshLayoutHome = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutHome);
        swipeRefreshLayoutHome.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        swipeRefreshLayoutHome.setDistanceToTriggerSync(400);
        swipeRefreshLayoutHome.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutHome.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutHome.setOnRefreshListener(this);
        listView.addFooterView(footView);
    }

    @Override
    public void initStatusOne() {
        if (beaned != null) {
            if (beaned.getStatus().equals("1")) {
                if (beaned.getInfo() != null) {
                    if (beaned.getInfo().size() > 0) {
                        initAdapter(beaned.getInfo());
                    }
                }
            }
        }

    }


    @Override
    public void initStatusZero() {
        if (beaned != null) {
            if (beaned.getStatus().equals("0")) {
                ToastUtils.showShort(getActivity(), beaned.getMsg());
            }
        }
    }

    @Override
    public void initStatusTwo() {
        if (beaned != null) {
            if (beaned.getStatus().equals("2")) {
                ToastUtils.showShort(getActivity(), beaned.getMsg());
            }
        }
    }

    @Override
    public void onRefresh() {
        initTask();
    }
}
