package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class LetterstatusBean {
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

    private String status;

    public void setInfo(LetterstatusBeaninfo info) {
        this.info = info;
    }

    public LetterstatusBeaninfo getInfo() {
        return info;
    }

    private LetterstatusBeaninfo info;
    private String msg;


    public class LetterstatusBeaninfo {
        public String getLetter() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String letter;
        private String message;

    }
}
