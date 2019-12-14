package com.medjay.trashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    protected int SPLASH_TIME_OUT = 2000;
    private static final int LOCATION_APP_PERM = 123;

    LocationManager locationManager;
    LocationListener locationListener;
    String provider;

    int minTime = 5000;
    float minDistance = 10;

    double x,y,z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        requestPermissions();

        askActualPosition();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(LOCATION_APP_PERM)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            Criteria cr = new Criteria();
            cr.setAccuracy(Criteria.ACCURACY_FINE);
            cr.setAltitudeRequired(true);
            cr.setBearingRequired(true);
            cr.setCostAllowed(false);
            cr.setPowerRequirement(Criteria.ACCURACY_HIGH);
            cr.setSpeedRequired(true);

            provider = locationManager.getBestProvider(cr, false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }
            Location location = locationManager.getLastKnownLocation(provider);
            Log.i("GeoFragment","le provider "+provider+" a ete selectionne");

            if (location!=null){
                Log.i("GeoFragment","position trouvé");
                x = location.getLongitude();
                y = location.getLatitude();
                z = location.getAltitude();

            }else {
                Log.i("GeoFragment","position non trouvé");
            }

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your position", LOCATION_APP_PERM, perms);
        }
    }

    private void askActualPosition() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                x = location.getLongitude();
                y = location.getLatitude();
                z = location.getAltitude();

                Toast.makeText(MainActivity.this,"getLongitudeNOW"+x,Toast.LENGTH_LONG).show();
                Log.i("getLongitudeNOW",""+x);
                Log.i("getLatitudeNOW",""+y);
                Log.i("getAltitudeNOW",""+z);

                if (x!=0 && y!=0 && z!=0 ){
                    startActivity(new Intent(MainActivity.this, Welcome.class));
                    finish();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        locationManager.requestLocationUpdates(provider, minTime, minDistance, locationListener);

    }
}
