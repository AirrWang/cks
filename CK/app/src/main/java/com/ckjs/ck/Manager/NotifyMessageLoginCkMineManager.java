package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyLoginCkMineMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyMessageLoginCkMineManager {

    private static NotifyMessageLoginCkMineManager instance = new NotifyMessageLoginCkMineManager();

    private NotifyMessageLoginCkMineManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyMessageLoginCkMineManager getInstance() {
        return instance;
    }

    private NotifyLoginCkMineMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyLoginCkMineMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flagRefresh
     */


    public void sendNotifyMessageCkMine(boolean flagRefresh) {
        if (listener != null) {
            listener.sendMessageCircleMineLogin(flagRefresh);
        }
    }


}
