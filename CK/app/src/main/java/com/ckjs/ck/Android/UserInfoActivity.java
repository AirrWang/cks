package com.ckjs.ck.Android;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.LoginBean;
import com.ckjs.ck.Bean.UserInfoBean;
import com.ckjs.ck.Bean.UupurlBean;
import com.ckjs.ck.Manager.NotifyPostUserInforMessageManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Service.StepService;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RememberStep.StepDcretor;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private static Uri imageUri;
    private static File imageFile;

    private Button next;

    private EditText et_petName;
    private EditText et_height;
    private EditText et_weight;

    private Toolbar toolbar;
    private TextView et_age;
    private RadioButton info_man;
    private RadioButton info_woman;
    private int sex = 1;
    private SimpleDraweeView user_pic;
    private LoginBean loginBean;
    public static String filePathUserInfo = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  //不显示系统的标题栏
        //  getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //    WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_info);
        initId();
        initGetUser();
        initTest();
        initToolbar();
    }

    /**
     * 获取登录页传来的个人信息数据
     */
    private void initGetUser() {
        Intent intent = this.getIntent();
        loginBean = (LoginBean) intent.getSerializableExtra("user");
    }

    /**
     * 标题栏
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
//        Button button = (Button) findViewById(R.id.toolbar_button);
//        button.setText(getResources().getText(R.string.user_info_jump));
        textView.setText(getResources().getText(R.string.user_info_activity));
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
//        button.setOnClickListener(this);

    }

    private void initTest() {
        next.setOnClickListener(this);
        info_man.setOnClickListener(this);
        info_woman.setOnClickListener(this);
        user_pic.setOnClickListener(this);
    }

    private void initId() {
        next = (Button) findViewById(R.id.info_next);

        et_age = (TextView) findViewById(R.id.et_age);
        et_petName = (EditText) findViewById(R.id.et_petname);
        et_height = (EditText) findViewById(R.id.et_height);
        et_weight = (EditText) findViewById(R.id.et_weight);
        info_man = (RadioButton) findViewById(R.id.info_man);
        info_woman = (RadioButton) findViewById(R.id.info_woman);
        user_pic = (SimpleDraweeView) findViewById(R.id.user_pic);
        dialog = new ProgressDialog(this);

        et_age.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_next:
                toNext();

                break;

            case R.id.info_man:
                sex = 1;

                break;
            case R.id.info_woman:
                sex = 2;

                break;

            case R.id.user_pic:
                changeUserImage();

                break;
            case R.id.et_age:

                showpicker();

                break;
        }

    }

    /**
     * 设置出生年月
     */
    private void showpicker() {

        //Calendar是设定年度日期对象和一个整数字段之间转换的抽象基类
        //获取当前年月日
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(UserInfoActivity.this, 3,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        et_age.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();

    }

    /**
     * 所需上传的个人信息的各项指标的数据控制和判断
     */
    private void toNext() {
        String url = "";
        if (picBean != null) {
            url = picBean.getPicurl();
        }
        if (TextUtils.isEmpty(et_petName.getText().toString().trim())) {
            ToastUtils.showShort(this, "昵称不能为空");
            return;
        } else if (isPetname(et_petName.getText().toString().trim())) {
            ToastUtils.showShort(this, "不能输入特殊字符哦~");
            return;
        } else if (TextUtils.isEmpty(et_height.getText().toString().trim())) {
            ToastUtils.showShort(this, "身高不能为空");
            return;
        } else if (100 > toNumber(et_height) || toNumber(et_height) > 250) {
            ToastUtils.showShort(this, "身高不正确（100-250cm以内）");
            return;
        } else if (TextUtils.isEmpty(et_weight.getText().toString().trim())) {
            ToastUtils.showShort(this, "体重不能为空");
            return;
        } else if (40 > toNumberF(et_weight) || toNumberF(et_weight) > 130) {
            ToastUtils.showShort(this, "体重不正确(40-130kg以内)");
            return;
        } else if (url == null || url.equals("")) {
            ToastUtils.showShort(this, "请上传头像");
        } else if (et_age.getText().toString().trim().equals("")) {
            ToastUtils.showShort(this, "请选择出生年月");
            return;
        } else {
            initToNext(this, url);
        }
    }

    /**
     * 验证昵称是否包含特殊字符
     *
     * @param str
     * @return
     */
    public boolean isPetname(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 提交个人信息时的警醒提示框
     */
    private AlertDialog alertDialog;

    private void initToNext(Context context, final String url) {
        alertDialog = new AlertDialog.Builder(context).setTitle("确认提交")
                .setMessage("性别和出生日期提交后不可更改\n\n身高单位为'cm',体重单位为'kg'")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        initToInfoTask(url);
                    }

                }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create(); // 创建对话框
        alertDialog.setCancelable(false);
        if (alertDialog != null) {
            if (!alertDialog.isShowing()) {
                alertDialog.show(); // 显示
            }
        }
    }

    private ProgressDialog dialog;

    /**
     * 上传个人信息接口
     *
     * @param picurl
     */
    private void initToInfoTask(String picurl) {
        MoudleUtils.dialogShow(dialog);
        WfApi userInfoApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = loginBean.getInfo().getUser_id();
        String token = loginBean.getInfo().getToken();

        System.out.print("----------" + picurl);
        //TODO
        Call<UserInfoBean> callBack = userInfoApi.userinfo(token, user_id, et_petName.getText().toString().trim()
                , picurl, toNumber(et_height), toNumberF(et_weight), sex, et_age.getText().toString().trim());

        callBack.enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                UserInfoBean userinfobean = response.body();
                toLoginFinishNext(userinfobean);//登录成功后的操作
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {

                MoudleUtils.toChekWifi(UserInfoActivity.this);
                MoudleUtils.dialogDismiss(dialog);


            }
        });
    }

    /**
     * 获取的个人信息接口数据的处理
     *
     * @param userinfobean
     */
    private void toLoginFinishNext(UserInfoBean userinfobean) {
        if (userinfobean != null) {
            if (userinfobean.getStatus().equals("1")) {
                ToastUtils.show(UserInfoActivity.this, userinfobean.getMsg(), 0);
                initSaveLoginData(loginBean);
                MoudleUtils.mySetAlias(SPUtils.get(CkApplication.getInstance(), "user_id", 0) + "");//设置别名

                KeyBoardUtils.closeKeyboard(et_petName, UserInfoActivity.this);
                Intent intent = new Intent();
                intent.setClass(UserInfoActivity.this, MainActivity.class);
                startActivity(intent);

                MoudleUtils.initToNotifyLoginSetThis();//登录成功回掉通知
                NotifyPostUserInforMessageManager.getInstance().sendNotifyPostUserInfoFlag(true);//登录成功回掉通知

                finish();
            } else if (userinfobean.getStatus().equals("0")) {
                ToastUtils.show(UserInfoActivity.this, userinfobean.getMsg(), 0);
            } else {
                ToastUtils.show(UserInfoActivity.this, userinfobean.getMsg(), 0);
            }
        } else {

        }
    }

    /**
     * 保存个人信息
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
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "iscoach", loginBean.getInfo().getUser_info().getIscoach());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "unrentstatus", loginBean.getInfo().getUser_info().getUnrentstatus());

        SavaDataLocalUtils.saveDataInt(this, "steps", loginBean.getInfo().getUser_info().getSteps());
        SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "beforDataTiime", StepService.getTodayDate());
        AppConfig.loginStep = loginBean.getInfo().getUser_info().getSteps();
        StepDcretor.stepBoolean = false;
        MoudleUtils.initSaveData(this);
    }

    /**
     * String型强转成int
     *
     * @param editText
     * @return
     */
    private int toNumber(EditText editText) {
        String str = editText.getText().toString().trim();
        int mInt = Integer.parseInt(str);
        return mInt;
    }

    /**
     * String型强转成float
     *
     * @param editText
     * @return
     */
    private float toNumberF(EditText editText) {
        String str = editText.getText().toString().trim();
        float mInt = Float.parseFloat(str);
        return mInt;
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

    /**
     * 选择照片
     */
    public void changeUserImage() {
        final CharSequence[] items = {"相册", "相机", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更换图片");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:

                        filePathUserInfo = AppConfig.hss_cache
                                + "/"
                                + System.currentTimeMillis()
                                + AppConfig.JPG;
                        imageFile = new File(filePathUserInfo);
                        imageUri = Uri.fromFile(imageFile);
                        MoudleUtils.btnclickxc(imageUri, imageFile, UserInfoActivity.this, AppConfig.hss_cache);
                        break;
                    case 1:
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                filePathUserInfo = AppConfig.hss_cache
                                        + "/"
                                        + System.currentTimeMillis()
                                        + AppConfig.JPG;
                                imageFile = new File(filePathUserInfo);
                                imageUri = Uri.fromFile(imageFile);
                                MoudleUtils.btnclick(imageUri, imageFile, UserInfoActivity.this, AppConfig.hss_cache);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    /**
     * 图片修减
     *
     * @param uri
     */
    public void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, AppConfig.CROP_IMAGE);
    }

    /**
     * 图片压缩
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        Log.i(TAG, scaleWidth + "" + scaleHeight);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        Log.i(TAG, newbmp + "");
        return newbmp;
    }

    /**
     * 选择照片后的结果处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UserInfoActivity.RESULT_OK) {
            switch (requestCode) {
                case AppConfig.SELECT_IMAGE:
                    try {
                        if (imageFile != null) {
                            if (imageFile.exists()) {
                                imageUri = Uri.fromFile(imageFile);
                                if (imageUri != null) {
                                    cropImage(data.getData());
                                }
                            } else {
                                ToastUtils.showShortNotInternet("图片跑丢了，再来一次吧");
                            }
                        } else {
                            ToastUtils.showShortNotInternet("图片跑丢了，再来一次吧");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case AppConfig.CAPTURE_IMAGE:
                    try {
                        if (imageFile != null) {
                            if (imageFile.exists()) {
                                MoudleUtils.initToseeDongTai(imageFile);//通知相册扫描
                                imageUri = Uri.fromFile(imageFile);
                                if (imageUri != null) {
                                    cropImage(imageUri);
                                }
                            } else {
                                ToastUtils.showShortNotInternet("图片跑丢了，再来一次吧");
                            }
                        } else {
                            ToastUtils.showShortNotInternet("图片跑丢了，再来一次吧");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case AppConfig.CROP_IMAGE:
                    try {
                        Log.i(TAG, imageFile.getAbsolutePath().toString());
                        if (imageFile != null) {
                            if (imageFile.exists()) {
                                Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                                if (bmp != null) {
                                    Log.i(TAG, bmp + "");
                                    bmp = zoomBitmap(bmp, 160, 160);
                                    if (bmp != null) {
                                        String s = MoudleUtils.Bitmap2Base64(bmp, 80);
                                        if (bmp != null) {
                                            bmp.recycle();
                                        }
                                        if (s != null) {
                                            iniTask(s);
                                        }
                                    }

                                }
                            } else {
                                ToastUtils.showShortNotInternet("图片跑丢了，再来一次吧");
                            }
                        } else {
                            ToastUtils.showShortNotInternet("图片跑丢了，再来一次吧");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        } else {
            try {
                if (imageFile != null) {
                    if (imageFile.exists()) {
                        imageFile.delete();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ToastUtils.showShortNotInternet("图片跑丢了，再来一次吧");
        }
    }

    /**
     * 横竖屏变换时的数据保存
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("filePath", filePathUserInfo);
        Log.d(TAG, "onSaveInstanceState");
    }

    /**
     * 横竖屏变换时的数据提取
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (TextUtils.isEmpty(filePathUserInfo)) {
            String filePath = savedInstanceState.getString("filePath");
            imageFile = new File(filePath);
            imageUri = Uri.fromFile(imageFile);
        }
        Log.d(TAG, "onRestoreInstanceState");
    }

    /**
     * 上传个人头像接口
     *
     * @param bmp
     */
    private void iniTask(String bmp) {
        int user_id = loginBean.getInfo().getUser_id();
        String token = loginBean.getInfo().getToken();

        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);

        Call<UupurlBean> callBack = restApi.uppic(token, user_id, bmp);


        callBack.enqueue(new Callback<UupurlBean>() {
            @Override
            public void onResponse(Call<UupurlBean> call, Response<UupurlBean> response) {
                UupurlBean uupurlbean = response.body();
                initData(uupurlbean);
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<UupurlBean> call, Throwable t) {

                ToastUtils.show(UserInfoActivity.this, t.getMessage(), 0);
                MoudleUtils.dialogDismiss(dialog);

            }
        });
    }

    /**
     * 上传个人头像接口获取数据处理
     *
     * @param uupurlbean
     */
    private UupurlBean.PicBean picBean;

    private void initData(UupurlBean uupurlbean) {
        if (uupurlbean != null) {
            if (uupurlbean.getStatus().equals("1")) {
                picBean = uupurlbean.getInfo();
                if (picBean != null) {
                    SPUtils.put(UserInfoActivity.this, "picurl", picBean.getPicurl());
                    //更新UI
                    FrescoUtils.setImage(user_pic, AppConfig.url + picBean.getPicurl());
                }
            } else {
                ToastUtils.show(UserInfoActivity.this, uupurlbean.getMsg(), 0);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
