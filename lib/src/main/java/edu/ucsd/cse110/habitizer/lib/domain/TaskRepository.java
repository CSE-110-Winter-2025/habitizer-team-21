package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
import java.util.stream.Collectors;


public class TaskRepository {
    private final InMemoryDataSource dataSource;

    public TaskRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Subject<Task> find(int id) {
        return dataSource.getTaskSubject(id);
    }

    public Subject<List<Task>> findAll() {
        return dataSource.getAllTasksSubject();
    }

    public void save(Task task) {
        dataSource.putTask(task);
    }

    public void save(List<Task> tasks) {
        dataSource.putTasks(tasks);
    }

    public void remove(int id) {
        dataSource.removeTask(id);
    }

    public void append(Task task){
        dataSource.putTask(
                task.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
    }

    public void prepend(Task task){
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(),1);
        dataSource.putTask(
                task.withSortOrder(dataSource.getMinSortOrder()-1)
        );
    }
    public void moveTaskUp(Task task) {
        List<Task> tasks = dataSource.getTasks().stream()
                .filter(t -> t.getRoutineId() == task.getRoutineId())
                .sorted((a, b) -> Integer.compare(a.sortOrder(), b.sortOrder()))
                .collect(Collectors.toList());

        int index = tasks.indexOf(task);
        if (index > 0) {
            Task prevTask = tasks.get(index - 1);
            swapTaskSortOrders(task, prevTask);
        }
    }

    public void moveTaskDown(Task task) {
        List<Task> tasks = dataSource.getTasks().stream()
                .filter(t -> t.getRoutineId() == task.getRoutineId())
                .sorted((a, b) -> Integer.compare(a.sortOrder(), b.sortOrder()))
                .collect(Collectors.toList());

        int index = tasks.indexOf(task);
        if (index != -1 && index < tasks.size() - 1) {
            Task nextTask = tasks.get(index + 1);
            swapTaskSortOrders(task, nextTask);
        }
    }

    private void swapTaskSortOrders(Task a, Task b) {
        int temp = a.sortOrder();
        Task updatedA = a.withSortOrder(b.sortOrder());
        Task updatedB = b.withSortOrder(temp);

        dataSource.putTask(updatedA);
        dataSource.putTask(updatedB);
    }



}