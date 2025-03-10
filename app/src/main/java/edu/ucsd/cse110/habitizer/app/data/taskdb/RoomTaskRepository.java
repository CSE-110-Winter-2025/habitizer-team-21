package edu.ucsd.cse110.habitizer.app.data.taskdb;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class RoomTaskRepository implements TaskRepository{
    private final TaskDao taskDao;

    public RoomTaskRepository(TaskDao taskDao){
        this.taskDao = taskDao;
    }

    @Override
    public Subject<Task> find(int id){
        var entityLiveData = taskDao.findAsLiveData(id);
        var taskLiveData = Transformations.map(entityLiveData, TaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public Subject<List<Task>> findAll(){
        var entityLiveData = taskDao.findAllAsLiveData();
        var taskLiveData = Transformations.map(entityLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public void save(Task task){
        taskDao.insert(TaskEntity.fromTask(task));
    }

    @Override
    public void save(List<Task> tasks){
        var entities = tasks.stream()
                .map(TaskEntity::fromTask)
                .collect(Collectors.toList());
        taskDao.insert(entities);
    }

    @Override
    public void append(Task task){
        taskDao.append(TaskEntity.fromTask(task));
    }

    @Override public void prepend(Task task){
        taskDao.prepend(TaskEntity.fromTask(task));
    }

    @Override
    public void remove(int id){
        taskDao.delete(id);
    }
}