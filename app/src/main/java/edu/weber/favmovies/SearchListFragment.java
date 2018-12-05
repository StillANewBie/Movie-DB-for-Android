package edu.weber.favmovies;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import edu.weber.favmovies.api.Authorization;
import edu.weber.favmovies.db.AppDatabase;
import edu.weber.favmovies.db.Movie;

public class SearchListFragment extends DialogFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter adapter;
    private View root;
    private MovieRecyclerViewAdapter.OnRecyclerViewAdapterListener mCallback;
    String movieName, movieYear;
    private MaterialSearchView searchView;
    List<Movie> movies;

    public SearchListFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search_list, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.rvSearchList);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.search_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.search_results);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

        searchView = (MaterialSearchView) root.findViewById(R.id.search_search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getArguments();
        movieName = bundle.getString("name");
        movieYear = bundle.getString("year");

        Context context = getContext();
        adapter = new MovieRecyclerViewAdapter(new ArrayList<Movie>(), mCallback);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        URL url = null;
        try {
            url = new URL("https://www.omdbapi.com/?s=" + movieName + "&y=" + movieYear
                    + "&apikey=" + Authorization.AUTH_TOKEN);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setRequestProperty("Authorization", "" + Authorization.AUTH_TOKEN);
        String rawJSON = "";

        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int status = 0;
        try {
            status = connection.getResponseCode();


            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection
                            .getInputStream()));
                    rawJSON = br.readLine();

                    System.out.println(rawJSON);

                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        JsonObject body = gson.fromJson(rawJSON, JsonObject.class);
        movies = new ArrayList<>();

        if (body.get("Search") != null) {
            for (JsonElement jo : body.get("Search").getAsJsonArray()) {
                movies.add(gson.fromJson(jo, Movie.class));
            }
        }
        adapter.addItem(movies);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ((AppCompatActivity) getActivity()).getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                List<Movie> newMovies = new ArrayList<>();
                System.out.println(userInput);
                for (Movie el: movies) {
                    if (el.getTitle().toLowerCase().contains(userInput)) {
                        newMovies.add(el);
                    }
                }

                adapter.addItem(newMovies);

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Movie movie = mCallback.swipeToDelete().get(0);

                    if (AppDatabase.getInstance(getContext()).movieDAO().loadByImdbID(movie
                            .getImdbID()) != null) {
                        AppDatabase.getInstance(getContext())
                                .movieDAO()
                                .delete(movie);
                    }
                    AppDatabase.getInstance(getContext())
                            .movieDAO()
                            .insert(movie);
                    AppDatabase.getInstance(getContext())
                            .movieDAO()
                            .delete(movie);
                }
            }).start();
//            FragmentManager manager = getFragmentManager();
//            manager.popBackStack();
//            manager.executePendingTransactions();
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (MovieRecyclerViewAdapter.OnRecyclerViewAdapterListener) activity;
        } catch (Exception e){
            throw new RuntimeException(activity.toString()
                    + " must implement OnRecyclerViewAdapterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
