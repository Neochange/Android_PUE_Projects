package com.example.hola.shoppingapp.service.backup;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by daa on 22/02/2017.
 */

public class CloudBackupService implements BackupService {

    public static final String HANDLER_BUNDLE_STATUS = "handler_message";

    @Override
    public boolean isBackupReady() {
        return true;
    }

    @Override
    public void doBackup(Handler uiHandler) throws TiendasBackupException {

        // El uiHandler es en realidad una pasarela al thread de UI
        for( int i = 0; i< 5;i++){
            uiHandler.sendMessage(createMessage("Step " + i));
            try{
                Thread.sleep(3000);
            }
            catch (InterruptedException e){

            }
        }
    }

    @Override
    public boolean isRestoreReady() {
        return true;
    }

    @Override
    public void doRestore(Handler uiHandler) throws TiendasBackupException {
        for( int i = 0; i< 5;i++){
            uiHandler.sendMessage(createMessage("Step " + i));
            try{
                Thread.sleep(3000);
            }
            catch (InterruptedException e){

            }
        }
    }

    public static Message createMessage(String message){
        Message result = new Message();
        Bundle data = new Bundle();
        data.putString(HANDLER_BUNDLE_STATUS, message);
        result.setData(data);
        return result;
    }
}
