package com.ca.passwordmanager.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ca.passwordmanager.data.PasswordRepository;

public class LoginViewModel extends ViewModel {

    public final MutableLiveData<String> password = new MutableLiveData<>("");
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>("");

    private final MutableLiveData<Boolean> createMode = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> navigateEvent = new MutableLiveData<>(false);

    private final PasswordRepository repository;

    public LoginViewModel(PasswordRepository repository) {
        this.repository = repository;

        // Decide mode on start
        new Thread(() -> {
            boolean hasMaster = repository.hasMasterPassword();
            createMode.postValue(!hasMaster); // true = create, false = login
        }).start();
    }

    public LiveData<Boolean> getCreateMode() {
        return createMode;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getNavigateEvent() {
        return navigateEvent;
    }

    public void onPrimaryButtonClicked() {
        String pwd = safe(password.getValue());
        String confirm = safe(confirmPassword.getValue());

        Boolean isCreate = createMode.getValue();
        if (isCreate != null && isCreate) {
            handleCreate(pwd, confirm);
        } else {
            handleLogin(pwd);
        }
    }

    private void handleCreate(String pwd, String confirm) {
        if (pwd.isEmpty() || confirm.isEmpty()) {
            errorMessage.setValue("Both fields are required");
            return;
        }
        if (!pwd.equals(confirm)) {
            errorMessage.setValue("Passwords do not match");
            return;
        }

        // Store master password (async inside repo)
        repository.setMasterPassword(pwd);

        errorMessage.setValue("");
        navigateEvent.setValue(true);  // 🔥 tell Activity to navigate
    }

    private void handleLogin(final String pwd) {
        if (pwd.isEmpty()) {
            errorMessage.setValue("Please enter your master password");
            return;
        }

        new Thread(() -> {
            boolean ok = repository.checkMasterPassword(pwd);
            if (ok) {
                errorMessage.postValue("");
                navigateEvent.postValue(true);  // 🔥 tell Activity to navigate
            } else {
                errorMessage.postValue("Wrong password");
            }
        }).start();
    }

    // called by Activity after handling the event
    public void clearNavigateEvent() {
        navigateEvent.setValue(false);
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
