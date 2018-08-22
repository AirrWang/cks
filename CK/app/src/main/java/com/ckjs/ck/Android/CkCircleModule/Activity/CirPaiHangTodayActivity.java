package com.ckjs.ck.Android.CkCircleModule.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class CirPaiHangTodayActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    private MyListView lv_paihang_detail;
    private KyLoadingBuilder builder;
    private List<CaltopInfoBean.CaltopInfoDetailBean.CaltopDetailBean> listAll = new ArrayList<>();
    private TextView tv_paihang_uptime;
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
    private SwipeRefreshLayout swipeRefreshLayoutTj;
    private SimpleDraweeView sd_icon;
    private RelativeLayout contentLayout;
    private TextView tv_pai_hang;
    private TextView tv_xh;
    private TextView tv_bu_shu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cir_pai_hang_today);
        builder = new KyLoadingBuilder(this);
        initToolbar();
        initId();
        initData();
    }

    private void initId() {
        contentLayout = (RelativeLayout) findViewById(R.id.r_fx);
        sd_icon = (SimpleDraweeView) findViewById(R.id.icon_pic);
        tv_bu_shu = (TextView) findViewById(R.id.tv_bu_shu);
        tv_xh = (TextView) findViewById(R.id.tv_xh);
        tv_pai_hang = (TextView) findViewById(R.id.tv_pai_hang);
        lv_paihang_detail = (MyListView) findViewById(R.id.lv_paihang_detail_t);
        tv_paihang_uptime = (TextView) findViewById(R.id.tv_paihang_uptime_t);
        tv_health_paihang_name = (TextView) findViewById(R.id.tv_health_paihang_name_t);
        sd_paihang_firpic = (SimpleDraweeView) findViewById(R.id.sd_paihang_firpic_t);
        tv_paihang_firstep = (TextView) findViewById(R.id.tv_paihang_firstep_t);
        tv_paihang_fircal = (TextView) findViewById(R.id.tv_paihang_fircal_t);
        tv_paihang_pingjuncal = (TextView) findViewById(R.id.tv_paihang_pingjuncal_t);
        tv_paihang_myrank = (TextView) findViewById(R.id.tv_paihang_myrank_t);
        sd_paihang_mypic = (SimpleDraweeView) findViewById(R.id.sd_paihang_mypic_t);
        tv_paihang_myname = (TextView) findViewById(R.id.tv_paihang_myname_t);
        tv_paihang_mystep = (TextView) findViewById(R.id.tv_paihang_mystep_t);
        tv_paihang_mycal = (TextView) findViewById(R.id.tv_paihang_mycal_t);
        LinearLayout ll_paihang_yestoday = (LinearLayout) findViewById(R.id.ll_paihang_yestoday);
        ll_paihang_yestoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(CirPaiHangTodayActivity.this, CirPaiHangMoreActivity.class));
            }
        });

        swipeRefreshLayoutTj = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutPH);
        swipeRefreshLayoutTj.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayoutTj.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutTj.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutTj.setOnRefreshListener(CirPaiHangTodayActivity.this);
    }

    private CaltopInfoBean.CaltopInfoDetailBean infoDetailBean;

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(CirPaiHangTodayActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(CirPaiHangTodayActivity.this, "token", "");
        Call<CaltopInfoBean> callBack = restApi.caltopinfo(user_id + "", token);

        callBack.enqueue(new Callback<CaltopInfoBean>() {
            @Override
            public void onResponse(Call<CaltopInfoBean> call, Response<CaltopInfoBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            if (response.body().getInfo().getToplist() != null && response.body().getInfo().getToplist().size() != 0) {
                                infoDetailBean = response.body().getInfo();
                                listAll.clear();
                                listAll.addAll(response.body().getInfo().getToplist());
                                initSet(response.body().getInfo().getToplist());

                                initAdapter();
                                initFxView(response.body().getInfo());
                                initUI(response.body().getInfo());
                            }

                        }
                    } else if (response.body().getStatus().equals("0")) {

                        ToastUtils.show(CirPaiHangTodayActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(CirPaiHangTodayActivity.this,response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutTj);
            }

            @Override
            public void onFailure(Call<CaltopInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(CirPaiHangTodayActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutTj);
            }
        });
    }

    private void initFxView(CaltopInfoBean.CaltopInfoDetailBean info) {
        MoudleUtils.textViewSetText(tv_bu_shu, info.getSteps());
        MoudleUtils.textViewSetText(tv_xh, info.getCalories());
        MoudleUtils.textViewSetText(tv_pai_hang, info.getRanking());
        FrescoUtils.setImage(sd_icon, AppConfig.url + info.getPicurl());

    }

    private void initSet(final List<CaltopInfoBean.CaltopInfoDetailBean.CaltopDetailBean> toplist) {
        lv_paihang_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("focus_id", toplist.get(position).getUser_id());
                intent.setClass(CirPaiHangTodayActivity.this, MineAttentionPeople.class);
                startActivity(intent);
            }
        });
    }

    private void initUI(CaltopInfoBean.CaltopInfoDetailBean info) {
        tv_paihang_uptime.setText(info.getUpdatetime());
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("实时热量消耗榜");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
        SimpleDraweeView sdFx = (SimpleDraweeView) findViewById(R.id.sd_button);
        FrescoUtils.setImage(sdFx, AppConfig.res + R.drawable.mpre_report);
        RelativeLayout rlFx = (RelativeLayout) findViewById(R.id.r_toolbar_button);
        rlFx.setOnClickListener(this);
        sdFx.setOnClickListener(this);
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
    public void onRefresh() {
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.r_toolbar_button:
            case R.id.sd_button:

                initFenXiang();
                break;
            case R.id.left_button:
                finish();
                break;
        }

    }

    private void initFenXiang() {
        if (infoDetailBean != null) {
            //生成排行分享图片
            if (contentLayout == null) return;
            contentLayout.setDrawingCacheEnabled(true);
            contentLayout.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            contentLayout.layout(0, 0, contentLayout.getMeasuredWidth(),
                    contentLayout.getMeasuredHeight());

            contentLayout.buildDrawingCache();
            Bitmap bitmap = contentLayout.getDrawingCache();
            if (bitmap != null) {
                initToPic(bitmap);
            }
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(CirPaiHangTodayActivity.this).
                onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 各个分享的回掉
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            android.util.Log.d("plat", "platform" + platform);

            Toast.makeText(CirPaiHangTodayActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(CirPaiHangTodayActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                android.util.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(CirPaiHangTodayActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 进行分享
     *
     * @param bitmap
     */

    private void initToPic(Bitmap bitmap) {
        UMImage umImage;
        umImage = new UMImage(CirPaiHangTodayActivity.this, bitmap);
        if (umImage == null) return;
        umImage.setThumb(umImage);
        new ShareAction(CirPaiHangTodayActivity.this)
                .withText("")
                .withTargetUrl("http://www.chaokongs.com/")
                .withMedia(umImage)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)
                .setCallback(umShareListener).open();
    }

}
