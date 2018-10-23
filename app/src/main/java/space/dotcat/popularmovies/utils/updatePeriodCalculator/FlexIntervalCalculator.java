package space.dotcat.popularmovies.utils.updatePeriodCalculator;

import space.dotcat.popularmovies.model.FlexInterval;

public interface FlexIntervalCalculator {

    FlexInterval calculateFlexInterval(long period_of_updating);
}
