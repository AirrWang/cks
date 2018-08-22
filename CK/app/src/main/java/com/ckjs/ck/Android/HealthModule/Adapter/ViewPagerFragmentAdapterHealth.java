package com.ckjs.ck.Android.HealthModule.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ckjs.ck.R;

import java.util.ArrayList;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ViewPagerFragmentAdapterHealth extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> list;
    private Context context;
    private int[] ids;

    public ViewPagerFragmentAdapterHealth(FragmentManager fm, ArrayList<Fragment> list, Context context, int[] ids) {
        super(fm);
        this.list = list;
        this.context = context;
        this.ids = ids;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(ids[position]);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    public View getTabView(int position) {
        View tabView = LayoutInflater.from(context).inflate(R.layout.custom_red_layout, null);
        TextView tabTitle = (TextView) tabView.findViewById(R.id.tv_tab_title);
        tabTitle.setText(ids[position]);
        return tabView;
    }
}
