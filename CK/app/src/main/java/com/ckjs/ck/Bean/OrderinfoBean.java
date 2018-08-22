package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class OrderinfoBean {
    private String status;
    private String msg;
    private OrderinfoBeanInfo info;

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

    public void setInfo(OrderinfoBeanInfo info) {
        this.info = info;
    }

    public OrderinfoBeanInfo getInfo() {
        return info;
    }

    public class OrderinfoBeanInfo {
        private String id;
        private String amount;
        private String num;
        private String type;
        private String status;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        private String user_id;
        private String adress;
        private String tel;
        private String relname;
        private String sex;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
        public String getAmount() {
            return amount;
        }

        public void setNum(String num) {
            this.num = num;
        }
        public String getNum() {
            return num;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }


        public void setAdress(String adress) {
            this.adress = adress;
        }
        public String getAdress() {
            return adress;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
        public String getTel() {
            return tel;
        }

        public void setRelname(String relname) {
            this.relname = relname;
        }
        public String getRelname() {
            return relname;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
        public String getSex() {
            return sex;
        }

    }
}

