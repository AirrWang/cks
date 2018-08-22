package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetstepsBean {
    private String status;
    private String msg;
    private GetstepsBeanInfo info;

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

    public void setInfo(GetstepsBeanInfo info) {
        this.info = info;
    }

    public GetstepsBeanInfo getInfo() {
        return info;
    }

    public class GetstepsBeanInfo {
        private String steps;

        public void setSteps(String steps) {
            this.steps = steps;
        }

        public String getSteps() {
            return steps;
        }

    }
}
