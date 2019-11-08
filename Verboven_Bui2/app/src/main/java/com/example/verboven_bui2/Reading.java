package com.example.verboven_bui2;


public class Reading {

    private String curTime;
    private String curDate;
    private int systolicReading;
    private int diastolicReading;
    private String condition;
    private String name;

    public String getCurTime() {
        return curTime;
    }

    public String getCurDate() {
        return curDate;
    }

    public int getSystolicReading() {
        return systolicReading;
    }

    public int getDiastolicReading() {
        return diastolicReading;
    }

    public String getName() {
        return name;
    }

    public Reading(String curTime, String curDate,
                   int systolicReading, int diastolicReading,
                   String name) {

        this.curTime = curTime;
        this.curDate = curDate;
        this.systolicReading = systolicReading;
        this.diastolicReading = diastolicReading;
        this.condition = analyzeReading();
        this.name = name;
    }

    /**
     * Check systolic and diastolic reading to find
     * condition.
     * @return condition as a String.
     */
    private String analyzeReading() {
        return "Bad";
    }

    public String getCondition() { return this.condition;}
}
