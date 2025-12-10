package com.ca.passwordmanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ca.passwordmanager.data.MasterPassword;
import com.ca.passwordmanager.data.PasswordItem;

import java.util.List;

@Dao
public interface PasswordDao {

    // Master password
    @Query("SELECT * FROM master_password WHERE id = 1 LIMIT 1")
    MasterPassword getMasterPasswordSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setMasterPassword(MasterPassword masterPassword);

    // Password list (Flow-like via LiveData)
    @Query("SELECT * FROM password_items ORDER BY accountName ASC")
    LiveData<List<PasswordItem>> getAllPasswords();

    @Query("SELECT * FROM password_items WHERE id = :id LIMIT 1")
    PasswordItem getByIdSync(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPassword(PasswordItem item);

    @Update
    void updatePassword(PasswordItem item);

    @Delete
    void deletePassword(PasswordItem item);
}
