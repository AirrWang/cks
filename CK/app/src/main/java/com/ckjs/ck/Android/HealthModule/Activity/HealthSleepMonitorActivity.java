package com.ckjs.ck.Android.HealthModule.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.ViewPagerFragmentAdapterHealth;
import com.ckjs.ck.Android.HealthModule.Fragment.HealthSleepDataFragment;
import com.ckjs.ck.Android.HealthModule.Fragment.HealthSleepWeekFragment;
import com.ckjs.ck.Android.HealthModule.Fragment.HealthSleepMonthFragment;
import com.ckjs.ck.R;

import java.util.ArrayList;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HealthSleepMonitorActivity extends AppCompatActivity {
    private TabLayout tabLayout;                            //定义TabLayout
    private ViewPager viewPager;                             //定义viewPager
    private Toolbar toolbar;
    private int[] ids = {R.string.health_sleep_top_data, R.string.health_sleep_top_week, R.string.health_sleep_top_year,};
    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_sleep_top);
        initToolbar();
        initId();
        initViewPager();
    }

    /**
     * 初始化各控件
     */
    private void initId() {

        tabLayout = (TabLayout) findViewById(R.id.tab_HealthFragment_title);
        viewPager = (ViewPager) findViewById(R.id.vp_HealthFragment_pager);

    }

    private void initViewPager() {
        tabLayout.addTab(tabLayout.newTab().setText(ids[0]));
        tabLayout.addTab(tabLayout.newTab().setText(ids[1]));
        tabLayout.addTab(tabLayout.newTab().setText(ids[2]));

        fragmentList = new ArrayList<>();
        fragmentList.add(new HealthSleepDataFragment());
        fragmentList.add(new HealthSleepWeekFragment());
        fragmentList.add(new HealthSleepMonthFragment());


        //给ViewPager设置适配器,此处注明***重点***：fragment嵌套fragment用getChildFragmentManager()！！！//activity使用getSupportFragmentManager()
        ViewPagerFragmentAdapterHealth adapter = new ViewPagerFragmentAdapterHealth(this.getSupportFragmentManager(), fragmentList, this, ids);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

    }

    /**
     * 标题栏
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("睡眠监测");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * 返回设置
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
