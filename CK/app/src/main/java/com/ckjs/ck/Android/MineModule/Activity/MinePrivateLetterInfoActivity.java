package com.ckjs.ck.Android.MineModule.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMinePrivateLetterInfo;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.LetterBean;
import com.ckjs.ck.Bean.PushLetterBean;
import com.ckjs.ck.Bean.UsereportBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.DataUtils;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class MinePrivateLetterInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView list_mine_private_letter_detail;
    private EditText mine_private_input_text;
    private String username = "";
    private String add_id;
    private View footView;
    private boolean flag;
    private KyLoadingBuilder builder;
    private int page = 1;
    private List<LetterBean.LetterInfoBean> listAll = new ArrayList<>();
    private ProgressDialog dialog;
    private int n;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_private_detail);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot_sx, null);
        dialog = new ProgressDialog(this);
        builder = new KyLoadingBuilder(this);
        initId();
        initToolbar();
        initData();
        initSet();
    }

    /**
     * 展示数据接口
     */
    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MinePrivateLetterInfoActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(MinePrivateLetterInfoActivity.this, "token", "");

        Call<LetterBean> callBack = restApi.circleletter(user_id, add_id, token, page);

        callBack.enqueue(new Callback<LetterBean>() {
            @Override
            public void onResponse(Call<LetterBean> call, Response<LetterBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (response.body().getInfo() != null) {


                            List<LetterBean.LetterInfoBean> listNew = new ArrayList<>();
                            listNew = response.body().getInfo();
                            if (listNew.size() > 0) {
                                if (page == 1) {
                                    n = listNew.size() - 1;
                                } else if (page > 1) {
                                    n = listNew.size();
                                }
                            } else if (listNew.size() == 0) {
                                n = 0;
                            }
                            if (page == 1) {
                                listAll.clear();
                            }
                            listNew.addAll(listAll);
                            listAll = listNew;
                            if (listAll.size() == 0) {
                                ToastUtils.show(MinePrivateLetterInfoActivity.this, "您还没有任何私聊信息", 0);
                            }
                            initAdapter();
                            page++;
                            initSet();
                        }

                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MinePrivateLetterInfoActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MinePrivateLetterInfoActivity.this, response.body().getMsg());
                    }
                }
                showFoot();
                MoudleUtils.kyloadingDismiss(builder);
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<LetterBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MinePrivateLetterInfoActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
                showFoot();
                MoudleUtils.dialogDismiss(dialog);
            }
        });

    }

    /**
     * 上拉加载
     */
    private void initSet() {
        list_mine_private_letter_detail.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (list_mine_private_letter_detail.getLastVisiblePosition() == (list_mine_private_letter_detail.getCount() - 1)) {

                        }
                        //判断滚动到顶部
                        if (list_mine_private_letter_detail.getFirstVisiblePosition() == 0) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                initData();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                flag = firstVisibleItem + visibleItemCount == totalItemCount && !flag;
            }
        });
    }

    /**
     * 隐藏脚布局
     */
    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * listView绑定数据
     *
     * @param
     */
    private void initAdapter() {
        if (listAdapterMinePrivateLetterinfo == null) {
            listAdapterMinePrivateLetterinfo = new ListAdapterMinePrivateLetterInfo();
            listAdapterMinePrivateLetterinfo.setDataSource(listAll);
            list_mine_private_letter_detail.setAdapter(listAdapterMinePrivateLetterinfo);
        } else {
            listAdapterMinePrivateLetterinfo.setDataSource(listAll);
            listAdapterMinePrivateLetterinfo.notifyDataSetChanged();
        }

        //让列表默认显示最后一行
        list_mine_private_letter_detail.setSelection(n);
    }

    private ListAdapterMinePrivateLetterInfo listAdapterMinePrivateLetterinfo;

    /**
     * 标题栏设置
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        if (!username.equals("")) {
            textView.setText(username);
        }
        setSupportActionBar(toolbar);

        RelativeLayout r_toolbar_button = (RelativeLayout) findViewById(R.id.r_toolbar_button);
        r_toolbar_button.setOnClickListener(this);
        SimpleDraweeView sd_button = (SimpleDraweeView) findViewById(R.id.sd_button);
        sd_button.setOnClickListener(this);

        FrescoUtils.setImage(sd_button, AppConfig.res + R.drawable.mpre_report);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    /**
     * toolbar返回按钮
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify shouhuan_serch parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initId() {
        list_mine_private_letter_detail = (ListView) findViewById(R.id.list_mine_private_letter_detail);
        list_mine_private_letter_detail.addHeaderView(footView);

        Button send = (Button) findViewById(R.id.mine_private_input_sd);
        mine_private_input_text = (EditText) findViewById(R.id.mine_private_input_content);

        //isRootNamespace()
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        add_id = intent.getStringExtra("add_id");


        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_private_input_sd:
                String s = mine_private_input_text.getText().toString().trim();
                if (s != null && !s.equals("")) {
                    KeyBoardUtils.closeKeyboard(mine_private_input_text, MinePrivateLetterInfoActivity.this);
                    initTask1();
                } else {
                    ToastUtils.showShort(this, "未输入任何信息");
                }

                break;
            case R.id.sd_button:
            case R.id.r_toolbar_button:
                toReport();
                break;

        }
    }

    /**
     * 举报弹框
     */
    private void toReport() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请输入举报理由(如骚扰，涉黄等)");

        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog_report_input, null);
        builder.setView(view);

        builder.setPositiveButton("举报", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input = (EditText) view.findViewById(R.id.et_dialog_report_input);
                String s = input.getText().toString().trim();
                if (s != null && !s.equals("")) {
                    if (!DataUtils.containsEmoji(s)) {
                        initTask(s);
                    } else {
                        ToastUtils.showShortNotInternet("不支持输入Emoji表情符号");
                    }
                } else {
                    KeyBoardUtils.closeKeyboard(input, MinePrivateLetterInfoActivity.this);
                    KeyBoardUtils.closeKeyboard(mine_private_input_text, MinePrivateLetterInfoActivity.this);
                    ToastUtils.showShortNotInternet("举报理由不能为空");
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

    /**
     * 举报接口
     *
     * @param reportReson
     */
    private void initTask(String reportReson) {
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MinePrivateLetterInfoActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(MinePrivateLetterInfoActivity.this, "token", "");
        if (token.equals("")) {
            MoudleUtils.initStatusTwo(MinePrivateLetterInfoActivity.this, true);
            return;
        }
        Call<UsereportBean> callBack = restApi.usereport(user_id, add_id, token, reportReson);

        callBack.enqueue(new Callback<UsereportBean>() {
            @Override
            public void onResponse(Call<UsereportBean> call, Response<UsereportBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        ToastUtils.showShort(MinePrivateLetterInfoActivity.this, response.body().getMsg());

                        MoudleUtils.dialogDismiss(dialog);
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MinePrivateLetterInfoActivity.this, response.body().getMsg(), 0);
                        MoudleUtils.dialogDismiss(dialog);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.show(MinePrivateLetterInfoActivity.this, response.body().getMsg(), 0);
                        MoudleUtils.dialogDismiss(dialog);
                    }
                } else {
                    MoudleUtils.dialogDismiss(dialog);
                }
                KeyBoardUtils.closeKeyboard(input, MinePrivateLetterInfoActivity.this);
                KeyBoardUtils.closeKeyboard(mine_private_input_text, MinePrivateLetterInfoActivity.this);
            }

            @Override
            public void onFailure(Call<UsereportBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MinePrivateLetterInfoActivity.this);
                MoudleUtils.dialogDismiss(dialog);
                KeyBoardUtils.closeKeyboard(input, MinePrivateLetterInfoActivity.this);
                KeyBoardUtils.closeKeyboard(mine_private_input_text, MinePrivateLetterInfoActivity.this);
            }
        });
    }

    /**
     * 私信发送接口
     */
    private void initTask1() {
        MoudleUtils.dialogShow(dialog);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MinePrivateLetterInfoActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(MinePrivateLetterInfoActivity.this, "token", "");
        if (token.equals("")) {
            MoudleUtils.initStatusTwo(MinePrivateLetterInfoActivity.this, true);
            return;
        }
        page = 1;
//        flag_end = false;
        Call<PushLetterBean> callBack = restApi.pushletter(user_id, token, add_id, mine_private_input_text.getText().toString().trim());

        callBack.enqueue(new Callback<PushLetterBean>() {
            @Override
            public void onResponse(Call<PushLetterBean> call, Response<PushLetterBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        ToastUtils.showShort(MinePrivateLetterInfoActivity.this, response.body().getMsg());
                        mine_private_input_text.setText("");
                        initData();
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MinePrivateLetterInfoActivity.this, response.body().getMsg(), 0);
                        MoudleUtils.dialogDismiss(dialog);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.show(MinePrivateLetterInfoActivity.this, response.body().getMsg(), 0);
                        MoudleUtils.dialogDismiss(dialog);
                    }
                } else {
                    MoudleUtils.dialogDismiss(dialog);
                }
                showFoot();

            }

            @Override
            public void onFailure(Call<PushLetterBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MinePrivateLetterInfoActivity.this);
                MoudleUtils.dialogDismiss(dialog);
                showFoot();
            }
        });
    }
}
