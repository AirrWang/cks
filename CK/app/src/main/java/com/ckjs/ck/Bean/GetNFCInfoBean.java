package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class GetNFCInfoBean {
    private String status;
    private String msg;
    private GetNFCDetailInfoBean info;

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

    public GetNFCDetailInfoBean getInfo() {
        return info;
    }

    public void setInfo(GetNFCDetailInfoBean info) {
        this.info = info;
    }

    public static class GetNFCDetailInfoBean {
        private String card_no;
        private String relname;
        private String tel;

        public String getCard_no() {
            return card_no;
        }

        public void setCard_no(String card_no) {
            this.card_no = card_no;
        }

        public String getRelname() {
            return relname;
        }

        public void setRelname(String relname) {
            this.relname = relname;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }
}
