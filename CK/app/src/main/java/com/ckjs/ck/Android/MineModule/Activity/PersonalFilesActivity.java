package com.ckjs.ck.Android.MineModule.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.FocusarchivesBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class PersonalFilesActivity extends AppCompatActivity {
    String focusId;
    private ProgressDialog dialog;
    private SimpleDraweeView sd_bg;
    private TextView tv_username;
    private FocusarchivesBean.FocusarchivesInfoBean info;
    private TextView tv_sex;
    private TextView tv_age;
    private TextView tv_today_bushu;
    private TextView tv_week_bushu;
    private TextView tv_today_xiaohao;
    private TextView tv_week_xiaohao;
    private TextView tv_today_run;
    private TextView tv_week_run;
    private TextView tv_health_score;
    private SimpleDraweeView sd_touxiang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_files);
        initGetFocusId();
        initToolbar();
        initId();
        initTask();
    }

    private void initTask() {
        MoudleUtils.dialogShow(dialog);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<FocusarchivesBean> callBack = restApi.focusarchives(token, user_id + "", focusId);

        callBack.enqueue(new Callback<FocusarchivesBean>() {

            @Override
            public void onResponse(Call<FocusarchivesBean> call, Response<FocusarchivesBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            info = response.body().getInfo();
                            initUI();
                        }
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.showShort(PersonalFilesActivity.this, response.body().getMsg());
                        finish();
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(PersonalFilesActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<FocusarchivesBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dialog);
            }
        });
    }

    private void initUI() {
        tv_username.setText(info.getUsername());
        if (info.getSex().equals("1")) {
            tv_sex.setText("男");
        } else {
            tv_sex.setText("女");
        }
        tv_age.setText(info.getAge() + "岁");
        tv_today_bushu.setText(info.getSteps());
        tv_week_bushu.setText(info.getWsteps());
        tv_today_xiaohao.setText(info.getFat());
        tv_week_xiaohao.setText(info.getWfat());
        tv_today_run.setText(info.getRunnum());
        tv_week_run.setText(info.getWrunnum());
        tv_health_score.setText(info.getHealthscore());
        FrescoUtils.setImage(sd_touxiang, AppConfig.url + info.getPicurl());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    DataUtils.downLoadImage(Uri.parse(AppConfig.url + info.getPicurl()), sd_bg, PersonalFilesActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initId() {
        sd_bg = (SimpleDraweeView) findViewById(R.id.sd_bg);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_today_bushu = (TextView) findViewById(R.id.tv_today_bushu);
        tv_week_bushu = (TextView) findViewById(R.id.tv_week_bushu);
        tv_today_xiaohao = (TextView) findViewById(R.id.tv_today_xiaohao);
        tv_week_xiaohao = (TextView) findViewById(R.id.tv_week_xiaohao);
        tv_today_run = (TextView) findViewById(R.id.tv_today_run);
        tv_week_run = (TextView) findViewById(R.id.tv_week_run);
        tv_health_score = (TextView) findViewById(R.id.tv_health_score);
        sd_touxiang = (SimpleDraweeView) findViewById(R.id.sd_touxiang);

        dialog = new ProgressDialog(this);

    }

    private void initGetFocusId() {
        Intent intent = getIntent();
        focusId = intent.getStringExtra("focusId");
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("运动健康档案");
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
}
