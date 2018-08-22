package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class WpressureBean {
    private String status;
    private String msg;
    private List<Info> info;
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

    public void setInfo(List<Info> info) {
        this.info = info;
    }
    public List<Info> getInfo() {
        return info;
    }

    public class Info {
        private String date;
        private List<Record> record;
        private int avgdpressure;
        private int avgspressure;
        public void setDate(String date) {
            this.date = date;
        }
        public String getDate() {
            return date;
        }

        public void setRecord(List<Record> record) {
            this.record = record;
        }
        public List<Record> getRecord() {
            return record;
        }

        public void setAvgdpressure(int avgdpressure) {
            this.avgdpressure = avgdpressure;
        }
        public int getAvgdpressure() {
            return avgdpressure;
        }

        public void setAvgspressure(int avgspressure) {
            this.avgspressure = avgspressure;
        }
        public int getAvgspressure() {
            return avgspressure;
        }

        public class Record {
            private String createtime;
            private String dpressure;
            private String spressure;
            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }
            public String getCreatetime() {
                return createtime;
            }

            public void setDpressure(String dpressure) {
                this.dpressure = dpressure;
            }
            public String getDpressure() {
                return dpressure;
            }

            public void setSpressure(String spressure) {
                this.spressure = spressure;
            }
            public String getSpressure() {
                return spressure;
            }

        }
    }
}
