package com.ckjs.ck.Bean;

import java.util.List;
/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class PrivilegeInfoBean {
    private String  status;
    private String msg;
    private PrivilegeDetailInfoBean info;

    public PrivilegeInfoBean() {
    }

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

    public PrivilegeDetailInfoBean getInfo() {
        return info;
    }

    public void setInfo(PrivilegeDetailInfoBean info) {
        this.info = info;
    }


    public static class PrivilegeDetailInfoBean {
        private String id;
        private String name;
        private float price;
        private String manual;

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



        public String getManual() {
            return manual;
        }

        public void setManual(String manual) {
            this.manual = manual;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }
    }
}
