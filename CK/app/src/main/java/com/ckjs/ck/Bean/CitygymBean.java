package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CitygymBean {
    private String status;
    private String msg;
    private List<CitygymBeanInfo> info;

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

    public void setInfo(List<CitygymBeanInfo> info) {
        this.info = info;
    }

    public List<CitygymBeanInfo> getInfo() {
        return info;
    }

    public class CitygymBeanInfo {
        private String distance;




        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;
        private String name;
        private String place;
        private String num;
        private String open;
        private String grade;
        private String lat;
        private String lon;

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getDistance() {
            return distance;
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

        public void setNum(String num) {
            this.num = num;
        }

        public String getNum() {
            return num;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getOpen() {
            return open;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getGrade() {
            return grade;
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
