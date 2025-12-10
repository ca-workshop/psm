package com.ca.passwordmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ca.passwordmanager.PasswordManagerApp;
import com.ca.passwordmanager.R;
import com.ca.passwordmanager.data.PasswordRepository;
import com.ca.passwordmanager.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) Inflate layout with DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);

        // 2) DI – get repository from PasswordManagerApp → AppContainer
        PasswordManagerApp app = (PasswordManagerApp) getApplication();
        PasswordRepository repo = app.getAppContainer().passwordRepository;

        ViewModelFactory factory = new ViewModelFactory(repo);
        viewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);
        binding.setViewModel(viewModel);

        // 3) Wire button and observers
        setupUiEvents();
        observeViewModel();
    }

    private void setupUiEvents() {
        // Primary button (Login / Create) – logic lives in ViewModel
        binding.btnPrimary.setOnClickListener(v -> viewModel.onPrimaryButtonClicked());
    }

    private void observeViewModel() {

        // Create vs login mode
        viewModel.getCreateMode().observe(this, isCreateMode -> {
            if (isCreateMode != null && isCreateMode) {
                binding.txtTitle.setText(R.string.create_master_password_title);
                binding.btnPrimary.setText(R.string.action_create_master_password);
                binding.inputConfirmPassword.setVisibility(View.VISIBLE);
            } else {
                binding.txtTitle.setText(R.string.login_title);
                binding.btnPrimary.setText(R.string.action_login);
                binding.inputConfirmPassword.setVisibility(View.GONE);
            }
        });

        // Error text
        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                binding.txtError.setVisibility(View.VISIBLE);
                binding.txtError.setText(msg);
            } else {
                binding.txtError.setVisibility(View.GONE);
            }
        });

        // Navigation
        viewModel.getNavigateEvent().observe(this, go -> {
            if (go != null && go) {
                Toast.makeText(this, "Login OK, opening list…", Toast.LENGTH_SHORT).show();
                openPasswordList();
                // IMPORTANT: tell ViewModel we handled it
                viewModel.clearNavigateEvent();
            }
        });
    }

    private void openPasswordList() {
        Intent intent = new Intent(this, PasswordListActivity.class);
        startActivity(intent);
        finish(); // no back to login
    }
}
