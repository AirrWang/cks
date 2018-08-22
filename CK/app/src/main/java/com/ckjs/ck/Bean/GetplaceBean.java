package com.ckjs.ck.Bean;

/**
 * Created by NiPing on 2017/4/27.
 */

public class GetplaceBean {
    String msg;
    String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GetplaceBeanInfor getInfo() {
        return info;
    }

    public void setInfo(GetplaceBeanInfor info) {
        this.info = info;
    }

    GetplaceBeanInfor  info;

    public class GetplaceBeanInfor {
        String lat;
        String lon;

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

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        String place;
    }
}
