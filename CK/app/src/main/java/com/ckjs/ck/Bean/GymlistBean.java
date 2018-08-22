package com.ckjs.ck.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GymlistBean {
    private String status;
    private String msg;
    private List<GymlistBeanInfo> info;
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

    public void setInfo(List<GymlistBeanInfo> info) {
        this.info = info;
    }
    public List<GymlistBeanInfo> getInfo() {
        return info;
    }

    public class GymlistBeanInfo implements Serializable{

        private String id;
        private String name;
        private String place;
        private String tel;
        private String lat;
        private String lon;
        private String intro;
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

        public void setTel(String tel) {
            this.tel = tel;
        }
        public String getTel() {
            return tel;
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

        public void setIntro(String intro) {
            this.intro = intro;
        }
        public String getIntro() {
            return intro;
        }
    }
}
