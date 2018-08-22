package com.ckjs.ck.Android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import static com.ckjs.ck.Tool.MoudleTwoTool.initToLoginType;


/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class SplashActivity extends AppCompatActivity {

    private View rootView;
    private SimpleDraweeView sdlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = SplashActivity.this.getLayoutInflater().inflate(R.layout.activity_splash, null);
        setContentView(rootView);
        sdlash = (SimpleDraweeView) findViewById(R.id.wel);
        FrescoUtils.initSetPlayImage(sdlash, AppConfig.res + R.drawable.splash);
        initData();
    }


    private void initData() {
        boolean isFirstIn = true;
        isFirstIn = (boolean) SPUtils.get(this, "first", isFirstIn);
        MoudleUtils.initSaveData(this);
        if (isFirstIn) {
            initGuide();
        } else {
            initToLoginType(this);//解决实名认证和支付时的type的名字重复问题将vip1且有电话号的人进行type存为1
            initSplash();
        }
    }


    private void initSplash() {
        AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(3500);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initToMain();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rootView.setAnimation(animation);
    }

    private void initToMain() {
        String token = "";
        token = (String) SPUtils.get("token", token);
        if (token.equals("")) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private void initGuide() {
        //首次打开软件 进入引导页
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        startActivity(intent);
        this.finish();

    }

    /**
     * 返回==home
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            moveTaskToBack(true);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
