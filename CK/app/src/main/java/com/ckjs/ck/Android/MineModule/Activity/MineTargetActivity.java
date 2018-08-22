package com.ckjs.ck.Android.MineModule.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ckjs.ck.Tool.ViewTool.WheelView;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetMyGoalsBean;
import com.ckjs.ck.Bean.UpdateGoalsBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.KeyBoardUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MineTargetActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private static final String[] STEPNUMBER = new String[]{"3000", "6000", "9000", "12000", "15000", "18000", "21000", "24000", "27000", "30000"};
    private static final String[] SLEEPTIME = new String[]{"5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};
    private EditText mCalTarget;
    private int tvProgress;
    private LinearLayout mStepNumber;
    private LinearLayout mSleepTime;
    private TextView tvStepNumber;
    private TextView tvSleepTime;
    private String sleepTime;
    private String stepNumber;
    private TextView button;
    private KyLoadingBuilder builder;
    private SeekBar mseefbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_target);
        builder = new KyLoadingBuilder(this);
        initId();
        initToolbar();
        initData();
    }

    private void initData() {
        MoudleUtils.kyloadingShow(builder);
        WfApi restApi = RetrofitUtils.retrofit.create(WfApi.class);
        int user_id = (int) (SPUtils.get(MineTargetActivity.this, "user_id", 0));
        String token = (String) SPUtils.get(MineTargetActivity.this, "token", "");
        Call<GetMyGoalsBean> callBack = restApi.mygoals(user_id, token);

        callBack.enqueue(new Callback<GetMyGoalsBean>() {
            @Override
            public void onResponse(Call<GetMyGoalsBean> call, Response<GetMyGoalsBean> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("1")) {
                        initUpUiTask(response);
                    } else if (response.body().getStatus().equals("0")) {
                        ToastUtils.show(MineTargetActivity.this, response.body().getMsg(), 0);
                    } else if (response.body().getStatus().equals("2")) {
                        ToastUtils.showShort(MineTargetActivity.this, response.body().getMsg());
                    }
                }
                MoudleUtils.kyloadingDismiss(builder);
            }

            @Override
            public void onFailure(Call<GetMyGoalsBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineTargetActivity.this);
                MoudleUtils.kyloadingDismiss(builder);
            }
        });

    }

    private void initUpUiTask(Response<GetMyGoalsBean> response) {
        mseefbar.setProgress(Integer.parseInt(response.body().getInfo().getFat()));
        tvSleepTime.setText(response.body().getInfo().getSleep() + "小时");
        tvStepNumber.setText(response.body().getInfo().getStep() + "步");
    }

    private void initId() {
        tvStepNumber = (TextView) findViewById(R.id.mine_target_stepnumber_text);
        tvSleepTime = (TextView) findViewById(R.id.mine_target_sleeptime_text);
        mSleepTime = (LinearLayout) findViewById(R.id.mine_target_sleeptime);
        mStepNumber = (LinearLayout) findViewById(R.id.mine_target_stepnumber);
        mCalTarget = (EditText) findViewById(R.id.mine_target_cal);
        mCalTarget.addTextChangedListener(textWatcher);

        mseefbar = (SeekBar) findViewById(R.id.mine_target_seekbar);

        mseefbar.setOnSeekBarChangeListener(this);
        mSleepTime.setOnClickListener(this);
        mStepNumber.setOnClickListener(this);
        dialog = new ProgressDialog(this);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                if (mCalTarget.getText().toString().trim().equals("")) {
                    tvProgress = 0;
                    mCalTarget.setText("0");
                }
                tvProgress = Integer.parseInt(mCalTarget.getText().toString().trim());

                if (0 <= tvProgress && tvProgress <= 3000) {
                    mseefbar.setProgress(tvProgress);
                    mCalTarget.setSelection(mCalTarget.getText().length());
                } else {
                    ToastUtils.showShortNotInternet("请输入1-3000之内的合理数值!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            tvProgress = Integer.parseInt(mCalTarget.getText().toString().trim());
        }
    };

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("目标设置");
        setSupportActionBar(toolbar);

        button = (TextView) findViewById(R.id.toolbar_button);
        button.setText("保存");
        button.setOnClickListener(this);
        RelativeLayout r_toolbar_button = (RelativeLayout) findViewById(R.id.r_toolbar_button);
        r_toolbar_button.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }

    //设置返回键可用
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


    //  监听seekbar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mCalTarget.setText(String.valueOf(seekBar.getProgress()));
        tvProgress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        tvProgress = seekBar.getProgress();
        //        SPUtils.put(this,"progress",progress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_target_stepnumber:
                toSetStepNumber();
                break;
            case R.id.mine_target_sleeptime:
                toSetSleepTime();
                break;
            case R.id.toolbar_button:
            case R.id.r_toolbar_button:
                if (tvProgress >= 0 && tvProgress <= 3000) {
                    toSetgoals();
                    KeyBoardUtils.closeKeyboard(mCalTarget, MineTargetActivity.this);
                } else {
                    ToastUtils.showShortNotInternet("请输入1-3000之内的合理数值!");
                }
                break;
        }
    }

    private ProgressDialog dialog;

    private void toSetgoals() {
        MoudleUtils.dialogShow(dialog);
        WfApi userInfoApi = RetrofitUtils.retrofit.create(WfApi.class);
        String token = (String) SPUtils.get(this, "token", "");
        int user_id = (int) SPUtils.get(this, "user_id", 0);
        //TODO
        Call<UpdateGoalsBean> callBack = userInfoApi.upmygoals(user_id, token, tvProgress + "", stepNumber, sleepTime);

        callBack.enqueue(new Callback<UpdateGoalsBean>() {
            @Override
            public void onResponse(Call<UpdateGoalsBean> call, Response<UpdateGoalsBean> response) {
                UpdateGoalsBean userinfobean = response.body();
                toLoginFinishNext(userinfobean);//成功后的操作
                MoudleUtils.dialogDismiss(dialog);
            }

            @Override
            public void onFailure(Call<UpdateGoalsBean> call, Throwable t) {
                MoudleUtils.toChekWifi(MineTargetActivity.this);
                MoudleUtils.dialogDismiss(dialog);

            }
        });

    }

    private void toLoginFinishNext(UpdateGoalsBean userinfobean) {
        if (userinfobean != null) {
            if (userinfobean.getStatus().equals("1")) {
                ToastUtils.show(MineTargetActivity.this, userinfobean.getMsg(), 0);
                finish();
                KeyBoardUtils.closeKeyboard(mCalTarget, MineTargetActivity.this);
            } else if (userinfobean.getStatus().equals("0")) {
                ToastUtils.show(MineTargetActivity.this, userinfobean.getMsg() + ",信息无变化", 0);
            } else if (userinfobean.getStatus().equals("2")) {
                MoudleUtils.initStatusTwo(MineTargetActivity.this, true);
            }
        }
    }


    private void toSetSleepTime() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MineTargetActivity.this);
        View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
        final WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        final Button buttonSure = (Button) outerView.findViewById(R.id.sure);

        wv.setOffset(2);
        wv.setItems(Arrays.asList(SLEEPTIME));
        wv.setSeletion(3);
        alertDialog.setView(outerView).create();
        final AlertDialog alert = alertDialog.create();
        alert.setTitle("设置睡眠时间");

        buttonSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleepTime = wv.getSeletedItem();
                tvSleepTime.setText(wv.getSeletedItem() + "小时");
                alert.dismiss();
            }
        });
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, final String item) {
                //滚轮滑动事件
            }
        });
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        alert.show();

    }


    private void toSetStepNumber() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MineTargetActivity.this);
        View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
        final WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        final Button buttonSure = (Button) outerView.findViewById(R.id.sure);

        wv.setOffset(2);
        wv.setItems(Arrays.asList(STEPNUMBER));
        wv.setSeletion(3);
        alertDialog.setView(outerView).create();
        final AlertDialog alert = alertDialog.create();
        alert.setTitle("设置步数");
        buttonSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepNumber = wv.getSeletedItem();
                tvStepNumber.setText(wv.getSeletedItem() + "步");
                alert.dismiss();
            }
        });
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        alert.show();

    }


}

