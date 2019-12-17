package com.medjay.trashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

public class Welcome extends AppCompatActivity {

    TextView _connecter;
    Button _chercher ;

    List<Address> addresses;
    Geocoder geocoder;
    double Longitude,Latitude,Altitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);


        Bundle bundle=getIntent().getExtras();

        Longitude=bundle.getDouble("Longitude");
        Latitude=bundle.getDouble("Latitude");
        Altitude=bundle.getDouble("Altitude");

        geocoder=new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(Latitude,Longitude,1);
            Toast.makeText(Welcome.this,"country "+addresses.get(0).getCountryName()+" wilaya "+addresses.get(0).getAdminArea(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        _connecter=findViewById(R.id.connecter);
        _connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this,con_enr.class));
            }
        });


       _chercher=findViewById(R.id.chercher);
        _chercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this,Home.class));
                finish();
            }
        });

    }


}
