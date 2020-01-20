package com.medjay.trashapp;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.Client;
import com.medjay.trashapp.entities.Photo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

public class adapter_items extends ArrayAdapter<Challenge>{


        Activity activity;
        int idimg;
        List<Challenge> items;

    private List<Address> addresses;
    private Geocoder geocoder;


        public adapter_items (Activity activity, int idimg, List<Challenge> items) {
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

            ImageView photo_challenge = layout.findViewById(R.id.chal_photo);

            List<Photo> photos = new ArrayList<>();
            photos.addAll(items.get(position).getPhoto());
            Glide.with(activity).load(photos.get(0).getPath()).into(photo_challenge);

            TextView owner = layout.findViewById(R.id.chal_owner);
            owner.setText(items.get(position).getOwner().getUser_name());


            try {
                addresses = geocoder.getFromLocation(items.get(position).getLatitude(),items.get(position).getLongitude(),1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            TextView location= layout.findViewById(R.id.chal_address);
            location.setText(addresses.get(0).getAdminArea()+ " "+addresses.get(0).getLocality());


            TextView date = layout.findViewById(R.id.chal_date);
            date.setText(items.get(position).getStarting_date()+"");


            return layout;

        }

}
