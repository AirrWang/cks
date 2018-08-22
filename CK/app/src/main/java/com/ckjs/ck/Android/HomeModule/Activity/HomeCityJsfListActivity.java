package com.ckjs.ck.Android.HomeModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.ckjs.ck.Android.HomeModule.Adapter.ListAdapterCityJsfList;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.CitygymBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.Location.Utils;
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
public class HomeCityJsfListActivity extends AppCompatActivity implements AMapLocationListener {
    @BindView(R.id.lv_home_city_jsf_list)
    ListView lv_home_city_jsf_list;
    private String city = "";
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_city_jsf_list);
        initGetType();
        ButterKnife.bind(this);
        initToolbar();
        initgetCityData();
        initDw();
        dwstart();

    }
    /**
     * type0：默认；1：共享手环；
     */
    private void initGetType() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
    }

    private void initgetCityData() {
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        if (city == null) {
            city = "";
        }
    }

    private AMapLocationClient mlocationClient;
    private AMapLocation mLocation;
    private String lat = "";
    private String lon = "";
    private KyLoadingBuilder builder;
    private ListAdapterCityJsfList listAdapterYuYue;


    //设置返回键可用
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
     * 定位初始化
     */
    private void initDw() {
        mlocationClient = new AMapLocationClient(this);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位参数
        mlocationClient.setLocationOption(Utils.initLocation());
    }

    /**
     * 结束定位
     */
    private void dwstop() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
    }

    /**
     * 开始定位
     */
    private void dwstart() {
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                lat = amapLocation.getLatitude() + "";
                lon = amapLocation.getLongitude() + "";
                mLocation = amapLocation;
                if (getSignBeanCall == null) {
                    initTaskJspList();
                } else {
                    MoudleUtils.kyloadingDismiss(builder);
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                MoudleUtils.kyloadingDismiss(builder);
                ToastUtils.showShortNotInternet("定位失败，无法获取该城市健身房列表");

            }
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dwstop();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    private Call<CitygymBean> getSignBeanCall;

    /**
     * 进行附近健身房列表接口获取
     */
    private void initTaskJspList() {

        getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).citygymFocus(lat, lon, city,type+"");
        getSignBeanCall.enqueue(new Callback<CitygymBean>() {
            @Override
            public void onResponse(Call<CitygymBean> call, Response<CitygymBean> response) {
                CitygymBean bean = response.body();
                initToTaskData(bean);
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<CitygymBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(builder);
            }
        });

    }

    /**
     * 处理附健身房接口数据
     *
     * @param bean
     */
    private void initToTaskData(CitygymBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                if (bean.getInfo() != null) {
                    if (bean.getInfo() != null) {
                        initAdatepter(bean);
                        if (bean.getInfo().size() == 0) {
                            ToastUtils.showShort(HomeCityJsfListActivity.this,bean.getMsg());
                        }
                    }
                }

            } else if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(HomeCityJsfListActivity.this,bean.getMsg());
            } else if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(HomeCityJsfListActivity.this,bean.getMsg());
            }
        }

    }

    /**
     * 健身房详情挑转
     *
     * @param bean
     */
    private void initSet(final CitygymBean bean) {
        lv_home_city_jsf_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("gym_id", bean.getInfo().get(position).getId());
                intent.putExtra("name", bean.getInfo().get(position).getName());
                intent.setClass(HomeCityJsfListActivity.this, HomeJsfMoreActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 进行listview数据绑定    //type 1超空门店 2超空私教 使用ListAdapterCityJsfList
     */
    private void initAdatepter(CitygymBean bean) {
        if (listAdapterYuYue == null) {
            listAdapterYuYue = new ListAdapterCityJsfList(this,"1");

            listAdapterYuYue.setList(bean.getInfo());
            lv_home_city_jsf_list.setAdapter(listAdapterYuYue);
        } else {

            listAdapterYuYue.setList(bean.getInfo());
            listAdapterYuYue.notifyDataSetChanged();
        }
        initSet(bean);
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("超空门店");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
