package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class BodyanalysedBean {
    private String status;
    private String msg;
    private BodyanalysedBeanInfo info;

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

    public void setInfo(BodyanalysedBeanInfo info) {
        this.info = info;
    }

    public BodyanalysedBeanInfo getInfo() {
        return info;
    }

    public class BodyanalysedBeanInfo {
        private String bodyanalyse;
        private List<Date> date;

        public void setBodyanalyse(String bodyanalyse) {
            this.bodyanalyse = bodyanalyse;
        }

        public String getBodyanalyse() {
            return bodyanalyse;
        }

        public void setDate(List<Date> date) {
            this.date = date;
        }

        public List<Date> getDate() {
            return date;
        }

        public class Date {
            private String name;
            private String place;
            private String tel;
            private String lat;
            private String lon;
            private String time;
            private String distance;

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

            public void setTime(String time) {
                this.time = time;
            }

            public String getTime() {
                return time;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getDistance() {
                return distance;
            }

        }
    }
}
