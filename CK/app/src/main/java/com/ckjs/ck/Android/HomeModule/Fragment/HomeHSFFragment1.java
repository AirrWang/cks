package com.ckjs.ck.Android.HomeModule.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ckjs.ck.Android.HomeModule.Activity.HomeShopH5Activity;
import com.ckjs.ck.Android.HomeModule.Activity.SearchFriendsActivity;
import com.ckjs.ck.Android.HomeModule.Adapter.ListAdapterHomeHSF;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.MyfamilyBean;
import com.ckjs.ck.Manager.NotifyActivityAddFamilyManager;
import com.ckjs.ck.Message.NotifyActicityAddFamilyMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.MyListView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeHSFFragment1 extends Fragment implements NotifyActicityAddFamilyMessage {
    private MyListView listView;
    private View view;
    private KyLoadingBuilder builder;
    private ListAdapterHomeHSF listAdapterHomeHSF;
    private List<MyfamilyBean.MyfamilyBeanInfo.memberList> list;
    private SimpleDraweeView ivAdd;

    public HomeHSFFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_hsffragment1, container, false);
        initId();
        initTaskData();
        return view;
    }

    private void initId() {
        NotifyActivityAddFamilyManager.getInstance().setNotifyMessage(this);
        builder = new KyLoadingBuilder(getActivity());
        listView = (MyListView) view.findViewById(R.id.listViewHome);
        ivAdd = (SimpleDraweeView) view.findViewById(R.id.ivAdd);
    }

    /**
     *
     */
    private void initTaskData() {
        MoudleUtils.kyloadingShow(builder);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        String token = "";
        token = (String) SPUtils.get("token", token);
        Call<MyfamilyBean> call = RetrofitUtils.retrofit.create(NpApi.class).myfamily(user_id, token, "1");
        call.enqueue(new Callback<MyfamilyBean>() {
            @Override
            public void onResponse(Call<MyfamilyBean> call, Response<MyfamilyBean> response) {
                try {
                    MyfamilyBean bean = response.body();
                    data(bean);
                    MoudleUtils.kyloadingDismiss(builder);
                    ivAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent().setClass(getActivity(), SearchFriendsActivity.class));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MyfamilyBean> call, Throwable t) {

                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(builder);

            }
        });

    }

    private void data(MyfamilyBean bean) {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                list = bean.getInfo().getMemberlist();
                if (list==null)return;
                if (listAdapterHomeHSF == null) {
                    listAdapterHomeHSF = new ListAdapterHomeHSF(getActivity());
                    listAdapterHomeHSF.setType("1");
                    listAdapterHomeHSF.setList(list);
                    listView.setAdapter(listAdapterHomeHSF);
                } else {
                    listAdapterHomeHSF.setType("1");
                    listAdapterHomeHSF.setList(list);
                    listAdapterHomeHSF.notifyDataSetChanged();
                }
                initToHsfBaoGao();
            } else {
                ToastUtils.showShort(getActivity(),bean.getMsg());
            }
        }
    }

    /**
     * 跳转健康报告
     */
    private void initToHsfBaoGao() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list == null || list.size() == 0) return;
                if (list.get(position).getUrl() == null || list.get(position).getUrl().equals(""))
                    return;
                int user_id = 0;
                user_id = (int) SPUtils.get("user_id", user_id);
                String token = "";
                token = (String) SPUtils.get("token", token);
                Intent intentH5 = new Intent();
                intentH5.putExtra("acurl", list.get(position).getUrl()+ "&user_id=" + user_id + "&token=" + token);
                intentH5.setClass(getActivity(), HomeShopH5Activity.class);
                startActivity(intentH5);
            }
        });
    }

    @Override
    public void sendNotifyActicityAddFamilyMessageFlag(boolean flag) {
        if (flag) {
            initTaskData();
        }
    }
}
