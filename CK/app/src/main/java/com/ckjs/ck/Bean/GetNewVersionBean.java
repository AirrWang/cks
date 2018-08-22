package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetNewVersionBean {
    private String status;
    private String msg;
    private GetNewVersionInfoBean info;

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

    public GetNewVersionInfoBean getInfo() {
        return info;
    }

    public void setInfo(GetNewVersionInfoBean info) {
        this.info = info;
    }


    public static class GetNewVersionInfoBean {
        private String isupdate;
        private String vername;
        private String vercode;
        private String verdec;
        private String downurl;

        public String getIsupdate() {
            return isupdate;
        }

        public void setIsupdate(String isupdate) {
            this.isupdate = isupdate;
        }

        public String getVername() {
            return vername;
        }

        public void setVername(String vername) {
            this.vername = vername;
        }

        public String getVercode() {
            return vercode;
        }

        public void setVercode(String vercode) {
            this.vercode = vercode;
        }

        public String getVerdec() {
            return verdec;
        }

        public void setVerdec(String verdec) {
            this.verdec = verdec;
        }

        public String getDownurl() {
            return downurl;
        }

        public void setDownurl(String downurl) {
            this.downurl = downurl;
        }
    }
}
