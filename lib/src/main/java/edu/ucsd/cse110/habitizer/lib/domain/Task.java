package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;


public class Task implements Serializable{
    private final @Nullable Integer id;
    private @NonNull String task;
    private boolean completed;
    private boolean deleted;
    private Integer sortOrder;

    private long timeSpent;

    private long timeDisplayed;
    private int routineId;

    public Task(@Nullable Integer id, @NonNull String task, int sortOrder, int routineId){
        this.completed = false;
        this.deleted = false;
        this.id = id;
        this.task = task;
        this.sortOrder = sortOrder;
        this.timeSpent = 0L;
        this.routineId = routineId;
    }

    public @Nullable java.lang.Integer id(){return id;}

    public @NonNull String task(){return task;}

    public @NonNull Boolean completed(){ return completed;}
    public int getRoutineId(){return routineId;}

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o == null || getClass()!=o.getClass()) return false;
        Task tasks = (Task) o;
        return Objects.equals(id, tasks.id) && Objects.equals(task, tasks.task) && Objects.equals(sortOrder, tasks.sortOrder);
    }


    public int hashcode() {return Objects.hash(id, task, sortOrder);}

    public int sortOrder() {
        return sortOrder;
    }

    public Task withSortOrder(int sortOrder) {
        return new Task(this.id, this.task, sortOrder, this.routineId);
    }

    public Task withId(int id) {
        return new Task(id, this.task, this.sortOrder, this.routineId);
    }

    public void complete(){
        this.completed = true;
    }
    public long getTimeSpent() {
        return this.timeSpent;
    }

    public void setTimeSpent(long timeSpent){
        this.timeSpent = timeSpent;
    }

    public long getDisplayedTime(){ return this.timeDisplayed;}

    public void setDisplayedTime(long timeDisplayed){this.timeDisplayed = timeDisplayed;}

    public void renameTask(@NonNull String newName){this.task = newName;}

    public boolean isDeleted() {return deleted;}
    public void deleteTask(){
        this.deleted = true;
    }
}
