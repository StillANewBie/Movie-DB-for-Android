package edu.weber.favmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.weber.favmovies.db.*;

import edu.weber.favmovies.MovieListFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private final List<Movie> movies;
    private final OnRecyclerViewAdapterListener mCallback;

    public void addItem(List<Movie> localMovies) {
        movies.clear();
        movies.addAll(localMovies);
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewAdapterListener {
        void showMovieDialog(Movie movie);
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
        Movie movie = movies.get(position);
        if (movie != null) {
            holder.movie = movie;
            holder.movieTitle.setText("Title: " + movie.getTitle());
            holder.movieYear.setText(movie.getYear());
            new DownloadImageTask(holder.poster).execute(movie.getPoster());

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ToDo: attach to a movie view
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
}
