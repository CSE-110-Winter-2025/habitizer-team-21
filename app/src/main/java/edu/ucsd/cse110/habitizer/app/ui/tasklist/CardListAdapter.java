package edu.ucsd.cse110.habitizer.app.ui.tasklist;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.databinding.ListItemCardBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class CardListAdapter extends ArrayAdapter<Task> {
    private final Consumer<Task> onTaskClicked;

    private final HashSet<Integer> struckThroughTasks = new HashSet<>();
    public CardListAdapter(Context context, List<Task> tasks, Consumer<Task> onTaskClicked) {
        super(context, 0, new ArrayList<>(tasks)); // Ensuring a mutable list
        this.onTaskClicked = onTaskClicked;
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

        if(task.getTimeSpent() > 0) {
            long ms = task.getTimeSpent();
            long minutes = ms / 60000;
            long seconds = (ms % 60000) / 1000;
            if(seconds <= 30){
                minutes = minutes + 1;
            }
            task.setDisplayedTime(minutes);
            binding.timeSpent.setText("Time: " + minutes + "m");
            //activityModel.getOrderedTasks().setValue(activityModel.getOrderedTasks().getValue());

        } else {
            binding.timeSpent.setText("");
        }

        // Applying strikethrough if the task was previously clicked on
        if (struckThroughTasks.contains(position)) {
            binding.Task.setPaintFlags(binding.Task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            binding.Task.setPaintFlags(binding.Task.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }


        // Toggle strikethrough on click for each task
        binding.getRoot().setOnClickListener(v -> {
            if (struckThroughTasks.contains(position)) {
                onTaskClicked.accept(task);
                struckThroughTasks.remove(position);
                binding.Task.setPaintFlags(binding.Task.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            } else {
                onTaskClicked.accept(task);
                struckThroughTasks.add(position);
                binding.Task.setPaintFlags(binding.Task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });



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