package com.ckjs.ck.Android.MineModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.R;

import java.util.List;
import java.util.Map;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MineSettingAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,String>> itemList;

    public MineSettingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder groupHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_setting, null);
            groupHolder = new ViewHolder();
            groupHolder.textViewContent = (TextView) convertView.findViewById(R.id.textView_set);
            groupHolder.textViewInfo = (TextView) convertView.findViewById(R.id.textViewInfo);
            groupHolder.textViewDetails= (TextView) convertView.findViewById(R.id.textView_set_details);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolder) convertView.getTag();
        }

        //获取当前版本
//        if(position==3){
//            groupHolder.textViewDetails.setText(MoudleUtils.getVersionName(context)+MoudleUtils.getVersionCode(context)+"版本");
//        }
        groupHolder.textViewContent.setText(itemList.get(position).get("content"));
        groupHolder.textViewInfo.setText(itemList.get(position).get("info"));
        return convertView;
    }

    public void setItemList(List<Map<String, String>> itemList) {
        this.itemList = itemList;
    }



    class ViewHolder {
        private TextView textViewDetails;
        private TextView textViewContent;
        private TextView textViewInfo;
    }


}
