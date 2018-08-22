package com.ckjs.ck.Android.HealthModule.Activity;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.RuntrackBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Location.TraceRePlay;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class RunJiLuActivity extends AppCompatActivity implements TraceListener, View.OnClickListener {
    private AMap aMap;
    private MapView mapView;
    private String run_id = "";
    private List<LatLng> mGraspLatLngList;
    private Toolbar toolbar;
    private Polyline mOriginPolyline;
    private Polyline mGraspPolyline;
    private RadioButton mOriginRadioButton, mGraspRadioButton;
    private TextView tv_km, tv_kcal, tv_km_m_s, chronometer, tv_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_run_ji_lu);
        initId();
        initToolbar();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2 + 3;
        mThreadPool = Executors.newFixedThreadPool(threadPoolSize);
        init();
        initGetRunId();
        initGetAllShuJu();
    }

    private void initId() {
        tv_km = (TextView) findViewById(R.id.tv_km);
        tv_kcal = (TextView) findViewById(R.id.tv_kcal);
        tv_km_m_s = (TextView) findViewById(R.id.tv_km_m_s);
        chronometer = (TextView) findViewById(R.id.chronometer);
        tv_data = (TextView) findViewById(R.id.tv_data);
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

    /**
     * 标题栏
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("轨迹查询");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        mGraspRadioButton = (RadioButton) findViewById(R.id.record_show_activity_grasp_radio_button);
        mOriginRadioButton = (RadioButton) findViewById(R.id.record_show_activity_origin_radio_button);
        mOriginRadioButton.setOnClickListener(this);
        mGraspRadioButton.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }


    private void initGetRunId() {
        Intent intent = getIntent();
        run_id = intent.getStringExtra("run_id");
    }

    private void initGetAllShuJu() {

        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        Call<RuntrackBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).runtrack(token, user_id, run_id);
        getSignBeanCall.enqueue(new Callback<RuntrackBean>() {
            @Override
            public void onResponse(Call<RuntrackBean> call, Response<RuntrackBean> response) {
                RuntrackBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        list = bean.getInfo().getTrack();
                        if (list == null || list.size() == 0) {
                            return;
                        }
                        initSetData(bean);
                        addOriginTrace();
                        initHuaXian();
                    } else {
                        ToastUtils.showShort(RunJiLuActivity.this, bean.getMsg());
                    }

                }
            }

            @Override
            public void onFailure(Call<RuntrackBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
            }
        });
    }

    private void initSetData(RuntrackBean bean) {
        MoudleUtils.textViewSetText(tv_km, bean.getInfo().getMileage());
        MoudleUtils.textViewSetText(tv_kcal, bean.getInfo().getFat());
        MoudleUtils.textViewSetText(tv_km_m_s, bean.getInfo().getSpeed());
        MoudleUtils.textViewSetText(chronometer, bean.getInfo().getTime());
        MoudleUtils.textViewSetText(chronometer, bean.getInfo().getTime());
        MoudleUtils.textViewSetText(tv_data, bean.getInfo().getCreatetime());
    }

    private void addOriginTrace() {
        List<LatLng> originList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            originList.add(new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLon())));
        }
        mGraspLatLngList = originList;
        LatLng startPoint = new LatLng(Double.parseDouble(list.get(0).getLat()), Double.parseDouble(list.get(0).getLon()));
        LatLng endPoint = new LatLng(Double.parseDouble(list.get(list.size() - 1).getLat()), Double.parseDouble(list.get(list.size() - 1).getLon()));
        if (aMap != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(),
                    100));
        }
        aMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(endPoint).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.end)));
        mGraspRoleMarker = aMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.route_walk_select))));
        mOriginPolyline = aMap.addPolyline(new PolylineOptions().color(
                Color.BLUE).addAll(originList));
        startMove();
    }

    /**
     * 用获取坐标点集合进行划线
     */
    private void initHuaXian() {

        // 轨迹纠偏初始化
        LBSTraceClient mTraceClient = new LBSTraceClient(
                getApplicationContext());
        List<TraceLocation> mGraspTraceLocationList = MoudleUtils
                .parseTraceLocationList(list);
        // 调用轨迹纠偏，将mGraspTraceLocationList进行轨迹纠偏处理
        mTraceClient.queryProcessedTrace(1, mGraspTraceLocationList,
                LBSTraceClient.TYPE_AMAP, this);

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mThreadPool != null) {
            mThreadPool.shutdownNow();
        }
    }

    List<RuntrackBean.RuntrackBeanInfo.Track> list = new ArrayList<>();


    @Override
    public void onRequestFailed(int i, String s) {

    }

    @Override
    public void onTraceProcessing(int i, int i1, List<LatLng> list) {

    }

    @Override
    public void onFinished(int i, List<LatLng> list, int i1, int i2) {
        addGraspTrace(list);
        setGraspEnable(false);


    }

    private LatLngBounds getBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (mGraspLatLngList == null) {
            return b.build();
        }
        for (int i = 0; i < mGraspLatLngList.size(); i++) {
            b.include(mGraspLatLngList.get(i));
        }
        return b.build();

    }


    private void addGraspTrace(List<LatLng> graspList) {
        if (graspList == null || graspList.size() < 2) {
            return;
        }

        mGraspPolyline = aMap.addPolyline(new PolylineOptions()
                .setCustomTexture(
                        BitmapDescriptorFactory
                                .fromResource(R.drawable.grasp_trace_line))
                .width(40).addAll(graspList));


    }

    private Marker mGraspRoleMarker;

    private void startMove() {
        if (mRePlay != null) {
            mRePlay.stopTrace();
        }
        mRePlay = rePlayTrace(mGraspLatLngList, mGraspRoleMarker);
    }

    private TraceRePlay mRePlay;

    /**
     * 轨迹回放方法
     */
    private TraceRePlay rePlayTrace(List<LatLng> list, final Marker updateMarker) {
        TraceRePlay replay = new TraceRePlay(list, 100,
                new TraceRePlay.TraceRePlayListener() {

                    @Override
                    public void onTraceUpdating(LatLng latLng) {
                        if (updateMarker != null) {
                            updateMarker.setPosition(latLng); // 更新小人实现轨迹回放
                        }
                    }

                    @Override
                    public void onTraceUpdateFinish() {
                        if (mGraspRadioButton.isChecked()) {
                            setGraspEnable(true);
                            setOriginEnable(false);
                        }
                        if (mOriginRadioButton.isChecked()) {
                            setOriginEnable(true);
                            setGraspEnable(false);
                        }

                    }
                });
        mThreadPool.execute(replay);
        return replay;
    }


    /**
     * 设置是否显示纠偏后轨迹
     *
     * @param enable
     */
    private void setGraspEnable(boolean enable) {
        if (mGraspPolyline == null) {
            return;
        }
        if (enable) {
            mGraspPolyline.setVisible(true);
        } else {
            mGraspPolyline.setVisible(false);
        }
    }

    /**
     * 设置是否显示原始轨迹
     *
     * @param enable
     */
    private void setOriginEnable(boolean enable) {
        if (mOriginPolyline == null) {
            return;
        }
        if (enable) {
            mOriginPolyline.setVisible(true);
        } else {
            mOriginPolyline.setVisible(false);
        }
    }

    private ExecutorService mThreadPool;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.record_show_activity_grasp_radio_button:
                mGraspRadioButton.setChecked(true);
                mOriginRadioButton.setChecked(false);
                setGraspEnable(true);
                setOriginEnable(false);
                break;
            case R.id.record_show_activity_origin_radio_button:
                mGraspRadioButton.setChecked(false);
                mOriginRadioButton.setChecked(true);
                setGraspEnable(false);
                setOriginEnable(true);
                break;
        }
    }
}
