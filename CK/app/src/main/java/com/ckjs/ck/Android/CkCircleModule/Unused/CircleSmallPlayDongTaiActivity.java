package com.ckjs.ck.Android.CkCircleModule.Unused;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ckjs.ck.Manager.NotifyActivityToSmallPlayManager;
import com.ckjs.ck.Message.NotifyActicityToSmallPlayMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Player.GiraffePlayer;

import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IMediaPlayer;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
/**
 * 发小视频，暂时不用。
 */
public class CircleSmallPlayDongTaiActivity extends AppCompatActivity implements NotifyActicityToSmallPlayMessage {
    private GiraffePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_small_play_dong_tai);
        ButterKnife.bind(this);
        player = new GiraffePlayer(this);
        NotifyActivityToSmallPlayManager.getInstance().setNotifyMessage(this);
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

    @Override
    public void sendMessageActivityToSmallPlay(boolean flag, String path) {
        if (flag) {
            player.play(path);
        }
    }
}
