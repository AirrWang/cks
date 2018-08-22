package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityToFinishendMessage;
import com.ckjs.ck.Message.NotifyjpushMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyJpushManager {

    private static NotifyJpushManager instance = new NotifyJpushManager();

    private NotifyJpushManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyJpushManager getInstance() {
        return instance;
    }

    private NotifyjpushMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyjpushMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyJpushFlag(boolean flag,String bodyanalyse,String bodyinfo) {
        if (listener != null) {
            listener.sendMessageJpushFlag(flag,bodyanalyse,bodyinfo);
        }
    }

}
