package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyLoginHealthMovenmentMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyMessageLoginHealthMovenmentManager {

    private static NotifyMessageLoginHealthMovenmentManager instance = new NotifyMessageLoginHealthMovenmentManager();

    private NotifyMessageLoginHealthMovenmentManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyMessageLoginHealthMovenmentManager getInstance() {
        return instance;
    }

    private NotifyLoginHealthMovenmentMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyLoginHealthMovenmentMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flagRefresh
     */


    public void sendNotifyMessageMovenmentHealth(boolean flagRefresh) {
        if(listener!=null) {
            listener.sendMessageHealthMovenmentLogin(flagRefresh);
        }
    }




}
