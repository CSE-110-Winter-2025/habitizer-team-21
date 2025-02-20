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

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.util.Subject;


public class MainViewModel extends ViewModel {
    private final Subject<List<Task>> orderedTasks;
    private TaskRepository taskRepository;
    /**
     * ROUTINE:
     * Added Routine field
     */
    private Routine routine;
    private final HashSet<Integer> strikethroughItems;


    /**
     * ROUTINE:
     * modified return statement to call HabitizerApplication's
     * getRoutine method
     */
    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository(), app.getRoutine());
                    });


    /**
     * ROUTINE:
     * Initializes routine as a parameter for the MainViewModel, which is initialized
     * within the HabitizerApplication
     * @param taskRepository from the app
     * @param routine from the app
     */
    public MainViewModel(TaskRepository taskRepository, Routine routine) {
        this.taskRepository = taskRepository;
        this.routine = routine;
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

    /**
     * ROUTINE:
     * Function now calls the Routine class's evening method
     */
    public void evening(){
        routine.evening();
        taskRepository = new TaskRepository(InMemoryDataSource.evening());
        taskRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var newOrderedCards = cards.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());

            orderedTasks.setValue(newOrderedCards);
        });

    }

    /**
     * ROUTINE:
     * Function now calls the Routine class's morning method
     */
    public void morning(){
        routine.morning();
        taskRepository = new TaskRepository(InMemoryDataSource.fromDefault());
        taskRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var newOrderedCards = cards.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());

            orderedTasks.setValue(newOrderedCards);
        });

    }

    /**
     * ROUTINE:
     * Function changed to use routine's isEvening method
     * @return boolean value indicating if it's evening
     */
    public boolean isEvening(){
        return routine.isEvening();
    }
    public boolean isRoutineStarted(){
        return routine.isStarted();
    }
    public boolean isRoutineCompleted(){
        return routine.isCompleted();
    }
    public void completeRoutine(){
        routine.complete();
    }
    public void startRoutine(){
        routine.start();
    }

}
