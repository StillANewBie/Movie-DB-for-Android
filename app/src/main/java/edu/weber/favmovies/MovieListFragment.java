package edu.weber.favmovies;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.weber.favmovies.db.Movie;

public class MovieListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter adapter;
    private View root;
    private MovieRecyclerViewAdapter.OnRecyclerViewAdapterListener mCallback;

    public MovieListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MovieListFragment newInstance(int columnCount) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
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
        root = inflater.inflate(R.layout.fragment_movie_list, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.rvMovieList);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        Context context = getContext();
        adapter = new MovieRecyclerViewAdapter(new ArrayList<Movie>(), mCallback);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);

        ViewModelProviders.of(this)
                .get(AllFavMovieViewModel.class)
                .getMovieList(context)
                .observe(this, new Observer<List<Movie>>() {

                    @Override
                    public void onChanged(@Nullable List<Movie> movies) {
                        if (movies != null) {
                            adapter.addItem(movies);
                        }
                    }
                });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListFragmentInteractionListener) {
            mCallback = (MovieRecyclerViewAdapter.OnRecyclerViewAdapterListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnRecyclerViewAdapterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
    }
}
