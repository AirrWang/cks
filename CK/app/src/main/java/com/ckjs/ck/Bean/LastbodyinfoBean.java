package com.ckjs.ck.Bean;


/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class LastbodyinfoBean {
    private String status;
    private String msg;
    private LastbodyinfoBeanInfo info;

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

    public void setInfo(LastbodyinfoBeanInfo info) {
        this.info = info;
    }

    public LastbodyinfoBeanInfo getInfo() {
        return info;
    }

    public class LastbodyinfoBeanInfo {

        private int hours;

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        private int daypoor;

        public void setDaypoor(int daypoor) {
            this.daypoor = daypoor;
        }

        public int getDaypoor() {
            return daypoor;
        }

        //        private String day;

        //        public void setDay(String day) {
        //            this.day = day;
        //        }

        //        public String getDay() {
        //            return day;
        //        }

        //        private int num;

        //        public void setNum(int num) {
        //            this.num = num;
        //        }

        //        public int getNum() {
        //            return num;
        //        }


    }
}
