package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Android.HomeModule.Adapter.ViewPagerAdapterHomeeJsfMore;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.GyminfoBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.ViewTool.AutoScrollViewPager;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HomeJsfMoreActivity extends AppCompatActivity {

    @BindView(R.id.tv_gz_num)
    TextView tv_gz_num;
    @BindView(R.id.tv_adr)
    TextView tv_adr;
    @BindView(R.id.tv_call)
    TextView tv_call;
    @BindView(R.id.vp_home_js_more)
    AutoScrollViewPager viewPager;
    @BindView(R.id.tv_md_js)
    TextView tv_md_js;
    @BindView(R.id.ll_point)
    LinearLayout llPoint;
    @BindView(R.id.sd_dw)
    SimpleDraweeView sd_dw;
    @BindView(R.id.sd_jp_jl)
    SimpleDraweeView sd_jp_jl;
    @BindView(R.id.sd_ck_th)
    SimpleDraweeView sd_ck_th;
    @BindView(R.id.tv_jin_pai_jl)
    TextView tv_jin_pai_jl;
    @BindView(R.id.tv_ck_th)
    TextView tv_ck_th;
    @BindView(R.id.sd_call)
    SimpleDraweeView sd_call;
    @BindView(R.id.tv_md_name)
    TextView tv_md_name;
    @BindView(R.id.tv_nan_num)
    TextView tv_nan_num;
    @BindView(R.id.tv_nv_num)
    TextView tv_nv_num;

    private String gym_id = "";
    private String name;
    private ViewPagerAdapterHomeeJsfMore vpAdapter;
    private int prePosition;
    private List<String> getPicture = new ArrayList<>();
    private String lat = "";
    private String lon = "";
    private String adr = "";
    private String tel = "";
    private String endNodeStr = "";
    private KyLoadingBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_jsf_more);
        ButterKnife.bind(this);
        builder = new KyLoadingBuilder(this);
        initGetgymid();
        initVpWh();
        initToolbar();
        initJsXqTask();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (viewPager != null) {
            viewPager.startAutoScroll();
        }
    }

    /**
     * 跳转定位
     *
     * @param view
     */
    public void goToDwSd(View view) {
        initToDw();
    }

    /**
     * 跳转定位
     */
    private void initToDw() {
        if (NetworkUtils.isNetworkAvailable(HomeJsfMoreActivity.this)) {
            Intent intent = new Intent();
            if (!adr.equals("") && !endNodeStr.equals("") && !lat.equals("") && !lon.equals("")) {
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("adr", adr);
                intent.putExtra("tel", tel);
                intent.putExtra("endNodeStr", endNodeStr);
                intent.setClass(this, PoiSearchOneJsfActivity.class);
                startActivity(intent);
            }

        } else {
            MoudleUtils.toChekWifi(this);
        }
    }

    /**
     * 去定位
     */
    private void goToDwTv() {
        tv_adr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initToDw();
            }
        });
    }

    /**
     * 提示打电话
     */
    public void goToCall(View view) {
        if (!tv_call.getText().toString().trim().equals("")) {
            new AlertDialog.Builder(this).setTitle("联系电话")
                    .setMessage(tv_call.getText().toString().trim())
                    .setPositiveButton("拨打", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initToCall();

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    /**
     * 打电话
     */
    private void initToCall() {
        try {
            String mobile = tv_call.getText().toString().trim();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "没有相关应用", Toast.LENGTH_SHORT).show();
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

    /**
     * 设置轮播的点
     */
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

    /**
     * 设置viewPager滑动监听
     */
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

    /**
     * 金牌教练和超空特惠的跳转事件监听
     *
     * @param id
     */
    private void initToSjList(final String id) {
        sd_jp_jl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initToSjListAc(id);
            }
        });
        sd_ck_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initToCkthAc(id);
            }
        });
        tv_jin_pai_jl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initToSjListAc(id);
            }
        });
        tv_ck_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initToCkthAc(id);
            }
        });
    }

    /**
     * 跳转金牌教练页面
     *
     * @param id
     */
    private void initToCkthAc(String id) {
        Intent intent = new Intent();
        intent.putExtra("gym_id", id);
        intent.setClass(HomeJsfMoreActivity.this, HomeJsfCkthListActivity.class);
        if (id != null) {
            if (!id.equals("")) {
                startActivity(intent);
            }
        }
    }

    /**
     * 跳转超空特惠页面
     *
     * @param id
     */
    private void initToSjListAc(String id) {
        Intent intent = new Intent();
        intent.putExtra("gym_id", id);
        intent.setClass(HomeJsfMoreActivity.this, HomeJsfSjListActivity.class);
        if (id != null) {
            if (!id.equals("")) {
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (viewPager != null) {
            viewPager.stopAutoScroll();
        }
    }

    /**
     * 设置viewPager宽高
     */
    private void initVpWh() {
        viewPager.setInterval(2000);
        viewPager.setAutoScrollDurationFactor(4);
        viewPager.setSwipeScrollDurationFactor(1.5);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(this), (int) (ScreenUtils.getScreenWidth(this) / 2));
        viewPager.setLayoutParams(layoutParams);
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(name);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    /**
     * 用intent拿到首页发来的超空门店gym_id
     */
    private void initGetgymid() {
        Intent intent = getIntent();
        gym_id = intent.getStringExtra("gym_id");
        name = intent.getStringExtra("name");
        MoudleUtils.textViewSetText(tv_md_name, name);
    }

    /**
     * 进行超空门店详情接口获取数据操作
     */
    private void initJsXqTask() {
        MoudleUtils.kyloadingShow(builder);
        Call<GyminfoBean> beanCall = RetrofitUtils.retrofit.create(NpApi.class).gyminfo(gym_id);
        beanCall.enqueue(new Callback<GyminfoBean>() {
            @Override
            public void onResponse(Call<GyminfoBean> call, Response<GyminfoBean> response) {
                GyminfoBean bean = response.body();
                initBean(bean);
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<GyminfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HomeJsfMoreActivity.this);

                MoudleUtils.kyloadingDismiss(builder);

            }
        });
    }

    /**
     * 处理网络获取的超空门店的详情信息bean
     *
     * @param bean
     */
    private void initBean(GyminfoBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                if (bean.getInfo() != null) {
                    initList(bean);
                    goToDwTv();
                } else {
                    ToastUtils.showShort(HomeJsfMoreActivity.this, bean.getMsg());
                }
            }
        }
    }

    /**
     * status为1时即成功时的操作
     *
     * @param bean
     */
    private void initList(GyminfoBean bean) {
        MoudleUtils.textViewSetText(tv_adr, bean.getInfo().getPlace());
        MoudleUtils.textViewSetText(tv_call, bean.getInfo().getTel());
        MoudleUtils.textViewSetText(tv_gz_num, bean.getInfo().getNum() + " 次关注");
        MoudleUtils.textViewSetText(tv_md_js, bean.getInfo().getIntro());
        MoudleUtils.textViewSetText(tv_nan_num, bean.getInfo().getUserman()+"人");
        MoudleUtils.textViewSetText(tv_nv_num, bean.getInfo().getUserwoman()+"人");
        lat = bean.getInfo().getLat();
        lon = bean.getInfo().getLon();
        adr = bean.getInfo().getPlace();
        tel = bean.getInfo().getTel();
        endNodeStr = bean.getInfo().getName();
        getPicture = bean.getInfo().getPicture();
        initLlPoint();
        setViewPagerAdapter();
        initSet();
        initToSjList(bean.getInfo().getId());
    }
}
