package com.ca.passwordmanager.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
        entities = {
                PasswordItem.class,
                Category.class,
                PasswordHistory.class,
                MasterPasswordEntity.class
        },
        version = 2,
        exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PasswordItemDao passwordItemDao();
    public abstract CategoryDao categoryDao();
    public abstract PasswordHistoryDao passwordHistoryDao();
    public abstract MasterPasswordDao masterPasswordDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context ctx) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(),
                                    AppDatabase.class,
                                    "password_manager_db")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // v1 -> v2: add categories + history, add categoryId to password_items, seed default categories
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {

            db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`name` TEXT)");

            db.execSQL("CREATE TABLE IF NOT EXISTS `password_history` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`passwordItemId` INTEGER NOT NULL, " +
                    "`oldPassword` TEXT, " +
                    "`changedAtTimestamp` INTEGER NOT NULL)");

            db.execSQL("CREATE INDEX IF NOT EXISTS `index_password_history_passwordItemId` ON `password_history` (`passwordItemId`)");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_password_history_passwordItemId_changedAtTimestamp` ON `password_history` (`passwordItemId`, `changedAtTimestamp`)");

            // Add categoryId column to password_items with default 1
            db.execSQL("ALTER TABLE `password_items` ADD COLUMN `categoryId` INTEGER NOT NULL DEFAULT 1");

            // Seed categories (id values will start at 1)
            db.execSQL("INSERT INTO `categories` (`name`) VALUES ('General')");
            db.execSQL("INSERT INTO `categories` (`name`) VALUES ('Bank')");
            db.execSQL("INSERT INTO `categories` (`name`) VALUES ('Mail')");
            db.execSQL("INSERT INTO `categories` (`name`) VALUES ('Car')");
            db.execSQL("INSERT INTO `categories` (`name`) VALUES ('Door')");
        }
    };
}
