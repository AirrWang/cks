package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyjpushCoachEndMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyJpushCoachEndManager {
    private static NotifyJpushCoachEndManager instance = new NotifyJpushCoachEndManager();

    private NotifyJpushCoachEndManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyJpushCoachEndManager getInstance() {
        return instance;
    }

    private NotifyjpushCoachEndMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyjpushCoachEndMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyJpushCoachEndFlag(boolean flag) {
        if (listener != null) {
            listener.sendNotifyJpushCoachEndFlag(flag);
        }
    }
}
