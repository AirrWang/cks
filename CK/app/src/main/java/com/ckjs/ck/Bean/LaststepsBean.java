package com.ckjs.ck.Bean;

/**
 * Created by NiPing on 2017/6/16.
 */

public class LaststepsBean {
    private String status;
    private String msg;
    private InfoLaststepsBean info;

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

    public void setInfo(InfoLaststepsBean info) {
        this.info = info;
    }

    public InfoLaststepsBean getInfo() {
        return info;
    }

    public class InfoLaststepsBean {

        private String num;

        public void setNum(String num) {
            this.num = num;
        }

        public String getNum() {
            return num;
        }

    }
}
