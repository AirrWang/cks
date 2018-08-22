package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyOutCkMineMessage;
import com.ckjs.ck.Message.NotifyOutHomeMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyOutCkMineMessageManager {

    private static NotifyOutCkMineMessageManager instance = new NotifyOutCkMineMessageManager();

    private NotifyOutCkMineMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyOutCkMineMessageManager getInstance() {
        return instance;
    }

    private NotifyOutCkMineMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyOutCkMineMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     */



    public void sendNotifyMessageCkMine(boolean flagRefresh) {
        if(listener!=null){
        listener.sendMessageCircleMineOut(flagRefresh);}
    }


}
