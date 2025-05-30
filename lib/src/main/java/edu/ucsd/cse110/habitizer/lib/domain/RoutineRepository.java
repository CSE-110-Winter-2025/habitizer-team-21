package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoutineRepository {
    private final InMemoryDataSource dataSource;

    public RoutineRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Subject<Routine> find(int id) {
        return dataSource.getRoutineSubject(id);
    }

    public Subject<List<Routine>> findAll() {
        return dataSource.getAllRoutinesSubject();
    }

    public void save(Routine routine) {
        dataSource.putRoutine(routine);
    }

    public void save(List<Routine> routines) {
        dataSource.putRoutines(routines);
    }

    public void remove(int id) {
        dataSource.removeRoutine(id);
    }

    public void append(Routine routine){
        dataSource.putRoutine(
                routine.withSortOrder(dataSource.getMaxSortOrderRoutine() + 1)
        );
    }

    public void prepend(Routine routine){
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrderRoutine(),1);
        dataSource.putRoutine(
                routine.withSortOrder(dataSource.getMinSortOrder()-1)
        );
    }

}
