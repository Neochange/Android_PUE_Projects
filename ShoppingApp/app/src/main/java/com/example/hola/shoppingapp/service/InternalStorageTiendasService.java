package com.example.hola.shoppingapp.service;

import android.content.Context;
import android.util.Log;

import com.example.hola.shoppingapp.model.Tienda;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
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
    }

    /**
     * Guardamos las tiendas a un fichero
     */
    private void saveAllTiendas(){

        OutputStream os = null;
        OutputStreamWriter osw = null;
        String toWrite = "";

        try{
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

            // TODO Convertir de sb al Map



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
