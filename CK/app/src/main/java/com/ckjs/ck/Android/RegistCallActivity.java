package com.ckjs.ck.Android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Android.MineModule.Activity.MineH5Activity;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.PwdCheckUtil;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.CodeBean;
import com.ckjs.ck.Bean.RegistUserBean;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class RegistCallActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static Button time;
    public static TimeCount nRegist;
    private LinearLayout userLook;
    private SimpleDraweeView iv;
    private String allow_argeemet = "1";
    private Button ok;
    private String userName = "";
    private String password = "";
    private String passwordAgain = "";
    private String prove_code = "";
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPasswordAgain;
    private EditText editTextCode;

    private String imei = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_regist_call);
        initToolbar();
        initId();
        initData();
        initSet();
    }

    private void initAllowArgeemet() {
        if (allow_argeemet.equals("1")) {
            //            iv.setBackgroundResource(R.drawable.weixuanzhong);
            FrescoUtils.setImage(iv, AppConfig.res + R.drawable.not_ok);
            allow_argeemet = "0";
        } else if (allow_argeemet.equals("0")) {
            //            iv.setBackgroundResource(R.drawable.xuanzhong);
            FrescoUtils.setImage(iv, AppConfig.res + R.drawable.ok);
            allow_argeemet = "1";
        }
    }

    private void initData() {
        toBtnState();
        TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        imei = "r" + mTm.getDeviceId();
    }

    private void toBtnState() {
        if (nRegist == null) {
            btnStart();
        } else {
            btnEnclick();
        }
    }

    private void initSet() {
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });
        userLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看协议
                Intent intent = new Intent();
                intent.putExtra("acurl", "http://www.chaokongs.com/public/Statement.html");

                intent.setClass(RegistCallActivity.this, MineH5Activity.class);
                startActivity(intent);
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAllowArgeemet();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(RegistCallActivity.this)) {
                    initStringData();
                    toRegistData();
                } else {
                    Toast.makeText(RegistCallActivity.this, "请检查网络!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void getCode() {
        if (nRegist == null) {
            if (!editTextUsername.getText().toString().trim().equals("")) {
                if (NetworkUtils.isNetworkAvailable(RegistCallActivity.this)) {
                    if (MoudleUtils.isPhone(editTextUsername.getText().toString().trim())) {
                        btnEnclick();
                        nRegist = new TimeCount(60000, 1000);
                        nRegist.start();
                        toGetCodeTask();
                    } else {
                        ToastUtils.showShort(CkApplication.getInstance(), "手机号码格式不正确");
                    }
                } else {
                    Toast.makeText(RegistCallActivity.this, "网络未连接!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegistCallActivity.this, "必须填写有效手机号!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toGetCodeTask() {
        Call<CodeBean> codeBeanCall = RetrofitUtils.retrofit.create(NpApi.class).getCode(editTextUsername.getText().toString().trim());
        codeBeanCall.enqueue(new Callback<CodeBean>() {
            @Override
            public void onResponse(Call<CodeBean> call, Response<CodeBean> response) {
                CodeBean codeBean = response.body();
                if (codeBean != null) {
                    ToastUtils.show(RegistCallActivity.this, codeBean.msg, Toast.LENGTH_SHORT);

                }
            }

            @Override
            public void onFailure(Call<CodeBean> call, Throwable t) {
                ToastUtils.show(RegistCallActivity.this, getResources().getString(R.string.not_wlan_show), Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private ProgressDialog dialog;

    private void toRegistData() {
        if (!userName.equals("") && !password.equals("") && !prove_code.equals("") && !passwordAgain.equals("")) {
            if (MoudleUtils.isPhone(userName)) {
                if (password.length() < 8 && password.length() > 16) {
                    ToastUtils.showShort(CkApplication.getInstance(), "密码位数为8-16");

                } else {
                    if (PwdCheckUtil.isLetterDigit(password)) {
                        if (passwordAgain.equals(password)) {
                            MoudleUtils.dialogShow(dialog);
                            initRegistTask();
                        } else {
                            ToastUtils.showShort(CkApplication.getInstance(), "两次密码不一致");
                        }
                    } else {
                        ToastUtils.showShortNotInternet("密码必须同时含有数字和字母");
                    }
                }
            } else {
                ToastUtils.showShort(CkApplication.getInstance(), "手机号码格式不正确");
            }
        } else {
            Toast.makeText(RegistCallActivity.this, "必须填写全部注册信息", Toast.LENGTH_LONG).show();
        }
    }


    private void initRegistTask() {
        Call<RegistUserBean> call = RetrofitUtils.retrofit.create(NpApi.class).regist(userName, password, prove_code);
        call.enqueue(new Callback<RegistUserBean>() {
            @Override
            public void onResponse(Call<RegistUserBean> call, Response<RegistUserBean> response) {
                RegistUserBean bean = response.body();//获取解析结果
                handleRegistDate(bean);
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<RegistUserBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialog);
                MoudleUtils.toChekWifi(RegistCallActivity.this);
            }
        });
    }

    private void handleRegistDate(RegistUserBean bean) {
        if (bean != null) {
            if (bean.status.equals("1")) {
                ToastUtils.showShort(RegistCallActivity.this, bean.msg);
                finish();
            }
            if (bean.status.equals("0")) {
                ToastUtils.showShort(RegistCallActivity.this, bean.msg);
            }
        }
    }


    private void initStringData() {
        userName = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        passwordAgain = editTextPasswordAgain.getText().toString().trim();
        prove_code = editTextCode.getText().toString().trim();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            btnCanClick();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            time.setText("   重新发送（" + millisUntilFinished / 1000 + "）");
        }
    }

    private void btnCanClick() {
        nRegist = null;
        btnStart();
    }

    private void btnStart() {
        //        time.setEnabled(true);
        time.setClickable(true);
        time.setText("点击获取");
        time.setBackgroundResource(R.drawable.btn_time_be);
    }

    private void btnEnclick() {
        //        time.setEnabled(false);
        time.setClickable(false); //防止重复点击
        time.setBackgroundResource(R.drawable.btn_time_hui);
    }


    private void initId() {
        time = (Button) findViewById(R.id.yzmImage);
        userLook = (LinearLayout) findViewById(R.id.UserLook);
        iv = (SimpleDraweeView) findViewById(R.id.ok);
        ok = (Button) findViewById(R.id.buttonLogin);
        editTextUsername = (EditText) findViewById(R.id.call);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordAgain = (EditText) findViewById(R.id.editTextPasswordAgagin);
        editTextCode = (EditText) findViewById(R.id.yzmCall);
        dialog = new ProgressDialog(this);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(getResources().getText(R.string.regist_activity));//设置标题
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
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
            finsihed();
        }

        return super.onOptionsItemSelected(item);
    }


    private void finsihed() {
        finish();
    }
}
