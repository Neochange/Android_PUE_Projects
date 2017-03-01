package com.example.hola.shoppingapp;

import android.app.Application;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.hola.shoppingapp.preferences.PreferencesManager;
import com.example.hola.shoppingapp.service.InMemoryTiendasService;
import com.example.hola.shoppingapp.service.InternalStorageTiendasService;
import com.example.hola.shoppingapp.service.TiendasService;
import com.example.hola.shoppingapp.service.backup.BackupIntentService;
import com.example.hola.shoppingapp.service.backup.BackupService;
import com.example.hola.shoppingapp.service.backup.CloudBackupService;
import com.example.hola.shoppingapp.service.sqlite.SQLiteTiendasService;

/**
 * Created by daa on 06/02/2017.
 */

public class TiendasApplication extends Application {

    private TiendasService tiendasService;


    private static TiendasApplication mInstance;
    private PreferencesManager preferencesManager;
    private CloudBackupService backupservice;
    private BroadcastReceiver shopnearNotificationBroadcast;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // tiendasService = new InMemoryTiendasService();
        // Dejamos de usar el InMemoryTiendasService para usar el InternalStorage en un fichero
        // Dejamos de usar el InternalStorageTiendasService para usar SQLite
        tiendasService = new SQLiteTiendasService(this);
        preferencesManager = new PreferencesManager(this);

        backupservice = new CloudBackupService(this);

        // TODO leer el último backup y decidir si hay que hacerlo de nuevo
        boolean autobackup = true;
        if( autobackup ){
            Intent i = new Intent(this, BackupIntentService.class);
            startService(i);
        }

        // Iniciamos el service que monitoriza la posición GPS
        Intent i = new Intent(this, LocationUpdateService.class);
        startService(i);


        shopnearNotificationBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long tiendas_ids[] = intent.getExtras().getLongArray("Shops_near_extra");

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(android.R.drawable.ic_menu_compass);
                mBuilder.setContentTitle("Shops around!");
                mBuilder.setContentText("There are "+ tiendas_ids.length + "shops near");

                NotificationManager ntm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                ntm.notify(201, mBuilder.build());
            }
        };
        // Registramos un broadcast receiver cuando se encuentre una tienda cerca
        LocalBroadcastManager.getInstance(this).registerReceiver(
                shopnearNotificationBroadcast,
                new IntentFilter(LocationUpdateService.SHOP_NEAR_BROADCAST)
        );

    }

    @Override
    public void onTerminate() {
        // Paramos el service que está obteniendo la localización
        Intent i = new Intent(this, LocationUpdateService.class);
        stopService(i);

        // Quitamos la suscripción del broadcast receiver para que deje de recibirlo
        LocalBroadcastManager.getInstance(this).unregisterReceiver(shopnearNotificationBroadcast);

        super.onTerminate();
    }

    public static TiendasApplication getInstance(){
        return mInstance;
    }

    public TiendasService getTiendasService(){
        return tiendasService;
    }

    public PreferencesManager getPreferencesManager(){
        return preferencesManager;
    }

    public BackupService getBackupService(){ return backupservice;}
}
