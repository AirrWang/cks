package com.ckjs.ck.Android.MineModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Android.CkCircleModule.Activity.CircleIcBigActivity;
import com.ckjs.ck.Android.CkCircleModule.Activity.PoiSearchOneCirActivity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.GetMyfocusCircleBean;
import com.ckjs.ck.Manager.NotifyToMineAtentionAcFxManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ViewTool.GoodView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterMineFocusPeople extends BaseAdapter {

    private List<GetMyfocusCircleBean.GetMyfocusCircleInfoBean> list;
    private ViewHolder holder;
    private Context context;
    private Call<GetMyfocusCircleBean> listcall;
    private GoodView mGoodView;

    public ListAdapterMineFocusPeople(Context context) {
        this.context = context;
    }

    public void setList(List<GetMyfocusCircleBean.GetMyfocusCircleInfoBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = null;
        holder = null;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ckcircle_recommend_mian, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.textView_username = (TextView) view.findViewById(R.id.textView_username);
        holder.iv_circle_mine = (SimpleDraweeView) view.findViewById(R.id.iv_circle_mine);
        holder.textView_information = (TextView) view.findViewById(R.id.textView_information);
        holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
        holder.tv_dw = (TextView) view.findViewById(R.id.tv_dw);
        holder.grd_iv = (GridView) view.findViewById(R.id.grd_iv);
        holder.sdFenxiang = (SimpleDraweeView) view.findViewById(R.id.sdFenxiang);
        holder.sd_dw = (SimpleDraweeView) view.findViewById(R.id.sd_dw);
        holder.sd_xb = (SimpleDraweeView) view.findViewById(R.id.sd_xb);
        holder.tv_pl_one = (TextView) view.findViewById(R.id.tv_pl_one);
        holder.tv_pl_two = (TextView) view.findViewById(R.id.tv_pl_two);
        holder.tv_pl_more = (TextView) view.findViewById(R.id.tv_pl_more);
        holder.textView_username.setText(list.get(position).getUsername());
        holder.tv_time.setText(list.get(position).getTime());
        holder.tv_dz_num = (TextView) view.findViewById(R.id.tv_dz_num);
        holder.sd_dz = (SimpleDraweeView) view.findViewById(R.id.sd_dz);
        if (list.get(position).getPlace().equals(null)) {
//            holder.sd_dw.setVisibility(View.INVISIBLE);
            MoudleUtils.viewInvisibily(holder.sd_dw);
            holder.tv_dw.setText("");
        } else {
            if (list.get(position).getPlace().equals("")) {
//                holder.sd_dw.setVisibility(View.INVISIBLE);
                MoudleUtils.viewInvisibily(holder.sd_dw);
                holder.tv_dw.setText("");
            } else {
//                holder.sd_dw.setVisibility(View.VISIBLE);
                MoudleUtils.viewShow(holder.sd_dw);
                FrescoUtils.setImage(holder.sd_dw, AppConfig.res + R.drawable.attention_dingwei);
                holder.tv_dw.setText(list.get(position).getPlace());

            }
        }
        if (list.get(position).getContent().equals("")) {
//            holder.textView_information.setVisibility(View.GONE);
            MoudleUtils.viewGone(holder.textView_information);

        } else {
//            holder.textView_information.setVisibility(View.VISIBLE);
            MoudleUtils.viewShow(holder.textView_information);

            holder.textView_information.setText(list.get(position).getContent());
        }
        FrescoUtils.setImage(holder.iv_circle_mine, AppConfig.url + list.get(position).getPicurl(), ScreenUtils.getScreenWidth() / 4);
        initGridIv(position);
        initJump(position);
        initFenXiang(position);
        if (list.get(position).getSex().equals("2")) {
            FrescoUtils.setImage(holder.sd_xb, AppConfig.res + R.drawable.my_girl);
        } else if (list.get(position).getSex().equals("1")) {
            FrescoUtils.setImage(holder.sd_xb, AppConfig.res + R.drawable.my_boy);
        } else {
            FrescoUtils.setImage(holder.sd_xb, AppConfig.res + R.drawable.my_boy);
        }
        holder.sd_dw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDw(list.get(position).getPlace(), list.get(position).getLat(),
                        list.get(position).getLon());
            }
        });
        holder.tv_dw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDw(list.get(position).getPlace(), list.get(position).getLat(),
                        list.get(position).getLon());
            }
        });

        if (list.get(position).getComment() != null) {
            if (list.get(position).getComment().size() > 0) {
                switch (list.get(position).getComment().size()) {
                    case 0:
                        holder.tv_pl_one.setText("");
                        holder.tv_pl_two.setText("");
                        initTwoGoe();
                        break;
                    case 1:
                        initTvPl(holder.tv_pl_more, holder.tv_pl_one, position, 0);
//                        holder.tv_pl_two.setVisibility(View.GONE);
                        MoudleUtils.viewGone(holder.tv_pl_two);
                        holder.tv_pl_two.setText("");
                        break;
                    case 2:
                        initTvPl(holder.tv_pl_more, holder.tv_pl_one, position, 0);
                        initTvPl(holder.tv_pl_more, holder.tv_pl_two, position, 1);
                        break;
                }

            } else {
                holder.tv_pl_one.setText("");
                holder.tv_pl_two.setText("");
                initTwoGoe();
            }
        } else {
            holder.tv_pl_one.setText("");
            holder.tv_pl_two.setText("");
            initTwoGoe();
        }
        initDz(position, holder);
        initdz(position, holder.sd_dz);
        initdz(position, holder.tv_dz_num);
        return view;
    }

    private void initDz(int position, ViewHolder myHolder) {
        if (list.get(position).getIsthumb().equals("1")) {
            FrescoUtils.setImage(myHolder.sd_dz, AppConfig.res + R.drawable.good_color);
            myHolder.tv_dz_num.setTextColor(CkApplication.getInstance().getResources().getColor(R.color.fea21249));

        } else if (list.get(position).getIsthumb().equals("0")) {
            FrescoUtils.setImage(myHolder.sd_dz, AppConfig.res + R.drawable.good);
            myHolder.tv_dz_num.setTextColor(CkApplication.getInstance().getResources().getColor(R.color.c_be));

        }
        Log.i(TAG, "list.get(position).getIsthumb():：" + list.get(position).getIsthumb());

        if (list.get(position).getThumbsum() != null) {
            if (!list.get(position).getThumbsum().equals("")) {
                myHolder.tv_dz_num.setText(list.get(position).getThumbsum());


            } else {
                myHolder.tv_dz_num.setText("0");

            }
        } else {
            myHolder.tv_dz_num.setText("0");

        }
    }

    private void initdz(final int position, final View sd_dz) {

        sd_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user_id = 0;
                user_id = (int) SPUtils.get("user_id", user_id);
                String token = "";
                token = (String) SPUtils.get("token", token);
                if (token.equals("")) {
                    MoudleUtils.initToLogin(context);
                } else {
                    listcall = RetrofitUtils.retrofit.create(NpApi.class).thumbMineFocus(list.get(position).getId(), user_id, token);
                    if (!listcall.isExecuted()) {
                        listcall.enqueue(new Callback<GetMyfocusCircleBean>() {
                            @Override
                            public void onResponse(Call<GetMyfocusCircleBean> call, Response<GetMyfocusCircleBean> response) {
                                GetMyfocusCircleBean bean = response.body();
                                if (bean != null) {
                                    initNextData(bean, position, sd_dz);
                                }
                            }

                            @Override
                            public void onFailure(Call<GetMyfocusCircleBean> call, Throwable t) {
                            }
                        });
                    }
                }
            }
        });
    }

    private void initNextData(GetMyfocusCircleBean bean, int p, View sd_dz) {
        switch (bean.getStatus()) {
            case "1":
                if (bean.getInfo() != null) {
                    list.set(p, bean.getInfo().get(0));
                    initDzXg(list.get(p).getIsthumb(), p, sd_dz);
                    notifyDataSetChanged();
                }
                break;
            case "0":

                break;
            case "2":
                MoudleUtils.initStatusTwo(context, false);
                break;
        }

    }

    private void initDzXg(String isthumb, int p, View sd_dz) {
        mGoodView = new GoodView(this.context);
        if (isthumb.equals("1")) {
            MoudleUtils.PointPraiseEffect(mGoodView, sd_dz, context, R.drawable.good_color);
        } else if (isthumb.equals("0")) {
            MoudleUtils.PointPraiseEffect(mGoodView, sd_dz, context, R.drawable.good);
        }
    }

    private void initTwoGoe() {
//        holder.tv_pl_one.setVisibility(View.GONE);
//        holder.tv_pl_two.setVisibility(View.GONE);
        MoudleUtils.viewGone(holder.tv_pl_one);
        MoudleUtils.viewGone(holder.tv_pl_two);
        MoudleUtils.viewGone(holder.tv_pl_more);
    }

    private void initTvPl(TextView tv_pl_more, TextView tv_pl, int position, int n) {
        if (list.get(position).getComment().get(n) != null) {
//            tv_pl.setVisibility(View.VISIBLE);
            MoudleUtils.viewShow(tv_pl_more);
            MoudleUtils.viewShow(tv_pl);
//            MoudleUtils.textViewSetText(tv_pl, list.get(position).getComment().get(n).getUsername() + ":  " +
//                    list.get(position).getComment().get(n).getContent());
            String name = list.get(position).getComment().get(n).getUsername().trim();
            String addname = list.get(position).getComment().get(n).getAddname().trim();
            String addname_hf = "回复";
            if (name == null || name.equals("")) {
                name = "";
            }
            if (addname == null || addname.equals("")) {
                addname = "";
                addname_hf = "";
            }
            String s1 = name + addname_hf +
                    addname + ": ".trim();
            String s2 = list.get(position).getComment().get(n).getContent().trim();
            String s = s1 + s2;
//            String s=name + addname_hf +
//                    addname + ": " +
//                    list.get(position).getComment().get(n).getContent();
            MoudleUtils.textViewSetText(tv_pl, s);

            SpannableString spannableString = new SpannableString(tv_pl.getText().toString().trim());
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
            ForegroundColorSpan colorSpanTwo = new ForegroundColorSpan(Color.parseColor("#666666"));

            spannableString.setSpan(colorSpan, name.length(), name.length() + addname_hf.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(colorSpanTwo, s1.length(), s1.length() + s2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_pl.setText(spannableString);

        }
    }

    private void goToDw(String endNodeStr, String lat, String lon) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            Intent intent = new Intent();
            if (!endNodeStr.equals("") && !lat.equals("") && !lon.equals("")) {
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("adr", "");
                intent.putExtra("tel", "");
                intent.putExtra("endNodeStr", endNodeStr);
                intent.setClass(context, PoiSearchOneCirActivity.class);
                context.startActivity(intent);
            }

        } else {
            MoudleUtils.toChekWifi(context);
        }
    }

    private void initFenXiang(final int position) {
        holder.sdFenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享操作写在adapter
                NotifyToMineAtentionAcFxManager.getInstance().sendNotifyActivityToFlag(true, list, position);

            }
        });

    }

    private void initJump(final int p) {
        holder.grd_iv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(p).getPicture() != null) {
                    if (list.get(p).getPicture().size() > 0) {
                        Intent intent = new Intent(context, CircleIcBigActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("getPicture", (ArrayList<String>) list.get(p).getPicture());
                        intent.putExtras(bundle);
                        intent.putExtra("p", position);
                        context.startActivity(intent);
                    }
                }
            }
        });
    }


    static class ViewHolder {
        GridView grd_iv;
        TextView textView_username;
        TextView textView_information;
        TextView tv_time;
        SimpleDraweeView iv_circle_mine;
        SimpleDraweeView sdFenxiang;
        SimpleDraweeView sd_dw;
        TextView tv_dw;
        SimpleDraweeView sd_xb;
        TextView tv_pl_one;
        TextView tv_pl_two;
        TextView tv_pl_more;
        TextView tv_dz_num;
        SimpleDraweeView sd_dz;
    }

    private void initGridIv(int position) {
        List<String> picture = list.get(position).getThumb();
        if (picture == null || picture.size() == 0) {
            picture = list.get(position).getPicture();
        }
        if (picture == null || picture.size() == 0) {
            picture = new ArrayList<>();
        }
        GridAdapterMineFocus gridAdapterMine = new GridAdapterMineFocus(context);
        gridAdapterMine.setList(picture);
        holder.grd_iv.setAdapter(gridAdapterMine);
    }


}
