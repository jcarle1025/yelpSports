package com.example.jcarle1025.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jcarle1025 on 5/3/16.
 *
 * trip object allows user to save stadium/restaurant cominations
 * to sqlite db
 */
public class Trip implements Parcelable{
    private ArrayList<Restaurant> foodList;
    private String tripName;
    private String stadiumName;

    public Trip(String tripName, String stadiumName){
        this.foodList = new ArrayList<Restaurant>();
        this.tripName = tripName;
        this.stadiumName = stadiumName;
    }

    public Trip(String tripName){
        this.foodList = new ArrayList<Restaurant>();
        this.tripName = tripName;
    }

    public Trip(Parcel source){
        tripName = source.readString();
        stadiumName = source.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
    public ArrayList<Restaurant> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<Restaurant> foodList) {
        this.foodList = foodList;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(foodList);
        dest.writeString(tripName);
        dest.writeString(stadiumName);
    }
}
