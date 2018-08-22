package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyBuShuLoginMessage;
import com.ckjs.ck.Message.NotifyBuShuOutMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyBuShuMessageManager {

    private static NotifyBuShuMessageManager instance = new NotifyBuShuMessageManager();

    private NotifyBuShuMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyBuShuMessageManager getInstance() {
        return instance;
    }

    private NotifyBuShuOutMessage listener;
    private NotifyBuShuLoginMessage listenerLoin;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyBuShuOutMessage nm) {
        listener = nm;
    }

    public void setNotifyOutMessage(NotifyBuShuLoginMessage nm) {
        listenerLoin = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyBuShuOutFlag(boolean flag) {
        if (listener != null) {
            listener.sendMessageBuShuOutFlag(flag);
        }
    }

    public void sendNotifyBuShuLoginFlag(boolean flag) {
        if (listenerLoin != null) {
            listenerLoin.sendMessageBuShuLoginFlag(flag);
        }
    }

}
