package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ReadlistBean {
    private String status;
    private String msg;
    private List<ReadlistBeanInfo> info;
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

    public void setInfo(List<ReadlistBeanInfo> info) {
        this.info = info;
    }
    public List<ReadlistBeanInfo> getInfo() {
        return info;
    }
    public class ReadlistBeanInfo {

        private String id;
        private String title;
        private String intro;
        private String readurl;
        private String picture;
        private String classname;
        private String type;

        public String getIsfavorite() {
            return isfavorite;
        }

        public void setIsfavorite(String isfavorite) {
            this.isfavorite = isfavorite;
        }

        private String isfavorite;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }
        public String getIntro() {
            return intro;
        }

        public void setReadurl(String readurl) {
            this.readurl = readurl;
        }
        public String getReadurl() {
            return readurl;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
        public String getPicture() {
            return picture;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }
        public String getClassname() {
            return classname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
