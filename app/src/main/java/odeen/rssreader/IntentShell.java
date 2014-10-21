package odeen.rssreader;

import android.content.Intent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Женя on 18.10.2014.
 */
public class IntentShell {
    static void put(Intent intent, News news) {
        intent.putExtra(Constants.BROADCAST_NEWS_DATE, news.getPubDate().getTime());
        intent.putExtra(Constants.BROADCAST_NEWS_ID, news.getId().toString());
        intent.putExtra(Constants.BROADCAST_NEWS_URL, news.getURL());
        intent.putExtra(Constants.BROADCAST_NEWS_TITLE, news.getTitle());
        intent.putExtra(Constants.BROADCAST_NEWS_DESCRIPTION, news.getDescription());
        intent.putExtra(Constants.BROADCAST_NEWS_IMAGE_LINK, news.getImageLink());
    }

    static News get(Intent intent) {
        News news = new News();
        news.setPubDate(new Date((Long)intent.getSerializableExtra(Constants.BROADCAST_NEWS_DATE)));
        news.setTitle((String) intent.getSerializableExtra(Constants.BROADCAST_NEWS_TITLE));
        news.setId(UUID.fromString((String) intent.getSerializableExtra(Constants.BROADCAST_NEWS_ID)));
        news.setURL((String)intent.getSerializableExtra(Constants.BROADCAST_NEWS_URL));
        news.setDescription((String)intent.getSerializableExtra(Constants.BROADCAST_NEWS_DESCRIPTION));
        news.setImageLink((String)intent.getSerializableExtra(Constants.BROADCAST_NEWS_IMAGE_LINK));
        return news;
    }

}
