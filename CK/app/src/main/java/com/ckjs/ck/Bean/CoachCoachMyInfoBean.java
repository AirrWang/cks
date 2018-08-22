package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CoachCoachMyInfoBean {
    private String status;
    private String msg;
    private CoachCoachMyInfoDetailBean info;

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

    public CoachCoachMyInfoDetailBean getInfo() {
        return info;
    }

    public void setInfo(CoachCoachMyInfoDetailBean info) {
        this.info = info;
    }

    public static class CoachCoachMyInfoDetailBean {
        private String id;
        private String name;
        private String gym_id;
        private String picture;
        private String grade;
        private String num;
        private String status;
        private String gymname;
        private String accumulated;
        private String balance;
        private String today;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGym_id() {
            return gym_id;
        }

        public void setGym_id(String gym_id) {
            this.gym_id = gym_id;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGymname() {
            return gymname;
        }

        public void setGymname(String gymname) {
            this.gymname = gymname;
        }

        public String getAccumulated() {
            return accumulated;
        }

        public void setAccumulated(String accumulated) {
            this.accumulated = accumulated;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getToday() {
            return today;
        }

        public void setToday(String today) {
            this.today = today;
        }
    }
}
