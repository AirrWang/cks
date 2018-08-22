package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityToFinishendMessage;
import com.ckjs.ck.Message.NotifyCkToDongTaiPicDeleteMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyCkToDongTaiPrcDeleteMessageManager {

    private static NotifyCkToDongTaiPrcDeleteMessageManager instance = new NotifyCkToDongTaiPrcDeleteMessageManager();

    private NotifyCkToDongTaiPrcDeleteMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyCkToDongTaiPrcDeleteMessageManager getInstance() {
        return instance;
    }

    private NotifyCkToDongTaiPicDeleteMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyCkToDongTaiPicDeleteMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyActivityToFlag(boolean flag, List<String> list) {
        if (listener != null) {
            listener.sendCkToDongTaiPicDeleteFlag(flag, list);
        }
    }

}
