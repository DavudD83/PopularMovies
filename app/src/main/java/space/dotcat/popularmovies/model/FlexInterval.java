package space.dotcat.popularmovies.model;

import java.util.concurrent.TimeUnit;

public class FlexInterval {

    private long mFlexInterval;

    private TimeUnit mTimeUnit;

    public FlexInterval(long flexInterval, TimeUnit timeUnit) {
        mFlexInterval = flexInterval;
        mTimeUnit = timeUnit;
    }

    public long getFlexInterval() {
        return mFlexInterval;
    }

    public void setFlexInterval(long flexInterval) {
        mFlexInterval = flexInterval;
    }

    public TimeUnit getTimeUnit() {
        return mTimeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        mTimeUnit = timeUnit;
    }
}
