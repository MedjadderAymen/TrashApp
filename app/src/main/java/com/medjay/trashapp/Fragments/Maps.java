package com.medjay.trashapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medjay.trashapp.R;

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
    private TextView _chal_toggle;
    private TileCache tileCache;

    private CardView cardView;
    private TextView _chal_address,_chal_takeMe;

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
        drawMarker(R.drawable.ic_my_location_black_24dp,latLong);
        mapView.setZoomLevel((byte) 19);
    }

    @Override
    public void onDestroy() {
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }

    public void drawMarker(int resourceId, final LatLong geoPoint){
        Drawable drawable = getResources().getDrawable(resourceId);

        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);

        bitmap.scaleTo(50,50);
        Marker marker = new Marker(geoPoint, bitmap, 0, -bitmap.getHeight() / 2){
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                if (contains(layerXY,tapXY)){
                    try {
                        addresses = geocoder.getFromLocation(geoPoint.latitude,geoPoint.longitude,1);
                        Toast.makeText(getContext()," Country "+addresses.get(0).getCountryName()+
                                " Wilaya "+addresses.get(0).getAdminArea()+
                                " Belediya "+addresses.get(0).getLocality()+
                                " Address "+addresses.get(0).getAddressLine(0)
                                ,Toast.LENGTH_LONG).show();
                        //*********open challenge
                        if (cardView.getVisibility()==View.INVISIBLE | cardView.getVisibility()==View.GONE){
                            cardView.setVisibility(View.VISIBLE);
                        }
                        _chal_address.setText(addresses.get(0).getAdminArea()+ " "+addresses.get(0).getLocality());

                        _chal_takeMe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                    GetRoute(geoPoint.latitude,geoPoint.longitude);
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

    private void GetRoute(double lat,double lng) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://www.mapquestapi.com/directions/v2/route?key=deamSBfbxULjOkFvP9dW1QiAKewVYxVg&json={locations:[{latLng:{lat:"+Latitude+",lng:"+Longitude+"}},{latLng:{lat: "+lat+" ,lng:"+lng+"}}]}";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
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
        paint.setColor(Color.RED);
        paint.setStrokeWidth(15);
        paint.setStyle(Style.STROKE);

        Polyline polyline=new Polyline(paint,AndroidGraphicFactory.INSTANCE);

        List<LatLong>  coordinateList=polyline.getLatLongs();

        coordinateList.addAll(Paths);


            mapView.getLayerManager().getLayers().add(polyline);

    }


}