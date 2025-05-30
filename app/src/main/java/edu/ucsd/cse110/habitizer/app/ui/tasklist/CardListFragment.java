package edu.ucsd.cse110.habitizer.app.ui.tasklist;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.CreateTaskFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.RenameRoutineFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.EditTaskFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class CardListFragment extends Fragment implements RenameRoutineFragment.RenameRoutineListener, TaskTimeResetCallback {
    private MainViewModel activityModel;
    private FragmentCardListBinding binding;
    private CardListAdapter adapter;
    public Handler routineTimeHandler = new Handler();

    /**
     * ROUTINE:
     * private boolean isRoutineStarted;
     * private boolean isRoutineCompleted;
     * removing these fields because we can access them through activityModel
     */

    //time variables
    public long routineStartTime;
    public long lastTaskStartTime;
    public long Pausedtime;
    public long Resumedtime;
    private Routine routine;
    public ToggleButton togbtn;
    public  boolean isPaused = false;
    private boolean useMockTime = false;
    private long mockElapsedRoutineMillis = 0;
    private long mockElapsedTaskMillis = 0;
    public Runnable routineTimeRunnable = new Runnable() {
        public void run() {
            if (useMockTime){ // exits early if mock time is used
                return;
            }

            if (routine.isStarted() && !routine.isCompleted()&& !isPaused) {
                long elapsedMillis = System.currentTimeMillis() - routineStartTime;
                long currentTaskMillis = System.currentTimeMillis() - CardListFragment.this.getLastTaskStartTime();
                int minutes = (int) (elapsedMillis / 60000);
                int seconds = (int) (currentTaskMillis / 1000);
                binding.totalTime.setText("Total Time: " + minutes + "m");
                binding.totalTime.setVisibility(View.VISIBLE);
                if(seconds < 60){
                    //5 seconds intervals
                    int temp = seconds/5;

                    binding.currTaskTime.setText("Current Task: " + temp*5 + "s");
                } else {
                    int temp = seconds/60;
                    binding.currTaskTime.setText("Current Task: " + temp + "m");
                }
                routineTimeHandler.postDelayed(this, 1000);
            }
        }
    };

    private void startRoutineTimer() {
        routineStartTime = System.currentTimeMillis();
        lastTaskStartTime = routineStartTime;
        useMockTime = false;
        adapter.setMockTimeState(false, 0); //stops using mock time mode when in "routine mode"
        routineTimeHandler.post(routineTimeRunnable);
    }

    public void stopRoutineTimer() {
        routineTimeHandler.removeCallbacks(routineTimeRunnable);
    }

    public CardListFragment(Routine routine) {
        // Required empty public constructor
        this.routine = routine;
    }

    public static CardListFragment newInstance(Routine routine) {
        CardListFragment fragment = new CardListFragment(routine);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onTaskTimeReset(long newTime) {
        if (useMockTime) {
            mockElapsedTaskMillis = 0; // resetting task timer in mock mode
            updateMockTimeDisplay(); // sets mocktime back to 0 when task is complete
        } else {
            setLastTaskStartTime(newTime); // routine time mode
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ViewModel
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
        // Initialize the Adapter with an empty list

        
        this.adapter = new CardListAdapter(
                requireContext(),
                new ArrayList<>(),
                task -> activityModel.remove(task),
                task -> onEditTask(task),
                this,
                task -> moveTaskUp(task),
                task -> moveTaskDown(task)
                );
        activityModel.loadTasksFromRoutine(routine.id());


        // Observe task changes from ViewModel
        activityModel.getOrderedTasks().observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks)); // Ensure mutable copy
            adapter.notifyDataSetChanged();
        });
        /**
         * ROUTINE:
         * Removing these
         * this.isRoutineCompleted = activityModel.isRoutineCompleted();
         * this.isRoutineStarted = activityModel.isRoutineStarted();
         */
        if(routine.sortOrder()==-1){
            activityModel.addRoutine(routine);
        }

    }

    private void moveTaskDown(Task task) { //moving task down
        activityModel.moveTaskDown(task);
    }

    private void moveTaskUp(Task task) { //moving task up
        activityModel.moveTaskUp(task);
    }

    public void onEditTask(Task task){ // edit button functionality
        if (task.id()==null) return;

        EditTaskFragment dialog = new EditTaskFragment(task.task(), task.id(), new EditTaskFragment.TaskEditListener() {
            @Override
            public void onRename(String newName) { //rename functionality
                if (task.id() != null){
                    task.renameTask(newName);
                    activityModel.save(task);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onDelete() { //delete functionality
                if (task.id()!=null){
                    task.deleteTask();
                    activityModel.remove(task);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        if (requireContext() instanceof FragmentActivity){
            dialog.show(((FragmentActivity)requireContext()).getSupportFragmentManager(), "EditTaskDialog");
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        this.binding = FragmentCardListBinding.inflate(inflater, container, false);


        // Set adapter to ListView
        binding.taskList.setAdapter(adapter);

        // Open dialog to add new tasks
        binding.floatingActionButton.setOnClickListener(v -> {
            var dialogFragment = CreateTaskFragment.newInstance(routine);
            dialogFragment.show(getParentFragmentManager(), "CreateCardDialogFragment");
        });

        binding.routineTitle.setOnClickListener(v -> {
            var dialogFragment = RenameRoutineFragment.newInstance(routine);
            dialogFragment.show(getChildFragmentManager(), "RenameRoutineDialog");
        });

        //Mocking time implementation
        binding.stopRealTimerBtn.setOnClickListener(v -> {
            if (!useMockTime) {  //switching to mock time mode, stopping routine time
                useMockTime = true;
                stopRoutineTimer();
                mockElapsedRoutineMillis = System.currentTimeMillis() - routineStartTime;
                mockElapsedTaskMillis = System.currentTimeMillis() - lastTaskStartTime;
                adapter.setMockTimeState(true, mockElapsedTaskMillis);
                updateMockTimeDisplay();
                Toast.makeText(requireContext(), "Switched to mock time", Toast.LENGTH_SHORT).show();
            } else { //switches from mock to routine time
                useMockTime = false;
                routineStartTime = System.currentTimeMillis() - mockElapsedRoutineMillis;
                lastTaskStartTime = System.currentTimeMillis() - mockElapsedTaskMillis;
                adapter.setMockTimeState(false, 0);
                routineTimeHandler.post(routineTimeRunnable);
                Toast.makeText(requireContext(), "Switched to routine time", Toast.LENGTH_SHORT).show();
            }
        });
        // manually advance by 15 seconds
        binding.advanceTimeBtn.setOnClickListener(v -> {
            if (useMockTime) {
                mockElapsedRoutineMillis += 15000;
                mockElapsedTaskMillis += 15000;
                updateMockTimeDisplay();
            }
        });

        setupMvp();

        return binding.getRoot();
    }

    private void updateMockTimeDisplay() {
        if (!routine.isStarted() || routine.isCompleted()) return;

        adapter.setMockTimeState(true, mockElapsedTaskMillis); // ensure mocktime is used

        // Total routine time
        int routineMinutes = (int) (mockElapsedRoutineMillis / 60000);
        int routineSeconds = (int) ((mockElapsedRoutineMillis % 60000) / 1000);

        String totalTimeStr = "Total Time: ";
        if (routineMinutes > 0) totalTimeStr += routineMinutes + "m ";
        if (routineSeconds > 0 || routineMinutes == 0) totalTimeStr += routineSeconds + "s";

        binding.totalTime.setText(totalTimeStr.trim());
        binding.totalTime.setVisibility(View.VISIBLE);

        // Current task time
        int taskMinutes = (int) (mockElapsedTaskMillis / 60000);
        int taskSeconds = (int) ((mockElapsedTaskMillis % 60000) / 1000);

        String taskTimeStr = "Current Task: "; //displays current task time in both minutes and seconds
        if (taskMinutes > 0) taskTimeStr += taskMinutes + "m ";
        if (taskSeconds > 0 || taskMinutes == 0) taskTimeStr += taskSeconds + "s";

        binding.currTaskTime.setText(taskTimeStr.trim());
    }


    private void addTask(Task task, boolean append) {
        if (append) {
            activityModel.append(task);
        } else {
            activityModel.prepend(task);
        }
    }
    private void setupMvp() {
        binding.routineButton.setText(getRoutineLabel());
        binding.routineTitle.setText(routine.name());
        //binding.totalTime.setVisibility(View.GONE);
        binding.goalTime.setText("Goal Time: " + Integer.toString(routine.getGoalTime()) + "m");



        binding.routineButton.setOnClickListener(v -> {
            toggleRoutine();

            String buttonText = binding.routineButton.getText().toString();
            if ("Routine Complete".equals(buttonText)) {
                binding.routineButton.setEnabled(false);
                binding.routineButton.setBackgroundColor(android.graphics.Color.parseColor("#B0B0B0"));
            } else {
                binding.routineButton.setEnabled(true);
                int color = "End Routine".equals(buttonText) ? android.graphics.Color.parseColor("#FF6347") : android.graphics.Color.parseColor("#4CAF50");
                binding.routineButton.setBackgroundColor(color);
            }
        });
        binding.togbtn.setOnClickListener(v->{
            if(binding.togbtn.isChecked()){
                Pausedtime = System.currentTimeMillis();
                adapter.disableCheck();
                stopRoutineTimer();
                isPaused=true;
            }else{
                Resumedtime = System.currentTimeMillis();
                long ptotal = Resumedtime- Pausedtime;
                routineStartTime+= ptotal;
                lastTaskStartTime+= ptotal;
                isPaused=false;
                adapter.enableCheck();
                adapter.updatePausedTaskTimes(ptotal);
                routineTimeHandler.postDelayed(routineTimeRunnable, 1000);
            }
        });

    }

    /**
     * ROUTINE
     * Changed function to use activityModel for routine info
     */
    private String getRoutineLabel() {
        if (routine.isStarted() && !routine.isCompleted()) {
            binding.togbtn.setEnabled(true);
            return "End Routine";
        }
        else if (routine.isCompleted()){
            binding.togbtn.setEnabled(false);
            return "Routine Complete";
        }
        else{
            binding.togbtn.setEnabled(false);
            return "Start Routine";
        }
    }

    /**
     * ROUTINE:
     * Changed function to access Routine information through activityModel
     */
    private void toggleRoutine() {
        if(routine.isStarted()&& isPaused==false){
            routine.complete();
            adapter.disableCheck();
            binding.routineButton.setText(getRoutineLabel());
            long total = adapter.getTotal();
            //binding.totalTime.setText("Total Time: " + total + "m");
            //binding.totalTime.setVisibility(View.VISIBLE);
            stopRoutineTimer();
        }
        else if(isPaused == false) {
            routine.start();
            adapter.enableCheck();
            binding.routineButton.setText(getRoutineLabel());
            //track the routine start time
            startRoutineTimer();
            lastTaskStartTime = System.currentTimeMillis();
            adapter.setRoutineStartTime(lastTaskStartTime);
        }
    }


    public long getRoutineStartTime(){
        return routineStartTime;
    }
    public long getLastTaskStartTime(){
        return lastTaskStartTime;
    }
    public void setLastTaskStartTime(long startTime){
        this.lastTaskStartTime = startTime;
    }

    @Override
    public void onRoutineRenamed(Routine updatedRoutine) {
        activityModel.saveRoutine(routine);
        binding.routineTitle.setText(routine.name());
        binding.goalTime.setText("Goal Time: " + Integer.toString(routine.getGoalTime()) + "m");
    }
}