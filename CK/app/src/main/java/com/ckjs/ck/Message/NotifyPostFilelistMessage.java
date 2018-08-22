package com.ckjs.ck.Message;

import java.util.ArrayList;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public interface NotifyPostFilelistMessage {
     void sendMessagePostFilelist(boolean flag, ArrayList<String> filelist);
}
