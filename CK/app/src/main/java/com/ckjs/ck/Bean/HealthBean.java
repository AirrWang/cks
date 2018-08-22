package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class HealthBean {
    private String status;
    private String msg;
    private Info info;

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

    public void setInfo(Info info) {
        this.info = info;
    }

    public Info getInfo() {
        return info;
    }

    public class Info {
        private String rate;
        private String rate_grade;
        private String oxygen;
        //        rate_vel，oxygen_vel，pressure_vel，sleep_vel
        private String rate_vel;
        private String oxygen_vel;
        private String fat;

        public String getFat() {
            return fat;
        }

        public void setFat(String fat) {
            this.fat = fat;
        }


        public float getOther() {
            return other;
        }

        public void setOther(float other) {
            this.other = other;
        }

        private float other;
        //fat	步行卡路里消耗
        //other	其他卡路里消耗

        public String getRate_vel() {
            return rate_vel;
        }

        public void setRate_vel(String rate_vel) {
            this.rate_vel = rate_vel;
        }

        public String getOxygen_vel() {
            return oxygen_vel;
        }

        public void setOxygen_vel(String oxygen_vel) {
            this.oxygen_vel = oxygen_vel;
        }

        public String getPressure_vel() {
            return pressure_vel;
        }

        public void setPressure_vel(String pressure_vel) {
            this.pressure_vel = pressure_vel;
        }

        public String getSleep_vel() {
            return sleep_vel;
        }

        public void setSleep_vel(String sleep_vel) {
            this.sleep_vel = sleep_vel;
        }

        private String pressure_vel;
        private String sleep_vel;

        public String getRate_grade() {
            return rate_grade;
        }

        public void setRate_grade(String rate_grade) {
            this.rate_grade = rate_grade;
        }

        public String getOxygen() {
            return oxygen;
        }

        public void setOxygen(String oxygen) {
            this.oxygen = oxygen;
        }

        public String getOxygen_grade() {
            return oxygen_grade;
        }

        public void setOxygen_grade(String oxygen_grade) {
            this.oxygen_grade = oxygen_grade;
        }

        public String getPressure_grade() {
            return pressure_grade;
        }

        public void setPressure_grade(String pressure_grade) {
            this.pressure_grade = pressure_grade;
        }

        public String getSleep_grade() {
            return sleep_grade;
        }

        public void setSleep_grade(String sleep_grade) {
            this.sleep_grade = sleep_grade;
        }

        private String oxygen_grade;
        private String pressure;
        private String pressure_grade;
        private String sleep;
        private String sleep_grade;

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getRate() {
            return rate;
        }


        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getPressure() {
            return pressure;
        }


        public void setSleep(String sleep) {
            this.sleep = sleep;
        }

        public String getSleep() {
            return sleep;
        }


    }
}
