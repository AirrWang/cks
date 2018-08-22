package com.ckjs.ck.Android;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ToggleButton;

import com.ckjs.ck.Android.CoachModule.Fragment.HealthFrgFragmentCoach;
import com.ckjs.ck.Android.CoachModule.Fragment.HomeFragmentCoach;
import com.ckjs.ck.Android.CoachModule.Fragment.MineFragmentCoach;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleTwoTool;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MainCoachActivity extends AppCompatActivity implements View.OnClickListener {
    private FragmentTransaction transaction;
    private Fragment nowFragment;
    private FragmentManager manager;

    private HomeFragmentCoach homeFragment;
    private HealthFrgFragmentCoach healthFrgFragment;
    private MineFragmentCoach mineFragment;


    private ToggleButton tb_home, tb_health, tb_mine;
    public static String tag_coach;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cocach);
        MoudleTwoTool.initLoginHxAndJpush();//环信和极光登录
        initId();

        initTab();
        initNowFrg();

        if (savedInstanceState == null) {
            initNew();
            initAdaaFragment();
            showHome();
        } else {
            initRestart(savedInstanceState);
            initTag();
            //            ToastUtils.showShort(this, getSupportFragmentManager().getFragments().size() + "");
        }
    }

    /**
     * 防止重复初始化各个模块
     */
    private void initTag() {
        if (tag_coach != null) {
            switch (tag_coach) {
                case "home__coach":
                    showHome();

                    break;
                case "health_coach":
                    showHelth();


                    break;

                case "mine_coach":
                    showMine();


                    break;
            }
        }
    }

    private void showMine() {
        getSupportFragmentManager().beginTransaction()
                .show(mineFragment)
                .hide(homeFragment)
                .hide(healthFrgFragment)
                .commit();
        nowFragment = mineFragment;
        initColor(tb_mine, tb_home, tb_health);
        tag_coach = "mine_coach";
    }


    private void showHelth() {
        // 解决重叠问题
        getSupportFragmentManager().beginTransaction()
                .show(healthFrgFragment)
                .hide(homeFragment)
                .hide(mineFragment)
                .commit();
        nowFragment = healthFrgFragment;
        initColor(tb_health, tb_home, tb_mine);
        tag_coach = "health_coach";
    }

    private void showHome() {
        // 解决重叠问题
        getSupportFragmentManager().beginTransaction()
                .show(homeFragment)
                .hide(healthFrgFragment)
                .hide(mineFragment)
                .commit();
        nowFragment = homeFragment;
        initColor(tb_home, tb_health, tb_mine);
        tag_coach = "home_coach";
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
                if (fragment instanceof HomeFragmentCoach) {
                    homeFragment = (HomeFragmentCoach) fragment;
                }
                if (fragment instanceof HealthFrgFragmentCoach) {
                    healthFrgFragment = (HealthFrgFragmentCoach) fragment;
                }
                if (fragment instanceof MineFragmentCoach) {
                    mineFragment = (MineFragmentCoach) fragment;
                }
            }


        }
    }


    /**
     * 进行findView
     */
    private void initId() {
        tb_home = (ToggleButton) findViewById(R.id.tb_home);
        tb_home.setOnClickListener(this);

        tb_health = (ToggleButton) findViewById(R.id.tb_health);
        tb_health.setOnClickListener(this);


        tb_mine = (ToggleButton) findViewById(R.id.tb_mine);
        tb_mine.setOnClickListener(this);
    }

    /**
     * 放所有的方法
     */


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化标签
     */
    private void initTab() {
        formatToggleButton(tb_home, R.drawable.frag_one_coach_selector,
                R.string.tb_home_hss_coach, getResources().getDimensionPixelSize(R.dimen.mine_four), getResources().getDimensionPixelSize(R.dimen.mine_four));
        formatToggleButton(tb_health, R.drawable.frag_two_coach_selector,
                R.string.tb_health_coach, getResources().getDimensionPixelSize(R.dimen.mine_four), getResources().getDimensionPixelSize(R.dimen.mine_four));

        formatToggleButton(tb_mine, R.drawable.frag_mine_coach_selector,
                R.string.tb_mine_coach, getResources().getDimensionPixelSize(R.dimen.mine_four), getResources().getDimensionPixelSize(R.dimen.mine_four));
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
            homeFragment = new HomeFragmentCoach();
        }
        if (healthFrgFragment == null) {
            healthFrgFragment = new HealthFrgFragmentCoach();
        }

        if (mineFragment == null) {
            mineFragment = new MineFragmentCoach();
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
                showHome();

                break;
            case R.id.tb_health:

                showHelth();
                break;

            case R.id.tb_mine:
                showMine();

                break;
            default:
                break;
        }

    }

    private void initAdaaFragment() {
        initFragment(homeFragment, "home_coach");
        initFragment(healthFrgFragment, "health_coach");
        initFragment(mineFragment, "mine_coach");
    }

    /**
     * 设定当前的TextView的图片和字体颜色
     *
     * @param tv_one
     * @param tv_two
     */
    private void initColor(ToggleButton tv_one, ToggleButton tv_two, ToggleButton tv_four) {
        tv_one.setChecked(true);
        tv_two.setChecked(false);
        tv_four.setChecked(false);
    }


}
