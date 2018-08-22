package com.ckjs.ck.Android.MineModule.Activity;

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
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.ViewPagerFragmentAdapterHealth;
import com.ckjs.ck.Android.MineModule.Fragment.MineMessageFragment;
import com.ckjs.ck.Android.MineModule.Fragment.MinePrivateLetterFragment;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.LetterstatusBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MineInformationActivity extends AppCompatActivity {


    private ViewPager vp_information_pager;
    private int[] ids = {R.string.information, R.string.information1};
    private ArrayList<Fragment> fragmentList;                            //定义要装fragment的列表
    private TabLayout tab_information_title;
    private ViewPagerFragmentAdapterHealth adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_information);
        initToolbar();
        initId();
        initViewPager();
    }

    private void initViewPager() {
        tab_information_title.addTab(tab_information_title.newTab().setText(ids[0]).setIcon(R.drawable.line_red));
        tab_information_title.addTab(tab_information_title.newTab().setText(ids[1]).setIcon(R.drawable.line_red));

        fragmentList = new ArrayList<>();
        fragmentList.add(new MinePrivateLetterFragment());
        fragmentList.add(new MineMessageFragment());


        //给ViewPager设置适配器,此处注明***重点***：fragment嵌套fragment用getChildFragmentManager()！！！
        adapter = new ViewPagerFragmentAdapterHealth(getSupportFragmentManager(), fragmentList, MineInformationActivity.this, ids);
        vp_information_pager.setAdapter(adapter);
        tab_information_title.setupWithViewPager(vp_information_pager);
        tab_information_title.setTabsFromPagerAdapter(adapter);
        initSlecet();
        initToGetSxRed();


    }

    private void initSlecet() {
        tab_information_title.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_information_title.getTabAt(tab.getPosition()).setCustomView(null);
                //设置小红点
                View tabView = adapter.getTabView(tab.getPosition());
                SimpleDraweeView imageView = (SimpleDraweeView) tabView.findViewById(R.id.iv_tab_red);
                FrescoUtils.setImage(imageView, "");
                TextView textView = (TextView) tabView.findViewById(R.id.tv_tab_title);
                textView.setTextColor(getResources().getColor(R.color.c_4491F2));
                vp_information_pager.setCurrentItem(tab.getPosition());
                tab_information_title.getTabAt(tab.getPosition()).setCustomView(tabView);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab_information_title.getTabAt(tab.getPosition()).setCustomView(null);
                //设置
                View tabView = adapter.getTabView(tab.getPosition());
                TextView textView = (TextView) tabView.findViewById(R.id.tv_tab_title);
                textView.setTextColor(getResources().getColor(R.color.c_33));
                tab_information_title.getTabAt(tab.getPosition()).setCustomView(tabView);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initId() {
        tab_information_title = (TabLayout) findViewById(R.id.tab_information_title);
        vp_information_pager = (ViewPager) findViewById(R.id.vp_information_pager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("我的消息");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * toolbar返回按钮
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

    /**
     * 走是否有新的私信查询网络请求，并且请求成功后刷新页面（显示小红点）
     */
    private void initToGetSxRed() {

        NpApi npApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");

        Call<LetterstatusBean> callBack = npApi.letterstatus(token, user_id);


        callBack.enqueue(new Callback<LetterstatusBean>() {
            @Override
            public void onResponse(Call<LetterstatusBean> call, Response<LetterstatusBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() == null) return;
                        if (response.body().getInfo().getMessage().equals("1")) {
                            initRed(1);
                        }
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<LetterstatusBean> call, Throwable t) {

            }
        });
    }

    /**
     * 在这里判断每个TabLayout的内容是否有更新，来设置小红点是否显示
     **/
    private void initRed(int i) {
        //设置小红点
        View tabView = adapter.getTabView(i);
        SimpleDraweeView imageView = (SimpleDraweeView) tabView.findViewById(R.id.iv_tab_red);
        FrescoUtils.setImage(imageView, AppConfig.res + R.drawable.message_red);
        tab_information_title.getTabAt(i).setCustomView(tabView);
    }

}
