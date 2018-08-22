package com.ckjs.ck.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetMyfocusBean {
    private String status;
    private String msg;
    private List<GetMyfocusInfoBean> info=new ArrayList<>();


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

    public List<GetMyfocusInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<GetMyfocusInfoBean> info) {
        this.info = info;
    }

    public static class GetMyfocusInfoBean {
        private String focus_id;
        private String focus_name;

        private String picurl;

        public String getFocus_id() {
            return focus_id;
        }

        public void setFocus_id(String focus_id) {
            this.focus_id = focus_id;
        }

        public String getFocus_name() {
            return focus_name;
        }

        public void setFocus_name(String focus_name) {
            this.focus_name = focus_name;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }
    }
}
