package com.ckjs.ck.Android.HealthModule.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.IntegralBean;
import com.ckjs.ck.Bean.RunmonthBean;
import com.ckjs.ck.R;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterRunRecord extends BaseAdapter {

    public List<RunmonthBean.RunmonthInfoBean> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<RunmonthBean.RunmonthInfoBean> dataSource) {
        this.dataSource = dataSource;
    }

    private List<RunmonthBean.RunmonthInfoBean> dataSource;

    public ListAdapterRunRecord() {
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (dataSource != null) {
            return dataSource.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = null;
        ViewHolder holder = null;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_run_record, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_run_record_data = (TextView) view.findViewById(R.id.tv_run_record_data);
        holder.tv_run_record_num = (TextView) view.findViewById(R.id.tv_run_record_num);


        holder.tv_run_record_data.setText(dataSource.get(position).getMonthv());
        holder.tv_run_record_num.setText(dataSource.get(position).getNum()+"次跑步");

        if (dataSource == null) {
            return null;
        } else {
            return view;
        }
    }


    static class ViewHolder {
        TextView tv_run_record_data;
        TextView tv_run_record_num;
    }

}
