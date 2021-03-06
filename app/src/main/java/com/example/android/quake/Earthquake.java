package com.example.android.quake;


public class Earthquake extends Object{

    // @param magnitude earth quake magnitude
    private String mMagnitude;

    // @param city location of earthquake
    private String mLocation;

    // @param date , the  date of the earthquake
    private Long mDate;

    public Earthquake(String mag, String loc, Long date) {
        this.mMagnitude = mag;
        this.mLocation = loc;
        this.mDate = date;
    }

    public void setMagnitude(String magnitude) {
        this.mMagnitude = magnitude;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public void setDate(Long date) {
        this.mDate = date;
    }

    public String getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public Long getDate() {
        return mDate;
    }

//    @Override
//    public String toString(){
//        return mMagnitude+"       "+mLocation+"    "+mDate;
//    }
}