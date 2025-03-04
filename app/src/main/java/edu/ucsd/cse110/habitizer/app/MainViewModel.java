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
    private Subject<Boolean> isEvening;
    private final Subject<List<Task>> orderedTasks;
    private TaskRepository taskRepository;
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
        this.taskRepository = taskRepository;
        this.orderedTasks = new Subject<>();
        this.orderedTasks.setValue(new ArrayList<>()); // Initialize with an empty list
        this.strikethroughItems = new HashSet<>();
        this.isEvening = new Subject<>();
        isEvening.setValue(false);

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

    public void evening(){
        taskRepository = HabitizerApplication.eveningTasks();
        taskRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var newOrderedCards = cards.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());

            orderedTasks.setValue(newOrderedCards);
            isEvening.setValue(true);
        });

    }

    public void morning(){
        taskRepository = HabitizerApplication.morningTasks();
        taskRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var newOrderedCards = cards.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());

            orderedTasks.setValue(newOrderedCards);
            isEvening.setValue(false);
        });

    }

    public boolean isEvening(){
        return isEvening.getValue();
    }

    public void remove(Task task) { // deletes task
        if (task != null && task.id() != null){
            taskRepository.remove(task.id());
        }
    }
    public void remove(int taskId) { // deletes task
            taskRepository.remove(taskId);
    }
    public void save(Task task) { //saves task
        taskRepository.save(task);
    }
    public void save(List<Task> tasks) { //saves list
        taskRepository.save(tasks);
    }

}

