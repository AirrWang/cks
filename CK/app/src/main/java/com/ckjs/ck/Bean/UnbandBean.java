package com.ckjs.ck.Bean;

/**
 * Created by NiPing on 2017/6/9.
 */

public class UnbandBean {
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

    public UnbandInforBean getInfo() {
        return info;
    }

    public void setInfo(UnbandInforBean info) {
        this.info = info;
    }

    private UnbandInforBean info;

    public class UnbandInforBean {
        public String getIsbind() {
            return isbind;
        }

        public void setIsbind(String isbind) {
            this.isbind = isbind;
        }

        private  String isbind;
    }
}
