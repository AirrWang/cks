package com.ckjs.ck.Android.HealthModule.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.IntegralBean;
import com.ckjs.ck.Bean.RunlistBean;
import com.ckjs.ck.R;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterRunDetailRecord extends BaseAdapter {

    public List<RunlistBean.RunlistInfoBean> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<RunlistBean.RunlistInfoBean> dataSource) {
        this.dataSource = dataSource;
    }

    private List<RunlistBean.RunlistInfoBean> dataSource;

    public ListAdapterRunDetailRecord() {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_run_record_detail, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_run_record_data_detail = (TextView) view.findViewById(R.id.tv_run_record_data_detail);
        holder.tv_run_record_data_week = (TextView) view.findViewById(R.id.tv_run_record_data_week);
        holder.tv_run_record_data_speed= (TextView) view.findViewById(R.id.tv_run_record_data_speed);
        holder.tv_run_record_data_distance= (TextView) view.findViewById(R.id.tv_run_record_data_distance);
        holder.tv_run_record_data_consume= (TextView) view.findViewById(R.id.tv_run_record_data_consume);

        holder.tv_run_record_data_detail.setText(dataSource.get(position).getCreatetime());
//        holder.tv_run_record_data_week.setText(dataSource.get(position).getIntegral());
        holder.tv_run_record_data_speed.setText(dataSource.get(position).getSpeed()+"/km");
        holder.tv_run_record_data_distance.setText(dataSource.get(position).getMileage()+"km");
        holder.tv_run_record_data_consume.setText(dataSource.get(position).getFat()+"kcal");
        if (dataSource == null) {
            return null;
        } else {
            return view;
        }
    }


    static class ViewHolder {
        TextView tv_run_record_data_detail;
        TextView tv_run_record_data_week;
        TextView tv_run_record_data_speed;
        TextView tv_run_record_data_distance;
        TextView tv_run_record_data_consume;
    }

}
