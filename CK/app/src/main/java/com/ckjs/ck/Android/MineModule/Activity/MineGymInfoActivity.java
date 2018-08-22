package com.ckjs.ck.Android.MineModule.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetGymInfoBean;
import com.ckjs.ck.Bean.UpmedicalfileBean;
import com.ckjs.ck.Manager.NotifyActivityAddJsfManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
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
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class MineGymInfoActivity extends Activity implements View.OnClickListener {


    private SimpleDraweeView gympic;
    private TextView gymname;
    private TextView gymtel;
    private TextView gymplace;
    private KyLoadingBuilder builder;
    private Button btn_lift_bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_mine_gyminfo);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 1.0);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.8
//        p.alpha = 1.0f;      //设置本身透明度
//        p.dimAmount = 0.8f;      //设置黑暗度
        builder=new KyLoadingBuilder(this);
        initId();
        initTask();
    }

    //网络接口拿数据
    private void initTask() {
        MoudleUtils.kyloadingShow(builder);
        int user_id= (int) SPUtils.get(MineGymInfoActivity.this,"user_id",0);
        String token= (String) SPUtils.get(MineGymInfoActivity.this,"token","");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<GetGymInfoBean> callBack = restApi.mygym(user_id,token);


        callBack.enqueue(new Callback<GetGymInfoBean>() {
            @Override
            public void onResponse(Call<GetGymInfoBean> call, Response<GetGymInfoBean> response) {

                GetGymInfoBean gyminfobean = response.body();
                if (gyminfobean != null) {
                    if (gyminfobean.getStatus().equals("1")) {
                        initUpUiTask(gyminfobean);
                    } else if (gyminfobean.getStatus().equals("0")) {
                        ToastUtils.show(MineGymInfoActivity.this, response.body().getMsg(), 0);
                    } else if (gyminfobean.getStatus().equals("2")) {
                        ToastUtils.showShort(MineGymInfoActivity.this,gyminfobean.getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<GetGymInfoBean> call, Throwable t) {
//                initUi();
                MoudleUtils.toChekWifi(MineGymInfoActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });


    }

    //根据数据更新UI
    private void initUpUiTask(GetGymInfoBean gyminfobean) {
        FrescoUtils.setImage(gympic, AppConfig.url_jszd+gyminfobean.getInfo().getPicture());
        gymname.setText(gyminfobean.getInfo().getName());
        gymtel.setText("电话："+gyminfobean.getInfo().getTel());
        gymplace.setText("地址："+gyminfobean.getInfo().getPlace());
    }

    private void initId() {
        Button btn_lift_bind= (Button) findViewById(R.id.btn_lift_bind);
        gympic = (SimpleDraweeView) findViewById(R.id.gyminfo_pic);
        gymname = (TextView) findViewById(R.id.gyminfo_name);
        gymtel = (TextView) findViewById(R.id.gyminfo_tel);
        gymplace = (TextView) findViewById(R.id.gyminfo_place);

        btn_lift_bind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token= (String) SPUtils.get(MineGymInfoActivity.this,"token","");
        int user_id= (int) SPUtils.get(MineGymInfoActivity.this,"user_id",0);
        Call<UpmedicalfileBean> callBack = restApi.unbindgym(user_id+"",token);

        callBack.enqueue(new Callback<UpmedicalfileBean>() {
            @Override
            public void onResponse(Call<UpmedicalfileBean> call, Response<UpmedicalfileBean> response) {

                if (response.body()!=null){
                    if (response.body().getStatus().equals("0")){
                        ToastUtils.showShort(MineGymInfoActivity.this,response.body().getMsg());
                    }else if (response.body().getStatus().equals("1")){
                        NotifyActivityAddJsfManager.getInstance().sendNotifyFlag(true);
                        ToastUtils.showShort(MineGymInfoActivity.this,response.body().getMsg());
                        finish();
                    }else if (response.body().getStatus().equals("2")){
                        ToastUtils.showShort(MineGymInfoActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);//使登录按钮可按

            }

            @Override
            public void onFailure(Call<UpmedicalfileBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineGymInfoActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });
    }
}
