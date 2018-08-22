package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyCircleRefrushMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyCircleRefrushMessageManager {

    private static NotifyCircleRefrushMessageManager instance = new NotifyCircleRefrushMessageManager();

    private NotifyCircleRefrushMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyCircleRefrushMessageManager getInstance() {
        return instance;
    }

    private NotifyCircleRefrushMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyCircleRefrushMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyCircleRefrushFlag(boolean flag) {
        if(listener!=null){
            listener.sendMessageCircleRefrushFlag(flag);
        }
    }

}
