package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.util.Subject;

public interface TaskRepository {
    Subject<Task> find(int id);

    Subject<List<Task>> findAll();

    void save(Task task);

    void save(List<Task> tasks);

    void remove(int id);

    void append(Task task);

    void prepend(Task task);
}
