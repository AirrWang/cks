package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

import com.ckjs.ck.Android.HomeModule.Adapter.ListAdapterChooseRelationship;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.BindacceptBean;
import com.ckjs.ck.Bean.MemberApplyBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.MyListView;
import com.facebook.drawee.view.SimpleDraweeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ChooseRelationship extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog dialog;
    private String memberId;
    private MemberApplyBean.MemberApplyInfoBean info;
    private TextView tv_username_friends;
    private TextView tv_sex_friends;
    private TextView tv_age_friends;
    private SimpleDraweeView sd_touxiang_friends;
    private SimpleDraweeView sd_bg;
    private MyListView mlv_relationship;
    private Button btn_relationship_choose;
    private ListAdapterChooseRelationship listAdapterChooseRelationship;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_relationship);
        initToolbar();
        initId();
        initTask();

    }

    private void initTask() {
        MoudleUtils.dialogShow(dialog);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<MemberApplyBean> callBack = restApi.memberapplyinfo(user_id + "", token, memberId);

        callBack.enqueue(new Callback<MemberApplyBean>() {

            @Override
            public void onResponse(Call<MemberApplyBean> call, Response<MemberApplyBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            info = response.body().getInfo();
                            initUI();
                            initSet();
                        }
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.showShort(ChooseRelationship.this, response.body().getMsg());
                        finish();
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(ChooseRelationship.this, response.body().getMsg());
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<MemberApplyBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dialog);
            }
        });

    }

    private void initUI() {
        tv_username_friends.setText(info.getUsername());
        if (info.getSex().equals("1")) {
            tv_sex_friends.setText("男");
        } else {
            tv_sex_friends.setText("女");
        }
        tv_age_friends.setText(info.getAge());

        FrescoUtils.setImage(sd_touxiang_friends, AppConfig.url + info.getPicurl());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    DataUtils.downLoadImage(Uri.parse(AppConfig.url + info.getPicurl()), sd_bg, ChooseRelationship.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initSet() {
        listAdapterChooseRelationship = new ListAdapterChooseRelationship(this);
        listAdapterChooseRelationship.setList(info.getLabel());
        listAdapterChooseRelationship.setMemeberId(memberId);
        mlv_relationship.setAdapter(listAdapterChooseRelationship);

    }

    private void initId() {
        sd_bg = (SimpleDraweeView) findViewById(R.id.sd_bg);
        tv_username_friends = (TextView) findViewById(R.id.tv_username_friends);
        tv_sex_friends = (TextView) findViewById(R.id.tv_sex_friends);
        tv_age_friends = (TextView) findViewById(R.id.tv_age_friends);
        sd_touxiang_friends = (SimpleDraweeView) findViewById(R.id.sd_touxiang_friends);
        mlv_relationship = (MyListView) findViewById(R.id.mlv_relationship);
        btn_relationship_choose = (Button) findViewById(R.id.btn_relationship_choose);
        btn_relationship_choose.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        Intent intent = getIntent();
        memberId = intent.getStringExtra("member_id");

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("选择亲友关系");
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (listAdapterChooseRelationship != null) {
            if (listAdapterChooseRelationship.getRelationship() != null && !listAdapterChooseRelationship.getRelationship().equals("")) {
                initTo(listAdapterChooseRelationship.getRelationship(), listAdapterChooseRelationship.getRelationshipType());
            } else {
                ToastUtils.showShort(ChooseRelationship.this, "请先选择关系，再进行提交!");
            }
        }
    }

    private void initTo(String relationship, String relationshipType) {
        MoudleUtils.dialogShow(dialog);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<BindacceptBean> callBack = restApi.memberapply(user_id + "", token, memberId, relationship, relationshipType);

        callBack.enqueue(new Callback<BindacceptBean>() {
            @Override
            public void onResponse(Call<BindacceptBean> call, Response<BindacceptBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        //成功操作
                        finish();
                        ToastUtils.showShort(ChooseRelationship.this, response.body().getMsg());
                    } else {
                        ToastUtils.showShort(ChooseRelationship.this, response.body().getMsg());
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<BindacceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dialog);
            }
        });
    }
}
