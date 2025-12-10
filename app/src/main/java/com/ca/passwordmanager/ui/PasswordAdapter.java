/*
 * Project : PasswordManagerMVVM
 * File    : PasswordAdapter.java
 * Author  : Alice & Bob
 *
 * Version : 1.2.0 (tag: v1.2.0)
 * Date    : 2025-12-09
 *
 * Summary :
 *    RecyclerView adapter for the password list screen.
 *    Uses ListAdapter + DiffUtil for efficient updates and
 *    Data Binding for binding PasswordItem to row_password_item.xml.
 *
 * History :
 *   2025-12-09  1.2.0  Alice  Initial implementation with click listener and getItemAt().
 */

package com.ca.passwordmanager.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ca.passwordmanager.R;
import com.ca.passwordmanager.data.PasswordItem;
import com.ca.passwordmanager.databinding.RowPasswordItemBinding;

public class PasswordAdapter extends ListAdapter<PasswordItem, PasswordAdapter.PasswordViewHolder> {

    // ---------------------------------------------------------------------------------------------
    // Click listener interface
    // ---------------------------------------------------------------------------------------------
    public interface OnItemClickListener {
        void onItemClick(PasswordItem item);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // ---------------------------------------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------------------------------------
    public PasswordAdapter() {
        super(DIFF_CALLBACK);
    }

    // Public accessor so Activity/Fragment can safely retrieve the item
    public PasswordItem getItemAt(int position) {
        return getItem(position); // ListAdapter's protected getItem()
    }

    // ---------------------------------------------------------------------------------------------
    // DiffUtil for efficient list updates
    // ---------------------------------------------------------------------------------------------
    private static final DiffUtil.ItemCallback<PasswordItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<PasswordItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull PasswordItem oldItem,
                                               @NonNull PasswordItem newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull PasswordItem oldItem,
                                                  @NonNull PasswordItem newItem) {
                    return oldItem.getAccountName().equals(newItem.getAccountName())
                            && oldItem.getUsernameOrEmail().equals(newItem.getUsernameOrEmail())
                            && oldItem.getPassword().equals(newItem.getPassword())
                            && oldItem.getLastChangedTimestamp() == newItem.getLastChangedTimestamp();
                }
            };

    // ---------------------------------------------------------------------------------------------
    // ViewHolder creation
    // ---------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowPasswordItemBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.row_password_item,
                parent,
                false
        );
        return new PasswordViewHolder(binding);
    }

    // ---------------------------------------------------------------------------------------------
    // ViewHolder binding
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        final PasswordItem item = getItem(position);
        holder.bind(item);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && item != null) {
                listener.onItemClick(item);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // ViewHolder inner class
    // ---------------------------------------------------------------------------------------------
    static class PasswordViewHolder extends RecyclerView.ViewHolder {

        private final RowPasswordItemBinding binding;

        PasswordViewHolder(@NonNull RowPasswordItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(PasswordItem item) {
            binding.setItem(item);
            binding.executePendingBindings();
        }
    }
}
