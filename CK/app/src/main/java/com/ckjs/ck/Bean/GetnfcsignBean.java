package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class GetnfcsignBean {
    private String status;
    private String msg;
    private GetnfcsignBeanInfo info;
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

    public void setInfo(GetnfcsignBeanInfo info) {
        this.info = info;
    }
    public GetnfcsignBeanInfo getInfo() {
        return info;
    }

    public class GetnfcsignBeanInfo {
        private String signcard;
        private String gymname;
        public void setSigncard(String signcard) {
            this.signcard = signcard;
        }
        public String getSigncard() {
            return signcard;
        }

        public void setGymname(String gymname) {
            this.gymname = gymname;
        }
        public String getGymname() {
            return gymname;
        }

    }
}
