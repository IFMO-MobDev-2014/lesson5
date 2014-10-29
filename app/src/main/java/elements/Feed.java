package elements;

import java.util.ArrayList;

/**
 * Created by daria on 29.10.14.
 */
public class Feed {
    public String title;
    public ArrayList<Article> articles;

    public Feed() {
        this.title = "Stack Overflow Feed";
    }

    public int totalArticles() {
        return this.articles.size();
    }

    public Article getArticle(int position) {
        if (position >= 0 && position < this.articles.size()) {
            return this.articles.get(position);
        }
        return null;
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
