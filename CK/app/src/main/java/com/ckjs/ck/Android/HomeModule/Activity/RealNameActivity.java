package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.CodeBean;
import com.ckjs.ck.Bean.RelnameBean;
import com.ckjs.ck.Manager.NotifyInfoUpdateMessageManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.PwdCheckUtil;
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
public class RealNameActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.editTextRelName)
    EditText editTextRelName;
    @BindView(R.id.call)
    EditText call;
    @BindView(R.id.yzmCall)
    EditText yzmCall;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    public static Button mytime;


    public static TimeCount rel_nRegist;
    private String token = "";
    private int user_id;
    private String s_editTextRelName = "";
    private String s_call = "";
    private String s_yzmImage = "";
    private String s_pwd = "";
    private ProgressDialog dialog;
    private KyLoadingBuilder kyLoadingBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_real_name);
        mytime = (Button) findViewById(R.id.yzmImage);
        init();
        toBtnState();
        initSetWinDowsWh();
    }

    /**
     * 控制显示验证码按钮状态
     */
    private void toBtnState() {
        if (rel_nRegist == null) {
            btnStart();
        } else {
            btnEnclick();
        }
    }

    /**
     * 进行各个控件的注册以及初始化
     */
    private void init() {
        ButterKnife.bind(this);//绑定View
        dialog = new ProgressDialog(this);
        buttonLogin.setOnClickListener(this);
        mytime.setOnClickListener(this);
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

    /**
     * 限定验证码60秒一次
     */

    private void getCode() {
        if (rel_nRegist == null) {
            if (!call.getText().toString().trim().equals("")) {
                if (MoudleUtils.isPhone(call.getText().toString().trim())) {
                    if (NetworkUtils.isNetworkAvailable(RealNameActivity.this)) {
                        kyLoadingBuilder = new KyLoadingBuilder(this);
                        MoudleUtils.kyloadingShow(kyLoadingBuilder);
                        btnEnclick();
                        rel_nRegist = new TimeCount(60000, 1000);
                        rel_nRegist.start();
                        toGetCodeTask();
                    } else {
                        MoudleUtils.toChekWifi(RealNameActivity.this);
                    }
                } else {
                    ToastUtils.showShortNotInternet("手机号码格式不正确");
                }
            } else {
                Toast.makeText(RealNameActivity.this, "必须填写有效手机号!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 请求验证码
     */
    private void toGetCodeTask() {
        user_id = (int) SPUtils.get(RealNameActivity.this, "user_id", user_id);
        token = (String) SPUtils.get(RealNameActivity.this, "token", token);
        Call<CodeBean> codeBeanCall = RetrofitUtils.retrofit.create(NpApi.class).relsms(token, user_id, call.getText().toString().trim());
        codeBeanCall.enqueue(new Callback<CodeBean>() {
            @Override
            public void onResponse(Call<CodeBean> call, Response<CodeBean> response) {
                CodeBean codeBean = response.body();
                if (codeBean != null) {
                    ToastUtils.show(RealNameActivity.this, codeBean.msg, Toast.LENGTH_SHORT);

                }
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);

            }

            @Override
            public void onFailure(Call<CodeBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                MoudleUtils.toChekWifi(RealNameActivity.this);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yzmImage:
                getCode();
                break;
            case R.id.buttonLogin:
                KeyBoardUtils.closeKeyboard(editTextRelName, this);
                toRel();
                break;

        }
    }

    /**
     * 进行实名认证
     */
    private void toRel() {
        if (NetworkUtils.isNetworkAvailable(RealNameActivity.this)) {
            initStringData();
            toRegistData();
        } else {
            Toast.makeText(RealNameActivity.this, "请检查网络!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 进行非空字符判断，才能进行下一部实名认证网络提交
     */
    private void toRegistData() {
        if (!s_editTextRelName.equals("") && !s_call.equals("") && !s_yzmImage.equals("") && !s_pwd.equals("")) {
            if (MoudleUtils.isPhone(s_call)) {
                if (MoudleUtils.checkNameChese(s_editTextRelName)) {
                    if (s_pwd.length() < 8 && s_pwd.length() > 16) {
                        ToastUtils.show(this, "密码长度为8-16位", Toast.LENGTH_SHORT);
                    } else {
                        if (PwdCheckUtil.isLetterDigit(s_pwd)) {
                            MoudleUtils.dialogShow(dialog);
                            initRegistTask();
                        } else {
                            ToastUtils.showShortNotInternet("密码必须同时含有数字和字母");
                        }
                    }
                } else {
                    Toast.makeText(CkApplication.getInstance(), "真实姓名只能为汉字字符", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RealNameActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RealNameActivity.this, "必须填写全部信息", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 进行实名认证网络接口
     */
    private void initRegistTask() {
        user_id = (int) SPUtils.get(RealNameActivity.this, "user_id", user_id);
        token = (String) SPUtils.get(RealNameActivity.this, "token", token);
        Call<RelnameBean> call = RetrofitUtils.retrofit.create(NpApi.class).relname(token, user_id, s_editTextRelName, s_call, s_yzmImage, s_pwd);
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
                MoudleUtils.toChekWifi(RealNameActivity.this);

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
                    MoudleUtils.integralAnimation(this, buttonLogin, "+" + bean.getInfo().getIntegral() + "超空币\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SavaDataLocalUtils.saveDataString(this, "vip", bean.getInfo().getVip());
                AppConfig.vip = (String) SPUtils.get(this, "vip", "");
                SavaDataLocalUtils.saveDataString(this, "tel", bean.getInfo().getTel());
                SavaDataLocalUtils.saveDataString(this, "relname", bean.getInfo().getRelname());
                ToastUtils.showShort(RealNameActivity.this, bean.getMsg());
                NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
            if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(RealNameActivity.this, bean.getMsg());
            }
            if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(RealNameActivity.this, bean.getMsg());
            }
        }
    }

    /**
     * 进行手机号验证码真实姓名字段值的获取
     */
    private void initStringData() {
        s_editTextRelName = editTextRelName.getText().toString().trim();
        s_call = call.getText().toString().trim();
        s_yzmImage = yzmCall.getText().toString().trim();
        s_pwd = pwd.getText().toString().trim();
    }

    /**
     * 验证码计时器
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            rel_nRegist = null;
            btnStart();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mytime.setText("   重新发送（" + millisUntilFinished / 1000 + "）");
        }
    }


    /**
     * 开始计时
     */
    private void btnStart() {
        mytime.setClickable(true);
        mytime.setText("点击获取");
        mytime.setBackgroundResource(R.drawable.btn_time_be);
    }

    /**
     * 防止重复点击，计时中
     */
    private void btnEnclick() {
        mytime.setClickable(false); //防止重复点击
        mytime.setBackgroundResource(R.drawable.btn_time_hui);
    }
}
