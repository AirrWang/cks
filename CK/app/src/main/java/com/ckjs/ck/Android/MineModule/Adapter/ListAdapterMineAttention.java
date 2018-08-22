package com.ckjs.ck.Android.MineModule.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
public class ListAdapterMineAttention extends BaseAdapter {

    public List<GetMyfocusBean.GetMyfocusInfoBean> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<GetMyfocusBean.GetMyfocusInfoBean> dataSource) {
        this.dataSource = dataSource;
    }

    private List<GetMyfocusBean.GetMyfocusInfoBean> dataSource;

    public ListAdapterMineAttention() {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mine_attention, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.mine_attention_name = (TextView) view.findViewById(R.id.mine_attention_name);
        holder.mine_attention_pic = (SimpleDraweeView) view.findViewById(R.id.mine_attention_pic);
        holder.mine_attention_name.setText(dataSource.get(position).getFocus_name());
        FrescoUtils.setImage(holder.mine_attention_pic, AppConfig.url + dataSource.get(position).getPicurl());
//            holder.mine_attention_pic.setImageURI(AppConfig.url+""+dataSource.get(position).getPicurl());


        if (dataSource == null) {
            return null;
        } else {
            return view;
        }
    }


    static class ViewHolder {
        TextView mine_attention_name;

        SimpleDraweeView mine_attention_pic;
    }

}
