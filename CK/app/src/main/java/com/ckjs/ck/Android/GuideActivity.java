package com.ckjs.ck.Android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager viewPager;
    private List<View> views = new ArrayList<>();

    private GuideAdapter adapter;
    private Button buttonEnter;

    private RadioButton radioButton01;
    private RadioButton radioButton02;
    private RadioButton radioButton03;
    private RadioButton radioButton04;
    View view01;
    View view02;
    //    View view03;
    View view04;
    private TextView toGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initComponent();
    }


    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                radioButton01.setChecked(true);
                break;
            case 1:
                radioButton02.setChecked(true);
                break;
            case 2:
                toGoHome.setVisibility(View.VISIBLE);
                radioButton03.setChecked(true);
                break;
            case 3:
                radioButton04.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * Called when shouhuan_serch view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioButton01:
                viewPager.setCurrentItem(0);
                break;
            case R.id.radioButton02:
                viewPager.setCurrentItem(1);
                break;
            case R.id.radioButton03:
                viewPager.setCurrentItem(2);
                break;
            case R.id.radioButton04:
                viewPager.setCurrentItem(3);
                break;
            case R.id.to_go_home:
                SPUtils.put(GuideActivity.this, "first", false);
                openHome();
                break;
        }
    }

    private void initComponent() {
        radioButton01 = (RadioButton) findViewById(R.id.radioButton01);
        radioButton02 = (RadioButton) findViewById(R.id.radioButton02);
        radioButton03 = (RadioButton) findViewById(R.id.radioButton03);
        radioButton04 = (RadioButton) findViewById(R.id.radioButton04);
        radioButton01.setChecked(true);

        radioButton01.setOnClickListener(this);
        radioButton02.setOnClickListener(this);
        radioButton03.setOnClickListener(this);
        radioButton04.setOnClickListener(this);

        view01 = getLayoutInflater().inflate(R.layout.layout_guide_01, null);
        view02 = getLayoutInflater().inflate(R.layout.layout_guide_02, null);
//        view03 = getLayoutInflater().inflate(R.layout.layout_guide_03, null);
        view04 = getLayoutInflater().inflate(R.layout.layout_guide_04, null);
        SimpleDraweeView simpleDraweeView= (SimpleDraweeView) view01.findViewById(R.id.sd_guid);
        SimpleDraweeView simpleDraweeView_two= (SimpleDraweeView) view02.findViewById(R.id.sd_guid);
        SimpleDraweeView simpleDraweeView_end= (SimpleDraweeView) view04.findViewById(R.id.sd_guid);
        FrescoUtils.setImage(simpleDraweeView, AppConfig.res+R.drawable.go_one);
        FrescoUtils.setImage(simpleDraweeView_two, AppConfig.res+R.drawable.go_two);
        FrescoUtils.setImage(simpleDraweeView_end, AppConfig.res+R.drawable.go_three);

        views.add(view01);
        views.add(view02);
//        views.add(view03);
        views.add(view04);

        adapter = new GuideAdapter(views);

        viewPager = (ViewPager) findViewById(R.id.viewPagerGuide);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        buttonEnter = (Button) view04.findViewById(R.id.buttonEnter);
        buttonEnter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SPUtils.put(GuideActivity.this, "first", false);
                openHome();
            }
        });
        toGoHome = (TextView) findViewById(R.id.to_go_home);
        toGoHome.setOnClickListener(this);
    }

    private void openHome() {
        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
    /**
     * 返回==home
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            moveTaskToBack(true);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
