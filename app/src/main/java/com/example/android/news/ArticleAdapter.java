package com.example.android.news;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Context context = getContext();
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.article_list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        if (currentArticle != null) {

            String sectionName = currentArticle.getSectionName();
            String title = currentArticle.getTitle();

            String sourceString = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>" + sectionName + "</b> / " + title;
            TextView titleView = (TextView) listItemView.findViewById(R.id.title);
            titleView.setText(Html.fromHtml(sourceString));

            String authorName = currentArticle.getAuthorName();
            TextView authorView = (TextView) listItemView.findViewById(R.id.author);
            authorView.setText(authorName);

            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT,
                    context.getResources().getConfiguration().locale);
            String publicationDate = df.format(currentArticle.getPublicationDate());
            TextView publicationDateView = (TextView) listItemView.findViewById(R.id.publication_date);
            publicationDateView.setText(publicationDate);
        }

        return listItemView;
    }
}
