package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetMyfocusDetailBean {
    private String status;
    private String msg;
    private GetMyfocusDetailInfoBean info;

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

    public GetMyfocusDetailInfoBean getInfo() {
        return info;
    }

    public void setInfo(GetMyfocusDetailInfoBean info) {
        this.info = info;
    }

    public static class GetMyfocusDetailInfoBean {
        private String gym_name;
        private String username;
        private String picurl;
        private String sex;
        private String focus_id;
        private String fanst;
        private String motto;
        private String gym_id;

        public String getAliasname() {
            return aliasname;
        }

        public void setAliasname(String aliasname) {
            this.aliasname = aliasname;
        }

        private String aliasname;

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        private String vip;

        public String getGym_name() {
            return gym_name;
        }

        public void setGym_name(String gym_name) {
            this.gym_name = gym_name;
        }

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

        public String getFocus_id() {
            return focus_id;
        }

        public void setFocus_id(String focus_id) {
            this.focus_id = focus_id;
        }

        public String getFanst() {
            return fanst;
        }

        public void setFanst(String fanst) {
            this.fanst = fanst;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getMotto() {
            return motto;
        }

        public void setMotto(String motto) {
            this.motto = motto;
        }

        public String getGym_id() {
            return gym_id;
        }

        public void setGym_id(String gym_id) {
            this.gym_id = gym_id;
        }
    }
}
