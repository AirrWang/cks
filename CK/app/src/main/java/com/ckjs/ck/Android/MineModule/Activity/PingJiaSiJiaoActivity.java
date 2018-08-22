package com.ckjs.ck.Android.MineModule.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ckjs.ck.Bean.CoachcommBean;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
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
public class PingJiaSiJiaoActivity extends AppCompatActivity {
    @BindView(R.id.pingjia_jiaolian_tuxiang)
    SimpleDraweeView pingjia_jiaolian_tuxiang;
    @BindView(R.id.pingjia_jiaolian_name)
    TextView pingjia_jiaolian_name;
    @BindView(R.id.sd_sex)
    SimpleDraweeView sd_sex;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_pj)
    TextView tv_pj;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_2)
    TextView tv_2;
    @BindView(R.id.tv_3)
    TextView tv_3;
    @BindView(R.id.tv_4)
    TextView tv_4;
    @BindView(R.id.sd_1)
    SimpleDraweeView sd_1;
    @BindView(R.id.sd_2)
    SimpleDraweeView sd_2;
    @BindView(R.id.sd_3)
    SimpleDraweeView sd_3;
    @BindView(R.id.sd_4)
    SimpleDraweeView sd_4;
    @BindView(R.id.sd_5)
    SimpleDraweeView sd_5;
    @BindView(R.id.tv_yq)
    TextView tv_yq;
    @BindView(R.id.btn_tj)
    Button btn_tj;
    @BindView(R.id.e_msg)
    EditText e_msg;
    private KyLoadingBuilder builder;
    private String id = "";
    private boolean flag1, flag2, flag3, flag4, flag5;//未选中true
    int n = 5;
    private ProgressDialog dilag;
    private boolean flag_t1;
    private boolean flag_t2;
    private boolean flag_t3;
    private boolean flag_t4;
    private String s1 = "";
    private String s2 = "";
    private String s3 = "";
    private String s4 = "";

    //1:非常不满意，各方面都很差；2：不满意，比较差；3：一般，需要改善；4：比较满意，但仍可改善；5：非常满意，无可挑剔
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sijiao_pingjia);
        ButterKnife.bind(this);
        initToolbar();
        dilag = new ProgressDialog(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        initCustomTask();
        initSet();

    }

    private void initSet() {
        sd_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag1) {
                    flag1 = false;
                    FrescoUtils.setImage(sd_1, AppConfig.res + R.drawable.evaluate_color);
                    n++;
                } else {
                    flag1 = true;
                    FrescoUtils.setImage(sd_1, AppConfig.res + R.drawable.evaluate);
                    n--;
                }
                initN();
            }
        });
        sd_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag2) {
                    flag2 = false;
                    FrescoUtils.setImage(sd_2, AppConfig.res + R.drawable.evaluate_color);
                    n++;
                } else {
                    flag2 = true;
                    FrescoUtils.setImage(sd_2, AppConfig.res + R.drawable.evaluate);
                    n--;
                }
                initN();
            }
        });
        sd_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag3) {
                    flag3 = false;
                    FrescoUtils.setImage(sd_3, AppConfig.res + R.drawable.evaluate_color);
                    n++;
                } else {
                    flag3 = true;
                    FrescoUtils.setImage(sd_3, AppConfig.res + R.drawable.evaluate);
                    n--;
                }
                initN();
            }
        });
        sd_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag4) {
                    flag4 = false;
                    FrescoUtils.setImage(sd_4, AppConfig.res + R.drawable.evaluate_color);
                    n++;
                } else {
                    flag4 = true;
                    FrescoUtils.setImage(sd_4, AppConfig.res + R.drawable.evaluate);
                    n--;
                }
                initN();
            }
        });
        sd_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag5) {
                    flag5 = false;
                    FrescoUtils.setImage(sd_5, AppConfig.res + R.drawable.evaluate_color);
                    n++;
                } else {
                    flag5 = true;
                    FrescoUtils.setImage(sd_5, AppConfig.res + R.drawable.evaluate);
                    n--;
                }
                initN();
            }
        });
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (flag_t1) {
                    flag_t1 = false;
                    tv_1.setBackgroundResource(R.drawable.text_retangle_biankuang);
                    tv_1.setTextColor(getResources().getColor(R.color.c_99));
                    s1 = "";
                } else {
                    flag_t1 = true;
                    tv_1.setBackgroundResource(R.color.fea21249);
                    tv_1.setTextColor(getResources().getColor(R.color.c_ffffff));
                    s1 = tv_1.getText().toString().trim();

                }
            }
        });
        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag_t2) {
                    flag_t2 = false;
                    tv_2.setBackgroundResource(R.drawable.text_retangle_biankuang);
                    tv_2.setTextColor(getResources().getColor(R.color.c_99));
                    s2 = "";
                } else {
                    flag_t2 = true;
                    tv_2.setBackgroundResource(R.color.fea21249);
                    tv_2.setTextColor(getResources().getColor(R.color.c_ffffff));
                    s2 = tv_2.getText().toString().trim();

                }

            }
        });
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_t3) {
                    flag_t3 = false;
                    tv_3.setBackgroundResource(R.drawable.text_retangle_biankuang);
                    tv_3.setTextColor(getResources().getColor(R.color.c_99));
                    s3 = "";
                } else {
                    flag_t3 = true;
                    tv_3.setBackgroundResource(R.color.fea21249);
                    tv_3.setTextColor(getResources().getColor(R.color.c_ffffff));
                    s3 = tv_3.getText().toString().trim();

                }

            }
        });
        tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_t4) {
                    flag_t4 = false;
                    tv_4.setBackgroundResource(R.drawable.text_retangle_biankuang);
                    tv_4.setTextColor(getResources().getColor(R.color.c_99));
                    s4 = "";
                } else {
                    flag_t4 = true;
                    tv_4.setBackgroundResource(R.color.fea21249);
                    tv_4.setTextColor(getResources().getColor(R.color.c_ffffff));
                    s4 = tv_4.getText().toString().trim();

                }

            }
        });
    }

    private void initTvFlag(boolean flag_t1, TextView textView, String s) {

    }

    private void initN() {
        switch (n) {
            case 1:
                MoudleUtils.textViewSetText(tv_pj, "非常不满意，各方面都很差");
                initNoGood(info);
                break;
            case 2:
                MoudleUtils.textViewSetText(tv_pj, "不满意，比较差");
                initNoGood(info);
                break;
            case 3:
                MoudleUtils.textViewSetText(tv_pj, "一般，需要改善");
                initNoGood(info);
                break;
            case 4:
                MoudleUtils.textViewSetText(tv_pj, "比较满意，但仍可改善");
                initNoGood(info);
                break;
            case 5:
                MoudleUtils.textViewSetText(tv_pj, "非常满意，无可挑剔");
                initGood(info);
                break;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("评价");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    CoachcommBean.CoachcommBeanInfo info;

    /**
     *
     */
    private void initCustomTask() {
        builder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(builder);
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");

        Call<CoachcommBean> callBack = restApi.coachcomm(token, user_id, id);

        callBack.enqueue(new Callback<CoachcommBean>() {
            @Override
            public void onResponse(Call<CoachcommBean> call, Response<CoachcommBean> response) {
                CoachcommBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        info = bean.getInfo();
                        if (info != null) {
                            initTaskDataInfo(info);
                            btn_tj.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (info != null) {
                                        String s = "";
                                        s = s1 + s2 + s3 + s4;
                                        if (s != null) {
                                            if (s.equals("")) {
                                                ToastUtils.showShortNotInternet("请选择至少一项评价内容");
                                            } else {
                                                toJieDan();
                                            }
                                        } else {
                                            ToastUtils.showShortNotInternet("请选择至少一项评价内容");
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        ToastUtils.showShort(PingJiaSiJiaoActivity.this,bean.getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<CoachcommBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    AlertDialog.Builder albuilder;
    AlertDialog alert;

    private void toJieDan() {
        if (albuilder == null) {
            albuilder = new AlertDialog.Builder(this);
        }
        albuilder.setMessage("您是否确认评价：");


        albuilder.setPositiveButton("评价", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = tv_pj.getText().toString().trim() + e_msg.getText().toString().trim();
                if (s != null && !s.equals("")) {
                    if (!DataUtils.containsEmoji(s)) {
                        MoudleUtils.dialogShow(dilag);
                        initJieDanTask(s);
                    } else {
                        ToastUtils.showShortNotInternet("不支持输入Emoji表情符号");
                    }
                } else {
                    ToastUtils.showShortNotInternet("评价不能为空");
                }
            }
        });
        albuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert = albuilder.create();
        if (!alert.isShowing()) {
            alert.show();
        }
    }

    private void initJieDanTask(String s) {

        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        Call<AcceptBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).coachcomment(token, user_id, id, s, n + "");
        getSignBeanCall.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                AcceptBean bean = response.body();
                if (bean != null) {

                    ToastUtils.showShort(PingJiaSiJiaoActivity.this,bean.getMsg());
                    if (bean.getStatus().equals("1")) {
                        finish();
                    }
                }
                MoudleUtils.dialogDismiss(dilag);
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dilag);

            }
        });
    }

    private void initTaskDataInfo(CoachcommBean.CoachcommBeanInfo info) {

        String sex = info.getSex();
        if (sex.equals("2")) {
            FrescoUtils.setImage(sd_sex, AppConfig.res + R.drawable.my_girl);
        } else if (sex.equals("1")) {
            FrescoUtils.setImage(sd_sex, AppConfig.res + R.drawable.my_boy);
        } else {
            FrescoUtils.setImage(sd_sex, "");
        }
        FrescoUtils.setImage(pingjia_jiaolian_tuxiang, AppConfig.url_jszd + info.getPicture());
        MoudleUtils.textViewSetText(tv_money, "￥" + info.getAmount());
        MoudleUtils.textViewSetText(pingjia_jiaolian_name, info.getName());
        initGood(info);

    }

    private void initGood(CoachcommBean.CoachcommBeanInfo info) {
        if (info != null) {
            if (info.getGoodlabel() == null || info.getGoodlabel().size() < 4) return;
            MoudleUtils.textViewSetText(tv_1, info.getGoodlabel().get(0).getLabel());
            MoudleUtils.textViewSetText(tv_2, info.getGoodlabel().get(1).getLabel());
            MoudleUtils.textViewSetText(tv_3, info.getGoodlabel().get(2).getLabel());
            MoudleUtils.textViewSetText(tv_4, info.getGoodlabel().get(3).getLabel());
            MoudleUtils.textViewSetText(tv_yq, "指出优点");
        }
    }

    private void initNoGood(CoachcommBean.CoachcommBeanInfo info) {
        if (info != null) {
            if (info.getBadlabel() == null || info.getBadlabel().size() < 4) return;
            MoudleUtils.textViewSetText(tv_1, info.getBadlabel().get(0).getLabel());
            MoudleUtils.textViewSetText(tv_2, info.getBadlabel().get(1).getLabel());
            MoudleUtils.textViewSetText(tv_3, info.getBadlabel().get(2).getLabel());
            MoudleUtils.textViewSetText(tv_4, info.getBadlabel().get(3).getLabel());
            MoudleUtils.textViewSetText(tv_yq, "指出不足");
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
