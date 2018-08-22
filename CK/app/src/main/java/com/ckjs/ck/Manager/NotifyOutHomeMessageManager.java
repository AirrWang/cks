package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyOutHomeMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyOutHomeMessageManager {

    private static NotifyOutHomeMessageManager instance = new NotifyOutHomeMessageManager();

    private NotifyOutHomeMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyOutHomeMessageManager getInstance() {
        return instance;
    }

    private NotifyOutHomeMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyOutHomeMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     */

    public void sendNotifyMessageHome(boolean flagRefresh) {
        if(listener!=null){

            listener.sendMessageHomeOut(flagRefresh);}
    }

  

}
