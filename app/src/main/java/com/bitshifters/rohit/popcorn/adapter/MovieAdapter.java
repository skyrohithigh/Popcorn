package com.bitshifters.rohit.popcorn.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bitshifters.rohit.popcorn.MainActivity;
import com.bitshifters.rohit.popcorn.MovieDetailActivity;
import com.bitshifters.rohit.popcorn.MovieDetailFragment;
import com.bitshifters.rohit.popcorn.R;
import com.bitshifters.rohit.popcorn.api.Movie;
import com.bitshifters.rohit.popcorn.api.MoviesService;
import com.bitshifters.rohit.popcorn.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rohit on 29/3/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

        private final List<Movie> mValues;
        private MainActivity mMainActivity;

        public MovieAdapter(final MainActivity activity, List<Movie> items) {
            mValues = items;
            mMainActivity = activity;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        //When Sorting criteria is changed
        public void changeDataSet(List<Movie> items){
            //deleting old movies
            mValues.clear();
            mValues.addAll(items);
            notifyDataSetChanged();

        }

        //When LoadMore is is called
        public void addDataSet(List<Movie> items){
            mValues.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);

            //Loading Image
            Picasso.with(mMainActivity)
                    .load(Utility.getPortraitPosterUrl(mMainActivity,holder.mItem.getPosterPath()))
                    .error(R.drawable.portrait_poster_not_found)
                    .into(holder.mPosterPortrait);

            //Setting OnClickListener
            holder.mView.setOnClickListener(holder);

            //For Two Pane View for first time setup
            if(mMainActivity.ismTwoPane() && position == 0){
                holder.onClick(holder.mView);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public final View mView;
            public Movie mItem;
            public ImageView mPosterPortrait;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mPosterPortrait = (ImageView) view.findViewById(R.id.ivPosterPortrait);
            }

            @Override
            public void onClick(View v) {
                //Loading fragment in the MainActivity in Two Pane Mode
                if (mMainActivity.ismTwoPane()) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable(MovieDetailFragment.ARG_MOVIE, mItem);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    mMainActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    //Starting Details Activity
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailFragment.ARG_MOVIE, mItem);
                    context.startActivity(intent);
                }
            }
        }
    }
