package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class SimpleRoutineRepository implements RoutineRepository {
    private final InMemoryDataSource dataSource;

    public SimpleRoutineRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Subject<Routine> find(int id) {
        return dataSource.getRoutineSubject(id);
    }

    @Override
    public Subject<List<Routine>> findAll() {
        return dataSource.getAllRoutinesSubject();
    }

    @Override
    public void save(Routine routine) {
        dataSource.putRoutine(routine);
    }

    @Override
    public void save(List<Routine> routines) {
        dataSource.putRoutines(routines);
    }

    @Override
    public void remove(int id) {
        dataSource.removeRoutine(id);
    }

    @Override
    public void append(Routine routine){
        dataSource.putRoutine(
                routine.withSortOrder(dataSource.getMaxSortOrderRoutine() + 1)
        );
    }

    @Override
    public void prepend(Routine routine){
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrderRoutine(),1);
        dataSource.putRoutine(
                routine.withSortOrder(dataSource.getMinSortOrder()-1)
        );
    }

}
