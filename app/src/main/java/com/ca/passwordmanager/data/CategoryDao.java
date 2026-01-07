package com.ca.passwordmanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Category category);

    @Query("SELECT * FROM categories ORDER BY name ASC")
    LiveData<List<Category>> observeAll();

    @Query("SELECT * FROM categories ORDER BY name ASC")
    List<Category> getAllNow();

    @Query("SELECT id FROM categories WHERE name = :name LIMIT 1")
    Long findIdByName(String name);
}
