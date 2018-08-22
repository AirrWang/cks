package com.ckjs.ck.Android.CkCircleModule.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ViewPagerFragmentAdapterCkCircle extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> list;
    private Context context;
    private int[] ids;

    public ViewPagerFragmentAdapterCkCircle(FragmentManager fm, ArrayList<Fragment> list, Context context, int[] ids) {
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

}
