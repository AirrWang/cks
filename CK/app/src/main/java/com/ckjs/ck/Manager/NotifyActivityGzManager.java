package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityGzMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyActivityGzManager {

    private static NotifyActivityGzManager instance = new NotifyActivityGzManager();

    private NotifyActivityGzManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyActivityGzManager getInstance() {
        return instance;
    }

    private NotifyActicityGzMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyActicityGzMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyFlag(boolean flag) {
        if (listener != null) {
            listener.sendNotifyActicityGzMessageFlag(flag);
        }
    }

}
