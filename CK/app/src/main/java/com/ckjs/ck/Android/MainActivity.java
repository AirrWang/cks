package com.ckjs.ck.Android;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ckjs.ck.Android.CkCircleModule.Fragment.CircleFragment;
import com.ckjs.ck.Android.HealthModule.Fragment.HealthFrgFragment;
import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Android.MineModule.Fragment.MineFragment;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Bean.GetCircleMineBean;
import com.ckjs.ck.Bean.GetCircleTjBean;
import com.ckjs.ck.Bean.GetNewVersionBean;
import com.ckjs.ck.Manager.NotifyToMainAcFxManager;
import com.ckjs.ck.Message.NotifyToMainAcFxMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.MoudleTwoTool;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.UpdateAppManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ckjs.ck.Android.MineModule.Activity.MineUpDataActivity.getVersionCode;
import static com.ckjs.ck.Tool.MoudleUtils.getVersionName;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

/**
 * NiPing20161013:CK项目MainActivity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, NotifyToMainAcFxMessage {
    private FragmentTransaction transaction;
    private Fragment nowFragment;
    private FragmentManager manager;

    private HomeFragment homeFragment;
    private HealthFrgFragment healthFrgFragment;
    private CircleFragment circleFragment;
    private MineFragment mineFragment;
    private GetNewVersionBean getuodata;

    private ToggleButton tb_home, tb_health, tb_ck_circle, tb_mine;
    public static String tag;

    /**
     * 返回==home
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            moveTaskToBack(true);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initTaskUpdata() {
        int OS = 0;
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<GetNewVersionBean> callBack = restApi.update(OS + "");
        callBack.enqueue(new Callback<GetNewVersionBean>() {
            @Override
            public void onResponse(Call<GetNewVersionBean> call, Response<GetNewVersionBean> response) {

                getuodata = response.body();
                if (getuodata != null) {
                    if (getuodata.getStatus().equals("1")) {
                        initData(getuodata);
                    } else if (getuodata.getStatus().equals("0")) {
                    }
                }
            }

            @Override
            public void onFailure(Call<GetNewVersionBean> call, Throwable t) {
                ToastUtils.show(MainActivity.this, getResources().getString(R.string.not_wlan_show), 0);
            }
        });
    }

    private void initData(GetNewVersionBean getuodata) {
        if (getuodata.getInfo() != null) {
            String versonName = getuodata.getInfo().getVername();
            String versonCode = getuodata.getInfo().getVercode();
            String mVname = getVersionName(this);
            String mVcode = getVersionCode(this);
            if (!versonName.equals(mVname)) {
                UpdateAppManager updateManager = new UpdateAppManager(this, "main");
                updateManager.checkUpdateInfo(getuodata.getInfo().getDownurl(), getuodata.getInfo().getIsupdate(), getuodata.getInfo().getVerdec());
            } else {
                if (!versonCode.equals(mVcode)) {
                    UpdateAppManager updateManager = new UpdateAppManager(this, "main");
                    updateManager.checkUpdateInfo(getuodata.getInfo().getDownurl(), getuodata.getInfo().getIsupdate(), getuodata.getInfo().getVerdec());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MoudleTwoTool.initLoginHxAndJpush();//环信和极光登录
        initId();
        init();
        initTab();
        initNowFrg();
        BluethUtils.initGetBluetooth();//初始化蓝牙
        if (savedInstanceState == null) {
            initNew();
            initAdaaFragment();
            showHome();
        } else {
            initRestart(savedInstanceState);
            initTag();
        }
        initTaskUpdata();
    }

    /**
     * 防止重复初始化各个模块
     */
    private void initTag() {
        if (tag != null) {
            switch (tag) {
                case "home":
                    showHome();

                    break;
                case "health":
                    showHelth();


                    break;
                case "circle":
                    showCil();

                    break;
                case "mine":
                    showMine();


                    break;
            }
        }
    }

    private void showMine() {
        getSupportFragmentManager().beginTransaction()
                .show(mineFragment)
                .hide(homeFragment)
                .hide(circleFragment)
                .hide(healthFrgFragment)
                .commit();
        nowFragment = mineFragment;
        initColor(tb_mine, tb_home, tb_health, tb_ck_circle);
        tag = "mine";
    }

    private void showCil() {
        getSupportFragmentManager().beginTransaction()
                .show(circleFragment)
                .hide(homeFragment)
                .hide(healthFrgFragment)
                .hide(mineFragment)
                .commit();
        nowFragment = circleFragment;
        initColor(tb_ck_circle, tb_home, tb_health, tb_mine);
        tag = "circle";
    }

    private void showHelth() {
        // 解决重叠问题
        getSupportFragmentManager().beginTransaction()
                .show(healthFrgFragment)
                .hide(homeFragment)
                .hide(circleFragment)
                .hide(mineFragment)
                .commit();
        nowFragment = healthFrgFragment;
        initColor(tb_health, tb_home, tb_ck_circle, tb_mine);
        tag = "health";
    }

    private void showHome() {
        // 解决重叠问题
        getSupportFragmentManager().beginTransaction()
                .show(homeFragment)
                .hide(circleFragment)
                .hide(healthFrgFragment)
                .hide(mineFragment)
                .commit();
        nowFragment = homeFragment;
        initColor(tb_home, tb_health, tb_ck_circle, tb_mine);
        tag = "home";
    }

    /**
     * 若保存fragment 内存重启时的操作，暂时不用
     *
     * @param savedInstanceState
     */
    private void initRestart(Bundle savedInstanceState) {
        if (savedInstanceState != null) {  // “内存重启”时调用
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof HomeFragment) {
                    homeFragment = (HomeFragment) fragment;
                }
                if (fragment instanceof CircleFragment) {
                    circleFragment = (CircleFragment) fragment;
                }
                if (fragment instanceof HealthFrgFragment) {
                    healthFrgFragment = (HealthFrgFragment) fragment;
                }
                if (fragment instanceof MineFragment) {
                    mineFragment = (MineFragment) fragment;
                }
            }


        }
    }


    /**
     * 分享回掉的注册
     */
    private void init() {

        NotifyToMainAcFxManager.getInstance().setNotifyMessage(this);
    }


    /**
     * 进行findView
     */
    private void initId() {
        tb_home = (ToggleButton) findViewById(R.id.tb_home);
        tb_home.setOnClickListener(this);

        tb_health = (ToggleButton) findViewById(R.id.tb_health);
        tb_health.setOnClickListener(this);

        tb_ck_circle = (ToggleButton) findViewById(R.id.tb_ck_circle);
        tb_ck_circle.setOnClickListener(this);

        tb_mine = (ToggleButton) findViewById(R.id.tb_mine);
        tb_mine.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 各个分享的回掉
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            android.util.Log.d("plat", "platform" + platform);

            Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                android.util.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 初始化标签
     */
    private void initTab() {
        formatToggleButton(tb_home, R.drawable.frag_one_selector,
                R.string.tb_home_hss, getResources().getDimensionPixelSize(R.dimen.mine_four), getResources().getDimensionPixelSize(R.dimen.mine_four));
        formatToggleButton(tb_health, R.drawable.frag_two_selector,
                R.string.tb_health, getResources().getDimensionPixelSize(R.dimen.mine_four), getResources().getDimensionPixelSize(R.dimen.mine_four));
        formatToggleButton(tb_ck_circle, R.drawable.frag_ck_circle_selector,
                R.string.tb_ck_circle, getResources().getDimensionPixelSize(R.dimen.mine_four), getResources().getDimensionPixelSize(R.dimen.mine_four));
        formatToggleButton(tb_mine, R.drawable.frag_mine_selector,
                R.string.tb_mine, getResources().getDimensionPixelSize(R.dimen.mine_four), getResources().getDimensionPixelSize(R.dimen.mine_four));
    }

    /**
     * 初始化标签
     *
     * @param button
     * @param drawableResId
     * @param resId
     * @param w
     * @param h
     */
    private void formatToggleButton(ToggleButton button, int drawableResId,
                                    int resId, int w, int h) {
        //设置标签文字
        button.setTextOff(getResources().getString(resId));
        button.setTextOn(getResources().getString(resId));
        //设置标签图片
        Drawable drawable = getResources().getDrawable(drawableResId);
        drawable.setBounds(0, 0, w, h);
        button.setCompoundDrawables(null, drawable, null, null);
    }

    /**
     * 对对象初始化
     */
    private void initNew() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        if (healthFrgFragment == null) {
            healthFrgFragment = new HealthFrgFragment();
        }
        if (circleFragment == null) {
            circleFragment = new CircleFragment();
        }
        if (mineFragment == null) {
            mineFragment = new MineFragment();
        }
    }

    /**
     * 正在显示的模块的初始化
     */
    private void initNowFrg() {
        if (nowFragment == null) {
            nowFragment = new Fragment();
        }
    }

    /**
     * 切換模块显示
     *
     * @param fragment
     */
    private void initFragment(Fragment fragment, String tag) {
        // TODO Auto-generated method stub
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        initAddFragMent(nowFragment, fragment, tag);
    }

    /**
     * 各个模块的添加和隐藏
     *
     * @param nowFragment
     * @param fragment
     * @param tag
     */
    private void initAddFragMent(Fragment nowFragment, Fragment fragment, String tag) {
        if (!fragment.isAdded()) {
            transaction.hide(nowFragment).add(R.id.frag_now, fragment, tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(nowFragment).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_home:
                //                AppConfig.EXTRA_DATA_STATE = 0;//0,1,2,3,4,5首页，健康监测，睡眠测量，心率测量，血氧测量，血压测量
                showHome();

                break;
            case R.id.tb_health:
                //                AppConfig.EXTRA_DATA_STATE = 1;//0,1,2,3,4,5首页，健康监测，睡眠测量，心率测量，血氧测量，血压测量
                showHelth();
                break;
            case R.id.tb_ck_circle:
                showCil();
                break;
            case R.id.tb_mine:
                showMine();
                break;
            default:
                break;
        }

    }

    private void initAdaaFragment() {
        initFragment(homeFragment, "home");
        initFragment(healthFrgFragment, "health");
        initFragment(circleFragment, "circle");
        initFragment(mineFragment, "mine");
    }

    /**
     * 设定当前的TextView的图片和字体颜色
     *
     * @param tv_one
     * @param tv_two
     * @param tv_three
     */
    private void initColor(ToggleButton tv_one, ToggleButton tv_two, ToggleButton tv_three, ToggleButton tv_four) {
        tv_one.setChecked(true);
        tv_two.setChecked(false);
        tv_three.setChecked(false);
        tv_four.setChecked(false);
    }


    /**
     * 超空圈分享回掉
     *
     * @param flag
     * @param list
     * @param p
     */
    @Override
    public void sendMessageToMainAcFxFlag(boolean flag, List<GetCircleBean.InfoBean> list, int p) {
        if (flag) {
            initToFx(list, p);

        }
    }

    /**
     * 超空圈我的关注的分享处理
     *
     * @param list
     * @param p
     */
    private void initToFx(List<GetCircleBean.InfoBean> list, int p) {
        UMImage umImage;
        if (list.get(p).getPicture() != null) {
            if (list.get(p).getPicture().size() > 0) {
                String imgerurl = list.get(p).getPicture().get(0);
                umImage = new UMImage(MainActivity.this, AppConfig.url + imgerurl);
            } else {
                umImage = new UMImage(MainActivity.this, R.drawable.app_icon);
            }
        } else {
            umImage = new UMImage(MainActivity.this, R.drawable.app_icon);
        }
        umImage.setThumb(umImage);
        String c = list.get(p).getContent();
        if (c == null || c.equals("")) {
            c = "暂无内容";
        }
        new ShareAction(MainActivity.this).
                withText(c)
                .withTitle("超空圈分享")
                .withTargetUrl("http://www.chaokongs.com/circle/circleinfo?id=" + list.get(p).getId())
                .withMedia(umImage)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)
                .setCallback(umShareListener).open();
    }

    /**
     * 超空圈推荐的分享处理
     *
     * @param list
     * @param p
     */
    private void initToTjFx(List<GetCircleTjBean.InfoBean> list, int p) {
        UMImage umImage;
        if (list.get(p).getPicture() != null) {
            if (list.get(p).getPicture().size() > 0) {
                String imgerurl = list.get(p).getPicture().get(0);
                umImage = new UMImage(MainActivity.this, AppConfig.url + imgerurl);
            } else {
                umImage = new UMImage(MainActivity.this, R.drawable.app_icon);
            }
        } else {
            umImage = new UMImage(MainActivity.this, R.drawable.app_icon);
        }
        umImage.setThumb(umImage);
        String c = list.get(p).getContent();
        if (c == null || c.equals("")) {
            c = "暂无内容";
        }
        new ShareAction(MainActivity.this).
                withText(c)
                .withTitle("超空圈分享")
                .withTargetUrl("http://www.chaokongs.com/circle/circleinfo?id=" + list.get(p).getId())
                .withMedia(umImage)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)
                .setCallback(umShareListener).open();
    }


    /**
     * 超空圈的我的S圈的分享处理
     *
     * @param list
     * @param p
     */
    private void initToMineFx(List<GetCircleMineBean.InfoBean> list, int p) {
        UMImage umImage;
        if (list.get(p).getPicture() != null) {
            if (list.get(p).getPicture().size() > 0) {
                String imgerurl = list.get(p).getPicture().get(0);
                umImage = new UMImage(MainActivity.this, AppConfig.url + imgerurl);
            } else {
                umImage = new UMImage(MainActivity.this, R.drawable.app_icon);
            }
        } else {
            umImage = new UMImage(MainActivity.this, R.drawable.app_icon);
        }
        umImage.setThumb(umImage);
        String c = list.get(p).getContent();
        if (c == null || c.equals("")) {
            c = "暂无内容";
        }
        new ShareAction(MainActivity.this).
                withText(c)
                .withTitle("超空圈分享")
                .withTargetUrl("http://www.chaokongs.com/circle/circleinfo?id=" + list.get(p).getId())
                .withMedia(umImage)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)
                .setCallback(umShareListener).open();
    }


    @Override
    public void sendMessageMineToMainAcFxFlag(boolean flag, List<GetCircleMineBean.InfoBean> list, int p) {
        if (flag) {
            initToMineFx(list, p);
        }
    }

    @Override
    public void sendMessageToTjMainAcFxFlag(boolean flag, List<GetCircleTjBean.InfoBean> list, int p) {
        if (flag) {
            initToTjFx(list, p);
        }
    }

    /**
     * 不保存fragment
     *
     * @param outState
     * @param outPersistentState
     */
    //    @Override
    //    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    ////        super.onSaveInstanceState(outState, outPersistentState);
    //    }
}
