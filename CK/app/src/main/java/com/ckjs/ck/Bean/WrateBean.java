package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class WrateBean {
    private String status;
    private String msg;
    private List<WrateBeanInfo> info;

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

    public void setInfo(List<WrateBeanInfo> info) {
        this.info = info;
    }

    public List<WrateBeanInfo> getInfo() {
        return info;
    }

    public class WrateBeanInfo {

        private String date;
        //        private String avg;
        private String time;
        //        private String max;
        //        private String min;
        private List<WrateBeanDetailInfo> record;

        public void setTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        //        public void setMax(String max) {
        //            this.max = max;
        //        }
        //
        //        public String getMax() {
        //            return max;
        //        }


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        //        public String getAvg() {
        //            return avg;
        //        }
        //
        //        public void setAvg(String avg) {
        //            this.avg = avg;
        //        }

        //        public String getMin() {
        //            return min;
        //        }
        //
        //        public void setMin(String min) {
        //            this.min = min;
        //        }

        public List<WrateBeanDetailInfo> getRecord() {
            return record;
        }

        public void setRecord(List<WrateBeanDetailInfo> record) {
            this.record = record;
        }

        public class WrateBeanDetailInfo {
            private String createtime;
            private String rate;
            private String maxrate;
            private String minrate;

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }

            public String getMaxrate() {
                return maxrate;
            }

            public void setMaxrate(String maxrate) {
                this.maxrate = maxrate;
            }

            public String getMinrate() {
                return minrate;
            }

            public void setMinrate(String minrate) {
                this.minrate = minrate;
            }
        }
    }
}
