package com.ckjs.ck.Android.MineModule.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.GetBodyInfoBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.ScreenUtils;
import java.util.List;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class GridAdapterBodyInfoMine extends BaseAdapter {
    private List<GetBodyInfoBean.GetBodyInfoDetailBean> list;
    private Context context;

    public GridAdapterBodyInfoMine(Context context) {
        super();
        this.context = context;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View v, ViewGroup arg2) {
        // TODO Auto-generated method stub
        View view = v;
        MyHodler myHodler = null;
        if (view == null) {

            view = LayoutInflater.from(context).inflate(
                    R.layout.item_grid_bodyinfomine, null);
            myHodler = new MyHodler();
            myHodler.body_info_name = (TextView) view
                    .findViewById(R.id.body_info_name);
            myHodler.body_info_level = (TextView) view
                    .findViewById(R.id.body_info_level);

            view.setTag(myHodler);
        } else {
            myHodler = (MyHodler) view.getTag();

        }
        initData(position, myHodler);
        return view;
    }


    private void initData(int position, MyHodler myHodler) {
        int w = ScreenUtils.getScreenWidth(CkApplication.getInstance()) / 24;
        if (list.get(position) != null) {
            if (!list.get(position).equals("")) {
                myHodler.body_info_name.setText(list.get(position).getName());
                if (list.get(position).getLevel().equals("1")) {
                    initSetZz(myHodler, (int) (w), (int) (w * 1.46), CkApplication.getInstance().getResources().getDrawable(R.drawable.mine_rectangle_1));
                    initSetZzTitile(myHodler, (int) (w), (int) (w /24));
                } else if (list.get(position).getLevel().equals("2")) {
                    initSetZz(myHodler, (int) (w), (int) (w * 2.93), CkApplication.getInstance().getResources().getDrawable(R.drawable.mine_rectangle_2));
                    initSetZzTitile(myHodler, (int) (w), (int) (w /24));
                } else if (list.get(position).getLevel().equals("3")) {
                    initSetZz(myHodler, (int) (w), (int) (w * 44), CkApplication.getInstance().getResources().getDrawable(R.drawable.mine_rectangle_3));
                    initSetZzTitile(myHodler, (int) (w), (int) (w /24));
                }

            }
        }

    }

    private void initSetZzTitile(MyHodler myHodler, int w, int i) {
//        RelativeLayout.LayoutParams layoutParams =
//                new RelativeLayout.LayoutParams(w, i);
//        myHodler.body_info_name.setLayoutParams(layoutParams);
    }

    private void initSetZz(MyHodler myHodler, int w2, int height, Drawable drawable) {
//        RelativeLayout.LayoutParams layoutParams =
//                new RelativeLayout.LayoutParams(w2, height);
//        myHodler.body_info_level.setLayoutParams(layoutParams);
        myHodler.body_info_level.setBackgroundDrawable(drawable);
    }

    public void setList(List<GetBodyInfoBean.GetBodyInfoDetailBean> list) {
        this.list = list;
    }


    class MyHodler {
        TextView body_info_name;
        TextView body_info_level;
    }
}
