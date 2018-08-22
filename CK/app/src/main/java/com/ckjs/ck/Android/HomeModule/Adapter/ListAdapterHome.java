package com.ckjs.ck.Android.HomeModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ckjs.ck.R;
import com.ckjs.ck.Bean.ActivityBean;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterHome extends BaseAdapter {

    private List<ActivityBean.ActivityBeanInfo> list;
    private Context context;

    public ListAdapterHome(Context context) {
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
                    R.layout.item_list_home, null);
            myHolder = new MyHolder();

            myHolder.simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.ivSm);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        FrescoUtils.setImage(myHolder.simpleDraweeView, AppConfig.url_jszd + list.get(position).getActcover());

        return view;
    }


    public void setList(List<ActivityBean.ActivityBeanInfo> list) {
        this.list = list;
    }


    class MyHolder {

        SimpleDraweeView simpleDraweeView;
    }


}
