package com.medjay.trashapp.entities;

import java.util.Objects;

public class Note {
    private int id_note;
    private int note_value;


    public Note(int note_value,Client c) {
        this.note_value = note_value;
        addClient(c);
    }

    public Note(){}

    //classe association  ==> one to many unidirectionnelle entre note et client

    private Client owner;
    public void addClient(Client c){
        setOwner(c);
    }

    public Client getOwner(){
        return owner;
    }
    public void setOwner(Client c){
        this.owner = c;
    }





    //***************************************************************************************************
    public int getNote_value() {
        return note_value;
    }

    public void setNote_value(int note_value) {
        this.note_value = note_value;
    }

    public int getId_note() {
        return id_note;
    }

    public void setId_note(int id_note) {
        this.id_note = id_note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return getNote_value() == note.getNote_value() &&
                Objects.equals(owner, note.owner);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNote_value(), owner);
    }
}
