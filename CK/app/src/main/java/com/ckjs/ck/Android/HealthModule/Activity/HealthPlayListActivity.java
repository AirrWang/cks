package com.ckjs.ck.Android.HealthModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.ListAdapterHealthPlayList;
import com.ckjs.ck.Android.HomeModule.Activity.AcH5Activity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.DirectlistBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HealthPlayListActivity extends AppCompatActivity {
    @BindView(R.id.lv_health_play_list)
    ListView lv_health_play_list;

    private ListAdapterHealthPlayList listAdapterHealthPlayList;
    private String class_id = "";
    private KyLoadingBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_play_list);
        builder=new KyLoadingBuilder(this);
        initToolbar();
        ButterKnife.bind(this);
        initGetClissId();
        initTaskPlatList();
    }

    /**
     * 接收小分类得id
     */
    private void initGetClissId() {
        Intent intent = getIntent();
        class_id = intent.getStringExtra("class_id");
    }

    /**
     * 进行视频列表填充
     */
    private void initSetAdapter(List<DirectlistBean.DirectlistBeanInfo.Direct> list) {
        listAdapterHealthPlayList = new ListAdapterHealthPlayList(this);
        listAdapterHealthPlayList.setList(list);
        lv_health_play_list.setAdapter(listAdapterHealthPlayList);
    }

    /**
     * 进行每个部位或者小分类得视频列表得请求
     */
    private void initTaskPlatList() {
        MoudleUtils.kyloadingShow(builder);
        Call<DirectlistBean> beanCall = RetrofitUtils.retrofit.create(NpApi.class).directlist(class_id);
        beanCall.enqueue(new Callback<DirectlistBean>() {
            @Override
            public void onResponse(Call<DirectlistBean> call, Response<DirectlistBean> response) {
                DirectlistBean bean = response.body();
                initList(bean);
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<DirectlistBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HealthPlayListActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });

    }

    private void initSetItemclick(final DirectlistBean bean) {
        lv_health_play_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (initToH5(position, intent, bean)) return;
                initHealthMore(position, intent, bean);

            }
        });
    }

    /**
     * 跳原生播放视频
     *
     * @param position
     * @param intent
     * @param bean
     */
    private void initHealthMore(int position, Intent intent, DirectlistBean bean) {
        if (bean.getInfo().getDirect().get(position).getType().equals("1")) {
            intent.putExtra("direct_id", bean.getInfo().getDirect().get(position).getId());
            intent.setClass(HealthPlayListActivity.this, HealthMoreActivity.class);
            startActivity(intent);
            return;
        }
    }

    /**
     * 跳h5
     *
     * @param position
     * @param intent
     * @param bean
     * @return
     */
    private boolean initToH5(int position, Intent intent, DirectlistBean bean) {
        if (bean.getInfo().getDirect().get(position).getType().equals("2")) {
            intent.putExtra("acurl", bean.getInfo().getDirect().get(position).getDirecturl());
            intent.setClass(HealthPlayListActivity.this, AcH5Activity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 视频列表获取数据处理
     *
     * @param bean
     */
    private void initList(DirectlistBean bean) {
        if (bean.getStatus().equals("1")) {
            if (bean.getInfo() != null) {
                if (bean.getInfo().getDirect() != null) {
                    if (bean.getInfo().getDirect().size() == 0) {
                        ToastUtils.showShort(HealthPlayListActivity.this, "此类暂无视频");
                        return;
                    }
                    initSetAdapter(bean.getInfo().getDirect());
                    initSetItemclick(bean);
                }
            }
        } else  {
            ToastUtils.showShort(this, bean.getMsg());
        }
    }

    /**
     * 导航栏
     */
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("视频列表");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    /**
     * 返回键
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
