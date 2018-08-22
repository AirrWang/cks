package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class MedicalFileBean {
    private String status;
    private String msg;
    private  MedicalFileInfoBean info;

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

    public MedicalFileInfoBean getInfo() {
        return info;
    }

    public void setInfo(MedicalFileInfoBean info) {
        this.info = info;
    }

    public static class MedicalFileInfoBean {
        private String mcondition;
        private String notes;
        private String allergy;
        private String bloodtype;
        private String druguse;
        private String contacttel;
        private String contactname;
        private String picurl;
        private String updatetime;
        private String relname;
        private String sex;
        private String age;
        private String height;
        private String weight;

        public String getMcondition() {
            return mcondition;
        }

        public void setMcondition(String mcondition) {
            this.mcondition = mcondition;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getAllergy() {
            return allergy;
        }

        public void setAllergy(String allergy) {
            this.allergy = allergy;
        }

        public String getBloodtype() {
            return bloodtype;
        }

        public void setBloodtype(String bloodtype) {
            this.bloodtype = bloodtype;
        }

        public String getDruguse() {
            return druguse;
        }

        public void setDruguse(String druguse) {
            this.druguse = druguse;
        }

        public String getContacttel() {
            return contacttel;
        }

        public void setContacttel(String contacttel) {
            this.contacttel = contacttel;
        }

        public String getContactname() {
            return contactname;
        }

        public void setContactname(String contactname) {
            this.contactname = contactname;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getRelname() {
            return relname;
        }

        public void setRelname(String relname) {
            this.relname = relname;
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

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }
    }
}
