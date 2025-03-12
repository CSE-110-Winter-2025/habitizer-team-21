package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.habitizer.app.data.routinedb.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.routinedb.RoutiineDatabase;
import edu.ucsd.cse110.habitizer.app.data.taskdb.RoomTaskRepository;
import edu.ucsd.cse110.habitizer.app.data.taskdb.TasksDatabase;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTaskRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class HabitizerApplication extends Application {
    private InMemoryDataSource dataSource;
    private TaskRepository taskRepository;
    private RoutineRepository routineRepository;


    @Override
    public void onCreate() {
        super.onCreate();

      //  this.dataSource = InMemoryDataSource.fromDefault();
        //this.taskRepository = new SimpleTaskRepository(dataSource);
       // this.routineRepository = new SimpleRoutineRepository(dataSource);
        var taskDB = Room.databaseBuilder(
                        getApplicationContext(),
                        TasksDatabase.class,
                        "tasks-database"
                )
                .allowMainThreadQueries()
                .build();

        var routineDB = Room.databaseBuilder(
                        getApplicationContext(),
                        RoutiineDatabase.class,
                        "routines-database"
                )
                .allowMainThreadQueries()
                .build();

        this.taskRepository = new RoomTaskRepository(taskDB.taskDao());
        this.routineRepository = new RoomRoutineRepository(routineDB.routineDao());

        var sharedPreferences = getSharedPreferences("habitizer", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if(isFirstRun){
            taskRepository.save(InMemoryDataSource.fromDefault().getTasks());
            routineRepository.save(InMemoryDataSource.fromDefault().getRoutines());

            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public RoutineRepository getRoutineRepository(){ return routineRepository;}

}