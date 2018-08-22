package com.ckjs.ck.Bean;


import java.util.List;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class MyCoachOrderBean {
    private String status;
    private String msg;
    private List<MyCoachOrderInfoBean> info;

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

    public List<MyCoachOrderInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<MyCoachOrderInfoBean> info) {
        this.info = info;
    }

    public static class MyCoachOrderInfoBean {
        private String id;
        private String name;
        private String picture;
        private String sex;
        private String type;
        private String status;
        private String amount;

        public String getObjection() {
            return objection;
        }

        public void setObjection(String objection) {
            this.objection = objection;
        }

        private String objection;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        private String num;

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

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
