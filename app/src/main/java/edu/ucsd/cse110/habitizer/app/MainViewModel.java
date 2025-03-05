package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;


import java.util.HashSet;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.util.Subject;


public class MainViewModel extends ViewModel {
    private final Subject<List<Task>> orderedTasks;
    private final Subject<List<Routine>> orderedRoutines;
    private TaskRepository taskRepository;
    private RoutineRepository routineRepository;
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
                        return new MainViewModel(app.getTaskRepository(), app.getRoutineRepository());
                    });


    /**
     * ROUTINE:
     * Initializes routine as a parameter for the MainViewModel, which is initialized
     * within the HabitizerApplication
     * @param taskRepository from the app
     * @param routineRepository from the app
     */
    public MainViewModel(TaskRepository taskRepository, RoutineRepository routineRepository) {
        this.taskRepository = taskRepository;
        this.routineRepository = routineRepository;
        this.orderedTasks = new Subject<>();
        this.orderedTasks.setValue(new ArrayList<>()); // Initialize with an empty list
        this.orderedRoutines = new Subject<>();
        this.orderedRoutines.setValue(new ArrayList<>());
        this.strikethroughItems = new HashSet<>();

        taskRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var newOrderedCards = cards.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());

            orderedTasks.setValue(newOrderedCards);
        });

        routineRepository.findAll().observe(routines -> {
            if (routines == null) return;

            var newOrderedRoutines = routines.stream()
                    .sorted(Comparator.comparingInt(Routine::sortOrder))
                    .collect(Collectors.toList());

            orderedRoutines.setValue(newOrderedRoutines);
        });

    }

    public Subject<List<Task>> getOrderedTasks() {
        return orderedTasks;
    }
    public Subject<List<Routine>> getOrderedRoutines() {
        return orderedRoutines;
    }

    public void append(Task task) {
        taskRepository.append(task);
    }

    public void prepend(Task task) {
        taskRepository.prepend(task);
    }

    public void addRoutine(Routine routine){
        routineRepository.append(routine);
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

    public void loadTasksFromRoutine(int id){
        taskRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore
            var newOrderedCards = cards.stream()
                    .filter(card -> card.getRoutineId() == id)
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());

            orderedTasks.setValue(newOrderedCards);
        });

    }
    public void saveRoutine(Routine routine){
        routineRepository.save(routine);
    }


}
