package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class IntegralBean {
    private String status;
    private String msg;
    private IntegralInfoBean info;

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

    public IntegralInfoBean getInfo() {
        return info;
    }

    public void setInfo(IntegralInfoBean info) {
        this.info = info;
    }

    public static class IntegralInfoBean {
        private String intervalue;
        private List<IntegralInfoDetailBean> interule;

        public String getIntervalue() {
            return intervalue;
        }

        public void setIntervalue(String intervalue) {
            this.intervalue = intervalue;
        }

        public List<IntegralInfoDetailBean> getInterule() {
            return interule;
        }

        public void setInterule(List<IntegralInfoDetailBean> interule) {
            this.interule = interule;
        }

        public static class IntegralInfoDetailBean {
            private String name;
            private String integral;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIntegral() {
                return integral;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }
        }
    }
}
