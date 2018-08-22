package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class RateWarningBean {
    private String status;
    private String msg;
    private RateWarningInfoBean info;

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

    public RateWarningInfoBean getInfo() {
        return info;
    }

    public void setInfo(RateWarningInfoBean info) {
        this.info = info;
    }

    public static class RateWarningInfoBean {

        private String day;
        private String apm;
        private String time;
        private String warnings;
        private String rate;
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

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }
    }
}
