package com.medjay.trashapp.entities;

import java.util.Date;
import java.util.Objects;

public class User {

   private int id_user ;
    private String last_name ;
    private  String first_name ;
    private String user_name ;
    private String password ;
    private Date birthdate ;

    public User(int id_user, String last_name, String first_name, String user_name, String password, Date birthdate) {
        this.id_user = id_user;
        this.last_name = last_name;
        this.first_name = first_name;
        this.user_name = user_name;
        this.password = password;
        this.birthdate = birthdate;

    }

    public User() {

    }




//****************************************************************************************************
    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_user);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
