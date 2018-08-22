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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.FamilyapplyinfoBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HSFTrueActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private KyLoadingBuilder kyLoadingBuilder;
    private String bind_id = "";
    private TextView tv_username;
    private TextView tv_sex;
    private TextView tv_age;
    private SimpleDraweeView sd_bg;
    private SimpleDraweeView sd_touxiang;
    private TextView tv_user_name;
    private String name;
    private Button button_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsftrue);
        initData();
    }

    private void initData() {
        initGetFocusId();
        initToolbar();
        initId();
        initTask();
    }

    private void initGetFocusId() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        bind_id = intent.getStringExtra("bind_id");
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(name);
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

    private void initId() {
        kyLoadingBuilder = new KyLoadingBuilder(this);
        dialog = new ProgressDialog(this);
        sd_bg = (SimpleDraweeView) findViewById(R.id.sd_bg);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        sd_touxiang = (SimpleDraweeView) findViewById(R.id.sd_touxiang);
        button_sure = (Button) findViewById(R.id.button_sure);

    }

    private void initTask() {
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<FamilyapplyinfoBean> callBack = restApi.familyapplyinfo(user_id, token, bind_id);

        callBack.enqueue(new Callback<FamilyapplyinfoBean>() {

            @Override
            public void onResponse(Call<FamilyapplyinfoBean> call, Response<FamilyapplyinfoBean> response) {
                FamilyapplyinfoBean bean = response.body();
                if (bean == null) return;
                if (bean.getStatus().equals("1")) {
                    initUi(bean.getInfo());
                } else if (bean.getStatus().equals("0")){
                    finish();
                    ToastUtils.showShort(HSFTrueActivity.this,bean.getMsg());
                }else if (bean.getStatus().equals("2")){
                    ToastUtils.showShort(HSFTrueActivity.this,bean.getMsg());
                }
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }

            @Override
            public void onFailure(Call<FamilyapplyinfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
            }
        });
    }

    private void initUi(final FamilyapplyinfoBean.FamilyapplyinfoBeaninfo info) {
        if (info != null) {
            if (info.getStatus() != null) {
                if (info.getStatus().equals("1")) {
                    initHavs();
                } else if (info.getStatus().equals("0")) {
                    initNoHave();
                    initToTure(info);

                }
            }
            tv_username.setText(info.getUsername());
            tv_user_name.setText(info.getUsername());
            if (info.getSex().equals("1")) {
                tv_sex.setText("男");
            } else {
                tv_sex.setText("女");
            }
            tv_age.setText(info.getAge() + "岁");
            FrescoUtils.setImage(sd_touxiang, AppConfig.url + info.getPicurl());
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        DataUtils.downLoadImage(Uri.parse(AppConfig.url + info.getPicurl()), sd_bg, HSFTrueActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void initToTure(final FamilyapplyinfoBean.FamilyapplyinfoBeaninfo info) {
        button_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.getBind_id() == null || info.getBind_id().equals("")) return;
                initTaskSure(info.getBind_id());
            }
        });
    }

    private void initNoHave() {
        button_sure.setBackgroundColor(getResources().getColor(R.color.c_4491F2));
        button_sure.setEnabled(true);
        button_sure.setText("确认加入");
        button_sure.setTextColor(getResources().getColor(R.color.c_ffffff));
    }

    /**
     * circle/familyaccept
     * 3.3.6超空家邀请接受
     */
    private void initTaskSure(String bind_id) {
        MoudleUtils.dialogShow(dialog);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<AcceptBean> callBack = restApi.familyaccept(token, user_id, bind_id, "1");

        callBack.enqueue(new Callback<AcceptBean>() {

            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                AcceptBean bean = response.body();
                if (bean == null) return;
                if (bean.getStatus().equals("1")) {
                    initHavs();
                    ToastUtils.showShort(HSFTrueActivity.this,bean.getMsg());
                } else {
                    ToastUtils.showShort(HSFTrueActivity.this,bean.getMsg());
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

    private void initHavs() {
        button_sure.setBackgroundColor(getResources().getColor(R.color.c_be));
        button_sure.setEnabled(false);
        button_sure.setText("已加入");
        button_sure.setTextColor(getResources().getColor(R.color.c_ffffff));
    }

}
