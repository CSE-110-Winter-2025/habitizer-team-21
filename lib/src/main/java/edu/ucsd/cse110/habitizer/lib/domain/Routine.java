package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Routine implements Serializable {
    private String name;
    private int goalTime;
    private @Nullable Integer id;
    private boolean isStarted;
    private boolean completed;
    private Integer sortOrder;
    public Routine(@Nullable Integer id, String name, int sortOrder, int goalTime){
        this.sortOrder = sortOrder;
        this.id = id;
        this.name = name;
        this.isStarted = false;
        this.completed = false;
        this.goalTime = goalTime;
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

    public void complete(){
        this.completed = true;
    }

    public void start(){
        this.isStarted = true;
    }
    public @Nullable Integer id(){return id;}
    public String name(){ return name; }

    public void setGoalTime(int goalTime) {
        this.goalTime = goalTime;
    }
    public int getGoalTime(){
        return goalTime;
    }

    public void reset(){
        this.isStarted = false;
        this.completed = false;
    }

    public Routine withSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Routine withId(int id) {
        this.id = id;
        return this;
    }

    public void rename(String name){
        this.name = name;
    }

    public static class RoutineBuilder{
        private String name = "New Routine";
        private int id = -1;
        private int sortOrder = -1;
        private int goalTime = 0;

        public RoutineBuilder setName(String name){this.name = name; return this;}
        public RoutineBuilder setId(int id){this.id = id; return this;}
        public RoutineBuilder setSortOrder(int sortOrder){this.sortOrder = sortOrder; return this;}
        public RoutineBuilder setGoalTime(int time){this.goalTime = time; return this;}
        public Routine build(){
            return new Routine(id, name, sortOrder, goalTime);
        }
    }

    public static RoutineBuilder builder(){
        return new RoutineBuilder();
    }
}
