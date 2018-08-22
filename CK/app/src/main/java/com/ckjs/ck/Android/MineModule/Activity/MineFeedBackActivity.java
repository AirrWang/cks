package com.ckjs.ck.Android.MineModule.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.FeedBackBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.DataUtils;
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

public class MineFeedBackActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView button;
    private EditText mFeedBack;
    private String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_feedback);
        progressDialog = new ProgressDialog(this);
        mFeedBack = (EditText) findViewById(R.id.mine_feedback);
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("意见反馈");
        setSupportActionBar(toolbar);

        button = (TextView) findViewById(R.id.toolbar_button);
        button.setText("提交");
        button.setOnClickListener(this);

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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_button:
                initData();

                break;
        }
    }

    private void initData() {
        content = mFeedBack.getText().toString().trim();
        if (content == null || content.equals("")) {
            ToastUtils.showShort(MineFeedBackActivity.this, "反馈不能为空哦");
        } else {
            if (!DataUtils.containsEmoji(content)) {
                initTaskFB();
            } else {
                ToastUtils.showShortNotInternet("不支持输入Emoji表情符号");
            }
        }
    }

    private ProgressDialog progressDialog;

    private void initTaskFB() {
        MoudleUtils.dialogShow(progressDialog);
        int userid = (int) SPUtils.get(this, "user_id", 0);
        String token = (String) SPUtils.get(this, "token", "");
        Call<FeedBackBean> getSignBeanCall = RetrofitUtils.retrofit.create(WfApi.class).feedback(token, userid, content);
        getSignBeanCall.enqueue(new Callback<FeedBackBean>() {
            @Override
            public void onResponse(Call<FeedBackBean> call, Response<FeedBackBean> response) {
                FeedBackBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        ToastUtils.showShort(MineFeedBackActivity.this, bean.getMsg());
                        MoudleUtils.dialogDismiss(progressDialog);
                        finish();
                    } else if (bean.getStatus().equals("0")) {
                        ToastUtils.showShort(MineFeedBackActivity.this, bean.getMsg());
                        MoudleUtils.dialogDismiss(progressDialog);
                    } else if (bean.getStatus().equals("2")) {
                        ToastUtils.showShort(MineFeedBackActivity.this,bean.getMsg());
                        MoudleUtils.dialogDismiss(progressDialog);

                    }
                }

            }

            @Override
            public void onFailure(Call<FeedBackBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(progressDialog);

                MoudleUtils.toChekWifi(MineFeedBackActivity.this);
            }
        });
    }
}
