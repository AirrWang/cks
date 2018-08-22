package com.ckjs.ck.Android.CoachModule.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Android.CoachModule.Activity.HanXinShiPinPlay.CallManager;
import com.ckjs.ck.Android.CoachModule.Activity.HanXinShiPinPlay.VideoCallActivity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.SartlessonBean;
import com.ckjs.ck.Bean.VideolessonBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleTwoTool;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ToZhiDaoActivity extends AppCompatActivity {
    @BindView(R.id.tv_time)
    TextView tv_tiime;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_lx)
    TextView tv_lx;
    @BindView(R.id.tv_call)
    TextView tv_call;
    @BindView(R.id.tv_jg)
    TextView tv_money;
    @BindView(R.id.custom_sex)
    SimpleDraweeView custom_sex;
    @BindView(R.id.btnVideoGuide)
    Button btnVideoGuide;
    @BindView(R.id.btnVideoGuideEnd)
    Button btnVideoGuideEnd;
    private String id = "";
    private String type = "";
    private String ease_id = "";
    private String rel_name = "";
    private String status = "";
    private String lesson_id = "";
    private String name = "";
    private KyLoadingBuilder kyLoadingBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_zd);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        id = intent.getStringExtra("id");
        initToolbar();
        initCustomTask();
    }

    /**
     * 打电话
     */
    private void initToCall() {
        try {
            String mobile = tv_call.getText().toString().trim();
            if (mobile != null && !mobile.equals("")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "没有相关应用", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     */
    private void initCustomTask() {

        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        String myname = "姓        名：" + "  ";
        if (type.equals("2")) {
            myname = "客        户：" + "  ";
        } else if (type.equals("1")) {
            myname = "教        练：" + "  ";
        }
        Call<VideolessonBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).videolesson(token, user_id, id, type);
        final String finalMyname = myname;
        getSignBeanCall.enqueue(new Callback<VideolessonBean>() {
            @Override
            public void onResponse(Call<VideolessonBean> call, Response<VideolessonBean> response) {
                VideolessonBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {
                            if (type.equals("2")) {
                                MoudleUtils.textViewSetText(tv_name, finalMyname + bean.getInfo().getRelname());
                            } else if (type.equals("1")) {
                                MoudleUtils.textViewSetText(tv_name, finalMyname + bean.getInfo().getName());
                            }
                            MoudleUtils.textViewSetText(tv_tiime, "  " + bean.getInfo().getHandler());
                            MoudleUtils.textViewSetText(tv_call, "  " + bean.getInfo().getTel());
                            tv_call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initToCall();
                                }
                            });

                            MoudleUtils.textViewSetText(tv_money, "  " + "￥" + bean.getInfo().getAmound());
                            String sex = bean.getInfo().getSex();
                            if (sex != null) {
                                if (sex.equals("1")) {
                                    FrescoUtils.setImage(custom_sex, AppConfig.res + R.drawable.my_boy);
                                } else if (sex.equals("2")) {
                                    FrescoUtils.setImage(custom_sex, AppConfig.res + R.drawable.my_girl);
                                }
                            }
                            String s_type = bean.getInfo().getType();
                            if (s_type != null) {
                                if (s_type.equals("distance")) {
                                    MoudleUtils.textViewSetText(tv_lx, "  视频指导");
                                } else if (s_type.equals("personal")) {
                                    MoudleUtils.textViewSetText(tv_lx, "  上门指导");
                                }
                            }
                            lesson_id = bean.getInfo().getId();
                            ease_id = bean.getInfo().getEase_id();
                            rel_name = bean.getInfo().getRelname();
                            name = bean.getInfo().getName();
                            status = bean.getInfo().getStatus();

                            if (status != null) {
                                if (status.equals("2")) {//已结束
                                    btnVideoGuide.setEnabled(false);
                                    btnVideoGuideEnd.setEnabled(false);
                                } else if (status.equals("1")) {//进行中
                                    btnVideoGuide.setEnabled(true);
                                    btnVideoGuideEnd.setEnabled(true);
                                    ToastUtils.showShort(ToZhiDaoActivity.this, "此课程正在进行中，亲您是不是忘记结束啦？");

                                } else if (status.equals("0")) {//未开始
                                    btnVideoGuide.setEnabled(true);
                                    btnVideoGuideEnd.setEnabled(true);
                                    ToastUtils.showShort(ToZhiDaoActivity.this, "祝您有一次愉悦的视频通话课程之旅，快开始吧");
                                }
                            }
                            btnVideoGuide.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initIsHaveWaseId(ease_id);
                                    MoudleTwoTool.initLoginHxAndJpush();//环信和极光登录
                                    toAnswer();
                                }
                            });
                            btnVideoGuideEnd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initIsHaveWaseId(lesson_id);
                                    toEndAnswer();
                                }
                            });
                        }
                    } else {
                        ToastUtils.showShort(ToZhiDaoActivity.this, bean.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<VideolessonBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
            }
        });


    }

    private void initIsHaveWaseId(String id) {
        if (id == null) {
            ToastUtils.showShort(ToZhiDaoActivity.this, "发生异常，请退出当前页面重试");
            return;
        }
        if (id.equals("")) {
            ToastUtils.showShort(ToZhiDaoActivity.this, "发生异常，请退出当前页面重试");
            return;
        }

    }

    private void initFinsh() {
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);

        Call<SartlessonBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).stoplesson(token, user_id, lesson_id, type);
        getSignBeanCall.enqueue(new Callback<SartlessonBean>() {
                                    @Override
                                    public void onResponse(Call<SartlessonBean> call, Response<SartlessonBean> response) {
                                        SartlessonBean bean = response.body();
                                        if (bean != null) {
                                            if (bean.getStatus().equals("1")) {
                                                if (bean.getInfo() != null) {
                                                    String status = bean.getInfo().getStatus();
                                                    if (status != null) {
                                                        if (status.equals("2")) {
                                                            finish();
                                                        }
                                                    }
                                                }

                                            }
                                            ToastUtils.showShort(ToZhiDaoActivity.this, bean.getMsg());
                                        }

                                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                    }

                                    @Override
                                    public void onFailure(Call<SartlessonBean> call, Throwable t) {
                                        MoudleUtils.toChekWifi();
                                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                    }
                                }

        );
    }

    AlertDialog.Builder builder;
    AlertDialog alert;

    /**
     * 进行开启视频通话
     */
    private void toAnswer() {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("您是否确认开启：");
        builder.setMessage("注意：\n若对方成功接通此视频通话课程，\n则[7]天后将自动确认完成!!!\n如有疑问请联系客服!");


        builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startVideoCall();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert = builder.create();
        if (!alert.isShowing()) {
            alert.show();
        }
    }

    /**
     * 进行开启视频通话
     */
    private void toEndAnswer() {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("您是否确认完成此视频通话课程：");
        builder.setMessage("注意：\n若此视频通话课程为已开启状态，\n则无论是否手动确认完成，\n[7]天后都将自动确认完成!!!\n如有疑问请联系客服!");


        builder.setPositiveButton("确认完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initFinsh();
            }
        });
        builder.setNegativeButton("再等等吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert = builder.create();
        if (!alert.isShowing()) {
            alert.show();
        }
    }

    /**
     * make shouhuan_serch video call
     */
    protected void startVideoCall() {
        String class_type = "";
        if (type.equals("1")) {
            class_type = "2";
        } else if (type.equals("2")) {
            class_type = "1";
        }
        String ext = lesson_id + "-" + class_type + "-" + rel_name + "-" + name;
        Intent intent = new Intent(ToZhiDaoActivity.this, VideoCallActivity.class);
        CallManager.getInstance().setChatId(ease_id);
        CallManager.getInstance().setChatName(name);
        CallManager.getInstance().setChatRelName(rel_name);
        CallManager.getInstance().setType(type);
        CallManager.getInstance().setLessonId(lesson_id);
        CallManager.getInstance().setExt(ext);
        CallManager.getInstance().setInComingCall(false);
        CallManager.getInstance().setCallType(CallManager.CallType.VIDEO);
        startActivity(intent);
    }


    /**
     * toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("视频指导");
        setSupportActionBar(toolbar);
        ActionBar actionBar = (getSupportActionBar());
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
