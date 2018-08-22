package com.ckjs.ck.Android.MineModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.MessageBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterMineMessageInfo extends BaseAdapter {

    private Context context;

    public ListAdapterMineMessageInfo(Context context) {
        this.context = context;
    }

    public List<MessageBean.MessageInfoBean> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<MessageBean.MessageInfoBean> dataSource) {
        this.dataSource = dataSource;
    }

    private List<MessageBean.MessageInfoBean> dataSource;

    public ListAdapterMineMessageInfo() {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mine_message_info, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.mine_letter_pic = (SimpleDraweeView) view.findViewById(R.id.mine_message_pic);
        holder.tv_duihua = (TextView) view.findViewById(R.id.tv_duihua);
        holder.tv_duihua_time = (TextView) view.findViewById(R.id.tv_duihua_time);
        holder.sd_message_pic = (SimpleDraweeView) view.findViewById(R.id.sd_message_pic);
        holder.tv_message_title = (TextView) view.findViewById(R.id.tv_message_title);
        if (dataSource.get(position).getType().equals("1")) {
            MoudleUtils.viewGone(holder.sd_message_pic);
            MoudleUtils.viewGone(holder.tv_message_title);
        }
        if (dataSource.get(position).getType().equals("4")) {
            FrescoUtils.setImage(holder.sd_message_pic, AppConfig.url + dataSource.get(position).getPicture());

        } else {
            FrescoUtils.setImage(holder.sd_message_pic, AppConfig.url_jszd + dataSource.get(position).getPicture());

        }
        FrescoUtils.setImage(holder.mine_letter_pic, AppConfig.url + dataSource.get(position).getPicurl());
        holder.tv_duihua.setText(dataSource.get(position).getContent());
        holder.tv_duihua_time.setText(dataSource.get(position).getCreatetime());
        holder.tv_message_title.setText(dataSource.get(position).getTitle());


        if (dataSource == null) {
            return null;
        } else {
            return view;
        }
    }


    static class ViewHolder {
        TextView tv_duihua;
        TextView tv_duihua_time;
        SimpleDraweeView mine_letter_pic;
        SimpleDraweeView sd_message_pic;
        TextView tv_message_title;
    }
}
