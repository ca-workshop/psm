package com.ca.passwordmanager.ui.passwords;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ca.passwordmanager.data.PasswordItem;
import com.ca.passwordmanager.databinding.RowPasswordItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PasswordListAdapter extends RecyclerView.Adapter<PasswordListAdapter.VH> {

    private final PasswordRowHandler handler;
    private final List<PasswordItem> data = new ArrayList<>();

    public PasswordListAdapter(PasswordRowHandler handler) {
        this.handler = handler;
    }

    public void submit(List<PasswordItem> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowPasswordItemBinding b = RowPasswordItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PasswordItem item = data.get(position);
        holder.binding.setItem(item);
        holder.binding.setHandler(handler);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final RowPasswordItemBinding binding;
        VH(RowPasswordItemBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
