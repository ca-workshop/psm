package com.ca.passwordmanager.data;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "password_items")
public class PasswordItem {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String accountName;
    private String usernameOrEmail;
    private String password;

    private long lastChangedTimestamp;

    // NEW: category
    private long categoryId;

    // UI-only field (from JOIN query)
    @Ignore
    @Nullable
    private String categoryName;

    public PasswordItem(String accountName,
                        String usernameOrEmail,
                        String password,
                        long lastChangedTimestamp,
                        long categoryId) {
        this.accountName = accountName;
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.lastChangedTimestamp = lastChangedTimestamp;
        this.categoryId = categoryId;
    }

    // Keep old constructor for backward compatibility (default category = "General")
    @Ignore
    public PasswordItem(String accountName,
                        String usernameOrEmail,
                        String password,
                        long lastChangedTimestamp) {
        this(accountName, usernameOrEmail, password, lastChangedTimestamp, 1L);
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
    public void setLastChangedTimestamp(long lastChangedTimestamp) { this.lastChangedTimestamp = lastChangedTimestamp; }

    public long getCategoryId() { return categoryId; }
    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }

    @Nullable
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(@Nullable String categoryName) { this.categoryName = categoryName; }

    // NEW: expires 3 months after last change (calendar-accurate)
    public long getExpireTimestamp() {
        return com.ca.passwordmanager.util.DateUi.addMonths(lastChangedTimestamp, 3);
    }
}
