package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyBuShuClearMessage;
import com.ckjs.ck.Message.NotifyBuShuLoginMessage;
import com.ckjs.ck.Message.NotifyBuShuOutMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyBuShuClearMessageManager {

    private static NotifyBuShuClearMessageManager instance = new NotifyBuShuClearMessageManager();

    private NotifyBuShuClearMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyBuShuClearMessageManager getInstance() {
        return instance;
    }

    private NotifyBuShuClearMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyBuShuClearMessage nm) {
        listener = nm;
    }



    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyBuShuClearFlag(boolean flag) {
        if (listener != null) {
            listener.sendMessageBuShuClearFlag(flag);
        }
    }



}
