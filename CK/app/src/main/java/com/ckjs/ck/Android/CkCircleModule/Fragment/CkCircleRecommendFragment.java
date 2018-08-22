package com.ckjs.ck.Android.CkCircleModule.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.CkCircleModule.Activity.CirPaiHangTodayActivity;
import com.ckjs.ck.Android.CkCircleModule.Activity.CircleRecommenMoreActivity;
import com.ckjs.ck.Android.CkCircleModule.Adapter.ListAdapterCkCircleRecommend;
import com.ckjs.ck.Android.CkCircleModule.Adapter.ListAdapterCkCircleRecommendMain;
import com.ckjs.ck.Android.CkCircleModule.Adapter.ListAdapterCkCircleRecommendXunLian;
import com.ckjs.ck.Android.HomeModule.Activity.AcH5Activity;
import com.ckjs.ck.Android.MineModule.Activity.MineAttentionPeople;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.CaltoplistBean;
import com.ckjs.ck.Bean.FanstoplistBean;
import com.ckjs.ck.Bean.GetCircleTjBean;
import com.ckjs.ck.Bean.GetcommentBean;
import com.ckjs.ck.Manager.NotifyLIstMessageManager;
import com.ckjs.ck.Manager.NotifyPlManager;
import com.ckjs.ck.Message.NotifyPlMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CkCircleRecommendFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, NotifyPlMessage {

    private View view, viewHead;//主布局和List的头布局
    private ListView listView, listViewHead;//帖子的ListView和榜单的ListView
    private ListAdapterCkCircleRecommendMain listAdapterCkCircleMine;//帖子的适配器
    private ListAdapterCkCircleRecommend listAdapterCkCircleRecommend;//榜单内容的适配器
    private ListAdapterCkCircleRecommendXunLian listAdapterCkCircleRecommendXunLian;//榜单内容的适配器
    private SimpleDraweeView textViewPopularity, textViewTrain;//人气榜单和训练榜单操作控件
    private boolean flagPopularity, flagTrain;//人气榜单和训练榜单的是否选中状态标记
    private int since_id, max_id;
    private List<GetCircleTjBean.InfoBean> listAll = new ArrayList<>();
    private View footView;
    private boolean flag;
    private SwipeRefreshLayout swipeRefreshLayoutTj;
    private int headerHeight;
    private TextView tv_ren, tv_xun_lian;
    private LinearLayout llPopularity;
    private LinearLayout llTrain;
    private Call<FanstoplistBean> call;
    private Call<GetCircleTjBean> listcall;
    private Call<CaltoplistBean> listRqcall;
    private int n;
    private SimpleDraweeView sdMore;

    public CkCircleRecommendFragment() {
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
        view = inflater.inflate(R.layout.fragment_ck_circle_recommend, container, false);
        viewHead = inflater.inflate(R.layout.fragment_ck_circle_recommend_head, null);
        footView = inflater.inflate(R.layout.next_foot, null);
        initId();
        initThis();
        initdata();
        initSet();
        return view;
    }

    private void initThis() {
        NotifyPlManager.getInstance().setNotifyMessage(this);
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
                                initJz();
                            }
                        }
                        //判断滚动到顶部
                        if (listView.getFirstVisiblePosition() == 0) {
                            //                            initRefruh();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 && position != listAll.size() + 1) {
                    String type = listAll.get(position - 1).getType();
                    if (type == null) {
                        type = "";
                    }
                    if (type.equals("1")) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), CircleRecommenMoreActivity.class);
                        intent.putExtra("circle_id", listAll.get(position - 1).getId());
                        intent.putExtra("user_id", listAll.get(position - 1).getUser_id());
                        intent.putExtra("name", "recommend");
                        intent.putExtra("p", position - 1);
                        startActivity(intent);
                    } else if (type.equals("2")) {
                        Intent intent = new Intent();
                        intent.putExtra("acurl", listAll.get(position - 1).getUrl());
                        intent.setClass(getActivity(), AcH5Activity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void initRefruh() {
        // 判断滚动到顶部
        View myview = listView.getChildAt(0);
        if (myview != null) {
            // 获取头布局现在的最上部的位置的相反数
            int top = -myview.getTop();
            // 获取头布局的高度
            headerHeight = myview.getHeight();
            // 满足这个条件的时候，是头布局在XListview的最上面第一个控件的时候，只有这个时候，我们才调整透明度
            if (top <= headerHeight && top >= 0) {
                // 获取当前位置占头布局高度的百分比
                float f = (float) (top / (float) headerHeight);
                if (f == 0) {
                    MoudleUtils.initRefrushTrue(swipeRefreshLayoutTj);
                    onRefresh();
                }
            }
        }
    }

    private void initJz() {

        since_id = 0;
        if (listAll != null) {
            if (listAll.size() > 0) {
                max_id = listAll.get(listAll.size() - 1).getId();
                initTask("jz");
            } else {
                showFoot();
            }
        } else {
            showFoot();
        }


    }

    private void initFoot() {
        listView.addFooterView(footView);
    }

    private void initdata() {
        initFalseData();//
        initTvNull();
        initAdapter();
        MoudleUtils.initRefrushTrue(swipeRefreshLayoutTj);
        onRefresh();


    }


    private void initTask(final String zhuangTai) {
        int user_id = 0;
        user_id = (int) SPUtils.get(getActivity(), "user_id", user_id);
        String token = "";
        token = (String) SPUtils.get(getActivity(), "token", token);
        listcall = RetrofitUtils.retrofit.create(NpApi.class).getcircleTj(since_id, max_id, user_id, token);
        listcall.enqueue(new Callback<GetCircleTjBean>() {
            @Override
            public void onResponse(Call<GetCircleTjBean> call, Response<GetCircleTjBean> response) {
                if (getActivity() != null) {
                    GetCircleTjBean bean = response.body();
                    if (bean != null) {
                        initNextData(bean, zhuangTai);
                    }
                }
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutTj);
                showFoot();
            }

            @Override
            public void onFailure(Call<GetCircleTjBean> call, Throwable t) {

                ToastUtils.showShort(getActivity(), "网络状况异常，请检查");
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutTj);
                showFoot();
            }
        });
    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    private void initNextData(GetCircleTjBean bean, String zhuangTai) {
        switch (bean.getStatus()) {
            case "1":
                if (bean.getInfo() != null) {
                    initStatusOne(bean, zhuangTai);
                    initToNotifiy();
                }
                break;
            case "0":
                initStatusZero(bean, zhuangTai);
                break;
            case "2":
                initStatusTwo(bean);
                break;
        }

    }

    private void initToNotifiy() {
        if (getActivity() != null) {
            NotifyLIstMessageManager.getInstance().sendLIstFlag(true, listAll);
        }
    }

    private void initBanDanTask() {
        if (flagPopularity) {
            initTaskBangDan();
            initTextColor(textViewPopularity, textViewTrain, flagPopularity, AppConfig.res + R.drawable.sentiment_pressed, AppConfig.res + R.drawable.training_normal);

        } else if (flagTrain) {
            initTaskBangDanXunLian();
            initTextColor(textViewTrain, textViewPopularity, flagTrain, AppConfig.res + R.drawable.training_pressed, AppConfig.res + R.drawable.sentiment_normal);
        }
        initTvAddData();
    }

    private void initTvAddData() {
        tv_ren.setText("人气榜");
        tv_xun_lian.setText("训练榜");
    }

    private void initStatusTwo(GetCircleTjBean bean) {
        ToastUtils.showShort(getActivity(), bean.getMsg());
    }

    private void initStatusZero(GetCircleTjBean bean, String zhuangTai) {
        if (zhuangTai.equals("jz")) {
            if (footView.getVisibility() == View.VISIBLE) {
                footView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initStatusOne(GetCircleTjBean bean, String zhuangTai) {
        List<GetCircleTjBean.InfoBean> list = new ArrayList<>();
        list = bean.getInfo();

        InitJzSxData(zhuangTai, list);


    }

    private void InitJzSxData(String zhuangTai, List<GetCircleTjBean.InfoBean> list) {
        List<GetCircleTjBean.InfoBean> listAllLinShi = new ArrayList<>();
        listAllLinShi = listAll;
        if (zhuangTai.equals("jz")) {
            listAllLinShi.addAll(list);
            listAll = listAllLinShi;
        } else if (zhuangTai.equals("sx")) {
            listAll.clear();
            list.addAll(listAllLinShi);
            listAll = list;
        }

    }

    /**
     * 适配器
     */
    private void initAdapter() {
        listAdapterCkCircleMine = new ListAdapterCkCircleRecommendMain(getActivity());
        listView.setAdapter(listAdapterCkCircleMine);


    }

    private void initToMineAttentionPeople(final FanstoplistBean bean) {
        listViewHead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toMintAttentionPeople("" + bean.getInfo().get(position).getUserId());
            }
        });
    }

    private void toMintAttentionPeople(String value) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MineAttentionPeople.class);
        intent.putExtra("focus_id", value);
        startActivity(intent);
    }

    private void initToMineAttentionPeopleXunLian(final CaltoplistBean bean) {
        listViewHead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toMintAttentionPeople("" + bean.getInfo().get(position).getUserId());
            }
        });
    }


    private void initTaskBangDan() {

        call = RetrofitUtils.retrofit.create(NpApi.class).fanstoplist();
        call.enqueue(new Callback<FanstoplistBean>() {
            @Override
            public void onResponse(Call<FanstoplistBean> call, Response<FanstoplistBean> response) {
                FanstoplistBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {
                            viewHead.setVisibility(View.VISIBLE);
                            listAdapterCkCircleRecommend = new ListAdapterCkCircleRecommend(getActivity());
                            listAdapterCkCircleRecommend.setList(bean.getInfo());
                            listViewHead.setAdapter(listAdapterCkCircleRecommend);
                            listAdapterCkCircleRecommendXunLian = null;
                            initToMineAttentionPeople(bean);
                        }
                    } else {
                        ToastUtils.showShort(getActivity(), bean.getMsg());
                    }

                }
            }

            @Override
            public void onFailure(Call<FanstoplistBean> call, Throwable t) {


            }
        });
    }

    private void initTaskBangDanXunLian() {

        listRqcall = RetrofitUtils.retrofit.create(NpApi.class).caltoplist();
        listRqcall.enqueue(new Callback<CaltoplistBean>() {
            @Override
            public void onResponse(Call<CaltoplistBean> call, Response<CaltoplistBean> response) {
                CaltoplistBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo() != null) {
                            viewHead.setVisibility(View.VISIBLE);
                            listAdapterCkCircleRecommendXunLian = new ListAdapterCkCircleRecommendXunLian(getActivity());
                            listAdapterCkCircleRecommendXunLian.setList(bean.getInfo());
                            listViewHead.setAdapter(listAdapterCkCircleRecommendXunLian);
                            listAdapterCkCircleRecommend = null;
                            initToMineAttentionPeopleXunLian(bean);
                        }
                    } else {
                        ToastUtils.showShort(getActivity(), bean.getMsg());
                    }

                }
            }

            @Override
            public void onFailure(Call<CaltoplistBean> call, Throwable t) {

            }
        });
    }

    /**
     *
     */
    private void initFalseData() {
        flagPopularity = true;
        flagTrain = false;

    }

    private void initId() {
        listView = (ListView) view.findViewById(R.id.list_CkCircleRecommend);
        listViewHead = (ListView) viewHead.findViewById(R.id.ck_circle_recommend_head_list);
        tv_ren = (TextView) viewHead.findViewById(R.id.tv_ren);
        tv_xun_lian = (TextView) viewHead.findViewById(R.id.tv_xun_lian);
        textViewPopularity = (SimpleDraweeView) viewHead.findViewById(R.id.textViewPopularity);
        textViewPopularity.setOnClickListener(this);
        sdMore = (SimpleDraweeView) viewHead.findViewById(R.id.sdMore);
        sdMore.setOnClickListener(this);

        textViewTrain = (SimpleDraweeView) viewHead.findViewById(R.id.textViewTrain);
        textViewTrain.setOnClickListener(this);
        llPopularity = (LinearLayout) viewHead.findViewById(R.id.llPopularity);
        llPopularity.setOnClickListener(this);
        llTrain = (LinearLayout) viewHead.findViewById(R.id.llTrain);
        llTrain.setOnClickListener(this);
        viewHead.setVisibility(View.INVISIBLE);
        listView.addHeaderView(viewHead);
        initFoot();
        swipeRefreshLayoutTj = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutTj);
        swipeRefreshLayoutTj.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //        swipeRefreshLayoutTj.setDistanceToTriggerSync(400);
        swipeRefreshLayoutTj.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutTj.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutTj.setOnRefreshListener(this);
        //        swipeRefreshLayoutTj.setEnabled(false);
    }

    private void initTvNull() {
        tv_xun_lian.setText("");
        tv_ren.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewPopularity:
            case R.id.llPopularity:
                initClickTvPopularity();
                initTaskBangDan();
                initTextColor(textViewPopularity, textViewTrain, flagPopularity, AppConfig.res + R.drawable.sentiment_pressed, AppConfig.res + R.drawable.training_normal);

                break;
            case R.id.sdMore:
                startActivity(new Intent().setClass(getActivity(), CirPaiHangTodayActivity.class));
                break;
            case R.id.textViewTrain:
            case R.id.llTrain:
                initClickTvTrain();
                initTaskBangDanXunLian();
                initTextColor(textViewTrain, textViewPopularity, flagTrain, AppConfig.res + R.drawable.training_pressed, AppConfig.res + R.drawable.sentiment_normal);

                break;
        }
    }

    /**
     * 进行训练榜单的点击操作
     */
    private void initClickTvTrain() {
        if (!flagTrain) {
            flagTrain = true;
            flagPopularity = false;
        }
    }

    /**
     * 进行人气榜单的点击操作
     */
    private void initClickTvPopularity() {
        if (!flagPopularity) {
            flagPopularity = true;
            flagTrain = false;
        }
    }

    /**
     * 改变选中textView的背景颜色
     *
     * @param flag
     */
    private void initTextColor(SimpleDraweeView textViewClick, SimpleDraweeView textViewUnclick, boolean flag, String id, String unid) {
        if (flag) {
            FrescoUtils.setImage(textViewClick, id);
            FrescoUtils.setImage(textViewUnclick, unid);
        }

    }


    @Override
    public void onRefresh() {
        initBanDanTask();
        initSxId();
        initTask("sx");
    }


    private void initSxId() {

        max_id = 0;
        since_id = 0;

    }


    @Override
    public void sendPlFlag(boolean flag, List<GetcommentBean.GetcommentBeanInfo> listAll, int p) {
        if (flag) {
            if (listAll != null) {
                if (listAll.size() > 0) {

                    if (listAll.size() == 1) {
                        n = 1;
                    } else if (listAll.size() >= 2) {
                        n = 2;
                    }
                    List<GetCircleTjBean.InfoBean.GetCircleCommentBean> comment = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        comment.add(new GetCircleTjBean().new InfoBean().new GetCircleCommentBean());
                        comment.get(i).setUsername(listAll.get(i).getUsername());
                        comment.get(i).setAddname(listAll.get(i).getAddname());
                        comment.get(i).setContent(listAll.get(i).getContent());
                        this.listAll.get(p).setComment(comment);
                        initToNotifiy();
                    }
                } else {
                    this.listAll.get(p).setComment(null);
                    initToNotifiy();
                }

            }
        }


    }

}
