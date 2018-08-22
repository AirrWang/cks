package com.ckjs.ck.Android.MineModule.Activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetNewVersionBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.UpdateAppManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MineUpDataActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mUpdata;
    private Button mUpdataNow;
    private GetNewVersionBean getuodata;
    private LinearLayout updata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_updata);
        initId();
        initToolbar();
        initTaskUpdata();

    }

    private void initData(GetNewVersionBean getuodata) {
        if (getuodata.getInfo() != null) {
            String versonName = getuodata.getInfo().getVername();
            String versonCode = getuodata.getInfo().getVercode();
            String mVname = getVersionName(this);
            String mVcode = getVersionCode(this);
            if (!versonName.equals(mVname)) {
                mUpdata.setText("已有新版本：超空" + versonName);
                mUpdataNow.setEnabled(true);
            } else {
                if (!versonCode.equals(mVcode)) {
                    mUpdata.setText("已有新版本：超空" + versonName);
                    mUpdataNow.setEnabled(true);
                } else {
                    mUpdataNow.setBackgroundResource(R.color.c_99);
                    mUpdata.setText("已是最新版本：超空" + versonName);
                    mUpdataNow.setEnabled(false);
                }
            }

        }

    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static String getVersionCode(Context context) {
        return getPackageInfo(context).versionCode + "";
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    private void initTaskUpdata() {
        int OS = 0;
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<GetNewVersionBean> callBack = restApi.update(OS + "");


        callBack.enqueue(new Callback<GetNewVersionBean>() {
            @Override
            public void onResponse(Call<GetNewVersionBean> call, Response<GetNewVersionBean> response) {

                getuodata = response.body();
                if (getuodata != null) {
                    if (getuodata.getStatus().equals("1")) {
                        updata.setVisibility(View.VISIBLE);
                        initData(getuodata);

                    } else if (getuodata.getStatus().equals("0")) {

                        ToastUtils.show(MineUpDataActivity.this, getuodata.getMsg(), 0);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetNewVersionBean> call, Throwable t) {

                ToastUtils.show(MineUpDataActivity.this, getResources().getString(R.string.not_wlan_show), 0);
            }
        });

    }

    private void initId() {
        updata = (LinearLayout) findViewById(R.id.mine_updata);
        mUpdata = (TextView) findViewById(R.id.mine_updata_info);
        mUpdataNow = (Button) findViewById(R.id.mine_updata_now);
        mUpdataNow.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("版本更新");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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
        if (getuodata == null) return;
        if (getuodata.getInfo() == null) return;
        UpdateAppManager updateManager = new UpdateAppManager(this,"set");
        updateManager.checkUpdateInfo(getuodata.getInfo().getDownurl(), getuodata.getInfo().getIsupdate(), getuodata.getInfo().getVerdec());
    }
}
