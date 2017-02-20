package com.example.hola.shoppingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class MenuFrecuencia extends AppCompatActivity {

    CheckBox sd_backup;
    CheckBox cloud_backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_frecuencia);

        sd_backup = (CheckBox) findViewById(R.id.sd_checkbox);
        sd_backup.setChecked(
                TiendasApplication.getInstance().getPreferencesManager().isExternalBackupEnabled()
        );
        /* Lo definimos en el layout que ya llamará a la función toggleSD en OnClick
        sd_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSD(v);
            }
        }); */

        cloud_backup = (CheckBox) findViewById(R.id.sd_checkbox);
        cloud_backup.setChecked(
                TiendasApplication.getInstance().getPreferencesManager().isCloudEnabled()
        );
        sd_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCloud(v);
            }
        });


    }

    public void toggleSD(View v){
        TiendasApplication.getInstance().getPreferencesManager().setExternalBackupEnabled(sd_backup.isChecked());
    }

    public void toggleCloud(View v){
        TiendasApplication.getInstance().getPreferencesManager().setCloudEnabled(cloud_backup.isChecked());
    }
}
