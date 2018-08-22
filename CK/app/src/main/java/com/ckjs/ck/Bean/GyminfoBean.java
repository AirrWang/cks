package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GyminfoBean {
    private String status;
    private String msg;
    private GyminfoBeanInfo info;

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

    public void setInfo(GyminfoBeanInfo info) {
        this.info = info;
    }

    public GyminfoBeanInfo getInfo() {
        return info;
    }

    public class GyminfoBeanInfo {
        private String id;
        private String name;
        private String place;
        private List<String> picture;
        private String tel;
        private String intro;
        private String sort;
        private String num;
        private String lat;
        private String lon;
        private String userman;

        public String getUserman() {
            return userman;
        }

        public void setUserman(String userman) {
            this.userman = userman;
        }

        public String getUserwoman() {
            return userwoman;
        }

        public void setUserwoman(String userwoman) {
            this.userwoman = userwoman;
        }

        private String userwoman;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getPlace() {
            return place;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }

        public List<String> getPicture() {
            return picture;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getTel() {
            return tel;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getIntro() {
            return intro;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getSort() {
            return sort;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getNum() {
            return num;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLat() {
            return lat;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getLon() {
            return lon;
        }


    }
}
