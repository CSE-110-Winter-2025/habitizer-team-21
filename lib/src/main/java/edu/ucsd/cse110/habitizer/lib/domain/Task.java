package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;


public class Task implements Serializable{
    private final @Nullable Integer id;
    private final @NonNull String task;
    private boolean completed;
    private Integer sortOrder;

    public Task(@Nullable Integer id, @NonNull String task, int sortOrder){
        this.completed = false;
        this.id = id;
        this.task = task;
        this.sortOrder = sortOrder;
    }

    public @Nullable Integer id(){return id;}

    public @NonNull String task(){return task;}

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
        return new Task(this.id, this.task, sortOrder);
    }

    public Task withId(int id) {
        return new Task(id, this.task, this.sortOrder);
    }

    public void complete(){
        this.completed = true;
    }

}
