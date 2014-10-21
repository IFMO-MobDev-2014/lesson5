package ru.itmo.delf.rssreader;



import java.util.Date;

/**
 * Created by delf on 21.10.14.
 */
public class RssItem {

    private String title;
    private String description;
    private Date publicateDateTime;
    private String link;

    public RssItem(String title, String description, Date pubDate, String link) {
        this.title = title;
        this.description = description;
        this.publicateDateTime = pubDate;
        this.link = link;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getLink()
    {
        return this.link;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Date getPublicateDateTime()
    {
        return this.publicateDateTime;
    }


}