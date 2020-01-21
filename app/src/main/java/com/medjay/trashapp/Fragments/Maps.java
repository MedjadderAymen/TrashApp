package com.medjay.trashapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.medjay.trashapp.Challenge_details;
import com.medjay.trashapp.MyHelperSQLite;
import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.R;
import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.ChallengeSQL;
import com.medjay.trashapp.entities.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Maps extends Fragment {

    private List<Address> addresses;
    private Geocoder geocoder;

    private MapView mapView;
    private TileCache tileCache;

    private CardView cardView;
    private ImageView _chal_photo;
    private TextView _chal_address,_chal_takeMe,_chal_owner,_chal_cancel,_chal_toggle;

    private SharedPreferences preferences;
    private float Latitude;
    private float Longitude;

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AndroidGraphicFactory.createInstance(this.getActivity().getApplication());
        view= inflater.inflate(R.layout.fragment_maps, container, false);

        preferences=getApplicationContext().getSharedPreferences("prefs",0);
        mapView=view.findViewById(R.id.mapView);
        cardView=view.findViewById(R.id.chal_on_map);
        _chal_address=view.findViewById(R.id.chal_address);
        _chal_takeMe=view.findViewById(R.id.chal_takeMe);
        _chal_owner=view.findViewById(R.id.chal_owner);
        _chal_cancel=view.findViewById(R.id.chal_cancel);
        _chal_photo=view.findViewById(R.id.chal_photo);
        _chal_toggle=view.findViewById(R.id.chal_toggle);

        geocoder=new Geocoder(getContext(), Locale.getDefault());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setZoomLevelMin((byte) 10);
        mapView.setZoomLevelMax((byte) 20);

        tileCache = AndroidUtil.createTileCache(getActivity(), "mapcache",
                mapView.getModel().displayModel.getTileSize(),1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());

        return view;
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
                //                drawMarker(R.drawable.ic_location_on_black_24dp, tapLatLong);
//                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//                vibrator.vibrate(500);
                return true;
            }
        }
        ;

        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
        mapView.getLayerManager().getLayers().add(tileRendererLayer);

        Latitude=preferences.getFloat("Latitude",0.0f);
        Longitude=preferences.getFloat("Longitude",0.0f);

        LatLong latLong=new LatLong(Latitude, Longitude);
        mapView.setCenter(latLong);
        drawMyPositionMarker(R.drawable.ic_my_location_black_24dp,latLong);
        mapView.setZoomLevel((byte) 15);

        getChallenges();
    }

    private void getChallenges() {
        WebServerIntf serverIntf= RetrofitBuilder.getRetrofitInstance().create(WebServerIntf.class);
        Call<List<Challenge>> call = serverIntf.getChallenges();

        call.enqueue(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, retrofit2.Response<List<Challenge>> response) {
                for (int i=0;i<response.body().size();i++){
                Challenge challenge=response.body().get(i);
                    drawChallengesMarker(R.drawable.ic_location_on_black_24dp,challenge);
                }
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                MyHelperSQLite myHelperSQLite=new MyHelperSQLite(getContext());

                List<ChallengeSQL> challenges=myHelperSQLite.getChallenges();
                for (int i=0;i<challenges.size();i++){
                    drawChallengesSQLMarker(R.drawable.ic_location_on_black_24dp,challenges.get(i));
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }

    public void drawChallengesMarker(int resourceId, final Challenge challenge){
        Drawable drawable = getResources().getDrawable(resourceId);

        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);

        bitmap.scaleTo(50,50);
        final LatLong latLong=new LatLong(challenge.getLatitude(),challenge.getLongitude());
        Marker marker = new Marker(latLong, bitmap, 0, -bitmap.getHeight() / 2){
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                if (contains(layerXY,tapXY)){
                    try {
                        addresses = geocoder.getFromLocation(latLong.latitude,latLong.longitude,1);
                        //*********open challenge
                        if (cardView.getVisibility()==View.INVISIBLE | cardView.getVisibility()==View.GONE){
                            cardView.setVisibility(View.VISIBLE);
                        }

                        _chal_owner.setText(challenge.getOwner().getUser_name());
                        _chal_address.setText(addresses.get(0).getAdminArea()+ " "+addresses.get(0).getLocality());
                        List<Photo> photos = new ArrayList<Photo>();
                        photos.addAll(challenge.getPhoto());
                        Glide.with(getActivity()).load(photos.get(0).getPath()).into(_chal_photo);

                        _chal_takeMe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                    GetRoute(latLong.latitude,latLong.longitude);
                            }
                        });

                        _chal_toggle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getActivity(), Challenge_details.class);
                                Bundle bundle=new Bundle();
                                bundle.putInt("challenge_id",challenge.getId_challenge());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        _chal_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cardView.getVisibility()==View.VISIBLE ){
                                    cardView.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                        //******************************
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return true;
                }

                return false;
            }
        };
        mapView.getLayerManager().getLayers().add(marker);
    }

    public void drawMyPositionMarker(int resourceId, final LatLong geoPoint){
        Drawable drawable = getResources().getDrawable(resourceId);

        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);

        bitmap.scaleTo(50,50);
        Marker marker = new Marker(geoPoint, bitmap, 0, -bitmap.getHeight() / 2){
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                if (contains(layerXY,tapXY)){

                    Toast.makeText(getContext(),"You are here",Toast.LENGTH_LONG).show();
                }

                return false;
            }
        };
        mapView.getLayerManager().getLayers().add(marker);
    }

    private void GetRoute(double lat,double lng) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://www.mapquestapi.com/directions/v2/route?key=AefLNjc23R9LoQlfFdBWG47ubfPzLXK1&json={locations:[{latLng:{lat:"+Latitude+",lng:"+Longitude+"}},{latLng:{lat: "+lat+" ,lng:"+lng+"}}]}";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getPathFromJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RouteApi","didn't work");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private List<LatLong> getPathFromJson(String response) {
        try {
            List<LatLong> path=new ArrayList<>();
            JSONObject jsonObject=new JSONObject(response);
            JSONArray maneuversObj = jsonObject.getJSONObject("route").getJSONArray("legs")
                    .getJSONObject(0)
                    .getJSONArray("maneuvers");

            for (int i=0;i<maneuversObj.length();i++){
                JSONObject obj=maneuversObj.getJSONObject(i)
                        .getJSONObject("startPoint");
                LatLong point=new LatLong(obj.getDouble("lat"),obj.getDouble("lng") );
                path.add(point);
            }
            DrawPath(path);
            return path;

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public void DrawPath(List<LatLong> Paths){
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(15);
        paint.setStyle(Style.STROKE);

        Polyline polyline=new Polyline(paint,AndroidGraphicFactory.INSTANCE);

        List<LatLong>  coordinateList=polyline.getLatLongs();

        coordinateList.addAll(Paths);


            mapView.getLayerManager().getLayers().add(polyline);

    }


    public void drawChallengesSQLMarker(int resourceId, final ChallengeSQL challengeSQL){
        Drawable drawable = getResources().getDrawable(resourceId);

        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);

        bitmap.scaleTo(50,50);
        final LatLong latLong=new LatLong(challengeSQL.getLatitude(),challengeSQL.getLongitude());
        Marker marker = new Marker(latLong, bitmap, 0, -bitmap.getHeight() / 2){
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                if (contains(layerXY,tapXY)){
                    try {
                        addresses = geocoder.getFromLocation(latLong.latitude,latLong.longitude,1);
                        //*********open challenge
                        if (cardView.getVisibility()==View.INVISIBLE | cardView.getVisibility()==View.GONE){
                            cardView.setVisibility(View.VISIBLE);
                        }

                        _chal_owner.setText(challengeSQL.getUser_name());
                        _chal_address.setText(addresses.get(0).getAdminArea()+ " "+addresses.get(0).getLocality());

                        Glide.with(getActivity()).load(challengeSQL.getOne_path()).into(_chal_photo);

                        _chal_takeMe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GetRoute(latLong.latitude,latLong.longitude);
                            }
                        });

                        _chal_toggle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getActivity(), Challenge_details.class);
                                Bundle bundle=new Bundle();
                                bundle.putInt("challenge_id",challengeSQL.getId_challenge());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        _chal_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cardView.getVisibility()==View.VISIBLE ){
                                    cardView.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                        //******************************
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return true;
                }

                return false;
            }
        };
        mapView.getLayerManager().getLayers().add(marker);
    }
}