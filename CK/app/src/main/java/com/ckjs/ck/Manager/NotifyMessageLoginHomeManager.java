package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyLoginHomeMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyMessageLoginHomeManager {

    private static NotifyMessageLoginHomeManager instance = new NotifyMessageLoginHomeManager();

    private NotifyMessageLoginHomeManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyMessageLoginHomeManager getInstance() {
        return instance;
    }

    private NotifyLoginHomeMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyLoginHomeMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flagRefresh
     */
    public void sendNotifyMessageHome(boolean flagRefresh) {
        if (listener != null) {
            listener.sendMessageHomeLoin(flagRefresh);
        }
    }

}
