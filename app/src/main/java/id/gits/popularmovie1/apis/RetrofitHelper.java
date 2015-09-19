package id.gits.popularmovie1.apis;

import id.gits.popularmovie1.utils.Constant;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by ibun on 19/09/15.
 */
public class RetrofitHelper {
    private static RetrofitHelper retrofitHelper;

    private Retrofit mRetrofit;
    private TheMovieService mService;

    private RetrofitHelper() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(TheMovieService.class);
    }

    public static RetrofitHelper getInstance() {
        if (retrofitHelper == null) {
            retrofitHelper = new RetrofitHelper();
        }

        return retrofitHelper;
    }

    public TheMovieService getService() {
        return mService;
    }
}
