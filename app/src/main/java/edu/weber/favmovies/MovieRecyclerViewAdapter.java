package edu.weber.favmovies;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.weber.favmovies.db.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private static List<Movie> movies = null;
    private final OnRecyclerViewAdapterListener mCallback;
    public interface OnRecyclerViewAdapterListener {
        void showMovieDialog(Movie movie);
        void openImdbPage(String imdbID);
        List<Movie> swipeToDelete();
    }

    public void addItem(List<Movie> localMovies) {
        movies.clear();
        Collections.reverse(localMovies);
        movies.addAll(localMovies);
        notifyDataSetChanged();
    }

    public Movie getMovieAt(int position) {
        return this.movies.get(position);
    }

    public MovieRecyclerViewAdapter(List<Movie> items, OnRecyclerViewAdapterListener listener) {
        movies = items;
        mCallback = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        if (movie != null) {
            holder.movie = movie;
            holder.movieTitle.setText("Title: " + movie.getTitle());
            holder.movieYear.setText(movie.getYear());
            new DownloadImageTask(holder.poster).execute(movie.getPoster());

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.showMovieDialog(movie);
                }
            });

            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    mCallback.openImdbPage(movie.getImdbID());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Movie movie;
        public TextView movieTitle, movieYear;
        public ImageView poster;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            movieTitle = (TextView) itemView.findViewById(R.id.movie_title_card);
            movieYear = (TextView) itemView.findViewById(R.id.movie_year_card);
            poster = (ImageView) itemView.findViewById(R.id.thumb_pic);
        }

    }

    public static List<Movie> getMovies() {
        return movies;
    }
}
