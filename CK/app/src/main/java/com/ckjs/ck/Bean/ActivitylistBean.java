package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ActivitylistBean {
    private String status;
    private String msg;
    private List<ActivitylistBeanInfo> info;
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

    public void setInfo(List<ActivitylistBeanInfo> info) {
        this.info = info;
    }
    public List<ActivitylistBeanInfo> getInfo() {
        return info;
    }

    public class ActivitylistBeanInfo {

        private String id;
        private String name;

        private String num;
        private String bmnum;
        private String isstart;
        private String actime;
        private String type;
        private String acurl;

        public String getActcover() {
            return actcover;
        }

        public void setActcover(String actcover) {
            this.actcover = actcover;
        }

        private String actcover;
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


        public void setNum(String num) {
            this.num = num;
        }
        public String getNum() {
            return num;
        }

        public void setBmnum(String bmnum) {
            this.bmnum = bmnum;
        }
        public String getBmnum() {
            return bmnum;
        }

        public void setIsstart(String isstart) {
            this.isstart = isstart;
        }
        public String getIsstart() {
            return isstart;
        }

        public void setTime(String time) {
            this.actime = time;
        }
        public String getTime() {
            return actime;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

        public void setAcurl(String acurl) {
            this.acurl = acurl;
        }
        public String getAcurl() {
            return acurl;
        }
    }
}
