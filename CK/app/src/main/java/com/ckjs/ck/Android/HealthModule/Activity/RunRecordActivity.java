package com.ckjs.ck.Android.HealthModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.ListAdapterRunRecord;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.RunmonthBean;
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


public class RunRecordActivity extends AppCompatActivity {

    private ListView lv_run_record;
    private KyLoadingBuilder builder;
    private List<RunmonthBean.RunmonthInfoBean> listAll = new ArrayList<>();
    private TextView tv_run_record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_record);
        builder = new KyLoadingBuilder(this);
        initToolbar();
        initId();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(RunRecordActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(RunRecordActivity.this, "token", "");
        Call<RunmonthBean> callBack = restApi.runmonth( user_id+"",token);

        callBack.enqueue(new Callback<RunmonthBean>() {
            @Override
            public void onResponse(Call<RunmonthBean> call, Response<RunmonthBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            listAll.clear();
                            listAll.addAll(response.body().getInfo());
                            initAdapter();
                            initSet(response.body().getInfo());
                            if (response.body().getInfo().size()==0){
                                MoudleUtils.viewGone(lv_run_record);
                                MoudleUtils.viewShow(tv_run_record);
                            }
                        }else {
                          MoudleUtils.viewGone(lv_run_record);
                            MoudleUtils.viewShow(tv_run_record);
                        }
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(RunRecordActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(RunRecordActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<RunmonthBean> call, Throwable t) {
                MoudleUtils.toChekWifi(RunRecordActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    /**
     * 条目点击事件
     * @param info
     */
    private void initSet(final List<RunmonthBean.RunmonthInfoBean> info) {
        lv_run_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(RunRecordActivity.this, RunRecordDetailActivity.class);
                intent.putExtra("month",info.get(position).getMonth());

                startActivity(intent);
            }
        });

    }

    private void initId() {
        lv_run_record = (ListView) findViewById(R.id.lv_run_record);
        tv_run_record = (TextView) findViewById(R.id.tv_run_record);
    }

    private ListAdapterRunRecord listAdapterRunRecord;

    private void initAdapter() {
        if (listAdapterRunRecord == null) {
            listAdapterRunRecord = new ListAdapterRunRecord();
            listAdapterRunRecord.setDataSource(listAll);
            lv_run_record.setAdapter(listAdapterRunRecord);
        } else {
            listAdapterRunRecord.setDataSource(listAll);
            listAdapterRunRecord.notifyDataSetChanged();
        }
    }


    /**
     * 设置标题栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("跑步记录");
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

