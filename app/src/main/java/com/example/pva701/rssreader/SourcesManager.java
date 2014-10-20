package com.example.pva701.rssreader;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pva701 on 14.10.14.
 */
public class SourcesManager {
    private static SourcesManager instance;
    private RSSDatabaseHelper helper;
    private ArrayList <Source> sources;

    private SourcesManager(Context context) {
        helper = new RSSDatabaseHelper(context.getApplicationContext());
        RSSDatabaseHelper.SourceCursor cursor = helper.querySources();
        sources = new ArrayList<Source>();
        while (cursor.moveToNext())
            sources.add(cursor.getSource());
    }

    public static class Source {
        private int id;
        private String url;
        private String name;
        private Date lastUpdate;

        public Source(String name, String url, Date lastUpdate) {
            this.name = name;
            this.url = url;
            this.lastUpdate = lastUpdate;
        }

        public Source(long id, String name, String url, Date lastUpdate) {
            this.id = (int)id;
            this.name = name;
            this.url = url;
            this.lastUpdate = lastUpdate;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        public Date getLastUpdate() {
            return lastUpdate;
        }

        public void setId(long id) {
            this.id = (int)id;
        }

        public int getId() {
            return id;
        }
    }

    public static SourcesManager getInstance(Context context) {
        if (instance == null)
            instance = new SourcesManager(context.getApplicationContext());
        return instance;
    }

    public ArrayList <Source> getSources() {
        if (sources == null)
            sources = new ArrayList<Source>();
        return sources;
    }

    public void resume() {
        for (int i = 0; i < sources.size(); ++i)
            if (sources.get(i).getId() == 0)///is changed
                sources.get(i).setId(helper.insertSource(sources.get(i)));
            else
                helper.updateSource(sources.get(i));
    }
}
