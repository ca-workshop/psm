package com.ca.passwordmanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "master_password")
public class MasterPassword {

    @PrimaryKey
    private int id = 1; // always single row

    private String password;

    // Required empty constructor for Room
    public MasterPassword() {
    }

    // Convenience constructor for manual creation
    @Ignore
    public MasterPassword(String password) {
        this.password = password;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
