package com.ca.passwordmanager.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ca.passwordmanager.data.PasswordRepository;
import com.ca.passwordmanager.ui.passwords.PasswordListViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Application app;
    private final PasswordRepository repository;

    // ✅ Recommended: build everything from Application
    public ViewModelFactory(@NonNull Application app) {
        this.app = app;
        this.repository = new PasswordRepository(app);
    }

    // ✅ Optional: if you already have a repository instance
    public ViewModelFactory(@NonNull Application app, @NonNull PasswordRepository repository) {
        this.app = app;
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        // LoginViewModel expects repository
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(repository);
        }

        // PasswordListViewModel is AndroidViewModel => needs Application
        if (modelClass.isAssignableFrom(PasswordListViewModel.class)) {
            return (T) new PasswordListViewModel(app);
        }

        // Add/Edit expects repository
        if (modelClass.isAssignableFrom(AddEditPasswordViewModel.class)) {
            return (T) new AddEditPasswordViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
