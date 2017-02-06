package com.example.hola.shoppingapp;

import android.app.Application;

import com.example.hola.shoppingapp.service.InMemoryTiendasService;
import com.example.hola.shoppingapp.service.TiendasService;

/**
 * Created by daa on 06/02/2017.
 */

public class TiendasApplication extends Application {

    private TiendasService tiendasService;
    private static TiendasApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        tiendasService = new InMemoryTiendasService();
    }

    public static TiendasApplication getInstance(){
        return mInstance;
    }

    public TiendasService getTiendasService(){
        return tiendasService;
    }
}
