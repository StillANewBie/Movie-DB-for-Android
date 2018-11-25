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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import edu.weber.favmovies.api.APIHelper;
import edu.weber.favmovies.api.Authorization;
import edu.weber.favmovies.db.Movie;

public class SearchListFragment extends DialogFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter adapter;
    private View root;
    private MovieRecyclerViewAdapter.OnRecyclerViewAdapterListener mCallback;
    String movieName, movieYear;

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
        List<Movie> movies = new ArrayList<>();

        for (JsonElement jo : body.get("Search").getAsJsonArray()) {
            movies.add(gson.fromJson(jo, Movie.class));
        }

        adapter.addItem(movies);

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
