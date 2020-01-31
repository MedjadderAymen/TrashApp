package com.medjay.trashapp.entities;


import java.util.Date;
import java.util.Objects;

public class Admin extends User {
    private  String email ;

    public Admin() {
    super();
    }


    public Admin(int id_user, String last_name, String first_name, String user_name, String password, Date birthdate, String email) {
        super(id_user, last_name, first_name, user_name, password, birthdate);
        this.email = email;
    }








    //******************************************************************************************

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        Admin admin = (Admin) o;
        return Objects.equals(getEmail(), admin.getEmail());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getEmail());
    }

//les fonction deleteclient()  et   disabledchallenge()
}
