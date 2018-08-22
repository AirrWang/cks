package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class UppressureBean {
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

    private String status;
    private String msg;


    public void setInfo(UppressureBeanInfo info) {
        this.info = info;
    }

    public UppressureBeanInfo getInfo() {
        return info;
    }

    private UppressureBeanInfo info;

    public class UppressureBeanInfo {
        public String getPressure_grade() {
            return pressure_grade;
        }

        public void setPressure_grade(String pressure_grade) {
            this.pressure_grade = pressure_grade;
        }

        String pressure_grade;

        public String getPressure_vel() {
            return pressure_vel;
        }

        public void setPressure_vel(String pressure_vel) {
            this.pressure_vel = pressure_vel;
        }

        String pressure_vel;


    }
}

