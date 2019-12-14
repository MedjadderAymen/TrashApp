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


            final String title= items.get(position).titre;
            final TextView _title= layout.findViewById(R.id.chal_title);
            _title.setText(title);

            final String date= items.get(position).date;
            final TextView _date= layout.findViewById(R.id.chal_date);
            _date.setText(date);

            final String location= items.get(position).location;
            final TextView _location= layout.findViewById(R.id.chal_location);
            _location.setText(location);

            final int image= items.get(position).image;
            final ImageView _image= layout.findViewById(R.id.chal_image);
            _image.setImageResource(image);

            return layout;

        }

}
