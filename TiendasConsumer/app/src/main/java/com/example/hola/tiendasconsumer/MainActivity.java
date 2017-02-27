package com.example.hola.tiendasconsumer;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = (TextView) findViewById(R.id.list_tiendas);

        Cursor c = getContentResolver().query(
                Uri.parse("content://com.example.hola.shoppingapp/tiendas"),
                null,
                null,
                null,
                null
        );

        String result = "no tiendas";
        if(c.moveToFirst()){
            result = "";
            do{
                // Este nombre de columna es el mismo que hemos puesto al configurar la base de
                // datos SQLite
                result += c.getString(c.getColumnIndex("name")) + ", ";
            }while(c.moveToNext());
            result = result.substring(0,result.length()-2);
        }
        text.setText(result);


    }
}
