package com.medjay.trashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle= getIntent().getExtras();

        Toast.makeText(getApplicationContext(),"hello "+bundle.getString("email"),Toast.LENGTH_LONG).show();
    }
}
