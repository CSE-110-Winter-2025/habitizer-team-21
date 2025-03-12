package edu.ucsd.cse110.habitizer.app.data.routinedb;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;


import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class RoomRoutineRepository implements RoutineRepository {
    private final RoutineDao routineDao;

    public RoomRoutineRepository(RoutineDao routineDao) {
        this.routineDao = routineDao;
    }

    @Override
    public Subject<Routine> find(int id) {
        var entityLiveData = routineDao.findAsLiveData(id);
        var routineLiveData = Transformations.map(entityLiveData, RoutineEntity::toRoutine);
        return new LiveDataSubjectAdapter<>(routineLiveData);
    }


    @Override
    public Subject<List<Routine>> findAll() {
        var entityLiveData = routineDao.findAllAsLiveData();
        var routineLiveData = Transformations.map(entityLiveData, entities -> {
            return entities.stream()
                    .map(RoutineEntity::toRoutine)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(routineLiveData);
    }

    @Override
    public void save(Routine routine) {
        routineDao.insert(RoutineEntity.fromRoutine(routine));
    }

    @Override
    public void save(List<Routine> routines) {
        var entities = routines.stream()
                .map(RoutineEntity::fromRoutine)
                .collect(Collectors.toList());
        routineDao.insert(entities);
    }

    @Override
    public void append(Routine routine) {
        routineDao.append(RoutineEntity.fromRoutine(routine));
    }

    @Override
    public void prepend(Routine routine) {
        routineDao.prepend(RoutineEntity.fromRoutine(routine));
    }

    @Override
    public void remove(int id) {
        routineDao.delete(id);
    }
}

