package elements;

/**
 * Created by daria on 29.10.14.
 */
public class Article {
    public String title;
    public String url;
    public String author;

    public Article(String title, String url, String author) {
        this.title = title;
        this.url = url;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }
}
