package com.ca.passwordmanager.ui.passwords;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ca.passwordmanager.data.PasswordHistory;
import com.ca.passwordmanager.data.PasswordRepository;

import java.util.List;

public class PasswordHistoryViewModel extends AndroidViewModel {

    private final PasswordRepository repo;

    public PasswordHistoryViewModel(@NonNull Application app) {
        super(app);
        repo = new PasswordRepository(app);
    }

    public LiveData<List<PasswordHistory>> historyFor(long itemId) {
        return repo.observeHistory(itemId);
    }
}
