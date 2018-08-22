package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CrinfoBean {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CrinfoBeanInfo getInfo() {
        return info;
    }

    public void setInfo(CrinfoBeanInfo info) {
        this.info = info;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String status;//|状态码|1：获取成功；0：获取失败；2：token值错误
    private String msg;//|错误信息|字符串

    private CrinfoBeanInfo info;//|返回信息|{};失败：NULL

    public class CrinfoBeanInfo {
        public int getFanst() {
            return fanst;
        }

        public void setFanst(int fanst) {
            this.fanst = fanst;
        }

        public CrinfoBeanCircle getCircle() {
            return circle;
        }

        public void setCircle(CrinfoBeanCircle circle) {
            this.circle = circle;
        }

        private int fanst;//|是否关注|1：已关注；0：未关注
        private CrinfoBeanCircle circle;//|朋友圈详情|{}



        public class CrinfoBeanCircle {
            private int id;//|朋友圈id|int

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            private String sex;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPlace() {
                return place;
            }

            public void setPlace(String place) {
                this.place = place;
            }

            public int getComnum() {
                return comnum;
            }

            public void setComnum(int comnum) {
                this.comnum = comnum;
            }

            public List<String> getPicture() {
                return picture;
            }

            public void setPicture(List<String> picture) {
                this.picture = picture;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

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

            private String username;//|发表用户昵称|字符串
            private String picurl;//|发表用户头像地址|字符串
            private int user_id;//|发表用户id|int
            private String time;//|发表时间|yyyy-mm-dd hh:ii:ss
            private String content;//|发表内容|字符串
            private List<String> picture;//|发表图片|[]
            private String place;//|发布地点|字符串
            private int comnum;//|评论数|int
        }

    }
}
