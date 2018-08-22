package com.ckjs.ck.Message;

import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Bean.GetCircleMineBean;
import com.ckjs.ck.Bean.GetCircleTjBean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public interface NotifyToMainAcFxMessage {
     void sendMessageToMainAcFxFlag(boolean flag, List<GetCircleBean.InfoBean> list, int p);
     void sendMessageMineToMainAcFxFlag(boolean flag, List<GetCircleMineBean.InfoBean> list, int p);
     void sendMessageToTjMainAcFxFlag(boolean flag, List<GetCircleTjBean.InfoBean> list, int p);
}
