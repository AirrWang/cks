package com.ckjs.ck.Android.MineModule.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMineIntegral;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.IntegralBean;
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

public class MineIntegralActivity extends AppCompatActivity {

    private ListView list_mine_integral;
    private TextView tv_mine_integral;
    private KyLoadingBuilder builder;
    private List<IntegralBean.IntegralInfoBean.IntegralInfoDetailBean> listAll = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_integral);
        builder = new KyLoadingBuilder(this);
        initToolbar();
        initId();

    }
    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    /**
     * 网络接口
     */
    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MineIntegralActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(MineIntegralActivity.this, "token", "");
        Call<IntegralBean> callBack = restApi.integral( user_id+"",token);

        callBack.enqueue(new Callback<IntegralBean>() {
            @Override
            public void onResponse(Call<IntegralBean> call, Response<IntegralBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            listAll.clear();
                            if(response.body().getInfo().getInterule()!=null) {
                                listAll.addAll(response.body().getInfo().getInterule());

                                initAdapter();
                            }
                            tv_mine_integral.setText(response.body().getInfo().getIntervalue());
                        }
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(MineIntegralActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MineIntegralActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<IntegralBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineIntegralActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });

    }
    private ListAdapterMineIntegral listAdapterMineIntegral;

    private void initAdapter() {
        if (listAdapterMineIntegral == null) {
            listAdapterMineIntegral = new ListAdapterMineIntegral();
            listAdapterMineIntegral.setDataSource(listAll);
            list_mine_integral.setAdapter(listAdapterMineIntegral);
        } else {
            listAdapterMineIntegral.setDataSource(listAll);
            listAdapterMineIntegral.notifyDataSetChanged();
        }
    }

    private void initId() {
        list_mine_integral = (ListView) findViewById(R.id.list_mine_integral);
        tv_mine_integral = (TextView) findViewById(R.id.tv_mine_integral);
    }

    /**
     * 设置标题栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("超空币");
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
