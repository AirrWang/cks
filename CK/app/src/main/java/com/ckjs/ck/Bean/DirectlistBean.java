package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class DirectlistBean {
    private String status;
    private String msg;
    private DirectlistBeanInfo info;

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

    public void setInfo(DirectlistBeanInfo info) {
        this.info = info;
    }

    public DirectlistBeanInfo getInfo() {
        return info;
    }

    public class DirectlistBeanInfo {
        private String classname;
        private List<Direct> direct;

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getClassname() {
            return classname;
        }

        public void setDirect(List<Direct> direct) {
            this.direct = direct;
        }

        public List<Direct> getDirect() {
            return direct;
        }

        public class Direct {
            private String id;
            private String name;
            private String picture;
            private String type;
            private String directurl;
            private String introduce;
            private String fat;
            private String time;
            private String difficult;
            public void setId(String id) {
                this.id = id;
            }
            public String getId() {
                return id;
            }

            public void setName(String name) {
                this.name = name;
            }
            public String getName() {
                return name;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }
            public String getPicture() {
                return picture;
            }

            public void setType(String type) {
                this.type = type;
            }
            public String getType() {
                return type;
            }

            public void setDirecturl(String directurl) {
                this.directurl = directurl;
            }
            public String getDirecturl() {
                return directurl;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }
            public String getIntroduce() {
                return introduce;
            }

            public void setFat(String fat) {
                this.fat = fat;
            }
            public String getFat() {
                return fat;
            }

            public void setTime(String time) {
                this.time = time;
            }
            public String getTime() {
                return time;
            }

            public void setDifficult(String difficult) {
                this.difficult = difficult;
            }
            public String getDifficult() {
                return difficult;
            }

        }
    }
}
