package com.example.hola.shoppingapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.hola.shoppingapp.model.Tienda;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.RunnableFuture;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ServiceConnection locServiceConnection;
    private LocationUpdateService.LocationServiceBinder locBinder;
    private boolean stopThread;
    private Marker myPositionMarker;

    // Cuando la activity de mapas pasa a segundo plano me desconecto del Service
    @Override
    protected void onPause() {
        super.onPause();
        unbindService(locServiceConnection);
        stopThread = true;
    }

    // Creamos una conexión con el Binder del Service para obtener la posición GPS.
    // Cuando se desconecta esa conexión será null
    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(this, LocationUpdateService.class);
        bindService(i,locServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                locBinder=(LocationUpdateService.LocationServiceBinder)service;
                stopThread = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(!stopThread){
                            final Location current = locBinder.getBestCurrentLocation();
                            if(current != null) {
                                final LatLng myLocation = new LatLng(current.getLatitude(), current.getLongitude());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(myPositionMarker!=null) myPositionMarker.remove();
                                        myPositionMarker = mMap.addMarker(new MarkerOptions()
                                                .position(myLocation)
                                                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.arrow_down_float))
                                                .title("It's a me!")
                                        );
                                    }
                                });

                            }
                            try{ Thread.sleep(20000); } catch (InterruptedException e){}
                        }
                    }
                }).start();


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                locBinder = null;
            }
        }, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("MapsActivity", "Map is ready");
        mMap = googleMap;

        List<Tienda> tiendas = TiendasApplication.getInstance().getTiendasService().getAllTiendas();
        final LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        for( Tienda t : tiendas){
            if(t.getLatitude()!=0 && t.getLongitude()!=0){
                LatLng tiendasLocation = new LatLng(t.getLatitude(), t.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(tiendasLocation).title(t.getNombre());
                mMap.addMarker(marker);
                bounds.include(tiendasLocation);
            }
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback(){
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),250));
            }
        });
    }
}
