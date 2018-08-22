package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyjpushCoachStarMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyJpushCoachStartManager {
    private static NotifyJpushCoachStartManager instance = new NotifyJpushCoachStartManager();

    private NotifyJpushCoachStartManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyJpushCoachStartManager getInstance() {
        return instance;
    }

    private NotifyjpushCoachStarMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyjpushCoachStarMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyFlag(boolean flag) {
        if (listener != null) {
            listener.sendNotifyJpushCoachStarFlag(flag);
        }
    }
}
