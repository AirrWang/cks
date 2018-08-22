package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityToSmallPlayMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyActivityToSmallPlayManager {

    private static NotifyActivityToSmallPlayManager instance = new NotifyActivityToSmallPlayManager();

    private NotifyActivityToSmallPlayManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyActivityToSmallPlayManager getInstance() {
        return instance;
    }

    private NotifyActicityToSmallPlayMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyActicityToSmallPlayMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendMessageActivityToSmallPlay(boolean flag,String path) {
        if (listener != null) {
            listener.sendMessageActivityToSmallPlay(flag,path);
        }
    }

}
