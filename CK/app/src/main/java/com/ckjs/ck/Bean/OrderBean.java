package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class OrderBean {
    private String status;//|状态码|1：成功；0：失败；2：token错误

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

    public List<OrderBeanInfo> getInfo() {
        return info;
    }

    public void setInfo(List<OrderBeanInfo> info) {
        this.info = info;
    }

    private String msg;//|错误信息|字符串
    private List<OrderBeanInfo> info;//|返回信息|成功[]失败为NULL

    public class OrderBeanInfo {
        private String id;//|订单id|

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getHandler() {
            return handler;
        }

        public void setHandler(String handler) {
            this.handler = handler;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRelname() {
            return relname;
        }

        public void setRelname(String relname) {
            this.relname = relname;
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

        private String type;//|指导类型|distance：视频指导；personal：上门指导
        private String adress;//|预约地址|
        private String amount;//|金额|
        private String handler;//|预约时间|
        private String createtime;//|下单时间|
        private String status;//|订单状态|1：教练未确认；2：教练已确认；3：用户已支付；4：课程已完成；5：已评论完成
        private String relname;//|客户姓名|
        private String picurl;//|客户头像|
        private String sex;//|客户性别|

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        private String num;
    }
}

