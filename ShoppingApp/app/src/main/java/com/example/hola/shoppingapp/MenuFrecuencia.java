package com.example.hola.shoppingapp;

import android.app.ProgressDialog;
import android.app.backup.BackupAgent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.hola.shoppingapp.service.backup.BackupService;
import com.example.hola.shoppingapp.service.backup.CloudBackupService;
import com.example.hola.shoppingapp.service.backup.TiendasBackupException;

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

    public void DoBackup(View view) {
        final BackupService service = TiendasApplication.getInstance().getBackupService();
        if( service.isBackupReady()){
            Log.i("BackupActivity", "Backup Ready");
            // Hay que poner final porque el thread no permite objeto mutables (modificables)
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("BackupProgress");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            new AsyncTask<Void, String, Boolean>() {

                Handler uiHandler;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd.show();
                    uiHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            // Aqui gestionamos la recepción del mensaje que enviamos desde CloudBackupService
                            // Cogemos el mensaje con la key que hemos definido en el CloudBackupService
                            pd.setMessage(msg.getData().getString(CloudBackupService.HANDLER_BUNDLE_STATUS));
                        }
                    };
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    if( aBoolean ){
                        Toast.makeText(MenuFrecuencia.this,"Backup done", Toast.LENGTH_LONG).show();
                        pd.cancel();
                    }
                    else{
                        Toast.makeText(MenuFrecuencia.this,"Error in backup", Toast.LENGTH_LONG).show();
                    }
                    super.onPostExecute(aBoolean);
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);
                }

                // Boolean es el objeto de salida porque se lo hemos puesto como resultado
                // El doInbackground se ejecuta en un workerThread mientras que onPreExecute,
                // OnPostExecute y onProgressUpdate se ejecutan en el UIThread y puede interactuar
                // con la UI
                @Override
                protected Boolean doInBackground(Void... params) {
                    boolean result = false;

                    try{
                        service.doBackup(uiHandler);
                        result = true;
                    } catch (TiendasBackupException e) {
                        e.printStackTrace();
                    }


                    return result;
                }
            }.execute(); // Dentro de execute tendriamos que pasarle lo que hayamos puesto como parametro de entrada.
        }
    }
}
