package com.example.hola.shoppingapp;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.hola.shoppingapp.model.Tienda;
import com.example.hola.shoppingapp.service.backup.BackupIntentService;

import java.util.ArrayList;
import java.util.List;

public class LocationUpdateService extends Service {

    Location lastLocation;
    DecomprasLocationListener listener;
    public static final int NOTIFICATION_ID = 1100011;
    public static final String SHOP_NEAR_BROADCAST = "shopping.radius";

    public LocationUpdateService() {
        listener = new DecomprasLocationListener();
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startLocationUpdates();
        // Con estas constantes le decimos al sistema que tiene que hacer con este service
        // si ha tenido que cerrarlo por recursos, pero ahora vuelve a tener esos recursos.
        return Service.START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // A partir de la versión 6 hay que programar la callback
    private void startLocationUpdates() {

        Log.i("LocationService", "Starting location updates");
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/

        try {
            lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 20, listener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 20, listener);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(android.R.drawable.ic_menu_compass);
            mBuilder.setContentTitle("Shopping App is taking your location");
            mBuilder.setContentText("GPS position is being taken to sell it");

            NotificationManager ntm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            ntm.notify(NOTIFICATION_ID, mBuilder.build());
        }
        catch (SecurityException e){
            Toast.makeText(this, "Error: location permissions missing", Toast.LENGTH_LONG).show();
        }

    }

    private void stopLocationUpdates(){
        Log.i("LocationService", "Stopping location updates");
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            manager.removeUpdates(listener);
            // Borramos la notificación
            NotificationManager ntm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            ntm.cancel(NOTIFICATION_ID);

        }catch (SecurityException e){
            Toast.makeText(this, "Error: location permissions missing", Toast.LENGTH_LONG).show();
        }
    }

    private void checkShopsNear(){
        Log.i("checkShopsNear", "Check if there are shops near of me");
        List<Tienda> tiendas = TiendasApplication.getInstance().getTiendasService().getAllTiendas();
        List<Long> tiendasNearIds = new ArrayList<>();
        for( Tienda t:tiendas){
            Location tiendaLoc = new Location("");
            tiendaLoc.setLatitude(t.getLatitude());
            tiendaLoc.setLongitude(t.getLongitude());
            Log.i("checkShopsNear", "Comparing " + lastLocation.getLatitude() + " " + lastLocation.getLongitude() +" with " + tiendaLoc.getLatitude() + " " + tiendaLoc.getLongitude() );
            Log.i("checkShopsNear", "Distance: " + lastLocation.distanceTo(tiendaLoc));
            if( lastLocation.distanceTo(tiendaLoc) < 200000){
                Log.i("checkShopsNear", "You are close to " + t.getNombre());
                tiendasNearIds.add(t.get_id());
            }
        }

        if( !tiendasNearIds.isEmpty()){
            Log.i("checkShopsNear", "There are" + tiendasNearIds.size() + " shops around");
            Intent i = new Intent(SHOP_NEAR_BROADCAST);
            long[] lista = new long[tiendasNearIds.size()];
            int j=0;
            for(long id:tiendasNearIds){
                lista[j] = id;
                j++;
            }
            i.putExtra("Shops_near_extra",lista);
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
    }

    // Creamos una clase para gestionar los cambios de GPS
    // Este listener se lo pasamos al LocationManager para que se ejecute con los eventos de localización
    private class DecomprasLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {

            Log.i("LocationService", "Received location: " + location.getLatitude()+ " "+ location.getLongitude());
            // Gestionamos el dato de GPS pero tenemos que hacer una gestión para eliminar muestras
            // Con mucho error o antiguas
            if( lastLocation == null){
                lastLocation = location;
            }
            else{
                if(location.getAccuracy() < lastLocation.getAccuracy()){
                    lastLocation = location;
                }
                else if(location.getAccuracy()< 20){
                    lastLocation= location;
                }
                else if(lastLocation.getTime()+30*1000 < System.currentTimeMillis()){
                    lastLocation= location;
                }
            }
            checkShopsNear();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
