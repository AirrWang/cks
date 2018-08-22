package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class GetnfcaccessBean {
    private String status;
    private String msg;
    private GetnfcaccessBeanInfo info;
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

    public void setInfo(GetnfcaccessBeanInfo info) {
        this.info = info;
    }
    public GetnfcaccessBeanInfo getInfo() {
        return info;
    }

    public class GetnfcaccessBeanInfo {
        private String accesscard;
        private String gymname;
        public void setAccesscard(String accesscard) {
            this.accesscard = accesscard;
        }
        public String getAccesscard() {
            return accesscard;
        }

        public void setGymname(String gymname) {
            this.gymname = gymname;
        }
        public String getGymname() {
            return gymname;
        }
    }
}
