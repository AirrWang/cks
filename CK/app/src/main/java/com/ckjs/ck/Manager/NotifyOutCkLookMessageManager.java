package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyOutCkLookMessage;
import com.ckjs.ck.Message.NotifyOutHomeMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyOutCkLookMessageManager {

    private static NotifyOutCkLookMessageManager instance = new NotifyOutCkLookMessageManager();

    private NotifyOutCkLookMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyOutCkLookMessageManager getInstance() {
        return instance;
    }

    private NotifyOutCkLookMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyOutCkLookMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     */


    public void sendNotifyMessageCkLook(boolean flagRefresh) {
        if(listener!=null){
        listener.sendMessageCircleLookOut(flagRefresh);}
    }



}
