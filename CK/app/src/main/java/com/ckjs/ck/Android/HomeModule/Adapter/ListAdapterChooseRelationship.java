package com.ckjs.ck.Android.HomeModule.Adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ckjs.ck.Android.HomeModule.Activity.ChooseRelationship;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.BindacceptBean;
import com.ckjs.ck.Bean.MemberApplyBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.ckjs.ck.Tool.ViewTool.MyGridView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ListAdapterChooseRelationship extends BaseAdapter {

    private List<MemberApplyBean.MemberApplyInfoBean.LabelBean> list;
    private Context context;
    private String memeberId;
    private int ps = -1;
    private int bs_p = -1;


    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    private String relationship;
    private String relationshipType;

    public ListAdapterChooseRelationship(Context context) {
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
                    R.layout.item_list_choose_relationship, null);
            myHolder = new MyHolder();
            myHolder.tv_item_title = (TextView) view.findViewById(R.id.tv_item_title);
            myHolder.mgv_relationship = (MyGridView) view.findViewById(R.id.mgv_relationship);


            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }

        initData(position, myHolder);
        initGridIv(position, myHolder);


        return view;
    }

    private void initData(int position, MyHolder myHolder) {
        MoudleUtils.textViewSetText(myHolder.tv_item_title, list.get(position).getName());
        myHolder.mgv_relationship.setTag(position);
    }

    private void initGridItemClick( final MyHolder myHolder) {
//        Log.d(AppConfig.TAG, ps + ":ps");
        myHolder.mgv_relationship.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ps = (int) myHolder.mgv_relationship.getTag();
                    if (position == list.get(ps).getList().size() - 1) {
                        toQy();
                    } else {
                        notifyDataSetChanged();
                        bs_p = position;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 填写亲属关系
     */
    private void toQy() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_choose_relationship_other, null);
        builder.setView(view);
        final EditText input = (EditText) view.findViewById(R.id.editText);
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);
        Button button = (Button) view.findViewById(R.id.button);
        Button button4 = (Button) view.findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = input.getText().toString().trim();
                if (s != null && !s.equals("")) {
                    if (!DataUtils.containsEmoji(s)) {
                        initTask(s, alert);
                    } else {
                        ToastUtils.showShortNotInternet("不支持输入Emoji表情符号");
                    }
                } else {
                    KeyBoardUtils.closeKeyboard(input, context);
                    ToastUtils.showShortNotInternet("内容不能为空");
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void initTask(String s, final AlertDialog alert) {
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<BindacceptBean> callBack = restApi.memberapply(user_id + "", token, memeberId, s, list.get(ps).getType());

        callBack.enqueue(new Callback<BindacceptBean>() {
            @Override
            public void onResponse(Call<BindacceptBean> call, Response<BindacceptBean> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        //成功操作
                        ((ChooseRelationship) (context)).finish();
                        ToastUtils.showShort(context,response.body().getMsg());
                    } else {
                        ToastUtils.showShort(context,response.body().getMsg());
                    }
                }
                alert.dismiss();
            }

            @Override
            public void onFailure(Call<BindacceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
                alert.dismiss();
            }
        });


    }

    private void initGridIv(int position, MyHolder myHolder) {
        GridAdapterChooseFamily gridAdapterChooseFamily = new GridAdapterChooseFamily(context);
        gridAdapterChooseFamily.setList(list.get(position).getList());
        myHolder.mgv_relationship.setAdapter(gridAdapterChooseFamily);
        initGridItemClick(myHolder);
        initBianSe(position, gridAdapterChooseFamily);
    }

    private void initBianSe(int p, GridAdapterChooseFamily gridAdapterChooseFamily) {
        if (ps >= 0 && bs_p >= 0) {
            if (p == ps) {
                int position = bs_p;
                gridAdapterChooseFamily.setI_xz(position);
                relationship = list.get(ps).getList().get(position);
                relationshipType = list.get(ps).getType();
                gridAdapterChooseFamily.notifyDataSetChanged();
            }
        }
    }


    public void setList(List<MemberApplyBean.MemberApplyInfoBean.LabelBean> list) {
        this.list = list;
    }

    public String getMemeberId() {
        return memeberId;
    }

    public void setMemeberId(String memeberId) {
        this.memeberId = memeberId;
    }


    class MyHolder {
        TextView tv_item_title;
        MyGridView mgv_relationship;
    }


}
