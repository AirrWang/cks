package com.ckjs.ck.Android.HomeModule.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.CitygymBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterCityJsfList extends BaseAdapter {

    private String type;

    public List<CitygymBean.CitygymBeanInfo> getList() {
        return list;
    }


    private List<CitygymBean.CitygymBeanInfo> list;
    private Context context;

    //type 1超空门店 2超空私教 使用
    public ListAdapterCityJsfList(Context context, String type) {
        super();
        this.context = context;
        this.type = type;
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
                    R.layout.item_list_city_jsf_list, null);
            myHolder = new MyHolder(view);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }

        initData(position, myHolder);
        return view;
    }

    private void initData(final int position, MyHolder myHolder) {
        if (list.get(position).getGrade().equals("0")) {
            myHolder.linearLayout_xing.removeAllViews();
        } else if (Integer.parseInt(list.get(position).getGrade()) >= 1) {
            myHolder.linearLayout_xing.removeAllViews();
            for (int i = 0; i < Integer.parseInt(list.get(position).getGrade()); i++) {
                SimpleDraweeView ivPoint = new SimpleDraweeView(context);
                int w = CkApplication.getInstance().getResources().getDimensionPixelOffset(R.dimen.city_jsf_xing);
                int w2 = CkApplication.getInstance().getResources().getDimensionPixelOffset(R.dimen.city_jsf_xing_m);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w, w);
                layoutParams.setMargins(0, 0, w2, w2);
                ivPoint.setLayoutParams(layoutParams);
                FrescoUtils.setImage(ivPoint, AppConfig.res + R.drawable.list_attention);
                myHolder.linearLayout_xing.addView(ivPoint);
            }
        }

        MoudleUtils.textViewSetText(myHolder.tv_name, list.get(position).getName());
        MoudleUtils.textViewSetText(myHolder.tv_jl, list.get(position).getDistance());
        String yy = list.get(position).getOpen();
        if (yy != null) {
            switch (yy) {
                case "0":
                    if (type.equals("2")) {//机构搜索下没有私教的超空门店变灰并且无法点击
                        myHolder.rl_item_list_city_jsf_list.setBackgroundResource(R.color.c_e6);
                    } else {
                        myHolder.rl_item_list_city_jsf_list.setBackgroundResource(R.color.c_ffffff);
                    }
                    MoudleUtils.textViewSetText(myHolder.tv_yy, "敬请期待");
                    break;
                case "1":
                    myHolder.rl_item_list_city_jsf_list.setBackgroundResource(R.color.c_ffffff);
                    MoudleUtils.textViewSetText(myHolder.tv_yy, "正在营业");
                    break;
            }
        }
        MoudleUtils.textViewSetText(myHolder.tv_gzd, "关注度：" + list.get(position).getNum());
        MoudleUtils.textViewSetText(myHolder.tv_adr, "地址：" + list.get(position).getPlace());
        MoudleUtils.textViewSetText(myHolder.tv_jl, list.get(position).getDistance());

    }


    public void setList(List<CitygymBean.CitygymBeanInfo> list) {
        this.list = list;
    }


    class MyHolder {
        @BindView(R.id.textView19)
        TextView tv_name;
        @BindView(R.id.textView21)
        LinearLayout linearLayout_xing;
        @BindView(R.id.textView41)
        TextView tv_yy;
        @BindView(R.id.textView38)
        TextView tv_gzd;
        @BindView(R.id.textView39)
        TextView tv_adr;
        @BindView(R.id.textView42)
        TextView tv_jl;
        @BindView(R.id.sd_to_jsf)
        SimpleDraweeView sd_to_jsf;
        @BindView(R.id.rl_item_list_city_jsf_list)
        RelativeLayout rl_item_list_city_jsf_list;

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
