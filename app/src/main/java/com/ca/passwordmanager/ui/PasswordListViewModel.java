package com.ca.passwordmanager.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ca.passwordmanager.data.PasswordItem;
import com.ca.passwordmanager.data.PasswordRepository;

import java.util.List;

public class PasswordListViewModel extends ViewModel {

    private final PasswordRepository repository;
    private final LiveData<List<PasswordItem>> passwordList;

    public PasswordListViewModel(PasswordRepository repository) {
        this.repository = repository;
        this.passwordList = repository.getAllPasswordsLiveData();
    }

    public LiveData<List<PasswordItem>> getPasswordList() {
        return passwordList;
    }

    public void addDummyPassword() {
        // Example method to add new item
        long now = System.currentTimeMillis();
        repository.addPassword("Gmail", "user@gmail.com", "12345678", now);
    }

    public void deleteItem(PasswordItem item) {
        repository.deletePassword(item);
    }
}
