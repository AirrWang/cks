package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class BandstatusBean {
    private String status;
    private String msg;
    private BandstatusBeanInfo info;

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

    public void setInfo(BandstatusBeanInfo info) {
        this.info = info;
    }

    public BandstatusBeanInfo getInfo() {
        return info;
    }

    public class BandstatusBeanInfo {
        private String status;
        private String type;
        private String ordernum;
        private String pay;

        public String getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(String ordernum) {
            this.ordernum = ordernum;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPay() {
            return pay;
        }

        public void setPay(String pay) {
            this.pay = pay;
        }


        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

    }
}
