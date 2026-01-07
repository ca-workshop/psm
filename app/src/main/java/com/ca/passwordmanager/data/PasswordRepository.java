package com.ca.passwordmanager.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PasswordRepository {

    private final PasswordItemDao passwordItemDao;
    private final CategoryDao categoryDao;
    private final PasswordHistoryDao historyDao;

    // NEW:
    private final MasterPasswordDao masterDao;

    private final ExecutorService io = Executors.newSingleThreadExecutor();

    public PasswordRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);

        passwordItemDao = db.passwordItemDao();
        categoryDao = db.categoryDao();
        historyDao = db.passwordHistoryDao();

        // NEW:
        masterDao = db.masterPasswordDao();
    }

    // -------------------------
    // MASTER PASSWORD API
    // -------------------------

    // Blocking read (call from background thread)
    public PasswordItem getPasswordSyncById(long id) {
        return passwordItemDao.getByIdNow(id);
    }
    //public PasswordItem getPasswordSyncById(long id) {
    //    return passwordItemDao.getByIdNow(id);
    //}
    public void addPassword(String accountName,
                            String usernameOrEmail,
                            String password,
                            long lastChangedTimestamp) {
        io.execute(() -> {
            long ts = (lastChangedTimestamp > 0) ? lastChangedTimestamp : System.currentTimeMillis();
            PasswordItem item = new PasswordItem(accountName, usernameOrEmail, password, ts);
            passwordItemDao.insert(item);
        });
    }

    public void deletePasswordById(long id) {
        io.execute(() -> passwordItemDao.deleteById(id));
    }

    /** blocking (call from background thread) */
    public boolean hasMasterPassword() {
        return masterDao.hasMasterNow() > 0;
    }

    /** async write */
    public void setMasterPassword(String rawPassword) {
        final String hash = sha256(rawPassword);
        io.execute(() -> masterDao.upsert(new MasterPasswordEntity(hash, System.currentTimeMillis())));
    }

    /** blocking check (call from background thread) */
    public boolean checkMasterPassword(String rawPassword) {
        String storedHash = masterDao.getHashNow();
        if (storedHash == null || storedHash.isEmpty()) return false;
        return storedHash.equals(sha256(rawPassword));
    }

    public LiveData<List<PasswordItem>> observeAllPasswordItems() {
        // If you already have passwordItemDao.observeAllRows()
        return new MappedLiveData<>(passwordItemDao.observeAllRows(), rows -> {
            List<PasswordItem> out = new ArrayList<>();
            if (rows == null) return out;
            for (PasswordItemRow r : rows) out.add(PasswordMappers.toPasswordItem(r));
            return out;
        });
    }

    public void updatePassword(PasswordItem updated) {
        io.execute(() -> {
            PasswordItem existing = passwordItemDao.getByIdNow(updated.getId());
            if (existing == null) {
                // if item doesn't exist, insert it
                if (updated.getLastChangedTimestamp() <= 0) {
                    updated.setLastChangedTimestamp(System.currentTimeMillis());
                }
                passwordItemDao.insert(updated);
                return;
            }

            String oldPass = existing.getPassword();
            String newPass = updated.getPassword();

            boolean changed = (oldPass != null && !oldPass.equals(newPass))
                    || (oldPass == null && newPass != null);

            if (changed) {
                // store old password in history (if you have historyDao)
                historyDao.insert(new PasswordHistory(
                        updated.getId(),
                        oldPass,
                        System.currentTimeMillis()
                ));
                updated.setLastChangedTimestamp(System.currentTimeMillis());
            } else {
                updated.setLastChangedTimestamp(existing.getLastChangedTimestamp());
            }

            passwordItemDao.update(updated);
        });
    }

    // Observe categories for spinner
    public LiveData<List<Category>> observeCategories() {
        return categoryDao.observeAll();
    }

    // Insert new item (async)
    public void insertPassword(PasswordItem item) {
        io.execute(() -> passwordItemDao.insert(item));
    }

    // Update existing (async) - IMPORTANT: do NOT insert here
    public void updatePasswordOnly(PasswordItem item) {
        io.execute(() -> passwordItemDao.update(item));
    }

    public LiveData<List<PasswordHistory>> observeHistory(long passwordItemId) {
        return historyDao.observeForItem(passwordItemId);
    }

    // -------------------------
    // HASHING
    // -------------------------
    private static String sha256(String input) {
        if (input == null) input = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(out.length * 2);
            for (byte b : out) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // very unlikely on Android; fallback (still deterministic)
            return String.valueOf(input.hashCode());
        }
    }

    // ... keep the rest of your existing repository methods (observeAllPasswordItems, updatePassword, etc.)
}
