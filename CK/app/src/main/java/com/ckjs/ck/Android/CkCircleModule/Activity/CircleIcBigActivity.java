package com.ckjs.ck.Android.CkCircleModule.Activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ckjs.ck.Android.CkCircleModule.Adapter.ViewPagerAdapterCireBigPic;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.ViewTool.MultiTouchViewPager;
import com.ckjs.ck.Tool.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CircleIcBigActivity extends AppCompatActivity {
    private MultiTouchViewPager viewPager;
    private ViewPagerAdapterCireBigPic vpAdapter;
    private LinearLayout llPoint;
    private int prePosition;
    private List<String> getPicture = new ArrayList<>();
    private int p;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_ic_big);
        initId();
        initGetPictureData();
        setViewPagerAdapter();
        initLlPoint();
        initSet();
    }

    private void initGetPictureData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getStringArrayList("getPicture") != null) {
                getPicture = bundle.getStringArrayList("getPicture");
            }
        }
        Intent intent = getIntent();
        p = intent.getIntExtra("p", 0);
        type = intent.getStringExtra("type");
        if (p > 0) {
            prePosition = p;
        }
    }

    private void initId() {
        viewPager = (MultiTouchViewPager) findViewById(R.id.vp_circle);
        llPoint = (LinearLayout) findViewById(R.id.ll_point);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(this), (int) (ScreenUtils.getScreenWidth(this)));
//        int h = ScreenUtils.getScreenHeight(this) - ScreenUtils.getScreenWidth(this);
//        layoutParams.setMargins(0, h / 2, 0, h / 2);
//        viewPager.setLayoutParams(layoutParams);

    }

    private void setViewPagerAdapter() {
        if (vpAdapter == null) {
            vpAdapter = new ViewPagerAdapterCireBigPic(getPicture,
                    this);
            if (type != null && !type.equals("")) {
                vpAdapter.setType(type);
            }
            viewPager.setAdapter(vpAdapter);
        } else {
            vpAdapter.notifyDataSetChanged();
        }
        viewPager.setCurrentItem(p);

    }

    private void initLlPoint() {
        for (int i = 0; i < getPicture.size(); i++) {
            ImageView ivPoint = new ImageView(
                    this);
            int margenH = ScreenUtils.getScreenWidth(CircleIcBigActivity.this) / AppConfig.point_w;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margenH, margenH);
            layoutParams.setMargins(margenH, 0, 0, margenH);
            ivPoint.setLayoutParams(layoutParams);
            ivPoint.setBackgroundResource(R.drawable.guide_indicator_normal);
            llPoint.addView(ivPoint);
        }
        llPoint.getChildAt(p).setBackgroundResource(
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
}
