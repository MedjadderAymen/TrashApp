package com.medjay.trashapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.medjay.trashapp.entities.Challenges;

import java.util.List;

import androidx.annotation.NonNull;

public class adapter_items extends ArrayAdapter<Challenges>{


        Activity activity;
        int idimg;
        List<Challenges> items;


        public adapter_items (Activity activity, int idimg, List<Challenges> items) {
            super(activity, idimg, items);
            this.activity = activity;
            this.idimg = idimg;
            this.items = items;
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
