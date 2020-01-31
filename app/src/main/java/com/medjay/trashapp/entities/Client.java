package com.medjay.trashapp.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Client extends User implements Serializable {
    private String phone_number ;
    private String android_version ;

    private Set<Challenge> challenge;
    private Set<Note> note;
    //private Set<Comment> comment;


    public Client() {
        super();
        challenge= new HashSet<Challenge>();
        note = new HashSet<Note>();
        //comment = new HashSet<Comment>();

    }

    public Client(int id_user, String last_name, String first_name, String user_name, String password, Date birthdate, String phone_number, String android_version) {
        super(id_user, last_name, first_name, user_name, password, birthdate);
        this.phone_number = phone_number;
        this.android_version = android_version;
        challenge= new HashSet<Challenge>();
        note = new HashSet<Note>();
       //comment = new HashSet<Comment>();

    }


    //association (owner) one to many BIDidirectionnelle entre challenge et client

    public void addChallenge(Challenge c) {
        //if( !getChallenge().contains(c) ) getChallenge().add(c);    //unidirectionnel
        if (!getChallenge().contains(c)) {       //bidirectionnele
            if (c.getOwner() != null) c.removeOwner();
            c.setOwner(this);
            getChallenge().add(c);
        }
    }

    public void removeChallenge(Challenge c){
        if(getChallenge().contains(c)) getChallenge().remove(c);
        c.setOwner(null);  }

    public Set<Challenge> getChallenge() {
        return challenge;
    }

    public void setChallenge(Set<Challenge> cc) {
        this.challenge = cc;
    }


    //classe association  ==> one to many unidirectionnelle entre note et client

    /*public void addNote(Note n){
        if( !getNote().contains(n) ) getNote().add(n);
    }

    public void removeNote(Note n){
        if(getNote().contains(n)) getNote().remove(n);
    }

    public Set<Note> getNote(){ return note; }
    public void setNote(Set<Note> note){ this.note = note; }*/

    //classe association  ==> one to many unidirectionnelle entre commentaire et client

   /* public void addComment(Comment c){
        if( !getComment().contains(c) ) getComment().add(c);
    }

    public void removeComment(Comment c){
        if(getComment().contains(c)) getComment().remove(c);
    }

    public Set<Comment> getComment(){ return comment; }
    public void setComment(Set<Comment> c){ this.comment = c; }*/










    //************************************************************************************

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }
@Override
    public int hashCode() {
        int result=3;
        result = 29 * result + getId_user();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(getPhone_number(), client.getPhone_number()) &&
                Objects.equals(getAndroid_version(), client.getAndroid_version());
    }
}
