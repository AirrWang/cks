package com.ckjs.ck.Android.HomeModule.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Adapter.ListAdapterHomeRead;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.ReadlistBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
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
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HomeTjActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private int page = 1;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayoutHome;
    private boolean flag;
    private List<ReadlistBean.ReadlistBeanInfo> listAll = new ArrayList<>();
    private ListAdapterHomeRead listAdapterHomeRead;
    private View footView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tj);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot, null);
        initId();
        initToolbar();
        initSet();
        MoudleUtils.initRefrushTrue(swipeRefreshLayoutHome);
        onRefresh();
    }


    private void initFoot() {
        listView.addFooterView(footView);
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("健康讲堂");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
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

    private void initAdapter() {
        if (listAdapterHomeRead == null) {
            listAdapterHomeRead = new ListAdapterHomeRead(this);
            listAdapterHomeRead.setList(listAll);
            listView.setAdapter(listAdapterHomeRead);
        } else {
            listAdapterHomeRead.setList(listAll);
            listAdapterHomeRead.notifyDataSetChanged();
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
                                if (page > 1) {
                                    initTaskHOmeTuiJian();
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

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    private void initId() {
        listView = (ListView) findViewById(R.id.listViewHome);
        swipeRefreshLayoutHome = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutHome);
        swipeRefreshLayoutHome.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayoutHome.setDistanceToTriggerSync(400);
        swipeRefreshLayoutHome.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutHome.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutHome.setOnRefreshListener(this);
        swipeRefreshLayoutHome.setEnabled(false);
        initFoot();
    }

    private void initTaskHOmeTuiJian() {
        int userid = (int) SPUtils.get(this, "user_id", 0);
        String token = (String) SPUtils.get(this, "token", "");
        Call<ReadlistBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).readlist(token,userid,page);
        getSignBeanCall.enqueue(new Callback<ReadlistBean>() {
            @Override
            public void onResponse(Call<ReadlistBean> call, Response<ReadlistBean> response) {
                ReadlistBean bean = response.body();
                if (bean != null) {
                        if (bean.getStatus().equals("1")) {
                            if (bean.getInfo() != null) {
                                List<ReadlistBean.ReadlistBeanInfo> list = new ArrayList<>();
                                list = bean.getInfo();
                                if (page==1){
                                    listAll.clear();
                                }
                                listAll.addAll(list);
                                if (listAll.size()==0){
                                    ToastUtils.showShort(HomeTjActivity.this, bean.getMsg());
                                }
                                initAdapter();
                                initSetItemClick(listAll);
                                page++;
                            }
                        } else  {
                            ToastUtils.showShort(HomeTjActivity.this, bean.getMsg());
                        }
                    }

                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);
                showFoot();
            }

            @Override
            public void onFailure(Call<ReadlistBean> call, Throwable t) {
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHome);
                MoudleUtils.toChekWifi(HomeTjActivity.this);

                showFoot();

            }
        });

    }

    private void initSetItemClick(final List<ReadlistBean.ReadlistBeanInfo> list) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (NetworkUtils.isNetworkAvailable(HomeTjActivity.this)) {
                    if (list.get(position).getReadurl().equals("")) {
                        ToastUtils.showShort(HomeTjActivity.this, "页面不存在");
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("acurl", list.get(position).getReadurl());
                        intent.putExtra("read_id", list.get(position).getId());
                        intent.putExtra("isfavorite", list.get(position).getIsfavorite());
                        intent.putExtra("subhead",list.get(position).getIntro());
                        intent.putExtra("imageurl", AppConfig.url_jszd + list.get(position).getPicture());

                        intent.setClass(HomeTjActivity.this, TjH5HomeActivity.class);
                        startActivity(intent);
                    }
                } else {
                    MoudleUtils.toChekWifi(HomeTjActivity.this);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        initTaskHOmeTuiJian();
    }
}
