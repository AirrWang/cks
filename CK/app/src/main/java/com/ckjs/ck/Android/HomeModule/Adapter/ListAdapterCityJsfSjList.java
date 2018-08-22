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
import com.ckjs.ck.Bean.CoachBean;
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
public class ListAdapterCityJsfSjList extends BaseAdapter {


    private List<CoachBean.CoachBeanInfo> list;
    private Context context;

    public ListAdapterCityJsfSjList(Context context) {
        super();
        this.context = context;
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
                    R.layout.item_list_city_jsf_sj_list, null);
            myHolder = new MyHolder(view);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }

        initData(position, myHolder);
        return view;
    }

    private void initData(final int position, MyHolder myHolder) {
        FrescoUtils.setImage(myHolder.sd_icon, AppConfig.url_jszd + list.get(position).getPicture());
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

        MoudleUtils.textViewSetText(myHolder.tv_name, list.get(position).getGymname());
        MoudleUtils.textViewSetText(myHolder.tv_my_name, list.get(position).getName());
        MoudleUtils.textViewSetText(myHolder.tv_dkl, "带课量：" + list.get(position).getNum());
        String xb = list.get(position).getSex();//1男2女
        if (xb != null) {
            if (xb.equals("1")) {
                FrescoUtils.setImage(myHolder.sd_xb, AppConfig.res + R.drawable.my_boy);
            } else if (xb.equals("2")) {
                FrescoUtils.setImage(myHolder.sd_xb, AppConfig.res + R.drawable.my_girl);
            }
        }
        String status = "";
        status = list.get(position).getStatus();
        if (status == null) {
            status = "";
        }
        if (status.equals("0")) {
            myHolder.r_jsf.setBackgroundResource(R.color.c_e6);
            myHolder.tv_is_gz.setTextColor(context.getResources().getColor(R.color.c_33));
            MoudleUtils.textViewSetText(myHolder.tv_is_gz, "休息");
        } else if (status.equals("1")) {
            myHolder.tv_is_gz.setTextColor(context.getResources().getColor(R.color.fea21249));
            myHolder.r_jsf.setBackgroundResource(R.color.c_ffffff);
            MoudleUtils.textViewSetText(myHolder.tv_is_gz, "工作");
        }

    }


    public void setList(List<CoachBean.CoachBeanInfo> list) {
        this.list = list;
    }


    class MyHolder {
        @BindView(R.id.textView19)
        TextView tv_name;
        @BindView(R.id.textView38)
        TextView tv_dkl;
        @BindView(R.id.textView40)
        TextView tv_my_name;
        @BindView(R.id.textView21)
        LinearLayout linearLayout_xing;
        @BindView(R.id.sd_icon)
        SimpleDraweeView sd_icon;
        @BindView(R.id.sd_xb)
        SimpleDraweeView sd_xb;
        @BindView(R.id.r_jsf)
        RelativeLayout r_jsf;
        @BindView(R.id.tv_is_gz)
        TextView tv_is_gz;

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
