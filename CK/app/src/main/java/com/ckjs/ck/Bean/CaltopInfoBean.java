package com.ckjs.ck.Bean;

import java.util.List;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class CaltopInfoBean {
    private String status;
    private String msg;
    private CaltopInfoDetailBean info;

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

    public CaltopInfoDetailBean getInfo() {
        return info;
    }

    public void setInfo(CaltopInfoDetailBean info) {
        this.info = info;
    }

    public static class CaltopInfoDetailBean {
        private String user_id;
        private String username;
        private String picurl;
        private String calories;
        private String steps;
        private String calavg;
        private String updatetime;
        private String ranking;
        private List<CaltopDetailBean> toplist;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getCalories() {
            return calories;
        }

        public void setCalories(String calories) {
            this.calories = calories;
        }

        public String getSteps() {
            return steps;
        }

        public void setSteps(String steps) {
            this.steps = steps;
        }

        public String getCalavg() {
            return calavg;
        }

        public void setCalavg(String calavg) {
            this.calavg = calavg;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public List<CaltopDetailBean> getToplist() {
            return toplist;
        }

        public void setToplist(List<CaltopDetailBean> toplist) {
            this.toplist = toplist;
        }

        public String getRanking() {
            return ranking;
        }

        public void setRanking(String ranking) {
            this.ranking = ranking;
        }

        public static class CaltopDetailBean {
            private String user_id;
            private String username;
            private String picurl;
            private String calories;
            private String steps;

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
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

            public String getCalories() {
                return calories;
            }

            public void setCalories(String calories) {
                this.calories = calories;
            }

            public String getSteps() {
                return steps;
            }

            public void setSteps(String steps) {
                this.steps = steps;
            }
        }
    }
}
