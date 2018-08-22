package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetBodyInfoBean {
    private String status;
    private String msg;
    private List<GetBodyInfoDetailBean> info;

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

    public List<GetBodyInfoDetailBean> getInfo() {
        return info;
    }

    public void setInfo(List<GetBodyInfoDetailBean> info) {
        this.info = info;
    }

    public static class GetBodyInfoDetailBean {
        private String name;
        private String value;
        private String level;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }


        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }
}
