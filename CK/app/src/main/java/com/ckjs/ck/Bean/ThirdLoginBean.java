package com.ckjs.ck.Bean;

import java.io.Serializable;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ThirdLoginBean implements Serializable {
    private String status;
    private String msg;
    private ThirdLoginInfoBean info;

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

    public ThirdLoginInfoBean getInfo() {
        return info;
    }

    public void setInfo(ThirdLoginInfoBean info) {
        this.info = info;
    }

    public static class ThirdLoginInfoBean implements Serializable {
        private String openid;
        private String access_token;
        private String type;
        private String tel;
        private String token;
        private int user_id;
        private ThirdLoginUserInfoBean user_info;
        private String bodyinfo;


        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


        public ThirdLoginUserInfoBean getUser_info() {
            return user_info;
        }

        public void setUser_info(ThirdLoginUserInfoBean user_info) {
            this.user_info = user_info;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getBodyinfo() {
            return bodyinfo;
        }

        public static class ThirdLoginUserInfoBean implements Serializable {
            private String picurl;
            private String username;
            private int height;
            private float weight;
            private int sex;
            private int age;

            public int getSteps() {
                return steps;
            }

            public void setSteps(int steps) {
                this.steps = steps;
            }

            private int steps;
            private int gym_id;
            private String isfirst;
            private String fanssum;
            private String motto;
            private String relname;
            private String vip;
            private String tel;
            private String gymname;

            public String getIsbind() {
                return isbind;
            }

            public void setIsbind(String isbind) {
                this.isbind = isbind;
            }

            private String isbind;

            public String getUnrentstatus() {
                return unrentstatus;
            }

            public void setUnrentstatus(String unrentstatus) {
                this.unrentstatus = unrentstatus;
            }

            private String unrentstatus;

            public String getEasepsd() {
                return easepsd;
            }

            public void setEasepsd(String easepsd) {
                this.easepsd = easepsd;
            }

            private String easepsd;

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

            public String getIsfirst() {
                return isfirst;
            }

            public void setIsfirst(String isfirst) {
                this.isfirst = isfirst;
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
}
