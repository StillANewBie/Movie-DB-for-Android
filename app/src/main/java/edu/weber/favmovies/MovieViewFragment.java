package edu.weber.favmovies;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import edu.weber.favmovies.api.Authorization;
import edu.weber.favmovies.db.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;


public class MovieViewFragment extends DialogFragment {

    private View root;
    private String currentID;
    private Movie movie;
    private TextView txtTitle, txtYear, txtReleased, txtRuntime, txtImdbRating,
    txtVotes, txtDirector, txtWriter, txtActors, txtCountry, txtPlot, txtLanguage, txtType;
    private ImageView imageView;
    private ImageButton saveBtn, removeBtn;

    public MovieViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_movie_view, container, false);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.detail_toolbar);
        toolbar.setTitle(R.string.app_name);

        ((AppCompatActivity)Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        currentID = bundle.getString("imdbID");

        // initialize textview
        txtTitle = (TextView) root.findViewById(R.id.movie_title);
        txtYear = (TextView) root.findViewById(R.id.movie_year);
        txtReleased = (TextView) root.findViewById(R.id.movie_released);
        txtRuntime = (TextView) root.findViewById(R.id.movie_time);
        txtImdbRating = (TextView) root.findViewById(R.id.movie_rating);
        txtVotes = (TextView) root.findViewById(R.id.movie_votes);
        txtDirector = (TextView) root.findViewById(R.id.movie_director);
        txtWriter = (TextView) root.findViewById(R.id.movie_writer);
        txtActors = (TextView) root.findViewById(R.id.movie_actors);
        txtCountry = (TextView) root.findViewById(R.id.movie_country);
        txtPlot = (TextView) root.findViewById(R.id.movie_plot);
        txtLanguage = (TextView) root.findViewById(R.id.movie_language);
        txtType = (TextView) root.findViewById(R.id.movie_type);
        imageView = (ImageView) root.findViewById(R.id.imageView);
        saveBtn = (ImageButton) root.findViewById(R.id.save_btn);
        removeBtn = (ImageButton) root.findViewById(R.id.remove_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (AppDatabase.getInstance(getContext()).movieDAO().loadByImdbID(movie
                                .getImdbID()) != null) {
                            AppDatabase.getInstance(getContext())
                                    .movieDAO()
                                    .delete(movie);
                        }
                        AppDatabase.getInstance(getContext())
                                .movieDAO()
                                .insert(movie);

                    }
                }).start();

                Toast.makeText(getContext(),"Movie added",Toast.LENGTH_SHORT).show();

            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        AppDatabase.getInstance(getContext())
                                .movieDAO()
                                .delete(movie);
                    }
                }).start();

                Toast.makeText(getContext(),"Movie removed",Toast.LENGTH_SHORT).show();

            }
        });

        // api
        String rawJSON = getAPIByIMDBID();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        movie = gson.fromJson(rawJSON, Movie.class);

        txtTitle.setText(movie.getTitle());
        txtYear.setText(movie.getYear());
        txtReleased.setText(movie.getReleased());
        txtRuntime.setText(movie.getRuntime());
        txtImdbRating.setText(String.valueOf(movie.getImdbRating()));
        txtVotes.setText(movie.getImdbVotes());
        txtDirector.setText(movie.getDirector());
        txtWriter.setText(movie.getWriter());
        txtActors.setText(movie.getActors());
        txtCountry.setText(movie.getCountry());
        txtPlot.setText(movie.getPlot());
        txtLanguage.setText(movie.getLanguage());
        txtType.setText(movie.getType());
        new DownloadImageTask(imageView).execute(movie.getPoster());

        return root;
    }

    private String getAPIByIMDBID() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        URL url = null;
        try {
            url = new URL("https://www.omdbapi.com/?i=" + currentID +
                    "&apikey=" + Authorization.AUTH_TOKEN);
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
        return rawJSON;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            FragmentManager manager = getFragmentManager();
            manager.popBackStack();
            manager.executePendingTransactions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
