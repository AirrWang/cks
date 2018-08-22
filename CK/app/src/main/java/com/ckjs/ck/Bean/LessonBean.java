package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class LessonBean {
    private String status;
    private String msg;
    private LessonBeanInfo info;
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

    public void setInfo(LessonBeanInfo info) {
        this.info = info;
    }
    public LessonBeanInfo getInfo() {
        return info;
    }
    public class Lesson {

        private String type;
        private String sort;
        private String cstartime;
        private String cstoptime;
        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
        public String getSort() {
            return sort;
        }

        public void setCstartime(String cstartime) {
            this.cstartime = cstartime;
        }
        public String getCstartime() {
            return cstartime;
        }

        public void setCstoptime(String cstoptime) {
            this.cstoptime = cstoptime;
        }
        public String getCstoptime() {
            return cstoptime;
        }

    }
    public class LessonBeanInfo {
        private String type;
        private String num;
        private String completed;
        private String tel;

        public String getHandler() {
            return handler;
        }

        public void setHandler(String handler) {
            this.handler = handler;
        }

        private String handler;
        private String status;
        private String id;
        private List<Lesson> lesson;
        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

        public void setNum(String num) {
            this.num = num;
        }
        public String getNum() {
            return num;
        }

        public void setCompleted(String completed) {
            this.completed = completed;
        }
        public String getCompleted() {
            return completed;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
        public String getTel() {
            return tel;
        }



        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }

        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setLesson(List<Lesson> lesson) {
            this.lesson = lesson;
        }
        public List<Lesson> getLesson() {
            return lesson;
        }

    }
}
