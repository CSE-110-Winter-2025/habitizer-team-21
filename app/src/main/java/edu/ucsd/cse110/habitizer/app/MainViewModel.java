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

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel();
                    });

    public MainViewModel() {
        this.orderedTasks = new Subject<>();
        this.orderedTasks.setValue(new ArrayList<>()); // Initialize with an empty list
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

}