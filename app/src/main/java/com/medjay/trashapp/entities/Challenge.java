package com.medjay.trashapp.entities;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Challenge implements Serializable {
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



    public Challenge(int id_challenge, Date creation_date, int state, Date starting_date, Date ending_date, float latitude, float longitude, String street, String city, String zip_code, String country) {
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
        photo= new HashSet<Photo>();
        client= new HashSet<Client>();
        note = new HashSet<Note>();
        comment = new HashSet<Comment>();

    }

    public Challenge() {
        photo= new HashSet<Photo>();
        client= new HashSet<Client>();
        note = new HashSet<Note>();
        comment = new HashSet<Comment>();

    }


    //association  one to many unidirectionnelle entre challenge et photo  (composition)

    private Set<Photo> photo;    //association one to many entre <challenge> et <photo>
    public void addPhoto(Photo pic){
        if( !getPhoto().contains(pic) ) getPhoto().add(pic); }

    public void removePhoto(Photo pic){
        if(getPhoto().contains(pic)) getPhoto().remove(pic); }

    public Set<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(Set<Photo> cf) {
        this.photo = cf;
    }

    //association (owner) one to many bidirectionnelle entre challenge et client (esq na7sbouha unidirectionnelle ou nn )
    private Client owner;
    public void addOwner(Client c){
        if(!c.getChallenge().contains(this)){
            if(getOwner() != null) removeOwner();
            setOwner(c); c.addChallenge(this); }
    }
    public void removeOwner(){ getOwner().removeChallenge(this); setClient(null);
    }
    public Client getOwner(){
        return owner;
    }
    public void setOwner(Client c){
        this.owner = c; }


    //association (participants) many to many unidirectionnelle entre challenge et client

    private Set<Client> client;    //association many to many entre <challenge> et <client>
    public void addClient(Client c){ if(
            !getClient().contains(c) ) getClient().add(c);  }

    public void removeClient(Client c){
        if(getClient().contains(c)) getClient().remove(c); }

    public Set<Client> getClient() {
        return client;
    }

    public void setClient(Set<Client> c) {
        this.client = c;
    }


    //classe association  ==> one to many unidirectionnelle entre note et challenge

    private Set<Note> note;
    public void addNote(Note n){
        if( !getNote().contains(n) ) getNote().add(n);
    }

    public void removeNote(Note n){
        if(getNote().contains(n)) getNote().remove(n);
    }

    public Set<Note> getNote(){
        return note;
    }

    public void setNote(Set<Note> note){
        this.note = note; }


    //classe association  ==> one to many unidirectionnelle entre comment et challenge
    private Set<Comment> comment;

    public void addComment(Comment c){
        if( !getComment().contains(c) ) getComment().add(c);
    }

    public void removeComment(Comment c){
        if(getComment().contains(c)) getComment().remove(c);
    }

    public Set<Comment> getComment(){
        return comment;
    }

    public void setComment(Set<Comment> c){
        this.comment = c; }



        //la fonction de getparticipant


    public int getId_challenge() {
        return id_challenge;
    }

    public void setId_challenge(int id_challenge) {
        this.id_challenge = id_challenge;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getStarting_date() {
        return starting_date;
    }

    public void setStarting_date(Date starting_date) {
        this.starting_date = starting_date;
    }

    public Date getEnding_date() {
        return ending_date;
    }

    public void setEnding_date(Date ending_date) {
        this.ending_date = ending_date;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Challenge)) return false;
        Challenge challenge = (Challenge) o;
        return getId_challenge() == challenge.getId_challenge();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_challenge());
    }

}
