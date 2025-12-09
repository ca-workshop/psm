package com.ca.passwordmanager.data;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PasswordRepository {

    private final PasswordDao passwordDao;
    private final LiveData<List<PasswordItem>> allPasswordsLiveData;

    public PasswordRepository(PasswordDao passwordDao) {
        this.passwordDao = passwordDao;
        this.allPasswordsLiveData = passwordDao.getAllPasswords();
    }

    public LiveData<List<PasswordItem>> getAllPasswordsLiveData() {
        return allPasswordsLiveData;
    }

    // sync read; we call it from background thread
    public MasterPassword getMasterPasswordSync() {
        return passwordDao.getMasterPasswordSync();
    }

    public void setMasterPassword(final String password) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                passwordDao.setMasterPassword(new MasterPassword(password));
            }
        });
    }

    public void addPassword(final String accountName,
                            final String usernameOrEmail,
                            final String password,
                            final long timestamp) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                PasswordItem item = new PasswordItem(
                        accountName,
                        usernameOrEmail,
                        password,
                        timestamp
                );
                passwordDao.insertPassword(item);
            }
        });
    }

    public void updatePassword(final PasswordItem item) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                passwordDao.updatePassword(item);
            }
        });
    }

    public void deletePassword(final PasswordItem item) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                passwordDao.deletePassword(item);
            }
        });
    }
}
