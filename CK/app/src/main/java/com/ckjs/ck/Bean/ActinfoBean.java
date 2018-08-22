package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ActinfoBean {
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

    public ActinfoInfoBean getInfo() {
        return info;
    }

    public void setInfo(ActinfoInfoBean info) {
        this.info = info;
    }

    /**
     * status|状态码|1：获取成功；0：获取失败；2：token值错误
     * msg|错误信息|字符串
     * info|返回信息|{};失败：NULL
     */
    private String status;
    private String msg;
    private ActinfoInfoBean info;

    public class ActinfoInfoBean {
        /**
         * id|活动id|int
         * picture|图片地址|字符串
         * name|活动名称|字符串
         * text|活动简介|字符串
         * startime|开始时间|
         * stoptime|结束时间|
         * num|最多人数|int
         * time|时长|int
         * fat|消耗热量|int
         * sportnum|运动组数|int
         * gym_id|健身房id|int
         * host|举办单位|字符串
         * address|活动地址|字符串
         * bmnum|已报名人数|int
         * bmstatus|是否报名|1：已报名；0：未报名
         **/
        private int id;

        public List<String> getPicture() {
            return picture;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }

        private List<String> picture;
        private String name;
        private String text;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }


        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getFat() {
            return fat;
        }

        public void setFat(int fat) {
            this.fat = fat;
        }

        public int getSportnum() {
            return sportnum;
        }

        public void setSportnum(int sportnum) {
            this.sportnum = sportnum;
        }

        public int getGym_id() {
            return gym_id;
        }

        public void setGym_id(int gym_id) {
            this.gym_id = gym_id;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getBmnum() {
            return bmnum;
        }

        public void setBmnum(int bmnum) {
            this.bmnum = bmnum;
        }

        public int getBmstatus() {
            return bmstatus;
        }

        public void setBmstatus(int bmstatus) {
            this.bmstatus = bmstatus;
        }

        public String getStartime() {
            return startime;
        }

        public void setStartime(String startime) {
            this.startime = startime;
        }

        public String getStoptime() {
            return stoptime;
        }

        public void setStoptime(String stoptime) {
            this.stoptime = stoptime;
        }

        private String startime;
        private String stoptime;
        private int num;
        private int time;
        private int fat;
        private int sportnum;
        private int gym_id;
        private String host;
        private String address;
        private int bmnum;
        private int bmstatus;


    }
}
