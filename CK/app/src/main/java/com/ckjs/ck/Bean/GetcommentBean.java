package com.ckjs.ck.Bean;


import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class GetcommentBean {
    private String status;//|状态码|1：获取成功；0：获取失败；2：token值错误

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String msg;//错误信息|字符串

    public List<GetcommentBeanInfo> getInfo() {
        return info;
    }

    public void setInfo(List<GetcommentBeanInfo> info) {
        this.info = info;
    }

    private List<GetcommentBeanInfo> info;//|返回信息|[];

    public class GetcommentBeanInfo {
        private String username;//|评论用户昵称|字符串
        private String picurl;//|评论用户头像|字符串
        private String content;//|评论内容|字符串

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        private String comment_id;//|评论内容|字符串

        public String getAddname() {
            return addname;
        }

        public void setAddname(String addname) {
            this.addname = addname;
        }

        private String addname;//|评论内容|字符串

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        private String user_id;//|评论用户id|

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        private String time;//|评论时间|yyyy-mm-dd hh:ii:ss
    }
}
