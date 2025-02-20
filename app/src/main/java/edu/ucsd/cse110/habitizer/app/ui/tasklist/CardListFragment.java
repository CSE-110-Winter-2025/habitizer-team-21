package edu.ucsd.cse110.habitizer.app.ui.tasklist;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.CreateTaskFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class CardListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentCardListBinding binding;
    private CardListAdapter adapter;
    private boolean isRoutineStarted;
    private boolean isRoutineCompleted;

    //time variables
    private long routineStartTime;
    private long lastTaskStartTime;

    public CardListFragment() {
        // Required empty public constructor
    }

    public static CardListFragment newInstance() {
        CardListFragment fragment = new CardListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        this.adapter = new CardListAdapter(requireContext(), new ArrayList<>());

        // Observe task changes from ViewModel
        activityModel.getOrderedTasks().observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks)); // Ensure mutable copy
            adapter.notifyDataSetChanged();
        });

        this.isRoutineCompleted = false;
        this.isRoutineStarted = false;
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
            var dialogFragment = CreateTaskFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateCardDialogFragment");
        });

        setupMvp();

        return binding.getRoot();
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

        var isEvening = activityModel.isEvening();
        if(isEvening){
            binding.routineTitle.setText("Evening Routine");
            binding.totalTime.setVisibility(View.GONE);
        }
        else{
            binding.routineTitle.setText("Morning Routine");
            binding.totalTime.setVisibility(View.GONE);
        }

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

    }

    private String getRoutineLabel() {
        if (isRoutineStarted && !isRoutineCompleted) {
            return "End Routine";
        }
        else if (isRoutineCompleted){
            return "Routine Complete";
        }
        else{
            return "Start Routine";
        }
    }

    private void toggleRoutine() {
        if(isRoutineStarted){
            isRoutineCompleted = true;
            adapter.disableCheck();
            binding.routineButton.setText(getRoutineLabel());
            long total = adapter.getTotal();
            binding.totalTime.setText("Total: " + total + "m");
            binding.totalTime.setVisibility(View.VISIBLE);
        }
        else {
            isRoutineStarted = true;
            adapter.enableCheck();
            binding.routineButton.setText(getRoutineLabel());
            //track the routine start time
            routineStartTime = System.currentTimeMillis();
            lastTaskStartTime = routineStartTime;
            adapter.setRoutineStartTime(routineStartTime);
        }
    }

    public boolean isRoutineCompleted() {
        return isRoutineCompleted;
    }

    public boolean isRoutineStarted() {
        return isRoutineStarted;
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
}