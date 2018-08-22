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
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterMinePrivateLetter extends BaseAdapter {

    public List<LetterListBean.LetterListInfoBean> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<LetterListBean.LetterListInfoBean> dataSource) {
        this.dataSource = dataSource;
    }

    private List<LetterListBean.LetterListInfoBean> dataSource;
    private Context context;


    public ListAdapterMinePrivateLetter(Context context) {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mine_private_letter, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.mine_private_tel = (TextView) view.findViewById(R.id.mine_private_tel);
        holder.mine_private_name = (TextView) view.findViewById(R.id.mine_private_name);
        holder.mine_private_time = (TextView) view.findViewById(R.id.mine_private_time);
        holder.mine_private_pic = (SimpleDraweeView) view.findViewById(R.id.mine_private_pic);
        holder.mine_private_num = (TextView) view.findViewById(R.id.mine_private_num);
        holder.mine_private_sex = (SimpleDraweeView) view.findViewById(R.id.mine_private_sex);
        holder.sd_red = (SimpleDraweeView) view.findViewById(R.id.sd_red);

        FrescoUtils.setImage(holder.mine_private_pic, AppConfig.url + dataSource.get(position).getPicurl());
        holder.mine_private_name.setText(dataSource.get(position).getUsername());

        holder.mine_private_time.setText(dataSource.get(position).getLasttime());
        if (dataSource.get(position).getNoread() != null) {
            if (dataSource.get(position).getNoread().equals("0")) {
                FrescoUtils.setImage(holder.sd_red, "");
                holder.mine_private_num.setText("");
            } else {
                FrescoUtils.setImage(holder.sd_red, AppConfig.res + R.drawable.message_red);
                holder.mine_private_num.setText(dataSource.get(position).getNoread());
            }
        }
        if (dataSource.get(position).getSex() != null) {
            if (dataSource.get(position).getSex().equals("1")) {
                FrescoUtils.setImage(holder.mine_private_sex, AppConfig.res + R.drawable.my_boy);
            } else if (dataSource.get(position).getSex().equals("2")) {
                FrescoUtils.setImage(holder.mine_private_sex, AppConfig.res + R.drawable.my_girl);
            }
        }
        holder.mine_private_tel.setText(dataSource.get(position).getContent());
        holder.mine_private_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, MineAttentionPeople.class);
                intent.putExtra("focus_id", "" + dataSource.get(position).getAdd_id());
                context.startActivity(intent);
            }
        });
        if (dataSource == null) {
            return null;
        } else {
            return view;
        }

    }




    static class ViewHolder {
        TextView mine_private_name;
        TextView mine_private_tel;
        TextView mine_private_time;
        TextView mine_private_num;
        SimpleDraweeView mine_private_pic;
        SimpleDraweeView mine_private_sex;
        SimpleDraweeView sd_red;
    }
}
