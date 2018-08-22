package com.ckjs.ck.Android.HealthModule.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.MonthrunBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.GpsTool;
import com.ckjs.ck.Tool.Location.Utils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MainRunJiuPianActivity extends AppCompatActivity implements
        AMapLocationListener, TraceListener, LocationSource {
    @BindView(R.id.btn_start)
    Button btn_start;
    @BindView(R.id.textViewKm)
    TextView textViewKm;//总里程数
    @BindView(R.id.tv_num)
    TextView tv_num;//总次数
    @BindView(R.id.tv_kcal_all)
    TextView tv_kcal_all;//总卡路里数
    @BindView(R.id.tv_time)
    TextView tv_time;//总时长
    @BindView(R.id.tv_km)
    TextView tv_km;
    @BindView(R.id.tv_km_m_s)
    TextView tv_km_m_s;
    @BindView(R.id.tv_kcal)
    TextView tv_kcal;
    @BindView(R.id.btn_end)
    Button btn_end;
    @BindView(R.id.ll_all_shu_ju)
    LinearLayout ll_all_shu_ju;
    @BindView(R.id.ll_now_shu_ju)
    LinearLayout ll_now_shu_ju;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;


    private AMap aMap;
    private MapView mapView;
    private AMapLocationClient mlocationClient;
    private LatLng myLoc;

    private String speed = "";
    private String fat = "";
    private String mileage = "";
    private String timeLine = "";

    private String personInfos = "";//坐标点数组转化成的json字符串
    private Toolbar toolbar;
    private float diatanceKcal;//千米用来显示和消耗卡路里计算
    private float diatanceM;//米
    private float expand;//kcal
    private PolylineOptions mPolyoptions;
    private Polyline mpolyline;
    private LocationSource.OnLocationChangedListener mListener;
    int n = 30;//控制上传点数
    private List<TraceLocation> mTracelocationlist = new ArrayList<>();
    private LatLng preLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_run);
        initToolbar();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        // 获得计时器对象
        time = (Chronometer) this.findViewById(R.id.chronometer);
        ButterKnife.bind(this);

        initGetAllShuJu();

        init();
        initDw();


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
        textView.setText("超空跑");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
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
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        }
    }


    private void initStarDw() {
        if (mlocationClient != null) {
            mlocationClient.startLocation();
        }
    }

    private void initDw() {
        mlocationClient = new AMapLocationClient(this);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位参数
        mlocationClient.setLocationOption(Utils.initLocationMore(2000));
    }


    private void initStopDw() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
    }

    private void initGetAllShuJu() {

        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        Call<MonthrunBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).monthrun(token, user_id);
        getSignBeanCall.enqueue(new Callback<MonthrunBean>() {
            @Override
            public void onResponse(Call<MonthrunBean> call, Response<MonthrunBean> response) {
                MonthrunBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        MoudleUtils.textViewSetText(textViewKm, bean.getInfo().getMileage());
                        MoudleUtils.textViewSetText(tv_num, bean.getInfo().getNum());
                        MoudleUtils.textViewSetText(tv_kcal_all, bean.getInfo().getFat() + "kcal");
                        MoudleUtils.textViewSetText(tv_time, bean.getInfo().getTime());
                    } else {
                        ToastUtils.showShort(MainRunJiuPianActivity.this, bean.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<MonthrunBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
            }
        });
    }

    @OnClick(R.id.ll_now_shu_ju)
    void llTop() {
        if (ll_top.getVisibility() == View.VISIBLE) {
            MoudleUtils.viewGone(ll_top);
        } else {
            MoudleUtils.viewShow(ll_top);

        }
    }

    @OnClick(R.id.ll_all_shu_ju)
    void toRunGuiJiJiLu() {
        startActivity(new Intent().setClass(this, RunRecordActivity.class));
    }

    /**
     * 参数|描述|值
     * user_id|用户id|
     * token|token值|
     * speed|速度|（例：12'22''/km00'00''
     * fat|卡路里消耗|（单位：千卡0.0
     * mileage|里程|（单位：米0.0
     * timeLine|运动时长|（单位：秒
     * track|轨迹|
     *
     * @param
     * @param
     * @param
     * @return
     */
    private void initPostGuiJjDian() {
        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        Call<AcceptBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).uprunrecord(token, user_id, speed, fat, mileage, timeLine, personInfos);
        getSignBeanCall.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                AcceptBean bean = response.body();
                if (bean != null) {
                    ToastUtils.showShort(MainRunJiuPianActivity.this, bean.getMsg());
                }
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
            }
        });
    }

    /**
     */
    private void initStart() {
        AlertDialog alertDialog = null;
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(MainRunJiuPianActivity.this).setTitle("温馨提示：")
                    .setMessage("请确认已允许定位,\n\n否则无法记录您的运动数据。\n\n请走到开阔的户外运动吧！")
                    .setPositiveButton("开始运动", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            maipClear();
                            initNowShuJu();
                            MoudleUtils.viewShow(ll_now_shu_ju);
                            MoudleUtils.viewGone(ll_all_shu_ju);
                            initStarDw();
                            getTimeStart();
                            MoudleUtils.viewShow(btn_end);
                            MoudleUtils.viewInvisibily(btn_start);
                        }

                    }).create(); // 创建对话框

        }
        if (alertDialog != null) {
            alertDialog.setCancelable(true);
            if (!alertDialog.isShowing()) {
                alertDialog.show(); // 显示
            }
        }

    }

    /**
     */
    private void initStartGps() {
        AlertDialog alertDialog = null;
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(MainRunJiuPianActivity.this).setTitle("设置GPS：")
                    .setMessage("请先开启GPS,\n\n否则无法记录您的运动数据!")
                    .setPositiveButton("去设置GPS", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                        }

                    }).create(); // 创建对话框

        }
        if (alertDialog != null) {
            alertDialog.setCancelable(true);
            if (!alertDialog.isShowing()) {
                alertDialog.show(); // 显示
            }
        }

    }

    public void initIsEnd() {
        final CharSequence[] items = {"结束", "结束并保存", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                initEnd();
                                initToJiuPian();
                                break;
                            case 1:
                                initSavePostData();
                                initToJiuPian();
                                break;
                        }
                    }
                }

        );

        AlertDialog alert = builder.create();
        alert.show();
    }

    /***
     * 结束并保存数据
     */
    private void initSavePostData() {
        initEnd();
        if (pointsListAll.size() > n - 1 && diatanceKcal > 0 && !speed.equals("00'00''") && !fat.equals("0.0") && !mileage.equals("0.0")) {
            initToJson();
            initPostGuiJjDian();
        } else {
            ToastUtils.showShort(MainRunJiuPianActivity.this, "保存失败,数据未达到保存条件");
        }
    }


    /**
     * 将坐标点数组转换成json数组然后以字符串得形式上传
     */
    private void initToJson() {
        JSONArray jsonArray = new JSONArray();
        JSONObject tmpObj = null;
        int count = pointsListAll.size();
        for (int i = 0; i < count; i++) {
            tmpObj = new JSONObject();
            try {
                tmpObj.put("lat", pointsListAll.get(i).latitude);
                tmpObj.put("lon", pointsListAll.get(i).longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(tmpObj);
            tmpObj = null;
        }
        personInfos = jsonArray.toString(); // 将JSONArray转换得到String
    }


    ArrayList<LatLng> pointsListAll = new ArrayList<>();


    /**
     * 记录轨迹点pointsListAll用于上传
     */
    private void drawPath(LatLng point) {
        mPolyoptions.add(point);
        TraceLocation location = new TraceLocation();
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);
        mTracelocationlist.add(location);
        pointsListAll.add(point);
    }


    private Chronometer time;

    @OnClick(R.id.btn_start)
    void toStart() {
        if (GpsTool.isOPen(MainRunJiuPianActivity.this)) {
            initStart();
        } else {
            initStartGps();
        }

    }

    private void initNowShuJu() {
        chaNow = 0;
        tv_km.setText("0.0");
        tv_kcal.setText("0.0");
        tv_km_m_s.setText("00'00''");
    }

    private void maipClear() {
        preLoc = null;
        initpolyline();
        myLoc = null;
        if (aMap != null) {
            aMap.clear();
        }
        if (pointsListAll != null) {
            pointsListAll.clear();
        }
        if (mTracelocationlist != null) {
            mTracelocationlist.clear();
        }
        personInfos = "";
    }

    private void getTimeStart() {
        if (time != null) {
            time.setBase(SystemClock.elapsedRealtime());//复位计时器，停止计时
            time.start();
        }
    }


    @OnClick(R.id.btn_end)
    void toEnd() {
        initIsEnd();

    }

    private void initEnd() {
        if (time != null) {
            time.stop();
        }
        MoudleUtils.viewShow(btn_start);
        MoudleUtils.viewInvisibily(btn_end);
        initStopDw();
        initEnd(myLoc);

    }

    private void initEnd(LatLng latLng) {
        if (latLng != null) {
            initBj(latLng, R.drawable.end);//对当前用户位置进行标记
        }
    }

    AlertDialog.Builder builder;
    AlertDialog alert;

    private void initToJiuPian() {
        builder = new AlertDialog.Builder(MainRunJiuPianActivity.this);
        builder.setTitle("是否进行轨迹纠偏");
        builder.setMessage("显示纠偏轨迹");


        builder.setPositiveButton("进行纠偏", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initEndPointJiuPian();
            }
        });
        builder.setNegativeButton("还是算了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(true);
        alert = builder.create();
        if (!alert.isShowing()) {
            alert.show();
        }
    }

    /**
     * 进行轨迹纠偏
     */
    private void initEndPointJiuPian() {
        if (mTracelocationlist.size() > n - 1 && diatanceKcal > 0) {
            trace();
        } else {
            ToastUtils.showShort(MainRunJiuPianActivity.this, "不能进行纠偏，数据未达到纠偏条件！");
        }
    }

    private int getChronometerSeconds(Chronometer cmt) {
        int totalss = 0;
        String string = cmt.getText().toString();
        if (string.length() == 7) {

            String[] split = string.split(":");
            String string2 = split[0];
            int hour = Integer.parseInt(string2);
            int Hours = hour * 3600;
            String string3 = split[1];
            int min = Integer.parseInt(string3);
            int Mins = min * 60;
            int SS = Integer.parseInt(split[2]);
            totalss = Hours + Mins + SS;
            return totalss;
        } else if (string.length() == 5) {

            String[] split = string.split(":");
            String string3 = split[0];
            int min = Integer.parseInt(string3);
            int Mins = min * 60;
            int SS = Integer.parseInt(split[1]);

            totalss = Mins + SS;
            return totalss;
        }

        return totalss;


    }    // 浏览路线节点相关


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (mListener != null && amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                nowTime = System.currentTimeMillis();
                LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                initDwStartData(amapLocation, latLng);
                initAddLoc(amapLocation, latLng);

            }
        }
    }

    private long beforTime;
    private long nowTime;

    private void initAddLoc(AMapLocation amapLocation, LatLng latLng) {
        if (preLoc == null) {
            initAddData(amapLocation, latLng);
            preLoc = latLng;
            redrawline();
        } else if (preLoc != latLng) {
            initKmKcaTime(amapLocation, preLoc, latLng);

        }
    }

    private float chaNow;

    private void initKmKcaTime(AMapLocation amapLocation, LatLng mypreLoc, LatLng latLng) {
        float distance = AMapUtils.calculateLineDistance(preLoc, latLng);
        long mcha = (nowTime - beforTime) / 1000 * 10;
        if (mcha <= 0) {
            mcha = 10;
        }
//        Log.i(AppConfig.TAG, distance + "," + "mcha:" + mcha);
        if (distance <= mcha) {
            chaNow = chaNow + distance;
            setDistance(chaNow);
            setExpand();
            initShiSu();
            initAddData(amapLocation, mypreLoc);
            initAddData(amapLocation, latLng);//新加入轨迹点
            preLoc = latLng;
            redrawline();
        }
    }

    private void initDwStartData(AMapLocation amapLocation, LatLng latLng) {
        if (myLoc == null && aMap != null) {
            mListener.onLocationChanged(amapLocation);
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 16, 30, 0)));
            initSratDreable(latLng);
        }
    }

    private void initAddData(AMapLocation amapLocation, LatLng latLng) {
        beforTime = System.currentTimeMillis();
        mListener.onLocationChanged(amapLocation);
        myLoc = latLng;
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        drawPath(latLng);
    }


    private void initpolyline() {
        mPolyoptions = null;
        mpolyline = null;
        mPolyoptions = new PolylineOptions();
        mPolyoptions.width(10f);
        mPolyoptions.color(Color.GRAY);

    }

    /**
     * 实时轨迹画线
     */
    private void redrawline() {
        if (mPolyoptions.getPoints().size() > 1 && diatanceKcal > 0) {
            if (mpolyline != null) {
                mpolyline.setPoints(mPolyoptions.getPoints());
            } else {
                mpolyline = aMap.addPolyline(mPolyoptions);
            }
        }
    }


    /**
     * 上传距离
     */

    /**
     * 更新距离
     *
     * @param diatance
     */
    private void setDistance(float diatance) {
        diatanceM = diatance;
        diatanceKcal = Float.valueOf(MoudleUtils.roundDouble(diatance / 1000, 2));
        mileage = diatanceM + "";
        if (tv_km != null) {
            tv_km.setText(diatanceKcal + "");
        }
    }


    /**
     * 更新消耗
     */
    private void setExpand() {
        float weight = 0;
        weight = (float) SPUtils.get("weight", weight);
        expand = (float) (weight * 1.036 * diatanceKcal);
        expand = (float) Math.round(expand * 100) / 100;
        int f = (int) expand;
        if (expand - f >= 0.5) {
            f = f + 1;
        }
        if (tv_kcal != null) {
            tv_kcal.setText(f + "");
        }
        fat = f + "";
    }

    private void trace() {
        LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());
        mTraceClient.queryProcessedTrace(1, mTracelocationlist, LBSTraceClient.TYPE_AMAP, this);
    }

    /**
     * 如果你在标准跑道上跑一圈（400米）需要3分钟的时间，那么你每跑一千米就需要7分30秒的时间。这也就是你的配速7m30s。
     */
    private void initShiSu() {
        if (diatanceKcal > 0) {
            int time_s = getChronometerSeconds(time);
            timeLine = time_s + "";
//            LogUtils.d("timeLine", timeLine);
            int ms = (int) (1000 * time_s / diatanceM);
            int n_ms_s = ms / 60;
            int n_ms = (int) (ms % 60);
            speed = n_ms_s + "'" + n_ms + "''";
            tv_km_m_s.setText(speed);
        } else if (diatanceKcal == 0) {
            tv_km_m_s.setText("00'00''");
            speed = "00'" + "00''";

        }
    }


    private void initSratDreable(LatLng ll) {
        if (myLoc == null) {
            initBj(ll, R.drawable.start);
        }
    }

    private MarkerOptions markerOption;

    private void initBj(LatLng latlng, int icon_st) {
        /**
         * 对当前用户位置进行标记
         */
        markerOption = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(icon_st))
                .position(latlng)
                .draggable(true);
        aMap.addMarker(markerOption);
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        initStopDw();
        time.stop();
    }


    @Override
    public void onRequestFailed(int i, String s) {
    }

    @Override
    public void onTraceProcessing(int i, int i1, List<LatLng> list) {

    }

    /**
     * 轨迹纠偏成功回调。
     *
     * @param i          纠偏的线路ID
     * @param linepoints 纠偏结果
     * @param i1         总距离
     * @param i2         等待时间
     */
    @Override
    public void onFinished(int i, List<LatLng> linepoints, int i1, int i2) {
        if (linepoints != null && linepoints.size() > 0) {
            aMap.addPolyline(new PolylineOptions()
                    .setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.grasp_trace_line))
                    .width(40).addAll(linepoints));
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }
}

