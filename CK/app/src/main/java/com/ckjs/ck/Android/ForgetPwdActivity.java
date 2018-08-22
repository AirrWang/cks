package com.ckjs.ck.Android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.PwdCheckUtil;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.ForgetCodeBean;
import com.ckjs.ck.Bean.UupdapsdBean;
import com.ckjs.ck.Tool.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ForgetPwdActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static Button timeNotPwd;
    public static TimeCount nNotpwd;
    private Button ok;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPasswordTwo;
    private EditText editTextCode;

    private String userName;
    private String password;
    private String prove_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_pwd_call);
        initToolbar();
        initId();
        initData();
        initSet();
    }


    private void initData() {
        toBtnState();
    }

    private void initSet() {
        timeNotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(ForgetPwdActivity.this)) {
                    initTask();
                } else {
                    Toast.makeText(ForgetPwdActivity.this, "请检查网络!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void getCode() {
        if (nNotpwd == null) {
            if (!editTextUsername.getText().toString().trim().equals("")) {
                if (NetworkUtils.isNetworkAvailable(ForgetPwdActivity.this)) {
                    if (MoudleUtils.isPhone(editTextUsername.getText().toString().trim())) {
                        btnEnclick();
                        nNotpwd = new TimeCount(60000, 1000);
                        nNotpwd.start();
                        toGetCodeTask();
                    } else {
                        ToastUtils.showShort(CkApplication.getInstance(), "手机号码格式不正确");
                    }
                } else {
                    Toast.makeText(ForgetPwdActivity.this, "网络未连接!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ForgetPwdActivity.this, "必须填写已注册手机号!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initTask() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            if (!editTextUsername.getText().toString().trim().equals("") && !editTextCode.getText().toString().trim().equals("") && !editTextPassword.getText().toString().trim().equals("") && !editTextPasswordTwo.getText().toString().trim().equals("")) {
                if (editTextPassword.getText().toString().trim().equals(editTextPasswordTwo.getText().toString().trim())) {
                    if (editTextPassword.getText().toString().trim().length() < 8 && editTextPassword.getText().toString().trim().length() > 16) {
                        ToastUtils.show(this, "密码长度为8-16位", Toast.LENGTH_SHORT);
                    } else {
                        if (PwdCheckUtil.isLetterDigit(editTextPassword.getText().toString().trim())) {
                            if (MoudleUtils.isPhone(editTextUsername.getText().toString().trim())) {
                                initStringData();
                                toForgetPwdOkTask();
                            } else {
                                ToastUtils.showShort(CkApplication.getInstance(), "手机号码格式不正确");
                            }
                        } else {
                            ToastUtils.showShortNotInternet("密码必须同时含有数字和字母");
                        }
                    }
                } else {
                    ToastUtils.show(this, "两次密码不一致", Toast.LENGTH_SHORT);
                }
            } else {
                ToastUtils.show(this, "请完整填写所需信息", Toast.LENGTH_SHORT);
            }
        } else {
            ToastUtils.show(this, this.getResources().getString(R.string.not_wlan_show), Toast.LENGTH_SHORT);
        }
    }

    private void initStringData() {
        userName = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        prove_code = editTextCode.getText().toString().trim();
    }

    private ProgressDialog dialog;

    private void toForgetPwdOkTask() {
        MoudleUtils.dialogShow(dialog);
        Call<UupdapsdBean> call = RetrofitUtils.retrofit.create(NpApi.class).updapsd(userName, password, prove_code);
        call.enqueue(new Callback<UupdapsdBean>() {
            @Override
            public void onResponse(Call<UupdapsdBean> call, Response<UupdapsdBean> response) {
                UupdapsdBean bean = response.body();//获取解析结果
                handleRegistDate(bean);
                MoudleUtils.dialogDismiss(dialog);

            }

            @Override
            public void onFailure(Call<UupdapsdBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialog);
                MoudleUtils.toChekWifi(ForgetPwdActivity.this);
            }
        });

    }

    private void handleRegistDate(UupdapsdBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                toLgin();
                Toast.makeText(ForgetPwdActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();

            } else if (bean.getStatus().equals("0")) {
                Toast.makeText(ForgetPwdActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void toLgin() {
        finish();
    }

    private void toGetCodeTask() {
        Call<ForgetCodeBean> updatePwdBeanCall = RetrofitUtils.retrofit.create(NpApi.class).UpdatePwdBean(editTextUsername.getText().toString().trim());
        updatePwdBeanCall.enqueue(new Callback<ForgetCodeBean>() {
            @Override
            public void onResponse(Call<ForgetCodeBean> call, Response<ForgetCodeBean> response) {
                ForgetCodeBean updatePwdBean = response.body();
                if (updatePwdBean != null) {
                    ToastUtils.show(ForgetPwdActivity.this, updatePwdBean.getMsg(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<ForgetCodeBean> call, Throwable t) {
                ToastUtils.show(ForgetPwdActivity.this, t.getMessage(), Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
            timeNotPwd.setText("   重新发送（" + millisUntilFinished / 1000 + "）");
        }
    }

    private void toBtnState() {
        if (nNotpwd == null) {
            btnStart();
        } else {
            btnEnclick();
        }
    }

    private void btnCanClick() {
        nNotpwd = null;
        btnStart();
    }

    private void btnStart() {
        timeNotPwd.setClickable(true);
        timeNotPwd.setText("点击获取");
        timeNotPwd.setBackgroundResource(R.drawable.btn_time_be);
    }

    private void btnEnclick() {
        timeNotPwd.setClickable(false); //防止重复点击
        timeNotPwd.setBackgroundResource(R.drawable.btn_time_hui);
        dialog = new ProgressDialog(this);
    }


    private void initId() {
        timeNotPwd = (Button) findViewById(R.id.yzmImage);
        ok = (Button) findViewById(R.id.buttonLogin);
        editTextUsername = (EditText) findViewById(R.id.call);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordTwo = (EditText) findViewById(R.id.editTextPasswordTwo);
        editTextCode = (EditText) findViewById(R.id.yzmCall);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(getResources().getText(R.string.forget_pwd_activity));//设置标题
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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
