package com.example.android.news;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>> {

    private static final String NEWS_API_URL = "http://content.guardianapis.com/search";

    private static final int NEWS_LOADER_ID = 1;

    /*** Adaptador para a lista de artigos */
    private ArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Encontra a referência para o ListView no layout
        ListView articleListView = (ListView) findViewById(R.id.list);
        articleListView.setEmptyView(findViewById(R.id.empty_list_item));

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        articleListView.setAdapter(mAdapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Article currentArticle = mAdapter.getItem(position);
                String url = currentArticle.getUrl();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        if (hasInternetConnection()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            TextView emptyView = (TextView) findViewById(R.id.empty_list_item);
            emptyView.setText(R.string.no_internet_connection);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.marker_progress);
            progressBar.setVisibility(View.GONE);
        }


    }

    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(NEWS_API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", "debates");
        uriBuilder.appendQueryParameter("api-key", "test");

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> articles) {

        mAdapter.clear();

        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        } else {
            TextView emptyView = (TextView) findViewById(R.id.empty_list_item);
            emptyView.setText(R.string.empty_list);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.marker_progress);
            progressBar.setVisibility(View.GONE);

        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Article>> loader) {

        mAdapter.clear();

    }

    private boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
