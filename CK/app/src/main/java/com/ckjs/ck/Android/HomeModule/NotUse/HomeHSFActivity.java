package com.ckjs.ck.Android.HomeModule.NotUse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.ViewPagerFragmentAdapterHealth;
import com.ckjs.ck.Android.HomeModule.Activity.AcH5Activity;
import com.ckjs.ck.Android.HomeModule.Fragment.HomeHSFFragment1;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HomeHSFActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] ids = {R.string.hsf1,R.string.hsf2};
    private ArrayList<Fragment> fragmentList;                            //定义要装fragment的列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_record);
        initId();
        initToolbar();
        initViewPager();
    }

    private void initId() {
        tabLayout = (TabLayout) findViewById(R.id.tab_record_title);
        viewPager = (ViewPager) findViewById(R.id.vp_record_pager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        RelativeLayout r_toolbar_button = (RelativeLayout) findViewById(R.id.r_toolbar_button);
        SimpleDraweeView sd_button = (SimpleDraweeView) findViewById(R.id.sd_button);
        FrescoUtils.setImage(sd_button,AppConfig.res+R.drawable.family_query);
        r_toolbar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().putExtra("acurl", AppConfig.h5_hsf_url).setClass(HomeHSFActivity.this,AcH5Activity.class));
            }
        });
        sd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().putExtra("acurl",AppConfig.h5_hsf_url).setClass(HomeHSFActivity.this,AcH5Activity.class));
            }
        });
        textView.setText("超空家");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    private void initViewPager() {
        tabLayout.addTab(tabLayout.newTab().setText(ids[0]));
        tabLayout.addTab(tabLayout.newTab().setText(ids[1]));

        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeHSFFragment1());
        fragmentList.add(new HomeHSFFragment2());



        //给ViewPager设置适配器,此处注明***重点***：fragment嵌套fragment用getChildFragmentManager()！！！
        ViewPagerFragmentAdapterHealth adapter = new ViewPagerFragmentAdapterHealth(getSupportFragmentManager(), fragmentList, HomeHSFActivity.this, ids);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

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
