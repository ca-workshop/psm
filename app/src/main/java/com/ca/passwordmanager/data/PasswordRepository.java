/*
 * Project : PasswordManagerMVVM
 * File    : PasswordRepository.java
 * Author  : Alice & Bob
 *
 * Version : 1.2.0 (tag: v1.2.0)
 * Date    : 2025-12-09
 *
 * Summary :
 *    Repository for password data.
 *    - Exposes LiveData list of PasswordItem.
 *    - Provides async CRUD for password items.
 *    - Provides sync helpers for master password checks (to be called from background threads).
 *
 * History :
 *   2025-12-09  1.2.0  Bob  Cleaned duplicate setMasterPassword, clarified sync/async behavior.
 */

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

    // ---------------------------------------------------------------------------------------------
    // LiveData list
    // ---------------------------------------------------------------------------------------------
    public LiveData<List<PasswordItem>> getAllPasswordsLiveData() {
        return allPasswordsLiveData;
    }

    // ---------------------------------------------------------------------------------------------
    // Master password (sync) – MUST be called from background thread
    // ---------------------------------------------------------------------------------------------

    /** Synchronous read. Call from background thread only. */
    public MasterPassword getMasterPasswordSync() {
        return passwordDao.getMasterPasswordSync();
    }

    /**
     * Check if master password row exists.
     * This method is synchronous → call it from a background thread.
     */
    public boolean hasMasterPassword() {
        MasterPassword mp = passwordDao.getMasterPasswordSync();
        return mp != null;
    }

    /**
     * Set / update master password.
     * This version runs on a background thread internally, safe to call from UI thread.
     * Later you should store a hash instead of plain text.
     */
    public void setMasterPassword(final String plain) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MasterPassword mp = new MasterPassword();
                mp.setId(1);              // fixed id = 1, single-row table
                mp.setPassword(plain);    // TODO: replace with hash
                passwordDao.setMasterPassword(mp);
            }
        });
    }

    /**
     * Verify master password.
     * This method is synchronous → call it from a background thread.
     */
    public boolean checkMasterPassword(String plain) {
        MasterPassword mp = passwordDao.getMasterPasswordSync();
        if (mp == null) return false;
        // TODO: compare hash instead of plain text
        return plain.equals(mp.getPassword());
    }

    // ---------------------------------------------------------------------------------------------
    // Password items CRUD
    // ---------------------------------------------------------------------------------------------

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

    /** Synchronous read by id – call from background thread (used by AddEditPasswordViewModel). */
    public PasswordItem getPasswordSyncById(long id) {
        return passwordDao.getByIdSync(id);
    }
}
