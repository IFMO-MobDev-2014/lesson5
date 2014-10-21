package com.lyamkin.rss;

import java.util.List;

interface FeedParsed {
    void onFeedParsed(List<Article> articleList);
}
