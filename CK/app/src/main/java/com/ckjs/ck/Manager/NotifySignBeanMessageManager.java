package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifySignBeanMessage;
import com.ckjs.ck.Bean.GetSignBean;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifySignBeanMessageManager {

    private static NotifySignBeanMessageManager instanceSignBean = new NotifySignBeanMessageManager();

    private NotifySignBeanMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifySignBeanMessageManager getInstanceSignBean() {
        return instanceSignBean;
    }

    private NotifySignBeanMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessageSignBean(NotifySignBeanMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param getSignInfoBean
     */
    public void sendNotifyGetSignInfoBean(GetSignBean.GetSignInfoBean getSignInfoBean) {
        if(listener!=null) {
            listener.sendMessageGetSignBean(getSignInfoBean);
        }
    }

}
