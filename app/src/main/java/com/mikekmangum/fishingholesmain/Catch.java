package com.mikekmangum.fishingholesmain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.sql.Timestamp;

public class Catch {



    private Timestamp mTimestamp;
    private String mSpecies;
    private double mLength;
    private double mWeight;
    private String mLure;
    private double mLatitude;
    private double mLongitude;
    private double mTemperature;
    private String mConditions;
    private byte[] mPicture;



    public Catch() {

    }

    public void setTimestamp(Timestamp timestamp) { mTimestamp = timestamp; }

    public Timestamp getTimestamp() {
        return mTimestamp;
    }

    public String getSpecies() {
        return mSpecies;
    }

    public void setSpecies(String species) { this.mSpecies = species; }

    public double getLength() {
        return mLength;
    }

    public void setLength(double length) { this.mLength = length; }

    public double getWeight() { return mWeight; }

    public void setWeight(double weight) { mWeight = weight; }

    public String getLure() { return mLure; }

    public void setLure(String lure) { mLure = lure; }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public double getTemperature() { return mTemperature; }

    public void setTemperature(double temperature) { mTemperature = temperature; }

    public String getConditions() {
        return mConditions;
    }

    public void setConditions(String conditions) {
        this.mConditions = conditions;
    }

    public byte[] getPicture() {
        return mPicture;
    }

    public void setPicture(byte[] picture) {
        this.mPicture = picture;
    }


}
