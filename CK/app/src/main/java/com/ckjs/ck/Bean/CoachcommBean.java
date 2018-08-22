package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CoachcommBean {
    private String status;
    private String msg;
    private CoachcommBeanInfo info;

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

    public void setInfo(CoachcommBeanInfo info) {
        this.info = info;
    }

    public CoachcommBeanInfo getInfo() {
        return info;
    }

    public class CoachcommBeanInfo {
        private String name;
        private String picture;
        private String sex;
        private String amount;
        private List<Goodlabel> goodlabel;
        private List<Badlabel> badlabel;
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
        public String getPicture() {
            return picture;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
        public String getSex() {
            return sex;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
        public String getAmount() {
            return amount;
        }

        public void setGoodlabel(List<Goodlabel> goodlabel) {
            this.goodlabel = goodlabel;
        }
        public List<Goodlabel> getGoodlabel() {
            return goodlabel;
        }

        public void setBadlabel(List<Badlabel> badlabel) {
            this.badlabel = badlabel;
        }
        public List<Badlabel> getBadlabel() {
            return badlabel;
        }

    }
    public class Goodlabel {

        private String label;
        public void setLabel(String label) {
            this.label = label;
        }
        public String getLabel() {
            return label;
        }

    }
    public class Badlabel {

        private String label;
        public void setLabel(String label) {
            this.label = label;
        }
        public String getLabel() {
            return label;
        }

    }
}
