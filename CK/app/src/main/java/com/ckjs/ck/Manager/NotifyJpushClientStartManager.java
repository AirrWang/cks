package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyjpushClientStarMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyJpushClientStartManager {
    private static NotifyJpushClientStartManager instance = new NotifyJpushClientStartManager();

    private NotifyJpushClientStartManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyJpushClientStartManager getInstance() {
        return instance;
    }

    private NotifyjpushClientStarMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyjpushClientStarMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyFlag(boolean flag) {
        if (listener != null) {
            listener.sendNotifyJpushClientStarFlag(flag);
        }
    }
}
