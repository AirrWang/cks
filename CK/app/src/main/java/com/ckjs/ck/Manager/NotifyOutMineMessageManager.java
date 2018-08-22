package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyOutHomeMessage;
import com.ckjs.ck.Message.NotifyOutMineMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyOutMineMessageManager {

    private static NotifyOutMineMessageManager instance = new NotifyOutMineMessageManager();

    private NotifyOutMineMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyOutMineMessageManager getInstance() {
        return instance;
    }

    private NotifyOutMineMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyOutMineMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     */



    public void sendNotifyMessageMine(boolean flagRefresh) {
        if(listener!=null){

            listener.sendMessageMineOut(flagRefresh);}
    }




}
