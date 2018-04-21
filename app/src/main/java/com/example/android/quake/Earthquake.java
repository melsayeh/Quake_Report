package com.example.android.quake;

/********************************************************/
/* Defines new object type Earthquake that has 3 params */
/********************************************************/


public class Earthquake extends Object{

    // @param magnitude earth quake magnitude
    private double mMagnitude;

    // @param city location of earthquake
    private String mLocation;

    // @param date , the  date of the earthquake
    private Long mDate;

    // @param url , the webpage of the earthquake on USGS

    private String mUrl;

    public Earthquake(double mag, String loc, Long date , String url) {
        this.mMagnitude = mag;
        this.mLocation = loc;
        this.mDate = date;
        this.mUrl = url;
    }

    public void setMagnitude(double magnitude) {
        this.mMagnitude = magnitude;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public void setDate(Long date) {
        this.mDate = date;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public Long getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

}