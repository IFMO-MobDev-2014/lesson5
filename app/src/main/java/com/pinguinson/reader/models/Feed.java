package com.pinguinson.reader.models;

import java.util.ArrayList;

/**
 * Created by pinguinson on 17.10.2014.
 */

public class Feed {
    public String title;
    public ArrayList<Article> articles;

    public Feed() {
        this.title = "Stack Overflow feed";
    }

    public int totalArticles() {
        return this.articles.size();
    }

    public Article getArticle(int index) {
        if (index >= articles.size() || index < 0) {
            return null;
        }
        return this.articles.get(index);
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
