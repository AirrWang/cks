package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.ckjs.ck.Tool.ViewTool.SignCalendar;
import com.ckjs.ck.Api.WfApi;
import com.ckjs.ck.Bean.GetSignBean;
import com.ckjs.ck.Bean.SignInBean;
import com.ckjs.ck.Manager.NotifyActivityToMessageManager;
import com.ckjs.ck.Manager.NotifySignBeanMessageManager;
import com.ckjs.ck.Manager.NotifyToHomeSignBeanMessageManager;
import com.ckjs.ck.Message.NotifySignBeanMessage;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.Location.Utils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.NetworkUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class PunchCardDialog extends Activity implements View.OnClickListener, NotifySignBeanMessage,
        AMapLocationListener {
    private String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式
    private TextView popupwindow_calendar_month;
    private SignCalendar calendar;
    private Button btn_signIn;
    boolean isinput = false;
    private String date1 = "";//单天日期
    private LinearLayout punchcard_back;
    private SimpleDraweeView sd_fh;
    private GetSignBean.GetSignInfoBean getSignInfoBean;
    private TextView puncherId;
    private TextView puncherTime;
    private TextView puncherFat;
    private boolean flag = true;
    private String home_startNodeStr = "";
    private TextView tv_home_startNodeStr;
    private KyLoadingBuilder buder;

    private AMapLocationClient mlocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_punchcard);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        //p.height = (int) (d.getHeight() * 1.0);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.8
        //p.alpha = 1.0f;      //设置本身透明度
        //p.dimAmount = 0.8f;      //设置黑暗度

        getWindow().setAttributes(p);

        //this.setFinishOnTouchOutside(false);
        initDw();
        init();
        initID();

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                home_startNodeStr = amapLocation.getAddress();
                if (tv_home_startNodeStr == null) {
                    return;
                }
                if (home_startNodeStr != null) {
                    tv_home_startNodeStr.setText(home_startNodeStr);
                }
            } else {
//                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
//                Log.e("AmapErr", errText);
                tv_home_startNodeStr.setText("定位失败!刷新一下");
            }
        }
    }


    private synchronized void initStartDw() {
        if (mlocationClient == null){
            MoudleUtils.textViewSetText(tv_home_startNodeStr,"请开启定位权限,然后点击刷新定位");
            return;
        }
        mlocationClient.startLocation();
    }

    private void initDw() {
        mlocationClient = new AMapLocationClient(this);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位参数
        mlocationClient.setLocationOption(Utils.initLocation());
    }


    private void initStopDw() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
        mlocationClient = null;
    }


    private void init() {
        NotifySignBeanMessageManager.getInstanceSignBean().setNotifyMessageSignBean(this);//向首页要数据

        NotifyActivityToMessageManager.getInstance().sendNotifyActivityToFlag(true);//通知首页可以给我获取的打卡数据了
    }

    private void initID() {

        popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
        btn_signIn = (Button) findViewById(R.id.btn_signIn);
        calendar = (SignCalendar) findViewById(R.id.popupwindow_calendar);
        puncherId = (TextView) findViewById(R.id.punchcard_ID);
        puncherTime = (TextView) findViewById(R.id.punchcard_time);
        puncherFat = (TextView) findViewById(R.id.punchcard_fat);
        tv_home_startNodeStr = (TextView) findViewById(R.id.tv_home_startNodeStr);
        tv_home_startNodeStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized (this) {
                    MoudleUtils.textViewSetText(tv_home_startNodeStr, "刷新定位中");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //TODO 步数
                            initStartDw();
                        }
                    }, 1000);
                }
            }
        });
        punchcard_back = (LinearLayout) findViewById(R.id.calendar_back);
        sd_fh = (SimpleDraweeView) findViewById(R.id.sd_fh);

        punchcard_back.setOnClickListener(this);
        sd_fh.setOnClickListener(this);
        btn_signIn.setOnClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        initStartDw();
        prepareData();
        // 初始化界面
        initSiginData();
        //监听当前月份
        initMoth();
    }

    private void initSiginData() {
        if (getSignInfoBean != null) {
            initUiUpdate();
            query();
        }

    }

    private void initMoth() {
        calendar.setOnCalendarDateChangedListener(new SignCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month
                        .setText(year + "年" + month + "月");
            }
        });
    }

    private void initUiUpdate() {
        //更新UI
        if (puncherId == null) {
            return;
        }

        if (getSignInfoBean.getUser_id() != 0) {
            puncherId.setText(getSignInfoBean.getUser_id() + "");
        } else {
            puncherId.setText(SPUtils.get(this, "user_id", 0) + "");
        }
        if (getSignInfoBean.getFat() != null) {
            if (!getSignInfoBean.getFat().equals("")) {
                puncherFat.setText("您已累计消耗" + getSignInfoBean.getFat() + "kcal");
            }
        }
        if (getSignInfoBean.getNum().equals("0")) {
            puncherTime.setText("今天您尚未签到");
        } else {
            puncherTime.setText("您是今天第" + getSignInfoBean.getNum() + "位签到的会员");
        }
    }

    private void prepareData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        date1 = formatter.format(curDate);
        popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
                + calendar.getCalendarMonth() + "月");
        if (null != date) {
            int years = Integer.parseInt(date.substring(0,
                    date.indexOf("-")));
            int month = Integer.parseInt(date.substring(
                    date.indexOf("-") + 1, date.lastIndexOf("-")));
            popupwindow_calendar_month.setText(years + "年" + month + "月");

            calendar.showCalendar(years, month);
            calendar.setCalendarDayBgColor(date,
                    R.drawable.calendar_date_focused);
        }
    }

    public void add(String date, String num) {
        if (getSignInfoBean != null) {
            if (getSignInfoBean.getDays() != null) {
                getSignInfoBean.getDays().add(date);
                getSignInfoBean.setNum(num);
            }
        } else {
            List<String> dateList = new ArrayList<>();
            dateList.add(date);
            getSignInfoBean = new GetSignBean.GetSignInfoBean();
            getSignInfoBean.setDays(dateList);
            getSignInfoBean.setNum(num);
        }
    }

    public void query() {
        if (getSignInfoBean != null) {
            if (getSignInfoBean.getDays() != null) {
                for (int i = 0; i < getSignInfoBean.getDays().size(); i++) {
                    if (date1.equals(getSignInfoBean.getDays().get(i))) {
                        isinput = true;
                    }
                }
                calendar.addMarks(getSignInfoBean.getDays(), 0);
            }
        }
        if (isinput) {
            initToOpen();
            initDakeEd();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signIn:
                //网络操作
                calsgin();
                break;
            case R.id.calendar_back:
            case R.id.sd_fh:
                finish();
                break;
            default:
                break;

        }
    }


    private void lockBack() {
        this.setFinishOnTouchOutside(false);
        punchcard_back.setClickable(false);
        flag = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (flag) {
                finish();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void calsgin() {

        if (NetworkUtils.isNetworkAvailable(this)) {
            //锁死返回
            lockBack();
            buder = new KyLoadingBuilder(this);
            MoudleUtils.kyloadingShow(buder);
            int userid = (int) SPUtils.get(this, "user_id", 0);
            int gym_id = (int) SPUtils.get(this, "gym_id", 0);
            String token = (String) SPUtils.get(this, "token", "");

            Call<SignInBean> signInBeanCall = RetrofitUtils.retrofit.create(WfApi.class).signin(token, userid, gym_id);
            signInBeanCall.enqueue(new Callback<SignInBean>() {
                @Override
                public void onResponse(Call<SignInBean> call, Response<SignInBean> response) {
                    SignInBean signInBean = response.body();
                    initDaka(signInBean);
                    initToOpen();
                    MoudleUtils.kyloadingDismiss(buder);
                }

                @Override
                public void onFailure(Call<SignInBean> call, Throwable t) {
                    initToOpen();
                    MoudleUtils.toChekWifi(PunchCardDialog.this);
                    MoudleUtils.kyloadingDismiss(buder);
                }
            });
        } else {
            MoudleUtils.toChekWifi(this);
        }
    }

    private void initDaka(SignInBean signInBean) {
        if (signInBean != null) {
            if (signInBean.getStatus().equals("1")) {
                puncherFat.setText("您已累计消耗" + signInBean.getInfo().getFat() + "kcal");
                //进行打卡成功后的操作
                MoudleUtils.integralAnimation(PunchCardDialog.this, btn_signIn, "+" + signInBean.getInfo().getIntegral() + "超空币\n");
                initToOpen();
                add(date1, signInBean.getInfo().getNum() + "");
                query();
                initDakeEd();
                initDakNum(signInBean);
                NotifyToHomeSignBeanMessageManager.getInstanceSignBean().sendNotifyGetSignInfoBean(getSignInfoBean);
            } else if (signInBean.getStatus().equals("0")) {
                ToastUtils.showShort(PunchCardDialog.this, signInBean.getMsg());
            } else if (signInBean.getStatus().equals("2")) {
                ToastUtils.showShort(PunchCardDialog.this,signInBean.getMsg());

            }
        }

    }

    private void initDakNum(SignInBean signInBean) {

        puncherTime.setText("您是今天第" + signInBean.getInfo().getNum() + "位签到的会员");
        initSiginData();
    }

    private void initDakeEd() {
        btn_signIn.setText("已签到");
        btn_signIn.setEnabled(false);
    }

    private void initToOpen() {
        this.setFinishOnTouchOutside(true);
        punchcard_back.setClickable(true);
        flag = true;
    }


    @Override
    public void sendMessageGetSignBean(GetSignBean.GetSignInfoBean getSignInfoBean) {
        this.getSignInfoBean = getSignInfoBean;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        initStopDw();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

}
