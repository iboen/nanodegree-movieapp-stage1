package id.gits.popularmovie1.apis.daos;

import java.util.List;

/**
 * Created by ibun on 19/09/15.
 */
public class DiscoverMovieApiDao {
    private int page;
    private int total_pages;
    private long total_results;
    private List<MovieDao> results;
    private String status_code;
    private String status_message;

    public String getStatus_code() {
        return status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public List<MovieDao> getResults() {
        return results;
    }

    public long getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getPage() {
        return page;
    }


}
