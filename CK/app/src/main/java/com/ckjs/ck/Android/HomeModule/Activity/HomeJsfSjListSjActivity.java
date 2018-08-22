package com.ckjs.ck.Android.HomeModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Adapter.ListAdapterCityJsfSjList;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.CoachBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

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
public class HomeJsfSjListSjActivity extends AppCompatActivity {
    @BindView(R.id.lv_home_jsf_sj_list)
    ListView lv_home_jsf_sj_list;
    private String gym_id = "";
    private Call<CoachBean> coachCall;
    private KyLoadingBuilder builder;
    private ListAdapterCityJsfSjList listAdapterYuYue;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_jsf_sj_list);
        ButterKnife.bind(this);
        builder = new KyLoadingBuilder(this);
        initGetGymId();
        initToolbar();
        initCoachTask();
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
     * 接受金牌教练列表接口所需上传参数gym_id
     */
    private void initGetGymId() {
        Intent intent = getIntent();
        gym_id = intent.getStringExtra("gym_id");
        name = intent.getStringExtra("name");
        if (name == null) {
            name = "私教列表";
        }
    }

    /**
     * 进行金牌教练列表接口请求
     */
    private void initCoachTask() {
        MoudleUtils.kyloadingShow(builder);
        coachCall = RetrofitUtils.retrofit.create(NpApi.class).gymcoach(gym_id);
        coachCall.enqueue(new Callback<CoachBean>() {
            @Override
            public void onResponse(Call<CoachBean> call, Response<CoachBean> response) {
                CoachBean bean = response.body();
                initToTaskData(bean);

                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<CoachBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    /**
     * 条目点击事件
     *
     * @param bean
     */
    List<CoachBean.CoachBeanInfo> list = new ArrayList<>();

    private void initSetTo(final CoachBean bean) {
        lv_home_jsf_sj_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list = bean.getInfo();
                if (list != null) {
                    if (list.size() > 0) {
                        if (list.get(position).getStatus() != null) {
                            if (list.get(position).getStatus().equals("1")) {
                                String token = (String) SPUtils.get("token", "");
                                if (token.equals("")) {
                                    MoudleUtils.initToLogin(HomeJsfSjListSjActivity.this);
                                } else {
                                    try {
                                        initToBd(position);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void initToBd(int position) {
        if ((SPUtils.get("vip", "").equals("1"))) {
            if ((SPUtils.get("type", "") + "").equals("1")) {
                startActivity(new Intent().setClass(this, RealNameYsActivity.class));
            } else {
                startActivity(new Intent().setClass(this, RealNameActivity.class));
            }
        } else {
            initToTIjOrder(position);
        }
    }

    private void initToTIjOrder(int position) {
        Intent intent = new Intent();
        intent.setClass(HomeJsfSjListSjActivity.this, SiJiaoYuYueActivity.class);
        intent.putExtra("name", list.get(position).getName());
        intent.putExtra("sijiao_id", list.get(position).getId());
        startActivity(intent);
    }

    /**
     * 处理金牌教练列表接口获取的数据
     *
     * @param bean
     */
    private void initToTaskData(CoachBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                if (bean.getInfo() != null) {
                    if (bean.getInfo() != null) {
                        initAdatepter(bean);
                        if (bean.getInfo().size() == 0) {
                            ToastUtils.showShort(HomeJsfSjListSjActivity.this,bean.getMsg());
                        } else {
                            initSetTo(bean);
                        }
                    }
                }

            } else if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(HomeJsfSjListSjActivity.this,bean.getMsg());
            } else if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(HomeJsfSjListSjActivity.this,bean.getMsg());
            }
        }
    }

    /**
     * 进行listView数据绑定
     *
     * @param bean
     */
    private void initAdatepter(CoachBean bean) {
        if (listAdapterYuYue == null) {
            listAdapterYuYue = new ListAdapterCityJsfSjList(this);
            listAdapterYuYue.setList(bean.getInfo());
            lv_home_jsf_sj_list.setAdapter(listAdapterYuYue);
        } else {
            listAdapterYuYue.setList(bean.getInfo());
            listAdapterYuYue.notifyDataSetChanged();
        }
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("私教列表");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }
}
