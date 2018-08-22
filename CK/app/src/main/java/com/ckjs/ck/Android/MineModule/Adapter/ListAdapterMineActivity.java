package com.ckjs.ck.Android.MineModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.GetMyActivityBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterMineActivity extends BaseAdapter {

    private List<GetMyActivityBean.GetMyActivityInfoBean> list;
    private Context context;

    public ListAdapterMineActivity(Context context) {
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
                    R.layout.item_list_mine_join, null);
            myHolder = new MyHolder();

            myHolder.simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.mine_activity_pic);
            myHolder.isstart= (TextView) view.findViewById(R.id.isstart);
            myHolder.actime= (TextView) view.findViewById(R.id.actime);
            myHolder.num= (TextView) view.findViewById(R.id.num);

            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        FrescoUtils.setImage(myHolder.simpleDraweeView, AppConfig.url_jszd + list.get(position).getPicture());
        MoudleUtils.textViewSetText(myHolder.isstart,list.get(position).getIsstart());
        MoudleUtils.textViewSetText(myHolder.actime,list.get(position).getActime());
        MoudleUtils.textViewSetText(myHolder.num,list.get(position).getBmnum()+"/"+list.get(position).getNum()+"人");
        return view;
    }


    public void setList(List<GetMyActivityBean.GetMyActivityInfoBean> list) {
        this.list = list;
    }


    class MyHolder {
        SimpleDraweeView simpleDraweeView;
        TextView isstart;
        TextView actime;
        TextView num;
    }


}
