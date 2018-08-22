package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyPostUserInfoMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyPostUserInforMessageManager {

    private static NotifyPostUserInforMessageManager instance = new NotifyPostUserInforMessageManager();

    private NotifyPostUserInforMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyPostUserInforMessageManager getInstance() {
        return instance;
    }

    private NotifyPostUserInfoMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyPostUserInfoMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyPostUserInfoFlag(boolean flag) {
        if (listener != null) {
            listener.sendMessagePostUserInfo(flag);
        }
    }

}
