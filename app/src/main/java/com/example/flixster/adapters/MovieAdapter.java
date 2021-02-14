package com.example.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.flixster.DetailActivity;
import com.example.flixster.MainActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;



public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    /*Context used to know where this adapter is being constructed from
    * and a list of data*/
    Context context;
    List<Movie> movies;



    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    /*Base RecyclerView.Adapter is an abstract class. There are methods you need to fill out when extending it.*/
    @NonNull
    @Override
    // Usually involves inflating a layout from XML and returning the holder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateVewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindVewHolder " + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the ViewHolder
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    /*Create inner ViewHolder class extends RecyclerView.ViewHolder
    * ViewHolder is a representation of our row in the recycler view*/
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Pulling out references to the RelativeLayout, we specified the id as 'container' to both
        // the portrait/landscape .xml files for the item_movie
        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container =itemView.findViewById(R.id.container);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            // logic to choose between landscape or portrait image
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            } else {
                imageUrl = movie.getPosterPath();
            }

            Glide.with(context)
                    .load(imageUrl)
                    .into(ivPoster);

            // 1. Register a click listener on entire container, we debugged with toast first
            // Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
            // 2. Navigate to a new activity on tap
            // after pulling reference to the item_movie.xml we changed the click listener for the
            // entire container
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 2. continued.. to navigate to new activity first create a new activity
                    // find where MainActivity resides, (in Main). right click > create new > Activity > Empty Activity
                    // create intent object, first param is context (member variable in the adaptor), second is class you want to navigate too
                    // then call startActivity method.
                    Intent i = new Intent(context, DetailActivity.class);
                    // Pass data to new activity
                    // you can pass data one at a time like bellow but it's tedious
                    // i.putExtra("title", movie.getTitle());
                    // instead use Parcel lib and wrap defined object
                    i.putExtra("movie", Parcels.wrap(movie));
                    // Using distinct transitions names which were placed in the activity_detail and item_movie .xml
                    // you can animate multiple elements. do NOT use default import of android.util.Pair.
                    // generically, use distinct transition names in source and target layout xml files.
                    Pair<View, String> p1 = Pair.create((TextView) tvTitle, "title");
                    Pair<View, String> p2 = Pair.create((TextView) tvOverview, "profile");
                    Pair<View, String> p3 = Pair.create((ImageView) ivPoster, "poster");
                 //   Pair<View, String> p4 = Pair.create((TextView) tvOverview, "profile");
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, p1, p2, p3);

                    context.startActivity(i, options.toBundle());
                }
            });
        }
    }
}
