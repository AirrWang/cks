package com.ckjs.ck.Android.HealthModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.WrateBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterHeartDetailweek extends BaseAdapter {


    private List<WrateBean.WrateBeanInfo.WrateBeanDetailInfo> list;
    private Context context;

    public ListAdapterHeartDetailweek(Context context) {
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
                    R.layout.item_list_heart_week_detail, null);
            myHolder = new MyHolder(view);

            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        initData(myHolder, position);

        return view;
    }

    private void initData(MyHolder myHolder, int p) {
        MoudleUtils.textViewSetText(myHolder.tv_time, list.get(p).getCreatetime());
        String max = list.get(p).getMaxrate();
        String min = list.get(p).getMinrate();
        if (max != null && !max.equals("") && !max.equals("0")) {
            myHolder.tv_max.setTextColor(context.getResources().getColor(R.color.fea21249));
            MoudleUtils.textViewSetText(myHolder.tv_max, list.get(p).getMaxrate() + "bpm");
        }else {
            myHolder.tv_max.setTextColor(context.getResources().getColor(R.color.c_33));
            MoudleUtils.textViewSetText(myHolder.tv_max, "--");
        }
        if (min != null && !max.equals("") && !min.equals("0")) {
            myHolder.tv_min.setTextColor(context.getResources().getColor(R.color.fea21249));
            MoudleUtils.textViewSetText(myHolder.tv_min, list.get(p).getMinrate() + "bpm");
        }else {
            myHolder.tv_min.setTextColor(context.getResources().getColor(R.color.c_33));
            MoudleUtils.textViewSetText(myHolder.tv_min, "--");
        }
        MoudleUtils.textViewSetText(myHolder.tv_ave, list.get(p).getRate() + "bpm");


    }


    public void setList(List<WrateBean.WrateBeanInfo.WrateBeanDetailInfo> list) {
        this.list = list;
    }


    class MyHolder {

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.tv_time_detail)
        TextView tv_time;
        @BindView(R.id.tv_max_detail)
        TextView tv_max;
        @BindView(R.id.tv_min_detail)
        TextView tv_min;
        @BindView(R.id.tv_ave_detail)
        TextView tv_ave;
    }


}
