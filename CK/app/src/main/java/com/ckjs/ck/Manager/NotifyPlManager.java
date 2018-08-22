package com.ckjs.ck.Manager;

import com.ckjs.ck.Bean.GetcommentBean;
import com.ckjs.ck.Message.NotifyActicityToFinishendMessage;
import com.ckjs.ck.Message.NotifyPlGzMessage;
import com.ckjs.ck.Message.NotifyPlLookMessage;
import com.ckjs.ck.Message.NotifyPlMessage;
import com.ckjs.ck.Message.NotifyPlMineMessage;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyPlManager {

    private static NotifyPlManager instance = new NotifyPlManager();

    private NotifyPlManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyPlManager getInstance() {
        return instance;
    }

    private NotifyPlMessage listener;
    private NotifyPlLookMessage listenerNotifyPlLookMessage;
    private NotifyPlMineMessage listenerNotifyPlMineMessage;
    private NotifyPlGzMessage listenerNotifyPlGzMessage;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyPlLookMessage nm) {
        listenerNotifyPlLookMessage = nm;
    }

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyPlMineMessage nm) {
        listenerNotifyPlMineMessage = nm;
    }

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyPlMessage nm) {
        listener = nm;
    }    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyPlGzMessage nm) {
        listenerNotifyPlGzMessage = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendPlFlag(boolean flag, List<GetcommentBean.GetcommentBeanInfo> listAll, int p) {
        if (listener != null) {
            listener.sendPlFlag(flag, listAll, p);
        }
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendPlMineFlag(boolean flag, List<GetcommentBean.GetcommentBeanInfo> listAll, int p) {
        if (listenerNotifyPlMineMessage != null) {
            listenerNotifyPlMineMessage.sendPlMineFlag(flag, listAll, p);
        }
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendPlLookFlag(boolean flag, List<GetcommentBean.GetcommentBeanInfo> listAll, int p) {
        if (listenerNotifyPlLookMessage != null) {
            listenerNotifyPlLookMessage.sendPlLookFlag(flag, listAll, p);
        }
    }  /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendPlGzFlag(boolean flag, List<GetcommentBean.GetcommentBeanInfo> listAll, int p) {
        if (listenerNotifyPlGzMessage != null) {
            listenerNotifyPlGzMessage.sendPlGzFlag(flag, listAll, p);
        }
    }

}
