package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class DirinfoBean {
    private String status;
    private String msg;
    private DirinfoBeanInfo info;

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

    public void setInfo(DirinfoBeanInfo info) {
        this.info = info;
    }

    public DirinfoBeanInfo getInfo() {
        return info;
    }

    public class DirinfoBeanInfo {
        private String directId;
        private String introduce;
        private String gist;
        private String goal;
        private String videourl;
        private String difficult;
        private String fat;
        private String time;
        private String name;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        private String cover;
        private List<String> picture;

        public void setDirectId(String directId) {
            this.directId = directId;
        }

        public String getDirectId() {
            return directId;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setGist(String gist) {
            this.gist = gist;
        }

        public String getGist() {
            return gist;
        }

        public void setGoal(String goal) {
            this.goal = goal;
        }

        public String getGoal() {
            return goal;
        }

        public void setVideourl(String videourl) {
            this.videourl = videourl;
        }

        public String getVideourl() {
            return videourl;
        }

        public void setDifficult(String difficult) {
            this.difficult = difficult;
        }

        public String getDifficult() {
            return difficult;
        }

        public void setFat(String fat) {
            this.fat = fat;
        }

        public String getFat() {
            return fat;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }

        public List<String> getPicture() {
            return picture;
        }

    }
}
