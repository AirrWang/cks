package com.ckjs.ck.Manager;

import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Message.NotifyListLookedMessage;
import com.ckjs.ck.Message.NotifyListMineMessage;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyListMessageLookedManager {

    private static NotifyListMessageLookedManager instance = new NotifyListMessageLookedManager();

    private NotifyListMessageLookedManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyListMessageLookedManager getInstance() {
        return instance;
    }

    private NotifyListLookedMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyListLookedMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendLIstFlag(boolean flag, List<GetCircleBean.InfoBean> list) {
        if (listener != null) {
            listener.sendMessageListFlag(flag, list);
        }
    }

}
