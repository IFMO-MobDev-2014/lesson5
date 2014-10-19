package com.wibk.rss;

import org.xml.sax.InputSource;

import java.util.List;

public class RssFeed {
    private final List<RssItem> rssItemList;

    public RssFeed(InputSource is) {
        rssItemList = SimpleSax.parseInputSource(is);
    }

    public List<RssItem> getRssItemList() {
        return rssItemList;
    }
}
