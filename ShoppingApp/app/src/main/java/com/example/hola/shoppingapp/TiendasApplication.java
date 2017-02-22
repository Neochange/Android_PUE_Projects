package com.example.hola.shoppingapp;

import android.app.Application;
import android.content.Intent;

import com.example.hola.shoppingapp.preferences.PreferencesManager;
import com.example.hola.shoppingapp.service.InMemoryTiendasService;
import com.example.hola.shoppingapp.service.InternalStorageTiendasService;
import com.example.hola.shoppingapp.service.TiendasService;
import com.example.hola.shoppingapp.service.backup.BackupIntentService;
import com.example.hola.shoppingapp.service.backup.BackupService;
import com.example.hola.shoppingapp.service.backup.CloudBackupService;

/**
 * Created by daa on 06/02/2017.
 */

public class TiendasApplication extends Application {

    private TiendasService tiendasService;
    private static TiendasApplication mInstance;
    private PreferencesManager preferencesManager;
    private CloudBackupService backupservice;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // tiendasService = new InMemoryTiendasService();
        // Dejamos de usar el InMemoryTiendasService para usar el InternalStorage en un fichero
        tiendasService = new InternalStorageTiendasService(this);
        preferencesManager = new PreferencesManager(this);

        backupservice = new CloudBackupService(this);

        // TODO leer el último backup y decidir si hay que hacerlo de nuevo
        boolean autobackup = true;
        if( autobackup ){
            Intent i = new Intent(this, BackupIntentService.class);
            startService(i);
        }
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
