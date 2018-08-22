package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Adapter.ViewPagerAdapterHomeeJsfMore;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.ActinfoBean;
import com.ckjs.ck.Bean.ActivityBaom;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class BaoMingActivity extends AppCompatActivity {

    private AutoScrollViewPager viewPager;
    private LinearLayout llPoint;
    private int activity_id;
    private String name = "";
    private TextView textViewName, textViewNum, textViewTimeNew, textViewCal, textViewAc, textViewNameJuB,
            textViewAddress, textViewJieShao, textViewTime;

    private Button button_bm;
    //    private SimpleDraweeView simpleDraweeView;
    private RelativeLayout activity_bao_ming;
    private ProgressDialog dialog;
    private KyLoadingBuilder builder;
    private ViewPagerAdapterHomeeJsfMore vpAdapter;
    private int prePosition;
    private List<String> getPicture = new ArrayList<>();
    private LinearLayout ll_2;
    private LinearLayout ll_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_ming);
        builder = new KyLoadingBuilder(this);
        initId();
        initVpWh();
        initGetIntentData();
        initToolbar();
        initTask();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewPager != null) {
            viewPager.startAutoScroll();
        }
    }

    private void setViewPagerAdapter() {
        if (vpAdapter == null) {
            vpAdapter = new ViewPagerAdapterHomeeJsfMore(getPicture,
                    this);
            viewPager.setAdapter(vpAdapter);
        } else {
            vpAdapter.notifyDataSetChanged();
        }

    }

    private void initLlPoint() {
        for (int i = 0; i < getPicture.size(); i++) {
            ImageView ivPoint = new ImageView(
                    this);
            int margenH = ScreenUtils.getScreenWidth() / AppConfig.point_w;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margenH, margenH);
            layoutParams.setMargins(margenH, 0, 0, margenH);
            ivPoint.setLayoutParams(layoutParams);
            ivPoint.setBackgroundResource(R.drawable.guide_indicator_normal);
            llPoint.addView(ivPoint);
        }
        llPoint.getChildAt(0).setBackgroundResource(
                R.drawable.guide_indicator_checked);

    }

    private void initSet() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {
                llPoint.getChildAt(position).setBackgroundResource(
                        R.drawable.guide_indicator_checked);
                llPoint.getChildAt(prePosition).setBackgroundResource(
                        R.drawable.guide_indicator_normal);
                prePosition = position;
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (viewPager != null) {
            viewPager.stopAutoScroll();
        }
    }

    private void initVpWh() {
        viewPager.setInterval(2000);
        viewPager.setAutoScrollDurationFactor(4);
        viewPager.setSwipeScrollDurationFactor(1.5);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(this), (int) (ScreenUtils.getScreenWidth(this) / 2));
        viewPager.setLayoutParams(layoutParams);
    }

    private void initToolbar() {
        dialog = new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("活动详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    private void initId() {
        textViewNum = (TextView) findViewById(R.id.textViewNum);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewCal = (TextView) findViewById(R.id.textViewCal);
        textViewAc = (TextView) findViewById(R.id.textViewAc);
        textViewNameJuB = (TextView) findViewById(R.id.textViewNameJuB);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewJieShao = (TextView) findViewById(R.id.textViewJieShao);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewTimeNew = (TextView) findViewById(R.id.textViewTimeNew);

        ll_2 = (LinearLayout) findViewById(R.id.ll_2);
        ll_3 = (LinearLayout) findViewById(R.id.ll_3);

        button_bm = (Button) findViewById(R.id.button_bm);
//        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.ivSm);
        activity_bao_ming = (RelativeLayout) findViewById(R.id.activity_bao_ming);
        viewPager = (AutoScrollViewPager) findViewById(R.id.vp_home_js_more);
        llPoint = (LinearLayout) findViewById(R.id.ll_point_bm);
    }


    private void initGetIntentData() {
        Intent intent = getIntent();
        activity_id = intent.getIntExtra("activity_id", 0);
        name = intent.getStringExtra("name");
    }

    private void initTask() {
        MoudleUtils.kyloadingShow(builder);
        int user_id = (int) (SPUtils.get(this, "user_id", 0));
        String token = (String) SPUtils.get(this, "token", "");
        Call<ActinfoBean> beanCall = RetrofitUtils.retrofit.create(NpApi.class).actinfo(token, user_id, activity_id);
        beanCall.enqueue(new Callback<ActinfoBean>() {
            @Override
            public void onResponse(Call<ActinfoBean> call, Response<ActinfoBean> response) {
                ActinfoBean bean = response.body();
                initList(bean);
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<ActinfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(BaoMingActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });

    }

    private void initNoBm(String name) {
        button_bm.setEnabled(true);
        button_bm.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_lan));
        button_bm.setText(name);
    }

    private void initList(ActinfoBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                initStateOne(bean);
//                activity_bao_ming.setVisibility(View.VISIBLE);
                MoudleUtils.viewShow(activity_bao_ming);
            } else if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(BaoMingActivity.this, bean.getMsg());
            } else if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(BaoMingActivity.this,bean.getMsg());
            }
        }

    }

    private void initStateOne(ActinfoBean bean) {
        if (bean.getInfo() != null) {
            if (bean.getInfo().getPicture() != null) {
                getPicture = bean.getInfo().getPicture();
                initLlPoint();
                setViewPagerAdapter();
                initSet();
            }
            MoudleUtils.textViewSetText(textViewName, bean.getInfo().getName());
            MoudleUtils.textViewSetText(textViewCal, bean.getInfo().getFat() + "kcal");
            MoudleUtils.textViewSetText(textViewNum, bean.getInfo().getBmnum() + "/" + bean.getInfo().getNum());
            MoudleUtils.textViewSetText(textViewAddress, bean.getInfo().getAddress());
            MoudleUtils.textViewSetText(textViewJieShao, bean.getInfo().getText());
            MoudleUtils.textViewSetText(textViewNameJuB, bean.getInfo().getHost());
            if (bean.getInfo().getSportnum() == 0) {
                ll_2.setVisibility(View.GONE);
                ll_3.setGravity(Gravity.END);
            }
            MoudleUtils.textViewSetText(textViewAc, bean.getInfo().getSportnum() + "个");
            MoudleUtils.textViewSetText(textViewTime, bean.getInfo().getTime() + "min");
            MoudleUtils.textViewSetText(textViewTimeNew, "开始：" + bean.getInfo().getStartime() + "\n" + "结束：" + bean.getInfo().getStoptime());
            if (bean.getInfo().getBmstatus() == 0) {
                if (bean.getInfo().getBmnum() >= bean.getInfo().getNum()) {
                    initBmed("人数已满");
                } else {
                    initNoBm("报名参加");
                    initSetOnclik();
                }
            } else {
                initBmed("已报名");
            }
//            ToastUtils.showShort(BaoMingActivity.this, bean.getMsg());
        }
    }



    private void initBmed(String name) {
        button_bm.setEnabled(false);
        button_bm.setText(name);
        button_bm.setTextColor(getResources().getColor(R.color.c_ffffff));
        button_bm.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_time_hui));

    }

    private void initSetOnclik() {
        button_bm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user_id = (int) (SPUtils.get(BaoMingActivity.this, "user_id", 0));
                String token = (String) SPUtils.get(BaoMingActivity.this, "token", "");
                if (token.equals("")) {
                    MoudleUtils.initToLogin(BaoMingActivity.this);
                    finish();
                } else {
                    if ((SPUtils.get(BaoMingActivity.this, "vip", "").equals("1"))) {
                        if ((SPUtils.get(BaoMingActivity.this, "type", "") + "").equals("1")) {
                            startActivity(new Intent().setClass(BaoMingActivity.this, RealNameYsActivity.class));
                        } else {
                            startActivity(new Intent().setClass(BaoMingActivity.this, RealNameActivity.class));
                        }
                    } else {
                        initTasskBm(user_id, token);
                    }
                }
            }
        });
    }

    private void initTasskBm(int user_id, String token) {
        MoudleUtils.dialogShow(dialog);
        Call<ActivityBaom> beanCall = RetrofitUtils.retrofit.create(NpApi.class).activityBaom(token, user_id, activity_id);
        beanCall.enqueue(new Callback<ActivityBaom>() {
            @Override
            public void onResponse(Call<ActivityBaom> call, Response<ActivityBaom> response) {
                ActivityBaom bean = response.body();
                initData(bean);
                MoudleUtils.dialogDismiss(dialog);

            }

            @Override
            public void onFailure(Call<ActivityBaom> call, Throwable t) {
                MoudleUtils.toChekWifi(BaoMingActivity.this);
                MoudleUtils.dialogDismiss(dialog);

            }
        });
    }

    private void initData(ActivityBaom bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                initBmed("已报名");
            } else if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(BaoMingActivity.this, bean.getMsg());

            } else if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(BaoMingActivity.this,bean.getMsg());


            }
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
}
