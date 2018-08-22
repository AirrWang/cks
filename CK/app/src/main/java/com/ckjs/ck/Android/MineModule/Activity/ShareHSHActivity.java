package com.ckjs.ck.Android.MineModule.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.BackStateActivity;
import com.ckjs.ck.Android.HomeModule.Activity.PoiSearchListJsfActivity;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.SPUtils;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ShareHSHActivity extends AppCompatActivity {

    private LinearLayout ll_sharehsh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_hsh);
        initID();
        initToolbar();
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
    /**
     * 标题栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("共享手环");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }
    private void initID() {
        TextView textview_84= (TextView) findViewById(R.id.textview_84);
        textview_84.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textview_84.getPaint().setAntiAlias(true);//抗锯齿
        TextView textview_85= (TextView) findViewById(R.id.textview_85);
        textview_85.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textview_85.getPaint().setAntiAlias(true);//抗锯齿
        ll_sharehsh = (LinearLayout) findViewById(R.id.ll_sharehsh);
        ll_sharehsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * type0：默认；1：共享手环；
                 */
                Intent intent = new Intent(ShareHSHActivity.this, PoiSearchListJsfActivity.class).putExtra("type", 1);
                startActivity(intent);
            }
        });
        LinearLayout ll_yajin= (LinearLayout) findViewById(R.id.ll_yajin);
        ll_yajin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((SPUtils.get("unrentstatus", "0") + "").equals("0")) {
                    startActivity(new Intent().setClass(ShareHSHActivity.this, ReturnCardActivity.class));//押金退款
                } else if ((SPUtils.get("unrentstatus", "0") + "").equals("1")) {
                    startActivity(new Intent().setClass(ShareHSHActivity.this, BackStateActivity.class));
                }
            }
        });
    }
}
