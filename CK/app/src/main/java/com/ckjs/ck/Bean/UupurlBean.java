package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class UupurlBean {

    private String status;
    private String msg;
    private PicBean info;

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

    public PicBean getInfo() {
        return info;
    }

    public void setInfo(PicBean info) {
        this.info = info;
    }

    public class PicBean {
        private String picurl;

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }
    }
}


