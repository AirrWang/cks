package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyjpushPaymentMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyJpushPaymentManager {
    private static NotifyJpushPaymentManager instance = new NotifyJpushPaymentManager();

    private NotifyJpushPaymentManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyJpushPaymentManager getInstance() {
        return instance;
    }

    private NotifyjpushPaymentMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyjpushPaymentMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyFlag(boolean flag) {
        if (listener != null) {
            listener.sendNotifyJpushPaymentFlag(flag);
        }
    }

}
