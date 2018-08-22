package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityToCoachMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyActivityCoachToMessageManager {

    private static NotifyActivityCoachToMessageManager instance = new NotifyActivityCoachToMessageManager();

    private NotifyActivityCoachToMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyActivityCoachToMessageManager getInstance() {
        return instance;
    }

    private NotifyActicityToCoachMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyActicityToCoachMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyActivityToFlag(boolean flag) {
        if(listener!=null){
            listener.sendMessageActivityToCoachFlag(flag);
        }
    }

}
