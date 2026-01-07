package com.ca.passwordmanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PasswordItemDao {

    // Join to show category name in list
    @Query("SELECT p.id, p.accountName, p.usernameOrEmail, p.password, p.lastChangedTimestamp, p.categoryId, c.name AS categoryName " +
            "FROM password_items p " +
            "LEFT JOIN categories c ON p.categoryId = c.id " +
            "ORDER BY p.accountName ASC")
    LiveData<List<PasswordItemRow>> observeAllRows();

    @Query("SELECT * FROM password_items WHERE id = :id LIMIT 1")
    PasswordItem getByIdNow(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PasswordItem item);

    @Update
    int update(PasswordItem item);

    @Query("DELETE FROM password_items WHERE id = :id")
    int deleteById(long id);
}
