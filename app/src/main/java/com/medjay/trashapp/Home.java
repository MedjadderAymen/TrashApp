package com.medjay.trashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.medjay.trashapp.Fragments.List;
import com.medjay.trashapp.Fragments.Maps;
import com.medjay.trashapp.Fragments.Profile;
import com.medjay.trashapp.Fragments.Settings;


public class Home extends AppCompatActivity{

    SpaceNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationView =findViewById(R.id.space);

        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_settings_black_24dp));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_terrain_black_24dp));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_map_black_24dp));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_person_black_24dp));

        navigationView.changeCurrentItem(1);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new List()).commit();

        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Toast.makeText(Home.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
                navigationView.setCentreButtonSelectable(true);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                //Toast.makeText(Home.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                switch (itemIndex){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new Settings()).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new List()).commit();
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new List()).commit();
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
