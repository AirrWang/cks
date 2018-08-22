package com.ckjs.ck.Android.HomeModule.Activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ckjs.ck.Android.MineModule.Activity.MineAttentionPeople;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HomeShopH5Activity extends AppCompatActivity {
    private WebView webViewLicense;
    private String url = "";
    private TextView textView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_h5);
        initToolbar();
        initId();
        initGetUrl();
        initWeb();
        initSet();
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }


    private void initGetUrl() {
        Intent intent = getIntent();
        url = intent.getStringExtra("acurl");
    }

    private void initSet() {
        webViewLicense.loadUrl(url);
        webViewLicense.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String str = url;
                String newStr = str.substring(0, str.indexOf("?"));
                if (newStr.equals(AppConfig.shop_id)) {
                    Intent intent = new Intent();
                    intent.setClass(HomeShopH5Activity.this, ShopIsOrderActivity.class);
                    intent.putExtra("id", Uri.parse(str).getQueryParameter("shop_id"));
                    startActivity(intent);
                    return true;
                } else if (newStr.equals(AppConfig.focus_id)) {
                    Intent intent = new Intent();
                    intent.setClass(HomeShopH5Activity.this, MineAttentionPeople.class);
                    intent.putExtra("focus_id", Uri.parse(str).getQueryParameter("focus_id"));
                    startActivity(intent);
                    return true;
                } else if (newStr.equals(AppConfig.os_type)) {
                    DataUtils.jumpStartInterface(Uri.parse(str).getQueryParameter("os_type"),HomeShopH5Activity.this);
                    return true;
                } else {
                    view.loadUrl(url);
                    return true;
                }

            }
        });
        webViewLicense.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress >= 100) {
                    progressBar.setVisibility(View.GONE);
                }
//                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                textView.setText(title);//shouhuan_serch textview
            }

        });
//        webViewLicense.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            if (webViewLicense.canGoBack()) {
                webViewLicense.goBack();// 返回前一个页面
                return true;
            } else {
                this.finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initId() {

        webViewLicense = (WebView) findViewById(R.id.webViewLicense);
        progressBar = (ProgressBar) findViewById(R.id.index_progressBar);
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

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWeb() {
        WebSettings settings = webViewLicense.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
    }
}
