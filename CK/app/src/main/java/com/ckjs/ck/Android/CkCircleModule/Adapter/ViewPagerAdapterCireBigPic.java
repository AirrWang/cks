package com.ckjs.ck.Android.CkCircleModule.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Animatable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;
import java.util.List;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ViewPagerAdapterCireBigPic extends PagerAdapter {
    List<String> data;
    Context context;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type = "cir";

    public ViewPagerAdapterCireBigPic(List<String> data, Context context) {
        // TODO Auto-generated constructor stub
        this.data = data;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
//        position %= data.size();
//        View view = LayoutInflater.from(context).inflate(R.layout.vp_item, container, false);
//        SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.image);
//        FrescoUtils.setControllerListener(imageView, AppConfig.url + data.get(position).toString(),ScreenUtils.getScreenWidth(CkApplication.getInstance()));
//        imageView.setOnPhotoTapListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((Activity) (context)).finish();
//            }
//        });
//        container.addView(view);
//        return view;
        final PhotoDraweeView photoDraweeView = new PhotoDraweeView(container.getContext());
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        if (type.equals("cir")) {
            controller.setUri(AppConfig.url + data.get(position).toString());
        } else {
            controller.setUri(AppConfig.url_jszd + data.get(position).toString());
        }
        controller.setOldController(photoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        photoDraweeView.setController(controller.build());

        try {
            container.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                ((Activity) (context)).finish();
            }
        });
        photoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                initToSavePic(data.get(position));
                return true;
            }
        });

        return photoDraweeView;

    }

    private void initToSavePic(final String pic) {
        final CharSequence[] items = {"保存", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        initSave(pic);
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initSave(String picStr) {
        String filePath = AppConfig.hss_save
                + "/"
                + System.currentTimeMillis()
                + AppConfig.JPG;
        File imageFile = new File(filePath);
        if (type.equals("cir")) {
            MoudleUtils.savePic(imageFile, context, AppConfig.url + picStr, AppConfig.hss_save);
        }else {
            MoudleUtils.savePic(imageFile, context, AppConfig.url_jszd + picStr, AppConfig.hss_save);
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
        container.removeView((View) object);
    }


}
