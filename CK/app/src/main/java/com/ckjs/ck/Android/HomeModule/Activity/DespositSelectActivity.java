package com.ckjs.ck.Android.HomeModule.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterCitySearchList;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.CitySearchBean;
import com.ckjs.ck.Bean.ProvinceBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.JsonFileReader;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class DespositSelectActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_select_city;
    private KyLoadingBuilder builder;
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
    private SimpleDraweeView mine_search_back;
    private ListView mine_search_jsf;
    private List<CitySearchBean.CitySearchInfoBean> listAll = new ArrayList<>();
    private String citysele = "北京市";
    private EditText et_search;
    private CitySearchBean body;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_jsf);
        builder = new KyLoadingBuilder(this);
        initId();
        initData();
    }

    private void initId() {
        tv_select_city = (TextView) findViewById(R.id.tv_select_city);
        mine_search_back = (SimpleDraweeView) findViewById(R.id.mine_search_back);
        mine_search_jsf = (ListView) findViewById(R.id.mine_search_jsf);
        Button btn_search = (Button) findViewById(R.id.btn_search);
        et_search = (EditText) findViewById(R.id.et_search);
        SimpleDraweeView sd_search = (SimpleDraweeView) findViewById(R.id.sd_search);

        tv_select_city.setOnClickListener(this);
        mine_search_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        sd_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_city:
                if (pvOptions != null) {
                    if (!pvOptions.isShowing()) {
                        pvOptions.show();
                    }
                }
                break;
            case R.id.mine_search_back:
                KeyBoardUtils.closeKeyboard(et_search, DespositSelectActivity.this);
                finish();
                break;
            case R.id.btn_search:
            case R.id.sd_search:
                KeyBoardUtils.closeKeyboard(et_search, DespositSelectActivity.this);
                String s = et_search.getText().toString().trim();
                if (s != null && !s.equals("")) {
                    if (!DataUtils.containsEmoji(s)) {
                        toSearch(s);
                    } else {
                        ToastUtils.showShortNotInternet("不支持输入Emoji表情符号");
                    }
                } else {
                    ToastUtils.showShort(this, "请输入有效的搜索关键字，如'超空'");
                }
                break;
        }
    }

    /**
     * type0：默认；1：共享手环；
     *
     * @param s
     */
    private void toSearch(String s) {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<CitySearchBean> callBack = restApi.citygymsearch(citysele, s, 1 + "");

        callBack.enqueue(new Callback<CitySearchBean>() {
            @Override
            public void onResponse(Call<CitySearchBean> call, Response<CitySearchBean> response) {
                if (response.body() != null) {
                    body = response.body();
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            if (response.body().getInfo().size() == 0) {
                                ToastUtils.showShortNotInternet("暂无合作健身房！");
                            }
                            listAll.addAll(response.body().getInfo());
                            initAdapter();
                            initSet();
                        }
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(DespositSelectActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(DespositSelectActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<CitySearchBean> call, Throwable t) {
                MoudleUtils.toChekWifi(DespositSelectActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });

    }

    private void initSet() {
        mine_search_jsf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (body == null || body.getInfo() == null) {
                    return;
                }
                AppConfig.select_jsf = body.getInfo().get(position).getName();
                AppConfig.select_jsf_id = body.getInfo().get(position).getGym_id();
                finish();
            }
        });

    }

    private ListAdapterCitySearchList listAdapterYuYue;

    private void initAdapter() {
        if (listAdapterYuYue == null) {
            listAdapterYuYue = new ListAdapterCitySearchList(this);

            listAdapterYuYue.setList(body.getInfo());
            mine_search_jsf.setAdapter(listAdapterYuYue);
        } else {

            listAdapterYuYue.setList(body.getInfo());
            listAdapterYuYue.notifyDataSetChanged();
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
                citysele = address;
                tv_select_city.setText(address);
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
}
