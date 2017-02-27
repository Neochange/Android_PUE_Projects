package com.example.hola.shoppingapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.hola.shoppingapp.service.sqlite.TiendasSQLiteOpenHelper;

public class TiendasContentProvider extends ContentProvider {

    // Guardamos el authorities que tengo en el Manifest para este ContentProvider
    // Podriamos ponerlo como una constante @String y cogerlo de las constantes de la app
    public static final String PROVIDER_NAME = "com.example.hola.shoppingapp";
    public static final int MATCH_ALL_TIENDAS=1;
    public static final int MATCH_ONE_TIENDA=2;

    TiendasSQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    UriMatcher uriMatcher;

    public TiendasContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        boolean result = false;

        // Creamos las URI del Content provider
        // Esto nos permitirá definir diferentes acciones que se pueden hacer en nuestro Content Provider
        // Es parecido al urls de django para comprobar si estás llamando a una función
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "tiendas", MATCH_ALL_TIENDAS);
        uriMatcher.addURI(PROVIDER_NAME, "tiendas/#", MATCH_ALL_TIENDAS);

        openHelper = new TiendasSQLiteOpenHelper(getContext());
        db = openHelper.getWritableDatabase();
        // Si puedo obtener una base de datos para escribir es que ha funcionado
        if( db != null) result = true;

        return result;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Log.i("TiendasContentProvider", "Querying");
        switch(uriMatcher.match(uri)){
            case MATCH_ALL_TIENDAS:
                return db.query(TiendasSQLiteOpenHelper.TIENDAS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

            case MATCH_ONE_TIENDA:
                return db.query(TiendasSQLiteOpenHelper.TIENDAS_TABLE_NAME,
                        projection,
                        TiendasSQLiteOpenHelper.COLUMN_ID+"=?", // Obtenemos el final de la URI que será un ID
                        new String[]{ uri.getLastPathSegment()},
                        null, null, null);

        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
