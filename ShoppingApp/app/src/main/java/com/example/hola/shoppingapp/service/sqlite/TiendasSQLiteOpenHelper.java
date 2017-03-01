package com.example.hola.shoppingapp.service.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by daa on 27/02/2017.
 */

public class TiendasSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Tiendas_DB";
    public static final int DB_VERSION = 2;

    public static final String TIENDAS_TABLE_NAME="tiendas",
    COLUMN_ID = "_id",
    COLUMN_NAME = "name",
    COLUMN_PRICE="precio",
    COLUMN_SERVICIO="servicio",
    COLUMN_RATING="rating",
    COLUMN_WEB="web",
    COLUMN_TEL="tel",
    COLUMN_LATITUDE="longitude",
    COLUMN_LONGITUDE="latitude";


    // Decide si crea la base de datos, si la version es superior a la existente la actualizará...
    public TiendasSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TiendasSQLiteOpenHelper", "Creating database");
        db.execSQL("CREATE TABLE " + TIENDAS_TABLE_NAME + " (" +
            COLUMN_ID + " integer primary key autoincrement, "+
                COLUMN_NAME + " text,"+
                COLUMN_PRICE + " integer,"+
                COLUMN_RATING + " integer,"+
                COLUMN_SERVICIO + " integer,"+
                COLUMN_WEB + " text,"+
                COLUMN_TEL + " text" +
                COLUMN_LATITUDE + " real," + // añadimos los dos campos en el oncreate
                COLUMN_LONGITUDE + " real " + // por si no hay bd que la cree bien desde el inicio
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("TiendasSQLiteOpenHelper", "Updating database from version " + oldVersion+ " to " + newVersion);

        // Actualizamos la base de datos para crear dos nuevos campos
        // Cambiamos la version en la constante DB_VERSION
        if( oldVersion <=1){
            db.execSQL("ALTER TABLE " + TIENDAS_TABLE_NAME+ " ADD "+ COLUMN_LATITUDE+" real");
            db.execSQL("ALTER TABLE " + TIENDAS_TABLE_NAME+ " ADD "+ COLUMN_LONGITUDE+" real");
        }

    }
}
