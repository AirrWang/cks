package com.ckjs.ck.Message;

import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Bean.GetCircleMineBean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public interface NotifyListMineMessage {
     void sendMessageListFlag(boolean flag, List<GetCircleMineBean.InfoBean> list);
}
