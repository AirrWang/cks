package com.ckjs.ck.Message;

import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Bean.GetMyfocusCircleBean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public interface NotifyToMineAtentionAcFxMessage {
     void sendMessageToMineAtentionAcFxFlag(boolean flag, List<GetMyfocusCircleBean.GetMyfocusCircleInfoBean> list, int p);
}
