package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.RelnameBean;
import com.ckjs.ck.Manager.NotifyActivityAddJsfManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class RealNameYsActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.editTextRelName)
    EditText editTextRelName;
    @BindView(R.id.call)
    EditText call;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;


    public static Button time;
    private String token = "";
    private int user_id;
    private String s_editTextRelName = "";
    private String s_call = "";
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_real_name_ys);
        init();
        initGetCall();
        initSetWinDowsWh();
    }

    private void initGetCall() {
        s_call = SPUtils.get(this, "tel", s_call) + "";
        call.setText(s_call);
    }

    /**
     * 进行各个控件的注册以及初始化
     */
    private void init() {
        ButterKnife.bind(this);//绑定View
        dialog = new ProgressDialog(this);
        buttonLogin.setOnClickListener(this);
    }

    /**
     * 设置窗口式activity的宽高
     */
    public void initSetWinDowsWh() {
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.8
//        p.height = (int) (d.getHeight() * 1.0);   //高度设置为屏幕的1.0
//        p.alpha = 1.0f;      //设置本身透明度
//        p.dimAmount = 0.8f;      //设置黑暗度
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                KeyBoardUtils.closeKeyboard(editTextRelName,this);
                toRel();
                break;

        }
    }

    /**
     * 进行实名认证
     */
    private void toRel() {
        if (NetworkUtils.isNetworkAvailable(RealNameYsActivity.this)) {
            initStringData();
            toRegistData();
        } else {
            Toast.makeText(RealNameYsActivity.this, "请检查网络!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 进行非空字符判断，才能进行下一部实名认证网络提交
     */
    private void toRegistData() {
        if (!s_editTextRelName.equals("") && !s_call.equals("")) {
            if (MoudleUtils.checkNameChese(s_editTextRelName)) {
                MoudleUtils.dialogShow(dialog);
                initRegistTask();
            } else {
                Toast.makeText(RealNameYsActivity.this, "真实姓名只能为汉字字符", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RealNameYsActivity.this, "必须填写全部信息", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 进行实名认证网络接口
     */
    private void initRegistTask() {
        user_id = (int) SPUtils.get(this, "user_id", 0);
        token = SPUtils.get(this, "token", token) + "";
        Call<RelnameBean> call = RetrofitUtils.retrofit.create(NpApi.class).relname(token, user_id, s_editTextRelName, s_call, "","");
        call.enqueue(new Callback<RelnameBean>() {
            @Override
            public void onResponse(Call<RelnameBean> call, Response<RelnameBean> response) {
                RelnameBean bean = response.body();//获取解析结果
                handleRegistDate(bean);
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<RelnameBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialog);
                MoudleUtils.toChekWifi(RealNameYsActivity.this);

            }
        });
    }
    private Handler handler = new Handler();

    /**
     * 进行实名认证接口获取的数据处理
     *
     * @param bean
     */
    private void handleRegistDate(RelnameBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                try {
                    MoudleUtils.integralAnimation(this, buttonLogin, "+"+bean.getInfo().getIntegral()+"超空币\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SavaDataLocalUtils.saveDataString(this, "vip", bean.getInfo().getVip());
                AppConfig.vip = (String) SPUtils.get(this, "vip", "");
                SavaDataLocalUtils.saveDataString(this, "tel",  bean.getInfo().getTel());
                SavaDataLocalUtils.saveDataString(this, "relname", bean.getInfo().getRelname());
                ToastUtils.showShort(RealNameYsActivity.this, bean.getMsg());
//                NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);//原来的通知刷新个人信息弃用
                NotifyActivityAddJsfManager.getInstance().sendNotifyFlag(true);//通知个人资料页刷新同时也会刷新个人信息
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
            if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(RealNameYsActivity.this, bean.getMsg());
            }
            if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(RealNameYsActivity.this, bean.getMsg());
            }
        }
    }

    /**
     * 进行手机号验证码真实姓名字段值的获取
     */
    private void initStringData() {
        s_editTextRelName = editTextRelName.getText().toString().trim();
        s_call = call.getText().toString().trim();
    }


}
