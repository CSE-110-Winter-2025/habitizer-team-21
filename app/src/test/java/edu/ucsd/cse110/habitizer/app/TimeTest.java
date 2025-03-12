package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.cse110.habitizer.app.ui.tasklist.CardListFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;


@RunWith(RobolectricTestRunner.class)
public class TimeTest {
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
    public void testTimeIncrease() {
        long pauseDuration = 10000; // 10 seconds in milliseconds
        fragment.routineStartTime += pauseDuration;
        assertEquals("Routine start time should increase by 10 seconds",
                startTime + pauseDuration, fragment.routineStartTime);
    }

    @Test
    public void testStartTimeChange() {
        long fakeTaskStartTime = System.currentTimeMillis() - 45000;
        fragment.setLastTaskStartTime(fakeTaskStartTime);

        long elapsed = System.currentTimeMillis() - fragment.getLastTaskStartTime();

        assertTrue("Elapsed time should be at least 45 seconds", elapsed >= 45000);
    }



}