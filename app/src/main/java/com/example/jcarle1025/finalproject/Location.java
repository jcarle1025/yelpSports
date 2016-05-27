package com.example.jcarle1025.finalproject;

/**
 * Created by jcarle1025 on 5/5/16.
 * locaiton object for each business
 */
public class Location {
    private String city;
    private String streetAddress;
    private int zip;
    private float latitude;
    private float longitude;

    public Location(){
        this.streetAddress = "";
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
