package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing on 2017/5/17.
 */

public class MyfamilyBean {
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
    private String msg;

    public MyfamilyBeanInfo getInfo() {
        return info;
    }

    public void setInfo(MyfamilyBeanInfo info) {
        this.info = info;
    }

    public MyfamilyBeanInfo info;

    public class MyfamilyBeanInfo {
        public List<memberList> getMemberlist() {
            return memberlist;
        }

        public void setMemberlist(List<memberList> memberlist) {
            this.memberlist = memberlist;
        }

        List<memberList> memberlist;

        public class memberList {
            private String member_id;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            private String url;

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

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            private String username;
            private String picurl;
            private String createtime;
        }
    }
}
