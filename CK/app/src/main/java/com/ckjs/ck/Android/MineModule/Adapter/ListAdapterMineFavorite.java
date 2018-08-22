package com.ckjs.ck.Android.MineModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.GetMyfavoriteBean;
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
public class ListAdapterMineFavorite extends BaseAdapter {

    private List<GetMyfavoriteBean.GetMyfavoriteInfoBean> list;
    private Context context;

    public ListAdapterMineFavorite(Context context) {
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
                    R.layout.item_mine_favorite, null);
            myHolder = new MyHolder();

            myHolder.simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.ivSm);
            myHolder.name = (TextView) view.findViewById(R.id.textViewName);
            myHolder.js = (TextView) view.findViewById(R.id.textViewJs);
            myHolder.sd_tag= (SimpleDraweeView) view.findViewById(R.id.sd_tag);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        FrescoUtils.setImage(myHolder.simpleDraweeView, AppConfig.url_jszd + list.get(position).getPicture());
        MoudleUtils.textViewSetText(myHolder.name,list.get(position).getTitle());
        MoudleUtils.textViewSetText(myHolder.js,list.get(position).getIntro());
        if (list.get(position).getType().equals("1")){
            FrescoUtils.setImage(myHolder.sd_tag, AppConfig.res + R.drawable.health_study);
        }
        else {
            FrescoUtils.setImage(myHolder.sd_tag, AppConfig.res + R.drawable.health_lecture);
        }
        return view;
    }


    public void setList(List<GetMyfavoriteBean.GetMyfavoriteInfoBean> list) {
        this.list = list;
    }


    class MyHolder {

        SimpleDraweeView simpleDraweeView;
        SimpleDraweeView sd_tag;
        TextView name;
        TextView js;
    }


}
