package com.ckjs.ck.Bean;

/**
 * Created by NiPing on 2017/6/16.
 */

public class StepPostBean {


    /**
     * "month": "5",
     * "day": "13",
     * "steps": "85",
     * "fat": "130",
     * "mileage": "130",
     */
    private int month;
    private int day;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }


    private int steps;


    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    private String mileage;


    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    private String fat;
}
