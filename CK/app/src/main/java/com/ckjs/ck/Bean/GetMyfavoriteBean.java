package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetMyfavoriteBean {
    private String status;
    private String msg;
    private List<GetMyfavoriteInfoBean> info;

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

    public List<GetMyfavoriteInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<GetMyfavoriteInfoBean> info) {
        this.info = info;
    }

    public static class GetMyfavoriteInfoBean {
        private String read_id;
        private String title;
        private String intro;
        private String readurl;
        private String picture;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type;

        public String getRead_id() {
            return read_id;
        }

        public void setRead_id(String read_id) {
            this.read_id = read_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getReadurl() {
            return readurl;
        }

        public void setReadurl(String readurl) {
            this.readurl = readurl;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}
