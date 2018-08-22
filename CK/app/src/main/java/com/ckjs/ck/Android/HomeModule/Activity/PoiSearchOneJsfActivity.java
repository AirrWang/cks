package com.ckjs.ck.Android.HomeModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Location.Utils;
import com.ckjs.ck.Tool.Location.route.Activity.RoutePlanActivity;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
/**
 * 演示poi搜索功能
 */
public class PoiSearchOneJsfActivity extends AppCompatActivity implements AMapLocationListener,
        AMap.InfoWindowAdapter, AMap.OnInfoWindowClickListener {


    private AMap aMap;
    private MapView mapView;
    LatLng center;


    // 定位相关
    private AMapLocationClient mlocationClient;
    private String startLat = "";
    private String startLon = "";
    private String startNodeStr = "";
    private String endNodeStr = "";
    private AMapLocation mylocation;
    private String lat = "";
    private String lon = "";
    private String adr = "";
    private String tel = "";
    private String mycity = "";
    private MarkerOptions markerOption;
    private Marker marker;
    private LatLng ll_jsf;

    private TextView textView;
    private TextView textViewName;
    private TextView textViewCal;
    private SimpleDraweeView button;
    private LatLng pt;
    private TextView toolbarName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poisearch_one);

        initToolbar();

        initGetLal();


        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        // 定位初始化
        initDw();

        initPoiJsf();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarName = (TextView) findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
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
     * 一，地图的三种模式
     * 3D地图SDK提供三种地图类型：MAP_TYPE_NORMAL 、 MAP_TYPE_SATELLITE 和 MAP_TYPE_NIGHT。
     * 1. MAP_TYPE_NORMAL：标准地图。地图包含道路、建筑，以及重要的自然风光(如河流)等。道路和功能标签为可见。
     * 2. MAP_TYPE_SATELLITE：卫星地图。3D地图道路和功能标签为可见的，2D地图道路和功能标签不可见。
     * 3. MAP_TYPE_NIGHT：夜景地图（仅3D地图）。道路和功能标签可见。
     * shouhuan_serch,矢量地图
     */
    private void setUpMap() {

        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
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
    private void initDwStop() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
    }

    /**
     * 开始定位
     */
    private void initStartDw() {
        mlocationClient.startLocation();
    }

    private void initGetLal() {
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
        adr = intent.getStringExtra("adr");
        tel = intent.getStringExtra("tel");
        endNodeStr = intent.getStringExtra("endNodeStr");
        toolbarName.setText(endNodeStr);
    }

    @Override
    protected void onStop() {
        super.onStop();

        initDwStop();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
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
        initStartDw();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    private void initToPoiClick(String adr, final String endNodeStr, final LatLng pt, String tel) {
        this.adr = adr;
        this.endNodeStr = endNodeStr;
        this.pt = pt;
        this.tel = tel;
        addMarkersToMap(pt, adr);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                startNodeStr = amapLocation.getAddress();
                mycity = amapLocation.getCity();
                startLat = amapLocation.getLatitude() + "";
                startLon = amapLocation.getLongitude() + "";
                center = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                mylocation = amapLocation;
//
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                ToastUtils.showShortNotInternet("定位失败");

            }
        }

    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latlng, String adr) {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin))
                .position(latlng)
                .snippet(adr)
                .draggable(true);
        marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();
    }


    private void initPoiJsf() {
        /**
         * 对指定健身房进行标记
         */
        ll_jsf = new LatLng(Double.valueOf(lat), Double.valueOf(lon));

        if (aMap != null) {
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(ll_jsf, 14, 30, 0)));
        }
        initToPoiClick(adr, endNodeStr, ll_jsf, tel);


    }


    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.layout_poi_tan_kuang, null);
        textView = (TextView) view.findViewById(R.id.textViewAdress);
        textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewCal = (TextView) view.findViewById(R.id.textViewCal);
        button = (SimpleDraweeView) view.findViewById(R.id.buttonLuXian);

        initInfor(adr, endNodeStr, pt, tel);
        return view;
    }

    private void initInfor(String adr, String endNodeStr, LatLng pt, String tel) {
        textViewName.setText(endNodeStr);
        textViewCal.setText("电话:" + tel);
        textView.setText("地址:" + adr);

    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (pt != null) {
            initToOne(endNodeStr, pt);
        }
    }

    private void initToOne(String endNodeStr, LatLng pt) {
        if (!startNodeStr.equals("") && !startNodeStr.equals("") && !mycity.equals("")) {
            Intent intent = new Intent().setClass(getApplicationContext(), RoutePlanActivity.class);
            intent.putExtra("startNodeStr", startNodeStr);
            intent.putExtra("startLat", startLat);
            intent.putExtra("startLon", startLon);
            intent.putExtra("endLat", pt.latitude + "");
            intent.putExtra("endLon", pt.longitude + "");
            intent.putExtra("mycity", mycity);
            intent.putExtra("endNodeStr", endNodeStr);
            startActivity(intent);
        } else {
            ToastUtils.showShort(getApplicationContext(), "还未能获取您的当前位置,无法进行线路规\n请稍后");
        }
    }


}

