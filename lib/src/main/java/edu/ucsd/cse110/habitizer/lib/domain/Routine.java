package edu.ucsd.cse110.habitizer.lib.domain;

public class Routine {
    private boolean isStarted;
    private boolean completed;
    private boolean isMorning;
    public Routine(){
        this.isStarted = false;
        this.completed = false;
    }
    public boolean isStarted(){
        return isStarted;
    }
    public boolean isCompleted(){
        return completed;
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
}
