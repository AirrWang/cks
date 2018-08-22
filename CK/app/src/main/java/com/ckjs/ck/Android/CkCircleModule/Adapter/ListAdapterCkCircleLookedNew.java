package com.ckjs.ck.Android.CkCircleModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.ckjs.ck.Android.MineModule.Activity.MineAttentionPeople;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Manager.NotifyListMessageLookedManager;
import com.ckjs.ck.Manager.NotifyToMainAcFxManager;
import com.ckjs.ck.Message.NotifyListLookedMessage;
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

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class ListAdapterCkCircleLookedNew extends BaseAdapter implements NotifyListLookedMessage {

    private Call<GetCircleBean> listcall;

    public List<GetCircleBean.InfoBean> getList() {
        return list;
    }

    private List<GetCircleBean.InfoBean> list = new ArrayList<>();
    private Context context;
    private GoodView mGoodView;

    public ListAdapterCkCircleLookedNew(Context context) {
        super();
        this.context = context;
        NotifyListMessageLookedManager.getInstance().setNotifyMessage(this);
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
            if (context == null) {
                return view;
            }
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_list_ckcircle_recommend_mian, null);
            myHolder = new MyHolder();
            myHolder.textView_username = (TextView) view.findViewById(R.id.textView_username);
            myHolder.textView_information = (TextView) view.findViewById(R.id.textView_information);
            myHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            myHolder.tv_dw = (TextView) view.findViewById(R.id.tv_dw);
            myHolder.iv_circle_mine = (SimpleDraweeView) view.findViewById(R.id.iv_circle_mine);
            myHolder.sd_xb = (SimpleDraweeView) view.findViewById(R.id.sd_xb);
            myHolder.grd_iv = (GridView) view.findViewById(R.id.grd_iv);
            myHolder.sdFenxiang = (SimpleDraweeView) view.findViewById(R.id.sdFenxiang);
            myHolder.sd_dw = (SimpleDraweeView) view.findViewById(R.id.sd_dw);
            myHolder.tv_pl_one = (TextView) view.findViewById(R.id.tv_pl_one);
            myHolder.tv_pl_two = (TextView) view.findViewById(R.id.tv_pl_two);
            myHolder.tv_pl_more = (TextView) view.findViewById(R.id.tv_pl_more);
            myHolder.tv_dz_num = (TextView) view.findViewById(R.id.tv_dz_num);
            myHolder.sd_dz = (SimpleDraweeView) view.findViewById(R.id.sd_dz);

//            myHolder.textView_username_new = (TextView) view.findViewById(R.id.textView_username_new);
//            myHolder.textView_information_new = (TextView) view.findViewById(R.id.textView_information_new);
//            myHolder.tv_time_new = (TextView) view.findViewById(R.id.tv_time_new);
//            myHolder.tv_xq = (TextView) view.findViewById(R.id.tv_xq);
//
//            myHolder.sdFenxiang_new = (SimpleDraweeView) view.findViewById(R.id.sdFenxiang_new);
//            myHolder.iv_circle_mine_new = (SimpleDraweeView) view.findViewById(R.id.iv_circle_mine_new);
//            myHolder.sd_pic = (SimpleDraweeView) view.findViewById(R.id.iv_circle_mine_new);
//            myHolder.sd_dz_new = (SimpleDraweeView) view.findViewById(R.id.sd_dz_new);
//            myHolder.tv_dz_num_new = (TextView) view.findViewById(R.id.tv_dz_num_new);
//
//            myHolder.r_my_tz = (RelativeLayout) view.findViewById(R.id.r_my_tz);
//            myHolder.r_gftj = (RelativeLayout) view.findViewById(R.id.r_gftj);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }

        initData(position, myHolder);
        initGridItemClick(myHolder, position);
        myHolder.iv_circle_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, MineAttentionPeople.class);
                intent.putExtra("focus_id", "" + list.get(position).getUser_id());
                context.startActivity(intent);
            }
        });
        myHolder.sd_dw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDw(list.get(position).getPlace(), list.get(position).getLat(),
                        list.get(position).getLon());
            }
        });
        myHolder.tv_dw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDw(list.get(position).getPlace(), list.get(position).getLat(),
                        list.get(position).getLon());
            }
        });
        initdz(position, myHolder.sd_dz);
        initdz(position, myHolder.tv_dz_num);
        return view;
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

    private void initGridItemClick(MyHolder myHolder, final int p) {
        myHolder.grd_iv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private void initData(final int position, MyHolder myHolder) {
        initDz(position, myHolder);

        if (!list.get(position).getPicurl().equals("")) {
            FrescoUtils.setImage(myHolder.iv_circle_mine, AppConfig.url + list.get(position).getPicurl(), ScreenUtils.getScreenWidth() / 4);
        }
        myHolder.textView_username.setText(list.get(position).getUsername());
        if (list.get(position).getContent().equals("")) {
            if (myHolder.textView_information.getVisibility() == View.VISIBLE) {
                myHolder.textView_information.setVisibility(View.GONE);
            }
            myHolder.textView_information.setText(list.get(position).getContent());
        } else {
            if (myHolder.textView_information.getVisibility() != View.VISIBLE) {
                myHolder.textView_information.setVisibility(View.VISIBLE);
            }
            myHolder.textView_information.setText(list.get(position).getContent());
        }
        if (list.get(position).getPlace().equals(null)) {
//            myHolder.sd_dw.setVisibility(View.INVISIBLE);
            MoudleUtils.viewInvisibily(myHolder.sd_dw);
            myHolder.tv_dw.setText("");
        } else {
            if (list.get(position).getPlace().equals("")) {
//                myHolder.sd_dw.setVisibility(View.INVISIBLE);
                MoudleUtils.viewInvisibily(myHolder.sd_dw);
                myHolder.tv_dw.setText("");
            } else {
//                myHolder.sd_dw.setVisibility(View.VISIBLE);
                MoudleUtils.viewShow(myHolder.sd_dw);
                FrescoUtils.setImage(myHolder.sd_dw, AppConfig.res + R.drawable.attention_dingwei);
                myHolder.tv_dw.setText(list.get(position).getPlace());

            }
        }
        if (list.get(position).getSex().equals("2")) {
            FrescoUtils.setImage(myHolder.sd_xb, AppConfig.res + R.drawable.my_girl);
        } else if (list.get(position).getSex().equals("1")) {
            FrescoUtils.setImage(myHolder.sd_xb, AppConfig.res + R.drawable.my_boy);
        } else {
            FrescoUtils.setImage(myHolder.sd_xb, AppConfig.res + R.drawable.my_boy);
        }
        myHolder.tv_time.setText(list.get(position).getTime());
        initGridIv(position, myHolder);
        myHolder.sdFenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyToMainAcFxManager.getInstance().sendNotifyActivityToFlag(true, list, position);
            }
        });
        if (list.get(position).getComment() != null) {
            if (list.get(position).getComment().size() > 0) {
                switch (list.get(position).getComment().size()) {
                    case 0:
                        initSetOneAndTwoNull(myHolder);
                        initGoOneAndTwo(myHolder);
                        break;
                    case 1:
                        initTvPl(myHolder.tv_pl_more, myHolder.tv_pl_one, position, 0);
//                        myHolder.tv_pl_two.setVisibility(View.GONE);
                        MoudleUtils.viewGone(myHolder.tv_pl_two);
                        myHolder.tv_pl_two.setText("");
                        break;
                    case 2:
                        initTvPl(myHolder.tv_pl_more, myHolder.tv_pl_one, position, 0);
                        initTvPl(myHolder.tv_pl_more, myHolder.tv_pl_two, position, 1);
                        break;
                }

            } else {
                initSetOneAndTwoNull(myHolder);
                initGoOneAndTwo(myHolder);
            }
        } else {
            initSetOneAndTwoNull(myHolder);
            initGoOneAndTwo(myHolder);
//            myHolder.tv_pl_one.setVisibility(View.GONE);
//            myHolder.tv_pl_two.setVisibility(View.GONE);
        }
    }

    private void initDz(int position, MyHolder myHolder) {
        if (list.get(position).getIsthumb().equals("1")) {

            FrescoUtils.setImage(myHolder.sd_dz, AppConfig.res + R.drawable.good_color);
            myHolder.tv_dz_num.setTextColor(CkApplication.getInstance().getResources().getColor(R.color.fea21249));

        } else if (list.get(position).getIsthumb().equals("0")) {

            FrescoUtils.setImage(myHolder.sd_dz, AppConfig.res + R.drawable.good);
            myHolder.tv_dz_num.setTextColor(CkApplication.getInstance().getResources().getColor(R.color.c_be));

        }
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

                    listcall = RetrofitUtils.retrofit.create(NpApi.class).thumb(list.get(position).getId(), user_id, token);
                    if (!listcall.isExecuted()) {
                        listcall.enqueue(new Callback<GetCircleBean>() {
                            @Override
                            public void onResponse(Call<GetCircleBean> call, Response<GetCircleBean> response) {
                                GetCircleBean bean = response.body();
                                if (bean != null) {
                                    initNextData(bean, position, sd_dz);
                                }
                            }

                            @Override
                            public void onFailure(Call<GetCircleBean> call, Throwable t) {
                            }
                        });
                    }
                }
            }
        });
    }

    private void initNextData(GetCircleBean bean, int p, View sd_dz) {
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

    private void initDzXg(String thumb, int p, View sd_dz) {
        mGoodView = new GoodView(context);
        if (thumb.equals("1")) {
            MoudleUtils.PointPraiseEffect(mGoodView, sd_dz, context, R.drawable.good_color);
        } else if (thumb.equals("0")) {
            MoudleUtils.PointPraiseEffect(mGoodView, sd_dz, context, R.drawable.good);
        }
    }

    private void initSetOneAndTwoNull(MyHolder myHolder) {
        myHolder.tv_pl_one.setText("");
        myHolder.tv_pl_two.setText("");
    }

    private void initGoOneAndTwo(MyHolder myHolder) {
//        myHolder.tv_pl_one.setVisibility(View.GONE);
//        myHolder.tv_pl_two.setVisibility(View.GONE);
        MoudleUtils.viewGone(myHolder.tv_pl_one);
        MoudleUtils.viewGone(myHolder.tv_pl_two);
        MoudleUtils.viewGone(myHolder.tv_pl_more);

    }

    private void initTvPl(TextView tv_pl_more, TextView tv_pl, int position, int n) {
        if (list.get(position).getComment().get(n) != null) {

            MoudleUtils.viewShow(tv_pl_more);
            MoudleUtils.viewShow(tv_pl);
//            tv_pl.setVisibility(View.VISIBLE);
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


    private void initGridIv(int position, MyHolder myHolder) {
        List<String> picture = list.get(position).getThumb();
        if (picture == null || picture.size() == 0) {
            picture = list.get(position).getPicture();
        }
        if (picture == null || picture.size() == 0) {
            picture=new ArrayList<>();
        }
        GridAdapterCkCircleMine gridAdapterCkCircleMine = new GridAdapterCkCircleMine(context);
        gridAdapterCkCircleMine.setList(picture);
        myHolder.grd_iv.setAdapter(gridAdapterCkCircleMine);
    }


    @Override
    public void sendMessageListFlag(boolean flag, List<GetCircleBean.InfoBean> list) {
        if (flag) {
            if (context != null) {
                this.list = list;
                notifyDataSetChanged();
            }
        }
    }


    class MyHolder {
        TextView textView_username;
        TextView textView_information;
        TextView tv_time;
        TextView tv_dw;
        SimpleDraweeView iv_circle_mine;
        SimpleDraweeView sdFenxiang;
        SimpleDraweeView sd_dw;
        GridView grd_iv;
        SimpleDraweeView sd_xb;
        TextView tv_pl_one;
        TextView tv_pl_two;
        TextView tv_pl_more;
        TextView tv_dz_num;
        SimpleDraweeView sd_dz;

//        TextView textView_username_new;
//        TextView textView_information_new;
//        TextView tv_time_new;
//        TextView tv_xq;
//
//        SimpleDraweeView sdFenxiang_new;
//        SimpleDraweeView iv_circle_mine_new;
//        SimpleDraweeView sd_pic;
//        SimpleDraweeView sd_dz_new;
//
//        RelativeLayout r_my_tz;
//        RelativeLayout r_gftj;
//
//        TextView tv_dz_num_new;
    }


}
