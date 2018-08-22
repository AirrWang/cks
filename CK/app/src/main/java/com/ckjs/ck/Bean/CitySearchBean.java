package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class CitySearchBean {
    private String status;
    private String msg;
    private List<CitySearchInfoBean> info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<CitySearchInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<CitySearchInfoBean> info) {
        this.info = info;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class CitySearchInfoBean {
        private String gym_id;
        private String name;
        private String place;
        private String num;
        private String grade;

        public String getGym_id() {
            return gym_id;
        }

        public void setGym_id(String gym_id) {
            this.gym_id = gym_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }
    }
}
