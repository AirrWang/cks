package com.ckjs.ck.Manager;

import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Message.NotifyCkGuazhuMessage;
import com.ckjs.ck.Message.NotifyToMainAcFxMessage;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyTCcGuanzhuManager {

    private static NotifyTCcGuanzhuManager instance = new NotifyTCcGuanzhuManager();

    private NotifyTCcGuanzhuManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyTCcGuanzhuManager getInstance() {
        return instance;
    }

    private NotifyCkGuazhuMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyCkGuazhuMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyActivityToFlag(boolean flag, int sice_id, int max_id) {
        if (listener != null) {
            listener.sendMessageCkGuazhuFlag(flag, sice_id, max_id);
        }
    }

}
