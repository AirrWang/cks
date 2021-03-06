package com.ckjs.ck.Android.CoachModule.Activity.HanXinShiPinPlay;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.VideostartBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.LogUtils;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.hyphenate.chat.EMCallManager;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.media.EMCallSurfaceView;
import com.superrtc.sdk.VideoView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 * <p>
 * 视频通话界面处理
 */
public class VideoCallActivity extends CallActivity {

    // 视频通话帮助类
    private EMCallManager.EMVideoCallHelper videoCallHelper;
    // SurfaceView 控件状态，-1 表示通话未接通，0 表示本小远大，1 表示远小本大
    private int surfaceState = -1;

    private EMCallSurfaceView localSurface = null;
    private EMCallSurfaceView oppositeSurface = null;
    private RelativeLayout.LayoutParams localParams = null;
    private RelativeLayout.LayoutParams oppositeParams = null;

    // 使用 ButterKnife 注解的方式获取控件
    @BindView(R.id.layout_root)
    View rootView;
    @BindView(R.id.layout_call_control)
    View controlLayout;
    @BindView(R.id.layout_surface_container)
    RelativeLayout surfaceLayout;

    @BindView(R.id.btn_exit_full_screen)
    ImageButton exitFullScreenBtn;
    @BindView(R.id.text_call_state)
    TextView callStateView;
    @BindView(R.id.text_call_time)
    TextView callTimeView;
    @BindView(R.id.btn_mic_switch)
    ImageButton micSwitch;
    @BindView(R.id.btn_camera_switch)
    ImageButton cameraSwitch;
    @BindView(R.id.btn_speaker_switch)
    ImageButton speakerSwitch;
    @BindView(R.id.btn_record_switch)
    ImageButton recordSwitch;
    @BindView(R.id.btn_screenshot)
    ImageButton screenshotSwitch;
    @BindView(R.id.btn_change_camera_switch)
    ImageButton changeCameraSwitch;
    @BindView(R.id.fab_reject_call)
    FloatingActionButton rejectCallFab;
    @BindView(R.id.fab_end_call)
    FloatingActionButton endCallFab;
    @BindView(R.id.fab_answer_call)
    FloatingActionButton answerCallFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        ButterKnife.bind(this);
        if (CallManager.getInstance().getType() == null) {
            if (CallManager.getInstance().isInComingCall()) {
                rejectCall();
                return;
            } else {
                endCall();
                return;
            }
        }
        initView();

    }


    /**
     * 重载父类方法,实现一些当前通话的操作，
     */
    @Override
    protected void initView() {
        super.initView();
        String showMsg = "";
        if (CallManager.getInstance().getType() != null) {
            if (CallManager.getInstance().getType().equals("1")) {
                if (CallManager.getInstance().getChatName() != null) {
                    showMsg = CallManager.getInstance().getChatName();
                }
            } else if (CallManager.getInstance().getType().equals("2")) {
                if (CallManager.getInstance().getChatRelName() != null) {
                    showMsg = CallManager.getInstance().getChatRelName();
                }
            }
        }
        //        LogUtils.d("sm:" + showMsg + "," + CallManager.getInstance().getType());
        if (CallManager.getInstance().isInComingCall()) {
            endCallFab.setVisibility(View.GONE);
            answerCallFab.setVisibility(View.VISIBLE);
            rejectCallFab.setVisibility(View.VISIBLE);
            callStateView.setText(showMsg + "申请与你进行通话");
        } else {
            endCallFab.setVisibility(View.VISIBLE);
            answerCallFab.setVisibility(View.GONE);
            rejectCallFab.setVisibility(View.GONE);
            callStateView.setText("正在呼叫" + showMsg);
        }

        micSwitch.setActivated(!CallManager.getInstance().isOpenMic());
        cameraSwitch.setActivated(!CallManager.getInstance().isOpenCamera());
        speakerSwitch.setActivated(CallManager.getInstance().isOpenSpeaker());
        recordSwitch.setActivated(CallManager.getInstance().isOpenRecord());

        // 初始化视频通话帮助类
        videoCallHelper = EMClient.getInstance().callManager().getVideoCallHelper();

        // 初始化显示通话画面
        initCallSurface();
        // 判断当前通话时刚开始，还是从后台恢复已经存在的通话
        if (CallManager.getInstance().getCallState() == CallManager.CallState.ACCEPTED) {
            endCallFab.setVisibility(View.VISIBLE);
            answerCallFab.setVisibility(View.GONE);
            rejectCallFab.setVisibility(View.GONE);
            callStateView.setText(R.string.call_accepted);
            refreshCallTime();
            // 通话已接通，修改画面显示
            onCallSurface();
        }

        try {
            // 设置默认摄像头为前置
            EMClient.getInstance().callManager().setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        CallManager.getInstance().setCallCameraDataProcessor();
    }

    /**
     * 界面控件点击监听器
     */
    @OnClick({
            R.id.layout_call_control, R.id.btn_exit_full_screen, R.id.btn_change_camera_switch, R.id.btn_mic_switch,
            R.id.btn_camera_switch, R.id.btn_speaker_switch, R.id.btn_record_switch, R.id.btn_screenshot, R.id.fab_reject_call,
            R.id.fab_end_call, R.id.fab_answer_call
    })
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_call_control:
                onControlLayout();
                break;
            case R.id.btn_exit_full_screen:
                // 最小化通话界面
                exitFullScreen();
                break;
            case R.id.btn_change_camera_switch:
                // 切换摄像头
                changeCamera();
                break;
            case R.id.btn_mic_switch:
                // 麦克风开关
                onMicrophone();
                break;
            case R.id.btn_camera_switch:
                // 摄像头开关
                onCamera();
                break;
            case R.id.btn_speaker_switch:
                // 扬声器开关
                onSpeaker();
                break;
            case R.id.btn_screenshot:
                // 保存通话截图
                onScreenShot();
                break;
            case R.id.btn_record_switch:
                // 录制开关
                onRecordCall();
                break;
            case R.id.fab_end_call:
                // 结束通话
                endCall();
                break;
            case R.id.fab_reject_call:
                // 拒绝接听通话
                rejectCall();
                break;
            case R.id.fab_answer_call:
                // 接听通话
                toAnswer();
                break;
        }
    }

    /**
     * 控制界面的显示与隐藏
     */
    private void onControlLayout() {
        if (controlLayout.isShown()) {
            controlLayout.setVisibility(View.GONE);
        } else {
            controlLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 退出全屏通话界面
     */
    private void exitFullScreen() {
        CallManager.getInstance().addFloatWindow();
        // 结束当前界面
        onFinish();
    }

    /**
     * 切换摄像头
     */
    private void changeCamera() {
        // 根据切换摄像头开关是否被激活确定当前是前置还是后置摄像头
        try {
            if (EMClient.getInstance().callManager().getCameraFacing() == 1) {
                EMClient.getInstance().callManager().switchCamera();
                EMClient.getInstance().callManager().setCameraFacing(0);
                // 设置按钮图标
                changeCameraSwitch.setImageResource(R.drawable.ic_camera_rear_white_24dp);
            } else {
                EMClient.getInstance().callManager().switchCamera();
                EMClient.getInstance().callManager().setCameraFacing(1);
                // 设置按钮图标
                changeCameraSwitch.setImageResource(R.drawable.ic_camera_front_white_24dp);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 麦克风开关，主要调用环信语音数据传输方法
     */
    private void onMicrophone() {
        try {
            // 根据麦克风开关是否被激活来进行判断麦克风状态，然后进行下一步操作
            if (micSwitch.isActivated()) {
                // 设置按钮状态
                micSwitch.setActivated(false);
                // 暂停语音数据的传输
                EMClient.getInstance().callManager().resumeVoiceTransfer();
                CallManager.getInstance().setOpenMic(true);
            } else {
                // 设置按钮状态
                micSwitch.setActivated(true);
                // 恢复语音数据的传输
                EMClient.getInstance().callManager().pauseVoiceTransfer();
                CallManager.getInstance().setOpenMic(false);
            }
        } catch (HyphenateException e) {
            //            LogUtils.e("exception code: %d, %s," + e.getErrorCode() + "," + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 摄像头开关
     */
    private void onCamera() {
        try {
            // 根据摄像头开关按钮状态判断摄像头状态，然后进行下一步操作
            if (cameraSwitch.isActivated()) {
                // 设置按钮状态
                cameraSwitch.setActivated(false);
                // 暂停视频数据的传输
                EMClient.getInstance().callManager().resumeVideoTransfer();
                CallManager.getInstance().setOpenCamera(true);
            } else {
                // 设置按钮状态
                cameraSwitch.setActivated(true);
                // 恢复视频数据的传输
                EMClient.getInstance().callManager().pauseVideoTransfer();
                CallManager.getInstance().setOpenCamera(false);
            }
        } catch (HyphenateException e) {
            //            LogUtils.e("exception code: %d, %s," + e.getErrorCode() + "," + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 扬声器开关
     */
    private void onSpeaker() {
        // 根据按钮状态决定打开还是关闭扬声器
        if (speakerSwitch.isActivated()) {
            // 设置按钮状态
            speakerSwitch.setActivated(false);
            CallManager.getInstance().closeSpeaker();
            CallManager.getInstance().setOpenSpeaker(false);
        } else {
            // 设置按钮状态
            speakerSwitch.setActivated(true);
            CallManager.getInstance().openSpeaker();
            CallManager.getInstance().setOpenSpeaker(true);
        }
    }

    /**
     * 录制视屏通话内容
     */
    private void onRecordCall() {
        // 根据开关状态决定是否开启录制
        if (recordSwitch.isActivated()) {
            // 设置按钮状态
            recordSwitch.setActivated(false);
            String path = videoCallHelper.stopVideoRecord();
            CallManager.getInstance().setOpenRecord(false);
            File file = new File(path);
            if (file.exists()) {
                Toast.makeText(activity, "录制视频成功 " + path, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "录制失败/(ㄒoㄒ)/~~", Toast.LENGTH_LONG).show();
            }
        } else {
            // 设置按钮状态
            recordSwitch.setActivated(true);
            // 先创建文件夹
            String dirPath = getExternalFilesDir("").getAbsolutePath() + "/videos";
            File dir = new File(dirPath);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            videoCallHelper.startVideoRecord(dirPath);
            LogUtils.d("开始录制视频");
            Toast.makeText(activity, "开始录制", Toast.LENGTH_LONG).show();
            CallManager.getInstance().setOpenRecord(true);
        }
    }

    /**
     * 保存通话截图
     */
    private void onScreenShot() {
        String dirPath = getExternalFilesDir("").getAbsolutePath() + "/videos/";
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        String path = dirPath + "video_" + System.currentTimeMillis() + ".jpg";
        boolean result = videoCallHelper.takePicture(path);
        Toast.makeText(activity, "截图保存成功 " + path, Toast.LENGTH_LONG).show();
    }

    /**
     * 提示是否接通此视频通话课程
     */
    AlertDialog.Builder builder;
    AlertDialog alert;

    private void toAnswer() {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("您是否确认接听：");
        builder.setMessage("注意：\n若成功接通此视频通话课程，\n则[7]天后将自动确认完成!!!\n如有疑问请联系客服!");


        builder.setPositiveButton("接听", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initAnswerTask();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert = builder.create();
        if (!alert.isShowing()) {
            alert.show();
        }
    }

    /**
     * 走开始接口然后进行接通视频
     */
    private void initAnswerTask() {

        String token = "";
        token = (String) SPUtils.get("token", token);
        int user_id = 0;
        user_id = (int) SPUtils.get("user_id", user_id);
        Call<VideostartBean> getSignBeanCall = RetrofitUtils.retrofit.create(NpApi.class).videostart(token, user_id, CallManager.getInstance().getLessonId(), CallManager.getInstance().getType());
        getSignBeanCall.enqueue(new Callback<VideostartBean>() {
            @Override
            public void onResponse(Call<VideostartBean> call, Response<VideostartBean> response) {
                try {
                    VideostartBean bean = response.body();
                    if (bean != null) {
                        if (bean.getStatus().equals("1")) {
                            answerCall();
                        }
                        ToastUtils.showShort(VideoCallActivity.this, bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<VideostartBean> call, Throwable t) {
                MoudleUtils.toChekWifi();
            }
        });
    }

    /**
     * 接听通话
     */
    @Override
    protected void answerCall() {
        super.answerCall();
        endCallFab.setVisibility(View.VISIBLE);
        rejectCallFab.setVisibility(View.GONE);
        answerCallFab.setVisibility(View.GONE);
    }

    /**
     * 初始化通话界面控件
     */
    private void initCallSurface() {
        // 初始化显示远端画面控件
        oppositeSurface = new EMCallSurfaceView(activity);
        oppositeParams = new RelativeLayout.LayoutParams(0, 0);
        oppositeParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        oppositeParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        oppositeSurface.setLayoutParams(oppositeParams);
        surfaceLayout.addView(oppositeSurface);

        // 初始化显示本地画面控件
        localSurface = new EMCallSurfaceView(activity);
        localParams = new RelativeLayout.LayoutParams(0, 0);
        localParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        localParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        localParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        localSurface.setLayoutParams(localParams);
        surfaceLayout.addView(localSurface);

        localSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onControlLayout();
            }
        });

        localSurface.setZOrderOnTop(false);
        localSurface.setZOrderMediaOverlay(true);

        // 设置本地和远端画面的显示方式，是填充，还是居中
        localSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill);
        oppositeSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill);
        // 设置通话画面显示控件
        EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
    }

    /**
     * 接通通话，这个时候要做的只是改变本地画面 view 大小，不需要做其他操作
     */
    private void onCallSurface() {
        // 更新通话界面控件状态
        surfaceState = 0;

        int width = VMDimenUtil.dp2px(activity, 96);
        int height = VMDimenUtil.dp2px(activity, 128);
        int rightMargin = VMDimenUtil.dp2px(activity, 16);
        int topMargin = VMDimenUtil.dp2px(activity, 96);

        localParams = new RelativeLayout.LayoutParams(width, height);
        localParams.width = width;
        localParams.height = height;
        localParams.rightMargin = rightMargin;
        localParams.topMargin = topMargin;
        localParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        localSurface.setLayoutParams(localParams);

        localSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCallSurface();
            }
        });

        oppositeSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onControlLayout();
            }
        });
    }

    /**
     * 切换通话界面，这里就是交换本地和远端画面控件设置，以达到通话大小画面的切换
     */
    private void changeCallSurface() {
        if (surfaceState == 0) {
            surfaceState = 1;
            EMClient.getInstance().callManager().setSurfaceView(oppositeSurface, localSurface);
        } else {
            surfaceState = 0;
            EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(CallEvent event) {
        if (event.isState()) {
            refreshCallView(event);
        }
        if (event.isTime()) {
            // 不论什么情况都检查下当前时间
            refreshCallTime();
        }
    }

    AlertDialog alertDialog = null;

    private void initToMsg() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setMessage("对方不在线，是否短信提醒？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            initToPostDuiFang();
                            onFinish();
                        }

                    }).setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    onFinish();
                                }
                            }).create(); // 创建对话框

        }
        if (alertDialog != null) {
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing()) {
                alertDialog.show(); // 显示
            }
        }
    }

    /**
     * 对方不在线
     * 视频指导提醒接口
     */
    private void initToPostDuiFang() {
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        int user_id = (int) (SPUtils.get("user_id", 0));
        String token = (String) SPUtils.get("token", "");
        Call<AcceptBean> callBack = restApi.lessonremind(token, user_id, CallManager.getInstance().getType(), CallManager.getInstance().getChatId());

        callBack.enqueue(new Callback<AcceptBean>() {
            @Override
            public void onResponse(Call<AcceptBean> call, Response<AcceptBean> response) {
                try {
                    if (response.body() != null) {
                        ToastUtils.showShortNotInternet(response.body().getMsg());
                        LogUtils.d("response.body()NotNULL");
                    } else {
                        LogUtils.d("response.body()Null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AcceptBean> call, Throwable t) {

            }
        });
    }

    /**
     * 刷新通话界面
     */
    private void refreshCallView(CallEvent event) {
        EMCallStateChangeListener.CallError callError = event.getCallError();
        EMCallStateChangeListener.CallState callState = event.getCallState();
        switch (callState) {
            case CONNECTING: // 正在呼叫对方，TODO 没见回调过
                LogUtils.i("正在呼叫对方" + callError);
                break;
            case CONNECTED: // 正在等待对方接受呼叫申请（对方申请与你进行通话）
                String showMsg = "对方";
                if (CallManager.getInstance().getType() != null) {
                    if (CallManager.getInstance().getType().equals("1")) {
                        if (CallManager.getInstance().getChatName() != null) {
                            showMsg = CallManager.getInstance().getChatName();
                        }
                    } else if (CallManager.getInstance().getType().equals("2")) {
                        if (CallManager.getInstance().getChatRelName() != null) {
                            showMsg = CallManager.getInstance().getChatRelName();
                        }
                    }
                }
                //                LogUtils.d("sm:" + showMsg);
                LogUtils.i("正在连接" + callError);
                if (CallManager.getInstance().isInComingCall()) {
                    callStateView.setText(showMsg + "申请与你进行通话");
                } else {
                    callStateView.setText("正在等待" + showMsg + "接受呼叫申请");
                }
                break;
            case ACCEPTED: // 通话已接通
                LogUtils.i("通话已接通");
                callStateView.setText(R.string.call_accepted);
                // 通话接通，更新界面 UI 显示
                onCallSurface();
                break;
            case DISCONNECTED: // 通话已中断
                try {
                    LogUtils.i("通话已结束" + callError);
                    if (callError == EMCallStateChangeListener.CallError.ERROR_UNAVAILABLE) {
                        LogUtils.i("对方不在线" + callError);
                        if (!CallManager.getInstance().isInComingCall()) {
                            // 通话结束，保存消息
                            initToMsg();
                        } else {
                            onFinish();
                        }
                    } else {
                        onFinish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case NETWORK_DISCONNECTED:
                LogUtils.i("对方网络断开");
                break;
            case NETWORK_NORMAL:
                LogUtils.i("网络正常");
                break;
            case NETWORK_UNSTABLE:
                if (callError == EMCallStateChangeListener.CallError.ERROR_NO_DATA) {
                    LogUtils.i("没有通话数据" + callError);
                } else {
                    LogUtils.i("网络不稳定" + callError);
                }
                break;
            case VIDEO_PAUSE:
                Toast.makeText(activity, "对方已暂停视频传输", Toast.LENGTH_SHORT).show();
                LogUtils.i("对方已暂停视频传输");
                break;
            case VIDEO_RESUME:
                Toast.makeText(activity, "对方已恢复视频传输", Toast.LENGTH_SHORT).show();
                LogUtils.i("对方已恢复视频传输");
                break;
            case VOICE_PAUSE:
                Toast.makeText(activity, "对方已暂停语音传输", Toast.LENGTH_SHORT).show();
                LogUtils.i("对方已暂停语音传输");
                break;
            case VOICE_RESUME:
                Toast.makeText(activity, "对方已恢复语音传输", Toast.LENGTH_SHORT).show();
                LogUtils.i("对方已恢复语音传输");
                break;
            default:
                break;
        }
    }

    /**
     * 刷新通话时间显示
     */
    private void refreshCallTime() {
        int t = CallManager.getInstance().getCallTime();
        int h = t / 60 / 60;
        int m = t / 60 % 60;
        int s = t % 60 % 60;
        String time = "";
        if (h > 9) {
            time = "" + h;
        } else {
            time = "0" + h;
        }
        if (m > 9) {
            time += ":" + m;
        } else {
            time += ":0" + m;
        }
        if (s > 9) {
            time += ":" + s;
        } else {
            time += ":0" + s;
        }
        if (!callTimeView.isShown()) {
            callTimeView.setVisibility(View.VISIBLE);
        }
        callTimeView.setText(time);
    }

    /**
     * 屏幕方向改变回调方法
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onUserLeaveHint() {
        //super.onUserLeaveHint();
        exitFullScreen();
    }

    /**
     * 通话界面拦截 Back 按键，不能返回
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        exitFullScreen();
    }

    @Override
    protected void onFinish() {
        // release surface view
        if (localSurface != null) {
            if (localSurface.getRenderer() != null) {
                localSurface.getRenderer().dispose();
            }
            localSurface.release();
            localSurface = null;
        }
        if (oppositeSurface != null) {
            if (oppositeSurface.getRenderer() != null) {
                oppositeSurface.getRenderer().dispose();
            }
            oppositeSurface.release();
            oppositeSurface = null;
        }
        super.onFinish();
    }
}
