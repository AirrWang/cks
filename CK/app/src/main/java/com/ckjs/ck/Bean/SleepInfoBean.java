package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class SleepInfoBean {
    private String status;
    private String msg;
    private SleepInfoDetailBean info;

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

    public SleepInfoDetailBean getInfo() {
        return info;
    }

    public void setInfo(SleepInfoDetailBean info) {
        this.info = info;
    }

    public static class SleepInfoDetailBean {
        private String sleepgoals;
        private List<SleepDetailBean> sleepinfo;

        public String getSleepgoals() {
            return sleepgoals;
        }

        public void setSleepgoals(String sleepgoals) {
            this.sleepgoals = sleepgoals;
        }

        public List<SleepDetailBean> getSleepinfo() {
            return sleepinfo;
        }

        public void setSleepinfo(List<SleepDetailBean> sleepinfo) {
            this.sleepinfo = sleepinfo;
        }

        public static class SleepDetailBean {
            private String date;
            private String hlength;
            private String ilength;
            private String shlength;
            private String silength;
            private String qhlength;
            private String qilength;
            private String ahlength;
            private String ailength;
            private String sleeptime;
            private String awaketime;
            private String sleepquality;
            private List<String> slog;

            public String getSleeptime() {
                return sleeptime;
            }

            public void setSleeptime(String sleeptime) {
                this.sleeptime = sleeptime;
            }

            public String getAwaketime() {
                return awaketime;
            }

            public void setAwaketime(String awaketime) {
                this.awaketime = awaketime;
            }

            public String getSleepquality() {
                return sleepquality;
            }

            public void setSleepquality(String sleepquality) {
                this.sleepquality = sleepquality;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public List<String> getSlog() {
                return slog;
            }

            public void setSlog(List<String> slog) {
                this.slog = slog;
            }

            public String getHlength() {
                return hlength;
            }

            public void setHlength(String hlength) {
                this.hlength = hlength;
            }

            public String getIlength() {
                return ilength;
            }

            public void setIlength(String ilength) {
                this.ilength = ilength;
            }

            public String getShlength() {
                return shlength;
            }

            public void setShlength(String shlength) {
                this.shlength = shlength;
            }

            public String getSilength() {
                return silength;
            }

            public void setSilength(String silength) {
                this.silength = silength;
            }

            public String getQhlength() {
                return qhlength;
            }

            public void setQhlength(String qhlength) {
                this.qhlength = qhlength;
            }

            public String getQilength() {
                return qilength;
            }

            public void setQilength(String qilength) {
                this.qilength = qilength;
            }

            public String getAhlength() {
                return ahlength;
            }

            public void setAhlength(String ahlength) {
                this.ahlength = ahlength;
            }

            public String getAilength() {
                return ailength;
            }

            public void setAilength(String ailength) {
                this.ailength = ailength;
            }
        }
    }
}
