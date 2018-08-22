package com.ckjs.ck.Android.HealthModule.Activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.ckjs.ck.Android.HealthModule.Adapter.ListAdapterHeartmergency;
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
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HealthHeartEmergencyActivity extends AppCompatActivity {

    private ListView lv_heart_emergency;
    private ListAdapterHeartmergency listAdapterHeartweek;
    private int page=1;
    private KyLoadingBuilder builder;
    private View footView;
    private boolean flag;
    private List<HealWarningsBean.HealWarningsInfoBean> listAll = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_heart_emergency);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot, null);
        initToolbar();
        initId();
        builder=new KyLoadingBuilder(this);

        initData();

    }

    private void initId() {
        lv_heart_emergency = (ListView) findViewById(R.id.lv_heart_emergency);
        lv_heart_emergency.addFooterView(footView);
    }
    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(HealthHeartEmergencyActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(HealthHeartEmergencyActivity.this, "token", "");
        Call<HealWarningsBean> callBack = restApi.healwarnings(user_id+"",token,page,"1");

        callBack.enqueue(new Callback<HealWarningsBean>() {
            @Override
            public void onResponse(Call<HealWarningsBean> call, Response<HealWarningsBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            listAll.addAll(response.body().getInfo());
                            if (listAll.size() == 0) {
                                ToastUtils.show(HealthHeartEmergencyActivity.this, "暂时没有突发状况", 0);
                            }

                            initSetAtapter();
                            initSet();
                            initToAdvice(listAll);
                            page++;

                        }
                    }else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(HealthHeartEmergencyActivity.this,response.body().getMsg());
                    }
                }
                showFoot();
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<HealWarningsBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HealthHeartEmergencyActivity.this);
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
        lv_heart_emergency.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_heart_emergency.getLastVisiblePosition() == (lv_heart_emergency.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                initData();
                            }

                        }
                        //判断滚动到顶部
                        if (lv_heart_emergency.getFirstVisiblePosition() == 0) {

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
     * 条目点击跳转
     * @param info
     */
    private void initToAdvice(final List<HealWarningsBean.HealWarningsInfoBean> info) {
        lv_heart_emergency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("id",info.get(position).getWarning_id());
                intent.setClass(HealthHeartEmergencyActivity.this,HealthHeartEmergencyAdviceActivity.class);
                startActivity(intent);
            }
        });

    }
    private void initSetAtapter() {
        if (listAdapterHeartweek == null) {
            listAdapterHeartweek = new ListAdapterHeartmergency(this);
            listAdapterHeartweek.setList(listAll);
            lv_heart_emergency.setAdapter(listAdapterHeartweek);
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
