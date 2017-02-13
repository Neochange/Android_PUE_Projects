package com.example.hola.shoppingapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class EditTienda extends AppCompatActivity {

    public static final String TIENDA_ID_EXTRA_KEY="tienda_id";
    private long tienda_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tienda);

        // Cogemos el extra que nos viene del intent
        tienda_id = getIntent().getLongExtra(TIENDA_ID_EXTRA_KEY,0);
        if(tienda_id==0){
            // TODO error
        }
        else{
            Log.i("EditTiendaTag", "Se va a editar la tienda"+ tienda_id);
        }

    }

}
