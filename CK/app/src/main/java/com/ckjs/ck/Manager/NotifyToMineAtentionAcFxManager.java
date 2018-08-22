package com.ckjs.ck.Manager;

import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Bean.GetMyfocusCircleBean;
import com.ckjs.ck.Message.NotifyToMainAcFxMessage;
import com.ckjs.ck.Message.NotifyToMineAtentionAcFxMessage;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyToMineAtentionAcFxManager {

    private static NotifyToMineAtentionAcFxManager instance = new NotifyToMineAtentionAcFxManager();

    private NotifyToMineAtentionAcFxManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyToMineAtentionAcFxManager getInstance() {
        return instance;
    }

    private NotifyToMineAtentionAcFxMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyToMineAtentionAcFxMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyActivityToFlag(boolean flag, List<GetMyfocusCircleBean.GetMyfocusCircleInfoBean> list, int p) {
        if (listener != null) {
            listener.sendMessageToMineAtentionAcFxFlag(flag, list, p);
        }
    }

}
