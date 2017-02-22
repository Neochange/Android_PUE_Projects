package com.example.hola.shoppingapp.service.backup;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by poo3 on 03/06/2016.
 */
public interface BackupService {

    boolean isBackupReady();
    void doBackup(Handler uiHandler) throws TiendasBackupException;
    boolean isRestoreReady();
    void doRestore(Handler uiHandler) throws TiendasBackupException;

}
