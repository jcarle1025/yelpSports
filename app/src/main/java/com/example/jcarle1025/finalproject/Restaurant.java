package com.example.jcarle1025.finalproject;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jcarle1025 on 5/3/16.
 * business object that gets populated from yelp json parser
 */
public class Restaurant implements Parcelable {
    private String name;
    private String city;
    private String address;
    private String trip;
    private String[] categories;
    private Location location;
    private float latitude;
    private float longitude;
    private String website;
    private double rating;
    private String mobileSite;
    private String phone;
    public Bitmap iconData;
    public Bitmap snippetIcon;
    public String snippet;
    private boolean selected;

    public Restaurant(){}

    public Restaurant(Parcel source){
        name = source.readString();
        city = source.readString();
        website = source.readString();
        mobileSite = source.readString();
        phone = source.readString();
        address = source.readString();
        latitude = source.readFloat();
        longitude = source.readFloat();
        rating = source.readDouble();
        trip = source.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        if(snippet.length() >=91)
            this.snippet = snippet.substring(0,90)+"...";
        else
            this.snippet = snippet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getMobileSite() {
        return mobileSite;
    }

    public void setMobileSite(String mobileSite) {
        this.mobileSite = mobileSite;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone(){
        return phone;
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

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(city);
        dest.writeString(website);
        dest.writeString(mobileSite);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeDouble(rating);
        dest.writeString(trip);
    }
}