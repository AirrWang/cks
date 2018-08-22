package com.ckjs.ck.Android.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ckjs.ck.Android.CkCircleModule.Activity.CircleIcBigActivity;
import com.ckjs.ck.Android.HomeModule.Activity.BaoMingActivity;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ViewPagerAdapterHomeeJsfMore extends PagerAdapter {
    List<String> data;
    Context context;

    public ViewPagerAdapterHomeeJsfMore(List<String> data, Context context) {
        // TODO Auto-generated constructor stub
        this.data = data;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        position %= data.size();
        View view = LayoutInflater.from(context).inflate(R.layout.vp_item_home_jsf, null);
        SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.image);
        FrescoUtils.setImage(imageView, AppConfig.url_jszd + data.get(position).toString());
        final int finalPosition = position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSetToBigPic(finalPosition, data);
            }
        });
        container.addView(view);
        return view;
    }

    private void initSetToBigPic(int prePosition, List<String> getPicture) {
        Intent intent = new Intent(context, CircleIcBigActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("getPicture", (ArrayList<String>) getPicture);
        intent.putExtras(bundle);
        intent.putExtra("p", prePosition);
        intent.putExtra("type", "ac");
        context.startActivity(intent);
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
        container.removeView((View) object);
    }


}
