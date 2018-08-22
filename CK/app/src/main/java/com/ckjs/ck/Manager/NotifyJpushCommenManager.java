package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyjpushCommenMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyJpushCommenManager {

    private static NotifyJpushCommenManager instance = new NotifyJpushCommenManager();

    private NotifyJpushCommenManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyJpushCommenManager getInstance() {
        return instance;
    }

    private NotifyjpushCommenMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyjpushCommenMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyJpushCommentFlag(boolean flag,String commen) {
        if (listener != null) {
            listener.sendNotifyJpushCommentFlag(flag,commen);
        }
    }

}
