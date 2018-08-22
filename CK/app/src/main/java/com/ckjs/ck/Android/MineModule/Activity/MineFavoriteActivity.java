package com.ckjs.ck.Android.MineModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.ckjs.ck.Android.HomeModule.Activity.TjH5HomeActivity;
import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMineFavorite;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetMyfavoriteBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
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
public class MineFavoriteActivity extends AppCompatActivity{
    private int page = 1;
    private ListView listView;
    private boolean flag;
    private List<GetMyfavoriteBean.GetMyfavoriteInfoBean> listAll = new ArrayList<>();
    private ListAdapterMineFavorite listAdapterMineFavorite;
    private View footView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_favorite);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot, null);
        initId();
        initToolbar();
        initSet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        initTaskGetMyFavorite();
    }

    private void initId() {
        listView = (ListView) findViewById(R.id.listViewFavorite);
        initFoot();
    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("我的收藏");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
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

    private void initFoot() {
        listView.addFooterView(footView);
    }

    private void initSet() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                if (page > 1) {
                                    initTaskGetMyFavorite();
                                }
                            }

                        }
                        //判断滚动到顶部
                        if (listView.getFirstVisiblePosition() == 0) {

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

    private void initTaskGetMyFavorite() {
        int userid = (int) SPUtils.get(this, "user_id", 0);
        String token = (String) SPUtils.get(this, "token", "");
        Call<GetMyfavoriteBean> getSignBeanCall = RetrofitUtils.retrofit.create(WfApi.class).myfavorite(token, userid, page);
        getSignBeanCall.enqueue(new Callback<GetMyfavoriteBean>() {
            @Override
            public void onResponse(Call<GetMyfavoriteBean> call, Response<GetMyfavoriteBean> response) {
                GetMyfavoriteBean bean = response.body();
                if (bean != null) {
                        if (bean.getStatus().equals("1")) {
                            if (bean.getInfo() != null) {
                                List<GetMyfavoriteBean.GetMyfavoriteInfoBean> list = new ArrayList<>();
                                list = bean.getInfo();
                                if (page==1){
                                    listAll.clear();
                                }
                                listAll.addAll(list);
                                if (listAll.size() == 0) {
                                    ToastUtils.showShort(MineFavoriteActivity.this, bean.getMsg());
                                }
                                initAdapter();
                                initSetItemClick();
                                page++;

                            }
                        } else if (bean.getStatus().equals("0")) {
                            ToastUtils.showShort(MineFavoriteActivity.this, bean.getMsg());
                        }else if (bean.getStatus().equals("2")){
                            ToastUtils.showShort(MineFavoriteActivity.this,bean.getMsg());
                        }

                }
                showFoot();
            }

            @Override
            public void onFailure(Call<GetMyfavoriteBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineFavoriteActivity.this);
                showFoot();

            }
        });

    }

    private void initAdapter() {

        if (listAdapterMineFavorite == null) {
            listAdapterMineFavorite = new ListAdapterMineFavorite(this);
            listAdapterMineFavorite.setList(listAll);
            listView.setAdapter(listAdapterMineFavorite);
        } else {
            listAdapterMineFavorite.setList(listAll);
            listAdapterMineFavorite.notifyDataSetChanged();
        }
    }

    private void initSetItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (NetworkUtils.isNetworkAvailable(MineFavoriteActivity.this)) {
                    if (listAll != null) {
                        if (listAll.get(position).getReadurl() != null) {
                            if (listAll.get(position).getReadurl().equals("")) {
                                ToastUtils.showShort(MineFavoriteActivity.this, "页面不存在");
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra("acurl", listAll.get(position).getReadurl());
                                intent.putExtra("read_id", listAll.get(position).getRead_id());
                                intent.setClass(MineFavoriteActivity.this, TjH5HomeActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            MoudleUtils.toChekWifi(MineFavoriteActivity.this);

                        }
                    }
                }
            }
        });
    }
}
