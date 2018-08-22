package com.ckjs.ck.Message;

import com.ckjs.ck.Bean.GetcommentBean;

import java.util.List;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public interface NotifyPlLookMessage {
     void sendPlLookFlag(boolean flag, List<GetcommentBean.GetcommentBeanInfo> listAll, int p);
}
