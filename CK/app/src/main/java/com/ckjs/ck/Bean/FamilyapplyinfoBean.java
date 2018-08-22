package com.ckjs.ck.Bean;

/**
 * Created by NiPing on 2017/5/18.
 */

public class FamilyapplyinfoBean {
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

    public FamilyapplyinfoBeaninfo getInfo() {
        return info;
    }

    public void setInfo(FamilyapplyinfoBeaninfo info) {
        this.info = info;
    }

    private String status;
    private String msg;
    private FamilyapplyinfoBeaninfo info;

    /**
     * member_id	成员id
     * username	成员昵称	字符串
     * picurl	成员头像地址	字符串
     * sex	性别	1：男；2：女；
     * age	年龄
     * bind_id	邀请信息id
     */
    public class FamilyapplyinfoBeaninfo {
        private String member_id;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getBind_id() {
            return bind_id;
        }

        public void setBind_id(String bind_id) {
            this.bind_id = bind_id;
        }

        private String username;
        private String picurl;
        private String sex;
        private String age;
        private String bind_id;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String status;//0：未处理；1：接受；

    }
}
