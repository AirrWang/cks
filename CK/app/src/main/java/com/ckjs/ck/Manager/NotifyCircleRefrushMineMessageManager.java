package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyCircleRefrushMineMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyCircleRefrushMineMessageManager {

    private static NotifyCircleRefrushMineMessageManager instance = new NotifyCircleRefrushMineMessageManager();

    private NotifyCircleRefrushMineMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyCircleRefrushMineMessageManager getInstance() {
        return instance;
    }

    private NotifyCircleRefrushMineMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyCircleRefrushMineMessage nm) {
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
