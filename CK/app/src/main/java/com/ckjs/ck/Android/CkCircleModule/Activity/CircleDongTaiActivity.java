package com.ckjs.ck.Android.CkCircleModule.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.ckjs.ck.Android.CkCircleModule.Adapter.GridAdapterCkCircleDongTai;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.CircleDongTaiBean;
import com.ckjs.ck.Manager.NotifyCircleRefrushMineMessageManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.Location.Utils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.MyGridView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CircleDongTaiActivity extends AppCompatActivity implements View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener,
        AMapLocationListener {
    @BindView(R.id.tv_palce)
    TextView tv_palce;
    @BindView(R.id.textView20)
    SimpleDraweeView sd_palce;

    private MyGridView myGridView;
    private GridAdapterCkCircleDongTai gridAdapterCkCircleDongTai;
    private List<String> list = new ArrayList<>();
    private String[] iv;
    private EditText editTextContent;
    private String text = "";
    private String name = "";
    private SimpleDraweeView sdQq, sdWx, sdWb;
    private boolean flagQQ = true, flagWX = true, flagWB = true;
    private CircleDongTaiBean bean;
    // 定位相关
    private AMapLocationClient mlocationClient;
    private AMapLocation mLocation;
    private String lat = "";
    private String lon = "";
    public static String filePath = "";//为了解决某些个别手机照相横竖屏无法保留相片的问题而将路径Sting变量改为pub static类型
    private GeocodeSearch geocoderSearch;
    private AlertDialog alert;
    private String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_dong_tai);
        ButterKnife.bind(this);
        tv_palce.setText("");

        initDw();
        initToolbar();
        initId();
        list.add("" + AppConfig.res + R.drawable.tianjiatup);
        iv = new String[]{"", "", "", "", "", ""};
        initAdapter();
        initSet();
    }

    private void initDw() {
        mlocationClient = new AMapLocationClient(this);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位参数
        mlocationClient.setLocationOption(Utils.initLocation());

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            dwstart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                if (mLocation == null) {
                    getAddress(new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude()));
                    lat = amapLocation.getLatitude() + "";
                    lon = amapLocation.getLongitude() + "";
                    city = amapLocation.getCity();
                    tv_palce.setText(city);
                }
                mLocation = amapLocation;


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                ToastUtils.showShort(CircleDongTaiActivity.this,"定位失败，无法获取附近位置信息");

            }
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }
    
    private void dwstop() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dwstop();
    }

    private void dwstart() {

        mlocationClient.startLocation();
    }


    private void toDwPlace() {
        if (mLocation == null) {
            ToastUtils.showShort(CircleDongTaiActivity.this,"尚未定位成功，稍等哦");
        } else {
            if (items == null) {
                ToastUtils.showShort(CircleDongTaiActivity.this,getResources().getString(R.string.no_result));
            } else {
                alert.show();
            }
        }

    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {

        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    private void initSet() {
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initToAddPic(position);
            }
        });

    }


    private void initAdapter() {
        if (gridAdapterCkCircleDongTai == null) {
            gridAdapterCkCircleDongTai = new GridAdapterCkCircleDongTai(this);
            gridAdapterCkCircleDongTai.setList(list);
            myGridView.setAdapter(gridAdapterCkCircleDongTai);
        } else {
            gridAdapterCkCircleDongTai.setList(list);
            gridAdapterCkCircleDongTai.notifyDataSetChanged();
        }
    }

    private void initId() {
        dialog = new ProgressDialog(this);
        myGridView = (MyGridView) findViewById(R.id.sdAddIc);
        editTextContent = (EditText) findViewById(R.id.textView18);
        sdQq = (SimpleDraweeView) findViewById(R.id.textView35);
        sdWx = (SimpleDraweeView) findViewById(R.id.textView36);
        sdWb = (SimpleDraweeView) findViewById(R.id.textView37);
        sdQq.setOnClickListener(this);
        sdWx.setOnClickListener(this);
        sdWb.setOnClickListener(this);
        sdWb.setOnClickListener(this);
        tv_palce.setOnClickListener(this);
        sd_palce.setOnClickListener(this);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    private void initToolbar() {
        TextView textViewCancel = (TextView) findViewById(R.id.toolbar_pretitle);
        textViewCancel.setText("取消");
        textViewCancel.setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("添加动态");
        TextView button = (TextView) findViewById(R.id.toolbar_button);
        button.setText("发送");
        button.setOnClickListener(this);
        RelativeLayout r_toolbar_button = (RelativeLayout) findViewById(R.id.r_toolbar_button);
        r_toolbar_button.setOnClickListener(this);
        RelativeLayout l_toolbar_button = (RelativeLayout) findViewById(R.id.l_toolbar_button);
        l_toolbar_button.setOnClickListener(this);
    }

    private Handler handler = new Handler();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_pretitle:
            case R.id.l_toolbar_button:
                KeyBoardUtils.closeKeyboard(editTextContent, this);
                finish();
                break;
            case R.id.toolbar_button:
            case R.id.r_toolbar_button:
                MoudleUtils.dialogShow(dialog);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initisToDt();
                    }
                }, 1100);

                break;
            case R.id.textView35:
                if (flagQQ) {
                    flagQQ = false;
                    name = "qq";
                    initSetBg();
                } else {
                    flagQQ = true;
                    name = "";
                    FrescoUtils.setImage(sdQq, AppConfig.res + R.drawable.publish_qq_normal);

                }


                break;
            case R.id.textView36:
                if (flagWX) {
                    flagWX = false;
                    name = "wx";
                    initSetBg();
                } else {
                    flagWX = true;
                    name = "";
                    FrescoUtils.setImage(sdWx, AppConfig.res + R.drawable.publish_wechat_normal);
                }
                break;
            case R.id.textView37:
                if (flagWB) {
                    flagWB = false;
                    name = "wb";
                    initSetBg();
                } else {
                    flagWB = true;
                    name = "";
                    FrescoUtils.setImage(sdWb, AppConfig.res + R.drawable.publish_microblog_normal);
                }
                break;
            case R.id.textView20:
            case R.id.tv_palce:
                toDwPlace();
                break;
        }
    }


    /**
     * 控制不语许无内容无图片时走网络接口
     */
    private void initisToDt() {
        initgetText();
        if (text != null && !text.equals("")) {
            initToDongTai();
        } else if (gridAdapterCkCircleDongTai.getCount() > 1) {
            initToDongTai();
        } else {
            MoudleUtils.dialogDismiss(dialog);
            ToastUtils.showShort(CircleDongTaiActivity.this,"未填写任何信息");
        }
    }

    /**
     * 得到发布内容
     */
    private void initgetText() {
        if (editTextContent.getText().toString().trim() != null) {
            if (!editTextContent.getText().toString().trim().equals("")) {
                text = editTextContent.getText().toString().trim();
            }
        }
    }


    private void initSetBg() {
        switch (name) {
            case "qq":
                FrescoUtils.setImage(sdQq, AppConfig.res + R.drawable.publish_qq_pressed);
                FrescoUtils.setImage(sdWx, AppConfig.res + R.drawable.publish_wechat_normal);
                FrescoUtils.setImage(sdWb, AppConfig.res + R.drawable.publish_microblog_normal);
                flagWX = true;
                flagWB = true;
                break;
            case "wx":
                FrescoUtils.setImage(sdQq, AppConfig.res + R.drawable.publish_qq_normal);
                FrescoUtils.setImage(sdWx, AppConfig.res + R.drawable.publish_wechat_pressed);
                FrescoUtils.setImage(sdWb, AppConfig.res + R.drawable.publish_microblog_normal);
                flagQQ = true;
                flagWB = true;
                break;
            case "wb":
                FrescoUtils.setImage(sdQq, AppConfig.res + R.drawable.publish_qq_normal);
                FrescoUtils.setImage(sdWx, AppConfig.res + R.drawable.publish_wechat_normal);
                FrescoUtils.setImage(sdWb, AppConfig.res + R.drawable.publish_micrblog_pressed);
                flagWX = true;
                flagQQ = true;
                break;
        }

    }

    public void initSdAddIc() {
        final CharSequence[] items = {"照片", "相机", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (list.size() < 7) {
                            switch (item) {
                                case 0:
                                    try {
                                        initToXiangChe();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 1:
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                filePath = AppConfig.hss_cache
                                                        + "/"
                                                        + System.currentTimeMillis()
                                                        + AppConfig.JPG;
                                                imageFile = new File(filePath);
                                                imageUri = Uri.fromFile(imageFile);
                                                MoudleUtils.btnclick(imageUri, imageFile, CircleDongTaiActivity.this, AppConfig.hss_cache);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                                    break;
                            }
                        } else {
                            ToastUtils.showShort(CircleDongTaiActivity.this,"最多选择6张图片");
                        }
                    }
                }

        );

        AlertDialog alert = builder.create();
        alert.show();
    }


    private void initToXiangChe() {

        boolean isShowCamera = false;
        AndroidImagePicker.getInstance().setSelectLimit(7 - list.size());
        AndroidImagePicker.getInstance().setCurrentSelectedImageSetPosition(0);
        AndroidImagePicker.getInstance().pickMulti(CircleDongTaiActivity.this, isShowCamera, new AndroidImagePicker.OnImagePickCompleteListener() {


            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                if (items != null && items.size() > 0) {
                    List<String> pathList = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        pathList.add(items.get(i).path);
                    }
                    initToSetGirAdapter(pathList);
                }
            }
        });
    }


    private ProgressDialog dialog;

    /**
     * 进行图片编码
     */
    private void initToDongTai() {
        if (list != null) {
            if (list.size() > 0) {
                if (list.size() > 1) {
                    initIsAddList();
                } else {
                    initTaskPic();
                }
            } else {
                ToastUtils.showShort(CircleDongTaiActivity.this,"图片跑到火星啦");
                MoudleUtils.dialogDismiss(dialog);
            }
        } else {
            ToastUtils.showShort(CircleDongTaiActivity.this,"图片跑到火星啦");
            MoudleUtils.dialogDismiss(dialog);
        }
    }

    /**
     * 进行帖子发布接口
     */
    private void initTaskPic() {
        int userid = (int) SPUtils.get(this, "user_id", 0);
        String token = (String) SPUtils.get(this, "token", "");

        Call<CircleDongTaiBean> call = RetrofitUtils.retrofit.create(NpApi.class).circlePublish(token, userid, text, tv_palce.getText().toString().trim(), iv[0],
                iv[1], iv[2], iv[3], iv[4], iv[5], lat, lon);
        call.enqueue(new Callback<CircleDongTaiBean>() {
            @Override
            public void onResponse(Call<CircleDongTaiBean> call, Response<CircleDongTaiBean> response) {
                CircleDongTaiBean bean = response.body();
                if (bean != null) {
                    initNextData(bean);
                }
                KeyBoardUtils.closeKeyboard(editTextContent, CircleDongTaiActivity.this);
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<CircleDongTaiBean> call, Throwable t) {
                MoudleUtils.toChekWifi(CircleDongTaiActivity.this);
                KeyBoardUtils.closeKeyboard(editTextContent, CircleDongTaiActivity.this);
                MoudleUtils.dialogDismiss(dialog);

            }
        });
    }

    /**
     * 进行贴子接口获取的数据处理
     *
     * @param bean
     */
    private void initNextData(CircleDongTaiBean bean) {
        switch (bean.status) {
            case "1":
                initStatusOne(bean);
                editTextContent.setText("");
                break;
            case "0":
                initStatusZero(bean);
                break;
            case "2":
                initStatusTwo(bean);
                break;
        }
    }

    /**
     * 帖子接口数据获取成功（status=0）
     *
     * @param bean
     */
    private void initStatusTwo(CircleDongTaiBean bean) {
        ToastUtils.showShort(CircleDongTaiActivity.this,bean.msg);

    }

    /**
     * 帖子接口数据获取失败（status=2）
     *
     * @param bean
     */
    private void initStatusZero(CircleDongTaiBean bean) {
        ToastUtils.showShort(this, bean.msg);
    }

    /**
     * 帖子接口数据获取成功(status=1)
     *
     * @param bean
     */
    private void initStatusOne(CircleDongTaiBean bean) {
        this.bean = bean;
        if (name.equals("")) {
            ToastUtils.showShort(this, bean.msg);
            initFinish();
        } else {
            switch (name) {
                case "qq":
                    initToQQ(bean.info.getCircle_id());
                    break;
                case "wx":
                    initToWx(bean.info.getCircle_id());
                    break;
                case "wb":
                    initToWb(bean.info.getCircle_id());
                    break;
            }
        }
    }

    private void initFinish() {
        NotifyCircleRefrushMineMessageManager.getInstance().sendNotifyCircleRefrushFlag(true);
        finish();
    }

    /**
     * qq分享处理
     *
     * @param id
     */
    private void initToQQ(String id) {
        UMImage umImage = getUmImage();
        new ShareAction(CircleDongTaiActivity.this).setPlatform(SHARE_MEDIA.QQ)
                .withTitle("超空圈分享")
                .withText(initGetTitleFx())
                .withTargetUrl("http://www.chaokongs.com/circle/circleinfo?id=" + id)
                .withMedia(umImage)
                .setCallback(umShareListener)
                .share();

    }

    /**
     * 各个分享的图片处理
     *
     * @return
     */
    @NonNull
    private UMImage getUmImage() {
        UMImage umImage;
        if (list != null) {
            if (list.size() > 1) {
                File file = new File(list.get(1));
                if (file.exists()) {
                    umImage = new UMImage(CircleDongTaiActivity.this, file);
                } else {
                    umImage = new UMImage(CircleDongTaiActivity.this, R.drawable.app_icon);
                }
            } else {
                umImage = new UMImage(CircleDongTaiActivity.this, R.drawable.app_icon);
            }
        } else {
            umImage = new UMImage(CircleDongTaiActivity.this, R.drawable.app_icon);
        }
        initGetTitleFx();
        umImage.setThumb(umImage);
        return umImage;
    }

    private String initGetTitleFx() {
        String s_content = "";
        if (text.equals("")) {
            s_content = "点击查看详情";
        } else {
            s_content = text;
        }
        return s_content;
    }

    private void initToWx(String id) {

        UMImage umImage = getUmImage();
        new ShareAction(CircleDongTaiActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .withTitle("超空圈分享")
                .withText(initGetTitleFx())
                .withTargetUrl("http://www.chaokongs.com/circle/circleinfo?id=" + id)
                .withMedia(umImage)
                .setCallback(umShareListener)
                .share();
    }

    private void initToWb(String id) {

        UMImage umImage = getUmImage();
        new ShareAction(CircleDongTaiActivity.this).setPlatform(SHARE_MEDIA.SINA)
                .withTitle("超空圈分享")
                .withText(initGetTitleFx())
                .withTargetUrl("http://www.chaokongs.com/circle/circleinfo?id=" + id)
                .withMedia(umImage)
                .setCallback(umShareListener)
                .share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (bean != null) {
                ToastUtils.showShort(CircleDongTaiActivity.this, bean.msg);
            }
            initFinish();
            Toast.makeText(CircleDongTaiActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(CircleDongTaiActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
            initFinish();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(CircleDongTaiActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            initFinish();
        }

    };
    private static Uri imageUri;
    private static File imageFile;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CircleDongTaiActivity.RESULT_OK) {
            switch (requestCode) {
                case AppConfig.CAPTURE_IMAGE:
                    initAddImage();
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
            ToastUtils.showShort(CircleDongTaiActivity.this,"图片跑丢了，再来一次吧");
        }
        UMShareAPI.get(CircleDongTaiActivity.this).
                onActivityResult(requestCode, resultCode, data);

    }

    private void initAddImage() {
        try {
            if (imageFile != null) {
                if (imageFile.exists()) {
                    if (imageFile.getAbsolutePath() != null) {
                        MoudleUtils.initToseeDongTai(imageFile);//通知相册扫描
                        list.add(imageFile.getAbsolutePath());
                        initAdapter();

                    } else {
                        ToastUtils.showShort(CircleDongTaiActivity.this,"图片跑丢了，再来一次吧");
                    }
                } else {
                    ToastUtils.showShort(CircleDongTaiActivity.this,"图片跑丢了，再来一次吧");
                }
            } else {
                ToastUtils.showShort(CircleDongTaiActivity.this,"图片跑丢了，再来一次吧");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(CircleDongTaiActivity.this,"图片跑丢了，再来一次吧");
        }
    }


    private void initToSetGirAdapter(List<String> pathList) {
        if ((list.size() + pathList.size()) <= 7) {
            if (pathList != null) {
                if (pathList.size() > 0) {
                    list.addAll(pathList);
                    initAdapter();
                }
            }

        } else {
            ToastUtils.showShort(this, "最多可发布六张图片");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("filePath", filePath);
        outState.putStringArrayList("list", (ArrayList<String>) list);//保存图片列表
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (TextUtils.isEmpty(filePath)) {
            String filePath = savedInstanceState.getString("filePath");
            imageFile = new File(filePath);
            imageUri = Uri.fromFile(imageFile);
            list = savedInstanceState.getStringArrayList("list");//获取图片列表
        } else {
            String filePath = savedInstanceState.getString("filePath");
            imageFile = new File(filePath);
            imageUri = Uri.fromFile(imageFile);
            list = savedInstanceState.getStringArrayList("list");//横屏拍照时获取图片列表
            if (list != null) {
                if (list.size() > 1) {
                    initAdapter();
                }
            }
        }

    }

    /**
     * 添加已选择的照片进上传列表并显示
     */
    private void initIsAddList() {
        boolean flag = true;
        if (list.size() <= 7 && list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i) != null) {
                    File file = new File(list.get(i));
                    if (file != null) {
                        if (file.exists()) {
                            Bitmap bitmap = MoudleUtils.getSmallBitmap(list.get(i), 480, 800);
                            if (bitmap != null) {
                                bitmap = MoudleUtils.rotaingImageView(MoudleUtils.readPictureDegree(list.get(i)), bitmap);
                                if (bitmap != null) {
                                    String s = MoudleUtils.Bitmap2Base64(bitmap, 80);
                                    if (bitmap != null) {
                                        bitmap.recycle();
                                    }
                                    if (s != null) {
                                        iv[i - 1] = s;//相册选择的照片处理
                                    } else {
                                        flag = initFileNoexcis(i);
                                        break;
                                    }
                                } else {
                                    flag = initFileNoexcis(i);
                                    break;
                                }
                            } else {
                                flag = initFileNoexcis(i);
                                break;
                            }
                        } else {
                            flag = initFileNoexcis(i);
                            break;
                        }
                    } else {
                        flag = initFileNoexcis(i);
                        break;
                    }
                } else {
                    flag = initFileNoexcis(i);
                    break;
                }
            }
            if (flag) {
                initTaskPic();
            }
        } else {
            ToastUtils.showShort(this, "最多可发布六张图片");
            MoudleUtils.dialogDismiss(dialog);
        }
    }

    private boolean initFileNoexcis(int i) {
        boolean flag;
        ToastUtils.showShort(CircleDongTaiActivity.this,"第" + i + "张图片不存再，请删除丢失图片吧");
        flag = false;
        MoudleUtils.dialogDismiss(dialog);
        return flag;
    }


    private void initToAddPic(int position) {
        if (position == list.size() - 1) {
            if (list.size() < 7) {
                KeyBoardUtils.closeKeyboard(editTextContent, CircleDongTaiActivity.this);
                initSdAddIc();
            } else {
                ToastUtils.showShort(this, "最多可发布六张图片");
            }
        }
    }

    CharSequence[] items;

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                List<PoiItem> aois = result.getRegeocodeAddress().getPois();
                if (aois == null) {
                    return;
                }
                int n = aois.size();
                items = new CharSequence[n + 1];
                if (n > 0) {
                    for (int i = 0; i < n; i++) {
                        items[i] = aois.get(i).getTitle();
                    }
                    items[n] = "不显示位置信息";
                    initToItem();
                } else if (n == 0) {
                    items[n] = "不显示位置信息";
                    initToItem();
                }
            }
        } else {
            items = new CharSequence[1];
            items[0] = "不显示位置信息";
        }
    }

    private void initToItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == items.length - 1) {
                            tv_palce.setText("");
                        } else {
                            tv_palce.setText(city + "·" + items[item]);
                        }

                    }
                }
        );
        alert = builder.create();
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
