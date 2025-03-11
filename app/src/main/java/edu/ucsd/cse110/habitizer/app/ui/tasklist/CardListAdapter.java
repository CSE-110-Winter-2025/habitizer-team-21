package edu.ucsd.cse110.habitizer.app.ui.tasklist;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemCardBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.EditTaskFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

interface TaskTimeResetCallback {
    void onTaskTimeReset(long newTime);
}
public class CardListAdapter extends ArrayAdapter<Task> {
    private boolean checkEnabled;
    private final HashSet<Integer> struckThroughTaskIds = new HashSet<>();
    private final HashMap<Integer, String> frozenTaskTimes = new HashMap<>();
    private long routineStartTime;
    private long lastTaskTime;
    private long total = 0;
    private final Consumer<Integer> onDeleteClick;
    private final Consumer<Task> onEditClick;
    private TaskTimeResetCallback taskTimeResetCallback;
    private boolean useMockTime = false;
    private long mockElapsedTaskMillis = 0;




    public CardListAdapter(Context context, List<Task> tasks, Consumer<Integer> onDeleteClick, Consumer<Task> onEditClick, TaskTimeResetCallback callback) {
        super(context, 0, new ArrayList<>(tasks)); // Ensuring a mutable list
        this.onDeleteClick = onDeleteClick != null ? onDeleteClick : id -> {};
        this.onEditClick = onEditClick != null ? onEditClick : task -> {};
        this.taskTimeResetCallback = callback;
        checkEnabled = false;
    }
    public void setRoutineStartTime(long time) {
        this.routineStartTime = time;
        this.lastTaskTime = time;
    }
    public long getLastTaskTime(){
        return lastTaskTime;
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

        // prepending should not recycle old behavior
        binding.Task.setPaintFlags(binding.Task.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        binding.taskTime.setText("");
        binding.taskTime.setVisibility(View.GONE);

        assert task != null;

        // Set task text
        binding.Task.setText(task.task());
        binding.taskTime.setVisibility(View.GONE);

        if (task.id() != null && struckThroughTaskIds.contains(task.id())) {
            binding.Task.setPaintFlags(binding.Task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String frozenTime = frozenTaskTimes.get(task.id()); //preserving time after complete and adding tasks when routine running
            if (frozenTime != null) {
                binding.taskTime.setText(frozenTime);
                binding.taskTime.setVisibility(View.VISIBLE);
            } else {
                binding.taskTime.setVisibility(View.GONE);
            }
        }

        // Toggle strikethrough on click for each task
        binding.getRoot().setOnClickListener(v -> {
            if (!struckThroughTaskIds.contains(task.id()) && checkEnabled) {
                struckThroughTaskIds.add(task.id());
                binding.Task.setPaintFlags(binding.Task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding.taskTime.setVisibility(View.VISIBLE);

                long timeTook = useMockTime ? mockElapsedTaskMillis : (System.currentTimeMillis() - lastTaskTime); //checks if mocktime is called
                int seconds = (int) (timeTook / 1000);
                String timeText;

                if (useMockTime){ // resetting timer depending on mode
                    if (taskTimeResetCallback != null){
                        taskTimeResetCallback.onTaskTimeReset(0); // resets mock task timer
                    }
                } else {
                    lastTaskTime = System.currentTimeMillis();
                    if (taskTimeResetCallback != null){
                        taskTimeResetCallback.onTaskTimeReset(System.currentTimeMillis());
                    }
                }

                if (seconds < 60) {
                    int rounded = (seconds / 5) * 5;
                    timeText = "Time: " + rounded + "s";
                } else if (seconds == 60) {
                    timeText = "Time: 1m";
                } else {
                    int minutes = (int) Math.ceil(seconds / 60.0); //rounds time up if over 60 seconds
                    timeText = "Time: " + minutes + "m";
                    total += minutes;
                }

                binding.taskTime.setText(timeText);
                frozenTaskTimes.put(task.id(), timeText);

            }
        });
        binding.editButton.setOnClickListener(v -> { // handles edit button function functionality
            if (task.id() == null){
                return;
            }
            onEditClick.accept(task);
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
    public void updatePausedTaskTimes(long ptotal){
        lastTaskTime+= ptotal;
    }
    public void setMockTimeState(boolean isMock, long mockTaskTime) { //updates mock state and current task time
        this.useMockTime = isMock;
        this.mockElapsedTaskMillis = mockTaskTime;
    }

}
