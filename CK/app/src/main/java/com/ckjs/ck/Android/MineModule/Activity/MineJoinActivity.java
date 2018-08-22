package com.ckjs.ck.Android.MineModule.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.AcH5Activity;
import com.ckjs.ck.Android.HomeModule.Activity.BaoMingActivity;
import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMineActivity;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetMyActivityBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
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

public class MineJoinActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private int page = 1;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayoutHome;
    private boolean flag;
    private List<GetMyActivityBean.GetMyActivityInfoBean> listAll = new ArrayList<>();
    private ListAdapterMineActivity listAdapterMineActivity;
    private View footView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_join);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot, null);
        initId();
        initToolbar();
        initSet();
        MoudleUtils.initRefrushTrue(swipeRefreshLayoutHome);
        onRefresh();
    }

    private void initId() {
        listView = (ListView) findViewById(R.id.listViewActivity);

        swipeRefreshLayoutHome = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutFavorite);
        swipeRefreshLayoutHome.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        swipeRefreshLayoutHome.setDistanceToTriggerSync(400);
        swipeRefreshLayoutHome.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutHome.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutHome.setOnRefreshListener(this);
        swipeRefreshLayoutHome.setEnabled(false);
        initFoot();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("我已参加");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        initTaskGetMyActivity();
    }

    private void initFoot() {
        listView.addFooterView(footView);
    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

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
                                if (page>1) {
                                    initTaskGetMyActivity();
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

    private void initTaskGetMyActivity() {
        int userid = (int) SPUtils.get(this, "user_id", 0);
        String token = (String) SPUtils.get(this, "token", "");
        Call<GetMyActivityBean> getSignBeanCall = RetrofitUtils.retrofit.create(WfApi.class).myactivity(token, userid, page);
        getSignBeanCall.enqueue(new Callback<GetMyActivityBean>() {
            @Override
            public void onResponse(Call<GetMyActivityBean> call, Response<GetMyActivityBean> response) {
                GetMyActivityBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        List<GetMyActivityBean.GetMyActivityInfoBean> list = new ArrayList<>();
                        if (bean.getInfo() != null) {
                            list = bean.getInfo();
                            if (page==1){
                                listAll.clear();
                            }
                            listAll.addAll(list);

                            if (listAll.size() == 0) {
                                ToastUtils.showShort(MineJoinActivity.this, "未参加任何活动");
                            }
                            initAdapter();
                            initSetItemClick(listAll);
                            page++;
                        }

                    } else if (bean.getStatus().equals("0")) {
                        ToastUtils.showShort(MineJoinActivity.this, bean.getMsg());
                    } else if (bean.getStatus().equals("2")) {
                        ToastUtils.showShort(MineJoinActivity.this, bean.getMsg());
                    }
                }
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);
                showFoot();
            }

            @Override
            public void onFailure(Call<GetMyActivityBean> call, Throwable t) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);

                MoudleUtils.toChekWifi(MineJoinActivity.this);
                showFoot();

            }
        });

    }

    private void initAdapter() {

        if (listAdapterMineActivity == null) {
            listAdapterMineActivity = new ListAdapterMineActivity(this);
            listAdapterMineActivity.setList(listAll);
            listView.setAdapter(listAdapterMineActivity);
        } else {
            listAdapterMineActivity.setList(listAll);
            listAdapterMineActivity.notifyDataSetChanged();
        }
    }

    private void initSetItemClick(final List<GetMyActivityBean.GetMyActivityInfoBean> list) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getType().equals("1")) {
                    initToBaoMing(position, list);
                } else {
                    initToAcH5(position, list);
                }
            }
        });
    }

    private void initToAcH5(int position, List<GetMyActivityBean.GetMyActivityInfoBean> list) {
        if (NetworkUtils.isNetworkAvailable(MineJoinActivity.this)) {
            Intent intent = new Intent();
            intent.putExtra("acurl", list.get(position).getAcurl());
            intent.setClass(MineJoinActivity.this, AcH5Activity.class);
            startActivity(intent);
        } else {
            MoudleUtils.toChekWifi(MineJoinActivity.this);

        }

    }

    private void initToBaoMing(int position, List<GetMyActivityBean.GetMyActivityInfoBean> list) {
        if (NetworkUtils.isNetworkAvailable(MineJoinActivity.this)) {
            Intent intent = new Intent();
            intent.setClass(MineJoinActivity.this, BaoMingActivity.class);
            int activity_id = Integer.parseInt(list.get(position).getActivity_id());
            intent.putExtra("activity_id", activity_id);
            intent.putExtra("name", list.get(position).getName());
            startActivity(intent);
        } else {
            MoudleUtils.toChekWifi(MineJoinActivity.this);

        }

    }

}
