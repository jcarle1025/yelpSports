package com.example.jcarle1025.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jcarle1025 on 5/5/16.
 * //stadium object
 * populated by buffered reader going through local text file
 * of all teams/stadiums/locations
 */
public class Venue implements Parcelable{
    private String league;
    private String stadium;
    private String team;
    private float lat;
    private float lon;

    public Venue(String league, String stadium, String team, float lat, float lon){
        this.league = league;
        this.stadium = stadium;
        this.team = team;
        this.lat = lat;
        this.lon = lon;
    }

    public Venue(Parcel source){
        league = source.readString();
        stadium = source.readString();
        team = source.readString();
        lat = source.readFloat();
        lon = source.readFloat();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(league);
        dest.writeString(stadium);
        dest.writeString(team);
        dest.writeFloat(lat);
        dest.writeFloat(lon);
    }
}