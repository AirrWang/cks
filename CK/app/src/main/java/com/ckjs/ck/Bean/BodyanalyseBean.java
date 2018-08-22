package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class BodyanalyseBean {
    private String status;
    private String msg;
    private BodyanalyseBeanInfo info;

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

    public void setInfo(BodyanalyseBeanInfo info) {
        this.info = info;
    }

    public BodyanalyseBeanInfo getInfo() {
        return info;
    }

    public class BodyanalyseBeanInfo {
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
            private String distance;

            public String getGym_id() {
                return gym_id;
            }

            public void setGym_id(String gym_id) {
                this.gym_id = gym_id;
            }

            private String gym_id;
            private String name;
            private String place;
            private String tel;
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

        }
    }
}
