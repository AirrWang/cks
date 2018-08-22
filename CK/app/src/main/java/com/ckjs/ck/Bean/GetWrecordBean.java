package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetWrecordBean {
    private String status;
    private String msg;
    private List<GetWrecordInfoBean> info;

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

    public List<GetWrecordInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<GetWrecordInfoBean> info) {
        this.info = info;
    }

    public static class GetWrecordInfoBean {
        private String value;
        private String creattime;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCreattime() {
            return creattime;
        }

        public void setCreattime(String creattime) {
            this.creattime = creattime;
        }
    }
}
