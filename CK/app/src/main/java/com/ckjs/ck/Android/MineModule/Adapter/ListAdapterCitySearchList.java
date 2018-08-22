package com.ckjs.ck.Android.MineModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.CitySearchBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterCitySearchList extends BaseAdapter {


    private List<CitySearchBean.CitySearchInfoBean> list;
    private Context context;

    public ListAdapterCitySearchList(Context context) {
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
                    R.layout.item_list_city_jsf_search, null);
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
        MoudleUtils.textViewSetText(myHolder.tv_gzs, list.get(position).getNum());
        MoudleUtils.textViewSetText(myHolder.tv_add, "地址： "+list.get(position).getPlace());
    }


    public void setList(List<CitySearchBean.CitySearchInfoBean> list) {
        this.list = list;
    }


    class MyHolder {
        @BindView(R.id.tv_8)
        TextView tv_name;
        @BindView(R.id.tv_gzs)
        TextView tv_gzs;
        @BindView(R.id.ll_7)
        LinearLayout linearLayout_xing;
        @BindView(R.id.tv_add)
        TextView tv_add;

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
