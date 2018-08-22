package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyLoginCkMsgMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyMessageLoginCkMSgLookManager {

    private static NotifyMessageLoginCkMSgLookManager instance = new NotifyMessageLoginCkMSgLookManager();

    private NotifyMessageLoginCkMSgLookManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyMessageLoginCkMSgLookManager getInstance() {
        return instance;
    }

    private NotifyLoginCkMsgMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyLoginCkMsgMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flagRefresh
     */


    public void sendNotifyMessageCkLook(boolean flagRefresh) {
        if(listener!=null) {
            listener.sendMessageCkMsgLogin(flagRefresh);
        }
    }




}
