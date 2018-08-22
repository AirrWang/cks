package com.ckjs.ck.Android.HomeModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.ckjs.ck.Android.HomeModule.Adapter.ListAdapterCityJsfList;
import com.ckjs.ck.Android.HomeModule.Adapter.ListAdapterCityJsfSjList;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.CitygymBean;
import com.ckjs.ck.Bean.CoachBean;
import com.ckjs.ck.Bean.ProvinceBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.JsonFileReader;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.Location.Utils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
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
public class CkSjActivity extends AppCompatActivity implements
        AMapLocationListener {
    @BindView(R.id.tv_jsf)
    TextView tv_jsf;
    @BindView(R.id.ll_left)
    LinearLayout ll_left;
    @BindView(R.id.ll_right)
    LinearLayout ll_right;
    @BindView(R.id.jsf)
    TextView jsf;
    @BindView(R.id.people)
    TextView people;
    @BindView(R.id.near)
    TextView near;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.btn_sure)
    Button btn_sure;
    @BindView(R.id.lv_home_jsf_sj_list)
    ListView lv_home_jsf_sj_list;
    @BindView(R.id.sd_fh)
    SimpleDraweeView sd_fh;

    private boolean flagLeft;
    private boolean flagLeftjsf, flagLeftPeople;
    private String name = "1";//1：机构；2：个人
    private String nameTwo = "shouhuan_serch";//shouhuan_serch:附近 b:城市
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
    private ListAdapterCityJsfSjList listAdapterYuYue;

    private double lat;
    private double lon;
    private String gym_id = "";
    private String nameJSf = "";
    private AMapLocationClient mlocationClient;
    private String tv_city_name = "北京市";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ck_sj);
        ButterKnife.bind(this);
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        // 定位初始化
        initDw();
        initData();
        initShowCity();
        initSure();
        initSet();

    }

    @OnClick(R.id.sd_fh)
    void sdFh() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initStartDw();
    }

    private void initSet() {

        lv_home_jsf_sj_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listAdapterYuYue != null) {
                    if (list != null) {
                        if (list.size() > 0) {
                            if (list.get(position).getStatus() != null) {
                                if (list.get(position).getStatus().equals("1")) {
                                    String token = (String) SPUtils.get("token", "");
                                    if (token.equals("")) {
                                        MoudleUtils.initToLogin(CkSjActivity.this);
                                    } else {
                                        try {
                                            initToBd(position);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }
                        }
                    }
                } else if (listAdapterJsf != null) {


                    if (listJsf != null) {
                        if (listJsf.size() > 0) {
                            gym_id = listJsf.get(position).getId();
                            nameJSf = listJsf.get(position).getName();
                            String yy = listJsf.get(position).getOpen();
                            if (yy == null || yy.equals("") || yy.equals("0"))//机构搜索下没有私教的超空门店变灰并且无法点击
                                return;
                            if (gym_id != null) {
                                if (!gym_id.equals("")) {
                                    initToSjList();
                                }
                            }
                        }
                    }

                }
            }
        });

    }

    private void initToBd(int position) {
        if ((SPUtils.get("vip", "").equals("1"))) {
            if ((SPUtils.get("type", "") + "").equals("1")) {
                startActivity(new Intent().setClass(this, RealNameYsActivity.class));
            } else {
                startActivity(new Intent().setClass(this, RealNameActivity.class));
            }
        } else {
            initToTIjOrder(position);
        }
    }

    private void initToTIjOrder(int position) {
        Intent intent = new Intent();
        if (list.size() == 0) {
            sj_name = "提交订单";
            sijiao_id = "";
        }
        if (list.size() > 0) {
            sj_name = list.get(position).getName();
            sijiao_id = list.get(position).getId();
            if (sj_name == null) {
                sj_name = "提交订单";
            }
            if (sijiao_id == null) {
                sijiao_id = "";
            }
        }
        intent.putExtra("name", sj_name);
        intent.putExtra("sijiao_id", sijiao_id);
        intent.setClass(CkSjActivity.this, SiJiaoYuYueActivity.class);
        startActivity(intent);
    }

    private void initToSjList() {
        Intent intent = new Intent();
        intent.putExtra("gym_id", gym_id);
        intent.putExtra("name", nameJSf);
        intent.setClass(CkSjActivity.this, HomeJsfSjListSjActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        initStopDw();
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

    /**
     * 定位SDK监听函数
     */
    private AMapLocation mylocation;

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                tv_city_name = amapLocation.getCity();
                lat = amapLocation.getLatitude();
                lon = amapLocation.getLongitude();
                if (mylocation == null) {
                    if (listAdapterYuYue == null && listAdapterJsf == null) {
                        initTaskFjJspList();
                    }
                }
                mylocation = amapLocation;
            } else {
                ToastUtils.showShortNotInternet("定位失败，无法获取附近健身房信息");
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                tv_city_name = "北京市";
            }
        }

    }


    @OnClick(R.id.tv_jsf)
    void tvJsf() {
        if (!flagLeft) {
            MoudleUtils.viewShow(ll_left);
            flagLeft = true;
        } else {
            initGoneXuanZe();
        }
    }


    private void initGoneXuanZe() {
        MoudleUtils.viewGone(ll_left);
        MoudleUtils.viewGone(ll_right);
        initFjCityClear(jsf, people);
        initFjCityClear(near, city);
        flagLeft = false;
        flagLeftjsf = false;
        flagLeftPeople = false;
    }

    private void initShowCity() {
        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameTwo.equals("b")) {
                    if (pvOptions != null) {
                        if (!pvOptions.isShowing()) {
                            pvOptions.show();
                        }
                    }
                } else {
                    //                    ToastUtils.showShortNotInternet("未选择为城市搜索");
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
                tv_city.setText(address);
            }
        });
    }

    KyLoadingBuilder kyLoadingBuilder;
    String s_city = "";

    private void initSure() {
        btn_sure.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (name.equals("2")) {
                                                if (nameTwo.equals("b")) {
                                                    if (tv_city != null) {
                                                        s_city = tv_city.getText().toString().trim();
                                                        if (s_city != null) {
                                                            if (s_city.equals("")) {
                                                                ToastUtils.showShortNotInternet("请选择城市");
                                                            } else {
                                                                kyLoadingBuilder = new KyLoadingBuilder(CkSjActivity.this);
                                                                initTaskCityList();
                                                                initGoneXuanZe();
                                                            }
                                                        }
                                                    }
                                                } else if (nameTwo.equals("shouhuan_serch")) {
                                                    if ((lon + "").equals("") && (lat + "").equals("")) {
                                                        ToastUtils.showShortNotInternet("定位失败啦");
                                                    } else {
                                                        kyLoadingBuilder = new KyLoadingBuilder(CkSjActivity.this);
                                                        initTaskNearList();
                                                        initGoneXuanZe();
                                                        intCityNull();
                                                    }
                                                } else {
                                                    ToastUtils.showShortNotInternet("请选择‘附近搜索’或‘城市搜索’");
                                                }
                                            } else if (name.equals("1")) {
                                                if (nameTwo.equals("shouhuan_serch")) {
                                                    if ((lat + "").equals("") && (lon + "").equals("")) {
                                                        ToastUtils.showShortNotInternet("定位失败啦");
                                                    } else {
                                                        kyLoadingBuilder = new KyLoadingBuilder(CkSjActivity.this);
                                                        MoudleUtils.kyloadingShow(kyLoadingBuilder);
                                                        initTaskFjJspList();
                                                        initGoneXuanZe();
                                                        intCityNull();
                                                    }
                                                } else if (nameTwo.equals("b")) {
                                                    if (tv_city != null) {
                                                        s_city = tv_city.getText().toString().trim();
                                                        if (s_city != null) {
                                                            if (s_city.equals("")) {
                                                                ToastUtils.showShortNotInternet("请选择城市");
                                                            } else {
                                                                if ((lat + "").equals("") && (lon + "").equals("")) {
                                                                    ToastUtils.showShortNotInternet("定位失败啦");
                                                                } else {
                                                                    kyLoadingBuilder = new KyLoadingBuilder(CkSjActivity.this);
                                                                    MoudleUtils.kyloadingShow(kyLoadingBuilder);
                                                                    initTaskJspList();
                                                                    initGoneXuanZe();
                                                                }

                                                            }
                                                        }
                                                    }
                                                } else {
                                                    ToastUtils.showShortNotInternet("请选择‘附近搜索’或‘城市搜索’");
                                                }
                                            } else {
                                                ToastUtils.showShortNotInternet("请选择筛选条件哦");
                                            }
                                        }
                                    }

        );
    }


    private ListAdapterCityJsfList listAdapterJsf;

    /**
     *
     */
    private Call<CitygymBean> getSignBeanCallFjJsp;

    private void initTaskFjJspList() {


        getSignBeanCallFjJsp = RetrofitUtils.retrofit.create(NpApi.class).gymlistJsf(lon + "", lat + "", "5000");
        if (!getSignBeanCallFjJsp.isExecuted()) {
            getSignBeanCallFjJsp.enqueue(new Callback<CitygymBean>() {
                @Override
                public void onResponse(Call<CitygymBean> call, Response<CitygymBean> response) {
                    CitygymBean bean = response.body();
                    initToTaskData(bean);
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }

                @Override
                public void onFailure(Call<CitygymBean> call, Throwable t) {
                    MoudleUtils.toChekWifi();
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }
            });
        }
    }

    /**
     * type0：默认；1：共享手环；
     */

    private void initTaskJspList() {

        Call<CitygymBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).citygymFocus(lat + "", lon + "", s_city, "0");
        getSignBeanCall.enqueue(new Callback<CitygymBean>() {
            @Override
            public void onResponse(Call<CitygymBean> call, Response<CitygymBean> response) {
                CitygymBean bean = response.body();
                initToTaskData(bean);
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }

            @Override
            public void onFailure(Call<CitygymBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });

    }

    /**
     * 进行listview数据绑定    //type 1超空门店 2超空私教 使用ListAdapterCityJsfList
     */
    private void initAdatepter(CitygymBean bean) {
        if (listAdapterJsf == null) {
            listAdapterJsf = new ListAdapterCityJsfList(this, "2");

            listAdapterJsf.setList(bean.getInfo());
            lv_home_jsf_sj_list.setAdapter(listAdapterJsf);
        } else {
            listAdapterJsf.setList(bean.getInfo());
            listAdapterJsf.notifyDataSetChanged();
        }
        listAdapterYuYue = null;
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
                        listJsf = bean.getInfo();
                        initAdatepter(bean);
                        if (bean.getInfo().size() == 0) {
                            ToastUtils.showShort(CkSjActivity.this, bean.getMsg());
                        }
                    }
                }

            } else if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(CkSjActivity.this, bean.getMsg());
            } else if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(CkSjActivity.this, bean.getMsg());
            }
        }

    }

    private void initTaskNearList() {
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        Call<CoachBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).nearcoach(lon + "", lat + "", "5000");
        getSignBeanCall.enqueue(new Callback<CoachBean>() {
            @Override
            public void onResponse(Call<CoachBean> call, Response<CoachBean> response) {
                CoachBean bean = response.body();
                initToTaskData(bean);
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }

            @Override
            public void onFailure(Call<CoachBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });
    }

    private void initTaskCityList() {
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        Call<CoachBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).citycoach(s_city);
        getSignBeanCall.enqueue(new Callback<CoachBean>() {
            @Override
            public void onResponse(Call<CoachBean> call, Response<CoachBean> response) {
                CoachBean bean = response.body();
                initToTaskData(bean);
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }

            @Override
            public void onFailure(Call<CoachBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });
    }

    /**
     * 处理金牌教练列表接口获取的数据
     *
     * @param bean
     */
    List<CoachBean.CoachBeanInfo> list;
    List<CitygymBean.CitygymBeanInfo> listJsf;
    String sj_name = "";
    String sijiao_id = "";

    private void initToTaskData(final CoachBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                list = bean.getInfo();
                if (list != null) {
                    initAdatepter(list);
                    if (list.size() == 0) {
                        ToastUtils.showShort(CkSjActivity.this, bean.getMsg());
                    }

                }

            } else if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(CkSjActivity.this, bean.getMsg());
            } else if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(CkSjActivity.this, bean.getMsg());
            }
        }
    }

    /**
     * 进行listView数据绑定
     *
     * @param list
     */
    private void initAdatepter(List<CoachBean.CoachBeanInfo> list) {
        if (listAdapterYuYue == null) {
            listAdapterYuYue = new ListAdapterCityJsfSjList(this);
            listAdapterYuYue.setList(this.list);
            lv_home_jsf_sj_list.setAdapter(listAdapterYuYue);
        } else {
            listAdapterYuYue.setList(this.list);
            listAdapterYuYue.notifyDataSetChanged();
        }
        listAdapterJsf = null;

    }


    @OnClick(R.id.jsf)
    void jsf() {
        if (!flagLeftjsf) {
            name = "1";
            initTurnSai(jsf, R.color.c_90c33, R.color.c_ffffff);//选中变色
            initTurnSai(people, R.color.c_1ababcbc, R.color.c_33);//未选中变色
            MoudleUtils.viewShow(ll_right);
            flagLeftjsf = true;
            flagLeftPeople = false;
            tv_jsf.setText("按机构");
        } else if (!flagLeftPeople) {
            initFjCityClear(city, near);
            initTurnSai(jsf, R.color.c_1ababcbc, R.color.c_33);
            MoudleUtils.viewGone(ll_right);
            flagLeftjsf = false;
        }
    }

    private void intCityNull() {
        tv_city.setText("");
        tv_city.setHint("附近");
    }

    @OnClick(R.id.people)
    void people() {
        if (!flagLeftPeople) {
            name = "2";
            initTurnSai(people, R.color.c_90c33, R.color.c_ffffff);
            initTurnSai(jsf, R.color.c_1ababcbc, R.color.c_33);
            MoudleUtils.viewShow(ll_right);
            flagLeftPeople = true;
            flagLeftjsf = false;
            tv_jsf.setText("按个人");
        } else if (!flagLeftjsf) {
            initFjCityClear(city, near);
            initTurnSai(people, R.color.c_1ababcbc, R.color.c_33);
            MoudleUtils.viewGone(ll_right);
            flagLeftPeople = false;
        }
    }

    private void initFjCityClear(TextView city, TextView near) {
        initTurnSai(city, R.color.c_1ababcbc, R.color.c_33);
        initTurnSai(near, R.color.c_1ababcbc, R.color.c_33);
    }

    private void initTurnSai(TextView textView, int dId, int cId) {
        textView.setBackgroundResource(dId);
        textView.setTextColor(getResources().getColor(cId));
    }

    @OnClick(R.id.near)
    void near() {
        nameTwo = "shouhuan_serch";
        initTurnSai(near, R.color.c_90c33, R.color.c_ffffff);
        initTurnSai(city, R.color.c_1ababcbc, R.color.c_33);
        intCityNull();

    }

    @OnClick(R.id.city)
    void city() {
        nameTwo = "b";
        initTurnSai(city, R.color.c_90c33, R.color.c_ffffff);
        initTurnSai(near, R.color.c_1ababcbc, R.color.c_33);
        tv_city.setHint("");
        tv_city.setText(tv_city_name);
    }
}
