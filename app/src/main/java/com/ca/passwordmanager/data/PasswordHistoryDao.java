package com.ca.passwordmanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PasswordHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PasswordHistory history);

    @Query("SELECT * FROM password_history WHERE passwordItemId = :itemId ORDER BY changedAtTimestamp DESC")
    LiveData<List<PasswordHistory>> observeForItem(long itemId);

    @Query("DELETE FROM password_history WHERE passwordItemId = :itemId")
    int deleteForItem(long itemId);

}
