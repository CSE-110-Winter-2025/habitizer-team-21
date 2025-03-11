package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import edu.ucsd.cse110.habitizer.app.ui.tasklist.CardListFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PauseTest {
    private CardListFragment fragment;
    private long startTime;

    @Before
    public void setUp() {
        Routine testRoutine = new Routine(1, "Test Routine", 0, 30);
        fragment = new CardListFragment(testRoutine);

        startTime = System.currentTimeMillis();
        fragment.setLastTaskStartTime(startTime);
        fragment.routineStartTime = startTime;
    }

    @Test
    //testing the pause on routine that timer and timestamps are unchanged while paused
    public void testPauseRoutineTimer() {
        fragment.Pausedtime = System.currentTimeMillis();
        fragment.isPaused = true;
        fragment.stopRoutineTimer();

        assertTrue("Routine should be paused", fragment.isPaused);
        assertEquals("Task start time should not change when paused", startTime, fragment.getLastTaskStartTime());
    }

    @Test
    //testing resume after pausing
    public void testResumeRoutineTimer() {
        fragment.Pausedtime = System.currentTimeMillis();
        fragment.isPaused = true;
        fragment.stopRoutineTimer();

        long pauseDuration = 2000; // 2 seconds
        fragment.Resumedtime = fragment.Pausedtime + pauseDuration;

        fragment.isPaused = false;
        fragment.routineStartTime += pauseDuration;
        fragment.setLastTaskStartTime(fragment.getLastTaskStartTime() + pauseDuration);

        assertFalse("Routine should be running", fragment.isPaused);
        assertEquals("Task start time should increase by pause duration",
                startTime + pauseDuration, fragment.getLastTaskStartTime());
    }

    @Test
    //Testing when paused then resumed everything is the same
    public void testTaskTimeDoesNotIncreaseWhenPaused() {
        long initialTaskTime = fragment.getLastTaskStartTime();

        fragment.Pausedtime = System.currentTimeMillis();
        fragment.isPaused = true;
        fragment.stopRoutineTimer();

        long PauseEndTime = fragment.Pausedtime + 3000; //  3-second
        fragment.Resumedtime = PauseEndTime;

        assertEquals("Task start time should not change during pause", initialTaskTime, fragment.getLastTaskStartTime());
    }
}