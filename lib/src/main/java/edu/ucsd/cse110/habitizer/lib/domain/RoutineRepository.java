package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.util.Subject;

public interface RoutineRepository {
    Subject<Routine> find(int id);

    Subject<List<Routine>> findAll();

    void save(Routine routine);

    void save(List<Routine> routines);

    void remove(int id);

    void append(Routine routine);

    void prepend(Routine routine);
    int maxSort();
}
