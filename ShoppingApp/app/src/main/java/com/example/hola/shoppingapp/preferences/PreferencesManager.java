package com.example.hola.shoppingapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by daa on 20/02/2017.
 */
public class PreferencesManager {

    public final static String PREFERENCES_FILE_NAME = "shopApp_preferences";
    public final static boolean PREF_DEFAULT_VALUE = false;
    public final static int PREF_FREQ_DEFAULT_VALUE = 1;

    public final static String PREF_SD = "pref_SD";
    public final static String PREF_CLOUD = "pref_CLOUD";
    public final static String PREF_FREQ = "pref_DAILY_FREQ";

    public final static String PREF_BACKUP_URL = "pref_BACKUP_URL";


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

    public int getFrequency(){
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE).
                getInt(PREF_FREQ, PREF_FREQ_DEFAULT_VALUE);
    }

    public void setFreq(int freq){
        // Grabamos las prefecias de la frecuencia del backUp que viene de un radioButton
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_FREQ, freq);
        editor.commit();
    }

    public String getCloudbackupUrl(){
        // Recuperamos la url del backup
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE).
                getString(PREF_BACKUP_URL, "");
    }

    public void setCloudbackupUrl(String url){
        // Grabamos la url donde se ha guardado el backup
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_BACKUP_URL, url);
        editor.commit();
    }

}
