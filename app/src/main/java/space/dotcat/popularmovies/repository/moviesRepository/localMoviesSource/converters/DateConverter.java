package space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource.converters;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static final SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @TypeConverter
    public static long frommStringToLong(String date) throws ParseException {
        Date d = sSimpleDateFormat.parse(date);

        return d.getTime();
    }

    @TypeConverter
    public static String fromLongToString(long timestamp) {
        Date date = new Date(timestamp);

        return sSimpleDateFormat.format(date);
    }
}
