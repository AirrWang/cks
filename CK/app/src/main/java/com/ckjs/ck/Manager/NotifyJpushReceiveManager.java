package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyjpushReceiveMessage;
import com.ckjs.ck.Message.NotifyjpushSubmitMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyJpushReceiveManager {
    private static NotifyJpushReceiveManager instance = new NotifyJpushReceiveManager();

    private NotifyJpushReceiveManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyJpushReceiveManager getInstance() {
        return instance;
    }

    private NotifyjpushReceiveMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyjpushReceiveMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyFlag(boolean flag) {
        if (listener != null) {
            listener.sendNotifyJpushReceiveFlag(flag);
        }
    }

}
