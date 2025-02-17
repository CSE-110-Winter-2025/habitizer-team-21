package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import org.junit.Test;
public class TaskTest {
    @Test
    public void testGetters(){
        var task = new Task(1,"task",0);
        assertEquals(Integer.valueOf(1), task.id());
        assertEquals("task", task.task());
        assertEquals(0, task.sortOrder());
    }

    @Test
    public void testWithId(){
        var task = new Task(1,"task",0);
        var expected = new Task(2,"task",0);
        var actual = task.withId(2);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithSortOrder(){
        var task = new Task(1,"task",0);
        var expected = new Task(1,"task",2);
        var actual = task.withSortOrder(2);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals(){
        var task1 = new Task(1,"task",0);
        var task2 = new Task(1,"task",0);
        var task3 = new Task(2,"task", 0);

        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }

    @Test
    public void testCompletion(){
        var task1 = new Task(1,"task",0);
        var task2 = new Task(1,"task",1);
        var task3 = new Task(2,"task", 2);

        task1.complete();

        assertTrue(task1.completed());
        assertFalse(task2.completed());
    }
}
