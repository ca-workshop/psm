/*
 * Project : PasswordManagerMVVM
 * File    : PasswordListActivity.java
 * Author  : Alice & Bob
 *
 * Version : 1.2.0 (tag: v1.2.0)
 * Date    : 2025-11-06
 *
 * Summary :
 *    Displays a list of saved passwords using RecyclerView + ListAdapter.
 *    Supports:
 *      - Swipe-to-delete
 *      - Tap to edit item
 *      - FAB → add new password
 *      - LiveData observation from ViewModel
 *      - DataBinding + DI (AppContainer)
 *
 * History :
 *   2025-11-03  1.0.0  Alice  Initial UI + RecyclerView
 *   2025-11-04  1.1.0  Bob    Integrated Room + ViewModel + LiveData
 *   2025-11-05  1.2.0  Bob    Added swipe-to-delete + DI
 */

package com.ca.passwordmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ca.passwordmanager.ui.PasswordAdapter;
import com.ca.passwordmanager.PasswordManagerApp;
import com.ca.passwordmanager.R;
import com.ca.passwordmanager.data.PasswordRepository;
import com.ca.passwordmanager.databinding.ActivityPasswordListBinding;
import com.ca.passwordmanager.data.PasswordItem;
import com.google.android.material.divider.MaterialDividerItemDecoration;

public class PasswordListActivity extends AppCompatActivity {

    private ActivityPasswordListBinding binding;
    private PasswordListViewModel viewModel;
    private PasswordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout with DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_list);
        binding.setLifecycleOwner(this);

        // DI – get repository from PasswordManagerApp → AppContainer
        PasswordManagerApp app = (PasswordManagerApp) getApplication();
        PasswordRepository repo = app.getAppContainer().passwordRepository;

        ViewModelFactory factory = new ViewModelFactory(repo);
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

    private void openEditPassword(long id) {
        Intent intent = new Intent(this, AddEditPasswordActivity.class);
        intent.putExtra("EXTRA_PASSWORD_ID", id);
        startActivity(intent);
    }
}
