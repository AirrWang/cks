package com.ckjs.ck.Android.MineModule.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.AcH5Activity;
import com.ckjs.ck.Android.HomeModule.Activity.HomeShopH5Activity;
import com.ckjs.ck.Android.MineModule.Adapter.MineSettingAdapter;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.MoudleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class MineSetActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private List<Map<String, String>> itemList;
    private MineSettingAdapter adapter;

    private static String[] ITEMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_set);
        initId();
        initToolbar();

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(getResources().getText(R.string.title_activity_mine_set));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }


    private void initData() {
        initList();
        initAdapter();
    }

    /**
     * listView绑定数据
     */
    private void initAdapter() {
        adapter = new MineSettingAdapter(this);
        adapter.setItemList(itemList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(MineSetActivity.this);
    }

    private void initId() {
        listView = (ListView) findViewById(R.id.listViewSetting);
    }

    /**
     * 初始化设置页面的各项的名称
     */
    private void initList() {
        itemList = new ArrayList<>();
        ITEMS = getResources().getStringArray(R.array.data_setting);
        for (String ITEM : ITEMS) {
            Map<String, String> map = new HashMap<>();
            map.put("content", ITEM);
            map.put("info", "");
            itemList.add(map);
        }

        itemList.get(0).put("info", "");
        itemList.get(1).put("info", "");
        itemList.get(2).put("info", "");
        itemList.get(3).put("info", "");
        itemList.get(4).put("info", "");
        itemList.get(5).put("info", "");
        itemList.get(6).put("info", "");
        itemList.get(7).put("info", "");
        itemList.get(8).put("info", "");

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                toAboutCKS();
                break;
            case 1:
                startActivity(new Intent().setClass(MineSetActivity.this, BusinessActivity.class));//门店认证
                break;
            case 2:
                Intent intent = new Intent();
                intent.putExtra("acurl", AppConfig.h5_ydqx);
                intent.setClass(MineSetActivity.this, HomeShopH5Activity.class);//需要原生与h5交互，截取点击事件
                startActivity(intent);
                break;
            case 3:
                toWipeCache();
                break;
            case 4:
                startActivity(new Intent().setClass(MineSetActivity.this, MineUpDataActivity.class));//检查更新
                break;
            case 5:
                startActivity(new Intent().setClass(MineSetActivity.this, MineFeedBackActivity.class));//意见反馈
                break;
            case 6:
                toStatement();
                break;
            case 7:
                Intent i = new Intent();
                i.putExtra("acurl", AppConfig.h5_szbz + MoudleUtils.getVersionCode(MineSetActivity.this));
                i.setClass(MineSetActivity.this, AcH5Activity.class);//普通h5跳转
                startActivity(i);
                break;
            case 8:
                logout();
                break;
        }
    }

    /**
     * 跳转超空协议页面
     */
    private void toStatement() {
        Intent intent = new Intent();
        intent.putExtra("acurl", "http://www.chaokongs.com/public/Statement.html");

        intent.setClass(MineSetActivity.this, MineH5Activity.class);
        startActivity(intent);
    }

    /**
     * 跳转清除缓存页面
     */
    private void toWipeCache() {
        startActivity(new Intent().setClass(this, MineWipeCacheActivity.class));
    }


    /**
     * 跳转关于超空页面
     */
    private void toAboutCKS() {
        startActivity(new Intent().setClass(this, MineAboutCKSActivity.class));
    }

    outAsyncTask asyncTask;
    /**
     * 退登录警醒提示框
     */
    private AlertDialog alertDialog;

    private void logout() {
        alertDialog = new AlertDialog.Builder(this).setTitle("确定退出？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (asyncTask == null) {
                            asyncTask = new outAsyncTask();
                        }
                        asyncTask.execute();
                    }
                }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create(); // 创建对话框
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing()) {
            alertDialog.show(); // 显示对话框
        }
    }

    class outAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            MoudleUtils.initOut(MineSetActivity.this);
            MoudleUtils.initOutNofiySetThis();
            return null;
        }

        @Override
        protected void onPreExecute() {
            MineSetActivity.this.finish();
            MoudleUtils.toLogin(MineSetActivity.this);
            super.onPreExecute();
        }
    }

    /**
     * 标题栏返回
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
