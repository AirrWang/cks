package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class TolessonBean {
    private String status;
    private String msg;
    private TolessonBeanInfo info;
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

    public void setInfo(TolessonBeanInfo info) {
        this.info = info;
    }
    public TolessonBeanInfo getInfo() {
        return info;
    }

    public class TolessonBeanInfo {
        private String id;
        private String status;
        private String relname;
        private String picurl;
        private String name;
        private String picture;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }

        public void setRelname(String relname) {
            this.relname = relname;
        }
        public String getRelname() {
            return relname;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }
        public String getPicurl() {
            return picurl;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
        public String getPicture() {
            return picture;
        }
    }
}
