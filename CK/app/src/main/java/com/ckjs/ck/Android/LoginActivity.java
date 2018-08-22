package com.ckjs.ck.Android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.LoginBean;
import com.ckjs.ck.Bean.ThirdLoginBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Service.StepService;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RememberStep.StepDcretor;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private SimpleDraweeView login_qq;
    private SimpleDraweeView login_wx;
    private SimpleDraweeView login_sina;

    private TextView mRegister;
    private EditText et_name, et_pass;
    private Button mLoginButton;
    private TextView mLoginError;

    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private TextWatcher username_watcher;
    private TextWatcher password_watcher;
    private Toolbar toolbar;
    private ProgressDialog dialog;
    private String openid;
    private String acToken;
    private String type = "";
    private String unionid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  //不显示系统的标题栏
        //  getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //    WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_login);
        initId();
        initQqWxWb();
        MoudleUtils.alertDialog = null;//重新登录弹框置null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        SavaDataLocalUtils.saveDataString(this, "type", "1");
    }

    /**
     * 锁定登录返回键，使其失效
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            moveTaskToBack(true);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    /**
     * 第三方登录的监听
     */
    private void initQqWxWb() {
        login_qq.setOnClickListener(clickListener);
        login_wx.setOnClickListener(clickListener);
        login_sina.setOnClickListener(clickListener);
    }

    /**
     * findViewById 绑定控件
     */
    private void initId() {
        dialog = new ProgressDialog(this);
        login_qq = (SimpleDraweeView) findViewById(R.id.login_qq);
        login_wx = (SimpleDraweeView) findViewById(R.id.login_wx);
        login_sina = (SimpleDraweeView) findViewById(R.id.login_sina);

        et_name = (EditText) findViewById(R.id.username);
        et_pass = (EditText) findViewById(R.id.password);

        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye.setOnClickListener(this);
        initWatcher();
        et_name.addTextChangedListener(username_watcher);
        et_pass.addTextChangedListener(password_watcher);

        mLoginButton = (Button) findViewById(R.id.login);
        mLoginError = (TextView) findViewById(R.id.login_error);
        mRegister = (TextView) findViewById(R.id.sigup);

        mLoginButton.setOnClickListener(this);
        mLoginError.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    /**
     * 手机号，密码输入控件公用这一个watcher
     */
    private void initWatcher() {
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                et_pass.setText("");
                if (s.toString().length() > 0) {
                    bt_username_clear.setVisibility(View.VISIBLE);
                } else {
                    bt_username_clear.setVisibility(View.INVISIBLE);
                }
            }
        };

        password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                } else {
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    String s_name = "";
    String s_pd = "";

    /**
     * 各种点击事件
     *
     * @param arg0
     */
    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.login:  //登陆
                s_name = et_name.getText().toString().trim();
                s_pd = et_pass.getText().toString().trim();

                if (s_name != null && s_pd != null) {
                    if (!s_name.equals("") && !s_pd.equals("")) {
                        login();
                    }
                }

                break;
            case R.id.login_error: //忘记密码
                toForget();

                break;
            case R.id.sigup:    //注册新的用户
                toRegist();
                break;
            case R.id.bt_username_clear://至空密码和用户名
                et_name.setText("");
                et_pass.setText("");
                break;
            case R.id.bt_pwd_clear://至空密码
                et_pass.setText("");
                break;
            case R.id.bt_pwd_eye://控制密码的输入字符
                if (et_pass.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {

                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {

                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                et_pass.setSelection(et_pass.getText().toString().length());
                break;
        }
    }

    /**
     * 执行登录接口
     */

    private void login() {
        if (isMobile(et_name.getText().toString().trim())) {
            ToastUtils.showShort(this, "手机号不正确");
            return;
        } else if (TextUtils.isEmpty(et_pass.getText().toString().trim())) {
            ToastUtils.showShort(this, "密码不能为空");
            return;
        }
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<LoginBean> callBack = restApi.login(s_name, s_pd);

        callBack.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                LoginBean loginBean = response.body();
                toLoginFinishNext(loginBean);//登录成功后的操作
                MoudleUtils.dialogDismiss(dialog);//使登录按钮可按
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {

                MoudleUtils.toChekWifi(LoginActivity.this);
                MoudleUtils.dialogDismiss(dialog);//使登录按钮可按

            }
        });

    }

    /**
     * 验证是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 原生手机号登录成功后操作
     *
     * @param loginBean
     */
    private void toLoginFinishNext(LoginBean loginBean) {
        if (loginBean != null) {
            if (loginBean.getStatus().equals("1")) {
                MoudleUtils.hxLG(loginBean.getInfo().getUser_id() + "", loginBean.getInfo().getUser_info().getEasepsd());
                KeyBoardUtils.closeKeyboard(et_name, this);
                toUserInfo(loginBean.getInfo().getUser_info().getHeight(), loginBean);
            } else {
                ToastUtils.show(LoginActivity.this, loginBean.getMsg(), 0);
            }
        } else {

        }
    }


    /**
     * 保存原生手机号登录信息
     *
     * @param loginBean
     */
    private void initSaveLoginData(LoginBean loginBean) {
        SavaDataLocalUtils.saveDataString(this, "token", loginBean.getInfo().getToken());
        SavaDataLocalUtils.saveDataString(this, "isbind", loginBean.getInfo().getUser_info().getIsbind());
        SavaDataLocalUtils.saveDataString(this, "picurl", loginBean.getInfo().getUser_info().getPicurl());
        SavaDataLocalUtils.saveDataString(this, "name", loginBean.getInfo().getUser_info().getUsername());
        SavaDataLocalUtils.saveDataString(this, "fanssum", loginBean.getInfo().getUser_info().getFanssum());
        SavaDataLocalUtils.saveDataInt(this, "user_id", loginBean.getInfo().getUser_id());

        SavaDataLocalUtils.saveDataString(this, "easepsd", loginBean.getInfo().getUser_info().getEasepsd());

        SavaDataLocalUtils.saveDataString(this, "motto", loginBean.getInfo().getUser_info().getMotto());
        SavaDataLocalUtils.saveDataInt(this, "gym_id", loginBean.getInfo().getUser_info().getGym_id());
        SavaDataLocalUtils.saveDataString(this, "gymname", loginBean.getInfo().getUser_info().getGymname());
        SavaDataLocalUtils.saveDataString(this, "vip", loginBean.getInfo().getUser_info().getVip());
        SavaDataLocalUtils.saveDataString(this, "tel", loginBean.getInfo().getUser_info().getTel());
        SavaDataLocalUtils.saveDataInt(this, "height", loginBean.getInfo().getUser_info().getHeight());
        SavaDataLocalUtils.saveDataString(this, "relname", loginBean.getInfo().getUser_info().getRelname());
        SavaDataLocalUtils.saveDataFlot(this, "weight", loginBean.getInfo().getUser_info().getWeight());
        SavaDataLocalUtils.saveDataInt(this, "sex", loginBean.getInfo().getUser_info().getSex());
        SavaDataLocalUtils.saveDataInt(this, "age", loginBean.getInfo().getUser_info().getAge());
        SavaDataLocalUtils.saveDataString(this, "bodyanalyse", loginBean.getInfo().getUser_info().getBodyanalyse());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyinfo", loginBean.getInfo().getBodyinfo());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "unrentstatus", loginBean.getInfo().getUser_info().getUnrentstatus());

        SavaDataLocalUtils.saveDataInt(this, "steps", loginBean.getInfo().getUser_info().getSteps());

        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "beforDataTiime", StepService.getTodayDate());
        AppConfig.loginStep = loginBean.getInfo().getUser_info().getSteps();
        StepDcretor.stepBoolean = false;
        MoudleUtils.initSaveData(this);
        ToastUtils.show(LoginActivity.this, loginBean.getMsg(), Toast.LENGTH_SHORT);
    }

    /**
     * 进行手机号登录成功后跳转个人信息页面
     *
     * @param h
     * @param loginBean
     */
    private void toUserInfo(int h, LoginBean loginBean) {
        if (h == 0) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, UserInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", loginBean);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else if (h > 0) {
            initSaveLoginData(loginBean);
            MoudleUtils.mySetAlias(SPUtils.get(CkApplication.getInstance(), "user_id", 0) + "");//设置别名

            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            MoudleUtils.initToNotifyLoginSetThis();//登录成功回掉通知

            finish();
        }
    }

    /**
     * 进行第三方登录后个人信息跳转
     *
     * @param h
     * @param thirdloginBean
     */
    private void toThredUserInfo(int h, ThirdLoginBean thirdloginBean) {
        if (h == 0) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, ThirdUserInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", thirdloginBean);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else if (h > 0) {
            initSaveThirdLoginData(thirdloginBean);
            MoudleUtils.mySetAlias(SPUtils.get(CkApplication.getInstance(), "user_id", 0) + "");//设置别名

            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            MoudleUtils.initToNotifyLoginSetThis();//登录成功回掉通知

            finish();
        }
    }


    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    /**
     * 第三方登录的类型监听
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.login_qq) {
                UMShareAPI mShareAPI = UMShareAPI.get(LoginActivity.this);
                mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                type = "3";

            } else if (view.getId() == R.id.login_wx) {
                UMShareAPI mShareAPI = UMShareAPI.get(LoginActivity.this);
                mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                type = "2";

            } else if (view.getId() == R.id.login_sina) {
                UMShareAPI mShareAPI = UMShareAPI.get(LoginActivity.this);
                mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.SINA, umAuthListener);
                type = "4";
                //获取用户信息
                //                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, umAuthListener);

            }
        }
    };

    /**
     * 第三方登录的回掉结果注册
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 第三方登录后的回掉
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {


            acToken = data.get("access_token");
            if (data.get("openid") == null) {
                openid = data.get("uid");
            } else {
                openid = data.get("openid");
            }

            unionid = data.get("unionid");

            initThirdLogin();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "授权登陆失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "授权取消", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 第三方登录
     */

    private void initThirdLogin() {
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<ThirdLoginBean> callBack = restApi.thirdlogin(openid, acToken, type,unionid);

        callBack.enqueue(new Callback<ThirdLoginBean>() {
            @Override
            public void onResponse(Call<ThirdLoginBean> call, Response<ThirdLoginBean> response) {
                ThirdLoginBean thirdloginBean = response.body();
                toThirdLoginFinishNext(thirdloginBean);//登录成功后的操作
                MoudleUtils.dialogDismiss(dialog);//使登录按钮可按
            }

            @Override
            public void onFailure(Call<ThirdLoginBean> call, Throwable t) {

                //                ToastUtils.show(LoginActivity.this, t.getMessage(), 0);
                MoudleUtils.toChekWifi(LoginActivity.this);
                MoudleUtils.dialogDismiss(dialog);//使登录按钮可按

            }
        });

    }

    /**
     * 第三方登录成功后操作
     *
     * @param thirdloginBean
     */
    private void toThirdLoginFinishNext(ThirdLoginBean thirdloginBean) {
        if (thirdloginBean != null) {
            if (thirdloginBean.getStatus().equals("1")) {
                MoudleUtils.hxLG(thirdloginBean.getInfo().getUser_id() + "", thirdloginBean.getInfo().getUser_info().getEasepsd());
                toThredUserInfo(thirdloginBean.getInfo().getUser_info().getHeight(), thirdloginBean);
            } else if (thirdloginBean.getStatus().equals("0")) {

            }
        } else {

        }
    }


    /**
     * 保存第三方登录成功后的信息
     *
     * @param thirdloginBean
     */
    private void initSaveThirdLoginData(ThirdLoginBean thirdloginBean) {
        SavaDataLocalUtils.saveDataString(this, "token", thirdloginBean.getInfo().getToken());
        SavaDataLocalUtils.saveDataString(this, "isbind", thirdloginBean.getInfo().getUser_info().getIsbind());
        SavaDataLocalUtils.saveDataString(this, "type", thirdloginBean.getInfo().getType());
        SavaDataLocalUtils.saveDataInt(this, "sex", thirdloginBean.getInfo().getUser_info().getSex());
        SavaDataLocalUtils.saveDataString(this, "picurl", thirdloginBean.getInfo().getUser_info().getPicurl());
        SavaDataLocalUtils.saveDataString(this, "name", thirdloginBean.getInfo().getUser_info().getUsername());
        SavaDataLocalUtils.saveDataString(this, "fanssum", thirdloginBean.getInfo().getUser_info().getFanssum());
        SavaDataLocalUtils.saveDataInt(this, "user_id", thirdloginBean.getInfo().getUser_id());
        SavaDataLocalUtils.saveDataString(this, "easepsd", thirdloginBean.getInfo().getUser_info().getEasepsd());

        SavaDataLocalUtils.saveDataInt(this, "gym_id", thirdloginBean.getInfo().getUser_info().getGym_id());

        SavaDataLocalUtils.saveDataString(this, "gymname", thirdloginBean.getInfo().getUser_info().getGymname());
        SavaDataLocalUtils.saveDataString(this, "vip", thirdloginBean.getInfo().getUser_info().getVip());
        SavaDataLocalUtils.saveDataString(this, "tel", thirdloginBean.getInfo().getUser_info().getTel());
        SavaDataLocalUtils.saveDataInt(this, "height", thirdloginBean.getInfo().getUser_info().getHeight());
        SavaDataLocalUtils.saveDataString(this, "relname", thirdloginBean.getInfo().getUser_info().getRelname());

        SavaDataLocalUtils.saveDataFlot(this, "weight", thirdloginBean.getInfo().getUser_info().getWeight());

        SavaDataLocalUtils.saveDataString(this, "motto", thirdloginBean.getInfo().getUser_info().getMotto());
        SavaDataLocalUtils.saveDataInt(this, "age", thirdloginBean.getInfo().getUser_info().getAge());
        SavaDataLocalUtils.saveDataString(this, "bodyanalyse", thirdloginBean.getInfo().getUser_info().getBodyanalyse());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyinfo", thirdloginBean.getInfo().getBodyinfo());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "iscoach", thirdloginBean.getInfo().getUser_info().getIscoach());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "unrentstatus", thirdloginBean.getInfo().getUser_info().getUnrentstatus());

        SavaDataLocalUtils.saveDataInt(this, "steps", thirdloginBean.getInfo().getUser_info().getSteps());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "beforDataTiime", StepService.getTodayDate());
        AppConfig.loginStep = thirdloginBean.getInfo().getUser_info().getSteps();

        StepDcretor.stepBoolean = false;
        MoudleUtils.initSaveData(this);
        ToastUtils.show(LoginActivity.this, thirdloginBean.getMsg(), Toast.LENGTH_SHORT);
    }

    /**
     * 跳转到注册页面
     */
    protected void toRegist() {
        startActivity(new Intent().setClass(LoginActivity.this, RegistCallActivity.class));
    }

    /**
     * 跳转到忘记密码页面
     */
    protected void toForget() {
        startActivity(new Intent().setClass(LoginActivity.this, ForgetPwdActivity.class));
    }


}
