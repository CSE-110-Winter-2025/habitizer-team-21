package edu.ucsd.cse110.habitizer.app;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import edu.ucsd.cse110.habitizer.app.ui.tasklist.CardListFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

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

}
