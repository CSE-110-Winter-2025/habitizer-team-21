package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class HabitizerApplication extends Application {
    private boolean isEvening;
    private InMemoryDataSource dataSource;
    private TaskRepository taskRepository;
    private Routine routine;

    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.fromDefault();
        this.taskRepository = new TaskRepository(dataSource);
        this.routine = new Routine();
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public Routine getRoutine() {return routine;}
}