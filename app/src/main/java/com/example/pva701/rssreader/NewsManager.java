package com.example.pva701.rssreader;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pva701 on 14.10.14.
 */
public class NewsManager {
    private static NewsManager instance;
    private volatile RSSDatabaseHelper helper;
    private volatile ArrayList <News> allNewses;
    private volatile Set<Integer> savedIds;

    public static class News {
        private int id;
        private String title;
        private String link;
        private String description;
        private Date pubDate;
        private ArrayList<String> category;
        private boolean read;
        private int sourceId;
        public News() {
        }

        public void setId(long id) {
            this.id = (int)id;
        }

        public int getId() {
            return id;
        }

        public boolean equals(News other) {
            return pubDate.equals(other.getPubDate()) && title.equals(other.getTitle());
        }

        public void setSourceId(int id) {
            sourceId = id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Date getPubDate() {
            return pubDate;
        }

        public String getLink() {
            return link;
        }

        public ArrayList<String> getCategory() {
            if (category == null)
                category = new ArrayList<String>();
            return category;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setPubDate(Date pubDate) {
            this.pubDate = pubDate;
        }

        public void addCategory(String cat) {
            if (category == null)
                category = new ArrayList<String>();
            category.add(cat);
        }

        public void setRead(boolean read) {
            this.read = read;
        }

        public String toStringCategory() {
            if (category == null)
                category = new ArrayList<String>();
            String ret = "";
            for (int i = 0; i < category.size(); ++i)
                if (i != category.size() - 1)
                    ret += category.get(i) + ", ";
                else
                    ret += category.get(i);
            return ret;
        }

        public boolean isRead() {
            return read;
        }

        public int getSourceId() {
            return sourceId;
        }

        @Override
        public String toString() {
            return "title: " + title + "; pubDate = " + pubDate.toString() + "; link = " + link;
        }
    }

    public NewsManager(Context context) {
        helper = new RSSDatabaseHelper(context.getApplicationContext());
        allNewses = new ArrayList<News>();
        RSSDatabaseHelper.NewsCursor cursor = helper.queryNewses();
        savedIds = new HashSet<Integer>();
        while (cursor.moveToNext()) {
            allNewses.add(cursor.getNews());
            savedIds.add(allNewses.get(allNewses.size() - 1).getId());
        }
    }

    public static NewsManager getInstance(Context context) {
        if (instance == null)
            instance = new NewsManager(context.getApplicationContext());
        return instance;
    }

    private void sortGreat(ArrayList <News> srt) {
        Collections.sort(srt, new Comparator<News>() {
            @Override
            public int compare(News news, News news2) {
                if (news.getPubDate().getTime() > news2.getPubDate().getTime())
                    return -1;
                if (news.getPubDate().equals(news2.getPubDate()))
                    return 0;
                return 1;

            }
        });
    }

    public void mergeNews(ArrayList <News> newses, int sourceId) {
        if (newses.size() == 0)
            return;
        for (int i = 0; i < newses.size(); ++i)
            newses.get(i).setSourceId(sourceId);
        sortGreat(newses);
        for (int j = 0; j < allNewses.size(); ++j) {
            News cur = allNewses.get(j);
            if (cur.getSourceId() != sourceId)
                continue;

            boolean found = false;
            for (int i = 0; i < newses.size(); ++i)
                if (cur.equals(newses.get(i))) {
                    newses.remove(i);
                    found = true;
                    break;
                }
            if (!found) {
                allNewses.remove(j);
                --j;
            }
        }

        for (int i = 0; i < newses.size(); ++i)
            allNewses.add(newses.get(i));
    }

    public int getCountUnread() {
        int unread = 0;
        for (int i = 0; i < allNewses.size(); ++i)
            if (!allNewses.get(i).isRead())
                ++unread;
        return unread;
    }

    public int getCountUnread(int id) {
        int unread = 0;
        for (int i = 0; i < allNewses.size(); ++i)
            if (!allNewses.get(i).isRead() && allNewses.get(i).getId() == id)
                ++unread;
        return unread;
    }

    public ArrayList <News> getNewses(int sourceId) {
        ArrayList <News> ret = new ArrayList<News>();
        for (int i = 0; i < allNewses.size(); ++i)
            if (sourceId == allNewses.get(i).getSourceId())
                ret.add(allNewses.get(i));
        sortGreat(ret);
        return ret;
    }

    public void resume() {
        Log.i("NewsManager", "in resume with size = " + allNewses.size());
        if (allNewses.size() == 0)
            return;

        for (int i = 0; i < allNewses.size(); ++i)
            if (allNewses.get(i).getId() == 0) {
                Log.i("NewsManager", "not added");
                allNewses.get(i).setId(helper.insertNews(allNewses.get(i)));
            } else {
                helper.updateNews(allNewses.get(i));
                if (savedIds.contains(allNewses.get(i).getId()))
                    savedIds.remove(allNewses.get(i).getId());
            }

        for (Integer e : savedIds)
            helper.deleteNews(e);
    }
}
