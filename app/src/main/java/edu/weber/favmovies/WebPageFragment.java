package edu.weber.favmovies;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebPageFragment extends DialogFragment {

    private View root;

    public WebPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_web_page, container, false);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.web_toolbar);
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
        String baseUrl = "https://www.imdb.com/title/";
        String imdbID = bundle.getString("imdbID");

        WebView webView = (WebView) root.findViewById(R.id.content_wv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(baseUrl + imdbID);

        return root;
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
