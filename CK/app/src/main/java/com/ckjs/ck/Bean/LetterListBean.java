package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class LetterListBean {
    private String status;
    private String msg;
    private List<LetterListInfoBean> info;

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

    public List<LetterListInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<LetterListInfoBean> info) {
        this.info = info;
    }

    public static class LetterListInfoBean {
        private String add_id;
        private String letter_id;
        private String username;
        private String picurl;
        private String content;
        private String lasttime;
        private String noread;
        private String sex;

        public String getAdd_id() {
            return add_id;
        }

        public void setAdd_id(String add_id) {
            this.add_id = add_id;
        }

        public String getLetter_id() {
            return letter_id;
        }

        public void setLetter_id(String letter_id) {
            this.letter_id = letter_id;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLasttime() {
            return lasttime;
        }

        public void setLasttime(String lasttime) {
            this.lasttime = lasttime;
        }

        public String getNoread() {
            return noread;
        }

        public void setNoread(String noread) {
            this.noread = noread;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
