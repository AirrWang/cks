package com.ckjs.ck.Android.MineModule.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.RealNameActivity;
import com.ckjs.ck.Android.HomeModule.Activity.RealNameYsActivity;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.GetInfoBean;
import com.ckjs.ck.Bean.UserInfoBean;
import com.ckjs.ck.Bean.UupurlBean;
import com.ckjs.ck.Manager.NotifyActivityAddJsfManager;
import com.ckjs.ck.Manager.NotifyInfoUpdateMessageManager;
import com.ckjs.ck.Message.NotifyActicityAddJsfMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MineUserInfoEditeActivity extends AppCompatActivity implements View.OnClickListener, NotifyActicityAddJsfMessage {


    private SimpleDraweeView userpic;
    private Toolbar toolbar;
    private EditText agetext;
    private EditText heighttext;
    private EditText petnametext;
    private EditText weighttext;
    private int user_id;
    private LinearLayout activity_user_info_edite;
    private TextView button;
    private TextView sextext;
    private GetInfoBean getinfobean;
    private KyLoadingBuilder builder;
    private EditText signnametext;
    private TextView teltext;
    private TextView relname;
    private TextView user_info_edite_bir_text;
    private static Uri imageUri;
    private static File imageFile;
    public static String filePathMineUserInfo = "";
    private TextView tv_mine_gymname;
    private LinearLayout ll_choose_gym;
    private LinearLayout ll_phone;
    private LinearLayout ll_name;
    private SimpleDraweeView mine_gym_icon_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_info_edite);
        builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);
        initId();
        initToolbar();
        initGetInfo("");

    }

    private void initGetInfo(final String name) {
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        user_id = (int) (SPUtils.get(this, "user_id", 0));
        String token = (String) SPUtils.get(this, "token", "");

        Call<GetInfoBean> callBack = restApi.getinfo(token, user_id);


        callBack.enqueue(new Callback<GetInfoBean>() {
            @Override
            public void onResponse(Call<GetInfoBean> call, Response<GetInfoBean> response) {
                getinfobean = response.body();
                if (getinfobean != null) {
                    if (getinfobean.getStatus().equals("1")) {
                        initUpdatUi(getinfobean);

                        initSetLenth();
                    } else if (getinfobean.getStatus().equals("0")) {
                        ToastUtils.showShort(MineUserInfoEditeActivity.this, getinfobean.getMsg());
                    } else if (getinfobean.getStatus().equals("2")) {
                        ToastUtils.showShort(MineUserInfoEditeActivity.this, getinfobean.getMsg());
                    }
                }
                if (!name.equals("")) {
                    NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<GetInfoBean> call, Throwable t) {

                ToastUtils.show(MineUserInfoEditeActivity.this, getResources().getString(R.string.not_wlan_show), 0);
                MoudleUtils.kyloadingDismiss(builder);


            }
        });
    }

    private void initSetLenth() {
        petnametext.setSelection(petnametext.length());
    }

    private void initUpdatUi(GetInfoBean getinfobean) {
        if (getinfobean != null) {
            FrescoUtils.setImage(userpic, AppConfig.url + getinfobean.getInfo().getPicurl());
            petnametext.setText(getinfobean.getInfo().getUsername());
            int age = getinfobean.getInfo().getAge();
            agetext.setText(age + " ");
            teltext.setText(getinfobean.getInfo().getTel());
            teltext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initToRelNameTel();
                }
            });
            relname.setText(getinfobean.getInfo().getRelname());
            relname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initToRelNameTel();
                }
            });
            heighttext.setText(getinfobean.getInfo().getHeight() + " ");
            weighttext.setText(getinfobean.getInfo().getWeight() + " ");
            signnametext.setText(getinfobean.getInfo().getMotto());
            user_info_edite_bir_text.setText(getinfobean.getInfo().getBirthday());
            tv_mine_gymname.setText(getinfobean.getInfo().getGymname());
            if (getinfobean.getInfo().getGym_id() == 1) {
                FrescoUtils.setImage(mine_gym_icon_1, AppConfig.res + R.drawable.my_member_icon_no);
            } else {
                FrescoUtils.setImage(mine_gym_icon_1, AppConfig.res + R.drawable.my_member_icon);
            }
            ll_choose_gym.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((SPUtils.get(MineUserInfoEditeActivity.this, "vip", "").equals("1"))) {
                        if ((SPUtils.get(MineUserInfoEditeActivity.this, "type", "") + "").equals("1")) {
                            startActivity(new Intent().setClass(MineUserInfoEditeActivity.this, RealNameYsActivity.class));
                        } else {
                            startActivity(new Intent().setClass(MineUserInfoEditeActivity.this, RealNameActivity.class));
                        }
                    } else {
                        bindJSF();
                    }
                }
            });
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "unrentstatus", getinfobean.getInfo().getUnrentstatus());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyanalyse", getinfobean.getInfo().getBodyanalyse());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyinfo", getinfobean.getInfo().getBodyinfo());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "iscoach", getinfobean.getInfo().getIscoach());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "isbind", getinfobean.getInfo().getIsbind());


            if (getinfobean.getInfo().getSex() == 1) {
                sextext.setText("男");
            } else {
                sextext.setText("女");
            }
            initSet();
        } else {
            ToastUtils.show(MineUserInfoEditeActivity.this, "设置失败", 0);
        }
    }

    private void initToRelNameTel() {
        if ((SPUtils.get(MineUserInfoEditeActivity.this, "vip", "").equals("1"))) {
            if ((SPUtils.get(MineUserInfoEditeActivity.this, "type", "") + "").equals("1")) {
                startActivity(new Intent().setClass(MineUserInfoEditeActivity.this, RealNameYsActivity.class));
            } else {
                startActivity(new Intent().setClass(MineUserInfoEditeActivity.this, RealNameActivity.class));
            }
        }
    }

    private synchronized void bindJSF() {
        if (getinfobean != null) {
            if (getinfobean.getInfo() != null) {
                if (getinfobean.getInfo().getGym_id() != 1) {
                    startActivity(new Intent().setClass(MineUserInfoEditeActivity.this, MineGymInfoActivity.class));//我的健身房信息
                } else {
                    startActivity(new Intent().setClass(MineUserInfoEditeActivity.this, MineSerchJSFActivity.class));//搜索健身房
                }
            }
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(getResources().getText(R.string.title_activity_mine_userinfo));
        setSupportActionBar(toolbar);

        button = (TextView) findViewById(R.id.toolbar_button);
        button.setText("保存");
        button.setOnClickListener(this);

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
            KeyBoardUtils.closeKeyboard(petnametext, MineUserInfoEditeActivity.this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void initId() {
        relname = (TextView) findViewById(R.id.user_info_edite_relnametext);
        teltext = (TextView) findViewById(R.id.user_info_edite_teltext);
        signnametext = (EditText) findViewById(R.id.user_info_edite_signnametext);

        sextext = (TextView) findViewById(R.id.user_info_edite_sextext);
        agetext = (EditText) findViewById(R.id.user_info_edite_agetext);

        heighttext = (EditText) findViewById(R.id.user_info_edite_heighttext);

        petnametext = (EditText) findViewById(R.id.user_info_edite_petnametext);

        weighttext = (EditText) findViewById(R.id.user_info_edite_weighttext);

        activity_user_info_edite = (LinearLayout) findViewById(R.id.activity_user_info_edite);
        user_info_edite_bir_text = (TextView) findViewById(R.id.user_info_edite_bir_text);

        userpic = (SimpleDraweeView) findViewById(R.id.mine_userpic);
        dialog = new ProgressDialog(this);

        tv_mine_gymname = (TextView) findViewById(R.id.tv_mine_gymname);
        ll_choose_gym = (LinearLayout) findViewById(R.id.ll_choose_gym);
        ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
        ll_name = (LinearLayout) findViewById(R.id.ll_name);
        mine_gym_icon_1 = (SimpleDraweeView) findViewById(R.id.mine_gym_icon_1);
        NotifyActivityAddJsfManager.getInstance().setNotifyMessage(this);
    }


    public void changeUserImage() {
        final CharSequence[] items = {"相册", "相机", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更换图片");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        filePathMineUserInfo = AppConfig.hss_cache
                                + "/"
                                + System.currentTimeMillis()
                                + AppConfig.JPG;
                        imageFile = new File(filePathMineUserInfo);
                        imageUri = Uri.fromFile(imageFile);
                        MoudleUtils.btnclickxc(imageUri, imageFile, MineUserInfoEditeActivity.this, AppConfig.hss_cache);
                        break;
                    case 1:
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                filePathMineUserInfo = AppConfig.hss_cache
                                        + "/"
                                        + System.currentTimeMillis()
                                        + AppConfig.JPG;
                                imageFile = new File(filePathMineUserInfo);
                                imageUri = Uri.fromFile(imageFile);
                                MoudleUtils.btnclick(imageUri, imageFile, MineUserInfoEditeActivity.this, AppConfig.hss_cache);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("filePath", filePathMineUserInfo);
        //        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (TextUtils.isEmpty(filePathMineUserInfo)) {
            String filePath = savedInstanceState.getString("filePath");
            imageFile = new File(filePath);
            imageUri = Uri.fromFile(imageFile);
        }
        //        Log.d(TAG, "onRestoreInstanceState");
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

    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        //        LogUtils.i(newbmp + "");
        return newbmp;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MineUserInfoEditeActivity.RESULT_OK) {
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
                        //                        LogUtils.i(imageFile.getAbsolutePath());
                        if (imageFile != null) {
                            if (imageFile.exists()) {
                                Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                                if (bmp != null) {
                                    //                                    LogUtils.i(bmp + "");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_userpic:
                changeUserImage();
                break;
            case R.id.toolbar_button:
                //保存修改信息
                if (!user_info_edite_bir_text.getText().equals("")) {
                    toNext();
                }
                break;
        }

    }

    private void toNext() {
        String url = "";
        url = (String) SPUtils.get(this, "picurl", "");
        if (TextUtils.isEmpty(petnametext.getText().toString().trim())) {
            ToastUtils.showShort(this, "昵称不能为空");
            return;
        } else if (DataUtils.containsEmoji(petnametext.getText().toString().trim())) {
            ToastUtils.showShort(this, "不支持输入Emoji表情符号~");
            return;
        } else if (TextUtils.isEmpty(signnametext.getText().toString().trim())) {
            ToastUtils.showShort(this, "个性签名不能为空");
            return;
        } else if (DataUtils.containsEmoji(signnametext.getText().toString().trim())) {
            ToastUtils.showShortNotInternet("不支持输入Emoji表情符号");
            return;
        } else if (TextUtils.isEmpty(heighttext.getText().toString().trim())) {
            ToastUtils.showShort(this, "身高不能为空");
            return;
        } else if (100 > toNumber(heighttext) || toNumber(heighttext) > 250) {
            ToastUtils.showShort(this, "身高不正确（100-250cm以内）");
            return;
        } else if (TextUtils.isEmpty(weighttext.getText().toString().trim())) {
            ToastUtils.showShort(this, "体重不能为空");
            return;
        } else if (30 > toNumberF(weighttext) || toNumberF(weighttext) > 120) {
            ToastUtils.showShort(this, "体重不正确(30-120kg以内)");
            return;
        } else if (0 > toNumber(agetext) || toNumber(agetext) > 200) {
            ToastUtils.showShort(this, "年龄不正确(0-200岁以内)");
            return;
        } else if (url.equals("")) {
            ToastUtils.showShort(this, "请上传头像");
        } else {
            initToInfoTask(url);
        }
    }

    private ProgressDialog dialog;

    private void initToInfoTask(String picurl) {
        MoudleUtils.dialogShow(dialog);
        WfApi userInfoApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");

        System.out.print("----------" + picurl);
        //TODO
        Call<UserInfoBean> callBack = userInfoApi.mineuserinfo(token, (int) SPUtils.get(this, "user_id", 0), petnametext.getText().toString().trim()
                , picurl, toNumber(heighttext), toNumberF(weighttext), getinfobean.getInfo().getSex(), user_info_edite_bir_text.getText().toString().trim(),
                signnametext.getText().toString().trim());

        callBack.enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                UserInfoBean userinfobean = response.body();
                toLoginFinishNext(userinfobean);//成功后的操作
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineUserInfoEditeActivity.this);
                MoudleUtils.dialogDismiss(dialog);

            }
        });
    }

    private void toLoginFinishNext(UserInfoBean userinfobean) {
        if (userinfobean != null) {
            if (userinfobean.getStatus().equals("1")) {
                ToastUtils.show(MineUserInfoEditeActivity.this, userinfobean.getMsg(), 0);
                NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                KeyBoardUtils.closeKeyboard(petnametext, MineUserInfoEditeActivity.this);
                finish();
            } else if (userinfobean.getStatus().equals("0")) {
                ToastUtils.show(MineUserInfoEditeActivity.this, userinfobean.getMsg() + "，无任何信息变动", 0);
            } else if (userinfobean.getStatus().equals("2")) {
                ToastUtils.showShort(MineUserInfoEditeActivity.this, getinfobean.getMsg());
            }
        } else {

        }
    }

    private int toNumber(EditText editText) {
        String str = editText.getText().toString().trim();
        return Integer.parseInt(str);
    }

    private float toNumberF(EditText editText) {
        String str = editText.getText().toString().trim();
        return Float.parseFloat(str);
    }


    private void iniTask(String bmp) {


        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");

        Call<UupurlBean> callBack = restApi.uppic(token, user_id, bmp);


        callBack.enqueue(new Callback<UupurlBean>() {
            @Override
            public void onResponse(Call<UupurlBean> call, Response<UupurlBean> response) {
                UupurlBean uupurlbean = response.body();
                if (uupurlbean != null) {
                    if (uupurlbean.getStatus().equals("1")) {
                        FrescoUtils.setImage(userpic, AppConfig.url + uupurlbean.getInfo().getPicurl());
                        SavaDataLocalUtils.saveDataString(MineUserInfoEditeActivity.this, "picurl", uupurlbean.getInfo().getPicurl());
                    } else {
                        ToastUtils.show(MineUserInfoEditeActivity.this, uupurlbean.getMsg(), 0);
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<UupurlBean> call, Throwable t) {

                ToastUtils.show(MineUserInfoEditeActivity.this, getResources().getString(R.string.not_wlan_show), 0);
                MoudleUtils.dialogDismiss(dialog);


            }
        });
    }

    private void initSet() {
        userpic.setOnClickListener(MineUserInfoEditeActivity.this);
        if (activity_user_info_edite.getVisibility() != View.VISIBLE) {
            activity_user_info_edite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void sendMessageActivityAddJsfFlag(boolean flag) {
        if (flag) {
            try {
                NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                initGetInfo("my");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

