package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class UpoxygenBean {
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

    public UpoxygenBeanInfo getInfo() {
        return info;
    }

    public void setInfo(UpoxygenBeanInfo info) {
        this.info = info;
    }

    /**
     * content、oxygen_grade
     */
    private UpoxygenBeanInfo info;

    public class UpoxygenBeanInfo {
        String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getOxygen_grade() {
            return oxygen_grade;
        }

        public void setOxygen_grade(String oxygen_grade) {
            this.oxygen_grade = oxygen_grade;
        }

        String oxygen_grade;

        public String getOxygen_vel() {
            return oxygen_vel;
        }

        public void setOxygen_vel(String oxygen_vel) {
            this.oxygen_vel = oxygen_vel;
        }

        String oxygen_vel;

    }
}

