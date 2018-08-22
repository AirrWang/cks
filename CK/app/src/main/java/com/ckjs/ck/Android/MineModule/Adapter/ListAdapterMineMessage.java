package com.ckjs.ck.Android.MineModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Android.MineModule.Activity.MineAttentionPeople;
import com.ckjs.ck.Bean.LetterListBean;
import com.ckjs.ck.Bean.MessageListBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterMineMessage extends BaseAdapter {

    public List<MessageListBean.MessageListDetailBean> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<MessageListBean.MessageListDetailBean> dataSource) {
        this.dataSource = dataSource;
    }

    private List<MessageListBean.MessageListDetailBean> dataSource;
    private Context context;


    public ListAdapterMineMessage(Context context) {
        this.context = context;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mine_message, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.mine_message_tel = (TextView) view.findViewById(R.id.mine_message_tel);
        holder.mine_message_name = (TextView) view.findViewById(R.id.mine_message_name);
        holder.mine_message_time = (TextView) view.findViewById(R.id.mine_message_time);
        holder.mine_message_pic = (SimpleDraweeView) view.findViewById(R.id.mine_message_pic);
        holder.mine_message_num = (TextView) view.findViewById(R.id.mine_message_num);
        holder.sd_red_1 = (SimpleDraweeView) view.findViewById(R.id.sd_red_1);

        FrescoUtils.setImage(holder.mine_message_pic, AppConfig.url + dataSource.get(position).getPicurl());
        holder.mine_message_name.setText(dataSource.get(position).getUsername());

        holder.mine_message_time.setText(dataSource.get(position).getLasttime());
        if (dataSource.get(position).getNoread() != null) {
            if (dataSource.get(position).getNoread().equals("0")) {
                FrescoUtils.setImage(holder.sd_red_1, "");
                holder.mine_message_num.setText("");
            } else {
                FrescoUtils.setImage(holder.sd_red_1, AppConfig.res + R.drawable.message_red);
                holder.mine_message_num.setText(dataSource.get(position).getNoread());
            }
        }
        holder.mine_message_tel.setText(dataSource.get(position).getContent());

        if (dataSource == null) {
            return null;
        } else {
            return view;
        }

    }




    static class ViewHolder {
        TextView mine_message_name;
        TextView mine_message_tel;
        TextView mine_message_time;
        TextView mine_message_num;
        SimpleDraweeView mine_message_pic;
        SimpleDraweeView sd_red_1;
    }
}
