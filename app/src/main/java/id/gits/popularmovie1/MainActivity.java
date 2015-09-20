package id.gits.popularmovie1;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import id.gits.popularmovie1.adapters.MovieListAdapter;
import id.gits.popularmovie1.apis.RetrofitHelper;
import id.gits.popularmovie1.apis.daos.DiscoverMovieApiDao;
import id.gits.popularmovie1.apis.daos.ErrorApiDao;
import id.gits.popularmovie1.apis.daos.MovieDao;
import id.gits.popularmovie1.utils.Constant;
import id.gits.popularmovie1.utils.MyProgressView;
import retrofit.Callback;
import retrofit.Response;

public class MainActivity extends BaseActivity {
    private final static int MAX_WIDTH_COL_DP = 185;
    private final static String STATE_SORT = "stateSort";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.progress_view)
    MyProgressView mProgressView;

    private StaggeredGridLayoutManager mLayoutManager;
    private MovieListAdapter mAdapter;

    private List<MovieDao> mDataset = new ArrayList<>();

    private String mSort = Constant.SORT_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            mSort = savedInstanceState.getString(STATE_SORT);
        }

        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MovieListAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        //change span dynamically based on screen width
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        int viewWidth = mRecyclerView.getMeasuredWidth();
                        float cardViewWidth = MAX_WIDTH_COL_DP * getResources().getDisplayMetrics().density;
                        int newSpanCount = Math.max(2, (int) Math.floor(viewWidth / cardViewWidth));
                        mLayoutManager.setSpanCount(newSpanCount);
                        mLayoutManager.requestLayout();
                    }
                });

        mProgressView.setRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();
            }
        });
        callApi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            showSortDialog();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_SORT, mSort);
    }

    /**
     * Show dialog to choose sort mode
     */
    private void showSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.main_sort_by)
                .setSingleChoiceItems(
                        new String[]{getString(R.string.main_sort_most_popular),
                                getString(R.string.main_sort_highest_rated)},
                        (mSort.equals(Constant.SORT_POPULAR)) ? 0 : 1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mSort = (which == 0) ? Constant.SORT_POPULAR : Constant.SORT_HIGHEST_RATED;
                                callApi();
                                dialog.dismiss();
                            }
                        });
        builder.create().show();
    }

    /**
     * Call api to get list of movies
     */
    private void callApi() {
        RetrofitHelper.getInstance().getService()
                .discoverMovie(Constant.MOVIEDB_APIKEY, mSort)
                .enqueue(new Callback<DiscoverMovieApiDao>() {
                    @Override
                    public void onResponse(Response<DiscoverMovieApiDao> response) {
                        if (response.body() != null) {
                            mDataset.clear();
                            mDataset.addAll(response.body().getResults());
                            mAdapter.notifyDataSetChanged();

                            mRecyclerView.setVisibility(View.VISIBLE);
                            mProgressView.stopAndGone();
                        } else {
                            Gson gson = new Gson();
                            try {
                                ErrorApiDao errorApiDao = gson.fromJson(response.errorBody().string().toString(), ErrorApiDao.class);
                                mProgressView.stopAndError(errorApiDao.getStatus_message(), true);
                            } catch (IOException e) {
                                e.printStackTrace();
                                mProgressView.stopAndError(e.getMessage(), true);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        mProgressView.stopAndError(t.getMessage(), true);
                    }
                });
    }

}
