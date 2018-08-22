package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetMyfocusCircleBean {
    private String status;
    private String msg;
    private List<GetMyfocusCircleInfoBean> info;

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

    public List<GetMyfocusCircleInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<GetMyfocusCircleInfoBean> info) {
        this.info = info;
    }

    public  class GetMyfocusCircleInfoBean {
        private String id;
        private String username;
        private String picurl;
        private String user_id;
        private String time;
        private String content;

        public String getAliasname() {
            return aliasname;
        }

        public void setAliasname(String aliasname) {
            this.aliasname = aliasname;
        }

        private String aliasname;
        private List<String> picture;

        public List<String> getThumb() {
            return thumb;
        }

        public void setThumb(List<String> thumb) {
            this.thumb = thumb;
        }

        private List<String> thumb;
        private String comnum;
        private String lat;
        private String isthumb;
        private String thumbsum;

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        private String sex;

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        private String lon;

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        private String place;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public List<String> getPicture() {
            return picture;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }


        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getComnum() {
            return comnum;
        }

        public void setComnum(String comnum) {
            this.comnum = comnum;
        }

        public List<GetMyfocusCircleCommentBean> getComment() {
            return comment;
        }

        public void setComment(List<GetMyfocusCircleCommentBean> comment) {
            this.comment = comment;
        }

        private List<GetMyfocusCircleCommentBean> comment;

        public String getIsthumb() {
            return isthumb;
        }

        public String getThumbsum() {
            return thumbsum;
        }

        public class GetMyfocusCircleCommentBean {
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
