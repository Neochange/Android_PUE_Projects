package com.example.hola.shoppingapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by daa on 20/02/2017.
 */
public class PreferencesManager {

    public final static String PREFERENCES_FILE_NAME = "shopApp_preferences";
    public final static boolean PREF_DEFAULT_VALUE = false;

    public final static String PREF_SD = "pref_SD";
    public final static String PREF_CLOUD = "pref_CLOUD";

    Context context;

    public PreferencesManager(Context c){
        this.context = c;
    }

    public boolean isExternalBackupEnabled(){
        return context.getSharedPreferences(PREFERENCES_FILE_NAME,Context.MODE_PRIVATE).
                getBoolean(PREF_SD, PREF_DEFAULT_VALUE);
    }

    public void setExternalBackupEnabled(boolean enabled){
        // Grabamos en las preferencias de la aplicación, se hace en una única transacción
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_SD, enabled);
        editor.commit();
    }


    public boolean isCloudEnabled(){
        return context.getSharedPreferences(PREFERENCES_FILE_NAME,Context.MODE_PRIVATE).
                getBoolean(PREF_CLOUD, PREF_DEFAULT_VALUE);
    }

    public void setCloudEnabled(boolean enabled){
        // Grabamos en las preferencias de la aplicación, se hace en una única transacción
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_CLOUD, enabled);
        editor.commit();
    }

}
