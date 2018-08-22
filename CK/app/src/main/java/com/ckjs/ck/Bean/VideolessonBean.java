package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class VideolessonBean {
    private String status;
    private String msg;
    private VideolessonBeanInfo info;

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

    public void setInfo(VideolessonBeanInfo info) {
        this.info = info;
    }

    public VideolessonBeanInfo getInfo() {
        return info;
    }

    public class VideolessonBeanInfo {
        private String name;
        private String tel;
        private String handler;
        private String amound;
        private String type;
        private String sex;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String status;

        public String getEase_id() {
            return ease_id;
        }

        public void setEase_id(String ease_id) {
            this.ease_id = ease_id;
        }

        private String ease_id;


        public String getRelname() {
            return relname;
        }

        public void setRelname(String relname) {
            this.relname = relname;
        }

        private String relname;
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
        public String getTel() {
            return tel;
        }

        public void setHandler(String handler) {
            this.handler = handler;
        }
        public String getHandler() {
            return handler;
        }

        public void setAmound(String amound) {
            this.amound = amound;
        }
        public String getAmound() {
            return amound;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
        public String getSex() {
            return sex;
        }
    }
}
