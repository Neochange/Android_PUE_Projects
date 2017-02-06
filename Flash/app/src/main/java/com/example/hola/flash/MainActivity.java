package com.example.hola.flash;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private ToggleButton mToggleButton;
    private Camera mCamera; // para usar la cámara hay que pedir los permisos de la aplicación

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean hasFlash = hasFlash();
        if(!hasFlash){
            AlertDialog alert= new AlertDialog.Builder(this).create();
            alert.setTitle("No tienes flash!!");
            alert.setMessage("Esta app no funciona sin flash");
            // Configuro que tiene que hacer la aplicación cuando alguien apreta OK en el botón del alert
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Pide al sistema que quiere cerrar la Activity
                    finish();
                }
            });
        }

        mToggleButton = (ToggleButton) findViewById(R.id.enciendeApagaLuz);

        // Podemos usar un evento de cuando el boton cambia de valor, que nos da un boolean
        // O podemos coger el evento sencillo OnClickListener y mirar el estado
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    turnOnFlash();
                }
                else{
                    turnOffFlash();
                }
            }
        });

        /*mToggleButton.setOnClickListener(new OnClickListener() {
                                             @Override
                                             public void onClick(View v) {

                                             }
                                         }*/

    }

    private boolean hasFlash(){
        // getApplicationContext -> Contexto en el que se está ejecutando la aplicación
        // getPackagemanager -> Libreria que controla que caracteristicas tiene el dispositivo, que permisos...
        return (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH));
    }

    private void turnOnFlash(){
        Camera.Parameters params=mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(params);
        mCamera.startPreview();
    }

    private void turnOffFlash(){
        Camera.Parameters params=mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(params);
        mCamera.stopPreview();
    }

    // Después de crearse la Activity se ejecuta esta función para pasar al estado onStart
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LINTERNA", "On the OnStart");

        if( mCamera==null){
            mCamera = Camera.open();
        }
    }

    // Onpause se ejecuta cuando la app deja de estar parcialmente visible
    // El evento de OnPause se ejecuta siempre, el sistema te garantiza que lo ejecuta
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LINTERNA", "On the onPause");
    }

    // Cuando del estado OnPause volvemos a estar visibles se ejecuta OnResume
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LINTERNA", "On the onResume");
    }

    // El evento OnStop se ejecuta si el sistema libera recursos, pero no garantiza que
    // te ejecutará la callback
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LINTERNA", "On the onStop");

        if( mCamera != null){
            mCamera.release();
            mCamera=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LINTERNA", "On the onDestroy");
    }





}
