package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityAddFamilyMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyActivityAddFamilyManager {

    private static NotifyActivityAddFamilyManager instance = new NotifyActivityAddFamilyManager();

    private NotifyActivityAddFamilyManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyActivityAddFamilyManager getInstance() {
        return instance;
    }

    private NotifyActicityAddFamilyMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyActicityAddFamilyMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyFlag(boolean flag) {
        if (listener != null) {
            listener.sendNotifyActicityAddFamilyMessageFlag(flag);
        }
    }

}
