package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class HabitizerApplication extends Application {
    private boolean isEvening;
    private InMemoryDataSource dataSource;
    private TaskRepository taskRepository;
    private RoutineRepository routineRepository;
    private Routine routine;

    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.fromDefault();
        this.taskRepository = new TaskRepository(dataSource);
        this.routineRepository = new RoutineRepository(dataSource);

        this.routine = new Routine(0,"PLACEHOLDER",0);
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public RoutineRepository getRoutineRepository(){ return routineRepository;}
    public Routine getRoutine() {return routine;}
}