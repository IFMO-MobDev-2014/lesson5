package ru.ifmo.md.lesson5;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsDownloadTask extends AsyncTask<String, Void, List<NewsItem> > {
    private ProgressDialog pd;
    private Context context;
    private ListView lv;

    protected NewsDownloadTask(Context context, ListView lv) {
        this.context = context;
        this.lv = lv;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setMessage(context.getString(R.string.DOWNLOADING_NEWS));
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    @Override
    protected List<NewsItem> doInBackground(String... params) {
        ArrayList<NewsItem> news = new ArrayList<NewsItem>();
        try {
            XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
            InputStream stream = new URL(params[0]).openStream();
            xpp.setInput(stream, null);

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("item")) {
                    String title = "";
                    String description = "";
                    String date = "";
                    String url = "";
                    xpp.next();
                    while (!(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals("item"))) {
                        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("title")) {
                            xpp.next();
                            title = xpp.getText();
                        }
                        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("description")) {
                            xpp.next();
                            description = xpp.getText();
                        }
                        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("pubDate")) {
                            xpp.next();
                            date = xpp.getText();
                        }
                        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("link")) {
                            xpp.next();
                            url = xpp.getText();
                        }
                        xpp.next();
                    }
                    news.add(new NewsItem(title, description, date, url));
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return news;
        } catch (IOException e) {
            e.printStackTrace();
            return news;
        }
        return news;
    }

    @Override
    protected void onPostExecute(List<NewsItem> news) {
        NewsListAdapter adapter = new NewsListAdapter(news);
        lv.setAdapter(adapter);
        if (news.isEmpty()) {
            news.add(new NewsItem(context.getString(R.string.CANT_FIND_NEWS), "", "", ""));
        }
        pd.dismiss();
    }
}
