package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class FocusarchivesBean {
    private String status;
    private String msg;
    private FocusarchivesInfoBean info;

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

    public FocusarchivesInfoBean getInfo() {
        return info;
    }

    public void setInfo(FocusarchivesInfoBean info) {
        this.info = info;
    }

    public static class FocusarchivesInfoBean {
        private String username;
        private String picurl;
        private String sex;
        private String age;
        private String focus_id;
        private String steps;
        private String wsteps;
        private String fat;
        private String wfat;
        private String runnum;
        private String wrunnum;
        private String healthscore;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getFocus_id() {
            return focus_id;
        }

        public void setFocus_id(String focus_id) {
            this.focus_id = focus_id;
        }

        public String getSteps() {
            return steps;
        }

        public void setSteps(String steps) {
            this.steps = steps;
        }

        public String getWsteps() {
            return wsteps;
        }

        public void setWsteps(String wsteps) {
            this.wsteps = wsteps;
        }

        public String getFat() {
            return fat;
        }

        public void setFat(String fat) {
            this.fat = fat;
        }

        public String getWfat() {
            return wfat;
        }

        public void setWfat(String wfat) {
            this.wfat = wfat;
        }

        public String getRunnum() {
            return runnum;
        }

        public void setRunnum(String runnum) {
            this.runnum = runnum;
        }

        public String getWrunnum() {
            return wrunnum;
        }

        public void setWrunnum(String wrunnum) {
            this.wrunnum = wrunnum;
        }

        public String getHealthscore() {
            return healthscore;
        }

        public void setHealthscore(String healthscore) {
            this.healthscore = healthscore;
        }
    }
}
