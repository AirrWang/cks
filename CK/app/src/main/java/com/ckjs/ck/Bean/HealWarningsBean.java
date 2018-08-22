package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class HealWarningsBean {
    private String status;
    private String msg;
    private List<HealWarningsInfoBean> info;

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

    public List<HealWarningsInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<HealWarningsInfoBean> info) {
        this.info = info;
    }

    public static class HealWarningsInfoBean {
        private  String warning_id;
        private String day;
        private String apm;
        private String time;
        private String warnings;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getApm() {
            return apm;
        }

        public void setApm(String apm) {
            this.apm = apm;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getWarnings() {
            return warnings;
        }

        public void setWarnings(String warnings) {
            this.warnings = warnings;
        }

        public String getWarning_id() {
            return warning_id;
        }

        public void setWarning_id(String warning_id) {
            this.warning_id = warning_id;
        }
    }
}
