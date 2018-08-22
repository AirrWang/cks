package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityToFinishendMessage;
import com.ckjs.ck.Message.NotifyWxpayFinishendMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyWxPayManager {

    private static NotifyWxPayManager instance = new NotifyWxPayManager();

    private NotifyWxPayManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyWxPayManager getInstance() {
        return instance;
    }

    private NotifyWxpayFinishendMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyWxpayFinishendMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyMWxpayFinishendFlag(boolean flag,String status) {
        if(listener!=null){
            listener.sendMWxpayFinishendFlag(flag,status);
        }
    }

}
