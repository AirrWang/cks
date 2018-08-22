package com.ckjs.ck.Android.CkCircleModule.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.CkCircleModule.Activity.CirMsgListActivity;
import com.ckjs.ck.Android.CkCircleModule.Activity.CircleDongTaiActivity;
import com.ckjs.ck.Android.HealthModule.Adapter.ViewPagerFragmentAdapterHealth;
import com.ckjs.ck.Android.HomeModule.Activity.HomeShopH5Activity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.ComnumBean;
import com.ckjs.ck.Manager.NotifyJpushCommenManager;
import com.ckjs.ck.Message.NotifyjpushCommenMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CircleFragment extends Fragment implements View.OnClickListener, NotifyjpushCommenMessage {

    private TabLayout tabLayout;                            //定义TabLayout
    private ViewPager viewPager;                             //定义viewPager

    private ArrayList<Fragment> fragmentList;                            //定义要装fragment的列表
    private View view;


    private int[] ids = {R.string.ckCircle_recommend, R.string.ckCircle_look, R.string.ckCircle_mine};
    private TextView tv_num;
    private LinearLayout ll_num;
    private SimpleDraweeView sd_red;
    //    private Toolbar toolbar;

    public CircleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_three, container, false);

        initId();
        initToolbar();
        initSetThis();
        initToDeleteMyPlNUm();
        initData();
        inset();
        return view;
    }

    private void inset() {
        ll_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    if (!SPUtils.get("token", "").equals("")) {
                        MoudleUtils.viewGone(ll_num);
                        startActivity(new Intent(getActivity(), CirMsgListActivity.class));
                    } else {
                        MoudleUtils.initStatusTwo(getActivity(), true);
                    }
                }
            }
        });
    }

    private void initSetThis() {
        NotifyJpushCommenManager.getInstance().setNotifyMessage(this);
    }

    private void initData() {
        initViewPager();
    }

    private void initToolbar() {
        //        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        TextView textView = (TextView) view.findViewById(R.id.toolbar_title);
        textView.setText(getResources().getText(R.string.tb_ck_circle));
        SimpleDraweeView button = (SimpleDraweeView) view.findViewById(R.id.sd_button);
        RelativeLayout r_toolbar_button = (RelativeLayout) view.findViewById(R.id.r_toolbar_button);
        r_toolbar_button.setOnClickListener(this);
        RelativeLayout l_toolbar_button = (RelativeLayout) view.findViewById(R.id.l_toolbar_button);
        TextView textViewShop = (TextView) view.findViewById(R.id.toolbar_pretitle);
        textViewShop.setText(getResources().getText(R.string.shop));
        textViewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initToShop();
            }
        });
        l_toolbar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initToShop();
            }
        });
        button.setOnClickListener(this);
        FrescoUtils.setImage(button, AppConfig.res + R.drawable.dongtai);
        //        Toolbar.LayoutParams buttonP = (Toolbar.LayoutParams) button.getLayoutParams();
        //        buttonP.width = getResources().getDimensionPixelSize(R.dimen.toolbar_btn_wh);
        //        buttonP.height = getResources().getDimensionPixelSize(R.dimen.toolbar_btn_wh);
        //        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //        if (actionBar != null) {
        //            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
        //            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        //        }
    }

    private void initToShop() {
        Intent intent = new Intent();
        String acurl = "http://www.chaokongs.com/shop/shoplist";
        intent.setClass(getActivity(), HomeShopH5Activity.class);
        intent.putExtra("acurl", acurl);
        startActivity(intent);
    }

    /**
     * 初始化各控件
     */
    private void initId() {

        tabLayout = (TabLayout) view.findViewById(R.id.tab_CkCircleFragment_title);
        viewPager = (ViewPager) view.findViewById(R.id.vp_CkCircleFragment_pager);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        ll_num = (LinearLayout) view.findViewById(R.id.ll_num);
        sd_red = (SimpleDraweeView) view.findViewById(R.id.sd_red);

    }

    private void initViewPager() {
        tabLayout.addTab(tabLayout.newTab().setText(ids[0]));
        tabLayout.addTab(tabLayout.newTab().setText(ids[1]));

        fragmentList = new ArrayList<>();
        fragmentList.add(new CkCircleRecommendFragment());
        fragmentList.add(new CkCircleLookedFragment());
        fragmentList.add(new CkCircleMinedFragment());


        //给ViewPager设置适配器,此处注明***重点***：fragment嵌套fragment用getChildFragmentManager()！！！
        ViewPagerFragmentAdapterHealth adapter = new ViewPagerFragmentAdapterHealth(getChildFragmentManager(), fragmentList, getActivity(), ids);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sd_button:
            case R.id.r_toolbar_button:
                initToDongTai();
                break;
        }
    }

    private void initToDongTai() {
        String token = (String) SPUtils.get(getActivity(), "token", "");
        if (token.equals("")) {
            MoudleUtils.initToLogin(getActivity());
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), CircleDongTaiActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void sendNotifyJpushCommentFlag(boolean flag, String commen) {
        if (flag) {
            if (commen != null) {
                if (commen.equals("1")) {
                    initToDeleteMyPlNUm();
                }
            }
        }
    }

    /**
     * 获取评论数量
     */
    private void initToDeleteMyPlNUm() {
        String token = "";
        token = (String) SPUtils.get("token", token);

        int user_id = (int) SPUtils.get("user_id", 0);
        Call<ComnumBean> call = RetrofitUtils.retrofit.create(NpApi.class).comnum(token, user_id);
        call.enqueue(new Callback<ComnumBean>() {
            @Override
            public void onResponse(Call<ComnumBean> call, Response<ComnumBean> response) {
                ComnumBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {
                            String s = bean.getInfo().getNum();
                            if (s != null) {
                                if (!s.equals("")) {
                                    if (s.equals("0")) {
                                        MoudleUtils.viewGone(ll_num);
                                    } else {
                                        MoudleUtils.textViewSetText(tv_num, "您有" + s + "条新评论");
                                        FrescoUtils.setImage(sd_red, AppConfig.res + R.drawable.message_red);
                                        MoudleUtils.viewShow(ll_num);
                                    }
                                }
                            }
                        }
                    } else if (bean.getStatus().equals("2")) {
                        ToastUtils.showShort(getActivity(), bean.getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<ComnumBean> call, Throwable t) {


            }
        });
    }
}


