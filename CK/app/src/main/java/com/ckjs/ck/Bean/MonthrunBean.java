package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class MonthrunBean {
    private String status;
    private String msg;

    public MonthrunBeanInfo getInfo() {
        return info;
    }

    public void setInfo(MonthrunBeanInfo info) {
        this.info = info;
    }

    private MonthrunBeanInfo info;

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


    public class MonthrunBeanInfo {
        private String fat;
        private String mileage;
        private String time;
        private String num;

        public void setFat(String fat) {
            this.fat = fat;
        }

        public String getFat() {
            return fat;
        }

        public void setMileage(String mileage) {
            this.mileage = mileage;
        }

        public String getMileage() {
            return mileage;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getNum() {
            return num;
        }

    }
}
