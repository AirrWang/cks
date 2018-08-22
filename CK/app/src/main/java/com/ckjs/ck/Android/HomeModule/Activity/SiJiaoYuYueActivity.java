package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bigkoo.pickerview.OptionsPickerView;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Android.MineModule.Activity.TouXiangActivity;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.CoachOrderBean;
import com.ckjs.ck.Bean.PriCoachInfoBean;
import com.ckjs.ck.Bean.ProvinceBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.JsonFileReader;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class SiJiaoYuYueActivity extends AppCompatActivity implements View.OnClickListener {

    private String type = "distance";
    private String name = "";
    private String sijiao_id = "";
    private SimpleDraweeView sijiao_yuyue_pic;
    private TextView tv_sijiao_yuyue_manual;
    private TextView tv_sijiao_yuyue_manualp;
    private LinearLayout ll_sijiao_yuyue_star;
    private TextView tv_sijiao_yuyue_distance;
    private TextView tv_sijiao_yuyue_personal;
    private RadioButton sijiao_yuyue_shipin;
    private RadioButton sijiao_yuyue_shangmen;
    private TextView tv_sijiao_yuyue_toatal;
    private PriCoachInfoBean sijiaoBean;
    private TextView tv_sijiao_yuyue_age;
    private TextView tv_sijiao_yuyue_height;
    private TextView tv_sijiao_yuyue_weight;
    private Button btn_num_down_sijiao_shangmen;
    private TextView text_num_sijiao_shangmen;
    private Button btn_num_up_sijiao_shangmen;
    private int numkc = 1;
    private String s;
    private TextView tv_sijiao_yuyue_city;
    private EditText et_sijiao_yuyue_adress;
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
    private String address = "";
    private String num = "1";
    private TextView tv_sijiao_yuyue_data;
    private TextView tv_sijiao_yuyue_hour;
    private String data;
    private Calendar c;
    private String time;
    private SimpleDraweeView sijiao_yuyue_pic1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sijiao_yuyue);
        initId();
        dialog = new ProgressDialog(this);
        c = Calendar.getInstance();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        sijiao_id = intent.getStringExtra("sijiao_id");
        initToolbar();
        initData();
        initpvOptions();
    }

    private void initpvOptions() {
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
                tv_sijiao_yuyue_city.setText(address);
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

    /**
     * 接口获取私教简介
     */

    private void initData() {
        final KyLoadingBuilder builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);

        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<PriCoachInfoBean> callBack = restApi.pricoachinfo(sijiao_id);

        callBack.enqueue(new Callback<PriCoachInfoBean>() {
            @Override
            public void onResponse(Call<PriCoachInfoBean> call, Response<PriCoachInfoBean> response) {
                sijiaoBean = response.body();
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        initUI(sijiaoBean);
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(SiJiaoYuYueActivity.this, response.body().getMsg(), 0);
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<PriCoachInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(SiJiaoYuYueActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });

    }

    /**
     * 更新UI
     *
     * @param sijiaoBean
     */
    private void initUI(PriCoachInfoBean sijiaoBean) {
        if (sijiaoBean.getInfo() != null) {
            FrescoUtils.setImage(sijiao_yuyue_pic, AppConfig.url_jszd + sijiaoBean.getInfo().getDatu());
            tv_sijiao_yuyue_manual.setText("        " + sijiaoBean.getInfo().getIntroduce());
            tv_sijiao_yuyue_manualp.setText("        " + sijiaoBean.getInfo().getIntro());
            drawStars(sijiaoBean);
            tv_sijiao_yuyue_distance.setText("￥" + sijiaoBean.getInfo().getDistance());
            tv_sijiao_yuyue_personal.setText("￥" + sijiaoBean.getInfo().getPersonal());
            tv_sijiao_yuyue_toatal.setText("￥" + sijiaoBean.getInfo().getDistance());
            tv_sijiao_yuyue_age.setText(sijiaoBean.getInfo().getAge() + "岁");
            tv_sijiao_yuyue_height.setText(sijiaoBean.getInfo().getHeight() + "cm");
            tv_sijiao_yuyue_weight.setText(sijiaoBean.getInfo().getWeight() + "kg");
            s = sijiaoBean.getInfo().getDistance() + "";
        }
    }

    /**
     * 画星方法
     */
    private void drawStars(PriCoachInfoBean sijiaoBean) {
        for (int i = 0; i < Integer.parseInt(sijiaoBean.getInfo().getGrade()); i++) {
            SimpleDraweeView ivPoint = new SimpleDraweeView(this);
            int w = CkApplication.getInstance().getResources().getDimensionPixelOffset(R.dimen.city_jsf_xing);
            int w2 = CkApplication.getInstance().getResources().getDimensionPixelOffset(R.dimen.city_jsf_xing_m);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w, w);
            layoutParams.setMargins(0, 0, w2, w2);
            ivPoint.setLayoutParams(layoutParams);
            FrescoUtils.setImage(ivPoint, AppConfig.res + R.drawable.list_attention);
            ll_sijiao_yuyue_star.addView(ivPoint);
        }
    }

    private void initId() {

        tv_sijiao_yuyue_age = (TextView) findViewById(R.id.tv_sijiao_yuyue_age);
        tv_sijiao_yuyue_height = (TextView) findViewById(R.id.tv_sijiao_yuyue_height);
        tv_sijiao_yuyue_weight = (TextView) findViewById(R.id.tv_sijiao_yuyue_weight);
        sijiao_yuyue_pic = (SimpleDraweeView) findViewById(R.id.sijiao_yuyue_pic);
        tv_sijiao_yuyue_manual = (TextView) findViewById(R.id.tv_sijiao_yuyue_manual);
        tv_sijiao_yuyue_manualp = (TextView) findViewById(R.id.tv_sijiao_yuyue_manualP);
        ll_sijiao_yuyue_star = (LinearLayout) findViewById(R.id.ll_sijiao_yuyue_star);
        tv_sijiao_yuyue_distance = (TextView) findViewById(R.id.tv_sijiao_yuyue_distance);
        tv_sijiao_yuyue_personal = (TextView) findViewById(R.id.tv_sijiao_yuyue_personal);
        tv_sijiao_yuyue_toatal = (TextView) findViewById(R.id.tv_sijiao_yuyue_toatal);

        Button btn_sijiao_yuyue_submit = (Button) findViewById(R.id.btn_sijiao_yuyue_submit);
        btn_sijiao_yuyue_submit.setOnClickListener(this);
        sijiao_yuyue_shipin = (RadioButton) findViewById(R.id.sijiao_yuyue_shipin);
        sijiao_yuyue_shipin.setOnClickListener(this);
        sijiao_yuyue_shangmen = (RadioButton) findViewById(R.id.sijiao_yuyue_shangmen);
        sijiao_yuyue_shangmen.setOnClickListener(this);

        text_num_sijiao_shangmen = (TextView) findViewById(R.id.text_num_sijiao);
        btn_num_down_sijiao_shangmen = (Button) findViewById(R.id.btn_num_down_sijiao);
        btn_num_down_sijiao_shangmen.setOnClickListener(this);
        btn_num_down_sijiao_shangmen.setClickable(false);
        btn_num_up_sijiao_shangmen = (Button) findViewById(R.id.btn_num_up_sijiao);
        btn_num_up_sijiao_shangmen.setOnClickListener(this);

        tv_sijiao_yuyue_city = (TextView) findViewById(R.id.tv_sijiao_yuyue_city);
        tv_sijiao_yuyue_city.setOnClickListener(this);
        et_sijiao_yuyue_adress = (EditText) findViewById(R.id.et_sijiao_yuyue_adress);

        tv_sijiao_yuyue_data = (TextView) findViewById(R.id.tv_sijiao_yuyue_data);
        tv_sijiao_yuyue_data.setOnClickListener(this);
        tv_sijiao_yuyue_hour = (TextView) findViewById(R.id.tv_sijiao_yuyue_hour);
        tv_sijiao_yuyue_hour.setOnClickListener(this);

        sijiao_yuyue_pic1 = (SimpleDraweeView) findViewById(R.id.sijiao_yuyue_pic);
        sijiao_yuyue_pic.setOnClickListener(this);
    }

    /**
     * toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(name);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sijiao_yuyue_shangmen:
                type = "personal";
                if (sijiaoBean.getInfo() != null) {
                    s = MoudleUtils.roundDouble(sijiaoBean.getInfo().getPersonal() * numkc, 2);
                    tv_sijiao_yuyue_toatal.setText("合计：￥" + s);
                }
                num = numkc + "";
                break;
            case R.id.sijiao_yuyue_shipin:
                type = "distance";
                if (sijiaoBean.getInfo() != null) {
                    s = MoudleUtils.roundDouble(sijiaoBean.getInfo().getDistance() * numkc, 2);
                    tv_sijiao_yuyue_toatal.setText("合计：￥" + s);
                }
                num = numkc + "";
                break;
            case R.id.btn_sijiao_yuyue_submit:

                toSubmit();

                break;
            case R.id.btn_num_down_sijiao:
                numkc--;
                if (numkc >= 1) {
                    btn_num_down_sijiao_shangmen.setClickable(true);
                    text_num_sijiao_shangmen.setText(numkc + "");
                    if (type.equals("personal")) {
                        s = MoudleUtils.roundDouble(sijiaoBean.getInfo().getPersonal() * numkc, 2);
                    } else {
                        s = MoudleUtils.roundDouble(sijiaoBean.getInfo().getDistance() * numkc, 2);
                    }
                    tv_sijiao_yuyue_toatal.setText("合计：￥" + s);
                } else {
                    btn_num_down_sijiao_shangmen.setClickable(false);
                }
                num = numkc + "";
                break;
            case R.id.btn_num_up_sijiao:
                numkc++;
                btn_num_up_sijiao_shangmen.setClickable(true);
                text_num_sijiao_shangmen.setText(numkc + "");
                if (type.equals("personal")) {
                    s = MoudleUtils.roundDouble(sijiaoBean.getInfo().getPersonal() * numkc, 2);
                } else {
                    s = MoudleUtils.roundDouble(sijiaoBean.getInfo().getDistance() * numkc, 2);
                }
                tv_sijiao_yuyue_toatal.setText("合计：￥" + s);
                if (numkc > 1) {
                    btn_num_down_sijiao_shangmen.setClickable(true);
                }
                num = numkc + "";
                break;
            case R.id.tv_sijiao_yuyue_city:
                pvOptions.show();
                break;
            case R.id.tv_sijiao_yuyue_data:
                showData();
                break;
            case R.id.tv_sijiao_yuyue_hour:
                showHour();
                break;
            case R.id.sijiao_yuyue_pic:
                if (sijiaoBean.getInfo() != null) {
                    Intent intent = new Intent();
                    intent.putExtra("touxiang", sijiaoBean.getInfo().getDatu());
                    intent.putExtra("type", "1");
                    intent.setClass(SiJiaoYuYueActivity.this, TouXiangActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 提交判断
     */
    //TODO
    private void toSubmit() {

        if (tv_sijiao_yuyue_data.getText().toString().trim().equals("")) {
            ToastUtils.showShort(this, "请先选择上课日期");
        } else if (tv_sijiao_yuyue_hour.getText().toString().trim().equals("")) {
            ToastUtils.showShort(this, "请先选择上课时间");
        } else if (tv_sijiao_yuyue_city.getText().toString().trim().equals("")) {
            ToastUtils.showShort(this, "请先选择城市");
        } else if (et_sijiao_yuyue_adress.getText().toString().trim().equals("")) {
            ToastUtils.showShort(this, "请先填写详细地址");
        } else if (DataUtils.containsEmoji(et_sijiao_yuyue_adress.getText().toString().trim())) {
            ToastUtils.showShort(this, "不支持输入Emoji表情符号");
        } else {
            submitYuYue();
        }
    }


    /**
     * 选择小时
     */
    private void showHour() {
        c.setTimeInMillis(System.currentTimeMillis());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(SiJiaoYuYueActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                view.setIs24HourView(true);
                time = hourOfDay + ":" + minute;
                tv_sijiao_yuyue_hour.setText(time);
                //                c.setTimeInMillis(System.currentTimeMillis());
                //                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                //                c.set(Calendar.MINUTE, minute);
                //                c.set(Calendar.SECOND, 0);
                //                c.set(Calendar.MILLISECOND, 0);
            }
        }, hour, minute, true).show();
    }

    /**
     * 选择日期
     */
    private void showData() {
        //Calendar是设定年度日期对象和一个整数字段之间转换的抽象基类
        //获取当前年月日

        new DatePickerDialog(SiJiaoYuYueActivity.this, 3,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        data = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        tv_sijiao_yuyue_data.setText(data);

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();
    }

    private ProgressDialog dialog;

    /**
     * 提交预约接口
     */
    private void submitYuYue() {
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String user_id = SPUtils.get(this, "user_id", 0) + "";
        String token = (String) SPUtils.get(this, "token", "");
        String adress = address + et_sijiao_yuyue_adress.getText().toString().trim();
        Call<CoachOrderBean> callBack = restApi.coachorder(user_id, token, sijiao_id, type, num, adress, data + "  " + time);

        callBack.enqueue(new Callback<CoachOrderBean>() {
            @Override
            public void onResponse(Call<CoachOrderBean> call, Response<CoachOrderBean> response) {
                MoudleUtils.dialogDismiss(dialog);
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        initStatusTwo();

                    } else {
                        ToastUtils.show(SiJiaoYuYueActivity.this, response.body().getMsg(), 0);
                    }
                }

            }

            @Override
            public void onFailure(Call<CoachOrderBean> call, Throwable t) {
                MoudleUtils.toChekWifi(SiJiaoYuYueActivity.this);
                MoudleUtils.dialogDismiss(dialog);

            }
        });

    }

    private AlertDialog alertDialog;

    private void initStatusTwo() {
        try {

            alertDialog = new AlertDialog.Builder(this).setTitle("提示:")
                    .setMessage("订单已提交成功，待教练确认后\n即可到“个人中心→我的订单→私教”页面进行支付\n")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    }).create(); // 创建对话框
            alertDialog.setCancelable(false);
            if (alertDialog != null) {
                if (!alertDialog.isShowing()) {
                    alertDialog.show(); // 显示
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
