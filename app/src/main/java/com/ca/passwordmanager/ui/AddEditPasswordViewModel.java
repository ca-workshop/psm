package com.ca.passwordmanager.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ca.passwordmanager.data.PasswordItem;
import com.ca.passwordmanager.data.PasswordRepository;

public class AddEditPasswordViewModel extends ViewModel {

    private final PasswordRepository repository;

    private final MutableLiveData<String> accountName = new MutableLiveData<>("");
    private final MutableLiveData<String> usernameOrEmail = new MutableLiveData<>("");
    private final MutableLiveData<String> password = new MutableLiveData<>("");
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> closeScreenEvent = new MutableLiveData<>(false);

    private boolean isEditMode = false;
    private long editingId = -1L;
    private PasswordItem currentItem;

    public AddEditPasswordViewModel(PasswordRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<String> getAccountName() {
        return accountName;
    }

    public MutableLiveData<String> getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getCloseScreenEvent() {
        return closeScreenEvent;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void initForNew() {
        isEditMode = false;
        editingId = -1L;
        currentItem = null;
        accountName.setValue("");
        usernameOrEmail.setValue("");
        password.setValue("");
        errorMessage.setValue("");
    }

    public void initForEdit(final long id) {
        isEditMode = true;
        editingId = id;

        // Load existing item in background
        new Thread(new Runnable() {
            @Override
            public void run() {
                PasswordItem item = repository.getPasswordSyncById(id);
                currentItem = item;
                if (item != null) {
                    accountName.postValue(item.getAccountName());
                    usernameOrEmail.postValue(item.getUsernameOrEmail());
                    password.postValue(item.getPassword());
                } else {
                    errorMessage.postValue("Item not found");
                }
            }
        }).start();
    }

    public void onSaveClicked() {
        String acc = accountName.getValue() != null ? accountName.getValue().trim() : "";
        String user = usernameOrEmail.getValue() != null ? usernameOrEmail.getValue().trim() : "";
        String pwd = password.getValue() != null ? password.getValue().trim() : "";

        if (acc.isEmpty() || user.isEmpty() || pwd.isEmpty()) {
            errorMessage.setValue("All fields are required");
            return;
        }

        long now = System.currentTimeMillis();

        if (isEditMode && currentItem != null) {
            currentItem.setAccountName(acc);
            currentItem.setUsernameOrEmail(user);
            currentItem.setPassword(pwd);
            currentItem.setLastChangedTimestamp(now);

            repository.updatePassword(currentItem);
        } else {
            repository.addPassword(acc, user, pwd, now);
        }

        closeScreenEvent.setValue(true);
    }
}
