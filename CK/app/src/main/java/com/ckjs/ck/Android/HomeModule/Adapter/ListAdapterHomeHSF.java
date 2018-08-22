package com.ckjs.ck.Android.HomeModule.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.MyfamilyBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterHomeHSF extends BaseAdapter {

    List<MyfamilyBean.MyfamilyBeanInfo.memberList> list;
    private Context context;
    private ProgressDialog dialog;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public ListAdapterHomeHSF(Context context) {
        super();
        this.context = context;
        dialog = new ProgressDialog(context);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        MyHolder myHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_list_home_hsf, null);
            myHolder = new MyHolder();


            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        initData(view, myHolder, position);
        return view;
    }

    private void initData(View v, MyHolder myHolder, final int p) {
        initId(v, myHolder);
        MoudleUtils.textViewSetText(myHolder.tv_name, list.get(p).getUsername());
        MoudleUtils.textViewSetText(myHolder.tv_time, list.get(p).getCreatetime());
        FrescoUtils.setImage(myHolder.ivHead, AppConfig.url + list.get(p).getPicurl());
        if (p % 3 == 1) {
            FrescoUtils.setImage(myHolder.ivBg, AppConfig.res + R.drawable.rectangle_yellow);
        } else if (p % 3 == 2) {
            FrescoUtils.setImage(myHolder.ivBg, AppConfig.res + R.drawable.rectangle_green);
        } else if (p % 3 == 0) {
            FrescoUtils.setImage(myHolder.ivBg, AppConfig.res + R.drawable.rectangle_pink);
        }
        myHolder.iv_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "";
                if (type.equals("1")) {
                    s = "您即将删除关心的人，确认后无法恢复";
                } else {
                    s = "您即将删除关心自己的人，确认后无法查看对方运动健康报告";
                }
                AlertDialog alertDialog = null;
                if (alertDialog == null) {
                    alertDialog = new AlertDialog.Builder(context)
                            .setMessage(s)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    initTask(p);
                                }

                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create(); // 创建对话框

                }
                if (alertDialog != null) {
                    alertDialog.setCancelable(false);
                    if (!alertDialog.isShowing()) {
                        alertDialog.show(); // 显示
                    }
                }
            }
        });
    }

    private void initTask(final int p) {

        MoudleUtils.dialogShow(dialog);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<AcceptBean> callBack = restApi.memberdel(token, user_id, list.get(p).getMember_id(), type);

        callBack.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        //成功操作
                        list.remove(p);
                        notifyDataSetChanged();
                        ToastUtils.showShort(context,response.body().getMsg());
                    } else {
                        ToastUtils.showShort(context,response.body().getMsg());
                    }
                }

                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dialog);

            }
        });
    }

    private void initId(View v, MyHolder myHolder) {
        myHolder.ivHead = (SimpleDraweeView) v.findViewById(R.id.ivHead);
        myHolder.ivBg = (SimpleDraweeView) v.findViewById(R.id.ivBg);
        myHolder.iv_cha = (SimpleDraweeView) v.findViewById(R.id.iv_cha);
        myHolder.tv_time = (TextView) v.findViewById(R.id.tv_time);
        myHolder.tv_name = (TextView) v.findViewById(R.id.tv_name);
    }


    public void setList(List<MyfamilyBean.MyfamilyBeanInfo.memberList> list) {
        this.list = list;
    }


    class MyHolder {
        private SimpleDraweeView ivHead;
        private SimpleDraweeView ivBg;
        private SimpleDraweeView iv_cha;
        private TextView tv_time;
        private TextView tv_name;
    }


}
