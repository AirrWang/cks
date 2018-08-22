package com.ckjs.ck.Android.MineModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Android.CkCircleModule.Activity.CircleRecommenMoreActivity;
import com.ckjs.ck.Android.MineModule.Adapter.ListAdapterMineFocusPeople;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.FansBean;
import com.ckjs.ck.Bean.FocusarchivesBean;
import com.ckjs.ck.Bean.GetMyfocusCircleBean;
import com.ckjs.ck.Bean.GetMyfocusDetailBean;
import com.ckjs.ck.Bean.GetcommentBean;
import com.ckjs.ck.Bean.UnFocusBean;
import com.ckjs.ck.Manager.NotifyActivityBzManager;
import com.ckjs.ck.Manager.NotifyActivityFsManager;
import com.ckjs.ck.Manager.NotifyActivityGzManager;
import com.ckjs.ck.Manager.NotifyPlManager;
import com.ckjs.ck.Manager.NotifyToMineAtentionAcFxManager;
import com.ckjs.ck.Message.NotifyPlGzMessage;
import com.ckjs.ck.Message.NotifyToMineAtentionAcFxMessage;
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
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class MineAttentionPeople extends AppCompatActivity implements View.OnClickListener, NotifyToMineAtentionAcFxMessage, NotifyPlGzMessage {

    private SimpleDraweeView mFocusPic;
    private TextView mFocusName;
    private TextView mFocusGym;
    private int isFocus = 0;
    private boolean flag;
    private GetMyfocusDetailBean body;
    private ListView mListView;
    private int page = 1;
    private View footView;
    private View headView;
    private List<GetMyfocusCircleBean.GetMyfocusCircleInfoBean> listAll = new ArrayList<>();

    private TextView tvZW;
    private KyLoadingBuilder builder;
    private SimpleDraweeView mFocusSex;
    private SimpleDraweeView mFocusLevel;
    private TextView mFocusSignname;
    private SimpleDraweeView mFocusHide;
    private int n;
    private SimpleDraweeView mine_attention_gym_icon;
    private SimpleDraweeView sd_button;
    private SimpleDraweeView mine_attention_info;
    private TextView tv_bz;
    private String aliasname;
    private RelativeLayout r_toolbar_button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_attention_people);
        builder = new KyLoadingBuilder(this);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot, null);
        headView = LayoutInflater.from(this).inflate(R.layout.activity_mine_attention_people_top, null);
        initID();
        initThis();
        initToolbar();
        initTask();
        initTaskB();
        initSet();
    }

    private void initThis() {
        NotifyToMineAtentionAcFxManager.getInstance().setNotifyMessage(this);
        NotifyPlManager.getInstance().setNotifyMessage(this);

    }

    /**
     * 个人帖子列表接口
     */
    private void initTaskB() {
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        String token = "";
        token = (String) SPUtils.get("token", token);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Intent intent = getIntent();
        int focus_id = Integer.parseInt(intent.getStringExtra("focus_id"));

        Call<GetMyfocusCircleBean> callBack = restApi.focuscircle(focus_id, page, user_id, token);

        callBack.enqueue(new Callback<GetMyfocusCircleBean>() {
            @Override
            public void onResponse(Call<GetMyfocusCircleBean> call, Response<GetMyfocusCircleBean> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            if (page == 1) {
                                listAll.clear();
                            }
                            listAll.addAll(response.body().getInfo());
                            initBottomUiTask(listAll);
                            page++;
                        } else if (response.body().getStatus().equals("0")) {
                            ToastUtils.show(MineAttentionPeople.this, response.body().getMsg(), 0);
                        } else if (response.body().getStatus().equals("2")) {
                            ToastUtils.show(MineAttentionPeople.this, response.body().getMsg(), 0);
                        }
                        showFoot();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<GetMyfocusCircleBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineAttentionPeople.this);
                showFoot();
                MoudleUtils.kyloadingDismiss(builder);

            }
        });
    }

    private void initSet() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (mListView.getLastVisiblePosition() == (mListView.getCount() - 1)) {
                            if (footView.getVisibility() != View.VISIBLE) {
                                footView.setVisibility(View.VISIBLE);
                                initTaskB();
                            }

                        }
                        //判断滚动到顶部
                        if (mListView.getFirstVisiblePosition() == 0) {

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

    private void initID() {
        tv_bz = (TextView) headView.findViewById(R.id.tv_bz);
        mine_attention_info = (SimpleDraweeView) headView.findViewById(R.id.mine_attention_info);
        mine_attention_gym_icon = (SimpleDraweeView) headView.findViewById(R.id.mine_attention_gym_icon);
        mFocusSex = (SimpleDraweeView) headView.findViewById(R.id.mine_attention_sex);
        mFocusLevel = (SimpleDraweeView) headView.findViewById(R.id.mine_attention_level);
        mFocusSignname = (TextView) headView.findViewById(R.id.mine_attention_signname);
        mFocusHide = (SimpleDraweeView) headView.findViewById(R.id.mine_attention_focushide);
        tvZW = (TextView) findViewById(R.id.tv_zhanwei);
        mListView = (ListView) findViewById(R.id.list_mine_attention_people);
        mFocusPic = (SimpleDraweeView) headView.findViewById(R.id.mine_attention_pic_detail);
        mFocusName = (TextView) headView.findViewById(R.id.mine_attention_name_detail);
        mFocusGym = (TextView) headView.findViewById(R.id.mine_attention_gym);

        mFocusPic.setOnClickListener(this);
        mFocusHide.setOnClickListener(this);
        initFoot();
    }

    private void initFoot() {
        mListView.addFooterView(footView);
        mListView.addHeaderView(headView);
    }

    /**
     * 修改备注
     */
    private void toBz(String name) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog_report_input_bz, null);
        builder.setView(view);
        final EditText input = (EditText) view.findViewById(R.id.editText);
        input.setHint(name);
        final AlertDialog alert = builder.create();
        Button button = (Button) view.findViewById(R.id.button);
        Button button4 = (Button) view.findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = input.getText().toString().trim();
                if (s != null && !s.equals("")) {
                    if (!DataUtils.containsEmoji(s)) {
                        initBzTask(s, alert);
                    } else {
                        ToastUtils.showShortNotInternet("不支持输入Emoji表情符号");
                    }
                } else {
                    KeyBoardUtils.closeKeyboard(input, MineAttentionPeople.this);
                    ToastUtils.showShortNotInternet("备注名不能为空");
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

    private void initBzTask(final String s, final AlertDialog alert) {
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get(MineAttentionPeople.this, "user_id", 0));
        String token = (String) SPUtils.get(MineAttentionPeople.this, "token", "");
        Intent intent = getIntent();
        final int focus_id = Integer.parseInt(intent.getStringExtra("focus_id"));

        Call<AcceptBean> callBack = restApi.upaliasname(token, user_id, s, focus_id + "");

        callBack.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        page = 1;
                        initTask();
                        initTaskB();
                        NotifyActivityBzManager.getInstance().sendNotifyFlag(true, s, focus_id);
                        NotifyActivityFsManager.getInstance().sendNotifyFlag(true, s, focus_id);
                        ToastUtils.showShort(MineAttentionPeople.this, response.body().getMsg());
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MineAttentionPeople.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MineAttentionPeople.this, response.body().getMsg());
                    }
                }
                alert.dismiss();
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineAttentionPeople.this);
                alert.dismiss();
            }
        });
    }

    /**
     * 用户个人信息接口
     */
    private void initTask() {
        builder = new KyLoadingBuilder(MineAttentionPeople.this);
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MineAttentionPeople.this, "user_id", 0));
        String token = (String) SPUtils.get(MineAttentionPeople.this, "token", "");
        Intent intent = getIntent();
        int focus_id = Integer.parseInt(intent.getStringExtra("focus_id"));

        Call<GetMyfocusDetailBean> callBack = restApi.myfocusinfo(token, user_id, focus_id);

        callBack.enqueue(new Callback<GetMyfocusDetailBean>() {
            @Override
            public void onResponse(Call<GetMyfocusDetailBean> call, Response<GetMyfocusDetailBean> response) {
                try {
                    body = response.body();
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            initUpUiTask(response);
                        } else if (response.body().getStatus().equals("0")) {

                            ToastUtils.show(MineAttentionPeople.this, response.body().getMsg(), 0);
                        } else if (response.body().getStatus().equals("2")) {
                            ToastUtils.showShort(MineAttentionPeople.this, response.body().getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GetMyfocusDetailBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineAttentionPeople.this);
            }
        });
    }

    private void showFoot() {
        if (footView.getVisibility() == View.VISIBLE) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    private void initUpUiTask(final Response<GetMyfocusDetailBean> response) {

        if (response.body() != null) {
            if (response.body().getInfo() != null) {
                FrescoUtils.setImage(mFocusPic, AppConfig.url + response.body().getInfo().getPicurl());
                aliasname = response.body().getInfo().getAliasname();
                nc = response.body().getInfo().getUsername();

                if (aliasname != null && !aliasname.equals("")) {
                    mFocusName.setText(aliasname + "(" + nc + ")");
                } else {
                    mFocusName.setText(nc);
                }
                mFocusGym.setText(response.body().getInfo().getGym_name());
                if (response.body().getInfo().getSex().equals("1")) {
                    FrescoUtils.setImage(mFocusSex, "res://com.ckjs.ck/" + R.drawable.my_boy);
                } else if (response.body().getInfo().getSex().equals("2")) {
                    FrescoUtils.setImage(mFocusSex, "res://com.ckjs.ck/" + R.drawable.my_girl);
                } else {

                }
                mFocusSignname.setText(response.body().getInfo().getMotto());
                if (response.body().getInfo().getGym_id().equals("1")) {
                    FrescoUtils.setImage(mine_attention_gym_icon, AppConfig.res + R.drawable.my_member_icon_no);
                } else {
                    FrescoUtils.setImage(mine_attention_gym_icon, AppConfig.res + R.drawable.my_member_icon);
                }
                String myId = SPUtils.get(MineAttentionPeople.this, "user_id", 0) + "";
                final String focusId = response.body().getInfo().getFocus_id();
                if (myId.equals(focusId)) {//对比用户id和主页id，若一致则隐藏关注，修改备注，以及私信图标
                    //                    mFocusHide.setVisibility(View.GONE);
                    MoudleUtils.viewGone(mine_attention_info);
                    MoudleUtils.viewGone(mFocusHide);
                } else {
                    sd_button.setOnClickListener(this);
                    r_toolbar_button.setOnClickListener(this);
                    FrescoUtils.setImage(sd_button, AppConfig.res + R.drawable.ckq_letter);
                }

                FrescoUtils.setImage(mine_attention_info, "res://com.ckjs.ck/" + R.drawable.personal_record);
                //ismc1互相关注0未互相关注

                mine_attention_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initTaskPF(focusId);
                    }
                });
                tv_bz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFocus == 0) {
                            toBz(response.body().getInfo().getUsername());
                        } else {
                            ToastUtils.showShortNotInternet("还未关注对方");
                        }
                    }
                });
                if (Integer.parseInt(response.body().getInfo().getFanst()) == 1) {
                    FrescoUtils.setImage(mFocusHide, "res://com.ckjs.ck/" + R.drawable.personal_focus_pre);
                    isFocus = 0;
                    MoudleUtils.viewShow(tv_bz);
                } else if (Integer.parseInt(response.body().getInfo().getFanst()) == 0) {
                    FrescoUtils.setImage(mFocusHide, "res://com.ckjs.ck/" + R.drawable.personal_focus);
                    isFocus = 1;
                    MoudleUtils.viewGone(tv_bz);
                } else {

                }
                if (response.body().getInfo().getVip() != null) {
                    mFocusLevel.setVisibility(View.VISIBLE);
                    if (response.body().getInfo().getVip().equals("1")) {
                        FrescoUtils.setImage(mFocusLevel, "res://com.ckjs.ck/" + R.drawable.member_leve);
                    } else if (response.body().getInfo().getVip().equals("2")) {
                        FrescoUtils.setImage(mFocusLevel, "res://com.ckjs.ck/" + R.drawable.member_levetwo);
                    } else {
                        FrescoUtils.setImage(mFocusLevel, "");
                    }
                } else {
                    mFocusLevel.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initTaskPF(final String focusId) {
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        Call<FocusarchivesBean> callBack = restApi.focusarchives(token, user_id + "", focusId);

        callBack.enqueue(new Callback<FocusarchivesBean>() {

            @Override
            public void onResponse(Call<FocusarchivesBean> call, Response<FocusarchivesBean> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            startActivity(new Intent().putExtra("focusId", focusId).setClass(MineAttentionPeople.this, PersonalFilesActivity.class));
                        } else {
                            ToastUtils.showShort(MineAttentionPeople.this, response.body().getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<FocusarchivesBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("用户主页");
        setSupportActionBar(toolbar);
        r_toolbar_button = (RelativeLayout) findViewById(R.id.r_toolbar_button);
        sd_button = (SimpleDraweeView) findViewById(R.id.sd_button);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_attention_focushide:
                if (SPUtils.get(MineAttentionPeople.this, "token", "") != "") {
                    if (isFocus == 0) {
                        unfocus();
                    } else {
                        refocus();
                    }
                } else {
                    MoudleUtils.initStatusTwo(MineAttentionPeople.this, true);
                }
                break;
            case R.id.mine_attention_pic_detail:
                //跳转头像查看界面
                if (body != null) {
                    if (body.getInfo() != null) {
                        Intent intent = new Intent();
                        intent.putExtra("touxiang", body.getInfo().getPicurl());
                        intent.putExtra("type", "2");
                        intent.setClass(MineAttentionPeople.this, TouXiangActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.r_toolbar_button:
            case R.id.sd_button:
                Intent intent = new Intent();
                intent.putExtra("username", body.getInfo().getUsername());
                intent.putExtra("add_id", body.getInfo().getFocus_id());
                intent.setClass(MineAttentionPeople.this, MinePrivateLetterInfoActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 关注
     */
    private void refocus() {
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get(MineAttentionPeople.this, "user_id", 0));
        String token = (String) SPUtils.get(MineAttentionPeople.this, "token", "");
        Intent intent = getIntent();
        int focus_id = Integer.parseInt(intent.getStringExtra("focus_id"));

        Call<FansBean> callBack = restApi.focus(token, user_id, focus_id);

        callBack.enqueue(new Callback<FansBean>() {
            @Override
            public void onResponse(Call<FansBean> call, Response<FansBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        NotifyActivityGzManager.getInstance().sendNotifyFlag(true);
                        FrescoUtils.setImage(mFocusHide, "res://com.ckjs.ck/" + R.drawable.personal_focus_pre);
                        MoudleUtils.viewShow(tv_bz);
                        isFocus = 0;
                        //1互相关注0未互相关注
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MineAttentionPeople.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.show(MineAttentionPeople.this, response.body().getMsg(), 0);

                    }
                }
            }

            @Override
            public void onFailure(Call<FansBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineAttentionPeople.this);
            }
        });

    }

    private String nc;

    /**
     * 取消关注
     */
    private void unfocus() {

        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MineAttentionPeople.this, "user_id", 0));
        String token = (String) SPUtils.get(MineAttentionPeople.this, "token", "");
        Intent intent = getIntent();
        final int focus_id = Integer.parseInt(intent.getStringExtra("focus_id"));

        Call<UnFocusBean> callBack = restApi.unfocus(token, user_id, focus_id);

        callBack.enqueue(new Callback<UnFocusBean>() {
            @Override
            public void onResponse(Call<UnFocusBean> call, Response<UnFocusBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        if (nc != null && !nc.equals("")) {
                            NotifyActivityFsManager.getInstance().sendNotifyFlag(true, nc, focus_id);
                        }
                        NotifyActivityGzManager.getInstance().sendNotifyFlag(true);
                        FrescoUtils.setImage(mFocusHide, "res://com.ckjs.ck/" + R.drawable.personal_focus);
                        MoudleUtils.viewGone(tv_bz);
                        isFocus = 1;
                        if (aliasname != null && !aliasname.equals("")) {
                            page = 1;
                            initTask();
                            initTaskB();
                        }
                        //1互相关注0未互相关注
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MineAttentionPeople.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MineAttentionPeople.this, response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<UnFocusBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineAttentionPeople.this);
            }
        });

    }

    /**
     * listView绑定数据
     */
    private void initAdapter() {
        if (listAdapterMineAttention == null) {
            listAdapterMineAttention = new ListAdapterMineFocusPeople(this);
            listAdapterMineAttention.setList(listAll);
            mListView.setAdapter(listAdapterMineAttention);
        } else {
            listAdapterMineAttention.setList(listAll);
            listAdapterMineAttention.notifyDataSetChanged();
        }
    }

    private ListAdapterMineFocusPeople listAdapterMineAttention;

    /**
     * 上拉加载处理
     *
     * @param listAll
     */
    private void initBottomUiTask(final List<GetMyfocusCircleBean.GetMyfocusCircleInfoBean> listAll) {
        if (listAll.size() == 0) {
            tvZW.setVisibility(View.VISIBLE);
        }

        initAdapter();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 && position != listAll.size() + 1) {
                    Intent intent = new Intent();
                    intent.setClass(MineAttentionPeople.this, CircleRecommenMoreActivity.class);
                    intent.putExtra("circle_id", Integer.parseInt(listAll.get(position - 1).getId()));
                    intent.putExtra("user_id", Integer.parseInt(listAll.get(position - 1).getUser_id()));
                    intent.putExtra("name", "gz");
                    intent.putExtra("p", position - 1);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * 我的关注分享回掉
     *
     * @param flag
     * @param list
     * @param p
     */
    @Override
    public void sendMessageToMineAtentionAcFxFlag(boolean flag, List<GetMyfocusCircleBean.GetMyfocusCircleInfoBean> list, int p) {
        if (flag) {
            UMImage umImage;
            if (list.get(p).getPicture() != null) {
                if (list.get(p).getPicture().size() > 0) {
                    String imgerurl = list.get(p).getPicture().get(0);
                    umImage = new UMImage(MineAttentionPeople.this, AppConfig.url + imgerurl);
                } else {
                    umImage = new UMImage(MineAttentionPeople.this, R.drawable.app_icon);
                }
            } else {
                umImage = new UMImage(MineAttentionPeople.this, R.drawable.app_icon);
            }
            umImage.setThumb(umImage);
            String c = list.get(p).getContent();
            if (c == null || c.equals("")) {
                c = "暂无内容";
            }
            new ShareAction(MineAttentionPeople.this)
                    .withText(c)
                    .withTitle("超空圈分享")
                    .withTargetUrl("http://www.chaokongs.com/circle/circleinfo?id=" + list.get(p).getId())
                    .withMedia(umImage)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)
                    .setCallback(umShareListener).open();
        }
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            android.util.Log.d("plat", "platform" + platform);

            Toast.makeText(MineAttentionPeople.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MineAttentionPeople.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                android.util.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MineAttentionPeople.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void sendPlGzFlag(boolean flag, List<GetcommentBean.GetcommentBeanInfo> listAll, int p) {
        if (flag) {
            if (listAll != null) {
                if (listAll.size() > 0) {

                    if (listAll.size() == 1) {
                        n = 1;
                    } else if (listAll.size() >= 2) {
                        n = 2;
                    }
                    List<GetMyfocusCircleBean.GetMyfocusCircleInfoBean.GetMyfocusCircleCommentBean> comment = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        comment.add(new GetMyfocusCircleBean().new GetMyfocusCircleInfoBean().new GetMyfocusCircleCommentBean());
                        comment.get(i).setUsername(listAll.get(i).getUsername());
                        comment.get(i).setAddname(listAll.get(i).getAddname());
                        comment.get(i).setContent(listAll.get(i).getContent());
                        this.listAll.get(p).setComment(comment);
                        initAdapter();
                    }
                } else {
                    this.listAll.get(p).setComment(null);
                    initAdapter();
                }


            }
        }
    }
}
