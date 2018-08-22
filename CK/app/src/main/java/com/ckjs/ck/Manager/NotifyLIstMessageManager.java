package com.ckjs.ck.Manager;

import com.ckjs.ck.Bean.GetCircleTjBean;
import com.ckjs.ck.Message.NotifyListMessage;
import com.ckjs.ck.Bean.GetCircleBean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyLIstMessageManager {

    private static NotifyLIstMessageManager instance = new NotifyLIstMessageManager();

    private NotifyLIstMessageManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyLIstMessageManager getInstance() {
        return instance;
    }

    private NotifyListMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyListMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendLIstFlag(boolean flag, List<GetCircleTjBean.InfoBean> list) {
        if (listener != null) {
            listener.sendMessageListFlag(flag, list);
        }
    }

}
