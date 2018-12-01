package edu.weber.favmovies;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import edu.weber.favmovies.db.Movie;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter
        .OnRecyclerViewAdapterListener, SearchDialogFragment.OnSearchDialogFragmentComplete {

    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.fav_toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setTitle(R.string.saved_movies);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
//
//        searchView = (MaterialSearchView) findViewById(R.id.fav_filter);
//        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewShown() {
//
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//
//            }
//        });
//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });


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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_item, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);
//        return true;
//    }

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
