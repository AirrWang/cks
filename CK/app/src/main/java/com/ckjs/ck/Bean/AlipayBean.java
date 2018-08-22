package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class AlipayBean {
    private String status;
    private String msg;
    private AlipayInfoBean info;

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

    public AlipayInfoBean getInfo() {
        return info;
    }

    public void setInfo(AlipayInfoBean info) {
        this.info = info;
    }

    public static class AlipayInfoBean {
        private String date;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
