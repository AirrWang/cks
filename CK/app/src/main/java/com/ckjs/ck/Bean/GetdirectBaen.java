package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class GetdirectBaen {
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<GetdirectBeanInfo> getInfo() {
        return info;
    }

    public void setInfo(List<GetdirectBeanInfo> info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String msg;
    private List<GetdirectBeanInfo> info;

    public class GetdirectBeanInfo {


        public List<GetdirectBeanInfoDirect> getDirect() {
            return direct;
        }

        public void setDirect(List<GetdirectBeanInfoDirect> direct) {
            this.direct = direct;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type;

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        private String classname;
        private List<GetdirectBeanInfoDirect> direct;

        public class GetdirectBeanInfoDirect {
            public GetdirectBeanInfoDirect() {
                super();
            }

            /**
             * id|子类指导id|字符串
             * name|指导名称|字符串
             * picture|封面url|字符串
             * ratio|高宽比|字符串
             * class|分类id|字符串
             * type|类型id|1：原生打开；2：H5打开
             */
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public String getRatio() {
                return ratio;
            }

            public void setRatio(String ratio) {
                this.ratio = ratio;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            private String name;
            private String picture;
            private String ratio;
            private String type;

            public String getClass_id() {
                return class_id;
            }

            public void setClass_id(String class_id) {
                this.class_id = class_id;
            }

            private String class_id;

            public String getDirecturl() {
                return directurl;
            }

            public void setDirecturl(String directurl) {
                this.directurl = directurl;
            }

            private String directurl;
        }
    }
}

