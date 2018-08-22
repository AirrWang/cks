package com.ckjs.ck.Android.HomeModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.ActivitylistBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterHomeAcList extends BaseAdapter {

    private List<ActivitylistBean.ActivitylistBeanInfo> list;
    private Context context;

    public ListAdapterHomeAcList(Context context) {
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
                    R.layout.item_home_ac_list, null);
            myHolder = new MyHolder();

            myHolder.simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.ivSm);
            myHolder.sdRen = (SimpleDraweeView) view.findViewById(R.id.sdRen);
            myHolder.name = (TextView) view.findViewById(R.id.textViewName);
            myHolder.zt = (TextView) view.findViewById(R.id.textViewZt);
            myHolder.time = (TextView) view.findViewById(R.id.textViewTime);
            myHolder.num = (TextView) view.findViewById(R.id.textViewNum);
//            myHolder.lx = (TextView) view.findViewById(R.id.textViewLx);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        FrescoUtils.setImage(myHolder.simpleDraweeView, AppConfig.url_jszd + list.get(position).getActcover());
        MoudleUtils.textViewSetText(myHolder.name, list.get(position).getName());
        MoudleUtils.textViewSetText(myHolder.zt, list.get(position).getIsstart());
        MoudleUtils.textViewSetText(myHolder.time, list.get(position).getTime());
        if (list.get(position).getNum() .equals( "0")) {
            myHolder.num.setVisibility(View.INVISIBLE);
            myHolder.sdRen.setVisibility(View.INVISIBLE);
        } else {
            myHolder.sdRen.setVisibility(View.VISIBLE);
            MoudleUtils.textViewSetText(myHolder.num, list.get(position).getBmnum() + "/" + list.get(position).getNum() + "人");
            myHolder.num.setVisibility(View.VISIBLE);
        }
        return view;
    }


    public void setList(List<ActivitylistBean.ActivitylistBeanInfo> list) {
        this.list = list;
    }


    class MyHolder {
        SimpleDraweeView simpleDraweeView;
        SimpleDraweeView sdRen;
        TextView name;
        TextView zt;
        TextView time;
        TextView num;
//        TextView lx;
    }


}
