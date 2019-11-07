package com.example.verboven_bui2;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class Reading {

    private GregorianCalendar curTime;
    private int systolicReading;
    private int diastolicReading;
    private String condition;

    public Reading(GregorianCalendar curTime,
                   int systolicReading, int diastolicReading) {

        this.curTime = curTime;
        this.systolicReading = systolicReading;
        this.diastolicReading = diastolicReading;
        this.condition = analyzeReading();
    }

    /**
     * Check systolic and diastolic reading to find
     * condition.
     * @return condition as a String.
     */
    private String analyzeReading() {
        return "Bad";
    }

    public String getCurDateAsStr() {
        int year = curTime.get(Calendar.YEAR);
        int month = curTime.get(Calendar.MONTH);
        int day = curTime.get(Calendar.DAY_OF_MONTH);

        return day + " "
                + curTime.getDisplayName(Calendar.MONTH,
                    Calendar.SHORT,
                    Locale.CANADA) + " "
                + year;

    }

    public String getCurTimeAsStr() {
        int hour = curTime.get(Calendar.HOUR);
        int min = curTime.get(Calendar.MINUTE);
        String amPm = curTime.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

        return hour + ":" + min + amPm;
    }


}
