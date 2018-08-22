package com.ckjs.ck.Android.CkCircleModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ckjs.ck.Manager.NotifyCkToDongTaiPrcDeleteMessageManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GridAdapterCkCircleDongTai extends BaseAdapter {
    private List<String> list;
    private Context context;

    public GridAdapterCkCircleDongTai(Context context) {
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
                    R.layout.item_grid_ckcircle_dong_tai, null);
            myHodler = new MyHodler();
            myHodler.iv_grid_circle_mine = (SimpleDraweeView) view
                    .findViewById(R.id.iv_grid_circle_mine);
            myHodler.tv_delete = (TextView) view
                    .findViewById(R.id.tv_delete);
            view.setTag(myHodler);
        } else {
            myHodler = (MyHodler) view.getTag();

        }
        initData(position, myHodler);
        return view;
    }

//    ImgLoader presenter = new GlideImgLoader();

    private void initData(final int position, final MyHodler myHodler) {
        if (position == list.size() - 1) {
            if (myHodler.tv_delete != null) {
                if (myHodler.tv_delete.getVisibility() == View.VISIBLE) {
                    myHodler.tv_delete.setVisibility(View.GONE);
                }
            }
            FrescoUtils.setImage(myHodler.iv_grid_circle_mine, list.get(0));
        } else if (position < list.size() - 1) {
            if (list.get(position + 1) != null) {
                myHodler.tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position + 1);
                        NotifyCkToDongTaiPrcDeleteMessageManager.getInstance().sendNotifyActivityToFlag(true, list);
                        notifyDataSetChanged();
                    }
                });
                MoudleUtils.viewShow(myHodler.tv_delete);
//                presenter.onPresentImageNoW(myHodler.iv_grid_circle_mine, list.get(position + 1));

                File file = new File(list.get(position + 1).toString());
                if (file != null) {
                    if (file.exists()) {
                        FrescoUtils.setFrescoFileUri(myHodler.iv_grid_circle_mine, AppConfig.file + list.get(position + 1).toString());
                    } else {
                        ToastUtils.showShort(context,"第" + position + "张图片不存在，请删除");
                    }
                } else {
                    ToastUtils.showShort(context,"第" + position + "张图片不存在，请删除");
                }
            }
        }
    }

    public void setList(List<String> list) {
        this.list = list;
    }


    class MyHodler {
        SimpleDraweeView iv_grid_circle_mine;
        TextView tv_delete;
    }
}
