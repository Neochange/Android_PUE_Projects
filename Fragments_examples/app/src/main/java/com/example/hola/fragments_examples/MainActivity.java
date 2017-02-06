package com.example.hola.fragments_examples;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

// Implementamos la functión de OnFragment1InteractionListener para capturar el click dentro del
// fragment
public class MainActivity extends AppCompatActivity implements FragmentBoton1.OnFragment1InteractionListener, FragmentBoton2.OnFragment2InteractionListener {

    FrameLayout main;
    FrameLayout extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // En función de las características del dispositivo cogerá un layout o otro
        // debajo de la carpeta de layout activity_main tenemos los dos templates
        setContentView(R.layout.activity_main);

        // usamos R.id para encontrar los elementos por id
        main= (FrameLayout) findViewById(R.id.main_fragment);
        extra= (FrameLayout) findViewById(R.id.extra_fragment);

        // Si extra es null significa que estamos en portrait, donde solo hay un fragment,
        // sino, es que estamos en landscape y hay que rellenar los 2
        FragmentManager fm = getSupportFragmentManager();

        // Iniciamos una transacción para que se lleven a cabo todos los cambios
        FragmentTransaction ftrans = fm.beginTransaction();
        ftrans.add(R.id.main_fragment,new FragmentBoton1());
        if( extra != null){
            ftrans.add(R.id.extra_fragment, new FragmentBoton2());
        }
        ftrans.commit();


    }

    @Override
    public void onButton1Clicked() {
        Log.i("MainActivity", "Se ha clicado el Botón 1 en el fragment 1");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ftrans = fm.beginTransaction();

        if( extra == null){ // Si estamos en portrait carga el fragment del botón 2
            ftrans.add(R.id.main_fragment, new FragmentBoton2());
        }
        else{ // Si estamos en landscape, cargamos el fragment del botón 3 en el extra_fragment
            ftrans.add(R.id.extra_fragment,new FragmentBoton3());
        }
        ftrans.commit();

    }

    @Override
    public void onButton2Clicked() {
        Log.i("MainActivity", "Se ha clicado el Botón 2 en el fragment 1");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ftrans = fm.beginTransaction();

        if( extra == null){ // Si estamos en portrait carga el fragment del botón 3
            ftrans.add(R.id.main_fragment, new FragmentBoton3());
        }
        else{ // Si estamos en landscape, cargamos el fragment del botón 3 en el extra_fragment
            ftrans.add(R.id.extra_fragment,new FragmentBoton3());
        }
        ftrans.commit();
    }
}
