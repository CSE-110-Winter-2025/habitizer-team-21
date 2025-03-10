package edu.ucsd.cse110.habitizer.app.data.routinedb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.app.data.taskdb.TaskEntity;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Entity(tableName = "routines")
public class RoutineEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public Integer id = null;

    @ColumnInfo(name="goal_time")
    public int goal_time;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name = "started")
    public Boolean started = false;
    @ColumnInfo(name = "completed")
    public Boolean completed = false;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    RoutineEntity(@NonNull String name, int sortOrder){
        this.name = name;
        this.sortOrder = sortOrder;
    }

    public static RoutineEntity fromRoutine(@NonNull Routine routine){
        var temp = new RoutineEntity(routine.name(),routine.sortOrder());
        temp.id = routine.id();
        temp.completed = routine.isCompleted();
        temp.started = routine.isStarted();
        temp.goal_time = routine.getGoalTime();
        return temp;
    }

    public @NonNull Routine toRoutine(){
        return new Routine(id, name, sortOrder, goal_time);
    }
}
