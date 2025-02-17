package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.widget.Button;
import android.widget.ListView;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.util.Subject;


public class MainViewModel extends ViewModel {
    private final Subject<List<Task>> orderedTasks;
    private final TaskRepository taskRepository;
    private final Subject<Boolean> isRoutineStarted;
    private final Subject<String> routineButtonLabel;
    private final Subject<Boolean> isRoutineCompleted;
    private final HashSet<Integer> strikethroughItems;



    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository());
                    });


    public MainViewModel(TaskRepository taskRepository) {
        this.isRoutineStarted = new Subject<>();
        this.isRoutineStarted.setValue(false);
        this.isRoutineCompleted = new Subject<>();
        this.isRoutineCompleted.setValue(false);
        this.routineButtonLabel = new Subject<>();
        updateRoutineButton();
        this.taskRepository = taskRepository;
        this.orderedTasks = new Subject<>();
        this.orderedTasks.setValue(new ArrayList<>()); // Initialize with an empty list
        this.strikethroughItems = new HashSet<>();

        taskRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var newOrderedCards = cards.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());

            orderedTasks.setValue(newOrderedCards);
        });

    }

    public Subject<List<Task>> getOrderedTasks() {
        return orderedTasks;
    }

    public void append(Task task) {
        taskRepository.append(task);
    }

    public void prepend(Task task) {
        taskRepository.prepend(task);
    }

    public Subject<String> getRoutineButton() {
        return routineButtonLabel;
    }

    public void toggleTaskStrikeThrough(int position) {
        if (strikethroughItems.contains(position)) {
            strikethroughItems.remove(position);
        } else {
            strikethroughItems.add(position);
        }
    }
    public boolean isTaskStruckThrough(int position) {
        return strikethroughItems.contains(position);
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
