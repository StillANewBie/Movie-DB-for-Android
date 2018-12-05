package edu.weber.favmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import edu.weber.favmovies.db.AppDatabase;
import edu.weber.favmovies.db.Movie;

public class AllSearchMovieViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> movieList = new MutableLiveData<>();

    public void update(List<Movie> movies) {
        movieList.setValue(movies);
    }

    public LiveData<List<Movie>> getMovieList(Context c) {
        if (movieList != null) return movieList;

        return movieList;
    }
}
