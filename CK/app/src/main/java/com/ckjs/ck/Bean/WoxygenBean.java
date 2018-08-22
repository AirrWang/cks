package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class WoxygenBean {
    private String status;
    private String msg;
    private List<WoxygenBeanInfo> info;

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

    public void setInfo(List<WoxygenBeanInfo> info) {
        this.info = info;
    }

    public List<WoxygenBeanInfo> getInfo() {
        return info;
    }


    public class WoxygenBeanInfo {
        private String date;
        private List<Record> record;

        public String getAvg() {
            return avg;
        }

        public void setAvg(String avg) {
            this.avg = avg;
        }

        private String avg;

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


        public class Record {

            private String createtime;
            private String oxygen;

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setOxygen(String oxygen) {
                this.oxygen = oxygen;
            }

            public String getOxygen() {
                return oxygen;
            }

        }

    }
}
