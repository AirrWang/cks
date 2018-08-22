package com.ckjs.ck.Android.CkCircleModule.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.ListAdapterPaihangDetail;
import com.ckjs.ck.Android.MineModule.Activity.MineAttentionPeople;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.CaltopInfoBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.MyListView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class CirPaiHangMoreActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MyListView lv_paihang_detail;
    private KyLoadingBuilder builder;
    private List<CaltopInfoBean.CaltopInfoDetailBean.CaltopDetailBean> listAll = new ArrayList<>();
    private TextView tv_health_paihang_name;
    private SimpleDraweeView sd_paihang_firpic;
    private TextView tv_paihang_firstep;
    private TextView tv_paihang_fircal;
    private TextView tv_paihang_pingjuncal;
    private TextView tv_paihang_myrank;
    private SimpleDraweeView sd_paihang_mypic;
    private TextView tv_paihang_myname;
    private TextView tv_paihang_mystep;
    private TextView tv_paihang_mycal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cir_pai_hang_more);
        builder = new KyLoadingBuilder(this);
        initToolbar();
        initId();
        initData();
    }

    private void initId() {
        lv_paihang_detail = (MyListView) findViewById(R.id.lv_paihang_detail);
        tv_health_paihang_name = (TextView) findViewById(R.id.tv_health_paihang_name);
        sd_paihang_firpic = (SimpleDraweeView) findViewById(R.id.sd_paihang_firpic);
        tv_paihang_firstep = (TextView) findViewById(R.id.tv_paihang_firstep);
        tv_paihang_fircal = (TextView) findViewById(R.id.tv_paihang_fircal);
        tv_paihang_pingjuncal = (TextView) findViewById(R.id.tv_paihang_pingjuncal);
        tv_paihang_myrank = (TextView) findViewById(R.id.tv_paihang_myrank);
        sd_paihang_mypic = (SimpleDraweeView) findViewById(R.id.sd_paihang_mypic);
        tv_paihang_myname = (TextView) findViewById(R.id.tv_paihang_myname);
        tv_paihang_mystep = (TextView) findViewById(R.id.tv_paihang_mystep);
        tv_paihang_mycal = (TextView) findViewById(R.id.tv_paihang_mycal);
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(CirPaiHangMoreActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(CirPaiHangMoreActivity.this, "token", "");
        Call<CaltopInfoBean> callBack = restApi.yesdaycaltop(user_id + "", token);

        callBack.enqueue(new Callback<CaltopInfoBean>() {
            @Override
            public void onResponse(Call<CaltopInfoBean> call, Response<CaltopInfoBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            if (response.body().getInfo().getToplist() != null && response.body().getInfo().getToplist().size() != 0) {
                                listAll.addAll(response.body().getInfo().getToplist());
                                initSet(response.body().getInfo().getToplist());
                                initAdapter();
                                initUI(response.body().getInfo());
                            }
                        }
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(CirPaiHangMoreActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(CirPaiHangMoreActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<CaltopInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(CirPaiHangMoreActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });
    }

    private void initSet(final List<CaltopInfoBean.CaltopInfoDetailBean.CaltopDetailBean> toplist) {
        lv_paihang_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("focus_id", toplist.get(position).getUser_id());
                intent.setClass(CirPaiHangMoreActivity.this, MineAttentionPeople.class);
                startActivity(intent);
            }
        });
    }

    private void initUI(CaltopInfoBean.CaltopInfoDetailBean info) {

        tv_health_paihang_name.setText("恭喜" + info.getToplist().get(0).getUsername() + "夺得榜首");
        FrescoUtils.setImage(sd_paihang_firpic, AppConfig.url + info.getToplist().get(0).getPicurl());
        tv_paihang_firstep.setText(info.getToplist().get(0).getSteps() + "步");
        tv_paihang_fircal.setText(info.getToplist().get(0).getCalories() + "kcal");
        tv_paihang_pingjuncal.setText("超空平均消耗" + info.getCalavg() + "kcal");
        tv_paihang_myrank.setText(info.getRanking());
        FrescoUtils.setImage(sd_paihang_mypic, AppConfig.url + info.getPicurl());
        tv_paihang_myname.setText(info.getUsername());
        tv_paihang_mystep.setText(info.getSteps() + "步");
        tv_paihang_mycal.setText(info.getCalories());
    }

    private ListAdapterPaihangDetail listAdapterPaihangDetail;

    private void initAdapter() {
        if (listAdapterPaihangDetail == null) {
            listAdapterPaihangDetail = new ListAdapterPaihangDetail();
            listAdapterPaihangDetail.setDataSource(listAll);
            lv_paihang_detail.setAdapter(listAdapterPaihangDetail);
        } else {
            listAdapterPaihangDetail.setDataSource(listAll);
            listAdapterPaihangDetail.notifyDataSetChanged();
        }
    }

    /**
     * 标题栏
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("昨日热量消耗榜");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
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
