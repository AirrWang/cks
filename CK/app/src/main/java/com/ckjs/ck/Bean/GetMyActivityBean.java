package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetMyActivityBean {
    private String status;
    private String msg;
    private List<GetMyActivityInfoBean> info;

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

    public List<GetMyActivityInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<GetMyActivityInfoBean> info) {
        this.info = info;
    }

    public static class GetMyActivityInfoBean {
        private String activity_id;
        private String name;
        private String actcover;
        private String num;
        private String bmnum;
        private String isstart;
        private String actime;
        private String type;
        private String acurl;

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture() {
            return actcover;
        }

        public void setPicture(String picture) {
            this.actcover = picture;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getBmnum() {
            return bmnum;
        }

        public void setBmnum(String bmnum) {
            this.bmnum = bmnum;
        }

        public String getIsstart() {
            return isstart;
        }

        public void setIsstart(String isstart) {
            this.isstart = isstart;
        }

        public String getActime() {
            return actime;
        }

        public void setActime(String actime) {
            this.actime = actime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAcurl() {
            return acurl;
        }

        public void setAcurl(String acurl) {
            this.acurl = acurl;
        }
    }
}
