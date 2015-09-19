package id.gits.popularmovie1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.gits.popularmovie1.MovieDetailActivity;
import id.gits.popularmovie1.R;
import id.gits.popularmovie1.apis.daos.MovieDao;
import id.gits.popularmovie1.utils.Constant;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private List<MovieDao> mDataset = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvPoster;
        public TextView mTvTitle;
        public View mRoot;

        public ViewHolder(View v) {
            super(v);
            mRoot = v;
            mIvPoster = (ImageView) v.findViewById(R.id.iv_poster);
            mTvTitle = (TextView) v.findViewById(R.id.tv_title);
        }
    }

    public MovieListAdapter(List<MovieDao> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MovieDao item = mDataset.get(position);

        final Context context = holder.mRoot.getContext();

        holder.mTvTitle.setText(item.getTitle());

        String imagePath = Constant.ROOT_POSTER_IMAGE_URL + item.getPoster_path();

        Picasso.with(context)
                .load(imagePath)
                .placeholder(R.color.grey)
                .error(android.R.color.transparent)
                .into(holder.mIvPoster);


        holder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to detail page
                MovieDetailActivity.startThisActivity(context, item);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}