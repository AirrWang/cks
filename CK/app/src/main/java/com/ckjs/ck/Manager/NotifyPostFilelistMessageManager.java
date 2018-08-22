package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyActicityToFinishendMessage;
import com.ckjs.ck.Message.NotifyPostFilelistMessage;

import java.util.ArrayList;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyPostFilelistMessageManager {

    private static NotifyPostFilelistMessageManager instance = new NotifyPostFilelistMessageManager();

    private NotifyPostFilelistMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyPostFilelistMessageManager getInstance() {
        return instance;
    }

    private NotifyPostFilelistMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyPostFilelistMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyPostFilelist(boolean flag, ArrayList<String> list) {
        if (listener != null) {
            listener.sendMessagePostFilelist(flag, list);
        }
    }

}
