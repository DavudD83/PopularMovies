package space.dotcat.popularmovies.utils.date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public class CalendarDateProvider implements DateProvider {

    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Inject
    public CalendarDateProvider() {
    }

    @Override
    public String getStartMovieDateForUpcoming() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        Date startDate = calendar.getTime();

        return mSimpleDateFormat.format(startDate);
    }

    @Override
    public String getEndMovieDateForUpcoming() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 14);

        Date futureDate = calendar.getTime();

        return mSimpleDateFormat.format(futureDate);
    }

    @Override
    public String getStartMovieDateForOngoing() {
        Calendar calendar = Calendar.getInstance();

        calendar.roll(Calendar.DATE, -14);

        Date startDate = calendar.getTime();

        return mSimpleDateFormat.format(startDate);
    }

    @Override
    public String getEndMovieDateForOngoing() {
        Calendar calendar = Calendar.getInstance();

        Date currentDate = calendar.getTime();

        return mSimpleDateFormat.format(currentDate);
    }
}
