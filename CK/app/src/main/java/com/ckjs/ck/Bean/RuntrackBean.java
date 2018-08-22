package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class RuntrackBean {
    private String status;
    private String msg;
    private RuntrackBeanInfo info;

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

    public void setInfo(RuntrackBeanInfo info) {
        this.info = info;
    }

    public RuntrackBeanInfo getInfo() {
        return info;
    }

    public class RuntrackBeanInfo {
        private List<Track> track;
        private String mileage;
        private String speed;
        private String time;
        private String createtime;
        private String fat;

        public void setTrack(List<Track> track) {
            this.track = track;
        }

        public List<Track> getTrack() {
            return track;
        }

        public void setMileage(String mileage) {
            this.mileage = mileage;
        }

        public String getMileage() {
            return mileage;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getSpeed() {
            return speed;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setFat(String fat) {
            this.fat = fat;
        }

        public String getFat() {
            return fat;
        }

        public class Track {
            private String lat;

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            private String lon;


        }
    }

}
