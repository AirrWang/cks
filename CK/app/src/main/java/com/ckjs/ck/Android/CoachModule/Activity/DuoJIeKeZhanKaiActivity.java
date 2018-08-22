package com.ckjs.ck.Android.CoachModule.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Android.CoachModule.Adapter.ListAdapterHomeCoachDuoJieKe;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.LessonBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class DuoJIeKeZhanKaiActivity extends AppCompatActivity {
    @BindView(R.id.lv_list)
    ListView lv_coach_home_list;
    private TextView tv_lx;
    private TextView tv_num;
    private TextView tv_call;
    private TextView tv_time;
    private TextView tv_go;
    private View view;
    private ListAdapterHomeCoachDuoJieKe listAdapterHomeCoach;
    private String type = "";
    private String my_id = "";//订单id
    private String status;
    private boolean flagAdd;
    private KyLoadingBuilder kyLoadingBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duo_jie_ke_zhan_kai);
        view = LayoutInflater.from(this).inflate(
                R.layout.item_duojieke_head, null);
        ButterKnife.bind(this);
        initId();
        lv_coach_home_list.addHeaderView(view);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        my_id = intent.getStringExtra("id");
        initToolbar();
        kyLoadingBuilder = new KyLoadingBuilder(this);
        MoudleUtils.kyloadingShow(kyLoadingBuilder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCustomTask();
        initSet();
    }

    private void initId() {
        tv_call = (TextView) view.findViewById(R.id.tv_call);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_lx = (TextView) view.findViewById(R.id.tv_lx);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        tv_go = (TextView) view.findViewById(R.id.tv_go);
    }

    private void initSet() {
        lv_coach_home_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flagAdd) {
                    if (position == 0) {
                        if (status != null) {
                            if (status.equals("4") || status.equals("5")) {
                                ToastUtils.showShort(DuoJIeKeZhanKaiActivity.this,"这节课上完了哦");
                            } else {
                                if (my_id != null) {
                                    if (!my_id.equals("")) {
                                        initGoClass();

                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    private void initGoClass() {
        if (tv_type.equals("distance")) {
            sureToSk();
        } else if (tv_type.equals("personal")) {
            initToIsFinsh();
        }
    }

    private void initToIsFinsh() {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("id", my_id);
        startActivity(intent.setClass(DuoJIeKeZhanKaiActivity.this, KcQrActivity.class));
    }

    /**
     * 打电话
     */
    private void initToCall() {
        try {
            String mobile = tv_call.getText().toString().trim();
            if (mobile!=null&&!mobile.equals("")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "没有相关应用", Toast.LENGTH_SHORT).show();
        }
    }

    Call<LessonBean> getSignBeanCall;
    String tv_type = "";

    /**
     *
     */
    private void initCustomTask() {
        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).lesson(token, user_id, my_id, type);
        getSignBeanCall.enqueue(new Callback<LessonBean>() {
            @Override
            public void onResponse(Call<LessonBean> call, Response<LessonBean> response) {
                LessonBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {

                            status = bean.getInfo().getStatus();
                            if (status != null) {
                                if (status.equals("4") || status.equals("5")) {
                                    lv_coach_home_list.removeHeaderView(view);
                                    flagAdd = false;
                                } else {
                                    flagAdd = true;
                                    String num = (Integer.parseInt(bean.getInfo().getNum()) - Integer.parseInt(bean.getInfo().getCompleted())) + "";
                                    MoudleUtils.textViewSetText(tv_call, bean.getInfo().getTel());
                                    tv_call.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            initToCall();
                                        }
                                    });
                                    MoudleUtils.textViewSetText(tv_time, bean.getInfo().getHandler());
                                    MoudleUtils.textViewSetText(tv_num, "剩余" + num + "节/共" + bean.getInfo().getNum() + "节");
                                    MoudleUtils.textViewSetText(tv_call, bean.getInfo().getTel());
                                    tv_type = bean.getInfo().getType();
                                    if (tv_type != null) {
                                        if (tv_type.equals("distance")) {
                                            MoudleUtils.textViewSetText(tv_lx, "视频指导");
                                        } else if (tv_type.equals("personal")) {
                                            MoudleUtils.textViewSetText(tv_lx, "上门指导");
                                        }
                                    }
                                    tv_go.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (status != null) {
                                                if (status.equals("4") || status.equals("5")) {
                                                } else {
                                                    initGoClass();
                                                }
                                            }
                                        }
                                    });
                                }
                            }

                            initAdatepter(bean.getInfo().getLesson());
                        }
                    } else {
                        ToastUtils.showShort(DuoJIeKeZhanKaiActivity.this,bean.getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);

            }

            @Override
            public void onFailure(Call<LessonBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);

            }
        });


    }

    private void sureToSk() {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("id", my_id);
        intent.setClass(this, ToZhiDaoActivity.class);
        startActivity(intent);
    }

    /**
     * toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        String name = "课程列表";
//        if (type.equals("1")) {
//            name = "客户上课情况";
//        } else if (type.equals("2")) {
//            name = "教练上课情况";
//        }
        textView.setText(name);
        setSupportActionBar(toolbar);
        ActionBar actionBar = (getSupportActionBar());
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }


    /**
     * 进行listView数据绑定
     *
     * @param lesson
     */
    private void initAdatepter(List<LessonBean.Lesson> lesson) {
        if (listAdapterHomeCoach == null) {
            listAdapterHomeCoach = new ListAdapterHomeCoachDuoJieKe(this);
            listAdapterHomeCoach.setList(lesson);
            lv_coach_home_list.setAdapter(listAdapterHomeCoach);
        } else {
            listAdapterHomeCoach.setList(lesson);
            listAdapterHomeCoach.notifyDataSetChanged();
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
    protected void onDestroy() {
        super.onDestroy();
        if (getSignBeanCall != null) {
            getSignBeanCall.isCanceled();
        }
    }
}
