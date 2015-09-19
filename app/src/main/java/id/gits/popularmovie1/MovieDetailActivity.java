package id.gits.popularmovie1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.gits.popularmovie1.apis.daos.MovieDao;
import id.gits.popularmovie1.utils.Constant;

public class MovieDetailActivity extends BaseActivity {
    private static final String EXTRA_ITEM = "item";

    private TextView mTvTitle, mTvReleaseDate, mTvSynopsis, mTvVoteAvg;
    private ImageView mIvPoster, mIvBackdrop;

    private MovieDao mMovieDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvReleaseDate = (TextView) findViewById(R.id.tv_releasedate);
        mTvSynopsis = (TextView) findViewById(R.id.tv_synopsis);
        mTvVoteAvg = (TextView) findViewById(R.id.tv_voteavg);
        mIvPoster = (ImageView) findViewById(R.id.iv_poster);
        mIvBackdrop = (ImageView) findViewById(R.id.iv_backdrop);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovieDao = getIntent().getParcelableExtra(EXTRA_ITEM);

        setTitle(mMovieDao.getTitle());

        initLayout();
    }

    private void initLayout() {
        mTvTitle.setText(mMovieDao.getTitle());

        //set release date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate;
        try {
            Date dateRelease = sdf.parse(mMovieDao.getRelease_date());
            formattedDate = DateFormat.format("dd MMM yyyy", dateRelease).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            formattedDate = mMovieDao.getRelease_date();
        }
        mTvReleaseDate.setText(formattedDate);

        mTvVoteAvg.setText(mMovieDao.getVote_average() + "/" + "10");
        mTvSynopsis.setText(mMovieDao.getOverview());

        //set all images
        Picasso.with(this).load(Constant.ROOT_POSTER_IMAGE_URL + mMovieDao.getPoster_path())
                .error(R.color.grey).placeholder(R.color.grey).into(mIvPoster);
        Picasso.with(this).load(Constant.ROOT_BACKDROP_IMAGE_URL + mMovieDao.getBackdrop_path())
                .error(R.color.colorPrimary).placeholder(R.color.grey).into(mIvBackdrop);
    }

    public static void startThisActivity(Context context, MovieDao movieDao) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_ITEM, movieDao);
        context.startActivity(intent);
    }

}
