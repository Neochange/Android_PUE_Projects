package com.example.hola.shoppingapp.service.backup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.hola.shoppingapp.TiendasApplication;
import com.example.hola.shoppingapp.model.Tienda;
import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * Created by daa on 22/02/2017.
 */

public class CloudBackupService implements BackupService {

    public static final String HANDLER_BUNDLE_STATUS = "handler_message";
    private static final String DEFAULT_BACKUP_URL ="https://api.myjson.com/bins" ;

    private Context context;
    public CloudBackupService(Context c){
        context = c;
    }

    private boolean hasConnectivity(){
        // Comprobamos si el dispositivo tiene alguna conexión activa
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if( info.isConnected()) return true;
        return false;
    }

    @Override
    public boolean isBackupReady() {
        return hasConnectivity();
    }

    @Override
    public void doBackup(Handler uiHandler) throws TiendasBackupException {

        // El uiHandler es en realidad una pasarela al thread de UI
        //uiHandler.sendMessage(createMessage("Step " + i));

        String url = TiendasApplication.getInstance().getPreferencesManager().getCloudbackupUrl();
        String method = "PUT"; // Update web service data
        if( url.isEmpty()){
            method = "POST";
            url =DEFAULT_BACKUP_URL;
        }

        Log.i("CloudBackup", "URL: " +url);

        uiHandler.sendMessage(createMessage("Preparing connection"));
        URL u = null;
        try{
            u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type","application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(30000);
            connection.setDoInput(true); // la conexión es para leer un resultado en el body
            connection.setDoOutput(true); // la conexión es para escribir datos en el body

            uiHandler.sendMessage(createMessage("Converting tiendas to service"));
            // Pasamos las tiendas a json y las escribiremos en el stream de salida
            Gson gson = new Gson();
            String json = gson.toJson(TiendasApplication.getInstance().getTiendasService().getAllTiendas());

            uiHandler.sendMessage(createMessage("Connecting"));
            connection.connect();

            uiHandler.sendMessage(createMessage("Sending JSON"));
            OutputStreamWriter out  = new OutputStreamWriter(connection.getOutputStream());
            out.write(json);
            out.flush();

            int responseCode = connection.getResponseCode();
            if(responseCode == 201 || responseCode == 200){

                // Leemos la URL que nos devuelve el web service como URL donde está nuestro backup
                uiHandler.sendMessage(createMessage("backup performed OK"));
                uiHandler.sendMessage(createMessage(String.valueOf(responseCode)));
                if( responseCode == 201){
                    JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
                    reader.beginObject();
                    if( reader.hasNext()){
                        reader.nextName();
                        String backupURI = reader.nextString();
                        Log.i("CloudBackup", "The URI is " + backupURI);
                        uiHandler.sendMessage(createMessage(backupURI));
                        TiendasApplication.getInstance().getPreferencesManager().setCloudbackupUrl(backupURI);
                    }
                    reader.close();
                }
            }
            else{
                Log.e("CloudBackup", "Response code is not OK "+ responseCode);
            }

        } catch (MalformedURLException e) {
            Log.e("CloudBackup", "URL no valida "+url,e);
        } catch (IOException e) {
            Log.e("CloudBackup", "Error escribiendo o leyendo",e);
        }

    }

    @Override
    public boolean isRestoreReady() {
        // TODO comprobar si se ha creado un backup anteriormente
        return hasConnectivity();
    }

    @Override
    public void doRestore(Handler uiHandler) throws TiendasBackupException {

        String url = TiendasApplication.getInstance().getPreferencesManager().getCloudbackupUrl();
        URL u=null;
        try{
            u=new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoOutput(false);
            connection.connect();

            int responseCode = connection.getResponseCode();
            switch (responseCode){
                case 200:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line=reader.readLine())!=null){
                        sb.append(line);
                    }
                    reader.close();
                    Gson gson = new Gson();
                    // TODO borrar las tiendas actuales y cargar las que hemos recibido del server
                    List<Tienda> tiendas = gson.fromJson(sb.toString(),new TypeToken<List<Tienda>>(){}.getType());
                    

                    break;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Message createMessage(String message){
        Message result = new Message();
        Bundle data = new Bundle();
        data.putString(HANDLER_BUNDLE_STATUS, message);
        result.setData(data);
        return result;
    }
}
