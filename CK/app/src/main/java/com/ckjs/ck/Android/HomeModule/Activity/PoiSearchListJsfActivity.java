package com.ckjs.ck.Android.HomeModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bigkoo.pickerview.OptionsPickerView;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.GymlistBean;
import com.ckjs.ck.Bean.ProvinceBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.JsonFileReader;
import com.ckjs.ck.Tool.Location.Utils;
import com.ckjs.ck.Tool.Location.route.Activity.RoutePlanActivity;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

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
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

/**
 * 演示poi搜索功能
 */
public class PoiSearchListJsfActivity extends FragmentActivity implements AMapLocationListener,
        AMap.OnMapLoadedListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, AMap.OnInfoWindowClickListener {
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.btn_sure)
    TextView btn_sure;
    @BindView(R.id.sd_fh)
    SimpleDraweeView sd_fh;


    private AMap aMap;
    private MapView mapView;
    //    private LatLng center;
    private int radius = 5000;
    private AMapLocationClient mlocationClient;
    private String startLat = "";
    private String startLon = "";
    private String startNodeStr = "";
    private String endNodeStr = "";
    private AMapLocation mylocation;
    private boolean isFirstLoc = true;
    private String mycity = "";
    private OptionsPickerView pvOptions;
    //  省份
    ArrayList<ProvinceBean> provinceBeanList = new ArrayList<>();
    //  城市
    ArrayList<String> cities;
    ArrayList<List<String>> cityList = new ArrayList<>();
    //  区/县
    ArrayList<String> district;
    ArrayList<List<String>> districts;
    ArrayList<List<List<String>>> districtList = new ArrayList<>();
    private TextView textView;
    private TextView textViewName;
    private TextView textViewCal;
    private SimpleDraweeView button;
    private String adr;
    private String tel;
    private int type;
    private LinearLayout ll_share;

    //选择城市
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poisearch_list);
        ll_share = (LinearLayout) findViewById(R.id.ll_share);
        initGetType();
        ButterKnife.bind(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        // 定位初始化
        initDw();
        initData();
        initShowCity();
        initSure();
    }

    /**
     * 0：默认；1：共享手环；
     */
    private void initGetType() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        //关于地图底部提示的显示隐藏 0 隐藏 1 显示
        isShow();
    }

    private void isShow() {
        if (type==0){
            MoudleUtils.viewInvisibily(ll_share);
        }else if (type==1){
            MoudleUtils.viewShow(ll_share);
        }
    }

    @OnClick(R.id.sd_fh)
    void sdFh() {
        finish();
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

    private void setUpMap() {
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置marker可拖拽事件监听器
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

    private void initShowCity() {
        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pvOptions != null) {
                    if (!pvOptions.isShowing()) {
                        pvOptions.show();
                    }
                }
            }
        });
    }

    //  解析json填充集合
    public void parseJson(String str) {
        try {
            //  获取json中的数组
            JSONArray jsonArray = new JSONArray(str);
            //  遍历数据组
            for (int i = 0; i < jsonArray.length(); i++) {
                //  获取省份的对象
                JSONObject provinceObject = jsonArray.optJSONObject(i);
                //  获取省份名称放入集合
                String provinceName = provinceObject.getString("name");
                provinceBeanList.add(new ProvinceBean(provinceName));
                //  获取城市数组
                JSONArray cityArray = provinceObject.optJSONArray("city");
                cities = new ArrayList<>();//   声明存放城市的集合
                districts = new ArrayList<>();//声明存放区县集合的集合
                //  遍历城市数组
                for (int j = 0; j < cityArray.length(); j++) {
                    //  获取城市对象
                    JSONObject cityObject = cityArray.optJSONObject(j);
                    //  将城市放入集合
                    String cityName = cityObject.optString("name");
                    cities.add(cityName);
                    district = new ArrayList<>();// 声明存放区县的集合
                    //  获取区县的数组
                    JSONArray areaArray = cityObject.optJSONArray("area");
                    //  遍历区县数组，获取到区县名称并放入集合
                    for (int k = 0; k < areaArray.length(); k++) {
                        String areaName = areaArray.getString(k);
                        district.add(areaName);
                    }
                    //  将区县的集合放入集合
                    districts.add(district);
                }
                //  将存放区县集合的集合放入集合
                districtList.add(districts);
                //  将存放城市的集合放入集合
                cityList.add(cities);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        //  创建选项选择器
        pvOptions = new OptionsPickerView(this);

        //  获取json数据
        String province_data_json = JsonFileReader.getJson(this, "province_data.json");
        //  解析json数据
        parseJson(province_data_json);

        //  设置三级联动效果
        pvOptions.setPicker(provinceBeanList, cityList, true);

        //  设置是否循环滚动
        pvOptions.setCyclic(false, false, false);


        // 设置默认选中的三级项目
        pvOptions.setSelectOptions(0, 0);
        //  监听确定选择按钮
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String city = provinceBeanList.get(options1).getPickerViewText();
                String address = "";
                //  如果是直辖市或者特别行政区只设置市和区/县
                if ("北京市".equals(city) || "上海市".equals(city) || "天津市".equals(city) || "重庆市".equals(city) || "澳门".equals(city) || "香港".equals(city)) {
                    address = provinceBeanList.get(options1).getPickerViewText();
                } else {
                    address = cityList.get(options1).get(option2);
                }
                s_city = address;
                tv_city.setText(s_city);
            }
        });
    }

    String s_city;

    /**
     * type0：默认；1：共享手环；
     */
    private void initSure() {
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s_city != null) {
                    if (s_city.equals("")) {
                        ToastUtils.showShortNotInternet("请选择城市");
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("city", s_city);
                        intent.putExtra("type", type);
                        intent.setClass(PoiSearchListJsfActivity.this, HomeCityJsfListActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * type0：默认；1：共享手环；
     *
     * @param latitude
     * @param longitude
     */
    private void initTaskFjjsfList(double latitude, double longitude) {
        Call<GymlistBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).gymlist(latitude + "", longitude + "", radius + "", type + "");
        getSignBeanCall.enqueue(new Callback<GymlistBean>() {
            @Override
            public void onResponse(Call<GymlistBean> call, Response<GymlistBean> response) {
                GymlistBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {
                            if (aMap != null) {
                                try {
                                    initPoiJsf(bean.getInfo());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        ToastUtils.showShort(PoiSearchListJsfActivity.this, bean.getMsg());
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<GymlistBean> call, Throwable t) {
                MoudleUtils.toChekWifi(PoiSearchListJsfActivity.this);
            }
        });

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
        initStartDw();
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

    private LatLng pt;

    private void initInfor(String adr, final String endNodeStr, final LatLng pt, String tel) {
        textViewName.setText(endNodeStr);
        textViewCal.setText("电话:" + tel);
        textView.setText("地址:" + adr);
    }

    private void initToOne(String endNodeStr, LatLng pt) {
        if (!startNodeStr.equals("") && !endNodeStr.equals("") & !mycity.equals("")) {
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
            ToastUtils.showShort(getApplicationContext(), "还未能获取您的当前位置,无法进行线路规\n" +
                    "请稍后");
        }
    }

    private MarkerOptions markerOption;
    private Marker marker;

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

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                s_city = amapLocation.getCity();
                startNodeStr = amapLocation.getAddress();
                mycity = amapLocation.getCity();
                startLat = amapLocation.getLatitude() + "";
                startLon = amapLocation.getLongitude() + "";
                mylocation = amapLocation;
                if (isFirstLoc) {
                    isFirstLoc = false;
                    /**
                     * 标记超空推荐的超空门店
                     */
                    initTaskFjjsfList(amapLocation.getLatitude(), amapLocation.getLongitude());
                }
                //
            } else {
                s_city = "北京市";
                ToastUtils.showShortNotInternet("定位失败，无法获取附近健身房信息");

            }
        }

    }

    List<GymlistBean.GymlistBeanInfo> listLatNew = new ArrayList<>();

    private void initPoiJsf(final List<GymlistBean.GymlistBeanInfo> listLat) {
        listLatNew = listLat;
        /**
         * 对指定健身房进行标记
         */
        if (listLat.size() == 0) {
            ToastUtils.showShort(this, "附近无超空推荐的‘超空门店’");
        } else if (listLat.size() > 0) {
            initOneLon(listLat);
            for (int i = 0; i < listLat.size(); i++) {
                final LatLng ll_jsf = new LatLng(Double.valueOf(listLat.get(i).getLat()), Double.valueOf(listLat.get(i).getLon()));
                /**
                 * 标记健身房的点
                 */
                markerOption = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin))
                        .position(ll_jsf)
                        .draggable(true);
                aMap.addMarker(markerOption);

            }

        }
    }

    private void initOneLon(List<GymlistBean.GymlistBeanInfo> listLat) {
        LatLng ll = new LatLng(Double.parseDouble(listLat.get(0).getLat()), Double.parseDouble(listLat.get(0).getLon()));
        /**
         * 对第一1个位置进行提醒
         */

        endNodeStr = listLat.get(0).getName();
        if (aMap != null) {
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(ll, 11, 30, 0)));
        }
        initToPoiClick(listLat.get(0).getPlace(), endNodeStr, ll, listLat.get(0).getTel());

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        LatLng markerLatLng = marker.getPosition();
        String markerLat = markerLatLng.latitude + "";
        for (int i = 0; i < listLatNew.size(); i++) {
            if (markerLat.equals(listLatNew.get(i).getLat())) {
                LatLng latLng = new LatLng(Double.valueOf(listLatNew.get(i).getLat()), Double.valueOf(listLatNew.get(i).getLon()));
                endNodeStr = listLatNew.get(i).getName();
                initToPoiClick(listLatNew.get(i).getPlace(), endNodeStr, latLng, listLatNew.get(i).getTel());
            }

        }
        return true;
    }

    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        if (listLatNew != null) {
            for (int i = 0; i < listLatNew.size(); i++) {
                LatLng latLng = new LatLng(Double.valueOf(listLatNew.get(i).getLat()), Double.valueOf(listLatNew.get(i).getLon()));
                bounds.include(latLng);
                LatLngBounds bound = bounds.build();
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound, 200));
            }
        }
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
}
