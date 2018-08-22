package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CoachInfoBean {
    private String status;
    private String msg;
    private CoachDetailInfoBean info;

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

    public CoachDetailInfoBean getInfo() {
        return info;
    }

    public void setInfo(CoachDetailInfoBean info) {
        this.info = info;
    }

    public static class CoachDetailInfoBean {
        private String id;
        private String name;
        private String datu;
        private String introduce;
        private String tel;
        private float price;
        private String unitprice;

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

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }


        public String getUnitprice() {
            return unitprice;
        }

        public void setUnitprice(String unitprice) {
            this.unitprice = unitprice;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }
    }
}
