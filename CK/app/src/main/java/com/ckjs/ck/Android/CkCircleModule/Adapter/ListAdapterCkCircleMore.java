package com.ckjs.ck.Android.CkCircleModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.GetcommentBean;
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
public class ListAdapterCkCircleMore extends BaseAdapter {


    public List<GetcommentBean.GetcommentBeanInfo> getList() {
        return list;
    }


    private List<GetcommentBean.GetcommentBeanInfo> list;
    private Context context;

    public ListAdapterCkCircleMore(Context context) {
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
                    R.layout.item_list_ckcircle_more, null);
            myHolder = new MyHolder();
            myHolder.textViewContent = (TextView) view.findViewById(R.id.textViewContent);
            myHolder.textViewName = (TextView) view.findViewById(R.id.textViewName);
            myHolder.textViewtime = (TextView) view.findViewById(R.id.textViewTime);
            myHolder.textViewNameToUserName = (TextView) view.findViewById(R.id.textViewNameToUserName);
            myHolder.textViewNameHuiFu = (TextView) view.findViewById(R.id.textViewNameHuiFu);
            myHolder.sdIcon = (SimpleDraweeView) view.findViewById(R.id.sdIcon);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }

        MoudleUtils.textViewSetText(myHolder.textViewName, list.get(position).getUsername());
        String addname = list.get(position).getAddname();
        String addname_hf = "回复";
        if (addname == null || addname.equals("")) {
            addname = "";
            addname_hf = "";
        }
        MoudleUtils.textViewSetText(myHolder.textViewNameToUserName, addname);
        MoudleUtils.textViewSetText(myHolder.textViewNameHuiFu, addname_hf);
        MoudleUtils.textViewSetText(myHolder.textViewContent, list.get(position).getContent());
        MoudleUtils.textViewSetText(myHolder.textViewtime, list.get(position).getTime());
        FrescoUtils.setImage(myHolder.sdIcon, AppConfig.url + list.get(position).getPicurl());
        return view;
    }

    public void setList(List<GetcommentBean.GetcommentBeanInfo> list) {
        this.list = list;
    }


    class MyHolder {
        TextView textViewContent;
        TextView textViewName;
        TextView textViewNameToUserName;
        TextView textViewNameHuiFu;
        TextView textViewtime;
        SimpleDraweeView sdIcon;
    }


}
