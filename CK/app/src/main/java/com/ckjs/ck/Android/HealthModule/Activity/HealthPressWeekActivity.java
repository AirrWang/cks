package com.ckjs.ck.Android.HealthModule.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.ListAdapterXueYaweek;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.WpressureBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.ArrayList;
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
public class HealthPressWeekActivity extends AppCompatActivity {
    @BindView(R.id.lv_heart_week)
    ListView listView;
    private ListAdapterXueYaweek listAdapterXueYaweek;
    private Toolbar toolbar;
    private List<WpressureBean.Info> listAll = new ArrayList<>();
    private KyLoadingBuilder kyLoadingBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_week);
        ButterKnife.bind(this);
        kyLoadingBuilder = new KyLoadingBuilder(this);
        initToolbar();
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        initGetWeekDate();

    }

    /**
     * 获取血压一周详情的接口
     */
    private void initGetWeekDate() {

        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        Call<WpressureBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).wpressure(token, user_id);
        getSignBeanCall.enqueue(new Callback<WpressureBean>() {
            @Override
            public void onResponse(Call<WpressureBean> call, Response<WpressureBean> response) {
                WpressureBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {

                        listAll = bean.getInfo();
                        if (listAll != null) {
                            if (listAll.size() > 0) {
                                initSetAtapter();
                            } else {
                                ToastUtils.showShort(HealthPressWeekActivity.this,"暂无");
                            }
                        }
                    } else {
                        ToastUtils.showShort(HealthPressWeekActivity.this,bean.getMsg());
                    }

                }
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }

            @Override
            public void onFailure(Call<WpressureBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);

            }
        });
    }

    private void initSetAtapter() {
        if (listAdapterXueYaweek == null) {
            listAdapterXueYaweek = new ListAdapterXueYaweek(this);
            listAdapterXueYaweek.setList(listAll);
            listView.setAdapter(listAdapterXueYaweek);
        } else {
            listAdapterXueYaweek.setList(listAll);
            listAdapterXueYaweek.notifyDataSetChanged();
        }
    }

    /**
     * 标题栏
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("一周详情");
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
