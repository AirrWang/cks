package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CircleDongTaiBean {
    public String status;
    public String msg;
    public CircleDongTaiBeanInfo info;

    /**
     * {
     * "status": "1",
     * "msg": "发表成功",
     * "info": {
     * "circle_id": "310",
     * "thumb": [],
     * "content": "123"
     * }
     * }
     */

    public class CircleDongTaiBeanInfo {
        public String getCircle_id() {
            return circle_id;
        }

        public void setCircle_id(String circle_id) {
            this.circle_id = circle_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String circle_id;
        public String content;


        public List<String> getThumb() {
            return thumb;
        }

        public void setThumb(List<String> thumb) {
            this.thumb = thumb;
        }

        public List<String> thumb;

    }
}
