package com.ckjs.ck.Android.MineModule.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Android.HomeModule.Activity.PoiSearchOneJsfActivity;
import com.ckjs.ck.Android.MineModule.Activity.YuYueActivity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.AnalyseBean;
import com.ckjs.ck.Bean.BodyanalyseBean;
import com.ckjs.ck.Manager.NotifyInfoUpdateMessageManager;
import com.ckjs.ck.Manager.NotifyJpushManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;

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
public class ListAdapterYuYue extends BaseAdapter {

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    private String adr;

    public List<BodyanalyseBean.BodyanalyseBeanInfo.Date> getList() {
        return list;
    }


    private List<BodyanalyseBean.BodyanalyseBeanInfo.Date> list;
    private Context context;

    public ListAdapterYuYue(Context context) {
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
                    R.layout.item_list_yu_yue, null);
            myHolder = new MyHolder(view);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        final MyHolder finalMyHolder = myHolder;
        myHolder.ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCall(finalMyHolder);
            }
        });
        myHolder.ll_dw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDw(list.get(position).getLat(), list.get(position).getLon(), list.get(position).getName(), list.get(position).getTel(), list.get(position).getPlace());
            }
        });
        initData(myHolder, position);
        myHolder.btn_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTaskJspList(list.get(position).getGym_id());
            }
        });
        return view;
    }

    /**
     * 进行预约接口
     *
     * @param gymId
     */
    private KyLoadingBuilder builder;

    private void initTaskJspList(String gymId) {
        if (context != null) {
            builder = new KyLoadingBuilder(context);
        }
        MoudleUtils.kyloadingShow(builder);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        String token = "";
        token = (String) SPUtils.get("token", token);
        Call<AnalyseBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).
                analyse(token, user_id, gymId);
        getSignBeanCall.enqueue(new Callback<AnalyseBean>() {
            @Override
            public void onResponse(Call<AnalyseBean> call, Response<AnalyseBean> response) {
                AnalyseBean bean = response.body();
                if (bean != null) {
                    if (bean.getStatus().equals("1")) {
                        if (bean.getInfo()!=null) {
                            SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "bodyanalyse", bean.getInfo().getBodyanalyse());
                            NotifyJpushManager.getInstance().sendNotifyJpushFlag(true, bean.getInfo().getBodyanalyse(), "yuyued");
                            initStatusTwo(bean.getInfo().getTime());
                        }
                    } else {
                        ToastUtils.showShort(context,bean.getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<AnalyseBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                MoudleUtils.kyloadingDismiss(builder);
            }
        });

    }

    private AlertDialog alertDialog;

    private void initStatusTwo(String time) {
        try {

            alertDialog = new AlertDialog.Builder(context).setTitle("请于一周内前往门店进行体测")
                    .setMessage("有效期：\n\n" + time + "\n\n失效请重新预约")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            NotifyInfoUpdateMessageManager.getInstance().sendNotify(true);
                            ((YuYueActivity) (context)).finish();
                        }

                    }).create(); // 创建对话框
            alertDialog.setCancelable(false);
            if (alertDialog != null) {
                if (!alertDialog.isShowing()) {
                    alertDialog.show(); // 显示
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void goToDw(String lat, String lon, String endNodeStr, String tel, String adr) {
        if (NetworkUtils.isNetworkAvailable()) {
            Intent intent = new Intent();
            if (adr != null && endNodeStr != null && lat != null && lon != null) {
                if (!adr.equals("") && !endNodeStr.equals("") && !lat.equals("") && !lon.equals("")) {
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    intent.putExtra("adr", adr);
                    intent.putExtra("tel", tel);
                    intent.putExtra("endNodeStr", endNodeStr);
                    intent.setClass(context, PoiSearchOneJsfActivity.class);
                    context.startActivity(intent);
                }
            }
        } else {
            MoudleUtils.toChekWifi();
        }
    }

    private void initData(MyHolder myHolder, int position) {
        MoudleUtils.textViewSetText(myHolder.tv_call, list.get(position).getTel());
        MoudleUtils.textViewSetText(myHolder.tv_adr, list.get(position).getPlace());
        MoudleUtils.textViewSetText(myHolder.tv_name, list.get(position).getName());
        MoudleUtils.textViewSetText(myHolder.tv_distance_m, "距离："+list.get(position).getDistance());


    }

    public void goToCall(final MyHolder myHolder) {
        if (!myHolder.tv_call.getText().toString().trim().equals("")) {
            new AlertDialog.Builder(context).setTitle("联系电话")
                    .setMessage(myHolder.tv_call.getText().toString().trim())
                    .setPositiveButton("拨打", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initToCall(myHolder);

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    private void initToCall(MyHolder myHolder) {
        try {
            String mobile = myHolder.tv_call.getText().toString().trim();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShortNotInternet("没有相关应用");
        }
    }

    public void setList(List<BodyanalyseBean.BodyanalyseBeanInfo.Date> list) {
        this.list = list;
    }


    class MyHolder {
        @BindView(R.id.tv_call)
        TextView tv_call;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_adr)
        TextView tv_adr;
        @BindView(R.id.ll_call)
        LinearLayout ll_call;
        @BindView(R.id.ll_dw)
        LinearLayout ll_dw;
        @BindView(R.id.btn_yuyue)
        Button btn_yuyue;
        @BindView(R.id.tv_distance_m)
        TextView tv_distance_m;
        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
