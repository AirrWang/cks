package com.ckjs.ck.Android.MineModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.UpmedicalfileBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MineInvitedFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    private UMImage umImage;
    private static String INVITEDURL = "http://www.chaokongs.com/circle/invite";
    private KyLoadingBuilder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invited_friends);
        builder = new KyLoadingBuilder(this);
        initToolbar();
        initId();
    }

    private void initId() {
        LinearLayout ll_invite_circle = (LinearLayout) findViewById(R.id.ll_invite_circle);
        LinearLayout ll_invite_weibo = (LinearLayout) findViewById(R.id.ll_invite_weibo);
        LinearLayout ll_invite_weixin = (LinearLayout) findViewById(R.id.ll_invite_weixin);
        LinearLayout ll_invite_qq = (LinearLayout) findViewById(R.id.ll_invite_qq);

        ll_invite_circle.setOnClickListener(this);
        ll_invite_weibo.setOnClickListener(this);
        ll_invite_weixin.setOnClickListener(this);
        ll_invite_qq.setOnClickListener(this);

        umImage = new UMImage(MineInvitedFriendsActivity.this, R.drawable.app_icon);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("邀请");
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
        switch (v.getId()) {
            case R.id.ll_invite_qq:
                new ShareAction(MineInvitedFriendsActivity.this).setPlatform(SHARE_MEDIA.QQ).
                        withText("加入超空运动健康智能管理系统，我们一起做健康达人，你也一起来吧!！")
                        .withTitle("超空好友邀请函")
                        .withTargetUrl(INVITEDURL)
                        .withMedia(umImage)
                        .setCallback(umShareListener).share();
                break;
            case R.id.ll_invite_circle:
                new ShareAction(MineInvitedFriendsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).
                        withText("加入超空运动健康智能管理系统，我们一起做健康达人，你也一起来吧!")
                        .withTitle("超空好友邀请函")
                        .withTargetUrl(INVITEDURL)
                        .withMedia(umImage)
                        .setCallback(umShareListener).share();
                break;
            case R.id.ll_invite_weibo:
                new ShareAction(MineInvitedFriendsActivity.this).setPlatform(SHARE_MEDIA.SINA).
                        withText("加入超空运动健康智能管理系统，我们一起做健康达人，你也一起来吧!")
                        .withTitle("超空好友邀请函")
                        .withTargetUrl(INVITEDURL)
                        .withMedia(umImage)
                        .setCallback(umShareListener).share();
                break;
            case R.id.ll_invite_weixin:
                new ShareAction(MineInvitedFriendsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).
                        withText("加入超空运动健康智能管理系统，我们一起做健康达人，你也一起来吧!")
                        .withTitle("超空好友邀请函")
                        .withTargetUrl(INVITEDURL)
                        .withMedia(umImage)
                        .setCallback(umShareListener).share();
                break;
        }
    }

    /**
     * 各个分享的回掉
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            android.util.Log.d("plat", "platform" + platform);
            initData();
            Toast.makeText(MineInvitedFriendsActivity.this, platform + " 好友邀请成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MineInvitedFriendsActivity.this, platform + " 好友邀请失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                android.util.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MineInvitedFriendsActivity.this, platform + " 好友邀请取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(MineInvitedFriendsActivity.this, "token", "");
        int user_id = (int) SPUtils.get(MineInvitedFriendsActivity.this, "user_id", 0);
        Call<UpmedicalfileBean> callBack = restApi.invite(user_id + "", token);

        callBack.enqueue(new Callback<UpmedicalfileBean>() {
            @Override
            public void onResponse(Call<UpmedicalfileBean> call, Response<UpmedicalfileBean> response) {

                if (response.body() != null) {
                    ToastUtils.showShort(MineInvitedFriendsActivity.this, response.body().getMsg());
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<UpmedicalfileBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineInvitedFriendsActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
