package com.ckjs.ck.Android.HealthModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GridAdapterHealthMore extends BaseAdapter {
    private List<String> list;
    private Context context;

    public GridAdapterHealthMore(Context context) {
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
                    R.layout.item_grid_health_more, null);
            myHodler = new MyHodler();
            myHodler.iv_grid_circle_mine = (SimpleDraweeView) view
                    .findViewById(R.id.iv_grid_circle_mine);

            view.setTag(myHodler);
        } else {
            myHodler = (MyHodler) view.getTag();

        }
        FrescoUtils.setImage(myHodler.iv_grid_circle_mine, AppConfig.url_jszd + list.get(position));

        return view;
    }


    public void setList(List<String> list) {
        this.list = list;
    }


    class MyHodler {
        SimpleDraweeView iv_grid_circle_mine;
    }
}
