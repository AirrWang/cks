package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class BandBean {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BandBeanInfo getInfo() {
        return info;
    }

    public void setInfo(BandBeanInfo info) {
        this.info = info;
    }

    private String status;
    private String msg;
    private BandBeanInfo info;

    public class BandBeanInfo {
        private String type;

        public String getIsbind() {
            return isbind;
        }

        public void setIsbind(String isbind) {
            this.isbind = isbind;
        }

        private String isbind;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(String ordernum) {
            this.ordernum = ordernum;
        }


        private String ordernum;
    }
}
