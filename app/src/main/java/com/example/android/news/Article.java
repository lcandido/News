package com.example.android.news;

import java.util.Date;

/**
 * An article of the News app. Each article includes title, section name, author, publication date
 * and URL.
 */
public class Article {

    private String mTitle;
    private String mSectionName;
    private String mAuthorName;
    private Date mPublicationDate;
    private String mUrl;

    public Article(String title, String sectionName, String authorName, Date publicationDate,
                   String url) {
        this.mTitle = title;
        this.mSectionName = sectionName;
        this.mAuthorName = authorName;
        this.mPublicationDate = publicationDate;
        this.mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public Date getPublicationDate() {
        return mPublicationDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
