package com.ca.passwordmanager.ui.passwords;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ca.passwordmanager.data.PasswordItem;
import com.ca.passwordmanager.data.PasswordRepository;

import java.util.List;

public class PasswordListViewModel extends AndroidViewModel {

    private final PasswordRepository repo;
    private final LiveData<List<PasswordItem>> items;

    public PasswordListViewModel(@NonNull Application app) {
        super(app);
        repo = new PasswordRepository(app);
        items = repo.observeAllPasswordItems();
    }


    // ✅ Keep existing API
    public LiveData<List<PasswordItem>> getItems() {
        return items;
    }

    public void updateItem(PasswordItem item) {
        repo.updatePassword(item);
    }

    // ✅ Add aliases expected by your Activity (no break)
    public LiveData<List<PasswordItem>> getPasswordList() {
        return items;
    }

    public void deleteItem(@NonNull PasswordItem item) {
        repo.deletePasswordById(item.getId());
    }
}
