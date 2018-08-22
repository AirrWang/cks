package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ReadBean {
    private String status;
    private String msg;
    private List<ReadBeanInfo> info;
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

    public void setInfo(List<ReadBeanInfo> info) {
        this.info = info;
    }
    public List<ReadBeanInfo> getInfo() {
        return info;
    }
    public class ReadBeanInfo {

        private String id;
        private String title;
        private String intro;
        private String readurl;
        private String picture;

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

    }
}
