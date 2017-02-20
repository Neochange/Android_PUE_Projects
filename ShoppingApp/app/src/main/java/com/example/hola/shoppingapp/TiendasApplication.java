package com.example.hola.shoppingapp;

import android.app.Application;

import com.example.hola.shoppingapp.preferences.PreferencesManager;
import com.example.hola.shoppingapp.service.InMemoryTiendasService;
import com.example.hola.shoppingapp.service.InternalStorageTiendasService;
import com.example.hola.shoppingapp.service.TiendasService;

/**
 * Created by daa on 06/02/2017.
 */

public class TiendasApplication extends Application {

    private TiendasService tiendasService;
    private static TiendasApplication mInstance;
    private PreferencesManager preferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // tiendasService = new InMemoryTiendasService();
        // Dejamos de usar el InMemoryTiendasService para usar el InternalStorage en un fichero
        tiendasService = new InternalStorageTiendasService(this);
        preferencesManager = new PreferencesManager(this);
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
}
