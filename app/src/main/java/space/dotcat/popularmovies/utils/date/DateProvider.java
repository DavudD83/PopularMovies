package space.dotcat.popularmovies.utils.date;

public interface DateProvider {

    String getStartMovieDateForUpcoming();

    String getEndMovieDateForUpcoming();

    String getStartMovieDateForOngoing();

    String getEndMovieDateForOngoing();
}
