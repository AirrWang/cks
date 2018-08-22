package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyLoginCkLookMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyMessageLoginCkLookManager {

    private static NotifyMessageLoginCkLookManager instance = new NotifyMessageLoginCkLookManager();

    private NotifyMessageLoginCkLookManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyMessageLoginCkLookManager getInstance() {
        return instance;
    }

    private NotifyLoginCkLookMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyLoginCkLookMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flagRefresh
     */


    public void sendNotifyMessageCkLook(boolean flagRefresh) {
        if(listener!=null) {
            listener.sendMessageCircleLookLogin(flagRefresh);
        }
    }




}
