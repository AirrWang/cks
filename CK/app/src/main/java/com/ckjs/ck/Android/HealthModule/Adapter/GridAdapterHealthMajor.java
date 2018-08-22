package com.ckjs.ck.Android.HealthModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Activity.HealthMoreActivity;
import com.ckjs.ck.Android.HealthModule.Activity.HealthPlayListActivity;
import com.ckjs.ck.Android.HomeModule.Activity.AcH5Activity;
import com.ckjs.ck.Bean.GetdirectBaen;
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
public class GridAdapterHealthMajor extends BaseAdapter {

    private int size;

//    public int getW() {
//        return w;
//    }
//
//    public void setW(int w) {
//        this.w = w;
//    }
//
//    public int getH() {
//        return h;
//    }
//
//    public void setH(int h) {
//        this.h = h;
//    }

//    private int w;
//    private int h;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    private int columns;
    private List<GetdirectBaen.GetdirectBeanInfo.GetdirectBeanInfoDirect> list;


    private Context context;

    public GridAdapterHealthMajor(Context context) {
        super();
        this.context = context;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (type == 3 || type == 5) {
            size = list.size();

            size = list.size() + 1 - columns;
        } else {
            size = list.size();
        }
        return size;
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

    @Override
    public View getView(final int position, View v, ViewGroup arg2) {
        // TODO Auto-generated method stub
        View view = v;
        MyHodler myHodler = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_grid_ckcirclemine_health, null);
            if (type == 3 || type == 5) {
//                AbsListView.LayoutParams layoutParams =
//                        new AbsListView.LayoutParams((int) w, (int) (h * 2));
//                view.setLayoutParams(layoutParams);
            }
            myHodler = new MyHodler();
            myHodler.iv_grid_circle_mine = (SimpleDraweeView) view
                    .findViewById(R.id.iv_grid_circle_mine);
            myHodler.iv_grid_circle_mine_two = (SimpleDraweeView) view
                    .findViewById(R.id.iv_grid_circle_mine_two);

            initThreeFive(position, myHodler);
            myHodler.tv_text = (TextView) view
                    .findViewById(R.id.tv_text);
            myHodler.tv_text_two = (TextView) view
                    .findViewById(R.id.tv_text_two);
            view.setTag(myHodler);
        } else {
            myHodler = (MyHodler) view.getTag();

        }
        initData(position, myHodler);
        myHodler.iv_grid_circle_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (type == 3) {
                    switch (position) {
                        case 0:
                            initType(intent, 0);
                            intent.putExtra("direct_id", list.get(0).getId());
                            context.startActivity(intent);
                            break;
                        case 1:
                            initType(intent, 1);

                            intent.putExtra("direct_id", list.get(1).getId());
                            context.startActivity(intent);
                            break;

                    }
                } else if (type == 5) {
                    switch (position) {
                        case 0:
                            initType(intent, 0);
                            intent.putExtra("direct_id", list.get(0).getId());
                            context.startActivity(intent);

                            break;
                        case 1:
                            initType(intent, 1);
                            intent.putExtra("direct_id", list.get(1).getId());
                            context.startActivity(intent);

                            break;
                        case 2:
                            initType(intent, 2);
                            intent.putExtra("direct_id", list.get(2).getId());
                            context.startActivity(intent);

                            break;

                    }
                } else {
                    initType(intent, position);
                    intent.putExtra("direct_id", list.get(position).getId());
                    context.startActivity(intent);
                }
            }
        });
        myHodler.iv_grid_circle_mine_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(context, HealthMoreActivity.class);
                if (type == 3) {
                    switch (position) {
                        case 1:
                            initType(intent, 2);
                            intent.putExtra("direct_id", list.get(2).getId());
                            context.startActivity(intent);

                            break;

                    }
                } else if (type == 5) {
                    switch (position) {
                        case 1:
                            initType(intent, 3);
                            intent.putExtra("direct_id", list.get(3).getId());
                            context.startActivity(intent);

                            break;
                        case 2:
                            initType(intent, 4);
                            intent.putExtra("direct_id", list.get(4).getId());
                            context.startActivity(intent);

                            break;

                    }
                }

            }
        });
        return view;
    }

    private void initType(Intent intent, int p) {
        if (list.get(p).getType().equals("1")) {
            intent.setClass(context, HealthMoreActivity.class);
        } else if (list.get(p).getType().equals("2")) {
            intent.putExtra("acurl", list.get(p).getDirecturl());
            intent.setClass(context, AcH5Activity.class);
        } else if (list.get(p).getType().equals("3")) {
            intent.putExtra("class_id", list.get(p).getClass_id());
            intent.setClass(context, HealthPlayListActivity.class);
        }
    }


    private void initThreeFive(int position, MyHodler myHodler) {
        if (type == 3 || type == 5) {
            if (position == 0) {
//                myHodler.iv_grid_circle_mine.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) (h * 2)));

            } else {
//                myHodler.iv_grid_circle_mine.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
//                myHodler.iv_grid_circle_mine_two.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));

            }
        } else {
//            myHodler.iv_grid_circle_mine.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
        }
    }

    private void initData(int position, MyHodler myHodler) {
        if (type == 5 || type == 3) {
            if (position == 0) {
                FrescoUtils.setImage(myHodler.iv_grid_circle_mine, AppConfig.url_jszd + list.get(position).getPicture());
                MoudleUtils.textViewSetText(myHodler.tv_text, list.get(position).getName());
                initGoneTwo(myHodler);
            } else {
                FrescoUtils.setImage(myHodler.iv_grid_circle_mine, AppConfig.url_jszd + list.get(position).getPicture());
                MoudleUtils.textViewSetText(myHodler.tv_text, list.get(position).getName());
                FrescoUtils.setImage(myHodler.iv_grid_circle_mine_two, AppConfig.url_jszd + list.get(position + size - 1).getPicture());
                MoudleUtils.textViewSetText(myHodler.tv_text_two, list.get(position + size - 1).getName());
                initShowTwo(myHodler);
            }

        } else {
            FrescoUtils.setImage(myHodler.iv_grid_circle_mine, AppConfig.url_jszd + list.get(position).getPicture());
            MoudleUtils.textViewSetText(myHodler.tv_text, list.get(position).getName());
        }
    }

    private void initGoneTwo(MyHodler myHodler) {
        myHodler.iv_grid_circle_mine_two.setVisibility(View.GONE);
        myHodler.tv_text_two.setVisibility(View.GONE);
    }

    private void initShowTwo(MyHodler myHodler) {
        myHodler.iv_grid_circle_mine_two.setVisibility(View.VISIBLE);
        myHodler.tv_text_two.setVisibility(View.VISIBLE);
    }

    public void setList(List<GetdirectBaen.GetdirectBeanInfo.GetdirectBeanInfoDirect> list) {
        this.list = list;
    }


    class MyHodler {
        SimpleDraweeView iv_grid_circle_mine;
        TextView tv_text;
        SimpleDraweeView iv_grid_circle_mine_two;
        TextView tv_text_two;
    }
}
