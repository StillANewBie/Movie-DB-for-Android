package edu.weber.favmovies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDAO {

    @Query("select * from Movie")
    LiveData<List<Movie>> getAll();

    @Query("select * from Movie where imdbID = :imdbID")
    Movie loadByImdbID(String imdbID);

    @Query("select * from Movie where Title = :Title")
    LiveData<List<Movie>> loadByTitle(String Title);

    @Query("select * from Movie where Type = :Type")
    LiveData<List<Movie>> loadByType(String Type);

    @Query("select * from Movie where Year = :Year")
    LiveData<List<Movie>> loadByYear(String Year);

    @Query("select * from Movie order by Title")
    LiveData<List<Movie>> sortByTytle();

    @Query("select * from Movie order by Year")
    LiveData<List<Movie>> sortByYear();

    @Query("select * from Movie order by imdbRating")
    LiveData<List<Movie>> sortByRating();

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Insert
    void insert(Movie movie);
}
