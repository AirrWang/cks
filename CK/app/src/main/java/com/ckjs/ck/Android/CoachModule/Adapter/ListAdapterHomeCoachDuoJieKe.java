package com.ckjs.ck.Android.CoachModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.LessonBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterHomeCoachDuoJieKe extends BaseAdapter {


    private List<LessonBean.Lesson> list;
    private Context context;

    public ListAdapterHomeCoachDuoJieKe(Context context) {
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
                    R.layout.item_duojieke, null);
            myHolder = new MyHolder(view);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }

        MoudleUtils.textViewSetText(myHolder.tv_end_time, list.get(position).getCstoptime());
        MoudleUtils.textViewSetText(myHolder.tv_start_time, list.get(position).getCstartime());
        MoudleUtils.textViewSetText(myHolder.tv_num, "第"+list.get(position).getSort()+"节课");

        String tv_type = list.get(position).getType();
        if (tv_type != null) {
            if (tv_type.equals("distance")) {
                MoudleUtils.textViewSetText(myHolder.tv_lx, "视频指导");
            } else if (tv_type.equals("personal")) {
                MoudleUtils.textViewSetText(myHolder.tv_lx, "上门指导");
            }
        }
        return view;
    }


    public void setList(List<LessonBean.Lesson>  list) {
        this.list = list;
    }


    class MyHolder {
        @BindView(R.id.tv_lx)
         TextView tv_lx;
        @BindView(R.id.tv_num)
        TextView tv_num;
        @BindView(R.id.tv_start_time)
        TextView tv_start_time;
        @BindView(R.id.tv_end_time)
        TextView tv_end_time;

        public MyHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }


}
