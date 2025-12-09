package com.ca.passwordmanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "password_items")
public class PasswordItem {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String accountName;
    private String usernameOrEmail;
    private String password;
    private long lastChangedTimestamp;

    public PasswordItem(String accountName,
                        String usernameOrEmail,
                        String password,
                        long lastChangedTimestamp) {
        this.accountName = accountName;
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.lastChangedTimestamp = lastChangedTimestamp;
    }

    // getters & setters

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getUsernameOrEmail() { return usernameOrEmail; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public long getLastChangedTimestamp() { return lastChangedTimestamp; }
    public void setLastChangedTimestamp(long lastChangedTimestamp) {
        this.lastChangedTimestamp = lastChangedTimestamp;
    }
}
