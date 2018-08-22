package com.ckjs.ck.Android.CoachModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.SartlessonBean;
import com.ckjs.ck.Bean.TolessonBean;
import com.ckjs.ck.Manager.NotifyJpushClientEndManager;
import com.ckjs.ck.Manager.NotifyJpushClientStartManager;
import com.ckjs.ck.Manager.NotifyJpushCoachEndManager;
import com.ckjs.ck.Manager.NotifyJpushCoachStartManager;
import com.ckjs.ck.Message.NotifyjpushClientEndMessage;
import com.ckjs.ck.Message.NotifyjpushClientStarMessage;
import com.ckjs.ck.Message.NotifyjpushCoachEndMessage;
import com.ckjs.ck.Message.NotifyjpushCoachStarMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
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

public class KcQrActivity extends AppCompatActivity implements NotifyjpushClientStarMessage, NotifyjpushClientEndMessage, NotifyjpushCoachEndMessage, NotifyjpushCoachStarMessage {
    @BindView(R.id.tv_kh)
    TextView tv_kh;
    @BindView(R.id.tv_sj)
    TextView tv_sj;
    @BindView(R.id.tv_zt)
    TextView tv_zt;
    @BindView(R.id.sd_kh)
    SimpleDraweeView sd_kh;
    @BindView(R.id.sd_sj)
    SimpleDraweeView sd_sj;
    @BindView(R.id.tv_jt)
    TextView tv_jt;
    @BindView(R.id.btn_sure)
    TextView btn_sure;
    @BindView(R.id.btn_cancle)
    TextView btn_cancle;
    private String id = "";
    private String class_id = "";
    private String type = "";
    private KyLoadingBuilder kyLoadingBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_kecheng);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        id = intent.getStringExtra("id");
        initThis();
        initToolbar();
        initBtnSure();
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        initCustomTask();
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null) {
                    initSure();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null) {
                    initFinsh();
                }
            }
        });

    }

    private void initThis() {
        if (type != null) {
            if (type.equals("2")) {
                NotifyJpushClientStartManager.getInstance().setNotifyMessage(this);
                NotifyJpushClientEndManager.getInstance().setNotifyMessage(this);
            } else if (type.equals("1")) {
                NotifyJpushCoachEndManager.getInstance().setNotifyMessage(this);
                NotifyJpushCoachStartManager.getInstance().setNotifyMessage(this);
            }
        }
    }

    private void initSure() {
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);

        Call<SartlessonBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).startlesson(token, user_id, class_id, type);
        getSignBeanCall.enqueue(new Callback<SartlessonBean>() {
            @Override
            public void onResponse(Call<SartlessonBean> call, Response<SartlessonBean> response) {
                SartlessonBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {
                            String status = bean.getInfo().getStatus();
                            if (status != null) {
                                if (status.equals("1")) {
                                    initClassStart();
                                } else {
                                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                }
                            } else {
                                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                            }
                        } else {
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                        }
                    } else {
                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                    }
                    ToastUtils.showShort(KcQrActivity.this,bean.getMsg());

                } else {
                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                }
            }

            @Override
            public void onFailure(Call<SartlessonBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);

            }
        });
    }

    private void initClassStart() {
        try {

            initBtnCancleTrue();
            btnCancle();
            initBtnSure();
            initCustomTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBtnCancleTrue() {
        btn_sure.setBackgroundResource(R.color.c_99);
        btn_cancle.setBackgroundResource(R.color.fea21249);
    }

    private void btnCancle() {
        btn_cancle.setEnabled(true);
        btn_sure.setEnabled(false);
    }

    private void btnTrue() {
        btn_sure.setEnabled(true);
        btn_cancle.setEnabled(false);
    }

    private void initFinsh() {
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);

        Call<SartlessonBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).stoplesson(token, user_id, class_id, type);
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
                                                            initClassEnd();

                                                        } else {
                                                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                                        }
                                                    } else {
                                                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                                    }
                                                } else {
                                                    MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                                }
                                            } else {
                                                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                            }
                                            ToastUtils.showShort(KcQrActivity.this,bean.getMsg());
                                        } else {
                                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<SartlessonBean> call, Throwable t) {
                                        MoudleUtils.toChekWifi();
                                        MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                                    }
                                }

        );
    }

    private void initClassEnd() {
        try {
            initbtnSureTrure();
            btnFalse();
            initBtnSure();
            initCustomTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initbtnSureTrure() {
        btn_cancle.setBackgroundResource(R.color.c_99);
        btn_sure.setBackgroundResource(R.color.fea21249);
    }

    private void btnFalse() {
        btn_cancle.setEnabled(false);
        btn_sure.setEnabled(false);
    }

    private void initBtnSure() {
        btn_cancle.setTextColor(getResources().getColor(R.color.c_ffffff));
        btn_sure.setTextColor(getResources().getColor(R.color.c_ffffff));
    }

    TolessonBean bean;

    /**
     *
     */
    private void initCustomTask() {


        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);

        Call<TolessonBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).tolesson(token, user_id, id, type);
        getSignBeanCall.enqueue(new Callback<TolessonBean>() {
            @Override
            public void onResponse(Call<TolessonBean> call, Response<TolessonBean> response) {
                bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {
                            MoudleUtils.textViewSetText(tv_kh, "客户：" + bean.getInfo().getRelname());
                            MoudleUtils.textViewSetText(tv_sj, "教练：" + bean.getInfo().getName());
                            FrescoUtils.setImage(sd_kh, AppConfig.url + bean.getInfo().getPicurl());
                            FrescoUtils.setImage(sd_sj, AppConfig.url_jszd + bean.getInfo().getPicture());
                            class_id = bean.getInfo().getId();
                            String status = bean.getInfo().getStatus();
                            if (status != null) {
                                if (status.equals("0")) {
                                    MoudleUtils.textViewSetText(tv_zt, "未进行");
                                    tv_jt.setTextColor(getResources().getColor(R.color.c_99));
                                    tv_zt.setTextColor(R.color.c_99);
                                    initbtnSureTrure();
                                    btnTrue();
                                } else if (status.equals("1")) {
                                    MoudleUtils.textViewSetText(tv_zt, "进行中");
                                    tv_jt.setTextColor(getResources().getColor(R.color.fea21249));
                                    tv_zt.setTextColor(getResources().getColor(R.color.fea21249));
                                    initBtnCancleTrue();
                                    btnCancle();
                                } else if (status.equals("2")) {
                                    MoudleUtils.textViewSetText(tv_zt, "已完成");
                                    tv_jt.setTextColor(getResources().getColor(R.color.c_99));
                                    tv_zt.setTextColor(getResources().getColor(R.color.c_99));
                                    initbtnSureTrure();
                                    btnFalse();
                                }
                            }
                        }
                    } else {
                        ToastUtils.showShort(KcQrActivity.this,bean.getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);

            }

            @Override
            public void onFailure(Call<TolessonBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);

            }
        });


    }

    /**
     * toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String name = "课程确认";
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        if (type.equals("1")) {
            name = "客户课程确认";
        } else if (type.equals("2")) {
            name = "教练课程确认";
        }
        textView.setText(name);
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

    @Override
    public void sendNotifyJpushClientEndFlag(boolean flag) {
        if (flag) {
            if (type != null) {
                if (type.equals("2")) {
                    initClassToSjEnd();
                }
            }
        }

    }

    private void initClassToSjEnd() {
        try {
            toShow("客户已结束课程,您也快快结束课程吧");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    AlertDialog.Builder builder;
    AlertDialog alert;
    private void toShow(String msg) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");

        builder.setMessage(msg);

        alert = builder.create();
        if (!alert.isShowing()) {
            alert.show();
        }
    }

    @Override
    public void sendNotifyJpushClientStarFlag(boolean flag) {
        if (flag) {
            if (type != null) {
                if (type.equals("2")) {
                    initClassToSjStart();
                }
            }
        }

    }

    private void initClassToSjStart() {
        try {
            toShow("客户已开始课程,您也快快开始课程吧");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendNotifyJpushCoachEndFlag(boolean flag) {

        if (flag) {
            if (type != null) {
                if (type.equals("1")) {
                    initClassEnd();
                }
            }
        }

    }

    @Override
    public void sendNotifyJpushCoachStarFlag(boolean flag) {
        if (flag) {
            if (type != null) {
                if (type.equals("1")) {
                    initClassStart();
                }
            }
        }
    }
}
