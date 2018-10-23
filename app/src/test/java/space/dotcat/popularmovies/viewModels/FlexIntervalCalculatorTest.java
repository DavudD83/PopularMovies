package space.dotcat.popularmovies.viewModels;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.TimeUnit;

import space.dotcat.popularmovies.model.FlexInterval;
import space.dotcat.popularmovies.utils.updatePeriodCalculator.FlexIntervalCalculator;
import space.dotcat.popularmovies.utils.updatePeriodCalculator.FlexIntervalCalculcatorImpl;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FlexIntervalCalculatorTest {

    private static final long ONE_DAY_PERIOD = 1;
    private static final long THREE_DAY_PERIOD = 3;
    private static final long SEVEN_DAY_PERIOD = 7;
    private static final long ZERO_DAY_PERIOD = 0;

    private FlexIntervalCalculator mFlexIntervalCalculator;

    @Before
    public void init() {
        mFlexIntervalCalculator = new FlexIntervalCalculcatorImpl();
    }

    @Test
    public void testCalculateIntervalForOneDay() {
        FlexInterval flexInterval = mFlexIntervalCalculator.calculateFlexInterval(ONE_DAY_PERIOD);

        assertEquals(20, flexInterval.getFlexInterval());
        assertEquals(TimeUnit.HOURS, flexInterval.getTimeUnit());
    }

    @Test
    public void testCalculateIntervalForThreeDays() {
        FlexInterval flexInterval = mFlexIntervalCalculator.calculateFlexInterval(THREE_DAY_PERIOD);

        assertEquals(THREE_DAY_PERIOD - 1, flexInterval.getFlexInterval());
        assertEquals(TimeUnit.DAYS, flexInterval.getTimeUnit());
    }

    @Test
    public void testCalculateIntervalForSevenDays() {
        FlexInterval flexInterval = mFlexIntervalCalculator.calculateFlexInterval(SEVEN_DAY_PERIOD);

        assertEquals(SEVEN_DAY_PERIOD - 1, flexInterval.getFlexInterval());
        assertEquals(TimeUnit.DAYS, flexInterval.getTimeUnit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateIntervalForLessOrEqualsToZeroDays() {
        mFlexIntervalCalculator.calculateFlexInterval(ZERO_DAY_PERIOD);
    }
}
