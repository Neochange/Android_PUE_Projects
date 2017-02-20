package com.example.hola.shoppingapp.service;

import android.content.Context;
import android.util.Log;

import com.example.hola.shoppingapp.model.Tienda;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daa on 20/02/2017.
 */

public class InternalStorageTiendasService implements TiendasService {

    public static final String FILE_NAME ="tiendas_file";

    Map<Long,Tienda> tiendas;
    Context context;

    public InternalStorageTiendasService(Context context){
        this.context = context;
        // Cargamos todas las tiendas desde un fichero
        loadAllTiendas();

        // Si es la primera vez y no tenemos aún fichero, inicializamos la variable tiendas
        // La siguiente vez el fichero existirá y ya no será necesario este paso
        if(tiendas == null){ tiendas = new LinkedHashMap<>(); }

        if( tiendas.isEmpty()){
            Tienda t = null;
            for(int i=0; i< 50; i++){
                t = new Tienda();
                t.set_id(getNextId());
                t.setNombre("Tienda " + i);
                t.setRating(i%5);
                t.setService(7%10);
                t.setTelefono("1236564654");
                t.setPrize((i+30)%10);
                t.setWeb("http://www.google.es");
                saveTienda(t);
            }
        }
    }

    /**
     * Guardamos las tiendas a un fichero
     */
    private void saveAllTiendas(){

        OutputStream os = null;
        OutputStreamWriter osw = null;
        String toWrite = "";
        Gson gson = new Gson();

        try{
            // Convertimos de tiendas a json
            toWrite = gson.toJson(tiendas);

            // Necesitabamos el context para poder usar openFileOutput, así que lo pedimos en el
            // constructor
            os = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(os);
            osw.write(toWrite);
        }
        catch (FileNotFoundException e) {
            Log.e("InternalStorage","Error file not found", e);
        } catch (IOException e) {
            Log.e("InternalStorage","Error writing to file", e);
        }finally {
            if(osw!=null){
                try {
                    osw.close();
                } catch (IOException e) {
                    Log.e("InternalStorage","Error closing OutputStreamWriter", e);
                }
            }
        }

    }

    /**
     * Cargamos las tiendas a una variable en memoria desde un fichero
     */
    private void loadAllTiendas(){
        BufferedReader reader = null;
        InputStream is = null;
        InputStreamReader isReader = null;

        try{
            is = context.openFileInput(FILE_NAME);
            isReader = new InputStreamReader(is);
            reader = new BufferedReader(isReader);

            StringBuilder sb = new StringBuilder();
            String temp="";

            while((temp=reader.readLine())!=null){
                sb.append(temp);
            }

            // Convertir de sb(String Buffer) al Map
            Gson gson = new Gson();
            tiendas = gson.fromJson(sb.toString(), new TypeToken<Map<Long, Tienda>>(){}.getType());


        } catch (FileNotFoundException e) {
            Log.e("InternalStorage","Error file not found", e);
        } catch (IOException e) {
            Log.e("InternalStorage","Error reading the file", e);
        }finally{
            if(reader!=null){
                try{
                    reader.close();
                } catch (IOException e) {
                    Log.e("InternalStorage","Error closing the InputStream", e);
                }
            }
        }

    }

    @Override
    public List<Tienda> getAllTiendas() {
        return new ArrayList<>(tiendas.values());
    }

    @Override
    public Tienda getTienda(long id) {
        return tiendas.get(id);
    }

    @Override
    public void removeTienda(long id) {
        tiendas.remove(id);
        saveAllTiendas();
    }

    @Override
    public void saveTienda(Tienda t) {
        if( t.get_id() == 0){
            t.set_id(getNextId());
        }
        tiendas.put(t.get_id(), t);
        saveAllTiendas();
    }

    private long lastId = 0L;
    private synchronized long getNextId(){
        return ++lastId;
    }
}
