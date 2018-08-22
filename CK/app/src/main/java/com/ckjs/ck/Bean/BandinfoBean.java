package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class BandinfoBean {
    private String status;
    private String msg;
    private BandinfoBeanInfo info;

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

    public void setInfo(BandinfoBeanInfo info) {
        this.info = info;
    }

    public BandinfoBeanInfo getInfo() {
        return info;
    }

    public class BandinfoBeanInfo {
        private String type;
        private String amount;
        private String firmware;
        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
        public String getAmount() {
            return amount;
        }

        public void setFirmware(String firmware) {
            this.firmware = firmware;
        }
        public String getFirmware() {
            return firmware;
        }

    }
}
