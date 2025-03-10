package edu.ucsd.cse110.habitizer.app.data.routinedb;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {RoutineEntity.class}, version = 1)
public abstract class RoutiineDatabase extends RoomDatabase{
    public abstract RoutineDao routineDao();
}
