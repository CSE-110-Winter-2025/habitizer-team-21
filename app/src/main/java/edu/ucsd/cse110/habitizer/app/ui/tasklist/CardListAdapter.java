package edu.ucsd.cse110.habitizer.app.ui.tasklist;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.graphics.Paint;
import java.util.function.Consumer;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemCardBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class CardListAdapter extends ArrayAdapter<Task> {
    private boolean checkEnabled;
    private final HashSet<Integer> struckThroughTasks = new HashSet<>();
    private long routineStartTime;
    private long lastTaskTime;
    private long total = 0;
    public CardListAdapter(Context context, List<Task> tasks) {
        super(context, 0, new ArrayList<>(tasks)); // Ensuring a mutable list
        checkEnabled = false;
    }
    public void setRoutineStartTime(long time) {
        this.routineStartTime = time;
        this.lastTaskTime = time;
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
        binding.taskTime.setVisibility(View.GONE);

        // Applying strikethrough if the task was previously clicked on
        if (struckThroughTasks.contains(position)) {
            binding.Task.setPaintFlags(binding.Task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        // Toggle strikethrough on click for each task
        binding.getRoot().setOnClickListener(v -> {
            if (!(struckThroughTasks.contains(position)) && checkEnabled) {
                struckThroughTasks.add(position);
                binding.Task.setPaintFlags(binding.Task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding.taskTime.setVisibility(View.VISIBLE);
                long timeTook = (System.currentTimeMillis() - lastTaskTime);
                long m = timeTook / 60000;
                long s = (timeTook %60000) / 1000;
                if (s <= 30){
                    m++;
                }
                lastTaskTime = System.currentTimeMillis();
                //long minutes = routineStartTime;
                binding.taskTime.setText("Time: " + m + "m");
                total += m;
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

    public void enableCheck(){
        this.checkEnabled = true;
    }

    public void disableCheck(){
        this.checkEnabled = false;
    }
    public long getTotal(){
        return total;
    }
}
