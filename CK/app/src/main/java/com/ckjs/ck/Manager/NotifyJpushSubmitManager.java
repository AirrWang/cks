package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyjpushCommenMessage;
import com.ckjs.ck.Message.NotifyjpushSubmitMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyJpushSubmitManager {
    private static NotifyJpushSubmitManager instance = new NotifyJpushSubmitManager();

    private NotifyJpushSubmitManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyJpushSubmitManager getInstance() {
        return instance;
    }

    private NotifyjpushSubmitMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyjpushSubmitMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyFlag(boolean flag) {
        if (listener != null) {
            listener.sendNotifyJpushSubmitFlag(flag);
        }
    }

}
