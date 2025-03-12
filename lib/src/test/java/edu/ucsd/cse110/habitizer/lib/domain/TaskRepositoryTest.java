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
    public void testMoveTaskUp() {
        Task task1 = new Task(103, "Task 1", 1, 1);
        Task task2 = new Task(104, "Task 2", 2, 1);
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
        Task task1 = new Task(105, "Task 1", 1, 1);
        Task task2 = new Task(106, "Task 2", 2, 1);
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
