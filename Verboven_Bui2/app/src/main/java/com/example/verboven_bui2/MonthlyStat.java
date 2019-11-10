package com.example.verboven_bui2;


/**
 * Track the monthly stat by each user.
 */
public class MonthlyStat {
    private int systolicSum;
    private int diastolicSum;
    private int counter;
    private String user;

    public MonthlyStat(String user, int systolicSum,
                       int diastolicSum, int counter) {
        this.user = user;
        this.systolicSum = systolicSum;
        this.diastolicSum = diastolicSum;
        this.counter = counter;
    }

    public float getSystolicAverage() {
        return (float) systolicSum / counter;
    }

    public float getDiastolicAverage() {
        return (float) diastolicSum / counter;
    }

    public String getUser() {
        return user;
    }

}
