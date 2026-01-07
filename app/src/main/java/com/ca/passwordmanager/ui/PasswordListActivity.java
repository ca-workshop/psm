package com.ca.passwordmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ca.passwordmanager.PasswordManagerApp;
import com.ca.passwordmanager.R;
import com.ca.passwordmanager.data.PasswordItem;
import com.ca.passwordmanager.data.PasswordRepository;
import com.ca.passwordmanager.databinding.ActivityPasswordListBinding;
import com.ca.passwordmanager.ui.passwords.PasswordHistoryDialog;
import com.ca.passwordmanager.ui.passwords.PasswordListAdapter;
import com.ca.passwordmanager.ui.passwords.PasswordListViewModel;
import com.ca.passwordmanager.ui.passwords.PasswordRowHandler;

public class PasswordListActivity extends AppCompatActivity implements PasswordRowHandler {

    //private PasswordListViewModel viewModel;
    //private PasswordListAdapter adapter;

    private ActivityPasswordListBinding binding;
    private PasswordListViewModel viewModel;
    private PasswordAdapter adapter;

    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);

        RecyclerView rv = findViewById(R.id.recyclerViewPasswords);

        adapter = new PasswordListAdapter(this *//* handler *//*);
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(PasswordListViewModel.class);
        viewModel.getItems().observe(this, items -> adapter.submit(items));
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout with DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_list);
        binding.setLifecycleOwner(this);

        // DI – get repository from PasswordManagerApp → AppContainer
        PasswordManagerApp app = (PasswordManagerApp) getApplication();
        PasswordRepository repo = app.getAppContainer().passwordRepository;

        ViewModelFactory factory = new ViewModelFactory(getApplication());//repo);
        viewModel = new ViewModelProvider(this, factory).get(PasswordListViewModel.class);
        binding.setViewModel(viewModel);

        setupRecyclerView();
        setupSwipeToDelete();
        setupFab();
        observeLiveData();
    }

    // ---------------------------------------------------------------------------------------------
    // RecyclerView setup
    // ---------------------------------------------------------------------------------------------
    private void setupRecyclerView() {
        adapter = new PasswordAdapter();
        binding.recyclerViewPasswords.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewPasswords.setAdapter(adapter);


        // simple Divider
        DividerItemDecoration divider =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.recyclerViewPasswords.addItemDecoration(divider);



        //Material Design insets divider
        //   MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(
        //           this, LinearLayoutManager.VERTICAL
        //   );

        //   divider.setDividerThickness(1); // px, optional
        //   divider.setDividerInsetStart(16); // dp
        //   divider.setDividerInsetEnd(16);   // dp


        adapter.setOnItemClickListener(item -> openEditPassword(item.getId()));

    }

    // ---------------------------------------------------------------------------------------------
    // LiveData Observers
    // ---------------------------------------------------------------------------------------------
    private void observeLiveData() {
        viewModel.getPasswordList().observe(this, list -> {
            adapter.submitList(list);
        });
    }

    // ---------------------------------------------------------------------------------------------
    // Swipe-to-delete implementation
    // ---------------------------------------------------------------------------------------------
    private void setupSwipeToDelete() {

        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false; // we don't support move
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) return;

                        PasswordItem item = adapter.getItemAt(position);
                        if (item != null) {
                            viewModel.deleteItem(item);
                            Toast.makeText(PasswordListActivity.this,
                                    "Deleted: " + item.getAccountName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        new ItemTouchHelper(swipeCallback)
                .attachToRecyclerView(binding.recyclerViewPasswords);
    }

    // ---------------------------------------------------------------------------------------------
    // Floating Action Button → Add new password
    // ---------------------------------------------------------------------------------------------
    private void setupFab() {
        binding.fabAdd.setOnClickListener(v -> openAddPassword());
    }

    // ---------------------------------------------------------------------------------------------
    // Navigation Helpers
    // ---------------------------------------------------------------------------------------------
    private void openAddPassword() {
        Intent intent = new Intent(this, AddEditPasswordActivity.class);
        startActivity(intent);
    }

    //private void openEditPassword(long id) {
    //    Intent intent = new Intent(this, AddEditPasswordActivity.class);
    //    intent.putExtra(Extras.EXTRA_PASSWORD_ID, id);
    //    startActivity(intent);
    //}

    private void openEditPassword(long id) {
        Intent intent = new Intent(this, AddEditPasswordActivity.class);
        intent.putExtra(AddEditPasswordActivity.EXTRA_PASSWORD_ID, id);
        startActivity(intent);
    }

    @Override
    public void onHistoryClicked(PasswordItem item) {
        // 1) Defensive checks
        if (item == null) {
            Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = item.getId();
        if (id <= 0) {
            Toast.makeText(this, "Item not saved yet (no history)", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2) Build dialog
        String title = item.getAccountName();
        if (title == null || title.trim().isEmpty()) title = "Account";

        PasswordHistoryDialog dialog = PasswordHistoryDialog.newInstance(id, title);

        // 3) Show safely (avoids IllegalStateException if called after onSaveInstanceState)
        try {
            dialog.show(getSupportFragmentManager(), "hist_" + id);
        } catch (IllegalStateException e) {
            // Fallback: allow state loss if activity is not in perfect state
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(dialog, "hist_" + id)
                    .commitAllowingStateLoss();
        }
    }
}
