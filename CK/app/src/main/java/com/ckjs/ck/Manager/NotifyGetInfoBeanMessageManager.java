package com.ckjs.ck.Manager;

import com.ckjs.ck.Message.NotifyGetInfoBeanMessage;
import com.ckjs.ck.Bean.GetInfoBean;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyGetInfoBeanMessageManager {

    private static NotifyGetInfoBeanMessageManager instanceSignBean = new NotifyGetInfoBeanMessageManager();

    private NotifyGetInfoBeanMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyGetInfoBeanMessageManager getInstanceSignBean() {
        return instanceSignBean;
    }

    private NotifyGetInfoBeanMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessageSignBean(NotifyGetInfoBeanMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param getUserInfoBean
     */
    public void sendNotifyGetSignInfoBean(GetInfoBean.GetUserInfoBean getUserInfoBean) {
        if(listener!=null) {
            listener.sendMessageGetSignBean(getUserInfoBean);
        }
    }

}
