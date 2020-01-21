package com.medjay.trashapp.entities;

import java.util.Date;

public class ChallengeSQL {

    private int id_challenge;
    private Date creation_date ;
    private int state;
    private Date starting_date;
    private Date ending_date ;
    private float latitude  ;
    private float longitude ;
    private String street;
    private String city ;
    private String zip_code;
    private String country;
    private String user_name;
    private String one_path;

    public int getId_challenge() {
        return id_challenge;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public int getState() {
        return state;
    }

    public Date getStarting_date() {
        return starting_date;
    }

    public Date getEnding_date() {
        return ending_date;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZip_code() {
        return zip_code;
    }

    public String getCountry() {
        return country;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getOne_path() {
        return one_path;
    }

    public ChallengeSQL(int id_challenge, Date creation_date, int state, Date starting_date, Date ending_date, float latitude, float longitude, String street, String city, String zip_code, String country, String user_name, String one_path) {
        this.id_challenge = id_challenge;
        this.creation_date = creation_date;
        this.state = state;
        this.starting_date = starting_date;
        this.ending_date = ending_date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.city = city;
        this.zip_code = zip_code;
        this.country = country;
        this.user_name = user_name;
        this.one_path = one_path;
    }
}
