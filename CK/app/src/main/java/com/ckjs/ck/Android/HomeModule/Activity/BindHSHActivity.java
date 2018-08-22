package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.UpmedicalfileBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class BindHSHActivity extends Activity implements View.OnClickListener {

    private Button buttonPay;
    private String ordernum;
    private LinearLayout ll_xy;
    private String bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_hsh);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.34);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.8
//        p.alpha = 1.0f;      //设置本身透明度
//        p.dimAmount = 0.8f;      //设置黑暗度
        Intent intent = getIntent();
        ordernum = intent.getStringExtra("ordernum");
        bind = intent.getStringExtra("bind");
        initId();
    }

    private void initId() {
        ll_xy = (LinearLayout) findViewById(R.id.ll_xy);
        buttonPay = (Button) findViewById(R.id.buttonPay);
        ll_xy.setOnClickListener(this);
        buttonPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_xy:
                Intent intentH5 = new Intent();
                intentH5.putExtra("acurl", "http://www.chaokongs.com/circle/rentagreement");
                intentH5.putExtra("name", "超空手环租赁协议");
                intentH5.setClass(BindHSHActivity.this, HSHH5Activity.class);
                startActivity(intentH5);
                break;
            case R.id.buttonPay:
                if (ordernum == null || ordernum.equals("")) return;
                Intent intent = new Intent();
                intent.putExtra("ordernum", ordernum);
                intent.putExtra("bind", bind);
                intent.setClass(BindHSHActivity.this, DepositActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
