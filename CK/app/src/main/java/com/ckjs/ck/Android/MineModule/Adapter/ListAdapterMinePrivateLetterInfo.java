package com.ckjs.ck.Android.MineModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Bean.LetterBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterMinePrivateLetterInfo extends BaseAdapter {

    private Context context;

    public ListAdapterMinePrivateLetterInfo(Context context) {
        this.context = context;
    }

    public List<LetterBean.LetterInfoBean> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<LetterBean.LetterInfoBean> dataSource) {
        this.dataSource = dataSource;
    }

    private List<LetterBean.LetterInfoBean> dataSource;

    public ListAdapterMinePrivateLetterInfo() {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mine_private_letter_info, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.mine_letter_pic= (SimpleDraweeView) view.findViewById(R.id.mine_letter_pic);
        holder.tv_duihua= (TextView) view.findViewById(R.id.tv_duihua);
        holder.tv_duihua_time= (TextView) view.findViewById(R.id.tv_duihua_time);
        holder.tv_right= (TextView) view.findViewById(R.id.tv_right);
        holder.tv_right_time= (TextView) view.findViewById(R.id.tv_right_time);
        holder.mine_letter_pic_right= (SimpleDraweeView) view.findViewById(R.id.mine_letter_pic_right);
        holder.ll_left= (LinearLayout) view.findViewById(R.id.ll_left);
        holder.ll_right= (LinearLayout) view.findViewById(R.id.ll_right);

        int use_id= (int) SPUtils.get(context,"user_id",0);
        if (!dataSource.get(position).getUser_id().equals(use_id+"")){
//            holder.ll_right.setVisibility(View.VISIBLE);
            MoudleUtils.viewShow(holder.ll_left);
//            holder.ll_left.setVisibility(View.GONE);
            MoudleUtils.viewGone(holder.ll_right);
            FrescoUtils.setImage(holder.mine_letter_pic,AppConfig.url+dataSource.get(position).getPicurl());
            holder.tv_duihua.setText(dataSource.get(position).getContent());
            holder.tv_duihua_time.setText(dataSource.get(position).getCreatetime());
        }else {
//            holder.ll_left.setVisibility(View.VISIBLE);
            MoudleUtils.viewShow(holder.ll_right);

//            holder.ll_right.setVisibility(View.GONE);
            MoudleUtils.viewGone(holder.ll_left);

            FrescoUtils.setImage(holder.mine_letter_pic_right,AppConfig.url+dataSource.get(position).getPicurl());
            holder.tv_right.setText(dataSource.get(position).getContent());
            holder.tv_right_time.setText(dataSource.get(position).getCreatetime());
        }


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
        TextView tv_right;
        TextView tv_right_time;
        SimpleDraweeView mine_letter_pic_right;
        LinearLayout ll_left;
        LinearLayout ll_right;
    }
}
