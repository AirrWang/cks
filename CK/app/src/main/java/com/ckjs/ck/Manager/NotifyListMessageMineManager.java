package com.ckjs.ck.Manager;

import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Bean.GetCircleMineBean;
import com.ckjs.ck.Message.NotifyListMessage;
import com.ckjs.ck.Message.NotifyListMineMessage;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class NotifyListMessageMineManager {

    private static NotifyListMessageMineManager instance = new NotifyListMessageMineManager();

    private NotifyListMessageMineManager() {
    }

    /**
     * 采取单例模式
     *
     * @return
     */
    public static NotifyListMessageMineManager getInstance() {
        return instance;
    }

    private NotifyListMineMessage listener;

    /**
     * A类注册管理器方法
     *
     * @param nm
     */
    public void setNotifyMessage(NotifyListMineMessage nm) {
        listener = nm;
    }

    /**
     * B类回答问题方法(通知刷新)
     *
     * @param flag
     */
    public void sendLIstFlag(boolean flag, List<GetCircleMineBean.InfoBean> list) {
        if (listener != null) {
            listener.sendMessageListFlag(flag, list);
        }
    }

}
