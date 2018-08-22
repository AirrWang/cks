package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class PreWarningBean {
    private String status;
    private String msg;
    private PreWarningInfoBean info;

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

    public PreWarningInfoBean getInfo() {
        return info;
    }

    public void setInfo(PreWarningInfoBean info) {
        this.info = info;
    }

    public static class PreWarningInfoBean {
        private String day;
        private String apm;
        private String time;
        private String warnings;
        private String spressure;
        private String dpressure;
        private String advice;

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

        public String getSpressure() {
            return spressure;
        }

        public void setSpressure(String spressure) {
            this.spressure = spressure;
        }

        public String getDpressure() {
            return dpressure;
        }

        public void setDpressure(String dpressure) {
            this.dpressure = dpressure;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }
    }
}
