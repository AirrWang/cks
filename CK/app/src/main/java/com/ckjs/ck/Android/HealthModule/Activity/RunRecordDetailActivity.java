package com.ckjs.ck.Android.HealthModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.ckjs.ck.Android.HealthModule.Adapter.ListAdapterRunDetailRecord;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.RunlistBean;
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

public class RunRecordDetailActivity extends AppCompatActivity {

    private ListView lv_run_record_detail;
    private KyLoadingBuilder builder;
    private List<RunlistBean.RunlistInfoBean> listAll = new ArrayList<>();
    private int page = 1;
    private boolean flag;
    private View footView;
    private String month;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_record_detail);
        builder = new KyLoadingBuilder(this);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot, null);
        initToolbar();
        initId();
        Intent intent = getIntent();
        month = intent.getStringExtra("month");
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(RunRecordDetailActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(RunRecordDetailActivity.this, "token", "");
        Call<RunlistBean> callBack = restApi.runlist(user_id + "", token, month, page);

        callBack.enqueue(new Callback<RunlistBean>() {
            @Override
            public void onResponse(Call<RunlistBean> call, Response<RunlistBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {

                            listAll.addAll(response.body().getInfo());

                            initAdapter();
                            initSet();
                            initFoot();
                            page++;

                        }
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(RunRecordDetailActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(RunRecordDetailActivity.this,response.body().getMsg());
                    }
                }
                showFoot();
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<RunlistBean> call, Throwable t) {
                showFoot();
                MoudleUtils.toChekWifi(RunRecordDetailActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 脚布局设置
     */

    private void initFoot() {
        lv_run_record_detail.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_run_record_detail.getLastVisiblePosition() == (lv_run_record_detail.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                initData();
                            }

                        }
                        //判断滚动到顶部
                        if (lv_run_record_detail.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && !flag) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });
    }

    /**
     * 条目点击事件
     */
    private void initSet() {
        lv_run_record_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO


                String run_id = "";
                if (listAll != null) {
                    if (listAll.size() > 0) {
                        if (position != listAll.size()) {
                            run_id = listAll.get(position).getRun_id();
                            startActivity(new Intent().putExtra("run_id", run_id).setClass(RunRecordDetailActivity.this, RunJiLuActivity.class));
                        }
                    }
                }

            }
        });

    }

    private ListAdapterRunDetailRecord listAdapterRunDetailRecord;

    private void initAdapter() {
        if (listAdapterRunDetailRecord == null) {
            listAdapterRunDetailRecord = new ListAdapterRunDetailRecord();
            listAdapterRunDetailRecord.setDataSource(listAll);
            lv_run_record_detail.setAdapter(listAdapterRunDetailRecord);
        } else {
            listAdapterRunDetailRecord.setDataSource(listAll);
            listAdapterRunDetailRecord.notifyDataSetChanged();
        }
    }

    private void initId() {
        lv_run_record_detail = (ListView) findViewById(R.id.lv_run_record_detail);

        lv_run_record_detail.addFooterView(footView);
    }

    /**
     * 设置标题栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("轨迹记录");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * 设置返回键
     *
     * @param item
     * @return
     */
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
}
