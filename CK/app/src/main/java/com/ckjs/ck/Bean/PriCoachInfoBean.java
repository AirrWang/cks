package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class PriCoachInfoBean {
    private String status;
    private String msg;
    private PriCoachDetailInfoBean info;

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

    public PriCoachDetailInfoBean getInfo() {
        return info;
    }

    public void setInfo(PriCoachDetailInfoBean info) {
        this.info = info;
    }

    public static class PriCoachDetailInfoBean {
        private String id;
        private String grade;
        private String datu;
        private String introduce;
        private Float distance;
        private Float personal;
        private String intro;
        private String height;
        private String weight;
        private String age;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getDatu() {
            return datu;
        }

        public void setDatu(String datu) {
            this.datu = datu;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }



        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public Float getDistance() {
            return distance;
        }

        public void setDistance(Float distance) {
            this.distance = distance;
        }

        public Float getPersonal() {
            return personal;
        }

        public void setPersonal(Float personal) {
            this.personal = personal;
        }
    }
}
