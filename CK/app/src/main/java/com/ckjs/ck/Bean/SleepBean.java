package com.ckjs.ck.Bean;

/**
 * Created by NiPing and AirrWang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class SleepBean {
    /**
     * "day": "3",
     * "hours": "22",
     * "minute": "37",
     * "awaketime": "01:37",
     * "num": "50",
     */
    private String day;

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    private String hours;
    private String minute;
    private String awaketime;
    private String num;
    private String slog;

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }



    public void setAwaketime(String awaketime) {
        this.awaketime = awaketime;
    }

    public String getAwaketime() {
        return awaketime;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public void setSlog(String slog) {
        this.slog = slog;
    }

    public String getSlog() {
        return slog;
    }
}
