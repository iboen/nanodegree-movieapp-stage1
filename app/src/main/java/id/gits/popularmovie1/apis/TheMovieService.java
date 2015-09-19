package id.gits.popularmovie1.apis;

import id.gits.popularmovie1.apis.daos.DiscoverMovieApiDao;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TheMovieService {
    @GET("discover/movie")
    Call<DiscoverMovieApiDao> discoverMovie(@Query("api_key") String apiKey, @Query("sort_by") String sortBy);
}