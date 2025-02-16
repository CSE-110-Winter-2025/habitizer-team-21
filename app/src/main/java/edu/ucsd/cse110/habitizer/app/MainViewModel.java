package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.ucsd.cse110.habitizer.lib.util.Subject;


public class MainViewModel extends ViewModel {
    private final Subject<List<String>> orderedTasks;
    private final Subject<Boolean> isRoutineStarted;
    private final Subject<String> routineButtonLabel;
    private final Subject<Boolean> isRoutineCompleted;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel();
                    });

    public MainViewModel() {
        this.isRoutineStarted = new Subject<>();
        this.isRoutineStarted.setValue(false);
        this.isRoutineCompleted = new Subject<>();
        this.isRoutineCompleted.setValue(false);
        this.orderedTasks = new Subject<>();
        this.orderedTasks.setValue(new ArrayList<>()); // Initialize with an empty list
        this.isRoutineStarted.setValue(false);
        this.routineButtonLabel = new Subject<>();
        updateRoutineButton();
    }

    public Subject<List<String>> getOrderedTasks() {
        return orderedTasks;
    }

    public void append(String task) {
        if (task == null || task.trim().isEmpty()) return;
        var tasks = new ArrayList<>(orderedTasks.getValue());
        tasks.add(task);
        orderedTasks.setValue(tasks);
    }

    public void prepend(String task) {
        if (task == null || task.trim().isEmpty()) return;
        var tasks = new ArrayList<>(orderedTasks.getValue());
        tasks.add(0, task);
        orderedTasks.setValue(tasks);
    }

    public Subject<String> getRoutineButton() {
        return routineButtonLabel;
    }

    // Call this method whenever routine state changes
    private void updateRoutineButton() {
        if (Boolean.TRUE.equals(isRoutineStarted.getValue()) && Boolean.FALSE.equals(isRoutineCompleted.getValue())) {
            routineButtonLabel.setValue("End Routine");
        }
        else if (Boolean.TRUE.equals(isRoutineCompleted.getValue())){
            routineButtonLabel.setValue("Routine Complete");
        }
        else{
            routineButtonLabel.setValue("Start Routine");
        }
    }

    // Function to toggle routine state
    public void toggleRoutine() {
        if(Boolean.TRUE.equals(isRoutineStarted.getValue())){
            boolean newState = !Boolean.TRUE.equals(isRoutineCompleted.getValue());
            isRoutineCompleted.setValue(newState);
            updateRoutineButton();
        }
        else {
            boolean newState = !Boolean.TRUE.equals(isRoutineStarted.getValue());
            isRoutineStarted.setValue(newState);
            updateRoutineButton();
        }// Update button text
    }

}