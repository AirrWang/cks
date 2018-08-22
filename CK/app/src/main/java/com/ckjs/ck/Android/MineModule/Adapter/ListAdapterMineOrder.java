package com.ckjs.ck.Android.MineModule.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.HomeShopH5Activity;
import com.ckjs.ck.Android.HomeModule.Activity.HomeStoreCardPayActivity;
import com.ckjs.ck.Android.HomeModule.Activity.HomeStoreCoachPayActivity;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetMyOrderBean;
import com.ckjs.ck.Bean.ReceiptBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterMineOrder extends BaseAdapter {

    private List<GetMyOrderBean.GetMyOrderInfoBean> list;
    private Context context;
    private AlertDialog alertDialog;
    private ProgressDialog dialog;


    public ListAdapterMineOrder(Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        MyHolder myHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_list_mine_order, null);
            myHolder = new MyHolder();
            myHolder.mine_shoporder_pic = (SimpleDraweeView) view.findViewById(R.id.mine_shoporder_pic);
            myHolder.mine_shoporder_name = (TextView) view.findViewById(R.id.mine_shoporder_name);
            myHolder.mine_shoporder_price = (TextView) view.findViewById(R.id.mine_shoporder_price);
            myHolder.mine_order_status = (TextView) view.findViewById(R.id.mine_order_status);
            myHolder.mine_order_time = (TextView) view.findViewById(R.id.mine_order_time);
            myHolder.order_ck_id = (TextView) view.findViewById(R.id.order_ck_id);
            myHolder.myorder_express = (TextView) view.findViewById(R.id.myorder_express);
            myHolder.myorder_expressid = (TextView) view.findViewById(R.id.myorder_expressid);
            myHolder.order_recipet = (TextView) view.findViewById(R.id.order_recipet);
            myHolder.order_repay = (TextView) view.findViewById(R.id.order_repay);
            myHolder.order_delete = (SimpleDraweeView) view.findViewById(R.id.order_delete);
            myHolder.ll_mine_order_bottom= (LinearLayout) view.findViewById(R.id.ll_mine_order_bottom);
            myHolder.ll_mine_order_express= (LinearLayout) view.findViewById(R.id.ll_mine_order_express);
            myHolder.tv_mine_order_bottom= (TextView) view.findViewById(R.id.tv_mine_order_bottom);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        if (list.get(position).getPicture()!=null) {
            FrescoUtils.setImage(myHolder.mine_shoporder_pic, AppConfig.url_jszd + list.get(position).getPicture());
        }
        if (list.get(position).getShopname()!=null) {
            MoudleUtils.textViewSetText(myHolder.mine_shoporder_name, list.get(position).getShopname());
        }
        if (list.get(position).getCreattime()!=null) {
            MoudleUtils.textViewSetText(myHolder.mine_order_time, list.get(position).getCreattime());
        }
        if (list.get(position).getOrder_id()!=null) {
            MoudleUtils.textViewSetText(myHolder.order_ck_id, list.get(position).getOrder_id());
        }
        //商城商品
        if (list.get(position).getShop_type()!=null) {
            if (list.get(position).getShop_type().equals("1")) {
                if (list.get(position).getAmount() != null) {
                    MoudleUtils.textViewSetText(myHolder.mine_shoporder_price, "共1件商品 实付款：" + list.get(position).getAmount());
                }
                if (list.get(position).getOrderstatus() != null) {
                    if (list.get(position).getOrderstatus().equals("1")) {
                        MoudleUtils.textViewSetText(myHolder.mine_order_status, "等待发货");
                        initShow(myHolder);
                    } else if (list.get(position).getOrderstatus().equals("2")) {
                        MoudleUtils.textViewSetText(myHolder.mine_order_status, "等待收货");
                        initShow(myHolder);
                    } else if (list.get(position).getOrderstatus().equals("3")) {
                        MoudleUtils.textViewSetText(myHolder.mine_order_status, "订单完成");
                        initgone(myHolder);
                    }
                }
                if (list.get(position).getOrder_id()!=null) {
                    initToSureOrder(position, myHolder);
                }
                if (list.get(position).getShop_id()!=null) {
                    initToRePay(position, myHolder);
                }
            } else {
                initShow(myHolder);
                //非商城商品
                MoudleUtils.viewShow(myHolder.tv_mine_order_bottom);
                MoudleUtils.textViewSetText(myHolder.mine_order_status, "");
                MoudleUtils.viewGone(myHolder.ll_mine_order_express);
                if (list.get(position).getShop_type().equals("2")) {
                    MoudleUtils.textViewSetText(myHolder.mine_shoporder_price, "共" + list.get(position).getNum() + "张卡 实付款：" + list.get(position).getAmount());
                    initToRePayCard(myHolder, list.get(position).getShopname(), list.get(position).getShop_id());

                } else if (list.get(position).getShop_type().equals("3")) {
                    MoudleUtils.textViewSetText(myHolder.mine_shoporder_price, "共" + list.get(position).getNum() + "节课程 实付款：" + list.get(position).getAmount());
                    initToRePaySJ(myHolder, list.get(position).getShopname(), list.get(position).getShop_id());
                }
                if (list.get(position).getOrderstatus()!=null) {
                    if (list.get(position).getOrderstatus().equals("1")) {
                        MoudleUtils.textViewSetText(myHolder.mine_order_status, "等待发货");
                        initShow(myHolder);
                    } else if (list.get(position).getOrderstatus().equals("2")) {
                        MoudleUtils.textViewSetText(myHolder.mine_order_status, "等待收货");
                        initShow(myHolder);
                    } else if (list.get(position).getOrderstatus().equals("3")) {
                        MoudleUtils.textViewSetText(myHolder.mine_order_status, "订单完成");
                        initgone(myHolder);
                    }
                }
                if (list.get(position).getOrder_id()!=null) {
                    initToSure(position, myHolder);
                }
            }
        }
        if (list.get(position).getExpressid() != null) {
            if (!list.get(position).getExpressid().equals("")) {
                MoudleUtils.textViewSetText(myHolder.myorder_expressid, list.get(position).getExpressid());
                MoudleUtils.textViewSetText(myHolder.myorder_express, list.get(position).getExpress());
            } else {
                initSetIdNull(myHolder);
            }
        } else {
            initSetIdNull(myHolder);
        }
        if (list.get(position).getOrder_id()!=null) {
            initDeleteOrder(position, myHolder);
        }

        return view;
    }

    /**
     * 删除订单弹窗
     *
     * @param position
     * @param myHolder
     */
    private void initDeleteOrder(final int position, MyHolder myHolder) {
        myHolder.order_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(context).setTitle("一经删除不可更改")
                        .setMessage("真的要删除此订单吗？")
                        .setPositiveButton("确定删除", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                initToDeleteTask(position);
                            }
                        }).setNegativeButton("暂不删除",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create(); // 创建对话框
                alertDialog.setCancelable(false);
                if (alertDialog != null) {
                    if (!alertDialog.isShowing()) {
                        alertDialog.show(); // 显示
                    }
                }
            }

        });
    }

    /**
     * 删除接口
     *
     * @param position
     */

    private void initToDeleteTask(final int position) {
        dialog = new ProgressDialog(context);
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(context, "user_id", 0));
        String token = (String) SPUtils.get(context, "token", "");

        Call<ReceiptBean> callBack = restApi.delorder(user_id, token, list.get(position).getOrder_id());
        callBack.enqueue(new Callback<ReceiptBean>() {
            @Override
            public void onResponse(Call<ReceiptBean> call, Response<ReceiptBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        list.remove(position);

                        notifyDataSetChanged();
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(context, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.show(context, response.body().getMsg(), 0);
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<ReceiptBean> call, Throwable t) {
                MoudleUtils.toChekWifi(context);
                MoudleUtils.dialogDismiss(dialog);
            }
        });

    }

    /**
     * 再次购买跳转商品
     *
     * @param position
     * @param myHolder
     */
    private void initToRePay(final int position, MyHolder myHolder) {
        myHolder.order_repay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String id = list.get(position).getShop_id();
                String acurl = "http://www.chaokongs.com/shop/shopinfo?id=" + id;
                intent.setClass(context, HomeShopH5Activity.class);
                intent.putExtra("acurl", acurl);
                context.startActivity(intent);
            }
        });
    }
    /**
     * 再次购买跳转教练
     *
     * @param myHolder
     */
    private void initToRePaySJ(MyHolder myHolder, final String sj_name, final String sijiao_id) {
        myHolder.order_repay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("name", sj_name);
                intent.putExtra("id", sijiao_id);
                intent.setClass(context, HomeStoreCoachPayActivity.class);
                context.startActivity(intent);
            }
        });
    }

    /**
     * 再次购买跳转会员卡
     *
     * @param myHolder
     */
    private void initToRePayCard( MyHolder myHolder, final String name, final String id) {
        myHolder.order_repay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("name", name);
                intent.putExtra("id",id);
                intent.setClass(context, HomeStoreCardPayActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void initShow(MyHolder myHolder) {
        if (myHolder.order_recipet.getVisibility() != View.VISIBLE) {
            myHolder.order_recipet.setText("确认收货");
            myHolder.order_recipet.setVisibility(View.VISIBLE);
        }
    }

    private void initgone(MyHolder myHolder) {
        if (myHolder.order_recipet.getVisibility() == View.VISIBLE) {
            myHolder.order_recipet.setText("");
            myHolder.order_recipet.setVisibility(View.GONE);
        }
    }

    private void initSetIdNull(MyHolder myHolder) {
        myHolder.myorder_expressid.setText("");
        myHolder.myorder_express.setText("");
    }

    /**
     * 弹窗确认收货
     *
     * @param position
     * @param myHolder
     */
    private void initToSureOrder(final int position, final MyHolder myHolder) {
        myHolder.order_recipet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(context)
//                        .setTitle("确认后不可更改")
                        .setMessage("确认后不可更改")
                        .setPositiveButton("确定收货", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                initToInfoTask(position);
                            }
                        }).setNegativeButton("暂不确认",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create(); // 创建对话框
                alertDialog.setCancelable(false);
                if (alertDialog != null) {
                    if (!alertDialog.isShowing()) {
                        alertDialog.show(); // 显示
                    }
                }
            }

        });
    }

    /**
     * 弹窗确认收货
     *
     * @param position
     * @param myHolder
     */
    private void initToSure(final int position, final MyHolder myHolder) {
        myHolder.order_recipet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(context)
//                        .setTitle("确认后不可更改")
                        .setMessage("已确认在门店完成交易")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                initToInfoTask(position);
                            }
                        }).setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create(); // 创建对话框
                alertDialog.setCancelable(false);
                if (alertDialog != null) {
                    if (!alertDialog.isShowing()) {
                        alertDialog.show(); // 显示
                    }
                }
            }

        });
    }

    /**
     * 确认收货 接口
     *
     * @param position
     */

    private void initToInfoTask(final int position) {
        dialog = new ProgressDialog(context);
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(context, "user_id", 0));
        String token = (String) SPUtils.get(context, "token", "");

        Call<ReceiptBean> callBack = restApi.receipt(user_id, token, list.get(position).getOrder_id());
        callBack.enqueue(new Callback<ReceiptBean>() {
            @Override
            public void onResponse(Call<ReceiptBean> call, Response<ReceiptBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
//                        initgone(finalMyHolder);
//                        finalMyHolder.mine_order_status.setText("已完成");
                        list.get(position).setOrderstatus("3");

                        notifyDataSetChanged();
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(context, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(context,response.body().getMsg());

                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<ReceiptBean> call, Throwable t) {
                MoudleUtils.toChekWifi(context);
                MoudleUtils.dialogDismiss(dialog);
            }
        });
    }

    public void setList(List<GetMyOrderBean.GetMyOrderInfoBean> list) {
        this.list = list;
    }


    class MyHolder {
        SimpleDraweeView mine_shoporder_pic;
        TextView mine_shoporder_name;
        TextView mine_shoporder_price;
        TextView mine_order_status;
        TextView order_ck_id;
        TextView mine_order_time;
        TextView myorder_express;
        TextView myorder_expressid;
        TextView order_recipet;
        TextView order_repay;
        SimpleDraweeView order_delete;
        LinearLayout ll_mine_order_bottom;
        LinearLayout ll_mine_order_express;
        TextView tv_mine_order_bottom;
    }


}
