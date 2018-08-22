package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class AnalyseBean {
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

    public AnalyseBeaInfo getInfo() {
        return info;
    }

    public void setInfo(AnalyseBeaInfo info) {
        this.info = info;
    }

    private String msg;
    private AnalyseBeaInfo info;

    public class AnalyseBeaInfo {
        private String bodyanalyse;

        public String getBodyanalyse() {
            return bodyanalyse;
        }

        public void setBodyanalyse(String bodyanalyse) {
            this.bodyanalyse = bodyanalyse;
        }

        public String getTime() {

            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        private String time;
    }

}
