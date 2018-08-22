package com.ckjs.ck.Android.HealthModule.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.ListAdapterHealth;
import com.ckjs.ck.Android.HomeModule.Activity.RealNameActivity;
import com.ckjs.ck.Android.HomeModule.Activity.RealNameYsActivity;
import com.ckjs.ck.Android.MineModule.Activity.MineDataH5Activity;
import com.ckjs.ck.Android.MineModule.Activity.MineEditHeightActivity;
import com.ckjs.ck.Android.MineModule.Activity.YuYueActivity;
import com.ckjs.ck.Android.MineModule.Activity.YuYuedActivity;
import com.ckjs.ck.Android.MineModule.Adapter.GridAdapterBodyInfoMine;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.GetBodyInfoBean;
import com.ckjs.ck.Bean.GetInfoBean;
import com.ckjs.ck.Bean.GetdirectBaen;
import com.ckjs.ck.Bean.MydirectBean;
import com.ckjs.ck.Interface.TaskResultApi;
import com.ckjs.ck.Manager.NotifyJpushManager;
import com.ckjs.ck.Message.NotifyjpushMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.MyListView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HealthCustomFragment extends Fragment implements TaskResultApi, NotifyjpushMessage, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private ListAdapterHealth listAdapterHealth;
    private MyListView listView;
    private MydirectBean beaned;

    private List<GetdirectBaen.GetdirectBeanInfo> list = new ArrayList<>();
    private Call<MydirectBean> call;
    private TextView mBmr;
    private TextView tvBMI;
    private TextView bmiPic;
    private TextView tvWeight;
    private LinearLayout mine_editheight;
    private Button btn_yj;
    private TextView tvTime;
    private RelativeLayout mine_subscribe_data;
    private LinearLayout mine_subscribe;
    private TextView tv_biao_zhun_weight;
    private SwipeRefreshLayout swipeRefreshLayoutHealth;

    public HealthCustomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.frag_health_new, container, false);


        initId();
        init();

        relname = (String) SPUtils.get("relname", relname);
        weight = (float) SPUtils.get("weight", weight);
        height = (int) SPUtils.get("height", 0);
        initGetInfo("up");
        initTask();
        inittologinTzData("login");
        return view;
    }

    float weight = 0;
    int height = 0;
    String relname = "";

    @Override
    public void onResume() {
        super.onResume();
        synchronized (this) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        try {
                            initData();
                            if (weight != 0 && height != 0) {
                                initBim();
                                initBmr();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    private void initSaveThirdLoginData(GetInfoBean thirdloginBean, String name) {
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
            SavaDataLocalUtils.saveDataString(getActivity(), "relname", thirdloginBean.getInfo().getRelname());

            SavaDataLocalUtils.saveDataFlot(getActivity(), "weight", thirdloginBean.getInfo().getWeight());

            SavaDataLocalUtils.saveDataString(getActivity(), "motto", thirdloginBean.getInfo().getMotto());
            SavaDataLocalUtils.saveDataInt(getActivity(), "age", thirdloginBean.getInfo().getAge());
            SavaDataLocalUtils.saveDataString(getActivity(), "bodyanalyse", thirdloginBean.getInfo().getBodyanalyse());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyinfo", thirdloginBean.getInfo().getBodyinfo());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "iscoach", thirdloginBean.getInfo().getIscoach());
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "unrentstatus", thirdloginBean.getInfo().getUnrentstatus());

            initBim();
            initBmr();
            MoudleUtils.initSaveData(getActivity());

        }
    }


    /**
     * 获取个人信息网络接口
     */
    private void initGetInfo(final String name) {
        String token = (String) SPUtils.get(getActivity(), "token", "");
        if (!token.equals("")) {
            int user_id = 0;
            user_id = (int) (SPUtils.get(getActivity(), "user_id", user_id));

            WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
            Call<GetInfoBean> callBack = restApi.getinfo(token, user_id);

            callBack.enqueue(new Callback<GetInfoBean>() {
                @Override
                public void onResponse(Call<GetInfoBean> call, Response<GetInfoBean> response) {
                    GetInfoBean getinfobean = response.body();
                    if (getinfobean != null) {
                        if (getinfobean.getStatus().equals("1")) {
                            initSaveThirdLoginData(getinfobean, name);
                            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyanalyse", getinfobean.getInfo().getBodyanalyse());
                            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyinfo", getinfobean.getInfo().getBodyinfo());
                            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "unrentstatus", getinfobean.getInfo().getUnrentstatus());
                            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "iscoach", getinfobean.getInfo().getIscoach());
                            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "isbind", getinfobean.getInfo().getIsbind());
                        } else if (getinfobean.getStatus().equals("2")) {
                            MoudleUtils.initStatusTwo(getActivity(), true);
                        }
                    }
                    MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHealth);

                }

                @Override
                public void onFailure(Call<GetInfoBean> call, Throwable t) {
                    MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHealth);

                }
            });
        }
    }

    private synchronized void initData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String date = formatter.format(curDate);
        tvTime.setText(date);
    }


    private synchronized void initBim() {
        float bmi = DataUtils.setTvBMI(getActivity(), tvWeight, tv_biao_zhun_weight);
        tvBMI.setText("BMI:" + bmi);
        bmiPic.setLayoutParams(DataUtils.initBmiPic(bmi));
    }

    private synchronized void initBmr() {
        mBmr.setText("您的基本代谢为" + DataUtils.setTvBMR(getActivity()) + "千卡，请加强锻炼！");
    }

    private void init() {
        NotifyJpushManager.getInstance().setNotifyMessage(this);
    }


    private void initTask() {
        int user_id = 0;
        user_id = (int) SPUtils.get(getActivity(), "user_id", user_id);
        String token = "";
        token = (String) SPUtils.get(getActivity(), "token", token);

        call = RetrofitUtils.retrofit.create(NpApi.class).mydirect(token, user_id);
        call.enqueue(new Callback<MydirectBean>() {
            @Override
            public void onResponse(Call<MydirectBean> call, Response<MydirectBean> response) {
                if (getActivity() != null) {
                    MydirectBean bean = response.body();
                    beaned = bean;
                    initStatusOne();
                    initStatusZero();
                    initStatusTwo();
                }
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHealth);

            }

            @Override
            public void onFailure(Call<MydirectBean> call, Throwable t) {
                ToastUtils.showShort(getActivity(), "网络状况异常，请检查");
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHealth);

            }
        });

    }


    private void initAdapter() {
        listAdapterHealth = new ListAdapterHealth(getActivity());
        listAdapterHealth.setList(list);
        listView.setAdapter(listAdapterHealth);
    }


    private void initId() {
        listView = (MyListView) view.findViewById(R.id.listViewHealthCustomized);

        tv_biao_zhun_weight = (TextView) view.findViewById(R.id.tv_biao_zhun_weight);
        tvBMI = (TextView) view.findViewById(R.id.mine_bmi);
        tvWeight = (TextView) view.findViewById(R.id.mine_weight);
        mBmr = (TextView) view.findViewById(R.id.mine_bmr);
        bmiPic = (TextView) view.findViewById(R.id.mine_bmipic);
        mine_editheight = (LinearLayout) view.findViewById(R.id.mine_editheight);
        mine_editheight.setOnClickListener(this);
        btn_yj = (Button) view.findViewById(R.id.btn_yj);
        btn_yj.setOnClickListener(this);
        tvTime = (TextView) view.findViewById(R.id.mine_time);
        bodyInfo = (GridView) view.findViewById(R.id.mine_body_info);
        mine_subscribe_data = (RelativeLayout) view.findViewById(R.id.mine_subscribe_data);
        mine_subscribe = (LinearLayout) view.findViewById(R.id.mine_subscribe);
        mine_subscribe_data.setOnClickListener(this);
        tv_mine_body_info_detail = (TextView) view.findViewById(R.id.tv_mine_body_info_detail);
        mine_body_info_detail = (SimpleDraweeView) view.findViewById(R.id.mine_body_info_detail);
        tv_mine_body_info_detail.setOnClickListener(this);
        mine_body_info_detail.setOnClickListener(this);
        mine_re_yuyue = (Button) view.findViewById(R.id.mine_re_yuyue);
        mine_re_yuyue.setOnClickListener(this);

        swipeRefreshLayoutHealth = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutHealth);
        swipeRefreshLayoutHealth.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayoutHealth.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutHealth.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutHealth.setOnRefreshListener(this);

    }

    @Override
    public void initStatusOne() {
        if (beaned != null) {
            if (beaned.getStatus().equals("1")) {
                if (beaned.getInfo() != null) {
                    if (beaned.getInfo().size() > 0) {
                        list = beaned.getInfo();
                        initAdapter();
                    }
                }
            }
        }

    }

    @Override
    public void initStatusZero() {
        if (beaned != null) {
            if (beaned.getStatus().equals("0")) {
                ToastUtils.showShort(getActivity(), beaned.getMsg());
            }
        }
    }

    @Override
    public void initStatusTwo() {
        if (beaned != null) {
            if (beaned.getStatus().equals("2")) {
                MoudleUtils.initStatusTwo(getActivity(), false);
            }
        }
    }


    private TextView tv_mine_body_info_detail;
    private SimpleDraweeView mine_body_info_detail;
    private Button mine_re_yuyue;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_editheight:
                startActivity(new Intent().setClass(getActivity(), MineEditHeightActivity.class));//修改体重
                break;
            case R.id.btn_yj:
                initToYj();
                break;
            case R.id.mine_re_yuyue:
                initToAgainYuYue();
                break;
            case R.id.mine_subscribe_data:
            case R.id.mine_body_info_detail:
            case R.id.tv_mine_body_info_detail:
                initToShowDataH5();
                break;
        }
    }

    /**
     * 进行预约
     */
    private void initToYj() {
        if ((SPUtils.get(getActivity(), "vip", "").equals("1"))) {
            if ((SPUtils.get(getActivity(), "type", "") + "").equals("1")) {
                startActivity(new Intent().setClass(getActivity(), RealNameYsActivity.class));
            } else {
                startActivity(new Intent().setClass(getActivity(), RealNameActivity.class));
            }
        } else {
            inittologinTzData("click");
        }
    }

    /**
     * 登录成获取体脂数据
     */
    private synchronized void inittologinTzData(String name) {
        /**
         * 如果无身体数据则进行显示预约体测按钮return
         */
        if ((SPUtils.get(getActivity(), "bodyinfo", "").equals("0"))) {
            initToIsYy(name);
        }
        /**
         * 如果无身体数据则进行获取数据并且隐藏预约体测按钮return
         */
        initDataTypeTo();

    }

    private void initShowTextBtnyj(String s) {
        btn_yj.setText(s);
    }

    /**
     * 体测数据获取
     */
    private synchronized void initDataTypeTo() {


        if ((SPUtils.get(getActivity(), "bodyinfo", "").equals("1"))) {
            //已体检
            initShowTextBtnyj("已体测,更新数据");
            initToGetTzDataTask();
            return;
        }

    }

    /**
     * 走获取体脂数据的网络请求，并且请求成功后刷新页面（隐藏预约按钮布局，显示体脂信息）
     */
    private void initToGetTzDataTask() {
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(getActivity(), "user_id", 0));
        String token = (String) SPUtils.get(getActivity(), "token", "");

        Call<GetBodyInfoBean> callBack = restApi.bodyinfo(user_id, token);


        callBack.enqueue(new Callback<GetBodyInfoBean>() {
            @Override
            public void onResponse(Call<GetBodyInfoBean> call, Response<GetBodyInfoBean> response) {
                if (getActivity() != null) {

                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            List<GetBodyInfoBean.GetBodyInfoDetailBean> list = response.body().getInfo();
                            initAdapterTc(list);

                        } else if (response.body().getStatus().equals("0")) {
                            initShowTextBtnyj("已体测,更新数据");
                        } else if (response.body().getStatus().equals("2")) {
                            ToastUtils.showShort(getActivity(), response.body().getMsg());
                        }
                    }
                }
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHealth);

            }

            @Override
            public void onFailure(Call<GetBodyInfoBean> call, Throwable t) {

                if (getActivity() != null) {
                    initShowTextBtnyj("已体测,更新数据");
                }
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutHealth);

            }
        });
    }

    private GridView bodyInfo;

    /**
     * 显示预约按钮
     */
    private void initNotShowis(String btn_text) {
        initShowTextBtnyj(btn_text);
        initGoneView(mine_subscribe_data);
        initShowView(mine_subscribe);
    }

    /**
     * 显示界面
     */
    private void initShowView(View mine_subscribe_data) {
        if (mine_subscribe_data.getVisibility() != View.VISIBLE) {
            mine_subscribe_data.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏界面
     */
    private void initGoneView(View mine_subscribe_data) {
        if (mine_subscribe_data.getVisibility() == View.VISIBLE) {
            mine_subscribe_data.setVisibility(View.GONE);
        }
    }

    /**
     * 显示身体数据的GrideView item 点击事件
     */
    private void initSet() {
        bodyInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initToShowDataH5();
            }
        });
    }

    /**
     * 跳转详细身体数据H5
     */

    private void initToShowDataH5() {
        Intent intent = new Intent();
        String token = "";
        token = (String) SPUtils.get("token", token);
        String user_id = ((int) SPUtils.get("user_id", 0)) + "";
        if (token == null || token.equals("")) {
            ToastUtils.showShort(getActivity(), "请重新登录");
            return;
        }
        intent.putExtra("acurl", RetrofitUtils.baseUrlH5 + "circle/bodyinfo?" + "token=" + token + "&" + "user_id=" + user_id);
        intent.setClass(getActivity(), MineDataH5Activity.class);
        startActivity(intent);
    }

    /**
     * 处理身体数据接口获取的数据
     *
     * @param list
     */
    private void initAdapterTc(List<GetBodyInfoBean.GetBodyInfoDetailBean> list) {
        if (getActivity() != null) {
            if (list != null) {
                if (list.size() > 0) {
                    GridAdapterBodyInfoMine gridAdapterBodyInfoMine = new GridAdapterBodyInfoMine(getActivity());

                    gridAdapterBodyInfoMine.setList(list);
                    bodyInfo.setAdapter(gridAdapterBodyInfoMine);
                    initShowView(mine_subscribe_data);
                    initGoneView(mine_subscribe);
                    initSet();
                } else {
                    initShowTextBtnyj("已体测,更新数据");
                }
            } else {
                initShowTextBtnyj("已体测,更新数据");
            }
        } else {
            initShowTextBtnyj("已体测,更新数据");
        }
    }

    /**
     * 根据预约状态显示预约按钮文字
     */
    private synchronized void initToIsYy(String tag) {
        if ((SPUtils.get(getActivity(), "bodyanalyse", "").equals("0"))) {
            //未预约
            initNotShowis("预约体测");
            if (tag.equals("click")) {
                initToYuYueAc();
            }
            return;
        }
        if (SPUtils.get(getActivity(), "bodyanalyse", "").equals("1")) {
            //已预约
            initNotShowis("查看预约");
            if (tag.equals("click")) {
                initToYuYueAced();
            }
            return;
        }
    }

    /**
     * 未预约：进行跳转到附近可预约的健身房列表页面
     */
    private void initToYuYueAc() {
        startActivity(new Intent().setClass(getActivity(), YuYueActivity.class));

    }

    /**
     * 跳转到已预约界面
     */
    private void initToYuYueAced() {
        startActivity(new Intent().setClass(getActivity(), YuYuedActivity.class));
    }

    /**
     * 极光推送接受更改的预约状态进行更新数据
     *
     * @param flag
     * @param bodyanalyse
     */
    @Override
    public void sendMessageJpushFlag(boolean flag, String bodyanalyse, String bodyinfo) {
        if (flag) {
            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyanalyse", bodyanalyse);
            if (!bodyinfo.equals("yuyued")) {
                SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyinfo", bodyinfo);
            }
            inittologinTzData("jpush_bodyanalyse");
        }
    }

    /**
     * 跳转预约界面
     */
    private void initToAgainYuYue() {
        if ((SPUtils.get(getActivity(), "bodyinfo", "").equals("1"))) {
            if ((SPUtils.get(getActivity(), "bodyanalyse", "").equals("0"))) {
                initToYuYueAc();
                return;
            }
            if (SPUtils.get(getActivity(), "bodyanalyse", "").equals("1")) {
                initToYuYueAced();
                return;
            }
        } else {
            ToastUtils.showShort(getActivity(), "账户数据异常，是否清除了账户信息");
        }
    }

    @Override
    public void onRefresh() {
        initGetInfo("up");
        initTask();
        inittologinTzData("login");
        onResume();
    }
}
