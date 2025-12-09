package com.ca.passwordmanager.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ca.passwordmanager.data.MasterPassword;
import com.ca.passwordmanager.data.PasswordRepository;

public class LoginViewModel extends ViewModel {

    private final PasswordRepository repository;

    private final MutableLiveData<String> inputPassword = new MutableLiveData<>("");
    private final MutableLiveData<LoginState> loginState = new MutableLiveData<>(LoginState.IDLE);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    public LoginViewModel(PasswordRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<String> getInputPassword() {
        return inputPassword;
    }

    public LiveData<LoginState> getLoginState() {
        return loginState;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void onLoginClicked() {
        final String pwd = inputPassword.getValue() != null ? inputPassword.getValue() : "";
        if (pwd.trim().isEmpty()) {
            loginState.setValue(LoginState.ERROR);
            errorMessage.setValue("Password must not be empty");
            return;
        }

        // Run DB access on background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                MasterPassword stored = repository.getMasterPasswordSync();
                if (stored == null) {
                    // First time - set master password
                    repository.setMasterPassword(pwd);
                    loginState.postValue(LoginState.SUCCESS);
                } else if (pwd.equals(stored.getPassword())) {
                    loginState.postValue(LoginState.SUCCESS);
                } else {
                    loginState.postValue(LoginState.ERROR);
                    errorMessage.postValue("Wrong master password");
                }
            }
        }).start();
    }
}
