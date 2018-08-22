package com.ckjs.ck.Android.CkCircleModule.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.ckjs.ck.Android.MineModule.Activity.MineAttentionPeople;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.DelcircleBean;
import com.ckjs.ck.Bean.GetCircleMineBean;
import com.ckjs.ck.Manager.NotifyCkMineListClreaMessageManager;
import com.ckjs.ck.Manager.NotifyListMessageMineManager;
import com.ckjs.ck.Manager.NotifyToMainAcFxManager;
import com.ckjs.ck.Message.NotifyListMineMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.GoodView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterCkCircleMineNew extends BaseAdapter implements NotifyListMineMessage {

    private Call<GetCircleMineBean> listcall;

    public List<GetCircleMineBean.InfoBean> getList() {
        return list;
    }

    private List<GetCircleMineBean.InfoBean> list = new ArrayList<>();
    private Context context;
    private GoodView mGoodView;

    public ListAdapterCkCircleMineNew(Context context) {
        super();
        this.context = context;
        NotifyListMessageMineManager.getInstance().setNotifyMessage(this);
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
                    R.layout.item_list_ckcircle_mine_delete, null);
            myHolder = new MyHolder();
            myHolder.textView_username = (TextView) view.findViewById(R.id.textView_username);
            myHolder.textView_information = (TextView) view.findViewById(R.id.textView_information);
            myHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            myHolder.tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            myHolder.sd_xb = (SimpleDraweeView) view.findViewById(R.id.sd_xb);
            myHolder.tv_pl_one = (TextView) view.findViewById(R.id.tv_pl_one);
            myHolder.tv_pl_two = (TextView) view.findViewById(R.id.tv_pl_two);
            myHolder.tv_pl_more = (TextView) view.findViewById(R.id.tv_pl_more);
            myHolder.tv_dw = (TextView) view.findViewById(R.id.tv_dw);
            myHolder.iv_circle_mine = (SimpleDraweeView) view.findViewById(R.id.iv_circle_mine);
            myHolder.sdFenxiang = (SimpleDraweeView) view.findViewById(R.id.sdFenxiang);
            myHolder.sdPinlunRed = (SimpleDraweeView) view.findViewById(R.id.sdPinlunRed);
            myHolder.sd_dw = (SimpleDraweeView) view.findViewById(R.id.sd_dw);
            myHolder.grd_iv = (GridView) view.findViewById(R.id.grd_iv);
            myHolder.tv_dz_num = (TextView) view.findViewById(R.id.tv_dz_num);
            myHolder.sd_dz = (SimpleDraweeView) view.findViewById(R.id.sd_dz);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        initData(position, myHolder);
        initGridItemClick(myHolder, position);
        myHolder.sdFenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyToMainAcFxManager.getInstance().sendNotifyActivityToMineFlag(true, list, position);
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
        myHolder.iv_circle_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, MineAttentionPeople.class);
                intent.putExtra("focus_id", "" + list.get(position).getUser_id());
                context.startActivity(intent);
            }
        });
        initdz(position, myHolder.sd_dz);
        initdz(position, myHolder.tv_dz_num);
        return view;
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
                    listcall = RetrofitUtils.retrofit.create(NpApi.class).thumbMine(list.get(position).getId(), user_id, token);
                    if (!listcall.isExecuted()) {
                        listcall.enqueue(new Callback<GetCircleMineBean>() {
                            @Override
                            public void onResponse(Call<GetCircleMineBean> call, Response<GetCircleMineBean> response) {
                                GetCircleMineBean bean = response.body();
                                if (bean != null) {
                                    initNextData(bean, position, sd_dz);
                                }
                            }

                            @Override
                            public void onFailure(Call<GetCircleMineBean> call, Throwable t) {
                            }
                        });
                    }
                }
            }
        });
    }

    private void initNextData(GetCircleMineBean bean, int p, final View sd_dz) {
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
        if (list.get(position).getNewcom() != null) {
            if (list.get(position).getNewcom().equals("1")) {
//            myHolder.sdPinlunRed.setVisibility(View.VISIBLE);
                MoudleUtils.viewShow(myHolder.sdPinlunRed);
            } else if (list.get(position).getNewcom().equals("0")) {
//            myHolder.sdPinlunRed.setVisibility(View.INVISIBLE);
                MoudleUtils.viewInvisibily(myHolder.sdPinlunRed);
            }
        } else {
            MoudleUtils.viewInvisibily(myHolder.sdPinlunRed);
        }
        if (!list.get(position).getPicurl().equals("")) {
            FrescoUtils.setImage(myHolder.iv_circle_mine, AppConfig.url + list.get(position).getPicurl(), ScreenUtils.getScreenWidth() / 4);
        }
        myHolder.textView_username.setText(list.get(position).getUsername());
        if (list.get(position).getContent().equals("")) {
            myHolder.textView_information.setText(list.get(position).getContent());
            if (myHolder.textView_information.getVisibility() == View.VISIBLE) {
                myHolder.textView_information.setVisibility(View.GONE);
            }
        } else {
            myHolder.textView_information.setText(list.get(position).getContent());
            if (myHolder.textView_information.getVisibility() != View.VISIBLE) {
                myHolder.textView_information.setVisibility(View.VISIBLE);
            }
        }
        if (list.get(position).getPlace().equals(null)) {
            initDisSddw(myHolder);
            myHolder.tv_dw.setText("");

        } else {
            if (list.get(position).getPlace().equals("")) {
                initDisSddw(myHolder);
                myHolder.tv_dw.setText("");

            } else {
                initShowSddw(myHolder);
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

        myHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTaskDelete(position);
            }
        });
        initGridIv(position, myHolder);
        if (list.get(position).getComment() != null) {
            if (list.get(position).getComment().size() > 0) {
                switch (list.get(position).getComment().size()) {
                    case 0:
                        myHolder.tv_pl_one.setText("");
                        myHolder.tv_pl_two.setText("");
                        initTwoGone(myHolder);
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
                myHolder.tv_pl_one.setText("");
                myHolder.tv_pl_two.setText("");
                initTwoGone(myHolder);
            }
        } else {
            myHolder.tv_pl_one.setText("");
            myHolder.tv_pl_two.setText("");
            initTwoGone(myHolder);
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

    private void initTwoGone(MyHolder myHolder) {
//        myHolder.tv_pl_one.setVisibility(View.GONE);
//        myHolder.tv_pl_two.setVisibility(View.GONE);
        MoudleUtils.viewGone(myHolder.tv_pl_one);
        MoudleUtils.viewGone(myHolder.tv_pl_two);
        MoudleUtils.viewGone(myHolder.tv_pl_more);
    }

    private void initShowSddw(MyHolder myHolder) {
        if (myHolder.sd_dw.getVisibility() != View.VISIBLE) {
            myHolder.sd_dw.setVisibility(View.VISIBLE);
        }
    }

    private void initDisSddw(MyHolder myHolder) {
        if (myHolder.sd_dw.getVisibility() == View.VISIBLE) {
            myHolder.sd_dw.setVisibility(View.INVISIBLE);
        }
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
            spannableString.setSpan(colorSpan, name.length(), name.length() +addname_hf.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(colorSpanTwo, s1.length(), s1.length() + s2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_pl.setText(spannableString);

        }
    }

    AlertDialog alertDialog;

    private void initTaskDelete(final int position) {
        String token = "";
        token = (String) SPUtils.get(context, "token", token);
        final int user_id = (int) SPUtils.get(context, "user_id", 0);
        if (token.equals("")) {
            MoudleUtils.initToLogin(context);
        } else {
            initIsToDelete(position, token, user_id);

        }
    }

    private void initIsToDelete(final int position, String token, final int user_id) {
        final String finalToken = token;
        alertDialog = new AlertDialog.Builder(context).setTitle("删除我的帖子")
                .setMessage("真的要删除我么")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        initToTask(finalToken, user_id, position);
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

    private void initToTask(String token, int user_id, final int position) {
        Call<DelcircleBean> call = RetrofitUtils.retrofit.create(NpApi.class).delcircle(token, user_id, list.get(position).getId());
        call.enqueue(new Callback<DelcircleBean>() {
            @Override
            public void onResponse(Call<DelcircleBean> call, Response<DelcircleBean> response) {
                DelcircleBean bean = response.body();
                if (bean != null) {
                    switch (bean.getStatus()) {
                        case "1":
                            list.remove(position);
                            notifyDataSetChanged();
                            if (list.size() == 0) {
                                NotifyCkMineListClreaMessageManager.getInstance().sendNotifyCkMineListClreaFlag(true);
                            }
                            break;
                        case "0":
                            ToastUtils.showShort(context, bean.getMsg());
                            break;
                        case "2":
                            MoudleUtils.initStatusTwo(context, true);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<DelcircleBean> call, Throwable t) {

                MoudleUtils.toChekWifi(context);

            }
        });
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
    public void sendMessageListFlag(boolean flag, List<GetCircleMineBean.InfoBean> list) {
        if (flag) {
            if (context != null) {
                this.list = list;
                this.notifyDataSetChanged();
            }
        }
    }


    class MyHolder {
        TextView textView_username;
        TextView textView_information;
        TextView tv_time;
        TextView tv_dw;
        TextView tv_delete;
        SimpleDraweeView iv_circle_mine;
        SimpleDraweeView sdFenxiang;
        SimpleDraweeView sd_dw;
        SimpleDraweeView sdPinlunRed;
        GridView grd_iv;
        SimpleDraweeView sd_xb;
        TextView tv_pl_one;
        TextView tv_pl_two;
        TextView tv_pl_more;
        TextView tv_dz_num;
        SimpleDraweeView sd_dz;
    }


}
