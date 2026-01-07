package com.ca.passwordmanager.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "password_history",
        indices = {
                @Index(value = "passwordItemId"),
                @Index(value = {"passwordItemId", "changedAtTimestamp"})
        }
)
public class PasswordHistory {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long passwordItemId;
    private String oldPassword;
    private long changedAtTimestamp;

    public PasswordHistory(long passwordItemId, String oldPassword, long changedAtTimestamp) {
        this.passwordItemId = passwordItemId;
        this.oldPassword = oldPassword;
        this.changedAtTimestamp = changedAtTimestamp;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getPasswordItemId() { return passwordItemId; }
    public void setPasswordItemId(long passwordItemId) { this.passwordItemId = passwordItemId; }

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public long getChangedAtTimestamp() { return changedAtTimestamp; }
    public void setChangedAtTimestamp(long changedAtTimestamp) { this.changedAtTimestamp = changedAtTimestamp; }
}
