package com.ckjs.ck.Android.HomeModule.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ckjs.ck.R;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HSHH5Activity extends AppCompatActivity {
    private WebView webViewLicense;
    private String url = "";
    private ProgressBar progressBar;
    private String name="超空";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_h5);
        initGetUrl();
        initToolbar();
        initId();
        initSet();
    }

    private TextView textView;

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(name);
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
        name = intent.getStringExtra("name");
    }

    private void initSet() {
        webViewLicense.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
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
        initWeb();
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

    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb() {
        webViewLicense.setWebViewClient(new WebViewClient());
        WebSettings settings = webViewLicense.getSettings();
        settings.setJavaScriptEnabled(true);

        webViewLicense.loadUrl(url);

        webViewLicense.onResume();
    }
}
