package com.ckjs.ck.Android.CoachModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.OrderBean;
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


public class ListAdapterHisList extends BaseAdapter {


    private List<OrderBean.OrderBeanInfo> list;
    private Context context;

    public ListAdapterHisList(Context context) {
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
                    R.layout.item_list_his_list, null);
            myHolder = new MyHolder(view);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }

        FrescoUtils.setImage(myHolder.custom_icon, AppConfig.url + list.get(position).getPicurl());
        MoudleUtils.textViewSetText(myHolder.tv_di_zhi, list.get(position).getAdress());
        MoudleUtils.textViewSetText(myHolder.tv_name, list.get(position).getRelname());
        MoudleUtils.textViewSetText(myHolder.tv_shou_yi, "￥"+list.get(position).getAmount());
        MoudleUtils.textViewSetText(myHolder.tv_timme, list.get(position).getCreatetime());

        String sex = list.get(position).getSex();
        if (sex != null) {
            if (sex.equals("1")) {
                FrescoUtils.setImage(myHolder.custom_sex, AppConfig.res + R.drawable.my_boy);
            } else if (sex.equals("2")) {
                FrescoUtils.setImage(myHolder.custom_sex, AppConfig.res + R.drawable.my_girl);
            }
        }
        String tv_type = list.get(position).getType();
        if (tv_type != null) {
            if (tv_type.equals("distance")) {
                MoudleUtils.textViewSetText(myHolder.tv_type, "视频指导");
            } else if (tv_type.equals("personal")) {
                MoudleUtils.textViewSetText(myHolder.tv_type, "上门指导");
            }
        }

        return view;
    }


    public void setList(List<OrderBean.OrderBeanInfo> list) {
        this.list = list;
    }


    class MyHolder {
        @BindView(R.id.custom_icon)
        SimpleDraweeView custom_icon;
        @BindView(R.id.tv_di_zhi)
        TextView tv_di_zhi;
        @BindView(R.id.custom_sex)
        SimpleDraweeView custom_sex;
        @BindView(R.id.tv_type)
        TextView tv_type;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_shou_yi)
        TextView tv_shou_yi;
        @BindView(R.id.tv_timme)
        TextView tv_timme;

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
