package com.ckjs.ck.Android.HomeModule.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.FavoriteBean;
import com.ckjs.ck.Bean.IsfavoriteBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.GoodView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.lang.reflect.InvocationTargetException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class TjH5HomeActivity extends AppCompatActivity {
    private WebView webViewLicense;
    private String url = "";
    private String subhead = "";
    private String imageurl = "";
    private String read_id = "";
    private SimpleDraweeView btn_1;
    private String isfavorite = "0";
    private boolean flag;
    private ProgressBar progressBar;
    private GoodView myGridView;
    private SimpleDraweeView btn;
    private LinearLayout ll_4;
    private LinearLayout ll_5;
    private LinearLayout ll_6;
    private int isShow = 1;
    private String title1;
    private RelativeLayout r_toolbar_button;
    private Toolbar toolbar;
    private String hsTypw = "0";//0竖屏，1横屏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_h5);
        try {
            myGridView = new GoodView(this);
            initId();
            initGetUrl();
            initToolbar();
            initSet();
            initIsLoveTask();
            initIsllShow();
            initToShare();
            initHsp();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(TjH5HomeActivity.this).
                onActivityResult(requestCode, resultCode, data);
    }

    private void initHsp() {
        if (hsTypw.equals("0")) {
            //竖屏
            MoudleUtils.viewShow(toolbar);
        } else if (hsTypw.equals("1")) {
            //横屏
            MoudleUtils.viewGone(toolbar);
        }
    }

    /**
     * 分享
     */
    private void initToShare() {
        final UMImage umImage;
        if (imageurl != null && !imageurl.equals("")) {
            umImage = new UMImage(TjH5HomeActivity.this, imageurl);
        } else {
            umImage = new UMImage(TjH5HomeActivity.this, R.drawable.app_icon);
        }
        ll_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subhead == null || subhead.equals("")) {
                    subhead = title1;
                }
                if (subhead == null || subhead.equals("")) {
                    subhead = "超空阅读";
                }
                new ShareAction(TjH5HomeActivity.this).
                        withText(subhead)
                        .withTitle(title1)
                        .withTargetUrl(url)
                        .withMedia(umImage)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)
                        .setCallback(umShareListener).open();
            }
        });
    }

    /**
     * 收藏分享是否显示
     */
    private void initIsllShow() {
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShow == 1) {
                    MoudleUtils.viewShow(ll_5);
                    isShow = 2;
                } else {
                    MoudleUtils.viewGone(ll_5);
                    isShow = 1;
                }
            }
        });
        r_toolbar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow == 1) {
                    MoudleUtils.viewShow(ll_5);
                    isShow = 2;
                } else {
                    MoudleUtils.viewGone(ll_5);
                    isShow = 1;
                }
            }
        });

    }

    private TextView textView;

    private void initToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        btn_1 = (SimpleDraweeView) findViewById(R.id.sd_button);
        r_toolbar_button = (RelativeLayout) findViewById(R.id.r_toolbar_button);
        FrescoUtils.setImage(btn_1, AppConfig.res + R.drawable.mpre_report);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    private void initLove(int user_id, String token) {

        if (!token.equals("")) {
            initLoveTask(user_id, token);
        } else {
            MoudleUtils.initToLogin(this);
        }
    }

    private void initIsLoveTask() {
        int user_id = 0;
        user_id = (int) SPUtils.get(TjH5HomeActivity.this, "user_id", user_id);
        String token = "";
        token = (String) SPUtils.get(TjH5HomeActivity.this, "token", token);
        final int finalUser_id = user_id;
        final String finalToken = token;
        Call<IsfavoriteBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).
                isfavorite(token, user_id, read_id);
        getSignBeanCall.enqueue(new Callback<IsfavoriteBean>() {
            @Override
            public void onResponse(Call<IsfavoriteBean> call, Response<IsfavoriteBean> response) {
                try {
                    IsfavoriteBean bean = response.body();
                    if (bean != null) {
                        if (bean.getStatus().equals("1")) {

                            initIsLoveData(bean, finalUser_id, finalToken);

                        } else {
                            ToastUtils.showShort(TjH5HomeActivity.this, bean.getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<IsfavoriteBean> call, Throwable t) {
                MoudleUtils.toChekWifi(TjH5HomeActivity.this);

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration cf = this.getResources().getConfiguration(); //获取设置的配置信息

        int ori = cf.orientation; //获取屏幕方向

        if (ori == cf.ORIENTATION_LANDSCAPE) {
            //            Log.i(AppConfig.TAG, "hp");
            hsTypw = "1";
        } else if (ori == cf.ORIENTATION_PORTRAIT) {
            //            Log.i(AppConfig.TAG, "sp");
            hsTypw = "0";
        }
        initHsp();
    }

    /**
     * 取消收藏
     *
     * @param bean
     * @param finalUser_id
     * @param finalToken
     */
    private void initIsLoveData(IsfavoriteBean bean, final int finalUser_id, final String finalToken) {
        if (bean.getInfo() != null) {
            isfavorite = bean.getInfo().getIsfavorite();
            if (isfavorite.equals("0")) {
                //                MoudleUtils.PointPraiseEffect(myGridView, btn, this, R.drawable.read_collect_dis);
                flag = false;
                FrescoUtils.setImage(btn, AppConfig.res + R.drawable.read_collect_dis);

                //            btn.setBackgroundResource(R.drawable.bg_tj_unlove);
            } else if (isfavorite.equals("1")) {
                //                MoudleUtils.PointPraiseEffect(myGridView, btn, this, R.drawable.read_collect);

                flag = true;
                FrescoUtils.setImage(btn, AppConfig.res + R.drawable.read_collect);

                //            btn.setBackgroundResource(R.drawable.bg_tj_love);
            }
            ll_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initLove(finalUser_id, finalToken);
                }
            });
        }
    }

    private void initLoveTask(int user_id, String token) {
        Call<FavoriteBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).
                favorite(token, user_id, read_id);
        getSignBeanCall.enqueue(new Callback<FavoriteBean>() {
            @Override
            public void onResponse(Call<FavoriteBean> call, Response<FavoriteBean> response) {
                FavoriteBean bean = response.body();
                initLoveType(bean);
            }

            @Override
            public void onFailure(Call<FavoriteBean> call, Throwable t) {
                MoudleUtils.toChekWifi(TjH5HomeActivity.this);

            }
        });
    }

    /**
     * 收藏
     *
     * @param bean
     */
    private void initLoveType(FavoriteBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                if (flag) {
                    MoudleUtils.PointPraiseEffect(myGridView, btn, this, R.drawable.read_collect_dis);
                    flag = false;
                    FrescoUtils.setImage(btn, AppConfig.res + R.drawable.read_collect_dis);
                    //                    btn.setBackgroundResource(R.drawable.bg_tj_unlove);
                } else {
                    MoudleUtils.PointPraiseEffect(myGridView, btn, this, R.drawable.read_collect);
                    flag = true;
                    FrescoUtils.setImage(btn, AppConfig.res + R.drawable.read_collect);

                    //                    btn.setBackgroundResource(R.drawable.bg_tj_love);

                }
                ToastUtils.showShort(TjH5HomeActivity.this, bean.getMsg());
            } else {
                ToastUtils.showShort(TjH5HomeActivity.this, bean.getMsg());
            }
        }
    }

    private void initGetUrl() {
        Intent intent = getIntent();
        url = intent.getStringExtra("acurl");
        read_id = intent.getStringExtra("read_id");
        subhead = intent.getStringExtra("subhead");
        imageurl = intent.getStringExtra("imageurl");
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
                title1 = title;
                textView.setText(title1);//shouhuan_serch textview
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
        btn = (SimpleDraweeView) findViewById(R.id.sd_shoucang);
        ll_4 = (LinearLayout) findViewById(R.id.ll_4);
        ll_5 = (LinearLayout) findViewById(R.id.ll_5);
        ll_6 = (LinearLayout) findViewById(R.id.ll_6);
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

    /**
     * 各个分享的回掉
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            android.util.Log.d("plat", "platform" + platform);

            Toast.makeText(TjH5HomeActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(TjH5HomeActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                android.util.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(TjH5HomeActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        try {
            webViewLicense.getClass().getMethod("onResume").invoke(webViewLicense, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            webViewLicense.getClass().getMethod("onPause").invoke(webViewLicense, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
