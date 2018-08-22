package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.FamilysearchBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class SearchFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_search_friends;
    private String member_id = "";
    private ProgressDialog dialog;
    private String content = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        initToolbar();
        initId();
        KeyBoardUtils.closeKeyboard(et_search_friends, SearchFriendsActivity.this);
    }

    private void initId() {
        et_search_friends = (EditText) findViewById(R.id.et_search_friends);
        dialog = new ProgressDialog(this);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("搜索亲友");
        SimpleDraweeView sd_button = (SimpleDraweeView) findViewById(R.id.sd_button);
        RelativeLayout r_toolbar_button = (RelativeLayout) findViewById(R.id.r_toolbar_button);
        FrescoUtils.setImage(sd_button, AppConfig.res + R.drawable.family_search);
        sd_button.setOnClickListener(this);
        r_toolbar_button.setOnClickListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * toolbar返回键设置
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
            KeyBoardUtils.closeKeyboard(et_search_friends, SearchFriendsActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        content = et_search_friends.getText().toString().trim();
        if (content != null && !content.equals("")) {
            if (!DataUtils.containsEmoji(content)) {
                initTask(content);
            } else {
                ToastUtils.showShortNotInternet("不支持输入Emoji表情符号");
            }
        } else {
            ToastUtils.showShortNotInternet("搜索内容为空");
        }
        KeyBoardUtils.closeKeyboard(et_search_friends, SearchFriendsActivity.this);
    }

    private void initTask(final String content) {
        MoudleUtils.dialogShow(dialog);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<FamilysearchBean> callBack = restApi.familysearch(token, user_id, content);

        callBack.enqueue(new Callback<FamilysearchBean>() {

            @Override
            public void onResponse(Call<FamilysearchBean> call, Response<FamilysearchBean> response) {
                FamilysearchBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        member_id = bean.getInfo().getMember_id();
                        startActivity(new Intent().putExtra("member_id", member_id).setClass(SearchFriendsActivity.this, ChooseRelationship.class));
                        finish();
                    } else if (bean.getStatus().equals("0")) {
                        if (bean.getInfo() != null) {
                            if (bean.getInfo().getType().equals("tel")) {
                                initToTaskPost(content);
                            }else {
                                ToastUtils.showShort(SearchFriendsActivity.this,bean.getMsg());
                            }
                        }
                    }else {
                        ToastUtils.showShort(SearchFriendsActivity.this,bean.getMsg());
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<FamilysearchBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dialog);
            }
        });
    }

    private void initToTaskPost(final String content) {
        AlertDialog alertDialog = null;
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setMessage("该用户尚未注册，是否邀请")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            initTaskPost(content);
                        }

                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create(); // 创建对话框

        }
        if (alertDialog != null) {
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing()) {
                alertDialog.show(); // 显示
            }
        }
    }

    private void initTaskPost(String content) {
        MoudleUtils.dialogShow(dialog);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<AcceptBean> callBack = restApi.invite(token, user_id, content);

        callBack.enqueue(new Callback<AcceptBean>() {

            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                AcceptBean bean = response.body();
                if (bean != null) {
                    ToastUtils.showShort(SearchFriendsActivity.this,bean.getMsg());
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dialog);
            }
        });
    }
}
