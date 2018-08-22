package com.ckjs.ck.Manager;

import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Bean.GetCircleMineBean;
import com.ckjs.ck.Bean.GetCircleTjBean;
import com.ckjs.ck.Message.NotifyActicityToFinishendMessage;
import com.ckjs.ck.Message.NotifyToMainAcFxMessage;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyToMainAcFxManager {

    private static NotifyToMainAcFxManager instance = new NotifyToMainAcFxManager();

    private NotifyToMainAcFxManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyToMainAcFxManager getInstance() {
        return instance;
    }

    private NotifyToMainAcFxMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyToMainAcFxMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendNotifyActivityToFlag(boolean flag, List<GetCircleBean.InfoBean> list, int p) {
        if (listener != null) {
            listener.sendMessageToMainAcFxFlag(flag, list, p);
        }
    }

    public void sendNotifyActivityTotjFlag(boolean flag, List<GetCircleTjBean.InfoBean> list, int p) {
        if (listener != null) {
            listener.sendMessageToTjMainAcFxFlag(flag, list, p);
        }
    }

    public void sendNotifyActivityToMineFlag(boolean flag, List<GetCircleMineBean.InfoBean> list, int p) {
        if (listener != null) {
            listener.sendMessageMineToMainAcFxFlag(flag, list, p);
        }
    }

}
