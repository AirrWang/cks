package com.ckjs.ck.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class CkCircleMineBean {




    private String status;
    private String msg;

    private List<CkCircleMineTwoBean> info = new ArrayList<>();

    public CkCircleMineBean() {

    }
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

    public List<CkCircleMineTwoBean> getInfo() {
        return info;
    }

    public void setInfo(List<CkCircleMineTwoBean> info) {
        this.info = info;
    }


    public class CkCircleMineTwoBean {
        private CkCircleMineIvBean iv;
        private String photo;
        private String username;
        private String information;

        public CkCircleMineTwoBean(){

        }
        public CkCircleMineTwoBean(String photo, String username, String information, CkCircleMineIvBean iv) {
            this.photo = photo;
            this.username = username;
            this.information = information;
            this.iv = iv;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }

        public CkCircleMineIvBean getIv() {
            return iv;
        }

        public void setIv(CkCircleMineIvBean iv) {
            this.iv = iv;
        }


        public class CkCircleMineIvBean {
            public CkCircleMineIvBean() {

            }

            private List<String> ivList = new ArrayList<>();

            public List<String> getIvList() {
                return ivList;
            }

            public void setIvList(List<String> ivList) {
                this.ivList = ivList;
            }
        }
    }


}
