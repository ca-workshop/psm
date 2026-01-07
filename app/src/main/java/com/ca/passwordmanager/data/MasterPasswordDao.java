package com.ca.passwordmanager.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MasterPasswordDao {

    @Query("SELECT COUNT(*) FROM master_password WHERE id = 1")
    int hasMasterNow();

    @Query("SELECT passwordHash FROM master_password WHERE id = 1 LIMIT 1")
    String getHashNow();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(MasterPasswordEntity entity);

    @Query("DELETE FROM master_password")
    void clearAll();
}
