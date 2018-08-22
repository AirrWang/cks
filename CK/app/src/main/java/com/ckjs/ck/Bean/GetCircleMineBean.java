package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class GetCircleMineBean {
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String msg;
    private String status;

    public String getNewcom() {
        return newcom;
    }

    public void setNewcom(String newcom) {
        this.newcom = newcom;
    }

    private String newcom;
    private List<InfoBean> info;

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public class InfoBean {
        private String lat;
        private String lon;
        private String isthumb;
        private String thumbsum;

        public String getNewcom() {
            return newcom;
        }

        public void setNewcom(String newcom) {
            this.newcom = newcom;
        }

        private String newcom;

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        private String sex;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        private List<String> picture;

        public List<String> getThumb() {
            return thumb;
        }

        public void setThumb(List<String> thumb) {
            this.thumb = thumb;
        }

        private List<String> thumb;



        public List<String> getPicture() {
            return picture;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }

        private int id;//|朋友圈id|int
        private String username;

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        private String place;
        private String picurl;//|发表用户头像地址|字符串
        private int user_id;//|发表用户id|int

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getComnum() {
            return comnum;
        }

        public void setComnum(String comnum) {
            this.comnum = comnum;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private String time;// time|发表时间|yyyy-mm-dd hh:ii:ss
        private String content; // content|发表内容|字符串
        private String comnum;//comnum|评论数|int
        public List<GetCircleCommentBean> getComment() {
            return comment;
        }

        public void setComment(List<GetCircleCommentBean> comment) {
            this.comment = comment;
        }

        private List<GetCircleCommentBean> comment;

        public String getIsthumb() {
            return isthumb;
        }

        public String getThumbsum() {
            return thumbsum;
        }

        public class GetCircleCommentBean {
            private String username;

            public String getAddname() {
                return addname;
            }

            public void setAddname(String addname) {
                this.addname = addname;
            }

            private String addname;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            private String content;
        }
    }

}
