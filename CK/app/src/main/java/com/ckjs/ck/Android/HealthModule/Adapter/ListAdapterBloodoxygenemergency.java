package com.ckjs.ck.Android.HealthModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.HealWarningsBean;
import com.ckjs.ck.R;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterBloodoxygenemergency extends BaseAdapter {

    private List<HealWarningsBean.HealWarningsInfoBean> list;
    private Context context;

    public ListAdapterBloodoxygenemergency(Context context) {
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
                    R.layout.item_health_emergency, null);
            myHolder = new MyHolder();

            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
            myHolder.tv_health_emergency_state= (TextView) view.findViewById(R.id.tv_health_emergency_state);
            myHolder.tv_health_emergency_time= (TextView) view.findViewById(R.id.tv_health_emergency_time);

        myHolder.tv_health_emergency_time.setText(list.get(position).getDay()+" "+list.get(position).getApm()+list.get(position).getTime());
        myHolder.tv_health_emergency_state.setText(list.get(position).getWarnings());
        return view;
    }


    public void setList(List<HealWarningsBean.HealWarningsInfoBean> list) {
        this.list = list;
    }


    class MyHolder {
        TextView tv_health_emergency_state;
        TextView tv_health_emergency_time;
    }


}
