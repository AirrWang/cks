package com.ckjs.ck.Bean;

/**
 * Created by NiPing on 2017/5/17.
 */

public class FamilysearchBean {
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

    public FamilysearchBeanInfo getInfo() {
        return info;
    }

    public void setInfo(FamilysearchBeanInfo info) {
        this.info = info;
    }

    private String status;
    private String msg;
    private FamilysearchBeanInfo info;

    public class FamilysearchBeanInfo {
        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        private String member_id;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type;
    }
}
