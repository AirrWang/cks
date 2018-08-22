package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class RelnameBean {
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

    public RelnameInfoBean getInfo() {
        return info;
    }

    public void setInfo(RelnameInfoBean info) {
        this.info = info;
    }

    private String status;
    private String msg;


    private RelnameInfoBean info;

    public class RelnameInfoBean {
        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        private String integral;

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        private String vip;

        public String getRelname() {
            return relname;
        }

        public void setRelname(String relname) {
            this.relname = relname;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        private String relname;
        private String tel;
        //          "relname": "马宏杰",
        //                  "tel": ""
    }
}
