package edu.ucsd.cse110.habitizer.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.MutableSubject;
import edu.ucsd.cse110.habitizer.lib.util.SimpleSubject;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

/**
 * Class used as a sort of "database" of decks and tasks that exist. This
 * will be replaced with a real database in the future, but can also be used
 * for testing.
 */
public class InMemoryDataSource {
    private int nextId = 0;
    private int nextIdR = 3;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;
    private int minSortOrderRoutine = Integer.MAX_VALUE;
    private int maxSortOrderRoutine = Integer.MIN_VALUE;

    private final Map<Integer, Task> tasks
            = new HashMap<>();
    private final Map<Integer, MutableSubject<Task>> taskSubjects
            = new HashMap<>();
    private final MutableSubject<List<Task>> allTasksSubject
            = new SimpleSubject<>();
    private final Map<Integer, Routine> routines
            = new HashMap<>();
    private final Map<Integer, MutableSubject<Routine>> routineSubjects
            = new HashMap<>();
    private final MutableSubject<List<Routine>> allRoutinesSubject
            = new SimpleSubject<>();

    public InMemoryDataSource() {
    }

    public final static List<Task> TASKS_LIST = List.of(
            new Task(0, "Brush Teeth",  0,1),
            new Task(1, "Shower",  1,1),
            new Task(2, "Make Breakfast",  2,1),
            new Task(3, "Pack Bag",  3,1),
            new Task(4, "Feed Dog",  4,1),
            new Task(5, "Lock Doors", 5,1),
            new Task(6, "Put away outerwear",  0,2),
            new Task(7, "Read",  1,2),
            new Task(8, "Make Dinner",  2,2),
            new Task(9, "Plan Week",  3,2),
            new Task(10, "Feed Dog",  4,2),
            new Task(11, "Brush Teeth", 5,2)
    );

    public final static List<Routine> ROUTINES_LIST = List.of(
            Routine.builder().setName("Morning Routine")
                    .setGoalTime(45)
                    .setId(1)
                    .setSortOrder(1)
                    .build(),
            Routine.builder()
                    .setName("Evening Routine")
                    .setGoalTime(30)
                    .setId(2)
                    .setSortOrder(2)
                    .build()
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        data.putTasks(TASKS_LIST);
        data.putRoutines(ROUTINES_LIST);
        return data;
    }


    public List<Task> getTasks() {
        return List.copyOf(tasks.values());
    }
    public List<Routine> getRoutines() {
        return List.copyOf(routines.values());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }
    public Routine getRoutine(int id) {
        return routines.get(id);
    }

    public Subject<Task> getTaskSubject(int id) {
        if (!taskSubjects.containsKey(id)) {
            var subject = new SimpleSubject<Task>();
            subject.setValue(getTask(id));
            taskSubjects.put(id, subject);
        }
        return taskSubjects.get(id);
    }
    public Subject<Routine> getRoutineSubject(int id) {
        if (!routineSubjects.containsKey(id)) {
            var subject = new SimpleSubject<Routine>();
            subject.setValue(getRoutine(id));
            routineSubjects.put(id, subject);
        }
        return routineSubjects.get(id);
    }

    public Subject<List<Task>> getAllTasksSubject() {
        return allTasksSubject;
    }
    public Subject<List<Routine>> getAllRoutinesSubject() {
        return allRoutinesSubject;
    }

    public int getMinSortOrder() {
        return minSortOrder;
    }

    public int getMaxSortOrder() {
        return maxSortOrder;
    }
    public int getMinSortOrderRoutine(){ return minSortOrderRoutine;}
    public int getMaxSortOrderRoutine(){ return maxSortOrderRoutine;}

    public void putTask(Task task) {
        var fixedTask = preInsert(task);

        tasks.put(fixedTask.id(), fixedTask);
        postInsert();
        assertSortOrderConstraints();

        if (taskSubjects.containsKey(fixedTask.id())) {
            taskSubjects.get(fixedTask.id()).setValue(fixedTask);
        }
        allTasksSubject.setValue(getTasks());
    }
    public void putRoutine(Routine routine) {
        var fixedRoutine = preInsertRoutine(routine);

        routines.put(fixedRoutine.id(), fixedRoutine);
        postInsertRoutine();

        if (routineSubjects.containsKey(fixedRoutine.id())) {
            routineSubjects.get(fixedRoutine.id()).setValue(fixedRoutine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void putTasks(List<Task> tasks_l) {
        var fixedTasks = tasks_l.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedTasks.forEach(task -> tasks.put(task.id(), task));
        postInsert();
        assertSortOrderConstraints();

        fixedTasks.forEach(task -> {
            if (taskSubjects.containsKey(task.id())) {
                taskSubjects.get(task.id()).setValue(task);
            }
        });
        allTasksSubject.setValue(getTasks());
    }
    public void putRoutines(List<Routine> rList) {
        var fixedRoutines = rList.stream()
                .map(this::preInsertRoutine)
                .collect(Collectors.toList());

        fixedRoutines.forEach(routine -> routines.put(routine.id(), routine));
        postInsertRoutine();

        fixedRoutines.forEach(routine -> {
            if (routineSubjects.containsKey(routine.id())) {
                routineSubjects.get(routine.id()).setValue(routine);
            }
        });
        allRoutinesSubject.setValue(getRoutines());
    }

    public void removeTask(int id) {
        var task = tasks.get(id);
        var sortOrder = task.sortOrder();

        tasks.remove(id);
                shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (taskSubjects.containsKey(id)) {
            taskSubjects.get(id).setValue(null);
        }
        allTasksSubject.setValue(getTasks());
    }
    public void removeRoutine(int id) {
        var routine = routines.get(id);
        var sortOrder = routine.sortOrder();

        routines.remove(id);
        shiftSortOrdersRoutine(sortOrder, maxSortOrder, -1);

        if (routineSubjects.containsKey(id)) {
            routineSubjects.get(id).setValue(null);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void shiftSortOrders(int from, int to, int by) {
        var tasks_l = tasks.values().stream()
                .filter(task -> task.sortOrder() >= from && task.sortOrder() <= to)
                .map(task -> task.withSortOrder(task.sortOrder() + by))
                .collect(Collectors.toList());

        putTasks(tasks_l);
    }
    public void shiftSortOrdersRoutine(int from, int to, int by) {
        var rList = routines.values().stream()
                .filter(routine -> routine.sortOrder() >= from && routine.sortOrder() <= to)
                .map(routine -> routine.withSortOrder(routine.sortOrder() + by))
                .collect(Collectors.toList());

        putRoutines(rList);
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that new
     * tasks inserted have an id, and updates the nextId if necessary.
     */
    private Task preInsert(Task task) {
        var id = task.id();
        if (id == null) {
            // If the task has no id, give it one.
            task = task.withId(nextId++);
        }
        else if (id > nextId) {
            // If the task has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load tasks like in fromDefault().
            nextId = id + 1;
        }

        return task;
    }
    private Routine preInsertRoutine(Routine routine) {
        var id = routine.id();
        if (id == null || id < 0) {
            // If the task has no id, give it one.
            routine = routine.withId(nextIdR++);
        }
        else if (id > nextIdR) {
            // If the task has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load tasks like in fromDefault().
            nextIdR = id + 1;
        }

        return routine;
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that the
     * min and max sort orders are up to date after an insert.
     */
    private void postInsert() {
        // Keep the min and max sort orders up to date.
        minSortOrder = tasks.values().stream()
                .map(Task::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = tasks.values().stream()
                .map(Task::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }
    private void postInsertRoutine() {
        // Keep the min and max sort orders up to date.
        minSortOrderRoutine = routines.values().stream()
                .map(Routine::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrderRoutine = routines.values().stream()
                .map(Routine::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }


    /**
     * Safety checks to ensure the sort order constraints are maintained.
     * <p></p>
     * Will throw an AssertionError if any of the constraints are violated,
     * which should never happen. Mostly here to make sure I (Dylan) don't
     * write incorrect code by accident!
     */
    private void assertSortOrderConstraints() {
        // Get all the sort orders...
        var sortOrders = tasks.values().stream()
                .map(Task::sortOrder)
                .collect(Collectors.toList());

        // Non-negative...
        //assert sortOrders.stream().allMatch(i -> i >= 0);

        // Unique...
        // assert sortOrders.size() == sortOrders.stream().distinct().count();

        // Between min and max...
        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
    }
}
