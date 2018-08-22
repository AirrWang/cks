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

import com.ckjs.ck.Android.HealthModule.Adapter.ListAdapterBloodoxygenemergency;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.HealWarningsBean;
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

public class HealthBloodOxygenEmergencyActivity extends AppCompatActivity{

    private ListView lv_bloodoxgen_emergency;
    private ListAdapterBloodoxygenemergency listAdapterHeartweek;
    private int page=1;
    private KyLoadingBuilder builder;
    private View footView;
    private boolean flag;
    private List<HealWarningsBean.HealWarningsInfoBean> listAll = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodoxygen_emergency);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot, null);
        initToolbar();
        initId();
        builder=new KyLoadingBuilder(this);

        initData();
    }

    private void initId() {
        lv_bloodoxgen_emergency = (ListView) findViewById(R.id.lv_bloodoxgen_emergency);
        lv_bloodoxgen_emergency.addFooterView(footView);
    }
    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(HealthBloodOxygenEmergencyActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(HealthBloodOxygenEmergencyActivity.this, "token", "");
        Call<HealWarningsBean> callBack = restApi.healwarnings(user_id+"",token,page,"2");

        callBack.enqueue(new Callback<HealWarningsBean>() {
            @Override
            public void onResponse(Call<HealWarningsBean> call, Response<HealWarningsBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            listAll.addAll(response.body().getInfo());
                            if (listAll.size() == 0) {
                                ToastUtils.show(HealthBloodOxygenEmergencyActivity.this, "暂时没有突发状况", 0);
                            }

                            initSetAtapter();
                            initSet();
                            initToAdvice(listAll);
                            page++;

                        }
                    }else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(HealthBloodOxygenEmergencyActivity.this,response.body().getMsg());
                    }
                }
                showFoot();
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<HealWarningsBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HealthBloodOxygenEmergencyActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
                showFoot();
            }
        });

    }
    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    private void initSet() {
        lv_bloodoxgen_emergency.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_bloodoxgen_emergency.getLastVisiblePosition() == (lv_bloodoxgen_emergency.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                initData();
                            }

                        }
                        //判断滚动到顶部
                        if (lv_bloodoxgen_emergency.getFirstVisiblePosition() == 0) {

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

    //条目点击事件 跳转
    private void initToAdvice(final List<HealWarningsBean.HealWarningsInfoBean> info) {
        lv_bloodoxgen_emergency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("id",info.get(position).getWarning_id());
                intent.setClass(HealthBloodOxygenEmergencyActivity.this,HealthBloodOxygenEmergencyAdviceActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initSetAtapter() {
        if (listAdapterHeartweek == null) {
            listAdapterHeartweek = new ListAdapterBloodoxygenemergency(this);
            listAdapterHeartweek.setList(listAll);
            lv_bloodoxgen_emergency.setAdapter(listAdapterHeartweek);
        } else {
            listAdapterHeartweek.setList(listAll);
            listAdapterHeartweek.notifyDataSetChanged();
        }

    }

    /**
     * 标题栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("突发状况");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * 返回设置
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
