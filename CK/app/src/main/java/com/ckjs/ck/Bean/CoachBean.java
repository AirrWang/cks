package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CoachBean {
    private String status;
    private String msg;
    private List<CoachBeanInfo> info;

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

    public void setInfo(List<CoachBeanInfo> info) {
        this.info = info;
    }

    public List<CoachBeanInfo> getInfo() {
        return info;
    }

    public class CoachBeanInfo {
        private String id;
        private String name;


        private String gym_id;

        public String getGym_id() {
            return gym_id;
        }

        public void setGym_id(String gym_id) {
            this.gym_id = gym_id;
        }

        private String grade;
        private String num;
        private String sex;
        private String picture;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String status;

        public String getGymname() {
            return gymname;
        }

        public void setGymname(String gymname) {
            this.gymname = gymname;
        }

        private String gymname;

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


        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getGrade() {
            return grade;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getNum() {
            return num;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSex() {
            return sex;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getPicture() {
            return picture;
        }

    }
}
