package com.ca.passwordmanager.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.ca.passwordmanager.data.AppDatabase;
import com.ca.passwordmanager.data.PasswordRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PasswordRepository repository;

    public ViewModelFactory(Context context) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        this.repository = new PasswordRepository(db.passwordDao());
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(repository);
        } else if (modelClass.isAssignableFrom(PasswordListViewModel.class)) {
            return (T) new PasswordListViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass);
        }
    }

    //new version
   /* @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(
            @NonNull Class<T> modelClass,
            @NonNull CreationExtras extras
    ) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(repository);
        } else if (modelClass.isAssignableFrom(PasswordListViewModel.class)) {
            return (T) new PasswordListViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass);
        }
    }*/
}
