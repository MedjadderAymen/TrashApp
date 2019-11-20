package com.medjay.trashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    protected  int SPLASH_TIME_OUT =2000;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (have_connexion()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this,Welcome.class));
                    finish();
                }
            },SPLASH_TIME_OUT);
        }else if (!have_connexion()){
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage("Erreur de Connexion");
            builder.setPositiveButton("Parametre", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("rafrichir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                    finish();
                }
            });
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.show();
        }
    }

    private boolean have_connexion(){
        boolean _wifi=false;
        boolean _mobile=false;

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] info=connectivityManager.getAllNetworkInfo();

        for (NetworkInfo in:info){
            if (in.getTypeName().equalsIgnoreCase("WIFI")){
                if (in.isConnected())
                    _wifi=true;
            }else if (in.getTypeName().equalsIgnoreCase("MOBILE")){
                if (in.isConnected())
                    _mobile=true;
            }
        }
        return _wifi||_mobile ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (have_connexion()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this,Welcome.class));
                    finish();
                }
            },SPLASH_TIME_OUT);
        }else if (!have_connexion()){
            dialog.show();
        }
    }
}
