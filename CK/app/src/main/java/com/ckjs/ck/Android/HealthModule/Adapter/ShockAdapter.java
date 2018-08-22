package com.ckjs.ck.Android.HealthModule.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import static com.ckjs.ck.Android.HealthModule.Activity.ShockActivity.ky;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ShockAdapter extends BaseExpandableListAdapter {
    private Context content;
    public String[] groupStrings = {"来电通知", "短信通知", "久坐提醒", "闹钟设置", "目标设置", "双向防丢"};
    public String[][] childStrings = {
            {""},
            {""},
            {"时长"},
            {"00:00"},
            {""},
            {"设置米数"}
    };

    public List<String> i_icons = new ArrayList<>();


    public ShockAdapter() {
        super();
        i_icons.add(R.drawable.remind_phone + "");
        i_icons.add(R.drawable.remind_note + "");
        i_icons.add(R.drawable.remind_sit + "");
        i_icons.add(R.drawable.remind_clock + "");
        i_icons.add(R.drawable.remind_target + "");
        i_icons.add(R.drawable.remind_find + "");
    }

    public Context getContent() {
        return content;
    }

    public void setContent(Context content) {
        this.content = content;
    }


    //        获取分组的个数
    @Override
    public int getGroupCount() {
        return groupStrings.length;
    }

    //        获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childStrings[groupPosition].length;
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupStrings[groupPosition];
    }

    //        获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childStrings[groupPosition][childPosition];
    }

    //        获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //        获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //        分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //        获取显示指定分组的视图
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(content).inflate(R.layout.el_grop, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_name);
            groupViewHolder.icons = (SimpleDraweeView) convertView.findViewById(R.id.sd_icon);
            groupViewHolder.icons_jt = (SimpleDraweeView) convertView.findViewById(R.id.sd_jian_tou);
            groupViewHolder.sw_xduan_ze = (Switch) convertView.findViewById(R.id.sw_xduan_ze);
            groupViewHolder.choose_clock = (TextView) convertView.findViewById(R.id.choose_clock);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupStrings[groupPosition]);
        FrescoUtils.setImage(groupViewHolder.icons, AppConfig.res + i_icons.get(groupPosition));
        if (groupPosition == 3) {
            MoudleUtils.viewShow(groupViewHolder.icons_jt);
            MoudleUtils.viewGone(groupViewHolder.sw_xduan_ze);

        } else {
            MoudleUtils.viewShow(groupViewHolder.sw_xduan_ze);
            MoudleUtils.viewGone(groupViewHolder.icons_jt);
        }
        if (groupPosition == 1 || groupPosition == 0 || groupPosition == 4) {
            groupViewHolder.sw_xduan_ze.setFocusable(true);
        } else {
            groupViewHolder.sw_xduan_ze.setFocusable(false);
            groupViewHolder.sw_xduan_ze.setClickable(true);

        }
        if (isExpanded) {
            FrescoUtils.setImage(groupViewHolder.icons_jt, AppConfig.res + R.drawable.down_up);
        } else {
            FrescoUtils.setImage(groupViewHolder.icons_jt, AppConfig.res + R.drawable.personal_viewall);
        }
        if (groupPosition == 0) {
            groupViewHolder.sw_xduan_ze.setChecked((Boolean) SPUtils.get(content, "phoneShake", false));
        } else if (groupPosition == 1) {
            groupViewHolder.sw_xduan_ze.setChecked((Boolean) SPUtils.get(content, "smsShake", false));
        } else if (groupPosition == 2) {
            groupViewHolder.sw_xduan_ze.setChecked((Boolean) SPUtils.get(content, "longtime", false));
        } else if (groupPosition == 4) {
            groupViewHolder.sw_xduan_ze.setChecked((Boolean) SPUtils.get(content, "goalset", false));
        } else if (groupPosition == 5) {
            groupViewHolder.sw_xduan_ze.setChecked((Boolean) SPUtils.get(content, "antilost", false));
        }

        groupViewHolder.sw_xduan_ze.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    if (groupPosition == 0) {
                        SPUtils.put(content, "phoneShake", true);
                    } else if (groupPosition == 1) {
                        SPUtils.put(content, "smsShake", true);
                    } else if (groupPosition == 2) {
                        if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                            if (!(Boolean) SPUtils.get(content, "longtime", false)) {
                                initShow();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x07, 0x05, 0x00, 0x00});
                                        SPUtils.put(content, "longtime", true);
                                    }
                                }, 1000);
                            }
                        } else {
                            groupViewHolder.sw_xduan_ze.setChecked(false);
                            ToastUtils.showShort(content,"手环未连接");
                        }
                    } else if (groupPosition == 5) {
                        SPUtils.put(content, "antilost", true);
                    } else if (groupPosition == 4) {
                        SPUtils.put(content, "goalset", true);
                    }
                } else {
                    if (groupPosition == 0) {
                        SPUtils.put(content, "phoneShake", false);
                    } else if (groupPosition == 1) {
                        SPUtils.put(content, "smsShake", false);
                    } else if (groupPosition == 2) {
                        if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                            if ((Boolean) SPUtils.get(content, "longtime", false)) {
                                initShow();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x07, 0x06, 0x00, 0x00});
                                        SPUtils.put(content, "longtime", false);
                                    }
                                }, 1000);
                            }
                        } else {
                            groupViewHolder.sw_xduan_ze.setChecked(true);
                            ToastUtils.showShort(content,"手环未连接");
                        }
                    } else if (groupPosition == 5) {
                        SPUtils.put(content, "antilost", false);
                    } else if (groupPosition == 4) {
                        SPUtils.put(content, "goalset", false);
                    }
                }
            }
        });
        return convertView;
    }

    //        获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(content).inflate(R.layout.item_expand_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_name_child);
            childViewHolder.tv_right_child = (TextView) convertView.findViewById(R.id.tv_right_child);
            childViewHolder.icons_jt_two = (SimpleDraweeView) convertView.findViewById(R.id.sd_jian_tou_two);
            childViewHolder.sw_xduan_ze_two = (Switch) convertView.findViewById(R.id.sw_xduan_ze_two);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (groupPosition == 0 || groupPosition == 2 || groupPosition == 5) {
            MoudleUtils.viewShow(childViewHolder.icons_jt_two);
            MoudleUtils.viewGone(childViewHolder.sw_xduan_ze_two);

        } else {
            MoudleUtils.viewShow(childViewHolder.sw_xduan_ze_two);
            MoudleUtils.viewGone(childViewHolder.icons_jt_two);
        }
        if (groupPosition == 2) {
            MoudleUtils.textViewSetText(childViewHolder.tv_right_child, SPUtils.get(content, "timeset", "") + "小时");
        } else if (groupPosition == 5) {
            MoudleUtils.textViewSetText(childViewHolder.tv_right_child, SPUtils.get(content, "losedistance", "3") + "米");
        } else {
            MoudleUtils.textViewSetText(childViewHolder.tv_right_child, " ");
        }
        if (groupPosition == 3) {
            MoudleUtils.textViewSetText(childViewHolder.tvTitle, SPUtils.get(content, "clockhour", "设置时间") + "");
        } else {
            childViewHolder.tvTitle.setText(childStrings[groupPosition][childPosition]);
        }
        childViewHolder.sw_xduan_ze_two.setChecked((Boolean) SPUtils.get(content, "clock", false));
        childViewHolder.sw_xduan_ze_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                        if (!(Boolean) SPUtils.get(content, "clock", false)) {
                            initShow();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x09, 0x01, 0x00, 0x00});
                                    SPUtils.put(content, "clock", true);
                                }
                            }, 1000);
                        }
                    } else {
                        childViewHolder.sw_xduan_ze_two.setChecked(false);
                        ToastUtils.showShort(content,"手环未连接");
                    }
                } else {
                    if (AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
                        if ((Boolean) SPUtils.get(content, "clock", false)) {
                            initShow();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    BluethUtils.displayGattServices(AppConfig.bluetoothGattServices, "fff1", new byte[]{0x09, 0x02, 0x00, 0x00});
                                    SPUtils.put(content, "clock", false);
                                }
                            }, 1000);
                        }
                    } else {
                        childViewHolder.sw_xduan_ze_two.setChecked(true);
                        ToastUtils.showShort(content,"手环未连接");
                    }
                }
            }
        });

        return convertView;
    }

    private void initShow() {
        ky = new KyLoadingBuilder(content);
        MoudleUtils.kyloadingShow(ky);
    }

    //        指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    static class GroupViewHolder {
        TextView tvTitle;
        SimpleDraweeView icons;
        SimpleDraweeView icons_jt;
        Switch sw_xduan_ze;
        TextView choose_clock;
    }

    static class ChildViewHolder {
        TextView tvTitle;
        TextView tv_right_child;
        SimpleDraweeView icons_jt_two;
        Switch sw_xduan_ze_two;
    }
}
