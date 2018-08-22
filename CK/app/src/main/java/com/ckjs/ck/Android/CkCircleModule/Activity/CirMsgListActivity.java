package com.ckjs.ck.Android.CkCircleModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.CkCircleModule.Adapter.ListAdapterCirMsgList;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.UsercommentBean;
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
public class CirMsgListActivity extends AppCompatActivity {
    @BindView(R.id.lv_cir_msg_list)
    ListView lv_cir_msg_list;
    private int page = 1;
    private ListAdapterCirMsgList listAdapterCirMsgList;
    private List<UsercommentBean.UsercommentBeanInfo> listAll = new ArrayList<>();
    private View footView;
    private KyLoadingBuilder kyLoadingBuilder;
    private boolean flag;
    private boolean flagNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cir_msg_list);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot_msg, null);
        ButterKnife.bind(this);
        lv_cir_msg_list.addFooterView(footView);
        initToolbar();
        initToCirMsgList();


    }

    /**
     * 进入帖子详情
     */
    private void initset() {
        lv_cir_msg_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initToTzMore(position);
            }
        });
        lv_cir_msg_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_cir_msg_list.getLastVisiblePosition() == (lv_cir_msg_list.getCount() - 1)) {
                            if (page > 1) {
                                initToCirMsgList();
                            }
                        }
                        //判断滚动到顶部
                        if (lv_cir_msg_list.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && !flag) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });
    }

    /**
     * 跳转帖子详情
     *
     * @param position
     */
    private void initToTzMore(int position) {
        Intent intent = new Intent();
        intent.setClass(CirMsgListActivity.this, CircleRecommenMoreActivity.class);
        intent.putExtra("user_id", Integer.parseInt(listAll.get(position).getFocus_id()));
        intent.putExtra("circle_id", Integer.parseInt(listAll.get(position).getCircle_id()));
        intent.putExtra("p", 0);
        intent.putExtra("name", "");//通知刷新外层评论时使用
        startActivity(intent);
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("消息");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取评论list
     */
    private void initToCirMsgList() {

        if (page==1) {
            kyLoadingBuilder = new KyLoadingBuilder(this);
            MoudleUtils.kyloadingShow(kyLoadingBuilder);
        }
        String token = "";
        token = (String) SPUtils.get("token", token);

        int user_id = (int) SPUtils.get("user_id", 0);
        Call<UsercommentBean> call = RetrofitUtils.retrofit.create(NpApi.class).usercomment(token, user_id, page);
        call.enqueue(new Callback<UsercommentBean>() {
            @Override
            public void onResponse(Call<UsercommentBean> call, Response<UsercommentBean> response) {
                UsercommentBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        List<UsercommentBean.UsercommentBeanInfo> list = new ArrayList<>();
                        list = bean.getInfo();
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        if (page == 1) {
                            listAll.clear();
                            if (!flagNext) {
                                MoudleUtils.viewShow(footView);
                            } else {
                                MoudleUtils.viewInvisibily(footView);
                            }
                            footView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    page = 1;
                                    flagNext = true;
                                    initToCirMsgList();
                                }
                            });
                        }

                        listAll.addAll(list);
                        if (listAll.size() == 0) {
                            ToastUtils.showShort(CirMsgListActivity.this,bean.getMsg());
                            MoudleUtils.kyloadingDismiss(kyLoadingBuilder);

                            return;
                        }
                        initListadapter();
                        initset();
                        page++;


                    } else {
                        ToastUtils.showShort(CirMsgListActivity.this,bean.getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);

            }

            @Override
            public void onFailure(Call<UsercommentBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);


            }
        });
    }

    /**
     * listView绑定数据
     */
    private void initListadapter() {

        if (listAdapterCirMsgList == null) {
            listAdapterCirMsgList = new ListAdapterCirMsgList(this);
            listAdapterCirMsgList.setList(listAll);
            lv_cir_msg_list.setAdapter(listAdapterCirMsgList);
        } else {
            listAdapterCirMsgList.setList(listAll);
            listAdapterCirMsgList.notifyDataSetChanged();
        }
    }

}
