package com.ckjs.ck.Android.MineModule.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.ViewPagerFragmentAdapterHealth;
import com.ckjs.ck.Android.HealthModule.Fragment.HealthCustomFragment;
import com.ckjs.ck.Android.HealthModule.Fragment.HealthMovementFragment;
import com.ckjs.ck.Android.MineModule.Fragment.MineAttentionFrament;
import com.ckjs.ck.Android.MineModule.Fragment.MineMyFansFragment;
import com.ckjs.ck.R;

import java.util.ArrayList;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MIneFrgGzActivity extends AppCompatActivity {
    private TabLayout tabLayout;                            //定义TabLayout
    private ViewPager viewPager;                             //定义viewPager

    private ArrayList<Fragment> fragmentList;                            //定义要装fragment的列表

    private int[] ids = {R.string.min_gz, R.string.min_fs,};

    public MIneFrgGzActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_record);

        initId();
        initToolbar();
        initData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("我的朋友");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    private void initData() {
        initViewPager();
    }

    /**
     * 初始化各控件
     */
    private void initId() {

        tabLayout = (TabLayout) findViewById(R.id.tab_record_title);
        viewPager = (ViewPager) findViewById(R.id.vp_record_pager);

    }

    private void initViewPager() {
        tabLayout.addTab(tabLayout.newTab().setText(ids[0]));
        tabLayout.addTab(tabLayout.newTab().setText(ids[1]));

        fragmentList = new ArrayList<>();
        fragmentList.add(new MineAttentionFrament());
        fragmentList.add(new MineMyFansFragment());


        //给ViewPager设置适配器,此处注明***重点***：fragment嵌套fragment用getChildFragmentManager()！！！
        ViewPagerFragmentAdapterHealth adapter = new ViewPagerFragmentAdapterHealth(getSupportFragmentManager(), fragmentList,this, ids);
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
