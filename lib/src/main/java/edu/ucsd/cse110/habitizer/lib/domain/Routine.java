package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Routine implements Serializable {
    private String name;
    private final @Nullable Integer id;
    private boolean isStarted;
    private boolean completed;
    private boolean isMorning;
    private Integer sortOrder;
    public Routine(@Nullable Integer id, String name, int sortOrder){
        this.sortOrder = sortOrder;
        this.id = id;
        this.name = name;
        this.isStarted = false;
        this.completed = false;
    }
    public boolean isStarted(){
        return isStarted;
    }
    public boolean isCompleted(){
        return completed;
    }
    public int sortOrder() {
        return sortOrder;
    }
    public boolean isMorning(){
        return isMorning;
    }
    public boolean isEvening(){
        return !isMorning;
    }
    public void complete(){
        this.completed = true;
    }

    public void start(){
        this.isStarted = true;
    }
    public @Nullable Integer id(){return id;}
    public String name(){ return name; }
    public void morning(){
        this.isMorning = true;
        this.isStarted = false;
        this.completed = false;
    }

    public void evening(){
        this.isMorning = false;
        this.isStarted = false;
        this.completed = false;
    }
    public Routine withSortOrder(int sortOrder) {
        return new Routine(this.id, this.name, sortOrder);
    }

    public Routine withId(int id) {
        return new Routine(id, this.name, this.sortOrder);
    }
}
