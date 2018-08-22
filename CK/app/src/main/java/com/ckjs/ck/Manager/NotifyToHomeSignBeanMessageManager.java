package com.ckjs.ck.Manager;

import com.ckjs.ck.Bean.GetSignBean;
import com.ckjs.ck.Message.NotifySignBeanMessage;
import com.ckjs.ck.Message.NotifyToHomeSignBeanMessage;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyToHomeSignBeanMessageManager {

    private static NotifyToHomeSignBeanMessageManager instanceSignBean = new NotifyToHomeSignBeanMessageManager();

    private NotifyToHomeSignBeanMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyToHomeSignBeanMessageManager getInstanceSignBean() {
        return instanceSignBean;
    }

    private NotifyToHomeSignBeanMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessageSignBean(NotifyToHomeSignBeanMessage nm) {
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
