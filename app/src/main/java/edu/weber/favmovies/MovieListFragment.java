package edu.weber.favmovies;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import edu.weber.favmovies.db.AppDatabase;
import edu.weber.favmovies.db.Movie;

public class MovieListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter adapter;
    private View root;
    private MovieRecyclerViewAdapter.OnRecyclerViewAdapterListener mCallback;
    private MaterialSearchView searchView;
    private AllFavMovieViewModel allFavMovieViewModel;
    List<Movie> listedMovies = new ArrayList<>();

    public MovieListFragment() {
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


        Toolbar toolbar = (Toolbar) root.findViewById(R.id.fav_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.search_results);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        setHasOptionsMenu(true);

        searchView = (MaterialSearchView) root.findViewById(R.id.search_search_view);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT
                | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                final int position = viewHolder.getAdapterPosition();

                if (swipeDir == 4 || swipeDir == 8) {
                    Toast.makeText(getContext(), R.string.movie_deleted, Toast.LENGTH_SHORT)
                            .show();
                    adapter.notifyItemRemoved(position);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            AppDatabase.getInstance(getContext())
                                    .movieDAO()
                                    .delete(mCallback.swipeToDelete().get(position));
                        }
                    }).start();
                } else {
                    Movie movie = mCallback.swipeToDelete().get(position);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Movie movie = mCallback.swipeToDelete().get(position);

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


                    Toast.makeText(getContext(), movie.getTitle() + getString(R.string.moved_to_top), Toast
                            .LENGTH_SHORT)
                            .show();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);



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
                            listedMovies = movies;
                        }
                    }
                });
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
                for (Movie el: listedMovies) {
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


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
    }
}
