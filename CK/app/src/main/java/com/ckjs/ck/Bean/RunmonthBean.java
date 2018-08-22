package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class RunmonthBean {
    private String status;
    private String msg;
    private List<RunmonthInfoBean> info;

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

    public List<RunmonthInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<RunmonthInfoBean> info) {
        this.info = info;
    }

    public class RunmonthInfoBean {
        private String num;
        private String month;
        private String monthv;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getMonthv() {
            return monthv;
        }

        public void setMonthv(String monthv) {
            this.monthv = monthv;
        }
    }
}
