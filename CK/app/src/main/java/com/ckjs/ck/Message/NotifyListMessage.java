package com.ckjs.ck.Message;

import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Bean.GetCircleTjBean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public interface NotifyListMessage {
     void sendMessageListFlag(boolean flag, List<GetCircleTjBean.InfoBean>list);
}
