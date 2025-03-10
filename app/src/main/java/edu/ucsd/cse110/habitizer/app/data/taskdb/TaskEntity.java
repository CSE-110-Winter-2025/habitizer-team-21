package edu.ucsd.cse110.habitizer.app.data.taskdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public Integer id = null;

    @ColumnInfo(name="routineID")
    public Integer routineID = null;

    @ColumnInfo(name="time")
    public Long time = null;
    @ColumnInfo(name="displayTime")
    public Long displayTime = null;

    @ColumnInfo(name="task")
    public String task;

    @ColumnInfo(name = "completed")
    public Boolean completed = false;
    @ColumnInfo(name = "deleted")
    public Boolean deleted = false;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    TaskEntity(@NonNull String task, int sortOrder){
        this.task = task;
        this.sortOrder = sortOrder;
    }
    public static TaskEntity fromTask(@NonNull Task task){
        var temp = new TaskEntity(task.task(),task.sortOrder());
        temp.id = task.id();
        temp.completed = task.completed();
        temp.deleted = task.isDeleted();
        temp.time = task.getTimeSpent();
        temp.displayTime = task.getDisplayedTime();
        temp.routineID = task.getRoutineId();
        return temp;
    }
    public @NonNull Task toTask(){
        return new Task(id,task,sortOrder,routineID);
    }
}
