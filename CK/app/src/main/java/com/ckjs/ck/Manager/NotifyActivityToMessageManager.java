package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityToMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyActivityToMessageManager {

    private static NotifyActivityToMessageManager instance = new NotifyActivityToMessageManager();

    private NotifyActivityToMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyActivityToMessageManager getInstance() {
        return instance;
    }

    private NotifyActicityToMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyActicityToMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyActivityToFlag(boolean flag) {
        if(listener!=null){
            listener.sendMessageActivityToFlag(flag);
        }
    }

}
