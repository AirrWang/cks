package com.ckjs.ck.Android.HealthModule.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Android.HealthModule.Adapter.GridAdapterHealthMore;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.DirinfoBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.ViewTool.MyGridView;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.Player.GiraffePlayer;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HealthMoreActivity extends AppCompatActivity {

    private String direct_id = "";
    private MyGridView myGridView;
    private GridAdapterHealthMore gridAdapterHealthMore;
    private TextView name, yl, mb;
    //视频地址
    private String path = "";
    private TextView tv_nan_du, tv_time, tv_xiao_hao;
    private SimpleDraweeView sd_quan_ping;
    private GiraffePlayer player;
    private KyLoadingBuilder builder;
    private SimpleDraweeView sdPlay;
    private SimpleDraweeView sdFengmian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_player);
        builder = new KyLoadingBuilder(this);
        player = new GiraffePlayer(this);
        //必须写这个，初始化加载库文件
        initId();
        initGetDirectId();
        initTask();
        initData();
    }


    //初始化数据
    private void initData() {

        player.onComplete(new Runnable() {
            @Override
            public void run() {
                //callback when video is finish
                Toast.makeText(getApplicationContext(), "视频播放完毕", Toast.LENGTH_SHORT).show();
            }
        }).onInfo(new GiraffePlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //do something when buffering start
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //do something when buffering end
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        //download speed
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        //do something when video rendering
                        break;
                }
            }
        }).onError(new GiraffePlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
                Toast.makeText(getApplicationContext(), "视频播放出错", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initGetDirectId() {
        Intent intent = getIntent();
        direct_id = intent.getStringExtra("direct_id");
    }

    private void initId() {
        myGridView = (MyGridView) findViewById(R.id.grd_iv_health);
        name = (TextView) findViewById(R.id.textViewName);
        yl = (TextView) findViewById(R.id.textViewYl);
        mb = (TextView) findViewById(R.id.textViewMb);
        tv_nan_du = (TextView) findViewById(R.id.tv_nan_du);
        tv_xiao_hao = (TextView) findViewById(R.id.tv_xiao_hao);
        tv_time = (TextView) findViewById(R.id.tv_time);
        sd_quan_ping = (SimpleDraweeView) findViewById(R.id.sd_quan_ping);
        sdPlay = (SimpleDraweeView) findViewById(R.id.sdPlay);
        sdFengmian = (SimpleDraweeView) findViewById(R.id.sdFengmian);
        MoudleUtils.viewShow(sdFengmian);
        MoudleUtils.viewShow(sdPlay);


    }

    private void initTask() {

        MoudleUtils.kyloadingShow(builder);

        Call<DirinfoBean> call = RetrofitUtils.retrofit.create(NpApi.class).dirinfo(direct_id);
        call.enqueue(new Callback<DirinfoBean>() {
            @Override
            public void onResponse(Call<DirinfoBean> call, Response<DirinfoBean> response) {
                final DirinfoBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {
                            List<String> picture = bean.getInfo().getPicture();
                            if (picture == null || picture.size() == 0) {
                                picture = new ArrayList<>();
                            }
                            gridAdapterHealthMore = new GridAdapterHealthMore(HealthMoreActivity.this);
                            gridAdapterHealthMore.setList(picture);
                            myGridView.setAdapter(gridAdapterHealthMore);
                            MoudleUtils.textViewSetText(name, bean.getInfo().getName());
                            MoudleUtils.textViewSetText(yl, bean.getInfo().getGist());
                            MoudleUtils.textViewSetText(mb, bean.getInfo().getGoal());

                            switch (bean.getInfo().getDifficult()) {
                                case "1":
                                    MoudleUtils.textViewSetText(tv_nan_du, "难度    初级");
                                    break;
                                case "2":
                                    MoudleUtils.textViewSetText(tv_nan_du, "难度    中级");

                                    break;
                                case "3":
                                    MoudleUtils.textViewSetText(tv_nan_du, "难度    高级");

                                    break;

                            }
                            if (bean.getInfo().getFat() != null && !bean.getInfo().getFat().equals("")&&!bean.getInfo().getFat().equals("0")) {
                                MoudleUtils.textViewSetText(tv_xiao_hao, "消耗    " + bean.getInfo().getFat() + "kcal");
                            }
                            if (bean.getInfo().getTime() != null && !bean.getInfo().getTime().equals("")&&!bean.getInfo().getTime().equals("0")) {
                                MoudleUtils.textViewSetText(tv_time, "时长    " + bean.getInfo().getTime() + "分钟");
                            }
                            path = bean.getInfo().getVideourl();
                            if (path != null) {
                                if (!path.equals("")) {
                                    if (NetworkUtils.isNetworkAvailable(HealthMoreActivity.this)) {
                                        initData();
                                        if (bean.getInfo().getName() != null) {
                                            player.setTitle(bean.getInfo().getName());
                                            FrescoUtils.setImage(sdFengmian, AppConfig.url_jszd + bean.getInfo().getCover());
                                            sdPlay.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    player.play(path);
                                                    MoudleUtils.viewGone(sdPlay);
                                                    MoudleUtils.viewGone(sdFengmian);
                                                }
                                            });
                                        }
                                    } else {
                                        MoudleUtils.toChekWifi(HealthMoreActivity.this);
                                    }
                                    sd_quan_ping.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                }
                            }
                        }

                    } else {
                        ToastUtils.showShort(HealthMoreActivity.this, bean.getMsg());
                    }
                }

                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<DirinfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(HealthMoreActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
