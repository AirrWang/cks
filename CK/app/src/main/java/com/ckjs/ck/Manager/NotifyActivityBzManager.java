package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityBzMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyActivityBzManager {

    private static NotifyActivityBzManager instance = new NotifyActivityBzManager();

    private NotifyActivityBzManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyActivityBzManager getInstance() {
        return instance;
    }

    private NotifyActicityBzMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyActicityBzMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     * @param focus_id
     */
    public void sendNotifyFlag(boolean flag, String bzName, int focus_id) {
        if (flag) {
            if (listener != null) {
                listener.sendNotifyActicityBzMessage(true, bzName, focus_id);
            }
        }
    }

}
