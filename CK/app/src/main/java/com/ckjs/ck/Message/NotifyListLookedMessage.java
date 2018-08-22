package com.ckjs.ck.Message;

import com.ckjs.ck.Bean.GetCircleBean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public interface NotifyListLookedMessage {
     void sendMessageListFlag(boolean flag, List<GetCircleBean.InfoBean> list);
}
