package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyjpushLetterMessage;
import com.ckjs.ck.Message.NotifyjpushMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyJpushLetterManager {

    private static NotifyJpushLetterManager instance = new NotifyJpushLetterManager();

    private NotifyJpushLetterManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyJpushLetterManager getInstance() {
        return instance;
    }

    private NotifyjpushLetterMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyjpushLetterMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyJpushLetterFlag(boolean flag,String letter) {
        if (listener != null) {
            listener.sendMessageJpushLetterFlag(flag,letter);
        }
    }

}
