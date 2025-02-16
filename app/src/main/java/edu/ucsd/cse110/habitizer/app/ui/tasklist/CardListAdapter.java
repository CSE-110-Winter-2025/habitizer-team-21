package edu.ucsd.cse110.habitizer.app.ui.tasklist;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.databinding.ListItemCardBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class CardListAdapter extends ArrayAdapter<Task> {

    public CardListAdapter(Context context, List<Task> tasks) {
        super(context, 0, new ArrayList<>(tasks)); // Ensuring a mutable list
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemCardBinding binding;

        if (convertView != null) {
            binding = ListItemCardBinding.bind(convertView);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemCardBinding.inflate(layoutInflater, parent, false);
        }

        Task task = getItem(position);
        assert task != null;

        // Set task text
        binding.Task.setText(task.task());

        return binding.getRoot();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}