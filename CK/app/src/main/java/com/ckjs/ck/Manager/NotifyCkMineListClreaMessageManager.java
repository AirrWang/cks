package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityToFinishendMessage;
import com.ckjs.ck.Message.NotifyCkMineListClreaMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyCkMineListClreaMessageManager {

    private static NotifyCkMineListClreaMessageManager instance = new NotifyCkMineListClreaMessageManager();

    private NotifyCkMineListClreaMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyCkMineListClreaMessageManager getInstance() {
        return instance;
    }

    private NotifyCkMineListClreaMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyCkMineListClreaMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyCkMineListClreaFlag(boolean flag) {
        if(listener!=null){
            listener.sendMessageCkMineListClreaFlag(flag);
        }
    }

}
