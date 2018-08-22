package com.ckjs.ck.Android.CoachModule.Activity.HanXinShiPinPlay;

import com.ckjs.ck.Tool.LogUtils;
import com.ckjs.ck.Tool.ToastUtils;
import com.hyphenate.chat.EMCallStateChangeListener;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 * <p>
 * <p>
 * 通话状态监听类，用来监听通话过程中状态的变化
 */

public class CallStateListener implements EMCallStateChangeListener {

    @Override
    public void onCallStateChanged(CallState callState, CallError callError) {
        CallEvent event = new CallEvent();
        event.setState(true);
        event.setCallError(callError);
        event.setCallState(callState);
        EventBus.getDefault().post(event);
        switch (callState) {
            case CONNECTING: // 正在呼叫对方，TODO 没见回调过
                LogUtils.i("正在呼叫对方:" + callError);
                CallManager.getInstance().setCallState(CallManager.CallState.CONNECTING);
                break;
            case CONNECTED: // 正在等待对方接受呼叫申请（对方申请与你进行通话）
                LogUtils.i("正在连接:" + callError);
                CallManager.getInstance().setCallState(CallManager.CallState.CONNECTED);
                break;
            case ACCEPTED: // 通话已接通
                LogUtils.i("通话已接通");
                CallManager.getInstance().stopCallSound();
                CallManager.getInstance().startCallTime();
                CallManager.getInstance().setEndType(CallManager.EndType.NORMAL);
                CallManager.getInstance().setCallState(CallManager.CallState.ACCEPTED);
                break;
            case DISCONNECTED: // 通话已中断
                LogUtils.i("通话已结束" + callError);
                LogUtils.i("onCallStateChanged,DISCONNECTED");
                CallManager.getInstance().setSoundPoolStopFlag(true);//第一次加载完毕铃声后进行播放音乐
                // 通话结束，重置通话状态
                if (callError == CallError.ERROR_UNAVAILABLE) {
                    LogUtils.i("对方不在线" + callError);
                    CallManager.getInstance().setEndType(CallManager.EndType.OFFLINE);
                } else if (callError == CallError.ERROR_BUSY) {
                    LogUtils.i("对方正忙" + callError);
                    ToastUtils.showShortNotInternet("对方正忙");
                    CallManager.getInstance().setEndType(CallManager.EndType.BUSY);
                } else if (callError == CallError.REJECTED) {
                    LogUtils.i("对方已拒绝" + callError);
                    ToastUtils.showShortNotInternet("对方已拒绝");
                    CallManager.getInstance().setEndType(CallManager.EndType.REJECTED);
                } else if (callError == CallError.ERROR_NORESPONSE) {
                    LogUtils.i("对方未响应，可能手机不在身边" + callError);
                    ToastUtils.showShortNotInternet("对方未响应，可能手机不在身边");
                    CallManager.getInstance().setEndType(CallManager.EndType.NORESPONSE);
                } else if (callError == CallError.ERROR_TRANSPORT) {
                    LogUtils.i("连接建立失败" + callError);
                    ToastUtils.showShortNotInternet("连接建立失败");
                    CallManager.getInstance().setEndType(CallManager.EndType.TRANSPORT);
                } else if (callError == CallError.ERROR_LOCAL_SDK_VERSION_OUTDATED) {
                    LogUtils.i("双方通讯协议不同" + callError);
                    ToastUtils.showShortNotInternet("双方通讯协议不同");
                    CallManager.getInstance().setEndType(CallManager.EndType.DIFFERENT);
                } else if (callError == CallError.ERROR_REMOTE_SDK_VERSION_OUTDATED) {
                    LogUtils.i("双方通讯协议不同" + callError);
                    ToastUtils.showShortNotInternet("双方通讯协议不同");
                    CallManager.getInstance().setEndType(CallManager.EndType.DIFFERENT);
                } else if (callError == CallError.ERROR_NO_DATA) {
                    LogUtils.i("没有通话数据" + callError);
                    ToastUtils.showShortNotInternet("没有通话数据");
                } else {
                    LogUtils.i("通话已结束" + callError);
                    if (CallManager.getInstance().getEndType() == CallManager.EndType.CANCEL) {
                        LogUtils.i("CallManager.EndType.CANCEL");
                        CallManager.getInstance().setEndType(CallManager.EndType.CANCELLED);
                    }
                    ToastUtils.showShortNotInternet("通话已结束");
                }
                // 通话结束，保存消息
                CallManager.getInstance().saveCallMessage();
                CallManager.getInstance().reset();
                break;
            case NETWORK_DISCONNECTED:
                LogUtils.i("对方网络不可用");
                ToastUtils.showShortNotInternet("对方网络不可用");
                break;
            case NETWORK_NORMAL:
                LogUtils.i("网络正常");
                break;
            case NETWORK_UNSTABLE:
                if (callError == CallError.ERROR_NO_DATA) {
                    LogUtils.i("没有通话数据" + callError);
                    ToastUtils.showShortNotInternet("没有通话数据");
                } else {
                    LogUtils.i("网络不稳定" + callError);
                    ToastUtils.showShortNotInternet("网络不稳定");
                }
                break;
            case VIDEO_PAUSE:
                LogUtils.i("视频传输已暂停");
                ToastUtils.showShortNotInternet("视频传输已暂停");
                break;
            case VIDEO_RESUME:
                LogUtils.i("视频传输已恢复");
                ToastUtils.showShortNotInternet("视频传输已恢复");
                break;
            case VOICE_PAUSE:
                LogUtils.i("语音传输已暂停");
                ToastUtils.showShortNotInternet("语音传输已暂停");
                break;
            case VOICE_RESUME:
                LogUtils.i("语音传输已恢复");
                ToastUtils.showShortNotInternet("语音传输已恢复");
                break;
            default:
                break;
        }
    }
}
