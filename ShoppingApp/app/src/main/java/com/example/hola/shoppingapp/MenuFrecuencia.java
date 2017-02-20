package com.example.hola.shoppingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class MenuFrecuencia extends AppCompatActivity {

    public static final int BACKUP_FREQUENCY_DAILY = 1;
    public static final int BACKUP_FREQUENCY_WEEKLY = 2;

    CheckBox sd_backup;
    CheckBox cloud_backup;

    RadioButton daily_freq;
    RadioButton weekly_freq;

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

        cloud_backup = (CheckBox) findViewById(R.id.cloud_checkBox);
        cloud_backup.setChecked(
                TiendasApplication.getInstance().getPreferencesManager().isCloudEnabled()
        );
        cloud_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCloud(v);
            }
        });

        // Configuramos el estado inicial del radio button Diario/Semanal
        daily_freq = (RadioButton)findViewById(R.id.daily_radioButton);
        weekly_freq = (RadioButton) findViewById(R.id.weekly_radioButton);
        boolean isDailyFreq = TiendasApplication.getInstance().getPreferencesManager().getFrequency() == BACKUP_FREQUENCY_DAILY;
        daily_freq.setChecked(isDailyFreq);
        weekly_freq.setChecked(!isDailyFreq);

        daily_freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDailyBackup(v);
            }
        });

        weekly_freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWeeklyBackup(v);
            }
        });
    }

    public void toggleSD(View v){
        TiendasApplication.getInstance().getPreferencesManager().setExternalBackupEnabled(sd_backup.isChecked());
    }

    public void toggleCloud(View v){
        TiendasApplication.getInstance().getPreferencesManager().setCloudEnabled(cloud_backup.isChecked());
    }

    public void setDailyBackup(View v){
        weekly_freq.setChecked(false);
        TiendasApplication.getInstance().getPreferencesManager().setFreq(BACKUP_FREQUENCY_DAILY);
    }

    public void setWeeklyBackup(View v){
        daily_freq.setChecked(false);
        TiendasApplication.getInstance().getPreferencesManager().setFreq(BACKUP_FREQUENCY_WEEKLY);
    }
}
