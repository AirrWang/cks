package com.ckjs.ck.Android.MineModule.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.MedicalFileBean;
import com.ckjs.ck.Bean.UpmedicalfileBean;
import com.ckjs.ck.Bean.UupurlBean;
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
import com.ckjs.ck.Tool.ViewTool.WheelView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */


public class MineAimCardActivity extends AppCompatActivity implements View.OnClickListener {

    private SimpleDraweeView touxiang;
    private static Uri imageUri;
    private static File imageFile;
    public static String filePathMineUserInfo = "";
    private ProgressDialog dialog;
    private LinearLayout ll_mine_aim_blood_type;
    private static final String[] STEPNUMBER = new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    private TextView tv_bloodtype;
    private TextView tv_aim_contact;
    private KyLoadingBuilder builder;
    private String realname;
    private EditText et_aim_mcondition;
    private EditText et_aim_note;
    private EditText et_aim_allergy;
    private EditText et_aim_medecin;
    private UupurlBean uupurlbean;
    private MedicalFileBean.MedicalFileInfoBean body;
    private TextView tv_6;
    private TextView tv_card_edite_weight;
    private TextView tv_card_edite_height;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_aimcard_edit);
        builder = new KyLoadingBuilder(this);
        Intent intent = getIntent();
        realname = intent.getStringExtra("realname");
        initToolBar();
        initId();
        initTask();
    }

    private void initTask() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MineAimCardActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(MineAimCardActivity.this, "token", "");

        Call<MedicalFileBean> callBack = restApi.medicalfile(token, user_id + "");

        callBack.enqueue(new Callback<MedicalFileBean>() {
            @Override
            public void onResponse(Call<MedicalFileBean> call, Response<MedicalFileBean> response) {


                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        body = response.body().getInfo();
                        if (body != null) {
                            initUpUiTask();
                        } else {
                            ToastUtils.showShortNotInternet("暂无数据,快去添加吧！");
                            MoudleUtils.kyloadingDismiss(builder);
                        }

                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MineAimCardActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MineAimCardActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<MedicalFileBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineAimCardActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });

    }

    private void initUpUiTask() {
        tv_6.setText(body.getRelname());
        FrescoUtils.setImage(touxiang, AppConfig.url + body.getPicurl());

        initEvData(et_aim_mcondition, body.getMcondition());

        initEvData(et_aim_note, body.getNotes());
        initEvData(et_aim_allergy, body.getAllergy());
        initEvData(et_aim_medecin, body.getDruguse());

        initTvData(tv_bloodtype, body.getBloodtype());

        if (body.getWeight() != null && !body.getWeight().equals("")) {
            tv_card_edite_weight.setText(body.getWeight() + "kg");
        }
        if (body.getHeight() != null && !body.getHeight().equals("")) {
            tv_card_edite_height.setText(body.getHeight() + "cm");
        }
        if (body.getContactname() != null && body.getContacttel() != null && !body.getContactname().equals("") && !body.getContacttel().equals("")) {
            tv_aim_contact.setText(body.getContactname() + "   " + body.getContacttel());
        }

    }

    private void initEvData(EditText textView, String s) {
        if (s != null && !s.equals("")) {
            textView.setText(s);
        }
    }

    private void initTvData(TextView textView, String s) {
        if (s != null && !s.equals("")) {
            textView.setText(s);
        }
    }

    private void initId() {
        tv_aim_contact = (TextView) findViewById(R.id.tv_aim_contact);
        LinearLayout ll_mine_add_contact = (LinearLayout) findViewById(R.id.ll_mine_add_contact);
        tv_bloodtype = (TextView) findViewById(R.id.tv_bloodtype);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        LinearLayout ll_mine_aim_blood_type = (LinearLayout) findViewById(R.id.ll_mine_aim_blood_type);
        touxiang = (SimpleDraweeView) findViewById(R.id.tv_5);
        dialog = new ProgressDialog(this);
        et_aim_mcondition = (EditText) findViewById(R.id.et_aim_mcondition);
        et_aim_note = (EditText) findViewById(R.id.et_aim_note);
        et_aim_allergy = (EditText) findViewById(R.id.et_aim_allergy);
        et_aim_medecin = (EditText) findViewById(R.id.et_aim_medecin);
        tv_card_edite_weight = (TextView) findViewById(R.id.tv_card_edite_weight);
        tv_card_edite_height = (TextView) findViewById(R.id.tv_card_edite_height);

        ll_mine_add_contact.setOnClickListener(this);
        ll_mine_aim_blood_type.setOnClickListener(this);
        touxiang.setOnClickListener(this);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("医疗档案急救卡");
        setSupportActionBar(toolbar);

        TextView button = (TextView) findViewById(R.id.toolbar_button);
        button.setText("完成");
        button.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_button:
                if (initIsToPost(et_aim_mcondition))
                    return;
                if (initIsToPost(et_aim_note))
                    return;
                if (initIsToPost(et_aim_allergy))
                    return;
                if (initIsToPost(et_aim_medecin))
                    return;
                MoudleUtils.kyloadingShow(builder);
                initData();
                break;
            case R.id.tv_5:
                changeUserImage();
                break;
            case R.id.ll_mine_aim_blood_type:
                chooseBloodType();//选择血型
                break;
            case R.id.ll_mine_add_contact:
                toChooseContact();//选择联系人
                break;
        }

    }

    private boolean initIsToPost(EditText s_et_aim_mcondition) {
        String s = s_et_aim_mcondition.getText().toString().trim();
        if (!TextUtils.isEmpty(s)) {
            if (DataUtils.containsEmoji(s)) {
                ToastUtils.showShort(this, "不支持输入Emoji表情符号");
                return true;
            }
        }
        return false;
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MineAimCardActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(MineAimCardActivity.this, "token", "");


        Call<UpmedicalfileBean> callBack = restApi.upmedicalfile(token, user_id + "", et_aim_mcondition.getText().toString().trim(), et_aim_note.getText().toString().trim(),
                et_aim_allergy.getText().toString().trim(), tv_bloodtype.getText().toString().trim(), et_aim_medecin.getText().toString().trim(),
                AppConfig.phonenum, AppConfig.phonename, (String) SPUtils.get(MineAimCardActivity.this, "picurlaim", ""));
        callBack.enqueue(new Callback<UpmedicalfileBean>() {
            @Override
            public void onResponse(Call<UpmedicalfileBean> call, Response<UpmedicalfileBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        ToastUtils.showShort(MineAimCardActivity.this, response.body().getMsg());
                        finish();
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MineAimCardActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MineAimCardActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<UpmedicalfileBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineAimCardActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    private void toChooseContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    private void chooseBloodType() {
        View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(1);
        wv.setItems(Arrays.asList(STEPNUMBER));
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                tv_bloodtype.setText("血型： " + item);
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("添加血型")
                .setView(outerView)
                .show();

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
                        MoudleUtils.btnclickxc(imageUri, imageFile, MineAimCardActivity.this, AppConfig.hss_cache);
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
                                MoudleUtils.btnclick(imageUri, imageFile, MineAimCardActivity.this, AppConfig.hss_cache);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify shouhuan_serch parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            KeyBoardUtils.closeKeyboard(et_aim_note, MineAimCardActivity.this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == MineAimCardActivity.RESULT_OK) {
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
                case 0:
                    if (data == null) {
                        return;
                    }
                    ContentResolver reContentResolverol = getContentResolver();
                    Uri contactData = data.getData();
                    @SuppressWarnings("deprecation")
                    Cursor cursor = managedQuery(contactData, null, null, null, null);
                    cursor.moveToFirst();     // 获取联系人的姓名
                    try {
                        String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        // 获取用户名
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (phone.moveToNext()) {
                            // 获取联系人号码
                            String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            tv_aim_contact.setText(username + ": " + usernumber);
                            AppConfig.phonenum = usernumber;
                            AppConfig.phonename = username;
                        }
                    } catch (Exception e)  //此处为了防止通讯录提示是否点击禁止而抛出异常
                    {
                        tv_aim_contact.setText("");
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void iniTask(String bmp) {
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");
        int user_id = (int) SPUtils.get(this, "user_id", 0);
        Call<UupurlBean> callBack = restApi.uppic(token, user_id, bmp);


        callBack.enqueue(new Callback<UupurlBean>() {
            @Override
            public void onResponse(Call<UupurlBean> call, Response<UupurlBean> response) {
                uupurlbean = response.body();
                if (uupurlbean != null) {
                    if (uupurlbean.getStatus().equals("1")) {
                        FrescoUtils.setImage(touxiang, AppConfig.url + uupurlbean.getInfo().getPicurl());
                        SavaDataLocalUtils.saveDataString(MineAimCardActivity.this, "picurlaim", uupurlbean.getInfo().getPicurl());
                    } else {
                        ToastUtils.show(MineAimCardActivity.this, uupurlbean.getMsg(), 0);
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<UupurlBean> call, Throwable t) {

                ToastUtils.show(MineAimCardActivity.this, getResources().getString(R.string.not_wlan_show), 0);
                MoudleUtils.dialogDismiss(dialog);


            }
        });
    }
}
