package com.ca.passwordmanager.data;

public final class PasswordMappers {
    private PasswordMappers() {}

    public static PasswordItem toPasswordItem(PasswordItemRow row) {
        PasswordItem item = new PasswordItem(
                row.accountName,
                row.usernameOrEmail,
                row.password,
                row.lastChangedTimestamp,
                row.categoryId
        );
        item.setId(row.id);
        item.setCategoryName(row.categoryName);
        return item;
    }
}
