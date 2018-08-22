package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyjpushClientEndMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyJpushClientEndManager {
    private static NotifyJpushClientEndManager instance = new NotifyJpushClientEndManager();

    private NotifyJpushClientEndManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyJpushClientEndManager getInstance() {
        return instance;
    }

    private NotifyjpushClientEndMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyjpushClientEndMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyFlag(boolean flag) {
        if (listener != null) {
            listener.sendNotifyJpushClientEndFlag(flag);
        }
    }
}
