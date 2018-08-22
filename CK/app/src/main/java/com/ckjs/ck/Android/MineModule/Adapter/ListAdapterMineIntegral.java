package com.ckjs.ck.Android.MineModule.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.IntegralBean;
import com.ckjs.ck.R;

import java.util.List;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterMineIntegral extends BaseAdapter {

    public List<IntegralBean.IntegralInfoBean.IntegralInfoDetailBean> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<IntegralBean.IntegralInfoBean.IntegralInfoDetailBean> dataSource) {
        this.dataSource = dataSource;
    }

    private List<IntegralBean.IntegralInfoBean.IntegralInfoDetailBean> dataSource;

    public ListAdapterMineIntegral() {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mine_integral, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_mine_integral_name = (TextView) view.findViewById(R.id.tv_mine_integral_name);
        holder.tv_mine_integral_integral = (TextView) view.findViewById(R.id.tv_mine_integral_integral);


        holder.tv_mine_integral_name.setText(dataSource.get(position).getName());
        holder.tv_mine_integral_integral.setText(dataSource.get(position).getIntegral());

        if (dataSource == null) {
            return null;
        } else {
            return view;
        }
    }


    static class ViewHolder {
        TextView tv_mine_integral_name;
        TextView tv_mine_integral_integral;
    }

}
