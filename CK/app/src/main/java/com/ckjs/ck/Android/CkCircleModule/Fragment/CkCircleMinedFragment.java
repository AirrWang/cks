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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.CkCircleModule.Activity.CircleRecommenMoreActivity;
import com.ckjs.ck.Android.CkCircleModule.Adapter.ListAdapterCkCircleMineNew;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.GetCircleMineBean;
import com.ckjs.ck.Bean.GetcommentBean;
import com.ckjs.ck.Manager.NotifyCircleRefrushMineMessageManager;
import com.ckjs.ck.Manager.NotifyCkMineListClreaMessageManager;
import com.ckjs.ck.Manager.NotifyListMessageMineManager;
import com.ckjs.ck.Manager.NotifyMessageLoginCkMineManager;
import com.ckjs.ck.Manager.NotifyPlManager;
import com.ckjs.ck.Message.NotifyCircleRefrushMineMessage;
import com.ckjs.ck.Message.NotifyCkMineListClreaMessage;
import com.ckjs.ck.Message.NotifyLoginCkMineMessage;
import com.ckjs.ck.Message.NotifyPlMineMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CkCircleMinedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NotifyCircleRefrushMineMessage,  NotifyLoginCkMineMessage, NotifyCkMineListClreaMessage, View.OnClickListener, NotifyPlMineMessage {

    private View view;
    private ListView listView;
    private ListAdapterCkCircleMineNew listAdapterCkCircleMine;//帖子的适配器
    private int since_id, max_id;
    private List<GetCircleMineBean.InfoBean> listAll = new ArrayList<>();
    private View footView;
    private boolean flag;
    private SwipeRefreshLayout swipeRefreshLayoutTj;
    private Button btn_to_login;
    private TextView tv_zw;
    private Call<GetCircleMineBean> call;
    private int n;


    public CkCircleMinedFragment() {
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
        view = inflater.inflate(R.layout.fragment_ck_circle_mine, container, false);
        footView = inflater.inflate(R.layout.next_foot, null);
        initId();
        init();
        initData();
        return view;
    }

    private void init() {
        NotifyMessageLoginCkMineManager.getInstance().setNotifyMessage(this);
        NotifyCircleRefrushMineMessageManager.getInstance().setNotifyMessage(this);
        NotifyCkMineListClreaMessageManager.getInstance().setNotifyMessage(this);
        NotifyPlManager.getInstance().setNotifyMessage(this);

    }


    private void initData() {
        String token = "";
        token = (String) SPUtils.get(getActivity(), "token", "");
        initGone();
        if (token.equals("")) {
            MoudleUtils.initF(swipeRefreshLayoutTj);
            initBtnLgoinShow();
        } else {
            MoudleUtils.initT(swipeRefreshLayoutTj);
            MoudleUtils.viewGone(btn_to_login);
            initLoginedData();
        }
    }

    private void initBtnLgoinShow() {
        MoudleUtils.viewShow(btn_to_login);
    }

    private void initLoginedData() {
        initdata();
        initSet();
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
                Intent intent = new Intent();
                intent.setClass(getActivity(), CircleRecommenMoreActivity.class);
                intent.putExtra("circle_id", listAll.get(position).getId());
                intent.putExtra("user_id", listAll.get(position).getUser_id());
                intent.putExtra("name", "mine");
                intent.putExtra("p", position);
                if (listAll.get(position).getNewcom() != null) {
                    if (listAll.get(position).getNewcom().equals("1")) {
                        listAll.get(position).setNewcom("0");
                        initToNotyfiy();
                    }
                }
                startActivity(intent);
            }
        });
    }


    private void initJz() {

        since_id = 0;
        if (listAll != null) {
            if (listAll.size() > 0) {
                max_id = listAll.get(listAll.size() - 1).getId();
                initTask("jz");
            }
        }


    }

    private void initFoot() {
        listView.addFooterView(footView);
    }

    private void initdata() {

        initAdapter();

        MoudleUtils.initRefrushTrue(swipeRefreshLayoutTj);
        onRefresh();


    }


    private void initTask(final String zhuangTai) {

        int user_id = 0;
        user_id = (int) SPUtils.get(getActivity(), "user_id", user_id);
        String token = "";
        token = (String) SPUtils.get(getActivity(), "token", token);
        call = RetrofitUtils.retrofit.create(NpApi.class).mycircle(user_id, token, since_id, max_id);
        call.enqueue(new Callback<GetCircleMineBean>() {
            @Override
            public void onResponse(Call<GetCircleMineBean> call, Response<GetCircleMineBean> response) {
                if (getActivity() != null) {
                    GetCircleMineBean bean = response.body();
                    if (bean != null) {
                        initNextData(bean, zhuangTai);
                    }
                }
                MoudleUtils.initSwipeRefreshLayoutFalse(swipeRefreshLayoutTj);
                showFoot();
                initToZw();
            }

            @Override
            public void onFailure(Call<GetCircleMineBean> call, Throwable t) {
//                LogUtils.i(TAG, "getMessage: " + t.getMessage());
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

    private void initNextData(GetCircleMineBean bean, String zhuangTai) {
        switch (bean.getStatus()) {
            case "1":
                if (bean.getInfo() != null) {
                    initStatusOne(bean, zhuangTai);
                        initToNotyfiy();
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

    private void initStatusTwo(GetCircleMineBean bean) {

        MoudleUtils.initStatusTwo(getActivity(), false);

    }

    private void initStatusZero(GetCircleMineBean bean, String zhuangTai) {
        if (zhuangTai.equals("jz")) {
            if (footView.getVisibility() == View.VISIBLE) {
                footView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initStatusOne(GetCircleMineBean bean, String zhuangTai) {
        List<GetCircleMineBean.InfoBean> list = new ArrayList<>();
        list = bean.getInfo();
//        LogUtils.i(TAG, "bean: " + list.size());
        InitJzSxData(zhuangTai, list);
        initGone();


    }

    private void InitJzSxData(String zhuangTai, List<GetCircleMineBean.InfoBean> list) {
        List<GetCircleMineBean.InfoBean> listAllLinShi = new ArrayList<>();
        listAllLinShi = listAll;
        if (zhuangTai.equals("jz")) {
            listAllLinShi.addAll(list);
            listAll = listAllLinShi;
        } else if (zhuangTai.equals("sx")) {
            listAll.clear();
            list.addAll(listAllLinShi);
            listAll = list;
        } else if (zhuangTai.equals("mysx")) {
            listAll.clear();
            list.addAll(listAllLinShi);
            listAll = list;
            listAdapterCkCircleMine = null;
            initAdapter();
        }
    }

    /**
     * 适配器
     */
    private void initAdapter() {
        if (getActivity() != null) {
            listAdapterCkCircleMine = new ListAdapterCkCircleMineNew(getActivity());
            listView.setAdapter(listAdapterCkCircleMine);
        }
    }


    private void initId() {
        listView = (ListView) view.findViewById(R.id.list_ck_circle_mine);
        btn_to_login = (Button) view.findViewById(R.id.btn_to_login);
        btn_to_login.setOnClickListener(this);
        tv_zw = (TextView) view.findViewById(R.id.tv_zw);
        initFoot();
        swipeRefreshLayoutTj = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutTj);
        swipeRefreshLayoutTj.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        swipeRefreshLayoutTj.setDistanceToTriggerSync(400);
        swipeRefreshLayoutTj.setBackgroundColor(Color.TRANSPARENT);
        swipeRefreshLayoutTj.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayoutTj.setOnRefreshListener(this);

    }


    @Override
    public void onRefresh() {
        initSxId();
        initTask("sx");
    }


    private void initSxId() {

        max_id = 0;
        since_id = 0;

    }



    @Override
    public void sendMessageCircleRefrushFlag(boolean flag) {
        if (flag) {
            initGone();
            MoudleUtils.initRefrushTrue(swipeRefreshLayoutTj);
            if (getActivity() != null) {
                initSxId();
                initTask("mysx");
            }
        }
    }


    @Override
    public void sendMessageCircleMineLogin(boolean flagRefresh) {
        if (flagRefresh) {
            MoudleUtils.initT(swipeRefreshLayoutTj);
            initGone();
            initLoginedData();
        }
    }

    private void initGone() {
        if (tv_zw != null) {
            if (tv_zw.getVisibility() == View.VISIBLE) {
                tv_zw.setVisibility(View.GONE);
            }
        }
        if (btn_to_login != null) {
            if (btn_to_login.getVisibility() == View.VISIBLE) {
                btn_to_login.setVisibility(View.GONE);
            }
        }
    }

    private void initToZw() {
        if (listAll.size() == 0) {
            if (tv_zw != null) {
                if (tv_zw.getVisibility() != View.VISIBLE) {
                    tv_zw.setVisibility(View.VISIBLE);
                }
            }
        } else if (listAll.size() > 0) {
            if (tv_zw != null) {
                if (tv_zw.getVisibility() == View.VISIBLE) {
                    tv_zw.setVisibility(View.GONE);
                }
            }
        }
    }
    private void initToNotyfiy() {
        if (getActivity() != null) {
            NotifyListMessageMineManager.getInstance().sendLIstFlag(true, listAll);
        }
    }

    @Override
    public void sendMessageCkMineListClreaFlag(boolean flag) {
        if (flag) {
            initGone();
            MoudleUtils.initRefrushTrue(swipeRefreshLayoutTj);
            onRefresh();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_login:
                MoudleUtils.initToLogin(getActivity());
                break;
        }
    }


    @Override
    public void sendPlMineFlag(boolean flag, List<GetcommentBean.GetcommentBeanInfo> listAll, int p) {
        if (flag) {
            if (listAll != null) {
                if (listAll.size() > 0) {
                    if (listAll.size() == 1) {
                        n = 1;
                    } else if (listAll.size() >= 2) {
                        n = 2;
                    }
                    List<GetCircleMineBean.InfoBean.GetCircleCommentBean> comment = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        comment.add(new GetCircleMineBean().new InfoBean().new GetCircleCommentBean());
                        comment.get(i).setUsername(listAll.get(i).getUsername());
                        comment.get(i).setAddname(listAll.get(i).getAddname());
                        comment.get(i).setContent(listAll.get(i).getContent());
                        this.listAll.get(p).setComment(comment);
                        initToNotyfiy();
                    }
                } else {
                    this.listAll.get(p).setComment(null);
                    initToNotyfiy();
                }

            }
        }
    }
}
