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
    public static final int DB_VERSION = 1;

    public static final String TIENDAS_TABLE_NAME="tiendas",
    COLUMN_ID = "_id",
    COLUMN_NAME = "name",
    COLUMN_PRICE="precio",
    COLUMN_SERVICIO="servicio",
    COLUMN_RATING="rating",
    COLUMN_WEB="web",
    COLUMN_TEL="tel";



    // Decide si crea la base de datos, si la version es inferior a la existente la actualizará...
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
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("TiendasSQLiteOpenHelper", "Updating database from version " + oldVersion+ " to " +
        newVersion);
    }
}
