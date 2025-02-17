package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class HabitizerApplication extends Application {
    private boolean isEvening;
    private InMemoryDataSource dataSource;
    private TaskRepository taskRepository;
    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.fromDefault();
        this.taskRepository = new TaskRepository(dataSource);
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public static TaskRepository eveningTasks(){
        var dataSource = InMemoryDataSource.evening();
        var taskRepository = new TaskRepository(dataSource);
        return taskRepository;
    }

    public static TaskRepository morningTasks(){
        var dataSource = InMemoryDataSource.fromDefault();
        var taskRepository = new TaskRepository(dataSource);
        return taskRepository;
    }
}