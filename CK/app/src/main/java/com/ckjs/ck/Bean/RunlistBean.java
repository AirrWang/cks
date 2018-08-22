package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class RunlistBean {
    private String status;
    private String msg;
    private List<RunlistInfoBean> info;

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

    public List<RunlistInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<RunlistInfoBean> info) {
        this.info = info;
    }


    public static class RunlistInfoBean {
        private String run_id;
        private String mileage;
        private String speed;
        private String createtime;
        private String fat;

        public String getRun_id() {
            return run_id;
        }

        public void setRun_id(String run_id) {
            this.run_id = run_id;
        }

        public String getMileage() {
            return mileage;
        }

        public void setMileage(String mileage) {
            this.mileage = mileage;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getFat() {
            return fat;
        }

        public void setFat(String fat) {
            this.fat = fat;
        }
    }
}
