package edu.ucsd.cse110.habitizer.app.data.taskdb;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TaskEntity.class}, version = 1)
public abstract class TasksDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
