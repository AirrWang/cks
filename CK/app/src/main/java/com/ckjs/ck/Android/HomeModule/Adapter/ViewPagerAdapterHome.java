package com.ckjs.ck.Android.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Android.HomeModule.Activity.AcH5Activity;
import com.ckjs.ck.Android.HomeModule.Activity.BaoMingActivity;
import com.ckjs.ck.Bean.ActivityBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ViewPagerAdapterHome extends PagerAdapter {
    public List<ActivityBean.ActivityBeanInfo> getData() {
        return data;
    }

    public void setData(List<ActivityBean.ActivityBeanInfo> data) {
        this.data = data;
    }

    private List<ActivityBean.ActivityBeanInfo> data= new ArrayList<>();
    private Context context;

    public ViewPagerAdapterHome(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        position %= data.size();
        View view = LayoutInflater.from(context).inflate(R.layout.vp_item_home, null);
        SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.image);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(CkApplication.getInstance()), (int) ((ScreenUtils.getScreenWidth(CkApplication.getInstance())) / 2.64));
        imageView.setLayoutParams(layoutParams);
        FrescoUtils.setImage(imageView, AppConfig.url_jszd + data.get(position).getActcover());
        final int finalPosition = position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(finalPosition).getType().equals("1")) {
                    initToBaoMing(finalPosition, data);
                } else {
                    initToAcH5(finalPosition, data);
                }

            }
        });
        container.addView(view);
        return view;
    }

    private void initToAcH5(int position, List<ActivityBean.ActivityBeanInfo> list) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            Intent intent = new Intent();
            intent.putExtra("acurl", list.get(position).getAcurl());
            intent.setClass(context, AcH5Activity.class);
            context.startActivity(intent);
        } else {
            MoudleUtils.toChekWifi(context);

        }

    }

    private void initToBaoMing(int position, List<ActivityBean.ActivityBeanInfo> list) {

        if (NetworkUtils.isNetworkAvailable(context)) {
            Intent intent = new Intent();
            intent.setClass(context, BaoMingActivity.class);
            intent.putExtra("activity_id", list.get(position).getId());
            intent.putExtra("name", list.get(position).getName());
            context.startActivity(intent);
        } else {
            MoudleUtils.toChekWifi(context);

        }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data != null ? data.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);//用来刷新
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;//用来刷新
    }
}
