package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.ProvinceBean;
import com.ckjs.ck.Bean.UpdateAdressBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.JsonFileReader;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class UpdAdressActivity extends AppCompatActivity implements View.OnClickListener {


    OptionsPickerView pvOptions;
    //  省份
    ArrayList<ProvinceBean> provinceBeanList = new ArrayList<>();
    //  城市
    ArrayList<String> cities;
    ArrayList<List<String>> cityList = new ArrayList<>();
    //  区/县
    ArrayList<String> district;
    ArrayList<List<String>> districts;
    ArrayList<List<List<String>>> districtList = new ArrayList<>();


    private Toolbar toolbar;
    private TextView button;
    private EditText name;
    private EditText tel;
    private EditText detail;
    private LinearLayout selectCity;
    private TextView tvcity;
    private String address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_updadress);
        initId();
        initToolbar();
        initData();
    }

    private void initData() {
        //  创建选项选择器
        pvOptions = new OptionsPickerView(this);

        //  获取json数据
        String province_data_json = JsonFileReader.getJson(this, "province_data.json");
        //  解析json数据
        parseJson(province_data_json);

        //  设置三级联动效果
        pvOptions.setPicker(provinceBeanList, cityList, districtList, true);

        //  设置是否循环滚动
        pvOptions.setCyclic(false, false, false);


        // 设置默认选中的三级项目
        pvOptions.setSelectOptions(0, 0, 0);
        //  监听确定选择按钮
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String city = provinceBeanList.get(options1).getPickerViewText();

                //  如果是直辖市或者特别行政区只设置市和区/县
                if ("北京市".equals(city) || "上海市".equals(city) || "天津市".equals(city) || "重庆市".equals(city) || "澳门".equals(city) || "香港".equals(city)) {
                    address = provinceBeanList.get(options1).getPickerViewText()
                            + " " + districtList.get(options1).get(option2).get(options3);
                } else {
                    address = provinceBeanList.get(options1).getPickerViewText()
                            + " " + cityList.get(options1).get(option2)
                            + " " + districtList.get(options1).get(option2).get(options3);
                }
                tvcity.setText(address);
            }
        });
    }

    private void initId() {
        tvcity = (TextView) findViewById(R.id.shop_city);
        selectCity = (LinearLayout) findViewById(R.id.shop_select_city);
        name = (EditText) findViewById(R.id.upd_shop_name);
        tel = (EditText) findViewById(R.id.upd_shop_tel);
        detail = (EditText) findViewById(R.id.upd_shop_detail);

        dialog = new ProgressDialog(this);
        selectCity.setOnClickListener(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("编辑收货地址");
        setSupportActionBar(toolbar);

        button = (TextView) findViewById(R.id.toolbar_button);
        button.setText("保存");
        button.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_button:
                toNext();
                break;
            case R.id.shop_select_city:
                KeyBoardUtils.closeKeyboard(name, UpdAdressActivity.this);
                pvOptions.show();
                break;
        }
    }

    private void toNext() {
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            ToastUtils.showShort(this, "收货人不能为空");
            return;
        } else if (DataUtils.containsEmoji(name.getText().toString().trim())) {
            ToastUtils.showShort(this, "不支持输入Emoji表情符号");
            return;
        } else if (isMobile(tel.getText().toString().trim())) {
            ToastUtils.showShort(this, "手机号不正确");
            return;
        } else if (TextUtils.isEmpty(detail.getText().toString().trim())) {
            ToastUtils.showShort(this, "请填写详细地址");
            return;
        } else if (DataUtils.containsEmoji(detail.getText().toString().trim())) {
            ToastUtils.showShort(this, "不支持输入Emoji表情符号");
            return;
        } else {
            initToInfoTask();
        }
    }

    /**
     * 验证是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }

    private ProgressDialog dialog;

    private void initToInfoTask() {
        MoudleUtils.dialogShow(dialog);
        WfApi userInfoApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");

        //TODO
        Call<UpdateAdressBean> callBack = userInfoApi.updadress((int) SPUtils.get(this, "user_id", 0), token, address + detail.getText().toString().trim()
                , tel.getText().toString().trim(), name.getText().toString().trim());

        callBack.enqueue(new Callback<UpdateAdressBean>() {
            @Override
            public void onResponse(Call<UpdateAdressBean> call, Response<UpdateAdressBean> response) {
                UpdateAdressBean userinfobean = response.body();
                toLoginFinishNext(userinfobean);//登录成功后的操作
//                ToastUtils.showLong(UserInfoActivity.this,userinfobean.getStatus()+userinfobean.getMsg()+userinfobean.getInfo());
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<UpdateAdressBean> call, Throwable t) {

                MoudleUtils.toChekWifi(UpdAdressActivity.this);
                MoudleUtils.dialogDismiss(dialog);


            }
        });

    }

    private void toLoginFinishNext(UpdateAdressBean userinfobean) {
        if (userinfobean != null) {
            if (userinfobean.getStatus().equals("1")) {
                ToastUtils.show(UpdAdressActivity.this, userinfobean.getMsg(), 0);
                KeyBoardUtils.closeKeyboard(name, UpdAdressActivity.this);
                finish();
            } else if (userinfobean.getStatus().equals("0")) {
                ToastUtils.show(UpdAdressActivity.this, userinfobean.getMsg(), 0);
            } else if (userinfobean.getStatus().equals("2")) {
                MoudleUtils.initStatusTwo(UpdAdressActivity.this, true);
            } else {
                ToastUtils.show(UpdAdressActivity.this, userinfobean.getMsg(), 0);
            }
        } else {

        }
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

    /**
     * toolbar返回键设置
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
