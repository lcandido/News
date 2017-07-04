package com.example.android.news;

import java.util.Date;

/**
 * Um artigo do app News. Cada artigo inclui o título, nome da seção, nome do autor, data de publicação e URL.
 */
public class Article {

    private String mTitle;          // Titulo do artigo
    private String mSectionName;    // Nome da seção do artigo
    private String mAuthorName;     // Nome do autor do artigo
    private Date mPublicationDate;  // Data de publicaçao do artigo
    private String mUrl;            // URL do artigo

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
