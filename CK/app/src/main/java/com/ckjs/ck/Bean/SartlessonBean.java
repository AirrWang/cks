package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class SartlessonBean {
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String msg;

    public SartlessonBeanInfo getInfo() {
        return info;
    }

    public void setInfo(SartlessonBeanInfo info) {
        this.info = info;
    }

    private SartlessonBeanInfo info;

    public class SartlessonBeanInfo {
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String status;
    }
}
