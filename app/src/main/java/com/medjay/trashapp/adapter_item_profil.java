package com.medjay.trashapp;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.Photo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

public class adapter_item_profil extends ArrayAdapter<Challenge> {

    Activity activity;
    int idimg;
    List<Challenge> items;

    private List<Address> addresses;
    private Geocoder geocoder;


    public adapter_item_profil (Activity activity, int idimg, List<Challenge> items) {
        super(activity, idimg, items);
        this.activity = activity;
        this.idimg = idimg;
        this.items = items;
        geocoder=new Geocoder(getContext(), Locale.getDefault());
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View layout = convertView;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(idimg, parent, false);
        }




        return layout;

    }


}
