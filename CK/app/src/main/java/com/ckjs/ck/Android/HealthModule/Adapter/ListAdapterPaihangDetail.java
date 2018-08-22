package com.ckjs.ck.Android.HealthModule.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.CaltopInfoBean;
import com.ckjs.ck.Bean.GetMyfocusBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterPaihangDetail extends BaseAdapter {

    public List<CaltopInfoBean.CaltopInfoDetailBean.CaltopDetailBean> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<CaltopInfoBean.CaltopInfoDetailBean.CaltopDetailBean> dataSource) {
        this.dataSource = dataSource;
    }

    private List<CaltopInfoBean.CaltopInfoDetailBean.CaltopDetailBean> dataSource;

    public ListAdapterPaihangDetail() {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_paihang_detail, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_paihang_list_rank = (TextView) view.findViewById(R.id.tv_paihang_list_rank);
        holder.sd_paihang_list_rank = (SimpleDraweeView) view.findViewById(R.id.sd_paihang_list_rank);
        holder.sd_paihang_list_pic = (SimpleDraweeView) view.findViewById(R.id.sd_paihang_list_pic);
        holder.tv_paihang_list_name= (TextView) view.findViewById(R.id.tv_paihang_list_name);
        holder.tv_paihang_list_step= (TextView) view.findViewById(R.id.tv_paihang_list_step);
        holder.tv_paihang_list_cal= (TextView) view.findViewById(R.id.tv_paihang_list_cal);


        holder.tv_paihang_list_name.setText(dataSource.get(position).getUsername());
        holder.tv_paihang_list_step.setText(dataSource.get(position).getSteps()+"步");
        holder.tv_paihang_list_cal.setText(dataSource.get(position).getCalories());
        FrescoUtils.setImage(holder.sd_paihang_list_pic, AppConfig.url + dataSource.get(position).getPicurl());
        if (position==0||position==1||position==2){
            holder.tv_paihang_list_rank.setText("");
            if (position==0){
                FrescoUtils.setImage(holder.sd_paihang_list_rank, AppConfig.res +R.drawable.number_one);
            }else if (position==1){
                FrescoUtils.setImage(holder.sd_paihang_list_rank, AppConfig.res +R.drawable.number_two);
            }else if (position==2){
                FrescoUtils.setImage(holder.sd_paihang_list_rank, AppConfig.res +R.drawable.numbwe_three);
            }
        }else {
            holder.tv_paihang_list_rank.setText(position+1+"");
        }


        if (dataSource == null) {
            return null;
        } else {
            return view;
        }
    }


    static class ViewHolder {
        TextView tv_paihang_list_rank;

        SimpleDraweeView sd_paihang_list_rank;

        SimpleDraweeView sd_paihang_list_pic;
        TextView tv_paihang_list_name;
        TextView tv_paihang_list_step;
        TextView tv_paihang_list_cal;
    }

}
