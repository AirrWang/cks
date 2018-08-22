package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyLoginMineMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyMessageLoginMineManager {

    private static NotifyMessageLoginMineManager instance = new NotifyMessageLoginMineManager();

    private NotifyMessageLoginMineManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyMessageLoginMineManager getInstance() {
        return instance;
    }

    private NotifyLoginMineMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyLoginMineMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flagRefresh
     */


    public void sendNotifyMessageMine(boolean flagRefresh) {
        if(listener!=null) {
            listener.sendMessageMineLogin(flagRefresh);

        }
    }


}
