package com.ckjs.ck.Android.MineModule.Activity;

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
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.UpmedicalfileBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
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

public class MineBindJSFActivity extends Activity implements View.OnClickListener {

    private KyLoadingBuilder builder;
    private EditText et_bind_cardnum;
    private Button btn_sure_bind;
    private String gym_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_jsf);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.34);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.8
//        p.alpha = 1.0f;      //设置本身透明度
//        p.dimAmount = 0.8f;      //设置黑暗度
        builder = new KyLoadingBuilder(this);

        Intent intent=getIntent();
        gym_id = intent.getStringExtra("gym_id");
        initId();
    }

    private void initId() {
        TextView tv_bind_realname= (TextView) findViewById(R.id.tv_bind_realname);
        tv_bind_realname.setText((String)SPUtils.get(MineBindJSFActivity.this,"relname",""));
        TextView tv_bind_tel= (TextView) findViewById(R.id.tv_bind_tel);
        tv_bind_tel.setText((String)SPUtils.get(MineBindJSFActivity.this,"tel",""));
        et_bind_cardnum = (EditText) findViewById(R.id.et_bind_cardnum);
        btn_sure_bind = (Button) findViewById(R.id.btn_sure_bind);
        btn_sure_bind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String s = et_bind_cardnum.getText().toString().trim();
        if (s != null && !s.equals("")) {
            if (!DataUtils.containsEmoji(s)) {
                initToPostKh(s);
            } else {
                ToastUtils.showShortNotInternet("不支持输入Emoji表情符号");
            }
        } else {
            ToastUtils.showShort(this, "亲，请输入有效的卡号");
        }
    }

    private void initToPostKh(String s) {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token= (String) SPUtils.get(MineBindJSFActivity.this,"token","");
        int user_id= (int) SPUtils.get(MineBindJSFActivity.this,"user_id",0);
        Call<UpmedicalfileBean> callBack = restApi.bindgym(user_id+"",token,gym_id,s);

        callBack.enqueue(new Callback<UpmedicalfileBean>() {
            @Override
            public void onResponse(Call<UpmedicalfileBean> call, Response<UpmedicalfileBean> response) {

                if (response.body()!=null){
                    if (response.body().getStatus().equals("0")){
                        ToastUtils.showShort(MineBindJSFActivity.this,response.body().getMsg());
                    }else if (response.body().getStatus().equals("1")){
                        ToastUtils.showShort(MineBindJSFActivity.this,response.body().getMsg());
                        finish();
                    }else if (response.body().getStatus().equals("2")){
                        ToastUtils.showShort(MineBindJSFActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);//使登录按钮可按

            }

            @Override
            public void onFailure(Call<UpmedicalfileBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineBindJSFActivity.this);
                MoudleUtils.kyloadingDismiss(builder);//使登录按钮可按

            }
        });
    }
}
