package com.example.android.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Loader para solicitar e receber os dados dos artigos da API do Guardian.
 */
public class ArticleLoader extends AsyncTaskLoader<ArrayList<Article>> {

    private static final String LOG_TAG = ArticleLoader.class.getName();

    private String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Article> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        ArrayList<Article> articles = QueryUtils.fetchArticleData(mUrl);

        return articles;
    }
}

