package com.ckjs.ck.Android.HomeModule.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GridAdapterChooseFamily extends BaseAdapter {
    private List<String> list;
    private Context context;

    public int getI_xz() {
        return i_xz;
    }

    public void setI_xz(int i_xz) {
        this.i_xz = i_xz;
    }

    private int i_xz = -1;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    public GridAdapterChooseFamily(Context context) {
        super();
        this.context = context;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View v, ViewGroup arg2) {
        // TODO Auto-generated method stub
        View view = v;
        MyHodler myHodler = null;
        if (view == null) {

            view = LayoutInflater.from(context).inflate(
                    R.layout.item_grid_choose_relationship, null);
            myHodler = new MyHodler();
            myHodler.tv_item_relationship = (TextView) view
                    .findViewById(R.id.tv_item_relationship);
            view.setTag(myHodler);
        } else {
            myHodler = (MyHodler) view.getTag();
        }

        if (position == i_xz) {
            myHodler.tv_item_relationship.setBackgroundColor(context.getResources().getColor(R.color.c07ac83));
            myHodler.tv_item_relationship.setTextColor(context.getResources().getColor(R.color.c_ffffff));
        } else {
            myHodler.tv_item_relationship.setBackgroundResource(R.drawable.text_retangle_biankuang_lv);
            myHodler.tv_item_relationship.setTextColor(context.getResources().getColor(R.color.c07ac83));
        }
        MoudleUtils.textViewSetText(myHodler.tv_item_relationship, list.get(position));
        return view;
    }

    public void setList(List<String> list) {
        this.list = list;
    }


    class MyHodler {
        TextView tv_item_relationship;
    }
}
