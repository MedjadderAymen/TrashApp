package com.medjay.trashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout=findViewById(R.id.DrawerHome);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_Drawer,R.string.Close_Drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new com.medjay.trashapp.Fragments.Home()).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id=menuItem.getItemId();

        switch (id){
            case R.id.discover:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new com.medjay.trashapp.Fragments.Home()).commit();
            break;

            case R.id.My_Chanlenges:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new com.medjay.trashapp.Fragments.MyChalenges()).commit();
                break;
        }

        DrawerLayout drawerLayout=findViewById(R.id.DrawerHome);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
