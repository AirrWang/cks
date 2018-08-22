package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityFsMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyActivityFsManager {

    private static NotifyActivityFsManager instance = new NotifyActivityFsManager();

    private NotifyActivityFsManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyActivityFsManager getInstance() {
        return instance;
    }

    private NotifyActicityFsMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyActicityFsMessage nm) {
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
                listener.sendNotifyActicityFsMessage(true, bzName, focus_id);
            }
        }
    }

}
