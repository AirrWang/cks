package com.ckjs.ck.Bean;

/**
 * Created by NiPing on 2017/4/27.
 */

public class RefundinfoBaom {
    private String status;
    private String msg;
    private RefundinfoBaomInfo info;
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

    public void setInfo(RefundinfoBaomInfo info) {
        this.info = info;
    }
    public RefundinfoBaomInfo getInfo() {
        return info;
    }

    public class RefundinfoBaomInfo {
        private String relname;
        private String tel;
        private String amount;
        private String pay;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        private String user_id;


        public void setRelname(String relname) {
            this.relname = relname;
        }
        public String getRelname() {
            return relname;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
        public String getTel() {
            return tel;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
        public String getAmount() {
            return amount;
        }

        public void setPay(String pay) {
            this.pay = pay;
        }
        public String getPay() {
            return pay;
        }
    }
}
