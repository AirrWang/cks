package com.ckjs.ck.Android.MineModule.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.FavoriteBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class TjH5MineActivity extends AppCompatActivity {
    private WebView webViewLicense;
    private String url = "";
    private String read_id = "";
    private TextView btn;
    private boolean flag = true;
//    private String isfavorite = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_h5);
        initId();
        initGetUrl();
        initToolbar();
        initSet();
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("超空S");
        btn = (TextView) findViewById(R.id.toolbar_button);
        btn.setBackgroundResource(R.drawable.bg_tj_unlove);
//        if (isfavorite.equals("0")) {
//            flag = false;
//            btn.setBackgroundResource(R.drawable.bg_tj_unlove);
//        } else if (isfavorite.equals("1")) {
//            flag = true;
        btn.setBackgroundResource(R.drawable.bg_tj_love);
//        }
        Toolbar.LayoutParams buttonP = (Toolbar.LayoutParams) btn.getLayoutParams();
        buttonP.width = getResources().getDimensionPixelSize(R.dimen.toolbar_btn_wh);
        buttonP.height = getResources().getDimensionPixelSize(R.dimen.toolbar_btn_wh);
        buttonP.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.toolbar_btn_wh), 0);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLove();
            }
        });
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    private void initLove() {
        int user_id = 0;
        user_id = (int) SPUtils.get(TjH5MineActivity.this, "user_id", user_id);
        String token = "";
        token = (String) SPUtils.get(TjH5MineActivity.this, "token", token);
        if (!token.equals("")) {
            initLoveTask(user_id, token);
        } else {
            MoudleUtils.initToLogin(this);
        }
    }

    private void initLoveTask(int user_id, String token) {
        Call<FavoriteBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).
                favorite(token, user_id, read_id);
        getSignBeanCall.enqueue(new Callback<FavoriteBean>() {
            @Override
            public void onResponse(Call<FavoriteBean> call, Response<FavoriteBean> response) {
                FavoriteBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (flag) {
                            flag = false;
                            btn.setBackgroundResource(R.drawable.bg_tj_unlove);
                        } else {
                            flag = true;
                            btn.setBackgroundResource(R.drawable.bg_tj_love);

                        }
                        ToastUtils.showShort(TjH5MineActivity.this, bean.getMsg());
                    } else {
                        ToastUtils.showShort(TjH5MineActivity.this, bean.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<FavoriteBean> call, Throwable t) {
                 MoudleUtils.toChekWifi(TjH5MineActivity.this);
            }
        });
    }

    private void initGetUrl() {
        Intent intent = getIntent();
        url = intent.getStringExtra("acurl");
        read_id = intent.getStringExtra("read_id");
//        isfavorite = intent.getStringExtra("isfavorite");
    }

    private void initSet() {
        webViewLicense.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        initWeb();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

//            if (webViewLicense.canGoBack()) {
//                webViewLicense.goBack();// 返回前一个页面
//                return true;
//            } else {
            this.finish();
//            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initId() {

        webViewLicense = (WebView) findViewById(R.id.webViewLicense);
        //不使用缓存：
        webViewLicense.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webViewLicense.canGoBack()) {
            webViewLicense.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb() {
        webViewLicense.setWebViewClient(new WebViewClient());
        WebSettings settings = webViewLicense.getSettings();
        settings.setJavaScriptEnabled(true);

        webViewLicense.loadUrl(url);

        webViewLicense.onResume();
    }
}
