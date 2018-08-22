package com.ckjs.ck.Android.HealthModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.WpressureBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.ViewTool.MyListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterXueYaweek extends BaseAdapter {
    private ListAdapterXueYaDetailweek listAdapterXueYaDetailweek;
    private List<WpressureBean.Info> list;
    private Context context;

    public ListAdapterXueYaweek(Context context) {
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
                    R.layout.item_list_xueya_week, null);
            myHolder = new MyHolder(view);

            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        initData(myHolder, position);

        return view;
    }

    private void initData(MyHolder myHolder, int p) {
        if (list == null || list.size() == 0)
            return;
        MoudleUtils.textViewSetText(myHolder.tv_day, list.get(p).getDate());
        //        if (list.get(p).getAvgspressure() > 0 && list.get(p).getAvgdpressure() > 0) {
        //            MoudleUtils.textViewSetText(myHolder.tv_ave, list.get(p).getAvgspressure() + "kpa");
        //            MoudleUtils.textViewSetText(myHolder.tv_max, list.get(p).getAvgdpressure() + "kpa");
        //            MoudleUtils.viewShow(myHolder.tv_line);
        //        } else {
        //            MoudleUtils.viewGone(myHolder.tv_line);
        //        }
        List<WpressureBean.Info.Record> record = list.get(p).getRecord();
        if (record != null && record.size() > 0) {
            listAdapterXueYaDetailweek = new ListAdapterXueYaDetailweek(context);
            listAdapterXueYaDetailweek.setList(record);
            myHolder.lv_heart_week_inside.setAdapter(listAdapterXueYaDetailweek);
        }
    }


    public void setList(List<WpressureBean.Info> list) {
        this.list = list;
    }


    class MyHolder {

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.tv_day)
        TextView tv_day;
        //        @BindView(R.id.tv_ave)
        //        TextView tv_ave;
        //        @BindView(R.id.tv_max)
        //        TextView tv_max;
        //        @BindView(R.id.tv_line)
        //        LinearLayout tv_line;
        @BindView(R.id.lv_heart_week_inside)
        MyListView lv_heart_week_inside;
    }


}
