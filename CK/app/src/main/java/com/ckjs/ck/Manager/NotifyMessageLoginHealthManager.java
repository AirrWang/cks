package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyLoginHealthMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyMessageLoginHealthManager {

    private static NotifyMessageLoginHealthManager instance = new NotifyMessageLoginHealthManager();

    private NotifyMessageLoginHealthManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyMessageLoginHealthManager getInstance() {
        return instance;
    }

    private NotifyLoginHealthMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyLoginHealthMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flagRefresh
     */


    public void sendNotifyMessageHealth(boolean flagRefresh) {
        if(listener!=null) {
            listener.sendMessageHealthLogin(flagRefresh);
        }
    }




}
