package com.example.verboven_bui2;


import android.os.Parcel;
import android.os.Parcelable;

public class Reading implements Parcelable {

    private String curTime;
    private String curDate;
    private int systolicReading;
    private int diastolicReading;
    private String condition;
    private String key;
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

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public Reading(String curTime, String curDate,
                   int systolicReading, int diastolicReading, String key, String name) {

        this.curTime = curTime;
        this.curDate = curDate;
        this.systolicReading = systolicReading;
        this.diastolicReading = diastolicReading;
        this.condition = analyzeReading();
        this.key = key;
        this.name = name;
    }

    // for firebase and parcelable
    public Reading() {

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

    // 99.9% of the time you can just ignore this
    // done for Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(curDate);
        out.writeString(curTime);
        out.writeString(condition);
        out.writeString(key);
        out.writeInt(systolicReading);
        out.writeInt(diastolicReading);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Reading> CREATOR = new Parcelable.Creator<Reading>() {
        public Reading createFromParcel(Parcel in) {
            return new Reading(in);
        }

        public Reading[] newArray(int size) {
            return new Reading[size];
        }
    };

    // constructor that takes a Parcel and gives
    // you an object populated with it's values
    private Reading(Parcel in) {
        name = in.readString();
        curDate = in.readString();
        curTime = in.readString();
        condition = in.readString();
        key = in.readString();
        systolicReading = in.readInt();
        diastolicReading = in.readInt();
    }
}
