package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class UsercommentBean {
    private String status;
    private String msg;
    private List<UsercommentBeanInfo> info;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setInfo(List<UsercommentBeanInfo> info) {
        this.info = info;
    }

    public List<UsercommentBeanInfo> getInfo() {
        return info;
    }

    public class UsercommentBeanInfo {
        private String time;
        private String content;
        private String username;
        private String picurl;
        private String circle_id;

        public String getFocus_id() {
            return focus_id;
        }

        public void setFocus_id(String focus_id) {
            this.focus_id = focus_id;
        }

        private String focus_id;
        private String picture;

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        private String sex;

        public String getCircle_content() {
            return circle_content;
        }

        public void setCircle_content(String circle_content) {
            this.circle_content = circle_content;
        }

        public String getCircle_id() {
            return circle_id;
        }

        public void setCircle_id(String circle_id) {
            this.circle_id = circle_id;
        }

        private String circle_content;

        public void setTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getPicture() {
            return picture;
        }


    }
}
