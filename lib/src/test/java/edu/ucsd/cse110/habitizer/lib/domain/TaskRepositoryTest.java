package edu.ucsd.cse110.habitizer.lib.domain;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TaskRepositoryTest {
    private TaskRepository taskRepo;
    private InMemoryDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = InMemoryDataSource.fromDefault();
        taskRepo = new TaskRepository(dataSource);
    }

    @Test
    public void testSaveAndFindTask() {
        Task task = new Task(100, "Test Task", 10, 1);
        taskRepo.save(task);

        Subject<Task> retrievedSubject = taskRepo.find(100);
        Task retrievedTask = retrievedSubject.getValue();

        assertNotNull(retrievedTask);
        assertEquals("Test Task", retrievedTask.task());
        assertEquals(10, retrievedTask.sortOrder());
    }
    @Test
    public void testFindAllTasks() {
        Subject<List<Task>> retrievedSubject = taskRepo.findAll();
        List<Task> retrievedTasks = retrievedSubject.getValue();
        assertEquals(12, retrievedTasks.size());

        Task task1 = new Task(200, "Task One", 1, 1);
        Task task2 = new Task(201, "Task Two", 2, 1);
        Task task3 = new Task(202, "Task Three", 3, 1);

        taskRepo.save(List.of(task1, task2, task3));
        retrievedSubject = taskRepo.findAll();
        retrievedTasks = retrievedSubject.getValue();
        assertNotNull(retrievedTasks);
        assertEquals(15, retrievedTasks.size());
        assertTrue(retrievedTasks.contains(task1));
        assertTrue(retrievedTasks.contains(task2));
        assertTrue(retrievedTasks.contains(task3));
    }


    @Test
    public void testRemoveTask() {
        Task task = new Task(101, "To be deleted", 5, 1);
        taskRepo.save(task);

        taskRepo.remove(101);
        Subject<Task> retrievedSubject = taskRepo.find(101);

        assertNull(retrievedSubject.getValue());
    }

    @Test
    public void testAppendTask() {
        Task task = new Task(102, "Appended Task", 0, 1);
        taskRepo.append(task);

        Task appendedTask = dataSource.getTask(102);
        assertNotNull(appendedTask);
        assertEquals(dataSource.getMaxSortOrder(), appendedTask.sortOrder());
    }

    @Test
    public void testPrepend() {
        Task task = new Task(102, "Prepended Task", 0, 1);
        taskRepo.prepend(task);

        Task prependedTask = dataSource.getTask(102);
        assertNotNull(prependedTask);
        assertEquals(dataSource.getMinSortOrder(), prependedTask.sortOrder());
    }

    @Test
    public void testGetSortedRoutineTasks() {
        // Given: Tasks with different routineIds and unordered sortOrders
        Task task1 = new Task(1, "Task 1", 2, 101);
        Task task2 = new Task(2, "Task 2", 0, 101);
        Task task3 = new Task(3, "Task 3", 1, 101);
        Task task4 = new Task(4, "Task 4", 5, 102); // Different routineId

        dataSource.putTask(task1);
        dataSource.putTask(task2);
        dataSource.putTask(task3);
        dataSource.putTask(task4);

        // When: Fetching sorted tasks for routineId 101
        List<Task> sortedTasks = taskRepo.getSortedRoutineTasks(101);

        // Then: Tasks are sorted by sortOrder
        assertEquals(3, sortedTasks.size());
        assertEquals(0, sortedTasks.get(0).sortOrder());
        assertEquals(1, sortedTasks.get(1).sortOrder());
        assertEquals(2, sortedTasks.get(2).sortOrder());

        // Ensure unique sortOrder values
        for (int i = 0; i < sortedTasks.size(); i++) {
            assertEquals(i, sortedTasks.get(i).sortOrder());
        }

        // Ensure tasks from other routines are not in this routine
        assertFalse(sortedTasks.stream().anyMatch(task -> task.getRoutineId() == 102));
    }
    @Test
    public void testMoveTaskUp() {
        Task task1 = new Task(103, "Task 1", 1, 49);
        Task task2 = new Task(104, "Task 2", 2, 49);
        taskRepo.save(List.of(task1, task2));

        System.out.println("Before move up:");
        System.out.println("Task 1 sortOrder: " + dataSource.getTask(103).sortOrder());
        System.out.println("Task 2 sortOrder: " + dataSource.getTask(104).sortOrder());

        taskRepo.moveTaskUp(task2);

        System.out.println("After move up:");
        System.out.println("Task 1 sortOrder: " + dataSource.getTask(103).sortOrder());
        System.out.println("Task 2 sortOrder: " + dataSource.getTask(104).sortOrder());

        assertEquals(1, dataSource.getTask(104).sortOrder());
        assertEquals(2, dataSource.getTask(103).sortOrder());
    }

    @Test
    public void testMoveTaskDown() {
        Task task1 = new Task(105, "Task 1", 1, 50);
        Task task2 = new Task(106, "Task 2", 2, 50);
        taskRepo.save(List.of(task1, task2));

        System.out.println("Before move down:");
        System.out.println("Task 1 sortOrder: " + dataSource.getTask(105).sortOrder());
        System.out.println("Task 2 sortOrder: " + dataSource.getTask(106).sortOrder());

        taskRepo.moveTaskDown(task1);

        System.out.println("After move down:");
        System.out.println("Task 1 sortOrder: " + dataSource.getTask(105).sortOrder());
        System.out.println("Task 2 sortOrder: " + dataSource.getTask(106).sortOrder());

        assertEquals(2, dataSource.getTask(105).sortOrder());
        assertEquals(1, dataSource.getTask(106).sortOrder());
    }

}
