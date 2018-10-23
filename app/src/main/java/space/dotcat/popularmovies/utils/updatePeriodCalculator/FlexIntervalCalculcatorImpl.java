package space.dotcat.popularmovies.utils.updatePeriodCalculator;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import space.dotcat.popularmovies.model.FlexInterval;

public class FlexIntervalCalculcatorImpl implements FlexIntervalCalculator {

    @Inject
    public FlexIntervalCalculcatorImpl() {
    }

    @Override
    public FlexInterval calculateFlexInterval(long period_of_updating) {
        if (period_of_updating <= 0) {
            throw new IllegalArgumentException("Period of updating popular movies can not be equal or less than zero." +
                    " Can not calculate flex interval.");
        }

        long flex_interval;

        TimeUnit timeUnit;

        if (period_of_updating == 1) {
            flex_interval = 20;

            timeUnit = TimeUnit.HOURS;
        } else {
            flex_interval = period_of_updating - 1;

            timeUnit = TimeUnit.DAYS;
        }

        return new FlexInterval(flex_interval, timeUnit);
    }
}
