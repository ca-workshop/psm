package com.ca.passwordmanager.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ca.passwordmanager.PasswordManagerApp;
import com.ca.passwordmanager.R;
import com.ca.passwordmanager.databinding.ActivityAddEditPasswordBinding;
import com.ca.passwordmanager.ui.AddEditPasswordViewModel;
import com.ca.passwordmanager.ui.ViewModelFactory;

public class AddEditPasswordActivity extends AppCompatActivity {

    public static final String EXTRA_PASSWORD_ID = "extra_password_id";

    private AddEditPasswordViewModel viewModel;
    private ActivityAddEditPasswordBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_password);

        PasswordManagerApp app = (PasswordManagerApp) getApplication();
        ViewModelFactory factory = new ViewModelFactory(app.getAppContainer().passwordRepository);
        viewModel = new ViewModelProvider(this, factory).get(AddEditPasswordViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);


        // Detect if edit or add
        long id = getIntent().getLongExtra(EXTRA_PASSWORD_ID, -1L);
        if (id == -1L) {
            setTitle("Add Password");
            viewModel.initForNew();
        } else {
            setTitle("Edit Password");
            viewModel.initForEdit(id);
        }

        // Observe error
        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null && !s.isEmpty()) {
                    binding.txtErrorAddEdit.setVisibility(View.VISIBLE);
                    binding.txtErrorAddEdit.setText(s);
                } else {
                    binding.txtErrorAddEdit.setVisibility(View.GONE);
                }
            }
        });

        // Observe close event
        viewModel.getCloseScreenEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean close) {
                if (close != null && close) {
                    finish();
                }
            }
        });
    }
}
