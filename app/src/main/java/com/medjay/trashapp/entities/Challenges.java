package com.medjay.trashapp.entities;

public class Challenges {

    public int image;
    public int id;
    public String titre,location,date;

    public Challenges(int image, int id, String titre, String location, String date) {
        this.image = image;
        this.id = id;
        this.titre = titre;
        this.location = location;
        this.date = date;
    }
}
