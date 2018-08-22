package com.ckjs.ck.Android.MineModule.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.AcH5Activity;
import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMineMessageInfo;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.MessageBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MineMessageInfoActivity extends AppCompatActivity {

    private ListView list_mine_message_detail;
    private View footView;
    private boolean flag;
    private KyLoadingBuilder builder;
    private int page = 1;
    private List<MessageBean.MessageInfoBean> listAll = new ArrayList<>();
    private ProgressDialog dialog;
    private int n;
    private String message_id;
    private String title = "";
    private String add_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_message_info);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot_sx, null);
        dialog = new ProgressDialog(this);
        builder = new KyLoadingBuilder(this);
        Intent intent = getIntent();
        add_id = intent.getStringExtra("add_id");
        message_id = intent.getStringExtra("message_id");
        title = intent.getStringExtra("title");
        initId();
        initToolbar();
        initData();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    /**
     * 展示数据接口
     */
    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MineMessageInfoActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(MineMessageInfoActivity.this, "token", "");

        Call<MessageBean> callBack = restApi.message(user_id + "", token, message_id, page,add_id);

        callBack.enqueue(new Callback<MessageBean>() {
            @Override
            public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            List<MessageBean.MessageInfoBean> listNew = new ArrayList<>();
                            listNew = response.body().getInfo();
                            if (listNew.size() > 0) {
                                if (page == 1) {
                                    n = listNew.size() - 1;
                                } else if (page > 1) {
                                    n = listNew.size();
                                }
                            } else if (listNew.size() == 0) {
                                n = 0;
                            }
                            if (page == 1) {
                                listAll.clear();
                            }
                            listNew.addAll(listAll);
                            listAll = listNew;
                            initAdapter();
                            page++;
                            initSet();
                            toSet(response.body().getInfo());
                        }

                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MineMessageInfoActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MineMessageInfoActivity.this, response.body().getMsg());
                    }
                }
                showFoot();
                MoudleUtils.kyloadingDismiss(builder);
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<MessageBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineMessageInfoActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
                showFoot();
                MoudleUtils.dialogDismiss(dialog);
            }
        });

    }

    private void toSet(final List<MessageBean.MessageInfoBean> info) {
        list_mine_message_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (info.get(position - 1).getType() != null) {
                    if (info.get(position - 1).getType().equals("1")) {

                    } else if (info.get(position - 1).getType().equals("2")) {
                        Intent intent = new Intent();
                        intent.putExtra("acurl", info.get(position - 1).getUrl());
                        intent.setClass(MineMessageInfoActivity.this, AcH5Activity.class);
                        startActivity(intent);
                    } else if (info.get(position - 1).getType().equals("3")) {
                        Intent intent = new Intent();
                        intent.putExtra("id", info.get(position - 1).getUrl());
                        intent.setClass(MineMessageInfoActivity.this, MineBindJSFSureActivity.class);
                        startActivity(intent);
                    } else if (info.get(position - 1).getType().equals("4")) {
                        Intent intent = new Intent();
                        intent.putExtra("bind_id", info.get(position - 1).getUrl());
                        intent.putExtra("name", title);
                        intent.setClass(MineMessageInfoActivity.this, HSFTrueActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * 上拉加载
     */
    private void initSet() {
        list_mine_message_detail.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (list_mine_message_detail.getLastVisiblePosition() == (list_mine_message_detail.getCount() - 1)) {

                        }
                        //判断滚动到顶部
                        if (list_mine_message_detail.getFirstVisiblePosition() == 0) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                initData();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                flag = firstVisibleItem + visibleItemCount == totalItemCount && !flag;
            }
        });
    }

    /**
     * 隐藏脚布局
     */
    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * listView绑定数据
     *
     * @param
     */
    private void initAdapter() {
        if (listAdapterMineMessageInfo == null) {
            listAdapterMineMessageInfo = new ListAdapterMineMessageInfo();
            listAdapterMineMessageInfo.setDataSource(listAll);
            list_mine_message_detail.setAdapter(listAdapterMineMessageInfo);
        } else {
            listAdapterMineMessageInfo.setDataSource(listAll);
            listAdapterMineMessageInfo.notifyDataSetChanged();
        }

        //让列表默认显示最后一行
        list_mine_message_detail.setSelection(n);
    }

    private ListAdapterMineMessageInfo listAdapterMineMessageInfo;

    /**
     * 标题栏设置
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(title);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * toolbar返回按钮
     *
     * @param item
     * @return
     */
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

    private void initId() {
        list_mine_message_detail = (ListView) findViewById(R.id.list_mine_message_info);
        list_mine_message_detail.addHeaderView(footView);
    }
}
