package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Tasks {
    private final @Nullable Integer id;
    private final @NonNull String task;
    private Tasks Objects;

    public Tasks(@Nullable Integer id, @NonNull String task){
        this.id = id;
        this.task = task;
    }

    public @Nullable Integer id(){return id;}

    public @NonNull String task(){return task;}


    public boolean equals(Object o){
        if(this==o) return true;
        if(o == null || getClass()!=o.getClass()) return false;
        Tasks tasks = (Tasks) o;
        return Objects.equals(id, tasks.id) && Objects.equals(task, tasks.task);
    }

    public int hashcode() {return Objects.hash(id, task);}
}
