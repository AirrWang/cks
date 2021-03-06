package com.ckjs.ck.Android.CoachModule.Activity.HanXinShiPinPlay;

import com.hyphenate.chat.EMCallStateChangeListener;

/**
 * 通话相关事件传递对象
 *
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CallEvent {

    private boolean isState;
    private boolean isTime;
    private EMCallStateChangeListener.CallState callState;
    private EMCallStateChangeListener.CallError callError;

    public EMCallStateChangeListener.CallState getCallState() {
        return callState;
    }

    public void setCallState(EMCallStateChangeListener.CallState callState) {
        this.callState = callState;
    }

    public EMCallStateChangeListener.CallError getCallError() {
        return callError;
    }

    public void setCallError(EMCallStateChangeListener.CallError callError) {
        this.callError = callError;
    }

    public boolean isState() {
        return isState;
    }

    public void setState(boolean state) {
        isState = state;
    }

    public boolean isTime() {
        return isTime;
    }

    public void setTime(boolean time) {
        isTime = time;
    }
}
