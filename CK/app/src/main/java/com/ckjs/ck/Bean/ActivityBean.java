package com.ckjs.ck.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class ActivityBean {
    private String status;//|状态码|1：更新成功；0：更新失败；2：token值错误
    private String msg;//|错误信息|字符串
    private List<ActivityBeanInfo> info = new ArrayList<>();

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

    public List<ActivityBeanInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ActivityBeanInfo> info) {
        this.info = info;
    }


    public class ActivityBeanInfo {
        private int id;
        private String name;

        public String getActcover() {
            return actcover;
        }

        public void setActcover(String actcover) {
            this.actcover = actcover;
        }

        private String actcover;

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

        private String type;
        private String acurl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }
}
