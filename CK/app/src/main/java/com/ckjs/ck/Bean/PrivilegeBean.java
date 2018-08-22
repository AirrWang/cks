package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class PrivilegeBean {
    private String status;
    private String msg;
    private List<PrivilegeBeanInfo> info;

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

    public void setInfo(List<PrivilegeBeanInfo> info) {
        this.info = info;
    }

    public List<PrivilegeBeanInfo> getInfo() {
        return info;
    }

    public class PrivilegeBeanInfo {
        private String id;
        private String name;
        private String originalprice;
        private String price;
        private String holidays;
        private String expiry;

        public String getGym_id() {
            return gym_id;
        }

        public void setGym_id(String gym_id) {
            this.gym_id = gym_id;
        }

        private String gym_id;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setOriginalprice(String originalprice) {
            this.originalprice = originalprice;
        }

        public String getOriginalprice() {
            return originalprice;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPrice() {
            return price;
        }

        public void setHolidays(String holidays) {
            this.holidays = holidays;
        }

        public String getHolidays() {
            return holidays;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getExpiry() {
            return expiry;
        }


    }
}
