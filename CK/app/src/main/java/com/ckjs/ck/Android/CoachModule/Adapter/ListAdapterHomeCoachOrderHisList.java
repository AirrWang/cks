package com.ckjs.ck.Android.CoachModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.CoachRecordBean;
import com.ckjs.ck.R;

import java.util.List;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */


public class ListAdapterHomeCoachOrderHisList extends BaseAdapter {


    private List<CoachRecordBean.CoachRecordInfoBean> list;
    private Context context;

    public ListAdapterHomeCoachOrderHisList(Context context) {
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
                    R.layout.item_order_his, null);
            myHolder = new MyHolder();
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        myHolder.tv_coach_order_name = (TextView) view.findViewById(R.id.tv_coach_order_name);
        myHolder.tv_coach_order_time = (TextView) view.findViewById(R.id.tv_coach_order_time);
        myHolder.tv_coach_order_shouyi = (TextView) view.findViewById(R.id.tv_coach_order_shouyi);
        myHolder.tv_coach_order_dakuan = (TextView) view.findViewById(R.id.tv_coach_order_dakuan);
        myHolder.tv_coach_order_tixian = (TextView) view.findViewById(R.id.tv_coach_order_tixian);

        myHolder.tv_coach_order_name.setText(list.get(position).getName());
        myHolder.tv_coach_order_time.setText(list.get(position).getTime());
        if (list.get(position).getType().equals("1")) {
            myHolder.tv_coach_order_shouyi.setText("+" + list.get(position).getAmount());
            myHolder.tv_coach_order_tixian.setText("");
            myHolder.tv_coach_order_dakuan.setText("");
        } else if (list.get(position).getType().equals("2")) {
            if (list.get(position).getStatus().equals("1")) {
                myHolder.tv_coach_order_dakuan.setText("已打款");
            } else if (list.get(position).getStatus().equals("0")) {
                myHolder.tv_coach_order_dakuan.setText("未打款");
            } else {
                myHolder.tv_coach_order_dakuan.setText("");
            }
            myHolder.tv_coach_order_shouyi.setText(" ");
            myHolder.tv_coach_order_tixian.setText("-" + list.get(position).getAmount());
        }

        return view;
    }


    public void setList(List<CoachRecordBean.CoachRecordInfoBean> list) {
        this.list = list;
    }


    class MyHolder {
        TextView tv_coach_order_name;
        TextView tv_coach_order_time;
        TextView tv_coach_order_shouyi;
        TextView tv_coach_order_dakuan;
        TextView tv_coach_order_tixian;
    }


}
