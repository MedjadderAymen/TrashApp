package com.medjay.trashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.medjay.trashapp.Fragments.Challenges;
import com.medjay.trashapp.Fragments.Connection;
import com.medjay.trashapp.Fragments.Maps;
import com.medjay.trashapp.Fragments.Profile;
import com.medjay.trashapp.Fragments.Settings;
import com.medjay.trashapp.entities.Client;

import java.lang.reflect.Type;
import java.util.List;


public class Home extends AppCompatActivity{

    SpaceNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Gson gson = new Gson();
        final String json = getSharedPreferences("prefs",0).getString("client", null);


        final RelativeLayout coordinatorLayout=findViewById(R.id.coordinatorLayout);
        navigationView =findViewById(R.id.space);

        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_settings_black_24dp));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_terrain_black_24dp));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_map_black_24dp));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_person_black_24dp));

        navigationView.changeCurrentItem(1);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new Challenges()).commit();

        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                if (json!=null){
                    startActivity(new Intent(Home.this,Create_Challenge.class));
                    navigationView.setCentreButtonSelectable(true);
                }else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "You must login", Snackbar.LENGTH_LONG)
                            .setAction("Go!", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getApplicationContext(), Welcome.class));
                                    finish();
                                }
                            });

                    snackbar.show();
                }

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                //Toast.makeText(Home.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                switch (itemIndex){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new Settings()).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new Challenges()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new Maps()).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new Profile()).commit();
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                //Toast.makeText(Home.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                switch (itemIndex){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new Settings()).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new Challenges()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new Maps()).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new Profile()).commit();
                        break;
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        navigationView.onSaveInstanceState(outState);
    }


}
