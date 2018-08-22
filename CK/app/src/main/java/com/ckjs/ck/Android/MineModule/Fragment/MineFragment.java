package com.ckjs.ck.Android.MineModule.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.RealNameActivity;
import com.ckjs.ck.Android.HomeModule.Activity.RealNameYsActivity;
import com.ckjs.ck.Android.HomeModule.Fragment.HomeFragment;
import com.ckjs.ck.Android.MainCoachActivity;
import com.ckjs.ck.Android.MineModule.Activity.MIneFrgGzActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineAimShowActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineFavoriteActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineGymInfoActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineInformationActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineIntegralActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineInvitedFriendsActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineJoinActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineOrderNewActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineRecordActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineSerchJSFActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineSetActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineTargetActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineUserInfoEditeActivity;
import com.ckjs.ck.Android.MineModule.Activity.ShareHSHActivity;
import com.ckjs.ck.Android.MineModule.Activity.SjRzActivity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.GetInfoBean;
import com.ckjs.ck.Bean.LetterstatusBean;
import com.ckjs.ck.Manager.NotifyActivityAddJsfManager;
import com.ckjs.ck.Manager.NotifyInfoUpdateMessageManager;
import com.ckjs.ck.Manager.NotifyJpushLetterManager;
import com.ckjs.ck.Manager.NotifyPostUserInforMessageManager;
import com.ckjs.ck.Message.NotifyActicityAddJsfMessage;
import com.ckjs.ck.Message.NotifyInfoUpdateMessage;
import com.ckjs.ck.Message.NotifyPostUserInfoMessage;
import com.ckjs.ck.Message.NotifyjpushLetterMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class MineFragment extends Fragment implements View.OnClickListener, NotifyInfoUpdateMessage, NotifyPostUserInfoMessage, NotifyjpushLetterMessage, NotifyActicityAddJsfMessage, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.ll_sj)
    LinearLayout ll_sj;

    private View view;
    private SimpleDraweeView mine_userpic;
    private TextView tvName;
    private GetInfoBean getinfobean;
    private float BMI;
    private LinearLayout mJoin;
    private LinearLayout mTarget;
    private LinearLayout mAttention;
    private LinearLayout mFavorite;
    private LinearLayout ll_my_order;
    private LinearLayout mRecord;
    private LinearLayout ll_edit_file;
    private SimpleDraweeView mSexpic;
    private TextView tvSignName;
    private TextView tvFans;
    private SimpleDraweeView mLevel;
    private TextView tvJsf;
    private TextView bind_bracelet;
    private LinearLayout ll_bind_bracelet;
    private String token = "";
    private SimpleDraweeView mine_gym_icon;
    private LinearLayout ll_jsf;
    float width = ScreenUtils.getScreenWidth();
    private SimpleDraweeView button_left;
    private String iscoach = "";
    private TextView tv_ren_zhen;
    private LinearLayout ll_tj;
    private LinearLayout ll_dl;
    private LinearLayout ll_bind_bracelet_new;
    private TextView tv_dl;
    private LinearLayout ll_mine_integral_new;
    private LinearLayout ll_invite_friends;
    private LinearLayout ll_yj;
    private SimpleDraweeView sd_mine_top_bg;

    public MineFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mine_new, container, false);
        ButterKnife.bind(this, view);
        initId();
        initToolbar();
        getActivity().registerReceiver(mGattUpdateReceiver, MoudleUtils.makeGattUpdateIntentFilter());
        initGetInfo("", "up");
        initToGetSxRed();
        return view;

    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (AppConfig.ACTION_GATT_CONNECTED.equals(action)) {
                showMineDl(true);
                MoudleUtils.textViewSetText(tv_dl, "数据开始通讯");
            } else if (AppConfig.ACTION_GATT_DISCONNECTED.equals(action)) {
                MoudleUtils.textViewSetText(tv_dl, "请重新连接");
            } else if (AppConfig.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                MoudleUtils.textViewSetText(tv_dl, "数据通讯中");
            } else if (AppConfig.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(AppConfig.EXTRA_DATA_UUID), intent.getByteArrayExtra(AppConfig.EXTRA_DATA));
            }
        }
    };

    private void displayData(String uuid, byte[] data) {
        if (uuid.contains("fff2")) {
            if (data != null) {
                if (data.length > 0) {
                    initDl(data);
                }
            }
        }

    }

    private void initDl(byte[] data) {
        if (data.length == 2) {
            int[] ints = DataUtils.byteTo10(data);
            if (ints.length > 1) {
                MoudleUtils.textViewSetText(tv_dl, "剩余电量  " + ints[1] + "%");
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

    }

    /**
     * 各个回掉注册
     */
    private void initData() {

        NotifyInfoUpdateMessageManager.getInstance().setNotifyMessage(this);//修改个人信息后刷新后刷新
        NotifyPostUserInforMessageManager.getInstance().setNotifyMessage(this);//上传个人信息后刷新后刷新
        NotifyJpushLetterManager.getInstance().setNotifyMessage(this);//极光推送接受私信小红点变化
        NotifyActivityAddJsfManager.getInstance().setNotifyMessage(this);//极光推送改变健身房图标
    }

    /**
     * 走是否有新的私信查询网络请求，并且请求成功后刷新页面（显示小红点）
     */
    private void initToGetSxRed() {

        NpApi npApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");

        Call<LetterstatusBean> callBack = npApi.letterstatus(token, user_id);


        callBack.enqueue(new Callback<LetterstatusBean>() {
            @Override
            public void onResponse(Call<LetterstatusBean> call, Response<LetterstatusBean> response) {
                if (getActivity() != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            FrescoUtils.setImage(button_left, AppConfig.res + R.drawable.letter_red);
                        } else if (response.body().getStatus().equals("0")) {
                            FrescoUtils.setImage(button_left, AppConfig.res + R.drawable.letter);
                        } else if (response.body().getStatus().equals("2")) {
                           MoudleUtils.initStatusTwo(getActivity(),true);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<LetterstatusBean> call, Throwable t) {


            }
        });
    }

    /**
     * 获取个人信息网络接口
     */
    private void initGetInfo(final String name, final String nameIsUp) {
        token = (String) SPUtils.get(getActivity(), "token", "");
        if (!token.equals("")) {
            int user_id = 0;
            try {
                user_id = (int) (SPUtils.get(getActivity(), "user_id", user_id));
            } catch (Exception e) {
                e.printStackTrace();
                user_id = AppConfig.user_id;
            }
            WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
            Call<GetInfoBean> callBack = restApi.getinfo(token, user_id);


            callBack.enqueue(new Callback<GetInfoBean>() {
                @Override
                public void onResponse(Call<GetInfoBean> call, Response<GetInfoBean> response) {
                    synchronized (this) {
                        if (getActivity() != null) {
                            getinfobean = response.body();
//                            if (name.equals("click")) {
//                                bindJSF();//绑定健身房
//                            }
                            if (getinfobean != null) {
                                if (getinfobean.getStatus().equals("1")) {
                                    initUpUiTask(getinfobean, nameIsUp);
                                } else if (getinfobean.getStatus().equals("0")) {
                                    initUi();
                                    ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                                } else if (getinfobean.getStatus().equals("2")) {
                                    MoudleUtils.initStatusTwo(getActivity(), true);
                                }
                            } else {
                                initUi();
                            }
                        }
                    }
                    MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHealth);
                }

                @Override
                public void onFailure(Call<GetInfoBean> call, Throwable t) {
                    synchronized (this) {
                        if (getActivity() != null) {
                            initUi();
                        }
                    }
                    MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHealth);
                }
            });
        }
    }


    /**
     * 标题栏
     */
    private void initToolbar() {

        TextView textView = (TextView) view.findViewById(R.id.toolbar_title);
        textView.setText("个人中心");
        SimpleDraweeView button = (SimpleDraweeView) view.findViewById(R.id.sd_button);
        RelativeLayout r_toolbar_button = (RelativeLayout) view.findViewById(R.id.r_toolbar_button);
        button_left = (SimpleDraweeView) view.findViewById(R.id.left_button);
        FrescoUtils.setImage(button_left, AppConfig.res + R.drawable.letter);
        RelativeLayout l_toolbar_button = (RelativeLayout) view.findViewById(R.id.l_toolbar_button);
        l_toolbar_button.setOnClickListener(this);
        r_toolbar_button.setOnClickListener(this);
        button_left.setOnClickListener(this);
        FrescoUtils.setImage(button, AppConfig.res + R.drawable.personal_settings);

        button.setOnClickListener(this);
    }

    /**
     * 绑定view-id
     */
    private void initId() {
        ll_dl = (LinearLayout) view.findViewById(R.id.ll_dl);
        ll_yj = (LinearLayout) view.findViewById(R.id.ll_yj);
        ll_yj.setOnClickListener(this);
        ll_tj = (LinearLayout) view.findViewById(R.id.ll_tj);
        ll_jsf = (LinearLayout) view.findViewById(R.id.ll_Jsf);
        mine_gym_icon = (SimpleDraweeView) view.findViewById(R.id.mine_gym_icon);
        mLevel = (SimpleDraweeView) view.findViewById(R.id.mine_level);
        tvSignName = (TextView) view.findViewById(R.id.tvSignName);
        tvFans = (TextView) view.findViewById(R.id.tvFans);
        tvJsf = (TextView) view.findViewById(R.id.tvJsf);
        tvFans.setOnClickListener(this);
        mSexpic = (SimpleDraweeView) view.findViewById(R.id.mine_sexpic);
        ll_edit_file = (LinearLayout) view.findViewById(R.id.mine_edit_file);
        mRecord = (LinearLayout) view.findViewById(R.id.mine_record);
        mFavorite = (LinearLayout) view.findViewById(R.id.mine_favorite);
        mJoin = (LinearLayout) view.findViewById(R.id.mine_join);
        mTarget = (LinearLayout) view.findViewById(R.id.mine_target);
        mAttention = (LinearLayout) view.findViewById(R.id.mine_attention);
        ll_my_order = (LinearLayout) view.findViewById(R.id.ll_my_order);

        tv_ren_zhen = (TextView) view.findViewById(R.id.tv_ren_zhen);
        mine_userpic = (SimpleDraweeView) view.findViewById(R.id.mine_userpic);
        tvName = (TextView) view.findViewById(R.id.tvName);

        tv_dl = (TextView) view.findViewById(R.id.tv_dl);
        ll_invite_friends = (LinearLayout) view.findViewById(R.id.ll_invite_friends);

        ll_invite_friends.setOnClickListener(this);
        ll_jsf.setOnClickListener(this);
        mRecord.setOnClickListener(this);
        mJoin.setOnClickListener(this);
        mTarget.setOnClickListener(this);
        mAttention.setOnClickListener(this);
        mFavorite.setOnClickListener(this);
        ll_my_order.setOnClickListener(this);
        ll_edit_file.setOnClickListener(this);


        ll_mine_integral_new = (LinearLayout) view.findViewById(R.id.ll_mine_integral_new);
        bind_bracelet = (TextView) view.findViewById(R.id.bind_bracelet);
        ll_bind_bracelet = (LinearLayout) view.findViewById(R.id.ll_bind_bracelet);
        ll_bind_bracelet_new = (LinearLayout) view.findViewById(R.id.ll_bind_bracelet_new);
        ll_bind_bracelet.setOnClickListener(this);
        ll_bind_bracelet_new.setOnClickListener(this);
        ll_mine_integral_new.setOnClickListener(this);

        LinearLayout ll_mine_aimcard = (LinearLayout) view.findViewById(R.id.ll_mine_aimcard);
        ll_mine_aimcard.setOnClickListener(this);

        sd_mine_top_bg = (SimpleDraweeView) view.findViewById(R.id.sd_mine_top_bg);

        swipeRefreshLayoutHealth = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutHealth);
        swipeRefreshLayoutHealth.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayoutHealth.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutHealth.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutHealth.setOnRefreshListener(this);
    }

    private SwipeRefreshLayout swipeRefreshLayoutHealth;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGattUpdateReceiver != null) {
            MoudleUtils.unregisterReceivermGattUpdateReceiver(getActivity(), mGattUpdateReceiver);
        }
    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        String token = (String) SPUtils.get(getActivity(), "token", "");

        if (!token.equals("")) {
            switch (v.getId()) {
                case R.id.mine_edit_file:
                    initToMineUserInfo();
                    break;
                case R.id.sd_button:
                case R.id.r_toolbar_button:
                    toSet();
                    break;
                case R.id.mine_join:
                    startActivity(new Intent().setClass(getActivity(), MineJoinActivity.class));//我已参加
                    break;
                case R.id.mine_target:
                    startActivity(new Intent().setClass(getActivity(), MineTargetActivity.class));//我的目标
                    break;
                case R.id.mine_attention:
                    startActivity(new Intent().setClass(getActivity(), MIneFrgGzActivity.class));//我的朋友
                    break;

                case R.id.mine_favorite:
                    startActivity(new Intent().setClass(getActivity(), MineFavoriteActivity.class));//我的收藏
                    break;
                case R.id.ll_my_order:
                    startActivity(new Intent().setClass(getActivity(), MineOrderNewActivity.class));//我的订单
                    break;
                case R.id.mine_record:
                    startActivity(new Intent().setClass(getActivity(), MineRecordActivity.class));//我的记录
                    break;
                case R.id.ll_Jsf:
                    //判断实名认证
                    if ((SPUtils.get(getActivity(), "vip", "").equals("1"))) {
                        if ((SPUtils.get(getActivity(), "type", "") + "").equals("1")) {
                            startActivity(new Intent().setClass(getActivity(), RealNameYsActivity.class));
                        } else {
                            startActivity(new Intent().setClass(getActivity(), RealNameActivity.class));
                        }
                    } else {
                        try {
//                            initGetInfo("click", "up");
                            bindJSF();//绑定健身房
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case R.id.ll_bind_bracelet:
                case R.id.ll_bind_bracelet_new:
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            bind();
                        }
                    });
                    break;
                case R.id.l_toolbar_button:
                case R.id.left_button:
                    FrescoUtils.setImage(button_left, AppConfig.res + R.drawable.letter);
                    //我的私信
                    startActivity(new Intent().setClass(getActivity(), MineInformationActivity.class));
                    break;

                case R.id.ll_mine_integral_new:
                    startActivity(new Intent().setClass(getActivity(), MineIntegralActivity.class));//我的积分
                    break;
                case R.id.ll_mine_aimcard:
                    //判断实名认证
                    if ((SPUtils.get(getActivity(), "vip", "").equals("1"))) {
                        if ((SPUtils.get(getActivity(), "type", "") + "").equals("1")) {
                            startActivity(new Intent().setClass(getActivity(), RealNameYsActivity.class));
                        } else {
                            startActivity(new Intent().setClass(getActivity(), RealNameActivity.class));
                        }
                    } else {
                        startActivity(new Intent().setClass(getActivity(), MineAimShowActivity.class));//医疗急救卡
                    }
                    break;
                case R.id.ll_invite_friends:
                    startActivity(new Intent().setClass(getActivity(), MineInvitedFriendsActivity.class));//邀请好友
                    break;
                case R.id.ll_yj:
                    //判断实名认证
                    if ((SPUtils.get(getActivity(), "vip", "").equals("1"))) {
                        if ((SPUtils.get(getActivity(), "type", "") + "").equals("1")) {
                            startActivity(new Intent().setClass(getActivity(), RealNameYsActivity.class));
                        } else {
                            startActivity(new Intent().setClass(getActivity(), RealNameActivity.class));
                        }
                    } else {
                        //unrentstatus|0：可用；1：不可用
                        initToYajin();
                    }
                    break;
            }
        } else {
            MoudleUtils.initToLogin(getActivity());
        }
    }

    /***
     * 跳转到共享手环模块
     */
    private void initToYajin() {
        startActivity(new Intent().setClass(getActivity(), ShareHSHActivity.class));
    }

    private synchronized void bindJSF() {
        if (getinfobean != null) {
            if (getinfobean.getInfo() != null) {
                if (getinfobean.getInfo().getGym_id() != 1) {
                    startActivity(new Intent().setClass(getActivity(), MineGymInfoActivity.class));//我的健身房信息
                } else {
                    startActivity(new Intent().setClass(getActivity(), MineSerchJSFActivity.class));//搜索健身房
                }
            }
        }
    }



    /**
     * 绑定手环
     */
    private void bind() {
        try {
            if (BluethUtils.bluetoothAdapter == null) {
                ToastUtils.showShort(CkApplication.getInstance(), "对不起，您的机器不具备蓝牙功能");
                return;
            }
            if (BluethUtils.bluetoothAdapter.isEnabled()) {
                String token = (String) SPUtils.get("token", "");
                if (token.equals("")) {
                    MoudleUtils.initToLogin(getActivity());
                } else {
                    MoudleUtils.initToBd(getActivity(), "mine");
                }
            } else {
                BluethUtils.initGetBluetooth(getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到设置页面
     */
    private void toSet() {
        startActivity(new Intent().setClass(getActivity(), MineSetActivity.class));

    }


    /**
     * 进行本地数据更新我页面数据
     */
    private synchronized void initUi() {
        iscoach = (String) SPUtils.get("iscoach", iscoach);
        if (iscoach != null) {
            initOncliSj();
            if (iscoach.equals("0")) {
                MoudleUtils.textViewSetText(tv_ren_zhen, "超空私教认证");

            } else if (iscoach.equals("1")) {
                MoudleUtils.textViewSetText(tv_ren_zhen, "超空私教入口");

            } else {
                MoudleUtils.textViewSetText(tv_ren_zhen, "私教？");
            }
        } else {
            MoudleUtils.textViewSetText(tv_ren_zhen, "私教？");
        }
        String picurl = "";
        String name = "超空";
        String motto = "这个家伙很懒，什么都没有留下";
        String fanssum = " ？";
        String gymname = "";
        int gymId = (int) SPUtils.get(getActivity(), "gym_id", 0);
        gymname = (String) SPUtils.get(getActivity(), "gymname", gymname);
        picurl = (String) SPUtils.get(getActivity(), "picurl", picurl);
        name = (String) SPUtils.get(getActivity(), "name", name);
        motto = (String) SPUtils.get(getActivity(), "motto", motto);
        fanssum = SPUtils.get(getActivity(), "fanssum", "") + " 粉丝";
        if (!picurl.equals("")) {
            FrescoUtils.setImage(mine_userpic, AppConfig.url + picurl);
        }
        final String finalPicurl = picurl;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    DataUtils.downLoadImage(Uri.parse(AppConfig.url + finalPicurl), sd_mine_top_bg, getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (!name.equals("")) {
            tvName.setText(name);
        }
        if (!motto.equals("")) {
            tvSignName.setText(motto);
        }
        if (!fanssum.equals("")) {
            tvFans.setText(fanssum);
        }
        if (!gymname.equals("")) {
            tvJsf.setText(gymname);
            if (gymId == 1) {
                FrescoUtils.setImage(mine_gym_icon, AppConfig.res + R.drawable.my_member_icon_no);
            } else {
                FrescoUtils.setImage(mine_gym_icon, AppConfig.res + R.drawable.my_member_icon);
            }
        }

        float weight = 0;
        int h = 0;
        weight = (float) SPUtils.get(getActivity(), "weight", weight);
        h = (int) SPUtils.get(getActivity(), "height", 0);


        int age;
        age = (int) SPUtils.get(getActivity(), "age", 0);
        int sex;
        sex = (int) SPUtils.get(getActivity(), "sex", 0);
        if (sex == 1) {
            FrescoUtils.setImage(mSexpic, "res://com.ckjs.ck/" + R.drawable.my_boy);
        } else if (sex == 2) {
            FrescoUtils.setImage(mSexpic, "res://com.ckjs.ck/" + R.drawable.my_girl);
        } else {
            FrescoUtils.setImage(mSexpic, "res://com.ckjs.ck/" + R.drawable.my_boy);
        }
        String vip = "";
        vip = (String) SPUtils.get(getActivity(), "vip", vip);
        if (vip.equals("1")) {
            FrescoUtils.setImage(mLevel, "res://com.ckjs.ck/" + R.drawable.member_leve);
        } else if (vip.equals("2")) {
            FrescoUtils.setImage(mLevel, "res://com.ckjs.ck/" + R.drawable.member_levetwo);
        } else {
            FrescoUtils.setImage(mLevel, "");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        synchronized (this) {

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        initPrompt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    private void showMineDl(boolean flag) {
        if (flag) {
            MoudleUtils.viewShow(ll_dl);
            MoudleUtils.viewGone(ll_tj);
        } else {
            MoudleUtils.viewShow(ll_tj);
            MoudleUtils.viewGone(ll_dl);
        }
    }

    /**
     * 蓝牙绑定提示状态判断
     */
    private synchronized void initPrompt() {
        if (BluethUtils.bluetoothAdapter != null) {
            if (BluethUtils.bluetoothAdapter.isEnabled()) {
                bind_bracelet.setText(getResources().getText(R.string.buletooth_activity));
                initToBindAgin();
            } else {
                initToBindAgin();
                bind_bracelet.setText(getResources().getText(R.string.buletooth_open));
            }
        }
    }

    private void initToBindAgin() {
        if ((SPUtils.get(getActivity(), "mDeviceAddress", "") + "") != null
                && !(SPUtils.get(getActivity(), "mDeviceAddress", "") + "").equals("")) {
            showMineDl(true);
        } else {
            showMineDl(false);
        }
    }

    private void initSaveThirdLoginData(GetInfoBean thirdloginBean, String name) {
        float weight = 0;
        int height = 0;
        String relname = "";

        relname = (String) SPUtils.get("relname", relname);
        weight = (float) SPUtils.get("weight", weight);
        height = (int) SPUtils.get("height", 0);
        if (weight == 0 || height == 0 || relname == null || relname.equals("") || name.equals("up")) {
            SavaDataLocalUtils.saveDataInt(getActivity(), "sex", thirdloginBean.getInfo().getSex());
            SavaDataLocalUtils.saveDataString(getActivity(), "picurl", thirdloginBean.getInfo().getPicurl());
            SavaDataLocalUtils.saveDataString(getActivity(), "name", thirdloginBean.getInfo().getUsername());
            SavaDataLocalUtils.saveDataString(getActivity(), "fanssum", thirdloginBean.getInfo().getFanssum());

            SavaDataLocalUtils.saveDataInt(getActivity(), "gym_id", thirdloginBean.getInfo().getGym_id());

            SavaDataLocalUtils.saveDataString(getActivity(), "gymname", thirdloginBean.getInfo().getGymname());
            SavaDataLocalUtils.saveDataString(getActivity(), "vip", thirdloginBean.getInfo().getVip());
            SavaDataLocalUtils.saveDataString(getActivity(), "tel", thirdloginBean.getInfo().getTel());
            SavaDataLocalUtils.saveDataInt(getActivity(), "height", thirdloginBean.getInfo().getHeight());
            SavaDataLocalUtils.saveDataString(getActivity(), "c", thirdloginBean.getInfo().getRelname());

            SavaDataLocalUtils.saveDataFlot(getActivity(), "weight", thirdloginBean.getInfo().getWeight());

            SavaDataLocalUtils.saveDataString(getActivity(), "motto", thirdloginBean.getInfo().getMotto());
            SavaDataLocalUtils.saveDataInt(getActivity(), "age", thirdloginBean.getInfo().getAge());
            SavaDataLocalUtils.saveDataString(getActivity(), "bodyanalyse", thirdloginBean.getInfo().getBodyanalyse());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyinfo", thirdloginBean.getInfo().getBodyinfo());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "iscoach", thirdloginBean.getInfo().getIscoach());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "unrentstatus", thirdloginBean.getInfo().getUnrentstatus());

            MoudleUtils.initSaveData(getActivity());
        }
    }

    /**
     * 进行网络数据进行我页面数据更新
     *
     * @param getinfobean
     */
    private synchronized void initUpUiTask(final GetInfoBean getinfobean, String name) {
        if (getinfobean.getInfo() != null) {
            FrescoUtils.setImage(mine_userpic, AppConfig.url + getinfobean.getInfo().getPicurl());
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        DataUtils.downLoadImage(Uri.parse(AppConfig.url + getinfobean.getInfo().getPicurl()), sd_mine_top_bg, getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            tvName.setText(getinfobean.getInfo().getUsername());
            tvSignName.setText(getinfobean.getInfo().getMotto());
            tvFans.setText(getinfobean.getInfo().getFanssum() + " 粉丝");
            tvJsf.setText(getinfobean.getInfo().getGymname());
            if (getinfobean.getInfo().getGym_id() == 1) {
                MoudleUtils.textViewSetText(tvJsf, "野生的健身者");
                FrescoUtils.setImage(mine_gym_icon, AppConfig.res + R.drawable.my_member_icon_no);
            } else {
                FrescoUtils.setImage(mine_gym_icon, AppConfig.res + R.drawable.my_member_icon);
            }
            initSaveThirdLoginData(getinfobean, name);
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "unrentstatus", getinfobean.getInfo().getUnrentstatus());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyanalyse", getinfobean.getInfo().getBodyanalyse());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyinfo", getinfobean.getInfo().getBodyinfo());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "iscoach", getinfobean.getInfo().getIscoach());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "isbind", getinfobean.getInfo().getIsbind());

            iscoach = getinfobean.getInfo().getIscoach();
            if (iscoach != null) {
                initOncliSj();
                if (iscoach.equals("0")) {
                    MoudleUtils.textViewSetText(tv_ren_zhen, "超空私教认证");

                } else if (iscoach.equals("1")) {
                    MoudleUtils.textViewSetText(tv_ren_zhen, "超空私教入口");

                } else {
                    MoudleUtils.textViewSetText(tv_ren_zhen, "私教？");
                }
            } else {
                MoudleUtils.textViewSetText(tv_ren_zhen, "私教？");
            }
            if (getinfobean.getInfo().getSex() == 1) {
                FrescoUtils.setImage(mSexpic, "res://com.ckjs.ck/" + R.drawable.my_boy);
            } else if (getinfobean.getInfo().getSex() == 2) {
                FrescoUtils.setImage(mSexpic, "res://com.ckjs.ck/" + R.drawable.my_girl);
            } else {
                FrescoUtils.setImage(mSexpic, "res://com.ckjs.ck/" + R.drawable.my_boy);
            }
            if (getinfobean.getInfo().getVip().equals("1")) {
                FrescoUtils.setImage(mLevel, "res://com.ckjs.ck/" + R.drawable.member_leve);
            } else if (getinfobean.getInfo().getVip().equals("2")) {
                FrescoUtils.setImage(mLevel, "res://com.ckjs.ck/" + R.drawable.member_levetwo);
            } else {
                FrescoUtils.setImage(mLevel, "");
            }
        } else {
            ToastUtils.show(getActivity(), "设置失败", 0);
        }
    }

    private void initOncliSj() {
        ll_sj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iscoach != null) {
                    if (iscoach.equals("0")) {//未认证
                        startActivity(new Intent().setClass(getActivity(), SjRzActivity.class));
                    } else if (iscoach.equals("1")) {//已认证
                        startActivity(new Intent().setClass(getActivity(), MainCoachActivity.class));
                    }
                }
            }
        });
    }

    /**
     * 进入基本信息修改页
     */
    private void initToMineUserInfo() {
        startActivity(new Intent().setClass(getActivity(), MineUserInfoEditeActivity.class));
    }


    /**
     * 修改信息后的回掉
     *
     * @param flag
     */
    @Override
    public void sendMessageInfoUpdate(boolean flag) {
        if (flag) {
            //更新信息后操作
            initGetInfo("", "up");
            initToGetSxRed();
        }
    }


    /**
     * 初次上传信息后的回掉
     *
     * @param flagRefresh
     */
    @Override
    public void sendMessagePostUserInfo(boolean flagRefresh) {
        if (flagRefresh) {
            //更新信息后操作
            initGetInfo("", "up");
            initToGetSxRed();
        }
    }


    /**
     * 极光推送接受更改是否有私信状态
     *
     * @param flag
     * @param letter
     */
    @Override
    public void sendMessageJpushLetterFlag(boolean flag, String letter) {
        if (flag) {
            if (letter != null) {
                if (letter.equals("1")) {
                    FrescoUtils.setImage(button_left, AppConfig.res + R.drawable.letter_red);
                }
            }
        }
    }

    @Override
    public void sendMessageActivityAddJsfFlag(boolean flag) {
        if (flag) {
            try {
                initGetInfo("", "up");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRefresh() {
        try {
            if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                if (HomeFragment.nHealthFlag && HomeFragment.nSleepFlag) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff2");
                        }
                    }, 500);
                }
            }
            initGetInfo("", "up");
            initToGetSxRed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
