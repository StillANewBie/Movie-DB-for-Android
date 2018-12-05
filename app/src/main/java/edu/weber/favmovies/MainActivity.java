package edu.weber.favmovies;

import android.content.ClipData;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import edu.weber.favmovies.db.Movie;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter
        .OnRecyclerViewAdapterListener, SearchDialogFragment.OnSearchDialogFragmentComplete{

    private MaterialSearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isSortedByTitle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();

                SearchDialogFragment sdf = new SearchDialogFragment();

                fm.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(android.R.id.content, sdf)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public void showMovieDialog(Movie movie) {
        FragmentManager fm = getSupportFragmentManager();
        MovieViewFragment movieViewFragment = new MovieViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.imdbid), movie.getImdbID());
        movieViewFragment.setArguments(bundle);

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(android.R.id.content, movieViewFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openImdbPage(String imdbID) {
        FragmentManager fm = getSupportFragmentManager();
        WebPageFragment webPageFragment = new WebPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.imdbid), imdbID);
        webPageFragment.setArguments(bundle);

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(android.R.id.content, webPageFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public List<Movie> swipeToDelete() {
        return MovieRecyclerViewAdapter.getMovies();
    }

    @Override
    public void doSearch(String name, String year) {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("year", year);

        FragmentManager fm = getSupportFragmentManager();
        SearchListFragment searchListFragment = new SearchListFragment();

        searchListFragment.setArguments(bundle);

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(android.R.id.content, searchListFragment)
                .addToBackStack(null)
                .commit();
    }
}
