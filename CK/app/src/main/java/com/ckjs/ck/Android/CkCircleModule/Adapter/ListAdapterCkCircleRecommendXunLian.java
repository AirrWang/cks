package com.ckjs.ck.Android.CkCircleModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.CaltoplistBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterCkCircleRecommendXunLian extends BaseAdapter {

    public void setList(List<CaltoplistBean.CaltoplistBeanInfo> list) {
        this.list = list;
    }

    private List<CaltoplistBean.CaltoplistBeanInfo> list;
    private Context context;

    public ListAdapterCkCircleRecommendXunLian(Context context) {
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
    public View getView( int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        MyHolder myHolder = null;
        if (view == null) {

            view = LayoutInflater.from(context).inflate(
                    R.layout.item_list_ckcircle_recommend_xun_lian, null);
            myHolder = new MyHolder();
            myHolder.textViewCount = (TextView) view.findViewById(R.id.textViewCount);
            myHolder.textViewName = (TextView) view.findViewById(R.id.textViewName);
            myHolder.textViewConsumptionNum = (TextView) view.findViewById(R.id.textViewConsumptionNum);
            myHolder.iv_circle_recommend = (SimpleDraweeView) view.findViewById(R.id.iv_circle_recommend);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        FrescoUtils.setImage(myHolder.iv_circle_recommend, AppConfig.url + list.get(position).getPicurl());
        MoudleUtils.textViewSetText(myHolder.textViewName, list.get(position).getUsername());
        MoudleUtils.textViewSetText(myHolder.textViewConsumptionNum, list.get(position).getCalories());
        MoudleUtils.textViewSetText(myHolder.textViewCount, (position +1)+ "");
        return view;
    }




    class MyHolder {

        TextView textViewCount;
        TextView textViewName;
        TextView textViewConsumptionNum;
        SimpleDraweeView iv_circle_recommend;
    }


}
