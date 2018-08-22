package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyCircleRefrushLookMessage;
import com.ckjs.ck.Message.NotifyCircleRefrushMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyCircleRefrushLookMessageManager {

    private static NotifyCircleRefrushLookMessageManager instance = new NotifyCircleRefrushLookMessageManager();

    private NotifyCircleRefrushLookMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyCircleRefrushLookMessageManager getInstance() {
        return instance;
    }

    private NotifyCircleRefrushLookMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyCircleRefrushLookMessage nm) {
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
