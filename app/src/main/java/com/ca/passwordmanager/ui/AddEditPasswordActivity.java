package com.ca.passwordmanager.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ca.passwordmanager.PasswordManagerApp;
import com.ca.passwordmanager.R;
import com.ca.passwordmanager.data.Category;
import com.ca.passwordmanager.data.PasswordRepository;
import com.ca.passwordmanager.databinding.ActivityAddEditPasswordBinding;
import com.ca.passwordmanager.ui.AddEditPasswordViewModel;
import com.ca.passwordmanager.ui.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class AddEditPasswordActivity extends AppCompatActivity {

    public static final String EXTRA_PASSWORD_ID = "EXTRA_PASSWORD_ID";

    private ActivityAddEditPasswordBinding binding;
    private AddEditPasswordViewModel viewModel;

    private final List<Category> categoryModels = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_password);
        binding.setLifecycleOwner(this);

        PasswordManagerApp app = (PasswordManagerApp) getApplication();
        PasswordRepository repo = app.getAppContainer().passwordRepository;

        // Your factory that supports repository-based VMs
        ViewModelFactory factory = new ViewModelFactory(getApplication(), repo);
        viewModel = new ViewModelProvider(this, factory).get(AddEditPasswordViewModel.class);

        binding.setViewModel(viewModel);

        setupCategorySpinner();
        setupErrorObserver();
        setupCloseObserver();

        long editId = getIntent().getLongExtra(EXTRA_PASSWORD_ID, -1L);
        if (editId > 0) {
            viewModel.initForEdit(editId);
        } else {
            viewModel.initForNew();
        }
    }

    private void setupCategorySpinner() {
        categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(categoryAdapter);

        // Load categories
        viewModel.getCategories().observe(this, list -> {
            categoryModels.clear();
            categoryAdapter.clear();

            if (list != null) {
                categoryModels.addAll(list);
                for (Category c : list) categoryAdapter.add(c.getName());
            }

            categoryAdapter.notifyDataSetChanged();

            // select currently chosen category (important in edit mode)
            Long selectedId = viewModel.getSelectedCategoryId().getValue();
            if (selectedId != null) {
                int pos = findCategoryPositionById(selectedId);
                if (pos >= 0) binding.spinnerCategory.setSelection(pos);
            }
        });

        binding.spinnerCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < categoryModels.size()) {
                    viewModel.setSelectedCategoryId(categoryModels.get(position).getId());
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }

    private int findCategoryPositionById(long categoryId) {
        for (int i = 0; i < categoryModels.size(); i++) {
            if (categoryModels.get(i).getId() == categoryId) return i;
        }
        return 0; // fallback
    }

    private void setupErrorObserver() {
        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null && !msg.trim().isEmpty()) {
                binding.txtErrorAddEdit.setVisibility(View.VISIBLE);
                binding.txtErrorAddEdit.setText(msg);
            } else {
                binding.txtErrorAddEdit.setVisibility(View.GONE);
            }
        });
    }

    private void setupCloseObserver() {
        viewModel.getCloseScreenEvent().observe(this, close -> {
            if (Boolean.TRUE.equals(close)) finish();
        });
    }



    /*private ActivityAddEditPasswordBinding binding;
    private AddEditPasswordViewModel viewModel;

    private ArrayAdapter<String> categoryAdapter;
    private final List<Category> categoryModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_password);
        binding.setLifecycleOwner(this);

        PasswordManagerApp app = (PasswordManagerApp) getApplication();
        PasswordRepository repo = app.getAppContainer().passwordRepository;

        ViewModelFactory factory = new ViewModelFactory(getApplication(), repo);
        viewModel = new ViewModelProvider(this, factory).get(AddEditPasswordViewModel.class);

        binding.setViewModel(viewModel);

        setupCategorySpinner();

        long editId = getIntent().getLongExtra(Extras.EXTRA_PASSWORD_ID, -1L);
        if (editId > 0) {
            viewModel.initForEdit(editId);
        } else {
            viewModel.initForNew();
        }

        // Close screen event
        viewModel.getCloseScreenEvent().observe(this, close -> {
            if (Boolean.TRUE.equals(close)) finish();
        });
    }

    private void setupCategorySpinner() {
        categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerCategory.setAdapter(categoryAdapter);

        // Populate spinner from DB
        viewModel.getCategories().observe(this, list -> {
            categoryModels.clear();
            categoryAdapter.clear();

            if (list != null) {
                categoryModels.addAll(list);
                for (Category c : list) categoryAdapter.add(c.getName());
            }
            categoryAdapter.notifyDataSetChanged();

            // select current value (edit mode)
            Long selectedId = viewModel.getSelectedCategoryId().getValue();
            if (selectedId != null) {
                int pos = findCategoryPositionById(selectedId);
                if (pos >= 0) binding.spinnerCategory.setSelection(pos);
            }
        });

        // When user selects -> update VM
        binding.spinnerCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position >= 0 && position < categoryModels.size()) {
                    viewModel.setSelectedCategoryId(categoryModels.get(position).getId());
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }

    private int findCategoryPositionById(long categoryId) {
        for (int i = 0; i < categoryModels.size(); i++) {
            if (categoryModels.get(i).getId() == categoryId) return i;
        }
        return -1;
    }*/


    /*public static final String EXTRA_PASSWORD_ID = "extra_password_id";

    private AddEditPasswordViewModel viewModel;
    private ActivityAddEditPasswordBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_password);

        PasswordManagerApp app = (PasswordManagerApp) getApplication();
        //ViewModelFactory factory = new ViewModelFactory(app.getAppContainer().passwordRepository);
        ViewModelFactory factory = new ViewModelFactory(getApplication());
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
    }*/
}
