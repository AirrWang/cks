package com.ckjs.ck.Android.MineModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ckjs.ck.Android.CoachModule.Activity.DuoJIeKeZhanKaiActivity;
import com.ckjs.ck.Android.MineModule.Activity.PeopleSureOrderActivity;
import com.ckjs.ck.Android.MineModule.Activity.PingJiaSiJiaoActivity;
import com.ckjs.ck.Bean.MyCoachOrderBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterMineSjOrderList extends BaseAdapter {


    private List<MyCoachOrderBean.MyCoachOrderInfoBean> list;
    private Context context;

    public ListAdapterMineSjOrderList(Context context) {
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
                    R.layout.item_list_mine_sijiao_order, null);
            myHolder = new MyHolder(view);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        if (list.get(position).getPicture()!=null) {
            FrescoUtils.setImage(myHolder.mine_sijiao_list, AppConfig.url_jszd + list.get(position).getPicture());
        }
        if (list.get(position).getSex()!=null){
        if (list.get(position).getSex().equals("2")) {
            FrescoUtils.setImage(myHolder.mine_sijiao_list_sex, AppConfig.res + R.drawable.my_girl);
        } else if (list.get(position).getSex().equals("1")) {
            FrescoUtils.setImage(myHolder.mine_sijiao_list_sex, AppConfig.res + R.drawable.my_boy);
        } else {
            FrescoUtils.setImage(myHolder.mine_sijiao_list_sex, "");
        }}
        if (list.get(position).getName()!=null) {
            myHolder.mine_sijiao_list_name.setText(list.get(position).getName());
        }
        if (list.get(position).getType()!=null) {
            if (list.get(position).getType().equals("distance")) {
                myHolder.mine_sijiao_list_type.setText("视频指导");
            } else {
                myHolder.mine_sijiao_list_type.setText("上门指导");
            }
        }
        if (list.get(position).getId()!=null) {
            myHolder.tv_mine_sijiao_orderid.setText(list.get(position).getId());
        }
        if (list.get(position).getNum()!=null) {
            MoudleUtils.textViewSetText(myHolder.tv_mine_sijiao_jieshu, list.get(position).getNum() + "节课");
        }
        if (list.get(position).getAmount()!=null) {
            MoudleUtils.textViewSetText(myHolder.mine_sijiao_list_money, "￥" + list.get(position).getAmount());
        }
        String s_tv_zt = "";
        String s_btn_zt = "";
        //status|订单状态|1：教练未确认；2：教练已确认；3：用户已支付；4：课程已完成；5：已评论完成
        String status = "";
        status = list.get(position).getStatus();
        if (status == null) {
            status = "";
        }
        switch (status) {
            case "1":
                s_tv_zt = "教练未确认";
                s_btn_zt = "待确认";
                initSetFose(myHolder);
                break;
            case "2":
                s_tv_zt = "教练已确认";
                s_btn_zt = "去支付";
                initSetTrue(myHolder);


                break;
            case "3":
                s_tv_zt = "用户已支付";
                s_btn_zt = "去上课";
                initSetTrue(myHolder);
                final String finalStatus1 = status;
                myHolder.tv_mine_sijiao_jieshu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (finalStatus1.equals("3")) {
                            Intent intent = new Intent();
                            intent.putExtra("type", "1");//1客户2教
                            intent.putExtra("id", list.get(position).getId());
                            context.startActivity(intent.setClass(context, DuoJIeKeZhanKaiActivity.class));
                        }
                    }
                });
                MoudleUtils.textViewSetText(myHolder.tv_mine_sijiao_jieshu, list.get(position).getNum() + "节课(点我去上课)");

                break;
            case "4":
                s_tv_zt = "课程已完成";
                s_btn_zt = "去评价";
                initSetTrue(myHolder);


                break;
            case "5":
                s_tv_zt = "已评论完成";
                s_btn_zt = "已评价";
                initSetFose(myHolder);

                break;
            case "-1":
                s_tv_zt = "教练已拒绝";
                s_btn_zt = "已拒绝";
//                initSetFose(myHolder);
                initSetTrue(myHolder);
                break;
            default:
                break;
        }
        myHolder.mine_sijiao_list_status.setText(s_tv_zt);
        myHolder.button.setText(s_btn_zt);
        final String finalStatus = status;
        myHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (finalStatus) {
                    case "2":
                        sure(position);
                        break;
                    case "3":
                        sureToSk(position);
                        break;
                    case "4":
                        sureToPj(position);
                        break;
                    case "-1":
                        sureToJuJue(position);
                        break;
                    default:
                        break;
                }

            }

        });
        return view;
    }


    AlertDialog.Builder builder;
    AlertDialog alert;

    private void sureToJuJue(int position) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("拒绝原因");
        String s_o = list.get(position).getObjection();
        if (s_o == null) {
            s_o = "";
        }
        builder.setMessage(s_o);

        alert = builder.create();
        if (!alert.isShowing()) {
            alert.show();
        }
    }


    private void sureToPj(int p) {
        Intent intent = new Intent();
        if (list!=null){
            if (list.size()>0){
                intent.putExtra("id",list.get(p).getId());
                intent.setClass(context, PingJiaSiJiaoActivity.class);
                context.startActivity(intent);
            }
        }

    }

    private void initSetFose(MyHolder myHolder) {
        myHolder.button.setEnabled(false);
        myHolder.button.setTextColor(context.getResources().getColor(R.color.c_ffffff));
        myHolder.button.setBackgroundResource(R.color.c_99);
    }

    private void initSetTrue(MyHolder myHolder) {
        myHolder.button.setEnabled(true);
        myHolder.button.setTextColor(context.getResources().getColor(R.color.c_ffffff));
        myHolder.button.setBackgroundResource(R.color.fea21249);

    }

    private void sure(int p) {
        Intent intent = new Intent();
        intent.putExtra("id", list.get(p).getId());
        intent.putExtra("sijiaotype", list.get(p).getType());
        intent.putExtra("name", list.get(p).getName());
        intent.putExtra("price", list.get(p).getAmount());
        intent.putExtra("sex", list.get(p).getSex());
        intent.putExtra("status", list.get(p).getStatus());
        intent.setClass(context, PeopleSureOrderActivity.class);
        context.startActivity(intent);
    }

    private void sureToSk(int p) {
        Intent intent = new Intent();
        intent.putExtra("type", "1");
        intent.putExtra("id", list.get(p).getId());
        context.startActivity(intent.setClass(context, DuoJIeKeZhanKaiActivity.class));
    }

    public void setList(List<MyCoachOrderBean.MyCoachOrderInfoBean> list) {
        this.list = list;
    }


    class MyHolder {
        @BindView(R.id.btn_sure)
        Button button;

        @BindView(R.id.mine_sijiao_list)
        SimpleDraweeView mine_sijiao_list;

        @BindView(R.id.mine_sijiao_list_sex)
        SimpleDraweeView mine_sijiao_list_sex;

        @BindView(R.id.mine_sijiao_list_name)
        TextView mine_sijiao_list_name;

        @BindView(R.id.mine_sijiao_list_type)
        TextView mine_sijiao_list_type;

        @BindView(R.id.mine_sijiao_list_status)
        TextView mine_sijiao_list_status;

        @BindView(R.id.tv_mine_sijiao_orderid)
        TextView tv_mine_sijiao_orderid;
        @BindView(R.id.tv_mine_sijiao_jieshu)
        TextView tv_mine_sijiao_jieshu;
        @BindView(R.id.mine_sijiao_list_money)
        TextView mine_sijiao_list_money;

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }


    }


}
