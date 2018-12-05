package edu.weber.favmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import edu.weber.favmovies.db.AppDatabase;
import edu.weber.favmovies.db.Movie;

import java.util.List;

public class AllFavMovieViewModel extends ViewModel {

    private LiveData<List<Movie>> movieList;

    public LiveData<List<Movie>> getMovieList(Context c) {
        if (movieList != null) return movieList;

        return movieList = AppDatabase.getInstance(c).movieDAO().getAll();
    }

    public LiveData<List<Movie>> getMovieListByTitle(Context c) {
        if (movieList != null) return movieList;

        return movieList = AppDatabase.getInstance(c).movieDAO().sortByTytle();
    }
}
