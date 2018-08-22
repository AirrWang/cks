package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityAddJsfMessage;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyActivityAddJsfManager {

    private static NotifyActivityAddJsfManager instance = new NotifyActivityAddJsfManager();

    private NotifyActivityAddJsfManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyActivityAddJsfManager getInstance() {
        return instance;
    }

    private NotifyActicityAddJsfMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyActicityAddJsfMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyFlag(boolean flag) {
        if (listener != null) {
            listener.sendMessageActivityAddJsfFlag(flag);
        }
    }

}
