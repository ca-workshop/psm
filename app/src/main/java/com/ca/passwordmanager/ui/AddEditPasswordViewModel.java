package com.ca.passwordmanager.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ca.passwordmanager.data.Category;
import com.ca.passwordmanager.data.PasswordItem;
import com.ca.passwordmanager.data.PasswordRepository;

import java.util.List;

public class AddEditPasswordViewModel extends ViewModel {

    private final PasswordRepository repository;

    private final MutableLiveData<String> accountName = new MutableLiveData<>("");
    private final MutableLiveData<String> usernameOrEmail = new MutableLiveData<>("");
    private final MutableLiveData<String> password = new MutableLiveData<>("");
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> closeScreenEvent = new MutableLiveData<>(false);

    // ✅ Categories
    private final LiveData<List<Category>> categories;
    private final MutableLiveData<Long> selectedCategoryId = new MutableLiveData<>(1L); // default "General" (id=1)

    private boolean isEditMode = false;
    private long editingId = -1L;
    private PasswordItem currentItem;

    public AddEditPasswordViewModel(PasswordRepository repository) {
        this.repository = repository;
        this.categories = repository.observeCategories();
    }

    public MutableLiveData<String> getAccountName() { return accountName; }
    public MutableLiveData<String> getUsernameOrEmail() { return usernameOrEmail; }
    public MutableLiveData<String> getPassword() { return password; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getCloseScreenEvent() { return closeScreenEvent; }

    public LiveData<List<Category>> getCategories() { return categories; }
    public MutableLiveData<Long> getSelectedCategoryId() { return selectedCategoryId; }
    public void setSelectedCategoryId(long id) { selectedCategoryId.setValue(id); }

    public boolean isEditMode() { return isEditMode; }

    public void initForNew() {
        isEditMode = false;
        editingId = -1L;
        currentItem = null;

        accountName.setValue("");
        usernameOrEmail.setValue("");
        password.setValue("");
        selectedCategoryId.setValue(1L);

        errorMessage.setValue("");
        closeScreenEvent.setValue(false);
    }

    public void initForEdit(final long id) {
        isEditMode = true;
        editingId = id;

        new Thread(() -> {
            PasswordItem item = repository.getPasswordSyncById(id);
            currentItem = item;

            if (item != null) {
                accountName.postValue(item.getAccountName());
                usernameOrEmail.postValue(item.getUsernameOrEmail());
                password.postValue(item.getPassword());
                selectedCategoryId.postValue(item.getCategoryId());
            } else {
                errorMessage.postValue("Item not found");
            }
        }).start();
    }

    public void onSaveClicked() {
        String acc = trim(accountName.getValue());
        String user = trim(usernameOrEmail.getValue());
        String pwd = trim(password.getValue());

        if (acc.isEmpty() || user.isEmpty() || pwd.isEmpty()) {
            errorMessage.setValue("All fields are required");
            return;
        }

        long now = System.currentTimeMillis();
        long catId = selectedCategoryId.getValue() != null ? selectedCategoryId.getValue() : 1L;

        if (isEditMode) {
            // ✅ If edit mode but item not loaded yet -> block save
            if (currentItem == null) {
                errorMessage.setValue("Please wait… loading item");
                return;
            }

            // ✅ Update existing row (no duplicate)
            currentItem.setAccountName(acc);
            currentItem.setUsernameOrEmail(user);
            currentItem.setPassword(pwd);
            currentItem.setCategoryId(catId);

            // Let your repository decide timestamp/history rules if you want,
            // or force timestamp update here:
            currentItem.setLastChangedTimestamp(now);

            repository.updatePasswordOnly(currentItem);
        } else {
            PasswordItem item = new PasswordItem(acc, user, pwd, now, catId);
            repository.insertPassword(item);
        }

        errorMessage.setValue("");
        closeScreenEvent.setValue(true);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
