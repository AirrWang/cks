package com.ckjs.ck.Android.HomeModule.Adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ckjs.ck.Bean.PrivilegeBean;
import com.ckjs.ck.R;

import com.ckjs.ck.Tool.MoudleUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterCityJsfCkthList extends BaseAdapter {


    private List<PrivilegeBean.PrivilegeBeanInfo> list;
    private Context context;

    public ListAdapterCityJsfCkthList(Context context) {
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
                    R.layout.item_list_city_jsf_ckth_list, null);
            myHolder = new MyHolder(view);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }

        initData(position, myHolder);
        return view;
    }

    private void initData(final int position, MyHolder myHolder) {


        MoudleUtils.textViewSetText(myHolder.tv_name, list.get(position).getName());
        MoudleUtils.textViewSetText(myHolder.tv_now_money, "￥" + list.get(position).getPrice());
        String s = list.get(position).getOriginalprice();
        if (s != null) {
            if (s.equals("")) {
                myHolder.tv_moneyed.setText("");
            } else {
                s = "￥" + s + " ";
                SpannableString ss = new SpannableString(s);
                ss.setSpan(new StrikethroughSpan(), 0, s.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                myHolder.tv_moneyed.setText(ss);
            }
        } else {
            myHolder.tv_moneyed.setText("");
        }
        MoudleUtils.textViewSetText(myHolder.tv_day, list.get(position).getHolidays() + "天请假数");
        MoudleUtils.textViewSetText(myHolder.tv_time, "即时生效，" + list.get(position).getExpiry() + "天有效期");


    }


    public void setList(List<PrivilegeBean.PrivilegeBeanInfo> list) {
        this.list = list;
    }


    class MyHolder {
        @BindView(R.id.textView19)
        TextView tv_name;
        @BindView(R.id.textView21)
        TextView tv_now_money;
        @BindView(R.id.textViewMoneyed)
        TextView tv_moneyed;
        @BindView(R.id.textViewDay)
        TextView tv_day;
        @BindView(R.id.textViewTime)
        TextView tv_time;
//        @BindView(R.id.tv_line)
//        TextView tv_line;


        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
