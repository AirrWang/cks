package com.ckjs.ck.Android.MineModule.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterYuYue;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.BodyanalyseBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.Location.Utils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class YuYueActivity extends AppCompatActivity implements
        AMapLocationListener {
    @BindView(R.id.lv_yy)
    ListView lv_yy;
    // 定位相关
    private String lat = "";
    private String lon = "";
    private KyLoadingBuilder builder;
    private ListAdapterYuYue listAdapterYuYue;
    private AMapLocationClient mlocationClient;
    private AMapLocation mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yu_yue);
        ButterKnife.bind(this);
        initToolbar();
        builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);
        initDw();
        initStartDw();
    }

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
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        initStopDw();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
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
                ToastUtils.showShortNotInternet("定位失败，无法获取附近可预约健身房列表");

            }
        }
    }


    private void initStartDw() {
        mlocationClient.startLocation();
    }

    private void initDw() {
        mlocationClient = new AMapLocationClient(this);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位参数
        mlocationClient.setLocationOption(Utils.initLocation());
    }


    private void initStopDw() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
    }


    private Call<BodyanalyseBean> getSignBeanCall;

    /**
     * 进行附近健身房列表接口获取
     */
    private void initTaskJspList() {

        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);

        String token = "";
        token = (String) SPUtils.get("token", token);
        getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).
                bodyanalyse(token, user_id, lat, lon);
        getSignBeanCall.enqueue(new Callback<BodyanalyseBean>() {
            @Override
            public void onResponse(Call<BodyanalyseBean> call, Response<BodyanalyseBean> response) {
                BodyanalyseBean bean = response.body();
                initToTaskData(bean);
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<BodyanalyseBean> call, Throwable t) {
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
    private void initToTaskData(BodyanalyseBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                if (bean.getInfo() != null) {
                    if (bean.getInfo().getDate() != null) {
                        initAdatepter(bean.getInfo());
                        if (bean.getInfo().getDate().size() == 0) {
                            ToastUtils.showShort(YuYueActivity.this,bean.getMsg());
                        }
                    }
                }

            } else if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(YuYueActivity.this,bean.getMsg());
            } else if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(YuYueActivity.this,bean.getMsg());
            }
        }

    }

    /**
     * 进行listview数据绑定
     *
     * @param info
     */
    private void initAdatepter(BodyanalyseBean.BodyanalyseBeanInfo info) {

        if (listAdapterYuYue == null) {
            listAdapterYuYue = new ListAdapterYuYue(this);
            if (mLocation != null) {
                listAdapterYuYue.setAdr(mLocation.getAddress());
            }
            listAdapterYuYue.setList(info.getDate());
            lv_yy.setAdapter(listAdapterYuYue);
        } else {
            if (mLocation != null) {
                listAdapterYuYue.setAdr(mLocation.getAddress());
            }
            listAdapterYuYue.setList(info.getDate());
            listAdapterYuYue.notifyDataSetChanged();
        }
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("预约体测");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
