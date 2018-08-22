package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MSleepInfoBean {
    private String status;
    private String msg;
    private MSleepDetailInfoBean info;

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

    public MSleepDetailInfoBean getInfo() {
        return info;
    }

    public void setInfo(MSleepDetailInfoBean info) {
        this.info = info;
    }

    public static class MSleepDetailInfoBean {
        private String month;
        private String sleepgoals;
        private String sleepquality;
        private String hlength;
        private String ilength;
        private String shlength;
        private String silength;
        private String qhlength;
        private String qilength;
        private String sleeptime;
        private String awaketime;
        private String ahlength;
        private String ailength;
        private int spercentage;
        private int qpercentage;
        private int apercentage;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getSleepgoals() {
            return sleepgoals;
        }

        public void setSleepgoals(String sleepgoals) {
            this.sleepgoals = sleepgoals;
        }

        public String getSleepquality() {
            return sleepquality;
        }

        public void setSleepquality(String sleepquality) {
            this.sleepquality = sleepquality;
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

        public int getSpercentage() {
            return spercentage;
        }

        public void setSpercentage(int spercentage) {
            this.spercentage = spercentage;
        }

        public int getQpercentage() {
            return qpercentage;
        }

        public void setQpercentage(int qpercentage) {
            this.qpercentage = qpercentage;
        }

        public int getApercentage() {
            return apercentage;
        }

        public void setApercentage(int apercentage) {
            this.apercentage = apercentage;
        }
    }
}
