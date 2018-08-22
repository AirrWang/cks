package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyOutHealthMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyOutHealthMessageManager {

    private static NotifyOutHealthMessageManager instance = new NotifyOutHealthMessageManager();

    private NotifyOutHealthMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyOutHealthMessageManager getInstance() {
        return instance;
    }

    private NotifyOutHealthMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyOutHealthMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     */


    public void sendNotifyMessageHealth(boolean flagRefresh) {
        if(listener!=null) {
            listener.sendMessageHealthOut(flagRefresh);
        }
    }




}
