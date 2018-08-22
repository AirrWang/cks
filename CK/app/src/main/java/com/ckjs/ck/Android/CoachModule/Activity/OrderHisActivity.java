package com.ckjs.ck.Android.CoachModule.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.CoachModule.Adapter.ListAdapterHomeCoachOrderHisList;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.CoachRecordBean;
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

public class OrderHisActivity extends AppCompatActivity {
    private ListView lv_coach_home_list;
    private ListAdapterHomeCoachOrderHisList listAdapterHomeCoach;
    private List<CoachRecordBean.CoachRecordInfoBean> list = new ArrayList<>();
    private int page = 1;
    private KyLoadingBuilder builder;
    private View footView;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_his);
        footView = LayoutInflater.from(this).inflate(R.layout.next_foot, null);
        builder = new KyLoadingBuilder(this);
        initId();
        initToolbar();
        initCustomTask();
        initSet();

    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
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
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) SPUtils.get(this, "user_id", 0);
        String token = (String) SPUtils.get(this, "token", "");

        Call<CoachRecordBean> callBack = restApi.coachrecord(user_id + "", token, page + "");


        callBack.enqueue(new Callback<CoachRecordBean>() {
            @Override
            public void onResponse(Call<CoachRecordBean> call, Response<CoachRecordBean> response) {
                CoachRecordBean coachRecordBean = response.body();
                if (coachRecordBean != null) {
                    if (coachRecordBean.getStatus().equals("1")) {
                        if (page == 1) {
                            list.clear();
                        }
                        if (coachRecordBean.getInfo() != null) {
                            list.addAll(coachRecordBean.getInfo());

                            initAdatepter();

                            page++;
                        }
                    } else if (coachRecordBean.getStatus().equals("0")) {
                        ToastUtils.showShort(OrderHisActivity.this, coachRecordBean.getMsg());
                    } else if (coachRecordBean.getStatus().equals("2")) {
                        ToastUtils.showShort(OrderHisActivity.this, coachRecordBean.getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
                showFoot();
            }

            @Override
            public void onFailure(Call<CoachRecordBean> call, Throwable t) {

                ToastUtils.show(OrderHisActivity.this, getResources().getString(R.string.not_wlan_show), 0);
                MoudleUtils.kyloadingDismiss(builder);
                showFoot();

            }
        });

    }

    private void initId() {
        lv_coach_home_list = (ListView) findViewById(R.id.lv_coach_home_list);
        initFoot();

    }

    /**
     * toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("交易记录");
        setSupportActionBar(toolbar);
        ActionBar actionBar = (getSupportActionBar());
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify shouhuan_serch parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 进行listView数据绑定
     */
    private void initAdatepter() {
        if (listAdapterHomeCoach == null) {
            listAdapterHomeCoach = new ListAdapterHomeCoachOrderHisList(this);
            listAdapterHomeCoach.setList(list);
            lv_coach_home_list.setAdapter(listAdapterHomeCoach);
        } else {
            listAdapterHomeCoach.setList(list);
            listAdapterHomeCoach.notifyDataSetChanged();
        }
    }
}
