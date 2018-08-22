package com.ckjs.ck.Android.MineModule.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ckjs.ck.Android.MineModule.Activity.MineMessageInfoActivity;
import com.ckjs.ck.Android.MineModule.Activity.MinePrivateLetterInfoActivity;
import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMineMessage;
import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMinePrivateLetter;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.BindacceptBean;
import com.ckjs.ck.Bean.GetMyfocusCircleBean;
import com.ckjs.ck.Bean.LetterListBean;
import com.ckjs.ck.Bean.MessageListBean;
import com.ckjs.ck.Bean.ReceiptBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
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
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MineMessageFragment extends android.support.v4.app.Fragment {


    private View view;
    private View footView;
    private ListView mListView;
    private boolean flag;
    private int page = 1;
    private List<MessageListBean.MessageListDetailBean> listAll = new ArrayList<>();
    private KyLoadingBuilder builder;
    private ProgressDialog dialog;

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
        view = inflater.inflate(R.layout.activity_mine_message, container, false);
        builder = new KyLoadingBuilder(getActivity());
        footView = LayoutInflater.from(getActivity()).inflate(
                R.layout.next_foot, null);
        initId();
        initTask();

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    /**
     * 获取列表接口
     */
    private void initTask() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(getActivity(), "user_id", 0));
        String token = (String) SPUtils.get(getActivity(), "token", "");
        Call<MessageListBean> callBack = restApi.messagelist(user_id + "", token, page);

        callBack.enqueue(new Callback<MessageListBean>() {
            @Override
            public void onResponse(Call<MessageListBean> call, Response<MessageListBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {
                            if (page == 1) {
                                listAll.clear();
                            }
                            listAll.addAll(response.body().getInfo());
                            initAdapter();
                            page++;
                            initSet();
                            initSetTo();

                        }

                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(getActivity(), response.body().getMsg());
                    }
                }
                showFoot();
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<MessageListBean> call, Throwable t) {
                MoudleUtils.toChekWifi(getActivity());
                MoudleUtils.kyloadingDismiss(builder);
                showFoot();
            }
        });
    }

    /**
     * 条目点击事件
     */
    private void initSetTo() {


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), MineMessageInfoActivity.class);
                intent.putExtra("title", listAll.get(position).getUsername());
                intent.putExtra("message_id", listAll.get(position).getMessage_id());
                intent.putExtra("add_id", listAll.get(position).getAdd_id());
                if (listAll.get(position).getNoread() != null) {
                    if (!listAll.get(position).getNoread().equals("0")) {
                        listAll.get(position).setNoread("0");
                        listAdapterMineMessage.notifyDataSetChanged();
                    }
                }
                startActivity(intent);

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                initDeletePrivateLetter(position);
                return true;
            }
        });

    }

    /**
     * 长按谈对话框删除
     *
     * @param position
     * @return
     */
    private void initDeletePrivateLetter(final int position) {
        final CharSequence[] items = {"删除对话", "再想想"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        initDelete(position);
                        break;
                    default:
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 删除接口
     */
    private void initDelete(final int position) {
        dialog = new ProgressDialog(getActivity());
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(getActivity(), "user_id", 0));
        String token = (String) SPUtils.get(getActivity(), "token", "");

        Call<BindacceptBean> callBack = restApi.delmessage(user_id + "", token, listAll.get(position).getMessage_id());
        callBack.enqueue(new Callback<BindacceptBean>() {
            @Override
            public void onResponse(Call<BindacceptBean> call, Response<BindacceptBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        listAll.remove(position);
                        listAdapterMineMessage.notifyDataSetChanged();
                        ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(getActivity(), response.body().getMsg(), 0);
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<BindacceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi(getActivity());
                MoudleUtils.dialogDismiss(dialog);
            }
        });

    }

    /**
     * listView绑定数据
     */
    private void initAdapter() {
        if (listAdapterMineMessage == null) {
            listAdapterMineMessage = new ListAdapterMineMessage(getActivity());
            listAdapterMineMessage.setDataSource(listAll);
            mListView.setAdapter(listAdapterMineMessage);
        } else {
            listAdapterMineMessage.setDataSource(listAll);
            listAdapterMineMessage.notifyDataSetChanged();
        }
    }

    private ListAdapterMineMessage listAdapterMineMessage;


    private void initId() {

        mListView = (ListView) view.findViewById(R.id.list_mine_message);
        mListView.addFooterView(footView);
    }

    /**
     * 上拉加载
     */
    private void initSet() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (mListView.getLastVisiblePosition() == (mListView.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                if (page > 1) {
                                    initTask();
                                }
                            }

                        }
                        //判断滚动到顶部
                        if (mListView.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                flag = firstVisibleItem + visibleItemCount == totalItemCount && !flag;
            }
        });
    }

    /**
     * 隐藏脚布局
     */
    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }
}
