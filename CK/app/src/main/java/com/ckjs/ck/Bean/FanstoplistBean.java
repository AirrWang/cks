package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class FanstoplistBean {
    private String status;
    private String msg;
    private List<FanstoplistBeanInfo> info;
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setInfo(List<FanstoplistBeanInfo> info) {
        this.info = info;
    }
    public List<FanstoplistBeanInfo> getInfo() {
        return info;
    }

    public class FanstoplistBeanInfo {
        private String user_id;
        private String username;
        private String picurl;
        private String fanssum;
        public void setUserId(String user_id) {
            this.user_id = user_id;
        }
        public String getUserId() {
            return user_id;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public String getUsername() {
            return username;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }
        public String getPicurl() {
            return picurl;
        }

        public void setFanssum(String fanssum) {
            this.fanssum = fanssum;
        }
        public String getFanssum() {
            return fanssum;
        }

    }
}
