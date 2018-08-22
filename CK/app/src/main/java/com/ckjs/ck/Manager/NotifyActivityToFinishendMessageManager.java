package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityToFinishendMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyActivityToFinishendMessageManager {

    private static NotifyActivityToFinishendMessageManager instance = new NotifyActivityToFinishendMessageManager();

    private NotifyActivityToFinishendMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyActivityToFinishendMessageManager getInstance() {
        return instance;
    }

    private NotifyActicityToFinishendMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyActicityToFinishendMessage nm) {
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
