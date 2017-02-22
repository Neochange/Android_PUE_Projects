package com.example.hola.shoppingapp.service.backup;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.hola.shoppingapp.TiendasApplication;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class BackupIntentService extends IntentService {

    public BackupIntentService() {
        super("BackupIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        BackupService backupService = TiendasApplication.getInstance().getBackupService();
        if(backupService.isBackupReady()){

            // getMainLooper está ejecutando el handler en el main thread
            Handler toastHandler = new Handler(Looper.getMainLooper()){
                // Como le he pasado el thread principal, el codigo de handleMessage se ejecuta
                // en el main thread
                @Override
                public void handleMessage(Message msg) {
                    Log.d("BackupIntentService", "handleMessage");
                    String mss = msg.getData().getString(CloudBackupService.HANDLER_BUNDLE_STATUS);
                    Toast.makeText(BackupIntentService.this,
                                    mss,
                                    Toast.LENGTH_LONG).show();
                    Log.d("BackupIntentService", mss);

                }
            };

            try{
                Log.i("BackupIntentService", "before DoBackup");
                backupService.doBackup(toastHandler);
            }catch (TiendasBackupException e){
                toastHandler.sendMessage(CloudBackupService.createMessage("Error performing backup"));
            }

        }
    }
    }
