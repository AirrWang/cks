package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetgymBean {

    private String status;
    private String msg;
    private List<GetgymBeanInfo> info;

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

    public void setInfo(List<GetgymBeanInfo> info) {
        this.info = info;
    }

    public List<GetgymBeanInfo> getInfo() {
        return info;
    }

    public class GetgymBeanInfo {
        private String gym_id;
        private String name;
        private String picture;

        public void setGymId(String gym_id) {
            this.gym_id = gym_id;
        }

        public String getGymId() {
            return gym_id;
        }

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
    }
}
