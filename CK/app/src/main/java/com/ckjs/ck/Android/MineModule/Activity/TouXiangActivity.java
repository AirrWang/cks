package com.ckjs.ck.Android.MineModule.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class TouXiangActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private SimpleDraweeView pic;
    private LinearLayout ll_pic;
    private String picStr="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_icon);
        initId();

        Intent intent=getIntent();
        String type=intent.getStringExtra("type");
        picStr = intent.getStringExtra("touxiang");
        if (type.equals("1")){
            picStr= AppConfig.url_jszd+ picStr +"";
        }else {
            picStr = AppConfig.url + picStr + "";
        }
        FrescoUtils.setImage(pic, picStr);

    }

    private void initId() {
        pic = (SimpleDraweeView) findViewById(R.id.people_pic);
        ll_pic = (LinearLayout) findViewById(R.id.ll_people_pic);
        ll_pic.setOnClickListener(this);
        ll_pic.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_people_pic:
                finish();
                break;
        }
    }

    /**
     * 弹窗提示保存
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        initToSavePic();
        return true;
    }

    private void initToSavePic() {
        final CharSequence[] items = {"保存", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        initSave();
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initSave() {
        String filePath = AppConfig.hss_save
                + "/"
                + System.currentTimeMillis()
                + AppConfig.JPG;
        File imageFile = new File(filePath);
        MoudleUtils.savePic(imageFile,TouXiangActivity.this,picStr,AppConfig.hss_save);
    }




}
