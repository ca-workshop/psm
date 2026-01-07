package com.ca.passwordmanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "master_password")
public class MasterPasswordEntity {

    @PrimaryKey
    public int id = 1; // single-row table

    public String passwordHash;
    public long createdAt;

    public MasterPasswordEntity(String passwordHash, long createdAt) {
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }
}
