package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyInfoUpdateMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyInfoUpdateMessageManager {

    private static NotifyInfoUpdateMessageManager instance = new NotifyInfoUpdateMessageManager();

    private NotifyInfoUpdateMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyInfoUpdateMessageManager getInstance() {
        return instance;
    }

    private NotifyInfoUpdateMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyInfoUpdateMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotify(Boolean flag) {
        if (listener != null) {
            listener.sendMessageInfoUpdate(flag);
        }
    }

}
