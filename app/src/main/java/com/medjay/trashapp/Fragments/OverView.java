package com.medjay.trashapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.medjay.trashapp.Network.RetrofitBuilder;
import com.medjay.trashapp.Network.WebServerIntf;
import com.medjay.trashapp.R;
import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.Client;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class OverView extends Fragment {

    private MapView mapView;
    private TileCache tileCache;
    private SharedPreferences preferences;
    private float Latitude;
    private float Longitude;

    Challenge challenge;

    private List<Address> addresses;
    private Geocoder geocoder;

    Button participate;
Client client;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AndroidGraphicFactory.createInstance(this.getActivity().getApplication());
        view= inflater.inflate(R.layout.fragment_over_view, container, false);

        final RelativeLayout coordinatorLayout=view.findViewById(R.id.coordinatorLayout);

        Gson gson = new Gson();
        final String json = getActivity().getSharedPreferences("prefs",0).getString("client", null);
        client= gson.fromJson(json, Client.class);

        preferences=getActivity().getSharedPreferences("prefs",0);
        challenge=(Challenge)getArguments().getSerializable("challenge");
        geocoder=new Geocoder(getContext(), Locale.getDefault());
        mapView=view.findViewById(R.id.mapView);

        TextView chal_address = (TextView) view.findViewById(R.id.chal_address);
        try {
            addresses = geocoder.getFromLocation(challenge.getLatitude(),challenge.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        chal_address.setText(addresses.get(0).getAdminArea()+ " "+addresses.get(0).getLocality());

        TextView chal_owner= (TextView) view.findViewById(R.id.chal_owner);
        chal_owner.setText(challenge.getOwner().getUser_name());

        TextView chal_date= (TextView) view.findViewById(R.id.chal_date);
        chal_date.setText(challenge.getStarting_date()+"");

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

        participate=view.findViewById(R.id.participate);
        participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(json==null){
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "You must login", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }else {
                    participateInChallenge();
                }

            }
        });

        return view;

    }

    private void participateInChallenge() {
        final WebServerIntf webServerIntf=RetrofitBuilder.getRetrofitInstance().create(WebServerIntf.class);
        Call<String> call=webServerIntf.participate(client.getId_user(),challenge.getId_challenge());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()==null){
                    Toast.makeText(getContext(),"already participated",Toast.LENGTH_SHORT).show();
                    final Button non_participate= (Button) view.findViewById(R.id.non_participate);
                    non_participate.setVisibility(View.VISIBLE);
                    non_participate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Call<String>  stringCall=webServerIntf.DeleteParticipents(challenge.getId_challenge(),client.getId_user());
                            stringCall.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(getContext(),"participation canceled ",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    non_participate.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                }else {

                    Toast.makeText(getContext(),"Nice!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage()+"ok",Toast.LENGTH_SHORT).show();
            }
        });
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
                return true;
            }
        };

        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
        mapView.getLayerManager().getLayers().add(tileRendererLayer);

        Latitude=preferences.getFloat("Latitude",0.0f);
        Longitude=preferences.getFloat("Longitude",0.0f);

        LatLong latLong=new LatLong(Latitude, Longitude);
        drawMyPositionMarker(R.drawable.ic_my_location_black_24dp,latLong);
        mapView.setZoomLevel((byte) 15);

        LatLong latLongChallenge=new LatLong(challenge.getLatitude(), challenge.getLongitude());
        mapView.setCenter(latLongChallenge);
        drawChallengePosition(R.drawable.ic_location_on_black_24dp, latLongChallenge);

    }

    private void drawChallengePosition(int resourceId, LatLong latLongChallenge) {
        Drawable drawable = getResources().getDrawable(resourceId);

        org.mapsforge.core.graphics.Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);

        bitmap.scaleTo(50,50);
        Marker marker = new Marker(latLongChallenge, bitmap, 0, -bitmap.getHeight() / 2){
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                if (contains(layerXY,tapXY)){

                    Toast.makeText(getActivity(),"Challenge here",Toast.LENGTH_LONG).show();
                }

                return false;
            }
        };
        mapView.getLayerManager().getLayers().add(marker);
    }


    @Override
    public void onDetach() {
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDetach();
    }

    public void drawMyPositionMarker(int resourceId, final LatLong geoPoint){
        Drawable drawable = getResources().getDrawable(resourceId);

        org.mapsforge.core.graphics.Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);

        bitmap.scaleTo(50,50);
        Marker marker = new Marker(geoPoint, bitmap, 0, -bitmap.getHeight() / 2){
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                if (contains(layerXY,tapXY)){

                    Toast.makeText(getActivity(),"You are here",Toast.LENGTH_LONG).show();
                }

                return false;
            }
        };
        mapView.getLayerManager().getLayers().add(marker);
    }


}
