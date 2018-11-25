package edu.weber.favmovies;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.weber.favmovies.api.APIHelper;
import edu.weber.favmovies.db.Movie;

import java.util.ArrayList;
import java.util.List;


public class SearchDialogFragment extends DialogFragment {

    private View root;
    public interface OnSearchDialogFragmentComplete {
        void doSearch(String name, String year);
    }
    private OnSearchDialogFragmentComplete mCallback;

    public SearchDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search_dialog, container, false);

        final TextInputLayout edtName = (TextInputLayout) root.findViewById(R.id.search_name_edt);
        final TextInputLayout edtYear = (TextInputLayout) root.findViewById(R.id.search_year_edt);
        Button btnCancel = (Button) root.findViewById(R.id.dialog_search_cancel);
        Button btnSearch = (Button) root.findViewById(R.id.dialog_search_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieName = "";
                String movieYear = "";
                List<Movie> movies = new ArrayList<>();

                if (edtName.getEditText().getText() != null) {
                    movieName = edtName.getEditText().getText().toString();
                } else {
                    Toast.makeText(getContext(), R.string.warning_no_blank, Toast
                            .LENGTH_LONG).show();
                }

                if (edtYear.getEditText().getText() != null) {
                    movieYear = edtYear.getEditText().getText().toString();
                } else {
                    Toast.makeText(getContext(), R.string.warning_no_blank, Toast
                            .LENGTH_LONG).show();
                }

                mCallback.doSearch(movieName, movieYear);

                dismiss();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        movies = APIHelper.getSearchResults(movieName, movieYear);
//
//                        System.out.println(movies);
//                    }
//                }).start();


            }
        });

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (SearchDialogFragment.OnSearchDialogFragmentComplete) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement" +
                    " OnSearchDialogFragmentComplete.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
