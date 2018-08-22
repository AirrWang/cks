package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ComnumBean {
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

    private String status;
    private String msg;

    public ComnumBeanInfo getInfo() {
        return info;
    }

    public void setInfo(ComnumBeanInfo info) {
        this.info = info;
    }

    public ComnumBeanInfo info;

    public class ComnumBeanInfo {
        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        private String num;
    }
}
