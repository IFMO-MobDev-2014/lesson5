package ru.ifmo.md.lesson5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.List;

public class NewsDownloadTask extends AsyncTask<String, Void, List<NewsItem> > {
    private Context context;
    ProgressDialog pd;

    protected NewsDownloadTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setMessage("News Downloading...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    @Override
    protected List<NewsItem> doInBackground(String... params) {
        String url = params[0];
        ArrayList<NewsItem> news = new ArrayList<NewsItem>();
        XmlPullParser xpp;
        try {
            xpp = XmlPullParserFactory.newInstance().newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return news;
        }
                
        return news;
    }

    @Override
    protected void onPostExecute(List<NewsItem> news) {
        pd.dismiss();
        if (news.isEmpty()) {
            news.add(new NewsItem("Check your internet", "and try again", "please", "http://www.google.com/"));
        }
    }
}
