package com.ckjs.ck.Android.CkCircleModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Bean.UsercommentBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterCirMsgList extends BaseAdapter {

    private List<UsercommentBean.UsercommentBeanInfo> list;
    private Context context;

    public ListAdapterCirMsgList(Context context) {
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
                    R.layout.item_list_cir_msg_lisg, null);
            myHolder = new MyHolder(view);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        FrescoUtils.setImage(myHolder.sd_icon, AppConfig.url + list.get(position).getPicurl());
        String xb = list.get(position).getSex();
        if (xb == null) {
            xb = "";
        }
        if (xb.equals("1")) {
            FrescoUtils.setImage(myHolder.sd_xb, AppConfig.res + R.drawable.my_boy);
        } else if (xb.equals("2")) {
            FrescoUtils.setImage(myHolder.sd_xb, AppConfig.res + R.drawable.my_girl);
        }
        String sd_msg = list.get(position).getPicture();
        if (sd_msg == null) {
            sd_msg = "";
        }
        if (sd_msg.equals("")) {
            FrescoUtils.setImage(myHolder.sd_msg, "");
            String tv_msg = list.get(position).getCircle_content();
            if (tv_msg == null) {
                tv_msg = "";
            }
            MoudleUtils.textViewSetText(myHolder.tv_msg, tv_msg);
        } else {
            MoudleUtils.textViewSetText(myHolder.tv_msg, "");
            FrescoUtils.setImage(myHolder.sd_msg, AppConfig.url + sd_msg);
        }
        MoudleUtils.textViewSetText(myHolder.tv_name, list.get(position).getUsername());
        MoudleUtils.textViewSetText(myHolder.tv_pl, list.get(position).getContent());
        MoudleUtils.textViewSetText(myHolder.tv_time, list.get(position).getTime());
        return view;
    }


    public void setList(List<UsercommentBean.UsercommentBeanInfo> list) {
        this.list = list;
    }


    class MyHolder {
        @BindView(R.id.textView43)
        SimpleDraweeView sd_icon;
        @BindView(R.id.sd_msg)
        SimpleDraweeView sd_msg;
        @BindView(R.id.sd_xb)
        SimpleDraweeView sd_xb;

        @BindView(R.id.textView44)
        TextView tv_name;
        @BindView(R.id.textView45)
        TextView tv_pl;
        @BindView(R.id.textView46)
        TextView tv_time;
        @BindView(R.id.tv_msg)
        TextView tv_msg;

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
