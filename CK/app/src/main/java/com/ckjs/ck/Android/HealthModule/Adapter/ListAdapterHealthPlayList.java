package com.ckjs.ck.Android.HealthModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.DirectlistBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterHealthPlayList extends BaseAdapter {

    private List<DirectlistBean.DirectlistBeanInfo.Direct> list;
    private Context context;

    public ListAdapterHealthPlayList(Context context) {
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
                    R.layout.item_health_play_list, null);
            myHolder = new MyHolder(view);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        initAdtapteData(position, myHolder);
        return view;
    }

    private void initAdtapteData(int position, MyHolder myHolder) {
        FrescoUtils.setControllerListenerHealth(myHolder.simpleDraweeView, AppConfig.url_jszd + list.get(position).getPicture(), (int) (ScreenUtils.getScreenWidth() * 0.45));
        MoudleUtils.textViewSetText(myHolder.tv_name, list.get(position).getName());
        MoudleUtils.textViewSetText(myHolder.tv_jie_shao, list.get(position).getIntroduce());
        String difficultlist = list.get(position).getDifficult();
        if (difficultlist != null && !difficultlist.equals("")) {
            if (difficultlist.equals("1")) {
                MoudleUtils.textViewSetText(myHolder.tv_more, list.get(position).getTime() + "分钟  " + list.get(position).getFat() + "kcal  " + "初级");

            }
            if (difficultlist.equals("2")) {
                MoudleUtils.textViewSetText(myHolder.tv_more, list.get(position).getTime() + "分钟  " + list.get(position).getFat() + "kcal  " + "中级");

            }
            if (difficultlist.equals("3")) {
                MoudleUtils.textViewSetText(myHolder.tv_more, list.get(position).getTime() + "分钟  " + list.get(position).getFat() + "kcal  " + "高级");
            }
        }
    }


    public void setList(List<DirectlistBean.DirectlistBeanInfo.Direct> list) {
        this.list = list;
    }


    static class MyHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_jie_shao)
        TextView tv_jie_shao;
        @BindView(R.id.tv_more)
        TextView tv_more;
        @BindView(R.id.sd_health_list_pic)
        SimpleDraweeView simpleDraweeView;

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
