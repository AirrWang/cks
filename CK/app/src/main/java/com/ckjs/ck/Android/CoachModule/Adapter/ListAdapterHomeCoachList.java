package com.ckjs.ck.Android.CoachModule.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ckjs.ck.Android.CoachModule.Activity.DuoJIeKeZhanKaiActivity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.OrderBean;
import com.ckjs.ck.Manager.NotifyActivityCoachToMessageManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterHomeCoachList extends BaseAdapter {

    private List<OrderBean.OrderBeanInfo> list;
    private Context context;
    private ProgressDialog dilag;

    public SwipeRefreshLayout getSwipeRefreshLayoutTj() {
        return swipeRefreshLayoutTj;
    }

    public void setSwipeRefreshLayoutTj(SwipeRefreshLayout swipeRefreshLayoutTj) {
        this.swipeRefreshLayoutTj = swipeRefreshLayoutTj;
    }

    private SwipeRefreshLayout swipeRefreshLayoutTj;

    public ListAdapterHomeCoachList(Context context) {
        super();
        this.context = context;
        dilag = new ProgressDialog(context);
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        MyHolder myHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_list_coach_home_list, null);
            myHolder = new MyHolder(view);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }


        FrescoUtils.setImage(myHolder.custom_icon, AppConfig.url + list.get(position).getPicurl());
        MoudleUtils.textViewSetText(myHolder.tv_di_zhi, list.get(position).getAdress());
        MoudleUtils.textViewSetText(myHolder.tv_name, list.get(position).getRelname());
        MoudleUtils.textViewSetText(myHolder.tv_time, list.get(position).getHandler());
        MoudleUtils.textViewSetText(myHolder.tv_num, list.get(position).getNum() + "节课");
        MoudleUtils.textViewSetText(myHolder.tv_order_id, "订单号：   " + list.get(position).getId());

        String sex = list.get(position).getSex();
        if (sex != null) {
            if (sex.equals("1")) {
                FrescoUtils.setImage(myHolder.custom_sex, AppConfig.res + R.drawable.my_boy);
            } else if (sex.equals("2")) {
                FrescoUtils.setImage(myHolder.custom_sex, AppConfig.res + R.drawable.my_girl);
            }
        }
        String tv_type = list.get(position).getType();
        if (tv_type != null) {
            if (tv_type.equals("distance")) {
                MoudleUtils.textViewSetText(myHolder.tv_zhi_dao, "视频指导");
            } else if (tv_type.equals("personal")) {
                MoudleUtils.textViewSetText(myHolder.tv_zhi_dao, "上门指导");
            }
        }
        String status = list.get(position).getStatus();
        final String fistatus = status;

        if (status != null) {

            if (status.equals("1")) {
                myHolder.btn_jie_dan.setEnabled(true);
                myHolder.btn_ju_jue.setEnabled(true);
                FrescoUtils.setImage(myHolder.sd_j_t, AppConfig.res + R.drawable.home_arow);
                myHolder.btn_jie_dan.setText("接单");
                myHolder.btn_ju_jue.setText("拒绝");
                initSetBtnTvCorclor(myHolder);
            } else if (status.equals("2") || status.equals("3")) {
                FrescoUtils.setImage(myHolder.sd_j_t, AppConfig.res + R.drawable.home_arow);
                myHolder.btn_jie_dan.setEnabled(false);
                myHolder.btn_jie_dan.setText("已接单");
                myHolder.btn_ju_jue.setEnabled(false);
                myHolder.btn_ju_jue.setText("未拒绝");
                initSetBtnTvCorclor(myHolder);

                if (status.equals("3")) {
                    MoudleUtils.textViewSetText(myHolder.tv_num, list.get(position).getNum() + "节课(点我去上课)");
                }


            } else {
                if (status.equals("-1")) {
                    myHolder.btn_ju_jue.setText("已拒绝");
                    myHolder.btn_jie_dan.setText("未接单");
                } else {
                    myHolder.btn_ju_jue.setText("未拒绝");
                    myHolder.btn_jie_dan.setText("已接单");
                }

                FrescoUtils.setImage(myHolder.sd_j_t, "");
                myHolder.btn_jie_dan.setEnabled(false);
                myHolder.btn_ju_jue.setEnabled(false);
                initSetBtnTvCorclor(myHolder);
            }
        }
        final int fiP = position;

        myHolder.tv_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fistatus != null) {
                    if (fistatus.equals("3")) {
                        try {
                            Intent intent = new Intent();
                            intent.putExtra("type", "2");

                            intent.putExtra("id", list.get(fiP).getId());

                            context.startActivity(intent.setClass(context, DuoJIeKeZhanKaiActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                }
            }
        });
        final MyHolder finalMyHolder1 = myHolder;
        myHolder.btn_jie_dan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toJieDan(fiP, finalMyHolder1);

            }
        });
        final MyHolder finalMyHolder = myHolder;
        myHolder.btn_ju_jue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReport(fiP, finalMyHolder);
            }
        });
        return view;
    }

    private void initSetBtnTvCorclor(MyHolder myHolder) {
        myHolder.btn_jie_dan.setTextColor(context.getResources().getColor(R.color.c_ffffff));
        myHolder.btn_ju_jue.setTextColor(context.getResources().getColor(R.color.c_ffffff));
    }

    private void initJieDanTask(final int position, final MyHolder myHolder) {
        String id = list.get(position).getId();
        if (id == null) {
            id = "";
        }
        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        Call<AcceptBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).accept(token, user_id, id);
        getSignBeanCall.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                AcceptBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        initRefrsh();

                    }
                    ToastUtils.showShort(context,bean.getMsg());
                }
                MoudleUtils.dialogDismiss(dilag);
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dilag);

            }
        });
    }

    /**
     * 自动刷新
     */
    private void initRefrsh() {
        if (swipeRefreshLayoutTj != null) {
            swipeRefreshLayoutTj.setRefreshing(true);
            NotifyActivityCoachToMessageManager.getInstance().sendNotifyActivityToFlag(true);
        }
    }


    private void initJuJueTask(final int position, String reportReson, final MyHolder myHolder) {
        String id = list.get(position).getId();
        if (id == null) {
            id = "";
        }
        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        Call<AcceptBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).refuse(token, user_id, id, reportReson);
        getSignBeanCall.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                AcceptBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        initRefrsh();
                    }
                    ToastUtils.showShort(context,bean.getMsg());
                }
                MoudleUtils.dialogDismiss(dilag);
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.dialogDismiss(dilag);
            }
        });
    }

    /**
     * 举报弹框
     */
    private EditText input;

    private void toReport(final int position, final MyHolder myHolder) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("请输入拒绝理由(如距离太远，已上课中等)");

        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_report_input, null);
        builder.setView(view);

        builder.setPositiveButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input = (EditText) view.findViewById(R.id.et_dialog_report_input);
                String reportReson = input.getText().toString().trim();
                if (reportReson == null || reportReson.equals("")) {
                    ToastUtils.showShort(context,"拒绝理由不能为空");
                } else {
                    if (!DataUtils.containsEmoji(reportReson)) {
                        MoudleUtils.dialogShow(dilag);
                        initJuJueTask(position, reportReson, myHolder);
                    } else {
                        ToastUtils.showShort(context,"不支持输入Emoji表情符号");
                    }
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    AlertDialog.Builder builder;
    AlertDialog alert;

    private void toJieDan(final int position, final MyHolder myHolder) {
        builder = new AlertDialog.Builder(context);
        builder.setMessage("您是否确认接单：");


        builder.setPositiveButton("接单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MoudleUtils.dialogShow(dilag);
                initJieDanTask(position, myHolder);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert = builder.create();
        if (!alert.isShowing()) {
            alert.show();
        }
    }

    public void setList(List<OrderBean.OrderBeanInfo> list) {
        this.list = list;
    }


    class MyHolder {
        @BindView(R.id.custom_icon)
        SimpleDraweeView custom_icon;
        @BindView(R.id.sd_j_t)
        SimpleDraweeView sd_j_t;
        @BindView(R.id.tv_di_zhi)
        TextView tv_di_zhi;
        @BindView(R.id.custom_sex)
        SimpleDraweeView custom_sex;
        @BindView(R.id.tv_zhi_dao)
        TextView tv_zhi_dao;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.btn_jie_dan)
        Button btn_jie_dan;
        @BindView(R.id.btn_ju_jue)
        Button btn_ju_jue;
        @BindView(R.id.tv_num)
        TextView tv_num;
        @BindView(R.id.tv_order_id)
        TextView tv_order_id;


        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
