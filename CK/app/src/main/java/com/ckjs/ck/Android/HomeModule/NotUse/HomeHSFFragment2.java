package com.ckjs.ck.Android.HomeModule.NotUse;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.HomeShopH5Activity;
import com.ckjs.ck.Android.HomeModule.Adapter.ListAdapterHomeHSF;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.MyfamilyBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeHSFFragment2 extends Fragment {
    private ListView listView;
    private View view;
    private KyLoadingBuilder builder;
    private ListAdapterHomeHSF listAdapterHomeHSF;
    private List<MyfamilyBean.MyfamilyBeanInfo.memberList> list;
    private TextView tv_zw;

    public HomeHSFFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_hsffragment2, container, false);
        initId();
        initTaskData();
        return view;
    }

    private void initId() {
        builder = new KyLoadingBuilder(getActivity());
        listView = (ListView) view.findViewById(R.id.listViewHome);
        tv_zw = (TextView) view.findViewById(R.id.tv_zw);
    }

    /**
     *
     */
    private void initTaskData() {

        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        String token = "";
        token = (String) SPUtils.get("token", token);
        Call<MyfamilyBean> call = RetrofitUtils.retrofit.create(NpApi.class).myfamily(user_id, token, "2");
        call.enqueue(new Callback<MyfamilyBean>() {
            @Override
            public void onResponse(Call<MyfamilyBean> call, Response<MyfamilyBean> response) {
                MyfamilyBean bean = response.body();
                data(bean);
                MoudleUtils.kyloadingDismiss(builder);

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
                if (list == null) return;
                if (list.size() == 0) {
                    MoudleUtils.textViewSetText(tv_zw, bean.getMsg());
                    MoudleUtils.viewShow(tv_zw);
                } else {
                    MoudleUtils.textViewSetText(tv_zw, "");
                    MoudleUtils.viewGone(tv_zw);
                }
                if (listAdapterHomeHSF == null) {
                    listAdapterHomeHSF = new ListAdapterHomeHSF(getActivity());
                    listAdapterHomeHSF.setType("2");
                    listAdapterHomeHSF.setList(list);
                    listView.setAdapter(listAdapterHomeHSF);
                } else {
                    listAdapterHomeHSF.setType("2");
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
                intentH5.putExtra("acurl", list.get(position).getUrl() + "&user_id=" + user_id + "&token=" + token);
                intentH5.setClass(getActivity(), HomeShopH5Activity.class);
                startActivity(intentH5);
            }
        });
    }

}
