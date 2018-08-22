package com.ckjs.ck.Android.CkCircleModule.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.CkCircleModule.Adapter.ListAdapterCkCircleMore;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.CircleReportBean;
import com.ckjs.ck.Bean.CommentBean;
import com.ckjs.ck.Bean.CrinfoBean;
import com.ckjs.ck.Bean.DelcommentBean;
import com.ckjs.ck.Bean.FansBean;
import com.ckjs.ck.Bean.GetcommentBean;
import com.ckjs.ck.Interface.TaskResultApi;
import com.ckjs.ck.Interface.TaskResultTwoApi;
import com.ckjs.ck.Manager.NotifyCircleRefrushLookMessageManager;
import com.ckjs.ck.Manager.NotifyPlManager;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.FrescoUtils;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ScreenUtils;
import com.ckjs.ck.Tool.ToastUtils;
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
public class CircleRecommenMoreActivity extends AppCompatActivity implements TaskResultApi, TaskResultTwoApi, View.OnClickListener {
    private int user_id;
    private int circle_id;
    private CrinfoBean bean;
    private GetcommentBean beanTwo;
    private SimpleDraweeView ivLook;
    private SimpleDraweeView ivMine;
    private TextView tvContent;
    private TextView tvTime;
    private TextView tvPlNum;
    private TextView tvGuanZ;
    private TextView tvName;
    private ListView listView;
    private ListAdapterCkCircleMore listAdapterCkCircleMore;
    private List<GetcommentBean.GetcommentBeanInfo> listAll = new ArrayList<>();
    private int page = 1;
    private boolean flag;
    private EditText etText;
    private TextView tv_post_pl;
    private RelativeLayout activity_circle_recommen_more;
    private boolean flagPage;
    private boolean flagTask;
    private int num;
    private ProgressDialog dialog;
    private SimpleDraweeView sd_xb;
    private KyLoadingBuilder builder;
    private RelativeLayout r;
    private SimpleDraweeView sd_pic;
    private int p;
    private String name = "";
    private String add_id = "0";
    private boolean keyBoardShown;
    private View footView;
    private View headView;
    private boolean plFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_recommen_more);
        builder = new KyLoadingBuilder(this);
        dialog = new ProgressDialog(this);
        headView = LayoutInflater.from(this).inflate(
                R.layout.cir_more_head, null);
        footView = LayoutInflater.from(this).inflate(
                R.layout.next_foot_pl, null);
        initId();
        initToolbar();
        inidata();
        initGetIntentData();
        initTaskXiangQingData();
        initTaskPingLunData();
        initSet();
        onLayoutChangeEt();
    }

    /**
     * 点击某一评论进行二级评论提示
     */
    private void initSet() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 && position != listAll.size() + 1) {
                    if (listAll == null) {
                        return;
                    }
                    if (listAll.size() > 0) {
                        int user_id = (int) SPUtils.get("user_id", 0);
                        if ((user_id + "").equals(listAll.get(position - 1).getUser_id())) {
                            toinitToDeleteMyPl(position - 1, listAll.get(position - 1).getComment_id());
                        } else {
                            KeyBoardUtils.openKeyboard(etText, CircleRecommenMoreActivity.this);
                            initSetHintEt("回复" + listAll.get(position - 1).getUsername() + ":", listAll.get(position - 1).getUser_id());
                        }
                    }
                }
            }
        });
    }

    /**
     * 举报弹窗
     *
     * @param comment_id
     */
    public void toinitToDeleteMyPl(final int p, final String comment_id) {
        final CharSequence[] items = {"删除此条评论", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        initToDeleteMyPl(p, comment_id);
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 删除自己的评论接口
     */
    private void initToDeleteMyPl(final int p, String comment_id) {
        String token = "";
        token = (String) SPUtils.get(this, "token", token);
        if (token.equals("")) {
            MoudleUtils.initToLogin(this);
            finish();
        } else {
            MoudleUtils.dialogShow(dialog);
            int user_id = (int) SPUtils.get(this, "user_id", 0);
            Call<DelcommentBean> call = RetrofitUtils.retrofit.create(NpApi.class).delcomment(comment_id, token, user_id);
            call.enqueue(new Callback<DelcommentBean>() {
                @Override
                public void onResponse(Call<DelcommentBean> call, Response<DelcommentBean> response) {
                    DelcommentBean bean = response.body();
                    if (bean != null) {
                        ToastUtils.showShort(CircleRecommenMoreActivity.this, bean.getMsg());
                        if (bean.getStatus().equals("1")) {
                            if (listAll != null) {
                                if (listAll.size() > 0) {
                                    listAll.remove(p);
                                    listAdapterCkCircleMore.notifyDataSetChanged();
                                    num = listAll.size();
                                    MoudleUtils.textViewSetText(tvPlNum, "全部评论        " + num);
                                    initToRefrushWcPl();
                                }
                            }
                        }
                    }
                    MoudleUtils.dialogDismiss(dialog);
                }

                @Override
                public void onFailure(Call<DelcommentBean> call, Throwable t) {
                    MoudleUtils.dialogDismiss(dialog);
                    MoudleUtils.toChekWifi(CircleRecommenMoreActivity.this);

                }
            });
        }
    }

    /**
     * 二级回复时提示所回复的用户名
     *
     * @param s
     */
    private void initSetHintEt(String s, String add_id) {
        if (etText != null) {
            if (s == null) {
                s = "";
            }
            etText.setHint(s);
        }
        this.add_id = add_id;
    }


    /**
     * 进行用户头像显示
     */
    private void inidata() {
        FrescoUtils.setImage(ivMine, AppConfig.url + SPUtils.get(this, "picurl", ""));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("动态详情");

        RelativeLayout r_toolbar_button = (RelativeLayout) findViewById(R.id.r_toolbar_button);
        r_toolbar_button.setOnClickListener(this);
        SimpleDraweeView sd_button = (SimpleDraweeView) findViewById(R.id.sd_button);
        sd_button.setOnClickListener(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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
            KeyBoardUtils.closeKeyboard(etText, this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 获取帖子详情接口
     */
    private void initTaskXiangQingData() {
        MoudleUtils.kyloadingShow(builder);
        int user_id = (int) SPUtils.get(this, "user_id", 0);
        Call<CrinfoBean> call = RetrofitUtils.retrofit.create(NpApi.class).cirinfo(user_id, circle_id);
        call.enqueue(new Callback<CrinfoBean>() {
            @Override
            public void onResponse(Call<CrinfoBean> call, Response<CrinfoBean> response) {
                CrinfoBean bean = response.body();
                initResultData(bean);
            }

            @Override
            public void onFailure(Call<CrinfoBean> call, Throwable t) {

                //                MoudleUtils.toChekWifi(CircleRecommenMoreActivity.this);

            }
        });

    }

    /**
     * 获取评论接口
     */
    private void initTaskPingLunData() {

        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        String token = "";
        token = (String) SPUtils.get("token", token);
        Call<GetcommentBean> call = RetrofitUtils.retrofit.create(NpApi.class).getcomment(user_id, token, circle_id, page);
        call.enqueue(new Callback<GetcommentBean>() {
            @Override
            public void onResponse(Call<GetcommentBean> call, Response<GetcommentBean> response) {
                GetcommentBean bean = response.body();
                initResultPlData(bean);
                MoudleUtils.kyloadingDismiss(builder);

            }

            @Override
            public void onFailure(Call<GetcommentBean> call, Throwable t) {

                MoudleUtils.toChekWifi(CircleRecommenMoreActivity.this);
                MoudleUtils.kyloadingDismiss(builder);

            }
        });

    }

    /**
     * 获取评论接口成功后进行外层各个模块的回掉刷新外层评论内容
     *
     * @param bean
     */
    private void initResultPlData(GetcommentBean bean) {
        this.beanTwo = bean;
        if (bean != null) {
            initTwoStatusOne();
            initTwoStatusZero();
            initTwoStatusTwo();
            initToRefrushWcPl();
        }
        flagTask = true;
    }

    /**
     * 刷新外层评论
     */
    private void initToRefrushWcPl() {
        if (page == 1) {
            if (listAll != null) {
                if (name != null) {
                    switch (name) {
                        case "look":
                            NotifyPlManager.getInstance().sendPlLookFlag(true, listAll, p);
                            break;
                        case "mine":
                            NotifyPlManager.getInstance().sendPlMineFlag(true, listAll, p);
                            break;
                        case "recommend":
                            NotifyPlManager.getInstance().sendPlFlag(true, listAll, p);
                            break;
                        case "gz":
                            NotifyPlManager.getInstance().sendPlGzFlag(true, listAll, p);
                            break;
                    }
                }
            }
        }
    }


    @Override
    public void initTwoStatusOne() {
        if (beanTwo != null) {
            if (beanTwo.getStatus().equals("1")) {
                flagPage = true;
                initListadapter(beanTwo);
                initSetScrol();
                etText.setText("");
            }
        }
    }

    @Override
    public void initTwoStatusZero() {
        if (beanTwo != null) {
            if (beanTwo.getStatus().equals("0")) {
                ToastUtils.showShort(CircleRecommenMoreActivity.this, beanTwo.getMsg());
            }
        }
    }

    @Override
    public void initTwoStatusTwo() {
        if (beanTwo.getStatus().equals("2")) {
            ToastUtils.showShort(CircleRecommenMoreActivity.this, beanTwo.getMsg());
        }
    }

    /**
     * 上拉加载
     */
    private void initSetScrol() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
                            if (flagPage) {
                                flagPage = flag;
                                page++;
                            }
                            if (flagTask) {
                                flagTask = flag;
                                initTaskPingLunData();
                            }
                        }
                        //判断滚动到顶部
                        if (listView.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && !flag) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });
    }

    /**
     * listView绑定数据
     *
     * @param bean
     */
    private void initListadapter(GetcommentBean bean) {
        List<GetcommentBean.GetcommentBeanInfo> list = new ArrayList<>();
        list = bean.getInfo();
        if (list == null) {
            list = new ArrayList<>();
        }
        if (page == 1) {
            listAll.clear();
        }
        listAll.addAll(list);
        if (listAdapterCkCircleMore == null) {
            listAdapterCkCircleMore = new ListAdapterCkCircleMore(this);
            listAdapterCkCircleMore.setList(listAll);
            listView.setAdapter(listAdapterCkCircleMore);
        } else {
            listAdapterCkCircleMore.setList(listAll);
            listAdapterCkCircleMore.notifyDataSetChanged();
        }
        num = listAll.size();
        MoudleUtils.textViewSetText(tvPlNum, "全部评论        " + num);
        if (plFlag) {
            //让列表默认显示最后一行
            if (listAll.size() > 0) {
                if (page == 1) {
                    listView.setSelection(1);
                }
            }
        }
    }

    /**
     * 帖子详情接口获取数据的处理
     *
     * @param bean
     */
    private void initResultData(final CrinfoBean bean) {
        this.bean = bean;
        if (bean != null) {
            initStatusOne();
            initStatusZero();
            initStatusTwo();
        }
    }

    /**
     * 获取上层页面传递的数据
     */
    private void initGetIntentData() {
        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id", 0);
        circle_id = intent.getIntExtra("circle_id", 0);
        p = intent.getIntExtra("p", 0);
        name = intent.getStringExtra("name");

    }

    private void initId() {
        r = (RelativeLayout) headView.findViewById(R.id.r);
        ivLook = (SimpleDraweeView) headView.findViewById(R.id.sdIcon);
        sd_xb = (SimpleDraweeView) headView.findViewById(R.id.sd_xb);
        tvContent = (TextView) headView.findViewById(R.id.tv_conten);
        tvPlNum = (TextView) headView.findViewById(R.id.tv_num);
        tvTime = (TextView) headView.findViewById(R.id.textViewTime);
        tvGuanZ = (TextView) headView.findViewById(R.id.textViewGz);
        tvGuanZ.setOnClickListener(this);
        tvName = (TextView) headView.findViewById(R.id.textViewName);
        sd_pic = (SimpleDraweeView) headView.findViewById(R.id.sd_pic);


        listView = (ListView) findViewById(R.id.listViewPL);
        listView.addHeaderView(headView);
        listView.addFooterView(footView);
        etText = (EditText) findViewById(R.id.text);
        tv_post_pl = (TextView) findViewById(R.id.tv_post_pl);
        SimpleDraweeView button = (SimpleDraweeView) findViewById(R.id.sd_button);
        FrescoUtils.setImage(button, AppConfig.res + R.drawable.mpre_report);
        button.setOnClickListener(this);
        activity_circle_recommen_more = (RelativeLayout) findViewById(R.id.activity_circle_recommen_more);
        tv_post_pl.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        ivMine = (SimpleDraweeView) findViewById(R.id.sdIconMine);


    }

    /**
     * 帖子详情获取接口的数据status为1时的处理
     */
    @Override
    public void initStatusOne() {
        if (bean != null) {
            if (bean.getStatus().equals("1")) {
                MoudleUtils.viewShow(r);
                if (bean.getInfo().getCircle().getPicture().size() > 0) {
                    FrescoUtils.setControllerListener(sd_pic, AppConfig.url + bean.getInfo().getCircle().getPicture().get(0).toString(), ScreenUtils.getScreenWidth(CircleRecommenMoreActivity.this));
                    sd_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (bean.getInfo().getCircle().getPicture() != null) {
                                if (bean.getInfo().getCircle().getPicture().size() > 0) {
                                    Intent intent = new Intent(CircleRecommenMoreActivity.this, CircleIcBigActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArrayList("getPicture", (ArrayList<String>) bean.getInfo().getCircle().getPicture());
                                    intent.putExtras(bundle);
                                    intent.putExtra("p", 0);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                } else {
                    //                    r.setVisibility(View.GONE);
                    MoudleUtils.viewGone(r);
                }
                FrescoUtils.setImage(ivLook, AppConfig.url + bean.getInfo().getCircle().getPicurl());
                if (bean.getInfo().getCircle().getContent() != null) {
                    if (bean.getInfo().getCircle().getContent().equals("")) {
                        //                        tvContent.setVisibility(View.GONE);
                        MoudleUtils.viewGone(tvContent);
                    } else {
                        //                        tvContent.setVisibility(View.VISIBLE);
                        MoudleUtils.viewShow(tvContent);
                        MoudleUtils.textViewSetText(tvContent, bean.getInfo().getCircle().getContent());
                    }
                } else {
                    tvContent.setVisibility(View.GONE);
                }
                MoudleUtils.textViewSetText(tvTime, bean.getInfo().getCircle().getTime());
                MoudleUtils.textViewSetText(tvName, bean.getInfo().getCircle().getUsername());
                if (bean.getInfo().getCircle().getSex().equals("2")) {
                    FrescoUtils.setImage(sd_xb, AppConfig.res + R.drawable.my_girl);
                } else if (bean.getInfo().getCircle().getSex().equals("1")) {
                    FrescoUtils.setImage(sd_xb, AppConfig.res + R.drawable.my_boy);
                } else {
                    FrescoUtils.setImage(sd_xb, AppConfig.res + R.drawable.my_boy);
                }
                num = bean.getInfo().getCircle().getComnum();
                MoudleUtils.textViewSetText(tvPlNum, "全部评论        " + num);
                if (bean.getInfo().getFanst() == 1) {
                    //                    tvGuanZ.setVisibility(View.INVISIBLE);
                    MoudleUtils.viewInvisibily(tvGuanZ);
                } else if (bean.getInfo().getFanst() == 0) {
                    int user_id = (int) SPUtils.get(this, "user_id", 0);
                    if (user_id != this.user_id) {
                        //                        tvGuanZ.setVisibility(View.VISIBLE);
                        MoudleUtils.viewShow(tvGuanZ);
                    }
                }
                activity_circle_recommen_more.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void initStatusZero() {
        if (bean != null) {
            if (bean.getStatus().equals("0")) {
                ToastUtils.showShort(this, bean.getMsg());
            }
        }
    }

    @Override
    public void initStatusTwo() {
        if (bean != null) {
            if (bean.getStatus().equals("2")) {
                ToastUtils.showShort(CircleRecommenMoreActivity.this, bean.getMsg());

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_post_pl:
                String s = etText.getText().toString().trim();
                KeyBoardUtils.closeKeyboard(etText, this);
                initPostPl(s);
                break;
            case R.id.textViewGz:
                initToGz();
                break;
            case R.id.sd_button:
            case R.id.r_toolbar_button:
                toReport();
                break;

        }
    }

    /**
     * 举报接口
     */
    private void initToReport() {
        String token = "";
        token = (String) SPUtils.get(this, "token", token);
        if (token.equals("")) {
            MoudleUtils.initToLogin(this);
            finish();
        } else {
            //TODO
            MoudleUtils.dialogShow(dialog);
            WfApi userInfoApi = RetrofitUtils.retrofit.create(WfApi.class);
            int user_id = (int) SPUtils.get(this, "user_id", 0);

            //TODO
            Call<CircleReportBean> callBack = userInfoApi.report(user_id, token, circle_id);

            callBack.enqueue(new Callback<CircleReportBean>() {
                @Override
                public void onResponse(Call<CircleReportBean> call, Response<CircleReportBean> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("0")) {
                            ToastUtils.showShort(CircleRecommenMoreActivity.this, response.body().getMsg());
                        } else if (response.body().getStatus().equals("1")) {
                            ToastUtils.showShort(CircleRecommenMoreActivity.this, response.body().getMsg());
                        } else if (response.body().getStatus().equals("2")) {
                            ToastUtils.showShort(CircleRecommenMoreActivity.this, response.body().getMsg());
                        } else {

                        }
                    } else {
                        MoudleUtils.toChekWifi(CircleRecommenMoreActivity.this);
                    }

                    MoudleUtils.dialogDismiss(dialog);
                }

                @Override
                public void onFailure(Call<CircleReportBean> call, Throwable t) {

                    MoudleUtils.toChekWifi(CircleRecommenMoreActivity.this);
                    MoudleUtils.dialogDismiss(dialog);

                }
            });
        }
    }

    /**
     * 举报弹窗
     */
    public void toReport() {
        final CharSequence[] items = {"举报", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        initToReport();
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 去关注
     */
    private void initToGz() {
        String token = "";
        token = (String) SPUtils.get(this, "token", token);
        int user_id = (int) SPUtils.get(this, "user_id", 0);
        if (token.equals("")) {
            MoudleUtils.initToLogin(this);
            finish();
        } else {
            if (user_id != this.user_id) {
                initTaskGz(token, user_id);
            }
        }

    }

    /**
     * 关注接口
     *
     * @param token
     * @param user_id
     */
    private void initTaskGz(String token, int user_id) {
        //        MoudleUtils.dialogShow(dialog);
        Call<FansBean> call = RetrofitUtils.retrofit.create(NpApi.class).focus(token, user_id, this.user_id);
        call.enqueue(new Callback<FansBean>() {
            @Override
            public void onResponse(Call<FansBean> call, Response<FansBean> response) {
                FansBean bean = response.body();
                if (bean != null) {
                    switch (bean.getStatus()) {
                        case "1":
                            ToastUtils.showShort(CircleRecommenMoreActivity.this, bean.getMsg());
                            tvGuanZ.setText("已关注");
                            tvGuanZ.setVisibility(View.INVISIBLE);
                            NotifyCircleRefrushLookMessageManager.getInstance().sendNotifyCircleRefrushFlag(true);
                            break;
                        case "0":
                            ToastUtils.showShort(CircleRecommenMoreActivity.this, bean.getMsg());

                            break;
                        case "2":
                            ToastUtils.showShort(CircleRecommenMoreActivity.this, bean.getMsg());

                            break;
                    }
                }
                //                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<FansBean> call, Throwable t) {
                //                MoudleUtils.dialogDismiss(dialog);
                MoudleUtils.toChekWifi(CircleRecommenMoreActivity.this);

            }
        });
    }

    /**
     * 进行评论
     *
     * @param s
     */
    private void initPostPl(String s) {
        String token = "";
        token = (String) SPUtils.get(this, "token", token);
        if (token.equals("")) {
            MoudleUtils.initToLogin(this);
            finish();
        } else {
            if (s != null && !s.equals("")) {
                initTaskPostPl(token, s);
            } else {
                ToastUtils.showShort(this, "亲，您尚未进行评论");
            }
        }
    }


    /**
     * 评论接口
     *
     * @param token
     * @param s
     */
    private void initTaskPostPl(String token, String s) {
        MoudleUtils.dialogShow(dialog);
        int user_id = (int) SPUtils.get(this, "user_id", 0);
        Call<CommentBean> call = RetrofitUtils.retrofit.create(NpApi.class).comment(token, user_id, circle_id, s, add_id);
        call.enqueue(new Callback<CommentBean>() {
            @Override
            public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {
                CommentBean bean = response.body();
                if (bean != null) {
                    switch (bean.getStatus()) {
                        case "1":
                            plFlag = true;
                            initEtClrear();
                            ToastUtils.showShort(CircleRecommenMoreActivity.this, bean.getMsg());
                            page = 1;
                            initTaskPingLunData();
                            break;
                        case "0":
                            ToastUtils.showShort(CircleRecommenMoreActivity.this, bean.getMsg());

                            break;
                        case "2":
                            ToastUtils.showShort(CircleRecommenMoreActivity.this, bean.getMsg());

                            break;
                    }
                }
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<CommentBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialog);
                MoudleUtils.toChekWifi(CircleRecommenMoreActivity.this);

            }
        });

    }


    /**
     * 监听软件盘的弹出收起进行回复用户清除
     */
    private View view;

    private void onLayoutChangeEt() {
        view = findViewById(R.id.r_cir);

        //给该layout设置监听，监听其布局发生变化事件
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                initJpTq(view);

            }
        });
    }


    private void initJpTq(View view) {
        final int softKeyboardHeight = 100;
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        int heightDiff = view.getRootView().getHeight() - view.getHeight();
        //        ToastUtils.showShort("-----------------h:"+heightDiff);
        // 大于100像素，是打开的情况
        if (heightDiff > softKeyboardHeight * dm.density) {
            keyBoardShown = true;
            // 如果已经打开软键盘，就不理会

            // do something when keyboard show，
            // i.e. listView or recyclerView scrolls to bottom

            return;
        } else {
            keyBoardShown = false;
            if (!keyBoardShown) {
                if (etText.getText().toString().trim().equals("")) {
                    initEtClrear();
                }
            }
        }
    }

    /**
     * 隐藏软键盘时进行回复用户清除
     */
    private void initEtClrear() {
        initSetHintEt("", "0");
        if (etText != null) {
            etText.setText("");
        }
    }

}
