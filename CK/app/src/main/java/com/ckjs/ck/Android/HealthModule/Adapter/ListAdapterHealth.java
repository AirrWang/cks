package com.ckjs.ck.Android.HealthModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.GridAdapterHealthMajor;
import com.ckjs.ck.Bean.GetdirectBaen;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.ViewTool.MyGridView;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.ScreenUtils;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterHealth extends BaseAdapter {

    private List<GetdirectBaen.GetdirectBeanInfo> list;
    private Context context;

    public ListAdapterHealth(Context context) {
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
                    R.layout.item_list_health_gir, null);
            myHolder = new MyHolder();
            myHolder.gridView = (MyGridView) view.findViewById(R.id.grd_iv_health);
            myHolder.textViewName = (TextView) view.findViewById(R.id.textViewName);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        MoudleUtils.textViewSetText(myHolder.textViewName, list.get(position).getClassname());
        initGridIv(position, myHolder);

        return view;
    }


    private void initGridIv(final int p, final MyHolder myHolder) {
        GridAdapterHealthMajor gridAdapterHealthMajor = new GridAdapterHealthMajor(context);
        int numAll = Integer.parseInt(list.get(p).getType());//一行显示几个
        int columns = 0;
        switch (numAll) {
            case 1:
                columns = 1;
                break;
            case 2:
                columns = 2;
                break;
            case 3:
                columns = 2;
                break;
            case 4:
                columns = 2;
                break;
            case 5:
                columns = 3;
                break;
            case 6:
                columns = 3;
                break;

        }

        myHolder.gridView.setNumColumns(columns);
        gridAdapterHealthMajor.setType(Integer.parseInt(list.get(p).getType()));
        gridAdapterHealthMajor.setColumns(columns);
//        gridAdapterHealthMajor.setW(ScreenUtils.getScreenWidth(context) / columns);
//        double d = Double.parseDouble(list.get(p).getDirect().get(list.get(p).getDirect().size() - 1).getRatio());
//        double d = Double.parseDouble(list.get(position).getDirect().get(0).getRatio());
//        gridAdapterHealthMajor.setH((int) (ScreenUtils.getScreenWidth(context) / columns * d));

        gridAdapterHealthMajor.setList(list.get(p).getDirect());
        myHolder.gridView.setAdapter(gridAdapterHealthMajor);
    }

    public void setList(List<GetdirectBaen.GetdirectBeanInfo> list) {
        this.list = list;
    }


    class MyHolder {
        MyGridView gridView;
        TextView textViewName;
    }


}
