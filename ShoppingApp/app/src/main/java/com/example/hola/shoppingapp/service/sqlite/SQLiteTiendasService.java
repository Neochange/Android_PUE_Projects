package com.example.hola.shoppingapp.service.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hola.shoppingapp.model.Tienda;
import com.example.hola.shoppingapp.service.TiendasService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daa on 27/02/2017.
 */

public class SQLiteTiendasService implements TiendasService {

    TiendasSQLiteOpenHelper openHelper;

    public SQLiteTiendasService(Context context) {
        openHelper = new TiendasSQLiteOpenHelper(context);

        List<Tienda> tiendas = getAllTiendas();
        if( tiendas.isEmpty()){
            for(int i=0; i< 10; i++){
                Tienda t = new Tienda();
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

    @Override
    public List<Tienda> getAllTiendas() {

        List<Tienda> listTiendas= new ArrayList<>();

        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cu = db.query(openHelper.TIENDAS_TABLE_NAME,
                null, // columnas seleccionadas que nos devuelve la query null=select *
                null, // No hay where para filtrar
                null,  // No hay parametros en el where
                null, // GroupBy
                null, // Having
                null, // OrderBy
                null // Limit
        );

        if( cu.moveToFirst()){ // Se asegura que tengamos algún elemento
            do{
                listTiendas.add(getTiendaFromCursor(cu));
            }
            while(cu.moveToNext());
        }
        cu.close();
        return listTiendas;
    }

    @Override
    public Tienda getTienda(long id) {
        Tienda result = null;
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cu = db.query(openHelper.TIENDAS_TABLE_NAME,
                            null, // columnas seleccionadas que nos devuelve la query null=select *
                            TiendasSQLiteOpenHelper.COLUMN_ID+"=?",
                            new String[]{String.valueOf(id)},
                            null, // GroupBy
                            null, // Having
                            null, // OrderBy
                            null // Limit
                            );

        if( cu.moveToFirst()){ // Se asegura que tengamos algún elemento
            result= getTiendaFromCursor(cu);
        }
        cu.close();
        return result;
    }



    @Override
    public void removeTienda(long id) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        int totalDeleted = db.delete(openHelper.TIENDAS_TABLE_NAME,
                                        TiendasSQLiteOpenHelper.COLUMN_ID+"=?",
                                        new String[]{String.valueOf(id)});
        if( totalDeleted != 1){
            Log.e("SQLiteTiendasService", "Deleted "+ totalDeleted+ " tiendas");
        }
    }

    @Override
    public void saveTienda(Tienda t) {
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TiendasSQLiteOpenHelper.COLUMN_NAME, t.getNombre());
        values.put(TiendasSQLiteOpenHelper.COLUMN_RATING, t.getRating());
        values.put(TiendasSQLiteOpenHelper.COLUMN_SERVICIO, t.getService());
        values.put(TiendasSQLiteOpenHelper.COLUMN_PRICE, t.getPrize());
        values.put(TiendasSQLiteOpenHelper.COLUMN_TEL, t.getTelefono());
        values.put(TiendasSQLiteOpenHelper.COLUMN_WEB, t.getWeb());

        // TODO mirar si en la base de datos está nuestra tienda y si la encontramos actualizamos,
        // si no la encontramos creamos una nueva
        if( t.get_id() == 0){ // Nunca la he guardado
            long tienda_id = db.insert(TiendasSQLiteOpenHelper.TIENDAS_TABLE_NAME,
                                        null,
                                        values
            );

            if( tienda_id < 0){ // El insert ha fallado
                Log.e("SQLiteTiendasService", "Error inserting Tienda");
            }
            else{
                t.set_id(tienda_id);
            }
        }
        else{
            db.update(TiendasSQLiteOpenHelper.TIENDAS_TABLE_NAME,
                    values,
                    TiendasSQLiteOpenHelper.COLUMN_ID+"=?",
                    new String[]{ String.valueOf(t.get_id())});
        }

    }

    // Para probar de hacer una consulta con un transacción
    public void deleteAllTiendas(){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.beginTransaction();
        int totalDeleted = db.delete(TiendasSQLiteOpenHelper.TIENDAS_TABLE_NAME,null,null);

        // Comprobamos que no queda ninguna
        Cursor total = db.query(TiendasSQLiteOpenHelper.TIENDAS_TABLE_NAME, null,null,null,null,null,null);
        if( total.getCount() == 0) {
            db.setTransactionSuccessful(); // Si se ha marcado como successful se guardan los datos
                                           // Si no entiende que ha ido mal y ejecutaria un rollback
        }
        db.endTransaction();
    }

    private Tienda getTiendaFromCursor(Cursor c){
        long id = c.getLong(c.getColumnIndex(TiendasSQLiteOpenHelper.COLUMN_ID));
        String name = c.getString(c.getColumnIndex(TiendasSQLiteOpenHelper.COLUMN_NAME));
        int price = c.getInt(c.getColumnIndex(TiendasSQLiteOpenHelper.COLUMN_PRICE));
        int servicio = c.getInt(c.getColumnIndex(TiendasSQLiteOpenHelper.COLUMN_SERVICIO));
        int rating= c.getInt(c.getColumnIndex(TiendasSQLiteOpenHelper.COLUMN_RATING));
        String web= c.getString(c.getColumnIndex(TiendasSQLiteOpenHelper.COLUMN_WEB));
        String tel= c.getString(c.getColumnIndex(TiendasSQLiteOpenHelper.COLUMN_TEL));

        Tienda result = new Tienda(id, name, rating, servicio, price, web, tel);
        return result;
    }
}
