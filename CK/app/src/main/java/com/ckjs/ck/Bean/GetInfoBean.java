package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetInfoBean {
    private String status;
    private String msg;
    private GetUserInfoBean info;

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

    public GetUserInfoBean getInfo() {
        return info;
    }

    public void setInfo(GetUserInfoBean info) {
        this.info = info;
    }

    public static class GetUserInfoBean {
        private String picurl;
        private String username;
        private int height;
        private float weight;
        private int sex;
        private int age;
        private int gym_id;
        private String fanssum;
        private String motto;
        private String relname;
        private String vip;
        private String tel;
        private String gymname;

        public String getUnrentstatus() {
            return unrentstatus;
        }

        public void setUnrentstatus(String unrentstatus) {
            this.unrentstatus = unrentstatus;
        }

        private String unrentstatus;

        public String getIscoach() {
            return iscoach;
        }

        public void setIscoach(String iscoach) {
            this.iscoach = iscoach;
        }

        private String iscoach;//1认证2未认证

        public String getBodyanalyse() {
            return bodyanalyse;
        }

        public void setBodyanalyse(String bodyanalyse) {
            this.bodyanalyse = bodyanalyse;
        }

        private String bodyanalyse;

        public String getIsbind() {
            return isbind;
        }

        public void setIsbind(String isbind) {
            this.isbind = isbind;
        }

        private String isbind;

        public String getBodyinfo() {
            return bodyinfo;
        }

        public void setBodyinfo(String bodyinfo) {
            this.bodyinfo = bodyinfo;
        }

        private String bodyinfo;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        private String birthday;

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public float getWeight() {
            return weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getGym_id() {
            return gym_id;
        }

        public void setGym_id(int gym_id) {
            this.gym_id = gym_id;
        }

        public String getFanssum() {
            return fanssum;
        }

        public void setFanssum(String fanssum) {
            this.fanssum = fanssum;
        }

        public String getMotto() {
            return motto;
        }

        public void setMotto(String motto) {
            this.motto = motto;
        }

        public String getRelname() {
            return relname;
        }

        public void setRelname(String relname) {
            this.relname = relname;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getGymname() {
            return gymname;
        }

        public void setGymname(String gymname) {
            this.gymname = gymname;
        }
    }
}
