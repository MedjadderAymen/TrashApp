package com.medjay.trashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.Photo;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class Create_Challenge extends AppCompatActivity {

    private List<Address> addresses;
    private Geocoder geocoder;
    TextView chal_address;

    private MapView mapView;

    private SharedPreferences preferences;
    private float Latitude;
    private float Longitude;
    private TileCache tileCache;

    //Firebase
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private StorageTask uploadTask;

    int i;
    int id_challenge;

    List<Uri> ListPhoto=new ArrayList<>();
    List<Bitmap> ListPhotoBitmap=new ArrayList<>();
    GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidGraphicFactory.createInstance(getApplication());
        setContentView(R.layout.activity_create__challenge);


        preferences=getSharedPreferences("prefs",0);
        mapView=findViewById(R.id.mapView);
        geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setZoomLevelMin((byte) 10);
        mapView.setZoomLevelMax((byte) 20);

        tileCache = AndroidUtil.createTileCache(getApplicationContext(), "mapcache",
                mapView.getModel().displayModel.getTileSize(),1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());

        chal_address = findViewById(R.id.chal_address);

        gridView=findViewById(R.id.grid);
        gridView.setAdapter(new ImageGride(Create_Challenge.this,ListPhotoBitmap));


        ImageView add_phot = findViewById(R.id.chal_add_photo);
        add_phot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String [] choix={"Camera","Gallery"};
                AlertDialog.Builder builder=new AlertDialog.Builder(Create_Challenge.this);
                builder.setItems(choix, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, 99);}
                            else {
                                if(checkCameraHardware(Create_Challenge.this)){
                                    Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (camIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(camIntent, 99); // 99 = requestCode
                                    }
                                }
                            }
                        }else {
                            Intent gal = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            startActivityForResult(gal,123);
                        }
                    }
                });

                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

        SwipeButton enableButton = (SwipeButton) findViewById(R.id.swipe_btn);
        enableButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if (active){
                    saveChallenge();
                }
            }
        });

    }

    private void saveChallenge() {

        DateFormat df= DateFormat.getDateInstance(DateFormat.SHORT);
        Date birthday = null;
        try {
            birthday = df.parse("2020-01-03 21:54");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        WebServerIntf webServerIntf= RetrofitBuilder.getRetrofitInstance().create(WebServerIntf.class);
        Challenge  challenge=new Challenge(1,birthday,
                1,
                birthday,
                birthday,
                (float) addresses.get(0).getLatitude(),
                (float)addresses.get(0).getLongitude(),
                addresses.get(0).getLocality()+" Address "+addresses.get(0).getAddressLine(0),
                addresses.get(0).getAdminArea()+"",
                addresses.get(0).getPostalCode()+"",
                addresses.get(0).getCountryName()+"");

            Call<Challenge> challengeCall=webServerIntf.PostChallenge(challenge,1);
            challengeCall.enqueue(new Callback<Challenge>() {
                @Override
                public void onResponse(Call<Challenge> call, Response<Challenge> response) {
                    Toast.makeText(getApplicationContext(),response.body().getId_challenge()+"",Toast.LENGTH_SHORT).show();
                    id_challenge=response.body().getId_challenge();
                    uploadImagesService();

                }

                @Override
                public void onFailure(Call<Challenge> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void savePhotos(Photo photo,int id) {
        WebServerIntf webServerIntf= RetrofitBuilder.getRetrofitInstance().create(WebServerIntf.class);

            Call<Photo> photoCall=webServerIntf.PostPhotos(photo,id);
            photoCall.enqueue(new Callback<Photo>() {
                @Override
                public void onResponse(Call<Photo> call, Response<Photo> response) {
                    Log.i("response","response "+response.body());
                }

                @Override
                public void onFailure(Call<Photo> call, Throwable t) {
                    Log.i("error","error "+t.getMessage());
                }
            });


    }

    private boolean uploadImagesService() {

        final ProgressDialog dialog=new ProgressDialog(Create_Challenge.this);
        dialog.setTitle("Uploading...");
        if (!ListPhoto.isEmpty()){
            dialog.show();
            for ( i=0;i<ListPhoto.size();i++) {

                final StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());
                uploadTask=ref.putFile(ListPhoto.get(i)).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double prog=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        dialog.setTitle("uploading "+(int)prog+" %");
                    }
                });
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task <UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        return ref.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String mUri = downloadUri.toString();
                            dialog.dismiss();

                            DateFormat df= DateFormat.getDateInstance(DateFormat.SHORT);
                            Date birthday = null;
                            try {
                                birthday = df.parse("2020-01-03");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            savePhotos(new Photo(1,mUri,birthday),id_challenge);
                            //PhotoFirebasePaths.add();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }

        }

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i){
        if(requestCode == 99 && resultCode == RESULT_OK){
            Bundle b = i.getExtras();
            Bitmap photo = (Bitmap) b.get("data");
            Uri imagePath=saveToStorage(photo);
            ListPhoto.add(imagePath);

            ListPhotoBitmap.add(photo);
            gridView.setAdapter(new ImageGride(Create_Challenge.this,ListPhotoBitmap));
        }

        else if (requestCode==123 && resultCode==RESULT_OK){
            Uri imageUri = i.getData();
            ListPhoto.add(imageUri);


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ListPhotoBitmap.add(bitmap);
                gridView.setAdapter(new ImageGride(Create_Challenge.this,ListPhotoBitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Uri saveToStorage(Bitmap photo){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fname = timeStamp+ ".jpg";
        File file = new File(myDir, fname);

        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri contentUri = Uri.fromFile(file);
        return contentUri;
    }

    @Override
    public void onStart() {
        super.onStart();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/Maps/constantine.map");
        file.setReadable(true, false);
        MapDataStore mapDataStore = new MapFile(file);
        //**************************

        //**********************************************
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                mapView.getModel().mapViewPosition,
                AndroidGraphicFactory.INSTANCE) {
            @Override
            public boolean onLongPress(LatLong tapLatLong, Point layerXY, Point tapXY) {
                               drawMarker(R.drawable.ic_location_on_black_24dp, tapLatLong);
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                return true;
            }
        };

        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
        mapView.getLayerManager().getLayers().add(tileRendererLayer);

        Latitude=preferences.getFloat("Latitude",0.0f);
        Longitude=preferences.getFloat("Longitude",0.0f);

        LatLong latLong=new LatLong(Latitude, Longitude);
        mapView.setCenter(latLong);
        drawMyPositionMarker(R.drawable.ic_my_location_black_24dp,latLong);
        mapView.setZoomLevel((byte) 15);

    }

    @Override
    public void onDestroy() {
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }

    public void drawMyPositionMarker(int resourceId, final LatLong geoPoint){
        Drawable drawable = getResources().getDrawable(resourceId);
        try {
            addresses = geocoder.getFromLocation(geoPoint.latitude,geoPoint.longitude,1);
            chal_address.setText(addresses.get(0).getAdminArea()+
                    " Belediya "+addresses.get(0).getLocality()+
                    " Address "+addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        org.mapsforge.core.graphics.Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);

        bitmap.scaleTo(50,50);
        Marker marker = new Marker(geoPoint, bitmap, 0, -bitmap.getHeight() / 2){
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                if (contains(layerXY,tapXY)){

                    Toast.makeText(getApplicationContext(),"You are here",Toast.LENGTH_LONG).show();
                }

                return false;
            }
        };
        mapView.getLayerManager().getLayers().add(marker);
    }

    public void drawMarker(int resourceId, final LatLong geoPoint){


        try {
            addresses = geocoder.getFromLocation(geoPoint.latitude,geoPoint.longitude,1);
            chal_address.setText(addresses.get(0).getAdminArea()+
                    " Belediya "+addresses.get(0).getLocality()+
                    " Address "+addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Drawable drawable = getResources().getDrawable(resourceId);

        org.mapsforge.core.graphics.Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);

        bitmap.scaleTo(50,50);
        Marker marker = new Marker(geoPoint, bitmap, 0, -bitmap.getHeight() / 2){
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                if (contains(layerXY,tapXY)){


                    Toast.makeText(getApplicationContext()," Country "+addresses.get(0).getCountryName()+
                                    " Wilaya "+addresses.get(0).getAdminArea()+
                                    " Belediya "+addresses.get(0).getLocality()+
                                    " Address "+addresses.get(0).getAddressLine(0)
                            ,Toast.LENGTH_LONG).show();

                }

                return false;
            }
        };
        mapView.getLayerManager().getLayers().add(marker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if(requestCode == 99 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

        }
    }

    private boolean checkCameraHardware(Context ctx) {
        if(ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }


}
