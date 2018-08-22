package com.ckjs.ck.Android.MineModule.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ckjs.ck.R;
import com.ckjs.ck.Tool.DataCleanManager;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class MineWipeCacheActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mWipeCache;
    private TextView mJindu;
    private boolean isOk = true;
    private int time = 0;
    private TextView tvWipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_wipecache);
        initId();
        initToolbar();


    }
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what == 0) {
                    mJindu.setText(time + "%");
                    time++;
                    if (time >= 100) {
                        time=100;
                        isOk = false;
                        tvWipe.setText("清理完成");
                    }
                }
        }
    };

    private void initId() {
        mJindu = (TextView) findViewById(R.id.wipe_jindu);
        mWipeCache = (Button) findViewById(R.id.mine_wipecache);
        mWipeCache.setOnClickListener(this);
        tvWipe = (TextView) findViewById(R.id.wipe_text);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("清除缓存");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
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

    @Override
    public void onClick(View v) {
        if(isOk){
            DataCleanManager.cleanApplicationData(MineWipeCacheActivity.this);
            start();
        }else{
            isOk=true;
            time=0;
            DataCleanManager.cleanApplicationData(MineWipeCacheActivity.this);
            start();
        }

        tvWipe.setText("开始清理");
    }

    private void start(){
        new Thread(){
            @Override
            public void run() {
                while(isOk){
                    try {
                        Thread.sleep(50);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
