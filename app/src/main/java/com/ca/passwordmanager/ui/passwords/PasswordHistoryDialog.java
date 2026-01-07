package com.ca.passwordmanager.ui.passwords;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.ca.passwordmanager.data.PasswordHistory;
import com.ca.passwordmanager.util.DateUi;

import java.util.ArrayList;
import java.util.List;

public class PasswordHistoryDialog extends DialogFragment {

    private static final String ARG_ID = "id";
    private static final String ARG_TITLE = "title";

    public static PasswordHistoryDialog newInstance(long itemId, String title) {
        PasswordHistoryDialog f = new PasswordHistoryDialog();
        Bundle b = new Bundle();
        b.putLong(ARG_ID, itemId);
        b.putString(ARG_TITLE, title);
        f.setArguments(b);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        long itemId = requireArguments().getLong(ARG_ID);
        String title = requireArguments().getString(ARG_TITLE, "History");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );

        PasswordHistoryViewModel vm = new ViewModelProvider(this).get(PasswordHistoryViewModel.class);
        //vm.historyFor(itemId).observe(this, list -> adapter.clearAndAddAll(map(list)));
        vm.historyFor(itemId).observe(this, list -> {
            adapter.clear();
            adapter.addAll(map(list));
            adapter.notifyDataSetChanged();
        });

        return new AlertDialog.Builder(requireContext())
                .setTitle("Password history: " + title)
                .setAdapter(adapter, null)
                .setPositiveButton("Close", (d, w) -> d.dismiss())
                .create();
    }

    private List<String> map(List<PasswordHistory> list) {
        List<String> out = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            out.add("(No history yet)");
            return out;
        }
        for (PasswordHistory h : list) {
            String line = DateUi.format(h.getChangedAtTimestamp()) + "  •  " + safe(h.getOldPassword());
            out.add(line);
        }
        return out;
    }

    private String safe(String s) {
        return (s == null) ? "(null)" : s;
    }

    // helper for ArrayAdapter (Java)
    private static class ClearAddAllAdapter extends ArrayAdapter<String> {
        public ClearAddAllAdapter(@NonNull android.content.Context context, int resource) {
            super(context, resource);
        }
        void clearAndAddAll(List<String> items) {
            clear();
            if (items != null) addAll(items);
            notifyDataSetChanged();
        }
    }
}
