package com.ckjs.ck.Android.HomeModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Adapter.ListAdapterCityJsfCkthList;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.PrivilegeBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HomeJsfCkthListActivity extends AppCompatActivity {
    @BindView(R.id.lv_home_jsf_sj_list)
    ListView lv_home_jsf_sj_list;
    private String gym_id = "";
    private Call<PrivilegeBean> coachCall;
    private KyLoadingBuilder builder;
    private ListAdapterCityJsfCkthList listAdapterYuYue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_jsf_sj_list);
        ButterKnife.bind(this);
        builder = new KyLoadingBuilder(this);
        initToolbar();
        initGetGymId();
        initCoachTask();
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
     * 接受超空特惠列表接口所需上传参数gym_id
     */
    private void initGetGymId() {
        Intent intent = getIntent();
        gym_id = intent.getStringExtra("gym_id");
    }

    /**
     * 进行超空特惠列表接口请求
     */
    private void initCoachTask() {
        MoudleUtils.kyloadingShow(builder);
        coachCall = RetrofitUtils.retrofit.create(NpApi.class).privilege(gym_id);
        coachCall.enqueue(new Callback<PrivilegeBean>() {
            @Override
            public void onResponse(Call<PrivilegeBean> call, Response<PrivilegeBean> response) {
                PrivilegeBean bean = response.body();
                initToTaskData(bean);
                MoudleUtils.kyloadingDismiss(builder);
                initSetTo(bean);
            }

            @Override
            public void onFailure(Call<PrivilegeBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    /**
     * 条目点击事件
     * @param bean
     */
    private void initSetTo(final PrivilegeBean bean) {
        lv_home_jsf_sj_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.setClass(HomeJsfCkthListActivity.this, HomeStoreCardPayActivity.class);
                intent.putExtra("name", bean.getInfo().get(position).getName());
                intent.putExtra("id",bean.getInfo().get(position).getId());
                startActivity(intent);

            }
        });
    }
    /**
     * 处理超空特惠列表接口获取的数据
     *
     * @param bean
     */
    private void initToTaskData(PrivilegeBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                if (bean.getInfo() != null) {
                    if (bean.getInfo() != null) {
                        initAdatepter(bean);
                        if (bean.getInfo().size() == 0) {
                            ToastUtils.showShort(HomeJsfCkthListActivity.this,bean.getMsg());
                        }
                    }
                }

            } else if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(HomeJsfCkthListActivity.this,bean.getMsg());
            } else if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(HomeJsfCkthListActivity.this,bean.getMsg());
            }
        }
    }

    /**
     * 进行listView数据绑定
     *
     * @param bean
     */
    private void initAdatepter(PrivilegeBean bean) {
        if (listAdapterYuYue == null) {
            listAdapterYuYue = new ListAdapterCityJsfCkthList(this);
            listAdapterYuYue.setList(bean.getInfo());
            lv_home_jsf_sj_list.setAdapter(listAdapterYuYue);
        } else {
            listAdapterYuYue.setList(bean.getInfo());
            listAdapterYuYue.notifyDataSetChanged();
        }
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("超空特惠");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

}
