package com.ckjs.ck.Android.MineModule.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.UserInfoBean;
import com.ckjs.ck.Manager.NotifyInfoUpdateMessageManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class MineEditHeightActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mine_edit_height;
    private TextView button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mine_edit_height);
        dialog=new ProgressDialog(this);
        mine_edit_height = (EditText) findViewById(R.id.mine_edit_height);

        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("输入体重");
        setSupportActionBar(toolbar);

        button = (TextView) findViewById(R.id.toolbar_button);
        button.setText("保存");
        button.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    private ProgressDialog dialog;

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mine_edit_height.getText().toString().trim())) {
            ToastUtils.showShort(this, "体重不能为空");
            return;
        } else if (30 > toNumberF(mine_edit_height) || toNumberF(mine_edit_height) > 120) {
            ToastUtils.showShort(this, "体重不正确(30-120kg以内)");
            return;
        } else {
            initTask();
        }
    }

    private void initTask() {
        MoudleUtils.dialogShow(dialog);
        WfApi userInfoApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");

        //TODO
        Call<UserInfoBean> callBack = userInfoApi.userinfoweight(token, (int) SPUtils.get(this, "user_id", 0),toNumberF(mine_edit_height));

        callBack.enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                UserInfoBean userinfobean = response.body();
                toLoginFinishNext(userinfobean);//成功后的操作
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineEditHeightActivity.this);
                MoudleUtils.dialogDismiss(dialog);

            }
        });
    }

    private float toNumberF(EditText editText) {
        if(editText==null){
            return 0;
        }
        String str = editText.getText().toString().trim();
        float mF = Float.parseFloat(str);
        return mF;
    }

    private void toLoginFinishNext(UserInfoBean userinfobean) {
        if (userinfobean != null) {
            if (userinfobean.getStatus().equals("1")) {
                ToastUtils.show(MineEditHeightActivity.this, userinfobean.getMsg(), 0);
                SavaDataLocalUtils.saveDataFlot(this, "weight",  toNumberF(mine_edit_height));

                NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                KeyBoardUtils.closeKeyboard(mine_edit_height, MineEditHeightActivity.this);
                finish();
            } else if (userinfobean.getStatus().equals("0")) {
                ToastUtils.show(MineEditHeightActivity.this, userinfobean.getMsg() + "，无任何信息变动", 0);
            } else if (userinfobean.getStatus().equals("2")) {
                ToastUtils.showShort(MineEditHeightActivity.this,userinfobean.getMsg());
            }
        } else {

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
            KeyBoardUtils.closeKeyboard(mine_edit_height, MineEditHeightActivity.this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
