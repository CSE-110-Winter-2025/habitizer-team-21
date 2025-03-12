package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;


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
    public List<Task> getSortedRoutineTasks(int routineId) {
        List<Task> routineTasks = new ArrayList<>();

        for (Task task : dataSource.getTasks()) {
            if (task.getRoutineId() == routineId) {
                routineTasks.add(task);
            }
        }

        routineTasks.sort(Comparator.comparingInt(Task::sortOrder));

        // Ensure unique sortOrder values
        int expectedSortOrder = 0;
        for (Task task : routineTasks) {
            if (task.sortOrder() != expectedSortOrder) {
                dataSource.putTask(task.withSortOrder(expectedSortOrder));
            }
            expectedSortOrder++;
        }

        return routineTasks;
    }



    public void moveTaskUp(Task task) { // tasks move up one position
        List<Task> routineTasks = getSortedRoutineTasks(task.getRoutineId());

        int index = routineTasks.indexOf(task);
        if (index > 0) {
            swapOrderAndSave(task, routineTasks.get(index - 1));
        }
    }


    public void moveTaskDown(Task task) { // tasks move down one position
        List<Task> routineTasks = getSortedRoutineTasks(task.getRoutineId());

        int index = routineTasks.indexOf(task);
        if (index != -1 && index < routineTasks.size() - 1) {
            swapOrderAndSave(task, routineTasks.get(index + 1));

        }
    }

    private void swapOrderAndSave(Task a, Task b) {
        int orderA = a.sortOrder();
        int orderB = b.sortOrder();

        // Create updated tasks with swapped sort orders
        Task updatedA = a.withSortOrder(orderB);
        Task updatedB = b.withSortOrder(orderA);

        // Save the tasks in the correct order
        dataSource.putTask(updatedB); // Save b first to avoid collision
        dataSource.putTask(updatedA); // Then save a
    }


}