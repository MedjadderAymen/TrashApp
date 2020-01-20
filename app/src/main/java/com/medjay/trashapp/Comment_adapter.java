package com.medjay.trashapp;

import android.app.Activity;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.Comment;
import com.medjay.trashapp.entities.Photo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

public class Comment_adapter extends ArrayAdapter<Comment> {

    Activity activity;
    int idimg;
    List<Comment> items;

    public Comment_adapter (Activity activity, int idimg, List<Comment> items) {
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

        TextView comment_owner = layout.findViewById(R.id.cmnt_owner);
        comment_owner.setText(items.get(position).getOwner().getUser_name());

        TextView comment_content= layout.findViewById(R.id.comment_content);
        comment_content.setText(items.get(position).getContent());


        return layout;

    }

}
